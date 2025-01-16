package vista;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controlador.Metodos;

public class Principal extends JFrame {
	private static final long serialVersionUID = 1L;

	public static enum enumAcciones {
		PANEL_LOGIN,
		PANEL_MENU,
		PANEL_HORARIO,
		PANEL_OTROS,
		PANEL_REUNIONES
	}

	private JPanel panelContenedor;
	private Login login;
	private Menu menu;
	private Horario horario;
	private Otros otros;
	private Reuniones reuniones;

	public Principal() {

		crearPanelContenedor();
		crearPanelLogin();
		crearPanelMenu();
		crearPanelHorario();
		crearPanelOtros();
		crearPanelReuniones();

		visualizarPaneles(enumAcciones.PANEL_LOGIN);
	}

	private void crearPanelContenedor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 450, 300);
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
				boolean respuesta = Metodos.Login(login.getUsuarioCampo(), login.getClaveCampo(), login.getErrorLabel());
				if (respuesta) {
					login.getUsuarioCampo().setText("");
					login.getClaveCampo().setText("");
					login.getErrorLabel().setText("");
					visualizarPaneles(enumAcciones.PANEL_MENU);
				}
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
				visualizarPaneles(enumAcciones.PANEL_LOGIN);
			}
		});

		menu.getHorarioBoton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				visualizarPaneles(enumAcciones.PANEL_HORARIO);
			}
		});

		menu.getOtroBoton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				visualizarPaneles(enumAcciones.PANEL_OTROS);
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
