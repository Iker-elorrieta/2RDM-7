package vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controlador.Metodos;
import modelo.Conexion;

public class Principal extends JFrame {
	private static final long serialVersionUID = 1L;

	public static enum enumAcciones {
		PANEL_LOGIN,
		PANEL_MENU,
		PANEL_HORARIO,
		PANEL_OTROS,
		PANEL_REUNIONES
	}

	private Metodos metodos;
	
	private JPanel panelContenedor;
	private Login login;
	private Menu menu;
	private Horario horario;
	private Otros otros;
	private Reuniones reuniones;

	private ObjectInputStream ois;
	
	public Principal() {

		metodos = new Metodos();
		
		crearPanelContenedor();
		crearPanelLogin();
		crearPanelMenu();
		crearPanelHorario();
		crearPanelOtros();
		crearPanelReuniones();

		visualizarPaneles(enumAcciones.PANEL_LOGIN);
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                    DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
                    output.writeUTF("DESCONECTAR");
                    output.flush();
                    
                    Conexion.conexion.close();
                    dispose();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
		
		try {
			ois = new ObjectInputStream(Conexion.conexion.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void crearPanelContenedor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 600, 380);
		panelContenedor = new JPanel();
		setContentPane(panelContenedor);
		panelContenedor.setLayout(null);
	}

	private void crearPanelLogin() {
		login = new Login();
		panelContenedor.add(login);
		login.setVisible(false);

		login.getLoginBoton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				login.getLoginBoton().setEnabled(false);
				boolean respuesta = metodos.Login(login.getUsuarioCampo(), login.getClaveCampo(), login.getErrorLabel());
				if (respuesta) {
					login.getUsuarioCampo().setText("");
					login.getClaveCampo().setText("");
					login.getErrorLabel().setText("");
					visualizarPaneles(enumAcciones.PANEL_MENU);
				}
				login.getLoginBoton().setEnabled(true);
			}
		});
	}

	private void crearPanelMenu() {
		menu = new Menu();
		panelContenedor.add(menu);
		menu.setVisible(false);

		menu.getSalirBoton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				metodos.Desconectar();
				visualizarPaneles(enumAcciones.PANEL_LOGIN);
			}
		});

		menu.getHorarioBoton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				visualizarPaneles(enumAcciones.PANEL_HORARIO);
				
				String[] horariosArray = new String[0];
				try {
                    DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
                    output.writeUTF("HORARIO");
                    output.flush();

                    if (ois != null) horariosArray = (String[]) ois.readObject();
                } catch (IOException | ClassNotFoundException ioe) {
                	ioe.printStackTrace();
                }
				horario.setHorarios(horariosArray);
			}
		});

		menu.getOtroBoton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				visualizarPaneles(enumAcciones.PANEL_OTROS);
				otros.getTablaPanel().setVisible(false);

				String[] profesoresArray = new String[0];
				try {
                    DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
                    output.writeUTF("PROFESORES");
                    output.flush();

                    if (ois != null) profesoresArray = (String[]) ois.readObject();
                } catch (IOException | ClassNotFoundException ioe) {
                	ioe.printStackTrace();
                }
				otros.setProfesores(profesoresArray);
			}
		});

		menu.getReunionesBoton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				visualizarPaneles(enumAcciones.PANEL_REUNIONES);
			}
		});
	}

	private void crearPanelHorario() {
		horario = new Horario();
		panelContenedor.add(horario);
		horario.setVisible(false);

		horario.getVolverBoton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				visualizarPaneles(enumAcciones.PANEL_MENU);
			}
		});
	}

	private void crearPanelOtros() {
		otros = new Otros();
		panelContenedor.add(otros);
		otros.setVisible(false);

		otros.getVolverBoton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				visualizarPaneles(enumAcciones.PANEL_MENU);
			}
		});
		
		otros.getProfesorCombo().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	String[] horariosArray = new String[0];
				try {
                    DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
                    output.writeUTF("OTROS");
                    output.flush();
                    
                    System.out.println(otros.getProfesorCombo().getSelectedIndex());
                    output.writeInt(otros.getProfesores()[otros.getProfesorCombo().getSelectedIndex()]);
                    output.flush();

                    if (ois != null) horariosArray = (String[]) ois.readObject();
                } catch (IOException | ClassNotFoundException ioe) {
                	ioe.printStackTrace();
                }
				otros.setHorarios(horariosArray);
				otros.getTablaPanel().setVisible(true);
		    }
		});
	}

	private void crearPanelReuniones() {
		reuniones = new Reuniones();
		panelContenedor.add(reuniones);
		reuniones.setVisible(false);

		reuniones.getVolverBoton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				visualizarPaneles(enumAcciones.PANEL_MENU);
			}
		});
	}

	public void visualizarPaneles(enumAcciones panel) {
		login.setVisible(false);
		menu.setVisible(false);
		horario.setVisible(false);
		otros.setVisible(false);
		reuniones.setVisible(false);

		switch (panel) {
		case PANEL_LOGIN:
			login.setVisible(true);
			break;
		case PANEL_MENU:
			menu.setVisible(true);
			break;
		case PANEL_HORARIO:
			horario.setVisible(true);
			break;
		case PANEL_OTROS:
			otros.setVisible(true);
			break;
		case PANEL_REUNIONES:
			reuniones.setVisible(true);
			break;
		default:
			break;

		}
	}

	public Login getPanelLogin() {
		return login;
	}
	
	public Menu getPanelMenu() {
		return menu;
	}

	public Horario getPanelHorarios() {
		return horario;
	}

	public Otros getPanelOtros() {
		return otros;
	}
	
	public Reuniones getPanelReuniones() {
		return reuniones;
	}
}
