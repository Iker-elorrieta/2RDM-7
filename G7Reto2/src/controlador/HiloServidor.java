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
	private final String mensajeHorarioAlum = "HORARIOALUMNO";
	private final String mensajeListaAlum = "LISTAALUMNOS";

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

			boolean _conectado = esperarLogin(dos, dis, oos);

			boolean conectado = true;
			while (conectado && _conectado) {
				String mensaje = dis.readUTF();
				System.out.println("Cliente " + clienteId + " dice: " + mensaje);

				if (mensaje.equalsIgnoreCase(mensajeLogout)) {
					System.out.println("Cliente " + clienteId + " ha solicitado cerrar sesion.");
					_conectado = esperarLogin(dos, dis, oos);
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
				} else if(mensaje.equalsIgnoreCase(mensajeHorarioAlum)) {
                    String[] array = obtenerHorarioAlumno(userId);
                    oos.writeObject(array);
                    oos.flush();
                } else if(mensaje.equalsIgnoreCase(mensajeListaAlum)) {
                    String[] array = obtenerListaAlumnos();
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

			String hql = "FROM Users WHERE username = :username AND password = :password AND (tipos.name='profesor' OR tipos.name='administrador' OR tipos.name='god')";
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

	private boolean esperarLogin(DataOutputStream dos, DataInputStream dis, ObjectOutputStream oos) {
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
                Users usuario1= comprobarUsuario(usuario, contrasena);
                autenticado = usuario1 != null;

                System.out.println(autenticado);
                dos.writeBoolean(autenticado);
                dos.flush();   

                if (autenticado) {
                    System.out.println("Cliente " + clienteId + " autenticado correctamente.");
                    
                    Object[] datosUsuario = new Object[] {
                    	usuario1.getTipos().getId(),
                    	usuario1.getId(),
                    	usuario1.getEmail(),
                    	usuario1.getUsername(),
                    	usuario1.getNombre(),
                    	usuario1.getApellidos(),
                    	usuario1.getDni()
                    };
                    oos.writeObject(datosUsuario);
                    oos.flush(); 
                } else
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
	
	private String[] obtenerHorarioAlumno(int alumnoID) {
        Transaction tx = null;
        String[] horarios = new String[0];

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String hql = "select h.modulos.nombre, h.id.hora, h.id.dia "
                       + "from Horarios h "
                       + "where h.modulos.ciclos.id = (select m.ciclos.id from Matriculaciones m where m.users.id = :idUsuario) "
                       + "and h.modulos.curso = (select mat.id.curso from Matriculaciones mat where mat.users.id = :idUsuario) "
                       + "order by h.id.dia, h.id.hora";
            
            Query query = session.createQuery(hql);
            query.setParameter("idUsuario", alumnoID); 

        
            List<Object[]> horariosEncontrados = query.list();
            horarios = new String[horariosEncontrados.size()];

            
            for (int i = 0; i < horarios.length; i++) {
                Object[] row = horariosEncontrados.get(i);
                String nombreModulo = (String) row[0];
                Integer hora = (Integer) row[1];
                Integer dia = (Integer) row[2];

                horarios[i] = nombreModulo + "," + hora + "," + dia;
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
	
    private String[] obtenerListaAlumnos() {
        Transaction tx = null;
        String[] horarios = new String[0];

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String hql = "FROM Users WHERE tipo_id = 4";
            Query query = session.createQuery(hql);
            

            List<Users> horariosEncontrados = query.list();
            horarios = new String[horariosEncontrados.size()];

            for (int i = 0; i < horarios.length; i++) {
                Users actual = horariosEncontrados.get(i);

                horarios[i] = actual.getNombre();
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
}
