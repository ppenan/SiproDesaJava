package pojo;
// Generated Dec 20, 2017 3:43:57 PM by Hibernate Tools 5.2.3.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RolPermisoId generated by hbm2java
 */
@Embeddable
public class RolPermisoId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3154114422278344320L;
	private int rolid;
	private int permisoid;

	public RolPermisoId() {
	}

	public RolPermisoId(int rolid, int permisoid) {
		this.rolid = rolid;
		this.permisoid = permisoid;
	}

	@Column(name = "rolid", nullable = false)
	public int getRolid() {
		return this.rolid;
	}

	public void setRolid(int rolid) {
		this.rolid = rolid;
	}

	@Column(name = "permisoid", nullable = false)
	public int getPermisoid() {
		return this.permisoid;
	}

	public void setPermisoid(int permisoid) {
		this.permisoid = permisoid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RolPermisoId))
			return false;
		RolPermisoId castOther = (RolPermisoId) other;

		return (this.getRolid() == castOther.getRolid()) && (this.getPermisoid() == castOther.getPermisoid());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRolid();
		result = 37 * result + this.getPermisoid();
		return result;
	}

}