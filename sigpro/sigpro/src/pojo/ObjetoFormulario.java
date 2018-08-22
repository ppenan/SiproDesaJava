package pojo;
// Generated Dec 20, 2017 3:43:57 PM by Hibernate Tools 5.2.3.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ObjetoFormulario generated by hbm2java
 */
@Entity
@Table(name = "objeto_formulario", catalog = "sipro")
public class ObjetoFormulario implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5686780110824348318L;
	private ObjetoFormularioId id;
	private Formulario formulario;
	private String usuarioCreo;
	private String usuarioActualizo;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private int estado;
	private Set<FormularioItemValor> formularioItemValors = new HashSet<FormularioItemValor>(0);

	public ObjetoFormulario() {
	}

	public ObjetoFormulario(ObjetoFormularioId id, Formulario formulario, String usuarioCreo, Date fechaCreacion,
			int estado) {
		this.id = id;
		this.formulario = formulario;
		this.usuarioCreo = usuarioCreo;
		this.fechaCreacion = fechaCreacion;
		this.estado = estado;
	}

	public ObjetoFormulario(ObjetoFormularioId id, Formulario formulario, String usuarioCreo, String usuarioActualizo,
			Date fechaCreacion, Date fechaActualizacion, int estado, Set<FormularioItemValor> formularioItemValors) {
		this.id = id;
		this.formulario = formulario;
		this.usuarioCreo = usuarioCreo;
		this.usuarioActualizo = usuarioActualizo;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
		this.estado = estado;
		this.formularioItemValors = formularioItemValors;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "formularioid", column = @Column(name = "formularioid", nullable = false)),
			@AttributeOverride(name = "objetoTipo", column = @Column(name = "objeto_tipo", nullable = false)),
			@AttributeOverride(name = "objetoId", column = @Column(name = "objeto_id", nullable = false)) })
	public ObjetoFormularioId getId() {
		return this.id;
	}

	public void setId(ObjetoFormularioId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "formularioid", nullable = false, insertable = false, updatable = false)
	public Formulario getFormulario() {
		return this.formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "objetoFormulario")
	public Set<FormularioItemValor> getFormularioItemValors() {
		return this.formularioItemValors;
	}

	public void setFormularioItemValors(Set<FormularioItemValor> formularioItemValors) {
		this.formularioItemValors = formularioItemValors;
	}

}