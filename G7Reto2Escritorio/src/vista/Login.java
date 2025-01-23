package vista;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.SwingConstants;

public class Login extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextField usuarioCampo;
	private JTextField claveCampo;
	private JButton loginBoton;
	private JLabel mensajeError;

	public Login() {
		setBounds(0, 0, 600, 380);
		setLayout(null);
		
		loginBoton = new JButton("Iniciar sesion");
		loginBoton.setBounds(157, 178, 135, 23);
		add(loginBoton);
		
		usuarioCampo = new JTextField();
		usuarioCampo.setBounds(85, 59, 278, 20);
		add(usuarioCampo);
		usuarioCampo.setColumns(10);
		
		
		claveCampo = new JPasswordField();
		claveCampo.setBounds(85, 119, 278, 20);
		add(claveCampo);
		claveCampo.setColumns(10);

		
		JLabel lblNewLabel = new JLabel("usuario");
		lblNewLabel.setBounds(85, 43, 278, 14);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("clave");
		lblNewLabel_1.setBounds(85, 104, 278, 14);
		add(lblNewLabel_1);
		
		mensajeError = new JLabel("");
		mensajeError.setHorizontalAlignment(SwingConstants.CENTER);
		mensajeError.setForeground(new Color(255, 0, 0));
		mensajeError.setBounds(85, 212, 278, 14);
		add(mensajeError);
	}
	
	public JButton getLoginBoton() {
		return this.loginBoton;
	}
	
	public JTextField getUsuarioCampo() {
		return this.usuarioCampo;
	}

	public JTextField getClaveCampo() {
		return this.claveCampo;
	}

	public JLabel getErrorLabel() {
		return this.mensajeError;
	}
}
