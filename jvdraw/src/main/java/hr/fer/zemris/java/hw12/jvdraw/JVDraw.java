package hr.fer.zemris.java.hw12.jvdraw;

import hr.fer.zemris.java.hw12.jvdraw.buttons.ObjectChooserGroup;
import hr.fer.zemris.java.hw12.jvdraw.buttons.ObjectCreatorButton;
import hr.fer.zemris.java.hw12.jvdraw.colors.JColorArea;
import hr.fer.zemris.java.hw12.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw12.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw12.jvdraw.objects.EmptyCircleDrawing;
import hr.fer.zemris.java.hw12.jvdraw.objects.FullCircleDrawing;
import hr.fer.zemris.java.hw12.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw12.jvdraw.objects.LineDrawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JVDraw extends JFrame {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 1L;

	private JDrawingCanvas drawingCanvas;
	private DrawingModel drawingModel;

	private JColorArea background;
	private JColorArea foreground;

	private ObjectChooserGroup objectChooserGroup;

	public JVDraw() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 400);
		initGUI();
	}

	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());

		drawingModel = new DrawingModel();
		drawingCanvas = new JDrawingCanvas(drawingModel);
		getContentPane().add(drawingCanvas, BorderLayout.CENTER);
		initDrawingCanvas();

		JPanel topPanel = new JPanel(new FlowLayout());

		background = new JColorArea(Color.WHITE);
		initColorArea(background, "background");

		foreground = new JColorArea(Color.BLACK);
		initColorArea(foreground, "foreground");

		topPanel.add(foreground);
		topPanel.add(background);
		topPanel.add(new JButton(new AbstractAction() {

			{
				putValue(Action.NAME, "Generate circle");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				Random random = new Random();
				drawingModel.addObject(new FullCircleDrawing(300 + random
						.nextInt() % 100, 200 + random.nextInt() % 100, 20,
						background.getCurrentColor(), foreground
								.getCurrentColor()));
			}
		}));
		for (ObjectCreatorButton creator : initObjectCreators()) {
			topPanel.add(creator);
		}

		getContentPane().add(topPanel, BorderLayout.PAGE_START);
	}

	private void initDrawingCanvas() {
		drawingCanvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point start = drawingCanvas.getStartPoint();
				int ex = e.getX();
				int ey = e.getY();
				if (start != null) {
					int width = start.x - ex;
					int height = start.y - ey;
					drawingModel.addObject(objectChooserGroup
							.createSelectedObject(start.x, start.y, width,
									height, background.getCurrentColor(),
									foreground.getCurrentColor()));
					drawingCanvas.resetStartPoint();
				} else {
					drawingCanvas.setStartPoint(ex, ey);
				}
			}
		});

		drawingCanvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point start = drawingCanvas.getStartPoint();
				if (start != null) {
					int width = start.x - e.getX();
					int height = start.y - e.getY();
					drawingCanvas.paintDrawingObject(objectChooserGroup
							.createSelectedObject(start.x, start.y, width,
									height, background.getCurrentColor(),
									foreground.getCurrentColor()));
				}
			}
		});

	}

	private void initColorArea(JColorArea colorArea, String name) {
		colorArea.addColorChangeListener((src, oldC, newC) -> {
			if (newC != null && !newC.equals(oldC)) {
				drawingCanvas.setBackground(newC);
			}
		});

		colorArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Color choosen = JColorChooser.showDialog(JVDraw.this, "Choose "
						+ name + " color", colorArea.getCurrentColor());
				if (choosen != colorArea.getCurrentColor()) {
					colorArea.setColor(choosen);
					colorArea.repaint();
				}
			}
		});
	}

	private ObjectCreatorButton[] initObjectCreators() {
		ObjectCreatorButton[] buttons = new ObjectCreatorButton[3];
		// FullCircleDrawing creator
		buttons[0] = new ObjectCreatorButton() {
			@Override
			public GeometricalObject createObject(int x, int y, int width,
					int height, Color bg, Color fg) {
				return new FullCircleDrawing(x, y, (int) Math.sqrt(height
						* height + width * width),
						foreground.getCurrentColor(),
						background.getCurrentColor());
			}
		};
		buttons[0].setText("Full Circle");

		buttons[1] = new ObjectCreatorButton() {
			@Override
			public GeometricalObject createObject(int x, int y, int width,
					int height, Color bg, Color fg) {
				return new EmptyCircleDrawing(x, y, (int) Math.sqrt(height
						* height + width * width), foreground.getCurrentColor());
			}
		};
		buttons[1].setText("Empty Circle");

		buttons[2] = new ObjectCreatorButton() {
			@Override
			public GeometricalObject createObject(int x, int y, int width,
					int height, Color bg, Color fg) {
				return new LineDrawing(x, y, x - width, y - height,
						foreground.getCurrentColor());
			}
		};
		buttons[2].setText("Line");

		objectChooserGroup = new ObjectChooserGroup(buttons);
		return buttons;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ignorable) {
			}
			
			JFrame frame = new JVDraw();
			frame.pack();
			frame.setVisible(true);
		});
	}

}
