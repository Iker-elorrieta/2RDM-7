package vista;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import controlador.Metodos;
import modelo.CenterCellRenderer;
import modelo.TablaNoEditable;
import java.awt.Font;

public class ReunionInfo extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton volverBoton;

	private Metodos metodos = new Metodos();
	
	public ReunionInfo() {
		setBounds(0, 0, 700, 380);
		setLayout(null);
		
		volverBoton = new JButton("Volver");
		volverBoton.setBounds(10, 11, 87, 23);
		add(volverBoton);
		
		JLabel lblNewLabel = new JLabel("REUNION");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 700, 14);
		add(lblNewLabel);
		
		JLabel tituloLabel = new JLabel("Reunion?");
		tituloLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tituloLabel.setBounds(0, 43, 700, 29);
		add(tituloLabel);
		
		JLabel estadoLabel = new JLabel("estado?");
		estadoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		estadoLabel.setBounds(0, 103, 700, 14);
		add(estadoLabel);
		
		JLabel personasLabel = new JLabel("alumno? ha solicitado una reunion\r\ncon el profesor profesor?");
		personasLabel.setHorizontalAlignment(SwingConstants.CENTER);
		personasLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		personasLabel.setBounds(0, 203, 700, 56);
		add(personasLabel);
		
		JLabel lugarLabel = new JLabel("reunion en centro? en el aula aula?");
		lugarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lugarLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lugarLabel.setBounds(0, 284, 700, 34);
		add(lugarLabel);
		
		JLabel asuntoLabel = new JLabel("asunto?");
		asuntoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		asuntoLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		asuntoLabel.setBounds(0, 73, 700, 29);
		add(asuntoLabel);
		
		JLabel fechaLabel = new JLabel("fecha de reunion: fecha?");
		fechaLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fechaLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		fechaLabel.setBounds(0, 145, 700, 34);
		add(fechaLabel);
	}

	public JButton getVolverBoton() {
		return this.volverBoton;
	}
	
	public void setReunion(Object[] reunion) {
		
	}
}
