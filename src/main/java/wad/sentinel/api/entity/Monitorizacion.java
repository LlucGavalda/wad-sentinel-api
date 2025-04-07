package wad.sentinel.api.entity;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import wad.sentinel.api.dto.MonitorizacionDto;

@Entity
@Table(name = "ws_monitorizaciones")

public class Monitorizacion extends AbstractEntity {

	private static final long serialVersionUID = 1132811703611564522L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_monitorizacion")
	protected Long id;

	@ManyToOne
	@JoinColumn(name = "id_servidor_fk", nullable = false)
	private Servidor servidor;

	@Column(name = "fecha", nullable = false)
	private Timestamp fecha;

	@JoinTable(name = "ws_servidores_monitorizaciones_puertos", joinColumns = @JoinColumn(name = "id_monitorizacion"), inverseJoinColumns = @JoinColumn(name = "id_monitorizacion_puerto"))
	@OneToMany(cascade = { CascadeType.ALL })
	private Set<MonitorizacionPuerto> puertos = Collections.<MonitorizacionPuerto>emptySet();

	@JoinTable(name = "ws_servidores_monitorizaciones_discos", joinColumns = @JoinColumn(name = "id_monitorizacion"), inverseJoinColumns = @JoinColumn(name = "id_monitorizacion_disco"))
	@OneToMany(cascade = { CascadeType.ALL })
	private Set<MonitorizacionDisco> discos = Collections.<MonitorizacionDisco>emptySet();

	@OneToOne
	@JoinColumn(name = "id_monitorizacion_memoria_fk", nullable = false)
	private MonitorizacionMemoria memoria;

	@OneToOne
	@JoinColumn(name = "id_monitorizacion_procesador_fk", nullable = false)
	private MonitorizacionProcesador procesador;

	@JoinTable(name = "ws_servidores_monitorizaciones_temperatura", joinColumns = @JoinColumn(name = "id_monitorizacion"), inverseJoinColumns = @JoinColumn(name = "id_monitorizacion_temperatura"))
	@OneToMany(cascade = { CascadeType.ALL })
	private Set<MonitorizacionTemperatura> temperaturas = Collections.<MonitorizacionTemperatura>emptySet();

	// Constructors -----------------------------

	public Monitorizacion() {
		super();
	}

	public Monitorizacion(MonitorizacionDto dto) {
		super(dto);
		this.id = dto.getId();
		this.servidor = dto.getServidor();
		this.fecha = dto.getFecha();
		// if (dto.getDisponible() != null) {
		// this.disponible = dto.getDisponible();
		// }
	}

	// Methods -----------------------------

	public MonitorizacionDto mapDto() {
		return new MonitorizacionDto(this);
	}

	public void updateFrom(MonitorizacionDto dto) {
		super.updateFrom(dto);
		this.servidor = dto.getServidor();
		this.fecha = dto.getFecha();
	}

	// Getters and Setters -----------------------------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Servidor getServidor() {
		return servidor;
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public Set<MonitorizacionPuerto> getPuertos() {
		return puertos;
	}

	public void setPuertos(Set<MonitorizacionPuerto> puertos) {
		this.puertos = puertos;
	}

	public Set<MonitorizacionDisco> getDiscos() {
		return discos;
	}

	public void setDiscos(Set<MonitorizacionDisco> discos) {
		this.discos = discos;
	}

	public MonitorizacionMemoria getMemoria() {
		return memoria;
	}

	public void setMemoria(MonitorizacionMemoria memoria) {
		this.memoria = memoria;
	}

	public MonitorizacionProcesador getProcesador() {
		return procesador;
	}

	public void setProcesador(MonitorizacionProcesador procesador) {
		this.procesador = procesador;
	}

	public Set<MonitorizacionTemperatura> getTemperaturas() {
		return temperaturas;
	}

	public void setTemperaturas(Set<MonitorizacionTemperatura> temperaturas) {
		this.temperaturas = temperaturas;
	}

}
