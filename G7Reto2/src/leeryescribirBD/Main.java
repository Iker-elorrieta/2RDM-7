package leeryescribirBD;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Main {

	public static void main(String[] args) {

		SessionFactory sesion = HibernateUtil.getSessionFactory();
		Session session = sesion.openSession();

		String hql = "from Users ";
		Query q = session.createQuery(hql);
		List<?> filas = q.list();
System.out.println("Apellidos:");
		for (int i = 0; i < filas.size(); i++) {
			modelo.Users user = (modelo.Users) filas.get(i);
			System.out.println(user.getApellidos());
		}
		
		modelo.Tipos alumno = new modelo.Tipos(4);
		añadirUser("pep", "pep.e@gmail.com", alumno);
	}

	
	public static void añadirUser(String apellido, String email, modelo.Tipos tipo) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		tx = session.beginTransaction();
		modelo.Users users = new modelo.Users ();
		users.setApellidos(apellido);
		users.setEmail(email);
		users.setTipos(tipo);
		session.save(users);
		tx.commit();
		System.out.println("Usuario: "+apellido+" añadido");
		session.close();

	}
}
