package wad.sentinel.api.dto;

import java.sql.Timestamp;

import wad.sentinel.api.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * Abstract DTO superclass.
 * It provides with common methods that apply to all subclasses, as well as a common
 * interface.
 *
 */
public abstract class AbstractDto {


	// User that has made a modification
	private String userModId;
	// Timestamp of the modification
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="Europe/Madrid")
	private Timestamp dateMod;
	private String disponible;

	
	// Constructors -----------------------------

	/**
	 * Default constructor.
	 * No fields are initialised.
	 */
	public AbstractDto() {
		
	}
	
	/**
	 * Convenience constructor that initialises itself with the received entity data.
	 * @param entity	The entity from which the fields Id, Valid, UserModId and DateMod will be initialised.
	 */
	public AbstractDto(AbstractEntity entity) {
		mapSystemFields(entity);
		this.disponible = entity.getDisponible();
		this.userModId = entity.getUserModId();

	}

	// Methods -----------------------------

	/**
	 * Convenience method to map Id, Valid, UserModId, DateMod.
	 * @param entity	The entity from which the fields Id, Valid, UserModId and DateMod will be initialised.
	 */
	public void mapSystemFields(AbstractEntity entity) {
		this.setDisponible(entity.getDisponible());
		this.setUserModId(entity.getUserModId());
		this.setDateMod(entity.getDateMod());
	}
	
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


	public abstract AbstractEntity mapEntity();
}
