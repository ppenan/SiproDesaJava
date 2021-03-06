package pojo;
// Generated Dec 20, 2017 3:43:57 PM by Hibernate Tools 5.2.3.Final

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

/**
 * TipoMoneda generated by hbm2java
 */
@Entity
@Table(name = "tipo_moneda", catalog = "sipro")
public class TipoMoneda implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4460403244972771061L;
	private Integer id;
	private String nombre;
	private String simbolo;
	private Set<Desembolso> desembolsos = new HashSet<Desembolso>(0);
	private Set<Prestamo> prestamos = new HashSet<Prestamo>(0);

	public TipoMoneda() {
	}

	public TipoMoneda(String nombre, String simbolo, Set<Desembolso> desembolsos, Set<Prestamo> prestamos) {
		this.nombre = nombre;
		this.simbolo = simbolo;
		this.desembolsos = desembolsos;
		this.prestamos = prestamos;
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

	@Column(name = "nombre", length = 1000)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "simbolo", length = 5)
	public String getSimbolo() {
		return this.simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoMoneda")
	public Set<Desembolso> getDesembolsos() {
		return this.desembolsos;
	}

	public void setDesembolsos(Set<Desembolso> desembolsos) {
		this.desembolsos = desembolsos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoMoneda")
	public Set<Prestamo> getPrestamos() {
		return this.prestamos;
	}

	public void setPrestamos(Set<Prestamo> prestamos) {
		this.prestamos = prestamos;
	}

}
