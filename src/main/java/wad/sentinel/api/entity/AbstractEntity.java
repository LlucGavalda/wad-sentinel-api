package wad.sentinel.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import wad.sentinel.api.dto.AbstractDto;
import wad.sentinel.authentication.utils.SecurityUtils;

/**
 * 
 * Abstract Entity superclass.
 * It provides convenience methods and constructors, a common interface for all
 * the entities
 * and common methods.
 *
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 8658231246277532919L;

	// Registry is valid or, otherwise, deleted/invalidated.
	@Column(name = "incidencia", length = 1, nullable = false)
	protected String incidencia = "S";

	// User that has made a modification
	@Column(name = "id_usuario_aud", length = 30, nullable = false)
	protected String usuarioAud;

	// Timestamp of the modification
	@Column(name = "fecha_aud", nullable = false)
	protected Timestamp fechaAud;

	// Constructors -----------------------------

	/**
	 * Default constructor.
	 * No field is initialised.
	 */
	public AbstractEntity() {

	}

	/**
	 * Convenience constructor that initialises itself with the received dto data.
	 * 
	 * @param dto The dto from which the fields Id, Valid, UsuarioAud and FechaAud
	 *            will be initialisec.
	 */
	public AbstractEntity(AbstractDto dto) {
		mapSystemFields(dto);
	}

	// Methods -----------------------------

	/**
	 * Convenience method that initialises itself with the received dto data.
	 * 
	 * @param dto The dto from which the fields Id, Valid, UsuarioAud and FechaAud
	 *            will be initialisec.
	 */
	public void mapSystemFields(AbstractDto dto) {
		this.setIncidencia(dto.getIncidencia());
		this.setUsuarioAud(dto.getUsuarioAud());
		this.setFechaAud(dto.getFechaAud());
	}

	/**
	 * Prepares the entity to be saved, by setting:
	 * - FechaAud
	 * - UsuarioAud
	 */
	public void prepareToSave() {
		this.setFechaAud(new Timestamp(System.currentTimeMillis()));
		this.setUsuarioAud(SecurityUtils.getUserName());

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
		this.incidencia = dto.getIncidencia();
		this.usuarioAud = dto.getUsuarioAud();
	}

	/**
	 * Returns a DTO corresponding to this entity
	 * 
	 * @return
	 */
	public abstract AbstractDto mapDto();

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

}
