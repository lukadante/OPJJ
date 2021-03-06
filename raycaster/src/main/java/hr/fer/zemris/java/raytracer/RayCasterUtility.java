package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;

/**
 * Utility class for ray casters which calculates a color of a pixel.
 * 
 * @author Luka Skugor
 *
 */
public class RayCasterUtility {

	/**
	 * Traces the color for given eye to view ray.
	 * 
	 * @param scene
	 *            scene in which tracing is calculated
	 * @param ray
	 *            eye to view ray
	 * @param rgb
	 *            array where results will be stored
	 */
	static void tracer(Scene scene, Ray ray, final short[] rgb) {
		RayIntersection intersection = closestIntersectionInScene(scene, ray);
		System.arraycopy(determineColorFor(intersection, scene, ray), 0, rgb,
				0, 3);
	}

	/**
	 * Determines color for an intersection in a scene.
	 * 
	 * @param intersection
	 *            intersection of eye to view ray and the closest object to the
	 *            eye
	 * @param scene
	 *            scene in which tracing is calculated
	 * @param eyeRay
	 *            eye to view ray
	 * @return color of the given pixel in an array <code>short[3]</code> where
	 *         elements represents colors as follows: red, green, blue
	 */
	private static short[] determineColorFor(RayIntersection intersection,
			Scene scene, Ray eyeRay) {

		if (intersection == null) {
			return new short[] { 0, 0, 0 };
		}

		short[] rgb = { 15, 15, 15 };
		for (LightSource light : scene.getLights()) {

			Ray lightRay = Ray.fromPoints(light.getPoint(),
					intersection.getPoint());
			RayIntersection lightIntersection = closestIntersectionInScene(
					scene, lightRay);

			if (lightIntersection != null
					&& lightIntersection.getPoint().sub(light.getPoint())
							.norm() + 1e-3 < intersection.getPoint()
							.sub(light.getPoint()).norm()
					&& intersection.isOuter()) {
				continue;
			}

			int r = light.getR();
			int g = light.getG();
			int b = light.getB();

			Point3D toLight = lightRay.direction.negate();
			Point3D normal = intersection.getNormal();

			double cosFi = Math.max(toLight.scalarProduct(normal), 0);

			Point3D reflected = normal.scalarMultiply(
					toLight.scalarProduct(normal) * 2).modifySub(toLight);
			double cosAlpha = Math.max(
					reflected.scalarProduct(eyeRay.direction.negate()), 0);
			double cosNAlpha = (cosAlpha != 0) ? Math.pow(cosAlpha,
					intersection.getKrn()) : 0;

			rgb[0] += (short) ((intersection.getKdr() * cosFi + intersection
					.getKrr() * cosNAlpha) * r);
			rgb[1] += (short) ((intersection.getKdg() * cosFi + intersection
					.getKrg() * cosNAlpha) * g);
			rgb[2] += (short) ((intersection.getKdb() * cosFi + intersection
					.getKrb() * cosNAlpha) * b);
		}
		return rgb;
	}

	/**
	 * Finds ray intersection for closest element the ray intersects.
	 * @param scene scene in which ray is instanced
	 * @param ray ray for which closest intersection is searched
	 * @return closest intersection for given ray
	 */
	private static RayIntersection closestIntersectionInScene(Scene scene,
			Ray ray) {
		RayIntersection intersection = null;
		for (GraphicalObject object : scene.getObjects()) {

			RayIntersection objectIntersection = object
					.findClosestRayIntersection(ray);

			if (objectIntersection != null
					&& (intersection == null || objectIntersection
							.getDistance() < intersection.getDistance())) {
				intersection = objectIntersection;
			}
		}
		return intersection;
	}

}
