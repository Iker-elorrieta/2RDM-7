package vista;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Horario extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton volverBoton;
	
	public Horario() {
		setBounds(0, 0, 520, 380);
		setLayout(null);
		
		volverBoton = new JButton("Volver");
		volverBoton.setBounds(10, 11, 87, 23);
		add(volverBoton);
		
		JLabel lblNewLabel = new JLabel("HORARIO");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 3, 450, 14);
		add(lblNewLabel);
	}

	public JButton getVolverBoton() {
		return this.volverBoton;
	}
}
