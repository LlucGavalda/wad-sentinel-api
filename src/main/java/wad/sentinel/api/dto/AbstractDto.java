package wad.sentinel.api.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import wad.sentinel.api.entity.AbstractEntity;

import wad.sentinel.api.Constants;

/**
 * 
 * Abstract DTO superclass.
 * It provides with common methods that apply to all subclasses, as well as a
 * common
 * interface.
 *
 */
public abstract class AbstractDto {

	// User that has made a modification
	private String usuarioAud;
	// Timestamp of the modification
	@JsonFormat(pattern = Constants.JSON_FORMAT_DATETIME, timezone = Constants.JSON_FORMAT_DATETIME_TIMEZONE)
	private Timestamp fechaAud;
	private String incidencia;

	// Constructors -----------------------------

	/**
	 * Default constructor.
	 * No fields are initialised.
	 */
	public AbstractDto() {

	}

	/**
	 * Convenience constructor that initialises itself with the received entity
	 * data.
	 * 
	 * @param entity The entity from which the fields Id, Valid, UsuarioAud and
	 *               FechaAud will be initialised.
	 */
	public AbstractDto(AbstractEntity entity) {
		mapSystemFields(entity);
		this.incidencia = entity.getIncidencia();
		this.usuarioAud = entity.getUsuarioAud();

	}

	// Methods -----------------------------

	/**
	 * Convenience method to map Id, Valid, UsuarioAud, FechaAud.
	 * 
	 * @param entity The entity from which the fields Id, Valid, UsuarioAud and
	 *               FechaAud will be initialised.
	 */
	public void mapSystemFields(AbstractEntity entity) {
		this.setIncidencia(entity.getIncidencia());
		this.setUsuarioAud(entity.getUsuarioAud());
		this.setFechaAud(entity.getFechaAud());
	}

	// Getters and Setters -----------------------------

	public String getIncidencia() {
		return incidencia;
	}

	public void setIncidencia(String incidencia) {
		this.incidencia = incidencia;
	}

	public String getUsuarioAud() {
		return usuarioAud;
	}

	public void setUsuarioAud(String usuarioAud) {
		this.usuarioAud = usuarioAud;
	}

	public Timestamp getFechaAud() {
		return fechaAud;
	}

	public void setFechaAud(Timestamp fechaAud) {
		this.fechaAud = fechaAud;
	}

	public abstract AbstractEntity mapEntity();
}
