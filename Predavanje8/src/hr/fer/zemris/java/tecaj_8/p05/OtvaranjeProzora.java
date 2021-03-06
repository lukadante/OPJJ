package hr.fer.zemris.java.tecaj_8.p05;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class OtvaranjeProzora extends JFrame {
	
	public OtvaranjeProzora() {
		setLocation(20, 50);
		setSize(500, 300);
		setTitle("Program tvog kompjutera");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		initGUI();
	}

	private void initGUI() {
		getContentPane().setLayout(null);
		
		getContentPane().setBackground(Color.ORANGE);
		MojaKomponenta komponenta = new MojaKomponenta();
		komponenta.setLocation(20, 50);
		komponenta.setSize(200, 100);
		komponenta.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
		komponenta.setOpaque(true);
		komponenta.setBackground(Color.MAGENTA);
		komponenta.setForeground(Color.GREEN);
		
		getContentPane().add(komponenta);
	}
	
	private static class MojaKomponenta extends JComponent {
		
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g.create();
			
			Dimension size = getSize();
			Insets ins = getInsets();
			
			Rectangle rect = new Rectangle(
				ins.left,
				ins.top,
				size.width - ins.left - ins.right,
				size.height - ins.top - ins.bottom
			);
			
			if (isOpaque()) {
				g2d.setColor(getBackground());
				g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
			}
			
			g2d.setColor(getForeground());
			g2d.setStroke(new BasicStroke(7));
			g2d.drawOval(rect.x + 3, rect.y + 3, rect.width - 8, rect.height - 8);
			
			g2d.dispose();
		}
	}

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new OtvaranjeProzora();
			frame.setVisible(true);			
		});
		
	}
	
}
