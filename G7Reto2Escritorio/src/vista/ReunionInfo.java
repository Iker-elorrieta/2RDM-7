package vista;

import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import controlador.Metodos;
import java.awt.Font;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReunionInfo extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton volverBoton;
	private JButton aceptarBoton;
	private JButton rechazarBoton;
	
	private JLabel tituloLabel;
	private JLabel estadoLabel;
	private JLabel personasLabel;
	private JLabel lugarLabel;
	private JLabel asuntoLabel;
	private JLabel fechaLabel;
	
	private int id;
	
	Metodos metodos = new Metodos();
	
	public ReunionInfo() {
		setBounds(0, 0, 700, 380);
		setLayout(null);
		
		aceptarBoton = new JButton("Aceptar");
		aceptarBoton.setBounds(264, 346, 87, 23);
		add(aceptarBoton);
		
		rechazarBoton = new JButton("Rechar");
		rechazarBoton.setBounds(361, 346, 87, 23);
		add(rechazarBoton);
		
		volverBoton = new JButton("Volver");
		volverBoton.setBounds(10, 11, 87, 23);
		add(volverBoton);
		
		JLabel lblNewLabel = new JLabel("REUNION");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 700, 14);
		add(lblNewLabel);
		
		tituloLabel = new JLabel("Reunion?");
		tituloLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tituloLabel.setBounds(0, 43, 700, 29);
		add(tituloLabel);
		
		estadoLabel = new JLabel("estado?");
		estadoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		estadoLabel.setBounds(0, 103, 700, 14);
		add(estadoLabel);
		
		personasLabel = new JLabel("alumno? ha solicitado una reunion\r\ncon el profesor profesor?");
		personasLabel.setHorizontalAlignment(SwingConstants.CENTER);
		personasLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		personasLabel.setBounds(0, 203, 700, 56);
		add(personasLabel);
		
		lugarLabel = new JLabel("reunion en centro? en el aula aula?");
		lugarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lugarLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lugarLabel.setBounds(0, 284, 700, 34);
		add(lugarLabel);
		
		asuntoLabel = new JLabel("asunto?");
		asuntoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		asuntoLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		asuntoLabel.setBounds(0, 73, 700, 29);
		add(asuntoLabel);
		
		fechaLabel = new JLabel("fecha de reunion: fecha?");
		fechaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fechaLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		fechaLabel.setBounds(0, 145, 700, 34);
		add(fechaLabel);
	}

	public JButton getVolverBoton() {
		return this.volverBoton;
	}
	
	public JButton getRechazarBoton() {
		return this.rechazarBoton;
	}
	
	public JButton getAceptarBoton() {
		return this.aceptarBoton;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setReunion(Object[] reunion) {
		tituloLabel.setText((String) reunion[1]);
		estadoLabel.setText(metodos.primeraMayus((String) reunion[0]));
		asuntoLabel.setText((String) reunion[2]);
		personasLabel.setText(String.format("<html>%s ha solicitado una reunion</br>con el profesor %s", metodos.primeraMayus((String) reunion[6]), metodos.primeraMayus((String) reunion[7])));
		lugarLabel.setText(String.format("Reunion en %s en el aula %s", reunion[4], reunion[5]));
		fechaLabel.setText(String.format("Fecha de reunion: %s", (String) reunion[3]));
		id = Integer.parseInt((String) reunion[8]);
	}
}
