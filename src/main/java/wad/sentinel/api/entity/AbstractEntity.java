package wad.sentinel.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected Long id;

	// Registry is valid or, otherwise, deleted/invalidated.
	@Column(name = "disponible", nullable = false)
	protected Boolean disponible = true;

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
		this.setId(dto.getId());
		this.setDisponible(dto.getDisponible());
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
		this.disponible = dto.getDisponible();
		this.usuarioAud = dto.getUsuarioAud();
	}

	/**
	 * Returns a DTO corresponding to this entity
	 * 
	 * @return
	 */
	public abstract AbstractDto mapDto();

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

}
