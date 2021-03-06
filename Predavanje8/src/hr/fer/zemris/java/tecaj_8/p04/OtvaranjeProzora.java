package hr.fer.zemris.java.tecaj_8.p04;


import java.awt.Color;

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
		
		MojaKomponenta komponenta = new MojaKomponenta();
		komponenta.setLocation(20, 50);
		komponenta.setSize(200, 100);
		komponenta.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
		
		getContentPane().add(komponenta);
	}
	
	private static class MojaKomponenta extends JComponent {
		
	}

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new OtvaranjeProzora();
			frame.setVisible(true);			
		});
		
	}
	
}
