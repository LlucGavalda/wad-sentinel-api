package wad.sentinel.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import wad.sentinel.security.entity.UserAuth;


public class UserAuthDto {
    
	private String usuario;
	@JsonIgnore
	private String password;

	
	public UserAuthDto() {
		super();
	}
	
	public UserAuthDto(UserAuth entity) {
		this.usuario = entity.getUsuario();
		this.password = entity.getPassword();
		}
	
	
	public UserAuth mapEntity() {
		return new UserAuth(this);
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
