package wad.sentinel.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import wad.sentinel.api.dto.AbstractDto;

/**
 * 
 * Abstract Entity superclass.
 * It provides convenience methods and constructors, a common interface for all the entities
 * and common methods.
 *
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 8658231246277532919L;
	
	// Registry is valid or, otherwise, deleted/invalidated.
	@Column(name = "disponible", length = 1, nullable = false)
	private String disponible = "S";
	
	// User that has made a modification
	@Column(name="id_usuario_aud", length=30, nullable=false)
	protected String userModId;

	// Timestamp of the modification
	@Column(name="fecha_aud", nullable=false)
	protected Timestamp dateMod;
	
	// Constructors -----------------------------

	/**
	 * Default constructor.
	 * No field is initialised.
	 */
	public AbstractEntity() {

	}
	
	/**
	 * Convenience constructor that initialises itself with the received dto data.
	 * @param dto	The dto from which the fields Id, Valid, UserModId and DateMod will be initialisec.
	 */
	public AbstractEntity(AbstractDto dto) {
		mapSystemFields(dto);
	}

	// Methods -----------------------------

	/**
	 * Convenience method that initialises itself with the received dto data.
	 * @param dto	The dto from which the fields Id, Valid, UserModId and DateMod will be initialisec.
	 */
	public void mapSystemFields(AbstractDto dto) {
		this.setDisponible(dto.getDisponible());
		this.setUserModId(dto.getUserModId());
		this.setDateMod(dto.getDateMod());
	}

	/**
	 * Prepares the entity to be saved, by setting:
	 * - DateMod
	 * - UserModId
	 */
	public void prepareToSave() {
	    // Guardar
	    this.setDateMod(new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * Sets self transient fields from the Dto
	 * 
	 * @param dto
	 */
	public void setTransientFrom(AbstractDto dto) {
		// Nothing to do here
	}
	
	/**
	 * Updates self fields with Dto fields
	 * 
	 * @param dto The dto from which take the data
	 */
	public void updateFrom(AbstractDto dto) {
		this.disponible = dto.getDisponible();
		this.userModId = dto.getUserModId();
	}
	
	/**
	 * Returns a DTO corresponding to this entity
	 * 
	 * @return
	 */
	public abstract AbstractDto mapDto();
	
	// Getters and Setters -----------------------------

	public String getDisponible() {
		return disponible;
	}

	public void setDisponible(String disponible) {
		this.disponible = disponible;
	}

	public String getUserModId() {
		return userModId;
	}

	public void setUserModId(String userModId) {
		this.userModId = userModId;
	}

	public Timestamp getDateMod() {
		return dateMod;
	}

	public void setDateMod(Timestamp dateMod) {
		this.dateMod = dateMod;
	}

}
