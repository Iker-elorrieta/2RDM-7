package controlador;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import leeryescribirBD.HibernateUtil;
import modelo.Horarios;
import modelo.HorariosId;
import modelo.Users;

public class HiloServidor extends Thread {
	private Socket cliente;
	private int clienteId;
	private int userId;
	
	private final String mensajeLogout = "LOGOUT";
	private final String mensajeDesconectar= "DESCONECTAR";
	private final String mensajeHorario = "HORARIO";
	private final String mensajeProfesores = "PROFESORES";
	private final String mensajeOtros = "OTROS";

	public HiloServidor(Socket cliente, int clienteId) {
		this.cliente = cliente;
		this.clienteId = clienteId;
	}

	@Override
	public void run() {
		try (DataInputStream dis = new DataInputStream(cliente.getInputStream());
				DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());
				ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream())) {

			System.out.println("Atendiendo al cliente " + clienteId);

			boolean _conectado = esperarLogin(dos, dis);

			boolean conectado = true;
			while (conectado && _conectado) {
				String mensaje = dis.readUTF();
				System.out.println("Cliente " + clienteId + " dice: " + mensaje);

				if (mensaje.equalsIgnoreCase(mensajeLogout)) {
					System.out.println("Cliente " + clienteId + " ha solicitado cerrar sesion.");
					_conectado = esperarLogin(dos, dis);
				} else if (mensaje.equalsIgnoreCase(mensajeDesconectar)) {
					System.out.println("Cliente " + clienteId + " ha solicitado desconectarse.");
					conectado = false;
				} else if (mensaje.equalsIgnoreCase(mensajeHorario)) {
					String[] array = obtenerHorario(userId);
		            oos.writeObject(array);
		            oos.flush();
				} else if (mensaje.equalsIgnoreCase(mensajeProfesores)) {
					String[] array = obtenerProfesores();
		            oos.writeObject(array);
		            oos.flush();
				} else if (mensaje.equalsIgnoreCase(mensajeOtros)) {
					int profe = dis.readInt();
					String[] array = obtenerHorario(profe);
		            oos.writeObject(array);
		            oos.flush();
				}
			}

		} catch (IOException e) {
			System.out.println("Error en el cliente " + clienteId + ": " + e.getMessage());
		} finally {
			try {
				cliente.close();
				System.out.println("Cliente " + clienteId + " desconectado.");
			} catch (IOException e) {
				System.out.println("Error al cerrar la conexión con cliente " + clienteId + ": " + e.getMessage());
			}
		}
	}

	private modelo.Users comprobarUsuario(String usuario, String contrasena) {
		Transaction tx = null;
		modelo.Users usuarioEncontrado = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();

			String hql = "FROM Users WHERE username = :username AND password = :password AND tipos.name='profesor'";
			Query query = session.createQuery(hql);
			query.setParameter("username", usuario);
			query.setParameter("password", contrasena);

			List<modelo.Users> listaUsuarios = query.list();
			if (!listaUsuarios.isEmpty()) {
				usuarioEncontrado = listaUsuarios.get(0);
				this.userId = usuarioEncontrado.getId();
			}

			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		}

		return usuarioEncontrado;
	}

	private boolean esperarLogin(DataOutputStream dos, DataInputStream dis) {
		boolean autenticado = false;

		while (!autenticado) {
			String usuario;
			try {
				usuario = dis.readUTF();
				if (usuario.equalsIgnoreCase(mensajeDesconectar)) {
					System.out.println("Cliente " + clienteId + " ha solicitado desconectarse.");
					return false;
				}
				
				System.out.println("Cliente " + clienteId + " - Usuario: " + usuario);

				String contrasena = dis.readUTF();
				System.out.println("Cliente " + clienteId + " - Contraseña: " + contrasena);

				autenticado = comprobarUsuario(usuario, contrasena) != null;
				//modelo.Tipos alumno = new modelo.Tipos(4);
				//añadirUser(alumno);
				System.out.println(autenticado);
				dos.writeBoolean(autenticado);

				if (autenticado)
					System.out.println("Cliente " + clienteId + " autenticado correctamente.");
				else
					System.out.println("Cliente " + clienteId + " falló el inicio de sesión. Intentando de nuevo...");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	public static void añadirUser(modelo.Tipos tipo) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		tx = session.beginTransaction();
		modelo.Users users = new modelo.Users ();
		users.setTipos(tipo);
		users.setUsername("paco");
		users.setPassword("pass");
		users.setNombre("paco");
		users.setApellidos("apellido");
		users.setDni("dni");
		users.setDireccion("direccion");
		users.setTelefono1(null);
		users.setTelefono2(null);
		
		session.save(users);
		tx.commit();
		System.out.println("Usuario: paco añadido");
		session.close();

	}
	
	private String[] obtenerHorario(int profesorID) {
		Transaction tx = null;
		String[] horarios = new String[0];

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();

			String hql = "FROM Horarios WHERE profe_id = :profesor";
			Query query = session.createQuery(hql);
			query.setParameter("profesor", profesorID);

			List<Horarios> horariosEncontrados = query.list();
			horarios = new String[horariosEncontrados.size()];
			
			for (int i = 0; i < horarios.length; i++) {
				Horarios actual = horariosEncontrados.get(i);
				
				horarios[i] = actual.getModulos().getNombre()+","+actual.getId().getHora()+","+actual.getId().getDia();
			}
			
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		}

		return horarios;
	}
	
	private String[] obtenerProfesores() {
		Transaction tx = null;
		ArrayList<String>profesores = new ArrayList<String>();

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();

			String hql = "FROM Users WHERE tipo_id != 4";
			Query query = session.createQuery(hql);

			List<Users> usuarios = query.list();
			
			for (int i = 0; i < usuarios.size(); i++) {
				Users actual = usuarios.get(i);
				
				profesores.add(actual.getNombre()+" "+actual.getApellidos()+","+actual.getId());
			}
			
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		}

		return profesores.toArray(new String[profesores.size()]);
	}
}
