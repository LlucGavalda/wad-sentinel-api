package wad.sentinel.api.dto;

import wad.sentinel.api.entity.Parameter;

public class ParameterDto extends AbstractDto {

	private String code;
	private String value;

	// Constructors -----------------------------

	public ParameterDto() {
		super();
	}

	public ParameterDto(Parameter entity) {
		super(entity);
		this.setCode(entity.getCode());
		this.setValue(entity.getValue());
	}

	// Methods -----------------------------

	public Parameter mapEntity() {
		return new Parameter(this);
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
