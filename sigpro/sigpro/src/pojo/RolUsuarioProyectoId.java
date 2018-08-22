package pojo;
// Generated Dec 20, 2017 3:43:57 PM by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RolUsuarioProyectoId generated by hbm2java
 */
@Embeddable
public class RolUsuarioProyectoId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3821441574509453716L;
	private int rol;
	private int proyecto;
	private String usuario;

	public RolUsuarioProyectoId() {
	}

	public RolUsuarioProyectoId(int rol, int proyecto, String usuario) {
		this.rol = rol;
		this.proyecto = proyecto;
		this.usuario = usuario;
	}

	@Column(name = "rol", nullable = false)
	public int getRol() {
		return this.rol;
	}

	public void setRol(int rol) {
		this.rol = rol;
	}

	@Column(name = "proyecto", nullable = false)
	public int getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(int proyecto) {
		this.proyecto = proyecto;
	}

	@Column(name = "usuario", nullable = false, length = 30)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RolUsuarioProyectoId))
			return false;
		RolUsuarioProyectoId castOther = (RolUsuarioProyectoId) other;

		return (this.getRol() == castOther.getRol()) && (this.getProyecto() == castOther.getProyecto())
				&& ((this.getUsuario() == castOther.getUsuario()) || (this.getUsuario() != null
						&& castOther.getUsuario() != null && this.getUsuario().equals(castOther.getUsuario())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRol();
		result = 37 * result + this.getProyecto();
		result = 37 * result + (getUsuario() == null ? 0 : this.getUsuario().hashCode());
		return result;
	}

}