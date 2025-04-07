package wad.sentinel.api.dto;

import java.sql.Timestamp;

import wad.sentinel.api.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Madrid")
	private Timestamp fechaAud;
	private String disponible;

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
		this.disponible = entity.getDisponible();
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
		this.setDisponible(entity.getDisponible());
		this.setUsuarioAud(entity.getUsuarioAud());
		this.setFechaAud(entity.getFechaAud());
	}

	// Getters and Setters -----------------------------

	public String getDisponible() {
		return disponible;
	}

	public void setDisponible(String disponible) {
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
