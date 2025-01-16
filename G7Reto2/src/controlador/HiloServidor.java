package controlador;

import java.net.Socket;
import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import leeryescribirBD.HibernateUtil;

public class HiloServidor extends Thread {
	private Socket cliente;
	private int clienteId;

	public HiloServidor(Socket cliente, int clienteId) {
		this.cliente = cliente;
		this.clienteId = clienteId;
	}

	@Override
	public void run() {
		try (DataInputStream dis = new DataInputStream(cliente.getInputStream());
				DataOutputStream dos = new DataOutputStream(cliente.getOutputStream())) {

			System.out.println("Atendiendo al cliente " + clienteId);

			esperarLogin(dos, dis);

			boolean conectado = true;
			while (conectado) {
				String mensaje = dis.readUTF();
				System.out.println("Cliente " + clienteId + " dice: " + mensaje);

				if (mensaje.equalsIgnoreCase("LOGOUT")) {
					System.out.println("Cliente " + clienteId + " ha solicitado cerrar sesion.");
					//dos.writeUTF("Desconexión exitosa. Hasta luego.");
					esperarLogin(dos, dis);
				} else if (mensaje.equalsIgnoreCase("DESCONECTAR")) {
					System.out.println("Cliente " + clienteId + " ha solicitado desconectarse.");
					conectado = false;
					//dos.writeUTF("Desconexión exitosa. Adiós.");
				} else {
					//dos.writeUTF("Servidor recibió: " + mensaje);
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

	private void esperarLogin(DataOutputStream dos, DataInputStream dis) {
		boolean autenticado = false;

		while (!autenticado) {
			String usuario;
			try {
				usuario = dis.readUTF();

				System.out.println("Cliente " + clienteId + " - Usuario: " + usuario);

				String contrasena = dis.readUTF();
				System.out.println("Cliente " + clienteId + " - Contraseña: " + contrasena);

				autenticado = comprobarUsuario(usuario, contrasena) != null;
				modelo.Tipos alumno = new modelo.Tipos(4);
				añadirUser(usuario, alumno);
				System.out.println(autenticado);
				dos.writeBoolean(autenticado);

				if (autenticado) {
					System.out.println("Cliente " + clienteId + " autenticado correctamente.");
					
					//dos.writeUTF("Bienvenido, " + usuario + ". Puede comenzar a interactuar con el servidor.");

				} else {
					System.out.println("Cliente " + clienteId + " falló el inicio de sesión. Intentando de nuevo...");
					//dos.writeUTF("Usuario o contraseña incorrectos. Intente nuevamente.");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void añadirUser(String apellido, modelo.Tipos tipo) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		tx = session.beginTransaction();
		modelo.Users users = new modelo.Users ();
		users.setApellidos(apellido);
		users.setTipos(tipo);
		session.save(users);
		tx.commit();
		System.out.println("Usuario: "+apellido+" añadido");
		session.close();

	}
}
