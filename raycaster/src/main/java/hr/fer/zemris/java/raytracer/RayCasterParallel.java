package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * This program represents a ray caster which data calculation is parallel.
 * 
 * @author Luka Skugor
 *
 */
public class RayCasterParallel {
	/**
	 * Called on program start
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0),
				new Point3D(0, 0, 0), new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Gets a ray tracer producer, of this ray caster, which calculated data
	 * parallely.
	 * 
	 * @return ray tracer producer
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			
			/**
			 * Pool which executed DataCalculators
			 */
			ForkJoinPool pool = new ForkJoinPool();

			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp,
					double horizontal, double vertical, int width, int height,
					long requestNo, IRayTracerResultObserver observer) {

				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D og = view.sub(eye).normalize();
				viewUp.modifyNormalize();

				Point3D yAxis = viewUp.sub(
						og.scalarMultiply(og.scalarProduct(viewUp)))
						.normalize();
				Point3D xAxis = og.vectorProduct(yAxis).normalize();

				Point3D screenCorner = view.sub(
						xAxis.scalarMultiply(horizontal / 2)).add(
						yAxis.scalarMultiply(vertical / 2));

				Scene scene = RayTracerViewer.createPredefinedScene();

				/**
				 * This class is a multi-thread recursive implementation for
				 * calculating color of each pixel for ray caster. It calculates
				 * pixels of range of y-coordinates when it reaches certain
				 * depth of recursion.
				 * 
				 * @author Luka Skugor
				 *
				 */
				class ColorCalculator extends RecursiveAction {

					/**
					 * Default serial version UID.
					 */
					private static final long serialVersionUID = 1L;
					/**
					 * Y-coordinate from which data is calculated.
					 */
					private int yMin;
					/**
					 * Y-coordinate to which data is calculated.
					 */
					private int yMax;

					/**
					 * Creates a new ColorCalculator which calculates colors for
					 * given range of y-coordinates.
					 * 
					 * @param yMin
					 *            y-coordinate from which data is calculated
					 * @param yMax
					 *            y-coordinate to which data is calculated
					 */
					public ColorCalculator(int yMin, int yMax) {
						super();
						this.yMin = yMin;
						this.yMax = yMax;
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see java.util.concurrent.RecursiveAction#compute()
					 */
					@Override
					protected void compute() {
						if (yMax - yMin > height
								/ (8 * Runtime.getRuntime()
										.availableProcessors())) {

							int halvedLines = (yMax - yMin) / 2;

							ColorCalculator c1 = new ColorCalculator(yMin, yMin
									+ halvedLines);
							ColorCalculator c2 = new ColorCalculator(yMin
									+ halvedLines, yMax);
							invokeAll(c1, c2);
						} else {
							calc();
						}
					}

					/**
					 * Calculates colors for given range of y-coordinates and
					 * stores them in adequate arrays (i.e. red[], green[],
					 * blue[]).
					 */
					private void calc() {
						short[] rgb = new short[3];
						int offset = yMin * width;
						for (int y = yMin; y < yMax; y++) {
							for (int x = 0; x < width; x++) {

								Point3D screenPoint = screenCorner.add(
										xAxis.scalarMultiply(horizontal * x
												/ (double) (width - 1))).sub(
										yAxis.scalarMultiply(vertical * y
												/ (double) (height - 1)));
								Ray ray = Ray.fromPoints(eye, screenPoint);

								RayCasterUtility.tracer(scene, ray, rgb);

								red[offset] = rgb[0] > 255 ? 255 : rgb[0];
								green[offset] = rgb[1] > 255 ? 255 : rgb[1];
								blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

								offset++;
							}
						}
					}

				}

				pool.invoke(new ColorCalculator(0, height));

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}
}
