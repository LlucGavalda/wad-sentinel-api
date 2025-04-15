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

	// Registry internal id
	private Long id;
	// User that has made a modification
	private String usuarioAud;
	// Timestamp of the modification
	@JsonFormat(pattern = Constants.JSON_FORMAT_DATETIME, timezone = Constants.JSON_FORMAT_DATETIME_TIMEZONE)
	private Timestamp fechaAud;
	private Boolean disponible;

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

	}

	// Methods -----------------------------

	/**
	 * Convenience method to map Id, Valid, UsuarioAud, FechaAud.
	 * 
	 * @param entity The entity from which the fields Id, Valid, UsuarioAud and
	 *               FechaAud will be initialised.
	 */
	public void mapSystemFields(AbstractEntity entity) {
		this.setId(entity.getId());
		this.setDisponible(entity.getDisponible());
		this.setUsuarioAud(entity.getUsuarioAud());
		this.setFechaAud(entity.getFechaAud());
	}

	// Getters and Setters -----------------------------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getDisponible() {
		return disponible;
	}

	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
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
