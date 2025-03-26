package wad.sentinel.api.dto;

import wad.sentinel.api.entity.Servidor;

public class ServidorDto extends AbstractDto {

	
	private Long id;
	private String servidor;
	private String host;

	// Constructors -----------------------------

	public ServidorDto() {
		super();
	}

	public ServidorDto(Servidor entity) {
		super(entity);
		this.id = entity.getId();
		this.servidor = entity.getServidor();
		this.host = entity.getHost();
	}

	// Methods -----------------------------

	public Servidor mapEntity() {
		return new Servidor(this);
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
