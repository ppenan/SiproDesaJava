package pojoSigade;
// Generated Sep 29, 2017 10:37:44 PM by Hibernate Tools 5.2.3.Final

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * DtmAvanceFisfinanDetDtiId generated by hbm2java
 */
@Embeddable
public class DtmAvanceFisfinanDetDtiId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4374812953835264368L;
	private long ejercicioFiscal;
	private String mesDesembolso;
	private String codigoPresupuestario;
	private Integer entidadSicoin;
	private Integer unidadEjecutoraSicoin;
	private String monedaDesembolso;
	private BigDecimal desembolsosMesMoneda;
	private BigDecimal tcMonUsd;
	private BigDecimal desembolsosMesUsd;
	private BigDecimal tcUsdGtq;
	private BigDecimal desembolsosMesGtq;

	public DtmAvanceFisfinanDetDtiId() {
	}

	public DtmAvanceFisfinanDetDtiId(long ejercicioFiscal) {
		this.ejercicioFiscal = ejercicioFiscal;
	}

	public DtmAvanceFisfinanDetDtiId(long ejercicioFiscal, String mesDesembolso, String codigoPresupuestario,
			Integer entidadSicoin, Integer unidadEjecutoraSicoin, String monedaDesembolso,
			BigDecimal desembolsosMesMoneda, BigDecimal tcMonUsd, BigDecimal desembolsosMesUsd, BigDecimal tcUsdGtq,
			BigDecimal desembolsosMesGtq) {
		this.ejercicioFiscal = ejercicioFiscal;
		this.mesDesembolso = mesDesembolso;
		this.codigoPresupuestario = codigoPresupuestario;
		this.entidadSicoin = entidadSicoin;
		this.unidadEjecutoraSicoin = unidadEjecutoraSicoin;
		this.monedaDesembolso = monedaDesembolso;
		this.desembolsosMesMoneda = desembolsosMesMoneda;
		this.tcMonUsd = tcMonUsd;
		this.desembolsosMesUsd = desembolsosMesUsd;
		this.tcUsdGtq = tcUsdGtq;
		this.desembolsosMesGtq = desembolsosMesGtq;
	}

	@Column(name = "ejercicio_fiscal", nullable = false, precision = 15, scale = 0)
	public long getEjercicioFiscal() {
		return this.ejercicioFiscal;
	}

	public void setEjercicioFiscal(long ejercicioFiscal) {
		this.ejercicioFiscal = ejercicioFiscal;
	}

	@Column(name = "mes_desembolso", length = 45)
	public String getMesDesembolso() {
		return this.mesDesembolso;
	}

	public void setMesDesembolso(String mesDesembolso) {
		this.mesDesembolso = mesDesembolso;
	}

	@Column(name = "codigo_presupuestario", length = 45)
	public String getCodigoPresupuestario() {
		return this.codigoPresupuestario;
	}

	public void setCodigoPresupuestario(String codigoPresupuestario) {
		this.codigoPresupuestario = codigoPresupuestario;
	}

	@Column(name = "entidad_sicoin")
	public Integer getEntidadSicoin() {
		return this.entidadSicoin;
	}

	public void setEntidadSicoin(Integer entidadSicoin) {
		this.entidadSicoin = entidadSicoin;
	}

	@Column(name = "unidad_ejecutora_sicoin")
	public Integer getUnidadEjecutoraSicoin() {
		return this.unidadEjecutoraSicoin;
	}

	public void setUnidadEjecutoraSicoin(Integer unidadEjecutoraSicoin) {
		this.unidadEjecutoraSicoin = unidadEjecutoraSicoin;
	}

	@Column(name = "moneda_desembolso", length = 45)
	public String getMonedaDesembolso() {
		return this.monedaDesembolso;
	}

	public void setMonedaDesembolso(String monedaDesembolso) {
		this.monedaDesembolso = monedaDesembolso;
	}

	@Column(name = "desembolsos_mes_moneda", precision = 15)
	public BigDecimal getDesembolsosMesMoneda() {
		return this.desembolsosMesMoneda;
	}

	public void setDesembolsosMesMoneda(BigDecimal desembolsosMesMoneda) {
		this.desembolsosMesMoneda = desembolsosMesMoneda;
	}

	@Column(name = "tc_mon_usd", precision = 15)
	public BigDecimal getTcMonUsd() {
		return this.tcMonUsd;
	}

	public void setTcMonUsd(BigDecimal tcMonUsd) {
		this.tcMonUsd = tcMonUsd;
	}

	@Column(name = "desembolsos_mes_usd", precision = 15)
	public BigDecimal getDesembolsosMesUsd() {
		return this.desembolsosMesUsd;
	}

	public void setDesembolsosMesUsd(BigDecimal desembolsosMesUsd) {
		this.desembolsosMesUsd = desembolsosMesUsd;
	}

	@Column(name = "tc_usd_gtq", precision = 15)
	public BigDecimal getTcUsdGtq() {
		return this.tcUsdGtq;
	}

	public void setTcUsdGtq(BigDecimal tcUsdGtq) {
		this.tcUsdGtq = tcUsdGtq;
	}

	@Column(name = "desembolsos_mes_gtq", precision = 15)
	public BigDecimal getDesembolsosMesGtq() {
		return this.desembolsosMesGtq;
	}

	public void setDesembolsosMesGtq(BigDecimal desembolsosMesGtq) {
		this.desembolsosMesGtq = desembolsosMesGtq;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DtmAvanceFisfinanDetDtiId))
			return false;
		DtmAvanceFisfinanDetDtiId castOther = (DtmAvanceFisfinanDetDtiId) other;

		return (this.getEjercicioFiscal() == castOther.getEjercicioFiscal())
				&& ((this.getMesDesembolso() == castOther.getMesDesembolso())
						|| (this.getMesDesembolso() != null && castOther.getMesDesembolso() != null
								&& this.getMesDesembolso().equals(castOther.getMesDesembolso())))
				&& ((this.getCodigoPresupuestario() == castOther.getCodigoPresupuestario())
						|| (this.getCodigoPresupuestario() != null && castOther.getCodigoPresupuestario() != null
								&& this.getCodigoPresupuestario().equals(castOther.getCodigoPresupuestario())))
				&& ((this.getEntidadSicoin() == castOther.getEntidadSicoin())
						|| (this.getEntidadSicoin() != null && castOther.getEntidadSicoin() != null
								&& this.getEntidadSicoin().equals(castOther.getEntidadSicoin())))
				&& ((this.getUnidadEjecutoraSicoin() == castOther.getUnidadEjecutoraSicoin())
						|| (this.getUnidadEjecutoraSicoin() != null && castOther.getUnidadEjecutoraSicoin() != null
								&& this.getUnidadEjecutoraSicoin().equals(castOther.getUnidadEjecutoraSicoin())))
				&& ((this.getMonedaDesembolso() == castOther.getMonedaDesembolso())
						|| (this.getMonedaDesembolso() != null && castOther.getMonedaDesembolso() != null
								&& this.getMonedaDesembolso().equals(castOther.getMonedaDesembolso())))
				&& ((this.getDesembolsosMesMoneda() == castOther.getDesembolsosMesMoneda())
						|| (this.getDesembolsosMesMoneda() != null && castOther.getDesembolsosMesMoneda() != null
								&& this.getDesembolsosMesMoneda().equals(castOther.getDesembolsosMesMoneda())))
				&& ((this.getTcMonUsd() == castOther.getTcMonUsd()) || (this.getTcMonUsd() != null
						&& castOther.getTcMonUsd() != null && this.getTcMonUsd().equals(castOther.getTcMonUsd())))
				&& ((this.getDesembolsosMesUsd() == castOther.getDesembolsosMesUsd())
						|| (this.getDesembolsosMesUsd() != null && castOther.getDesembolsosMesUsd() != null
								&& this.getDesembolsosMesUsd().equals(castOther.getDesembolsosMesUsd())))
				&& ((this.getTcUsdGtq() == castOther.getTcUsdGtq()) || (this.getTcUsdGtq() != null
						&& castOther.getTcUsdGtq() != null && this.getTcUsdGtq().equals(castOther.getTcUsdGtq())))
				&& ((this.getDesembolsosMesGtq() == castOther.getDesembolsosMesGtq())
						|| (this.getDesembolsosMesGtq() != null && castOther.getDesembolsosMesGtq() != null
								&& this.getDesembolsosMesGtq().equals(castOther.getDesembolsosMesGtq())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getEjercicioFiscal();
		result = 37 * result + (getMesDesembolso() == null ? 0 : this.getMesDesembolso().hashCode());
		result = 37 * result + (getCodigoPresupuestario() == null ? 0 : this.getCodigoPresupuestario().hashCode());
		result = 37 * result + (getEntidadSicoin() == null ? 0 : this.getEntidadSicoin().hashCode());
		result = 37 * result + (getUnidadEjecutoraSicoin() == null ? 0 : this.getUnidadEjecutoraSicoin().hashCode());
		result = 37 * result + (getMonedaDesembolso() == null ? 0 : this.getMonedaDesembolso().hashCode());
		result = 37 * result + (getDesembolsosMesMoneda() == null ? 0 : this.getDesembolsosMesMoneda().hashCode());
		result = 37 * result + (getTcMonUsd() == null ? 0 : this.getTcMonUsd().hashCode());
		result = 37 * result + (getDesembolsosMesUsd() == null ? 0 : this.getDesembolsosMesUsd().hashCode());
		result = 37 * result + (getTcUsdGtq() == null ? 0 : this.getTcUsdGtq().hashCode());
		result = 37 * result + (getDesembolsosMesGtq() == null ? 0 : this.getDesembolsosMesGtq().hashCode());
		return result;
	}

}