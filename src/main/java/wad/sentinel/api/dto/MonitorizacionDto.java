package wad.sentinel.api.dto;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

import wad.sentinel.api.Constants;
import wad.sentinel.api.entity.Monitorizacion;
import wad.sentinel.api.entity.Servidor;

public class MonitorizacionDto extends AbstractDto {

	private Long id;
	private Servidor servidor;
	@JsonFormat(pattern = Constants.JSON_FORMAT_DATETIME, timezone = Constants.JSON_FORMAT_DATETIME_TIMEZONE)
	private Timestamp fecha;

	private Set<MonitorizacionPuertoDto> puertos;
	private Set<MonitorizacionDiscoDto> discos;
	private MonitorizacionMemoriaDto memoria;
	private MonitorizacionProcesadorDto procesador;
	private Set<MonitorizacionTemperaturaDto> temperaturas;

	// Constructors -----------------------------

	public MonitorizacionDto() {
		super();
	}

	public MonitorizacionDto(Monitorizacion entity) {
		super(entity);
		this.id = entity.getId();
		this.servidor = entity.getServidor();
		this.fecha = entity.getFecha();
		if (entity.getPuertos() != null && !entity.getPuertos().isEmpty()) {
			this.puertos = entity.getPuertos().stream().map(MonitorizacionPuertoDto::new).collect(Collectors.toSet());
		}
		if (entity.getDiscos() != null && !entity.getDiscos().isEmpty()) {
			this.discos = entity.getDiscos().stream().map(MonitorizacionDiscoDto::new).collect(Collectors.toSet());
		}
		if (entity.getMemoria() != null) {
			this.memoria = new MonitorizacionMemoriaDto(entity.getMemoria());
		}
		if (entity.getProcesador() != null) {
			this.procesador = new MonitorizacionProcesadorDto(entity.getProcesador());
		}
		if (entity.getTemperaturas() != null && !entity.getTemperaturas().isEmpty()) {
			this.temperaturas = entity.getTemperaturas().stream()
					.map(MonitorizacionTemperaturaDto::new)
					.collect(Collectors.toSet());
		}
	}

	// Methods -----------------------------

	public Monitorizacion mapEntity() {
		return new Monitorizacion(this);
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

	public Set<MonitorizacionPuertoDto> getPuertos() {
		return puertos;
	}

	public void setPuertos(Set<MonitorizacionPuertoDto> puertos) {
		this.puertos = puertos;
	}

	public Set<MonitorizacionDiscoDto> getDiscos() {
		return discos;
	}

	public void setDiscos(Set<MonitorizacionDiscoDto> discos) {
		this.discos = discos;
	}

	public MonitorizacionMemoriaDto getMemoria() {
		return memoria;
	}

	public void setMemoria(MonitorizacionMemoriaDto memoria) {
		this.memoria = memoria;
	}

	public MonitorizacionProcesadorDto getProcesador() {
		return procesador;
	}

	public void setProcesador(MonitorizacionProcesadorDto procesador) {
		this.procesador = procesador;
	}

	public Set<MonitorizacionTemperaturaDto> getTemperaturas() {
		return temperaturas;
	}

	public void setTemperaturas(Set<MonitorizacionTemperaturaDto> temperaturas) {
		this.temperaturas = temperaturas;
	}

}
