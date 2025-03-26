package wad.sentinel.security.entity;

import java.io.Serializable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import wad.sentinel.security.dto.UserAuthDto;

@Entity
@Table(name = "usuarios")

public class UserAuth implements Serializable {
    
    private static final long serialVersionUID = 7905215688006129713L;

	@Id	
	@Column(length=50)
	private String usuario;
	
	@Column(length=100)
	private String password;

    	/**
	 * Convenience empty constructor.
	 */
	public UserAuth() {
		
	}
	
	/**
	 * Convenience constructor with all the fields of a valid user.
	 * 
	 * @param usuario
	 * @param password
	 */
	public UserAuth(String usuario, String password) {
		this.usuario = usuario;
		this.password = password;
	}
	
	public UserAuth(UserAuthDto dto) {
		this.usuario = dto.getUsuario();
		this.password = dto.getPassword();

	}
		
	public UserAuthDto mapDto() {
		return new UserAuthDto(this);
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
