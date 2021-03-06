package hr.fer.zemris.java.tecaj_8.p12;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
		getContentPane().setLayout(new BorderLayout());
		
		JLabel labela = new JLabel("Ovo je tekst labele");
		JPanel panelSGumbima = new JPanel(new GridLayout(1, 0));
		
		JButton[] gumbi = new JButton[4];
		
		ActionListener obrada = e -> {
			JButton gumb = (JButton)e.getSource();
			labela.setText("Stisnut je " + gumb.getText());
		};
		
		for (int i = 0; i < gumbi.length; i++) {
			gumbi[i] = new JButton("Gumb " + (i+1));
			panelSGumbima.add(gumbi[i]);
			gumbi[i].addActionListener(obrada);
		}
		
		labela.setOpaque(true);
		labela.setBackground(Color.YELLOW);
		labela.setHorizontalAlignment(SwingConstants.CENTER);
		
		getContentPane().add(labela, BorderLayout.CENTER);
		getContentPane().add(panelSGumbima, BorderLayout.PAGE_END);
	}


	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new OtvaranjeProzora();
			// frame.pack();
			frame.setVisible(true);			
		});
		
	}
	
}
