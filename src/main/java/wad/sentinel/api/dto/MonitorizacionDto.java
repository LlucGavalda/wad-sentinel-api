package wad.sentinel.api.dto;

import java.sql.Timestamp;

import wad.sentinel.api.entity.Monitorizacion;
import wad.sentinel.api.entity.Servidor;

public class MonitorizacionDto extends AbstractDto {

	private Long id;
	private Servidor servidor;
	private Timestamp fecha;

	public MonitorizacionPuertoDto[] puertos;
	public MonitorizacionDiscoDto[] discos;
	public MonitorizacionMemoriaDto memoria;
	public MonitorizacionProcesadorDto procesador;
	public MonitorizacionTemperaturaDto[] temperatura;

	// Constructors -----------------------------

	public MonitorizacionDto() {
		super();
	}

	public MonitorizacionDto(Monitorizacion entity) {
		super(entity);
		this.id = entity.getId();
		this.servidor = entity.getServidor();
		this.fecha = entity.getFecha();
		// this.puertos = entity.getPuertos();
		// this.discos = entity.getDiscos();
		// this.memoria = entity.getMemoria();
		// this.procesador = entity.getProcesador();
		// this.temperatura = entity.getTemperatura();
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

	public MonitorizacionPuertoDto[] getPuertos() {
		return puertos;
	}

	public void setPuertos(MonitorizacionPuertoDto[] puertos) {
		this.puertos = puertos;
	}

	public MonitorizacionDiscoDto[] getDiscos() {
		return discos;
	}

	public void setDiscos(MonitorizacionDiscoDto[] discos) {
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

	public MonitorizacionTemperaturaDto[] getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(MonitorizacionTemperaturaDto[] temperatura) {
		this.temperatura = temperatura;
	}

}
