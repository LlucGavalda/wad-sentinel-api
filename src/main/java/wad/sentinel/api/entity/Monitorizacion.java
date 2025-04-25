package wad.sentinel.api.entity;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import wad.sentinel.api.dto.MonitorizacionDto;
import wad.sentinel.api.utils.SearchSpecification;

@Entity
@Table(name = "ws_monitorizaciones")

public class Monitorizacion extends AbstractEntity {

	private static final long serialVersionUID = 1132811703611564522L;

	@ManyToOne
	@JoinColumn(name = "id_servidor_fk", nullable = false)
	private Servidor servidor;

	@Column(name = "incidencia", nullable = false)
	private Boolean incidencia;

	@Column(name = "fecha", nullable = false)
	private Timestamp fecha;

	@JoinTable(name = "ws_servidores_monitorizaciones_puertos", joinColumns = @JoinColumn(name = "id_monitorizacion"), inverseJoinColumns = @JoinColumn(name = "id_monitorizacion_puerto"))
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	private Set<MonitorizacionPuerto> puertos = Collections.<MonitorizacionPuerto>emptySet();

	@JoinTable(name = "ws_servidores_monitorizaciones_discos", joinColumns = @JoinColumn(name = "id_monitorizacion"), inverseJoinColumns = @JoinColumn(name = "id_monitorizacion_disco"))
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	private Set<MonitorizacionDisco> discos = Collections.<MonitorizacionDisco>emptySet();

	@OneToOne
	@JoinColumn(name = "id_monitorizacion_memoria_fk", nullable = false)
	private MonitorizacionMemoria memoria;

	@OneToOne
	@JoinColumn(name = "id_monitorizacion_procesador_fk", nullable = false)
	private MonitorizacionProcesador procesador;

	@JoinTable(name = "ws_servidores_monitorizaciones_temperatura", joinColumns = @JoinColumn(name = "id_monitorizacion"), inverseJoinColumns = @JoinColumn(name = "id_monitorizacion_temperatura"))
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	private Set<MonitorizacionTemperatura> temperaturas = Collections.<MonitorizacionTemperatura>emptySet();

	public static final Map<String, SearchSpecification.SearchType> SEARCHABLE_FIELDS = ImmutableMap.<String, SearchSpecification.SearchType>builder()
			.put("disponible", SearchSpecification.SearchType.BOOLEAN)
			.put("servidor.id", SearchSpecification.SearchType.NUMBER)
			.put("id", SearchSpecification.SearchType.NUMBER)
			.put("incidencia", SearchSpecification.SearchType.BOOLEAN)
			.put("fecha", SearchSpecification.SearchType.DATE_TIME)
			.put("usuarioAud", SearchSpecification.SearchType.STRING)

			.build();

	// Constructors -----------------------------

	public Monitorizacion() {
		super();
	}

	public Monitorizacion(MonitorizacionDto dto) {
		super(dto);
		updateFrom(dto);

	}

	// Methods -----------------------------

	public MonitorizacionDto mapDto() {
		return new MonitorizacionDto(this);
	}

	public void updateFrom(MonitorizacionDto dto) {
		super.updateFrom(dto);
		if (dto.getServidor() != null) {
			this.servidor = dto.getServidor().mapEntity();
		}
		this.incidencia = dto.getIncidencia();
		this.fecha = dto.getFecha();
		if (dto.getPuertos() != null && !dto.getPuertos().isEmpty()) {
			this.puertos = dto.getPuertos().stream().map(ent -> ent.mapEntity()).collect(Collectors.toSet());
		}
		if (dto.getDiscos() != null && !dto.getDiscos().isEmpty()) {
			this.discos = dto.getDiscos().stream().map(ent -> ent.mapEntity()).collect(Collectors.toSet());
		}
		if (dto.getMemoria() != null) {
			this.memoria = new MonitorizacionMemoria(dto.getMemoria());
		}
		if (dto.getProcesador() != null) {
			this.procesador = new MonitorizacionProcesador(dto.getProcesador());
		}
		if (dto.getTemperaturas() != null && !dto.getTemperaturas().isEmpty()) {
			this.temperaturas = dto.getTemperaturas().stream()
					.map(ent -> ent
							.mapEntity())
					.collect(Collectors.toSet());
		}
	}

	// Getters and Setters -----------------------------

	public Servidor getServidor() {
		return servidor;
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}

	public Boolean getIncidencia() {
		return incidencia;
	}

	public void setIncidencia(Boolean incidencia) {
		this.incidencia = incidencia;
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
