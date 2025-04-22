package wad.sentinel.api.entity;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import wad.sentinel.api.dto.ServidorDto;
import wad.sentinel.api.utils.SearchSpecification;

@Entity
@Table(name = "ws_servidores")

public class Servidor extends AbstractEntity {

	private static final long serialVersionUID = 1132811703611564522L;

	public static final String cacheName = "ServidorCache";

	@Column(name = "servidor", length = 50, nullable = false)
	private String servidor;
	@Column(name = "host", length = 50, nullable = false)
	private String host;

	public static final Map<String, SearchSpecification.SearchType> SEARCHABLE_FIELDS = ImmutableMap.<String, SearchSpecification.SearchType>builder()
			.put("disponible", SearchSpecification.SearchType.BOOLEAN)
			.put("servidor", SearchSpecification.SearchType.STRING)
			.put("host", SearchSpecification.SearchType.STRING)
			.put("id", SearchSpecification.SearchType.NUMBER)

			.build();

	// Constructors -----------------------------

	public Servidor() {
		super();
	}

	public Servidor(ServidorDto dto) {
		updateFrom(dto);
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
