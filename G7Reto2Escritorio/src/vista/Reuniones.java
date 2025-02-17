package vista;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Reuniones extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton volverBoton;
	
	public Reuniones() {
		setBounds(0, 0, 700, 380);
		setLayout(null);
		
		volverBoton = new JButton("Volver");
		volverBoton.setBounds(10, 11, 87, 23);
		add(volverBoton);
		
		JLabel lblNewLabel = new JLabel("REUNIONES");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 3, 450, 14);
		add(lblNewLabel);
	}

	public JButton getVolverBoton() {
		return this.volverBoton;
	}
}
