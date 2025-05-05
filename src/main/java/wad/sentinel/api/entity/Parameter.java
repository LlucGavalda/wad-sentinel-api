package wad.sentinel.api.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.envers.Audited;

import com.google.common.collect.ImmutableMap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import wad.sentinel.api.dto.ParameterDto;
import wad.sentinel.api.utils.SearchSpecification;

@Entity
@Table(name = "parametres")
@Audited
public class Parameter extends AbstractEntity {

	private static final long serialVersionUID = -5107577686082261704L;
	public static final String cacheName = "parameterCache";

	// public static final String PAGE_GROUP = Constants.PAGE_GROUP_SECURITY;
	// public static final String PAGE_ID = Constants.PAGE_ID_SECURITY_PARAMETER;
	@Column(name = "codi", length = 50, nullable = false)
	private String code;

	@Column(name = "valor", length = 255, nullable = false)
	private String value;

	/**
	 * List of searchable fields.
	 * Use "." preceded of the join field name to access join table fields.
	 */
	public static final Map<String, SearchSpecification.SearchType> SEARCHABLE_FIELDS = ImmutableMap.<String, SearchSpecification.SearchType>builder()
			.put("id", SearchSpecification.SearchType.NUMBER)
			.put("valid", SearchSpecification.SearchType.BOOLEAN)
			.put("code", SearchSpecification.SearchType.STRING)
			.put("value", SearchSpecification.SearchType.STRING)
			.build();
	public static final List<String> ORDER_FIELDS = Collections.unmodifiableList(Arrays.asList(
			"id",
			"value",
			"code"));

	// Constructors -----------------------------

	public Parameter() {
		super();
	}

	public Parameter(ParameterDto dto) {
		super(dto);
		this.setCode(dto.getCode());
		this.setValue(dto.getValue());
	}

	// Methods -----------------------------

	public ParameterDto mapDto() {
		return new ParameterDto(this);
	}

	// Getters and Setters -----------------------------

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
