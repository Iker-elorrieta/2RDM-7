package modelo;
// Generated 14 ene 2025, 10:11:24 by Hibernate Tools 6.5.1.Final

/**
 * Matriculaciones generated by hbm2java
 */
public class Matriculaciones implements java.io.Serializable {

	private MatriculacionesId id;
	private Ciclos ciclos;
	private Users users;

	public Matriculaciones() {
	}

	public Matriculaciones(MatriculacionesId id, Ciclos ciclos, Users users) {
		this.id = id;
		this.ciclos = ciclos;
		this.users = users;
	}

	public MatriculacionesId getId() {
		return this.id;
	}

	public void setId(MatriculacionesId id) {
		this.id = id;
	}

	public Ciclos getCiclos() {
		return this.ciclos;
	}

	public void setCiclos(Ciclos ciclos) {
		this.ciclos = ciclos;
	}

	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

}
