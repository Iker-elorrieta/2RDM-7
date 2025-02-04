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
import modelo.Reuniones;
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
	private final String mensajeReuniones = "REUNIONES";
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

				switch (mensaje) {
					case mensajeLogout:
						System.out.println("Cliente " + clienteId + " ha solicitado cerrar sesion.");
						_conectado = esperarLogin(dos, dis, oos);
						break;
					case mensajeDesconectar:
						System.out.println("Cliente " + clienteId + " ha solicitado desconectarse.");
						conectado = false;
						break;
					case mensajeHorario:
						Object[][] array = obtenerHorario(userId);
			            oos.writeObject(array);
			            oos.flush();
						break;
					case mensajeProfesores:
						Object[][] array1 = obtenerProfesores();
			            oos.writeObject(array1);
			            oos.flush();
						break;
					case mensajeOtros:
						int profe = dis.readInt();
						Object[][] array2 = obtenerHorario(profe);
			            oos.writeObject(array2);
			            oos.flush();
						break;
					case mensajeReuniones:
						Object[][] array3 = obtenerReuniones(userId);
			            oos.writeObject(array3);
			            oos.flush();
						break;
					case mensajeHorarioAlum:
	                    Object[][] array4 = obtenerHorarioAlumno(userId);
	                    oos.writeObject(array4);
	                    oos.flush();
						break;
					case mensajeListaAlum:
	                    String[] array5 = obtenerListaAlumnos();
	                    oos.writeObject(array5);
	                    oos.flush();
						break;
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

	private modelo.Users comprobarUsuario(String usuario, String contrasena, boolean android) {
		Transaction tx = null;
		modelo.Users usuarioEncontrado = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();

			String hql = "FROM Users WHERE username = :username AND password = :password";
			if (!android) hql += " AND (tipos.name='profesor' OR tipos.name='administrador' OR tipos.name='god')";
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
                boolean esAndroid = dis.readBoolean();
                System.out.println("Cliente (" + (esAndroid ? "android" : "java") + ") " + clienteId + " - Contraseña: " + contrasena);
                Users usuario1= comprobarUsuario(usuario, contrasena, esAndroid);
                autenticado = usuario1 != null;

                System.out.println(autenticado);
                dos.writeBoolean(autenticado);
                dos.flush();   

                if (autenticado) {
                    System.out.println("Cliente " + clienteId + " autenticado correctamente.");
                    
                    if (esAndroid) {
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
                            
        					dos.writeUTF(obtenerMatriculaciones(usuario1.getId()));
                            dos.flush();
                    }
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
	
	private Object[][] obtenerHorario(int profesorID) {
		Transaction tx = null;
		Object[][] horarios = new Object[0][0];

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();

			String hql = "FROM Horarios WHERE profe_id = :profesor";
			Query query = session.createQuery(hql);
			query.setParameter("profesor", profesorID);

			List<Horarios> horariosEncontrados = query.list();
			horarios = new Object[horariosEncontrados.size()][3];
			
			for (int i = 0; i < horarios.length; i++) {
				Horarios actual = horariosEncontrados.get(i);
				
				horarios[i][0] = actual.getModulos().getNombre();
				horarios[i][1] = actual.getId().getHora();
				horarios[i][2] = actual.getId().getDia();
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

	private Object[][] obtenerReuniones(int profesorID) {
		Transaction tx = null;
		Object[][] reuniones = new Object[0][0];

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();

			String hql = "FROM Reuniones WHERE profesor_id = :profesor";
			Query query = session.createQuery(hql);
			query.setParameter("profesor", profesorID);

			List<Reuniones> reunionesEncontradas = query.list();
			reuniones = new Object[reunionesEncontradas.size()][7];
			
			for (int i = 0; i < reuniones.length; i++) {
				Reuniones actual = reunionesEncontradas.get(i);
				
				reuniones[i][0] = actual.getEstado();
				reuniones[i][1] = actual.getTitulo();
				reuniones[i][2] = actual.getAsunto();
				reuniones[i][3] = actual.getFecha();
				reuniones[i][4] = actual.getIdCentro();
				reuniones[i][5] = actual.getAula();
				Users alumno = actual.getUsersByAlumnoId();
				reuniones[i][6] = alumno.getNombre() + " " + alumno.getApellidos();
			}
			
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		}

		return reuniones;
	}
	
	private Object[][] obtenerProfesores() {
		Transaction tx = null;
		ArrayList<Users> profesores = new ArrayList<Users>();

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();

			String hql = "FROM Users WHERE tipo_id != 4";
			Query query = session.createQuery(hql);

			List<Users> usuarios = query.list();
			
			for (int i = 0; i < usuarios.size(); i++) {
				Users actual = usuarios.get(i);
				profesores.add(actual);
			}
			
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		}
		
		Object[][] result = new Object[profesores.size()][2];
		for (int i = 0; i < profesores.size(); i++) {
			Users actual = profesores.get(i);
			result[i][0] = actual.getNombre()+" "+actual.getApellidos();
			result[i][1] = actual.getId();
		}

		return result;
	}
	
	private Object[][] obtenerHorarioAlumno(int alumnoID) {
        Transaction tx = null;
        Object[][] horarios = new Object[0][0];

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
            horarios = new Object[horariosEncontrados.size()][3];

            
            for (int i = 0; i < horarios.length; i++) {
                Object[] row = horariosEncontrados.get(i);
                String nombreModulo = (String) row[0];
                Integer hora = (Integer) row[1];
                Integer dia = (Integer) row[2];

                horarios[i][0] = nombreModulo;
                horarios[i][1] = hora;
                horarios[i][2] = dia;
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
    
    private String obtenerMatriculaciones(int userId) {
        Transaction tx = null;
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            
            
            Query query = session.createQuery("SELECT c.nombre FROM Ciclos c JOIN c.matriculacioneses m JOIN m.users u WHERE u.id = :userId");
            query.setParameter("userId", userId);
            List<String> ciclos = query.list(); 
            
            if (ciclos.size() > 0)
                
                  return ciclos.get(0); 
            
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        }
        System.out.println("Llega a return");
        return "-";
    }
}
