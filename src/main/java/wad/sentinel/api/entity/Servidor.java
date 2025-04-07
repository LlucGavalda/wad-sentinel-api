package wad.sentinel.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import wad.sentinel.api.dto.ServidorDto;
import jakarta.persistence.Id;

@Entity
@Table(name = "ws_servidores")

public class Servidor extends AbstractEntity {

	private static final long serialVersionUID = 1132811703611564522L;

	public static final String cacheName = "ServidorCache";

	// DB Id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_servidor")
	protected Long id;
	@Column(name = "servidor", length = 50, nullable = false)
	private String servidor;
	@Column(name = "host", length = 50, nullable = false)
	private String host;

	// Constructors -----------------------------

	public Servidor() {
		super();
	}

	public Servidor(ServidorDto dto) {
		updateFrom(dto);
		this.id = dto.getId();
		this.servidor = dto.getServidor();
		this.host = dto.getHost();
	}

	// Methods -----------------------------

	public ServidorDto mapDto() {
		return new ServidorDto(this);
	}

	public void updateFrom(ServidorDto dto) {
		super.updateFrom(dto);
		this.servidor = dto.getServidor();
		this.host = dto.getHost();
	}

	// Getters and Setters -----------------------------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServidor() {
		return servidor;
	}

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
