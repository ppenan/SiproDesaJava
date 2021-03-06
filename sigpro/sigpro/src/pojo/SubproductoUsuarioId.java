package pojo;
// Generated Dec 20, 2017 3:43:57 PM by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SubproductoUsuarioId generated by hbm2java
 */
@Embeddable
public class SubproductoUsuarioId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5925864498157002540L;
	private int subproductoid;
	private String usuario;

	public SubproductoUsuarioId() {
	}

	public SubproductoUsuarioId(int subproductoid, String usuario) {
		this.subproductoid = subproductoid;
		this.usuario = usuario;
	}

	@Column(name = "subproductoid", nullable = false)
	public int getSubproductoid() {
		return this.subproductoid;
	}

	public void setSubproductoid(int subproductoid) {
		this.subproductoid = subproductoid;
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
		if (!(other instanceof SubproductoUsuarioId))
			return false;
		SubproductoUsuarioId castOther = (SubproductoUsuarioId) other;

		return (this.getSubproductoid() == castOther.getSubproductoid())
				&& ((this.getUsuario() == castOther.getUsuario()) || (this.getUsuario() != null
						&& castOther.getUsuario() != null && this.getUsuario().equals(castOther.getUsuario())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getSubproductoid();
		result = 37 * result + (getUsuario() == null ? 0 : this.getUsuario().hashCode());
		return result;
	}

}
