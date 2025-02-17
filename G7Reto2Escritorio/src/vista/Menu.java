package vista;

import javax.swing.JPanel;
import javax.swing.JButton;

public class Menu extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton horarioBoton;
	private JButton otroBoton;
	private JButton reunionesBoton;
	private JButton salirBoton;
	
	public Menu() {
		setBounds(0, 0, 700, 380);
		setLayout(null);
		
		horarioBoton = new JButton("Consultar horario");
		horarioBoton.setBounds(257, 133, 182, 23);
		add(horarioBoton);
		
		otroBoton = new JButton("Consultar otros horarios");
		otroBoton.setBounds(257, 180, 182, 23);
		add(otroBoton);
		
		reunionesBoton = new JButton("Ver reuiniones");
		reunionesBoton.setBounds(257, 226, 182, 23);
		add(reunionesBoton);
		
		salirBoton = new JButton("Salir");
		salirBoton.setBounds(10, 11, 64, 23);
		add(salirBoton);
	}

	public JButton getHorarioBoton() {
		return this.horarioBoton;
	}

	public JButton getOtroBoton() {
		return this.otroBoton;
	}

	public JButton getReunionesBoton() {
		return this.reunionesBoton;
	}

	public JButton getSalirBoton() {
		return this.salirBoton;
	}
}
