package pojo;
// Generated Dec 20, 2017 3:43:57 PM by Hibernate Tools 5.2.3.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TipoAdquisicion generated by hbm2java
 */
@Entity
@Table(name = "tipo_adquisicion", catalog = "sipro")
public class TipoAdquisicion implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8754763186643134247L;
	private Integer id;
	private int cooperantecodigo;
	private String nombre;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private int estado;
	private int convenioCdirecta;
	private Set<PlanAdquisicion> planAdquisicions = new HashSet<PlanAdquisicion>(0);

	public TipoAdquisicion() {
	}

	public TipoAdquisicion(int cooperantecodigo, String nombre, String usuarioCreo, Date fechaCreacion, int estado,
			int convenioCdirecta) {
		this.cooperantecodigo = cooperantecodigo;
		this.nombre = nombre;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
		this.estado = estado;
		this.convenioCdirecta = convenioCdirecta;
	}

	public TipoAdquisicion(int cooperantecodigo, String nombre, String usuarioCreo, String usuarioActualizo,
			Date fechaCreacion, Date fechaActualizacion, int estado, int convenioCdirecta,
			Set<PlanAdquisicion> planAdquisicions) {
		this.cooperantecodigo = cooperantecodigo;
		this.nombre = nombre;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
		this.convenioCdirecta = convenioCdirecta;
		this.planAdquisicions = planAdquisicions;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "cooperantecodigo", nullable = false)
	public int getCooperantecodigo() {
		return this.cooperantecodigo;
	}

	public void setCooperantecodigo(int cooperantecodigo) {
		this.cooperantecodigo = cooperantecodigo;
	}

	@Column(name = "nombre", nullable = false, length = 1000)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "usuario_creo", nullable = false, length = 30)
	public String getUsuarioCreo() {
		return this.usuarioCreo;
	}

	public void setUsuarioCreo(String usuarioCreo) {
		this.usuarioCreo = usuarioCreo;
	}

	@Column(name = "usuario_actualizo", length = 30)
	public String getUsuarioActualizo() {
		return this.usuarioActualizo;
	}

	public void setUsuarioActualizo(String usuarioActualizo) {
		this.usuarioActualizo = usuarioActualizo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_creacion", nullable = false, length = 19)
	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_actualizacion", length = 19)
	public Date getFechaActualizacion() {
		return this.fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	@Column(name = "estado", nullable = false)
	public int getEstado() {
		return this.estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	@Column(name = "convenio_cdirecta", nullable = false)
	public int getConvenioCdirecta() {
		return this.convenioCdirecta;
	}

	public void setConvenioCdirecta(int convenioCdirecta) {
		this.convenioCdirecta = convenioCdirecta;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoAdquisicion")
	public Set<PlanAdquisicion> getPlanAdquisicions() {
		return this.planAdquisicions;
	}

	public void setPlanAdquisicions(Set<PlanAdquisicion> planAdquisicions) {
		this.planAdquisicions = planAdquisicions;
	}

}
