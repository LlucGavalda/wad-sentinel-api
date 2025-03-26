package wad.sentinel.security.dto;

public class LoginOutDto {

	private String usuario;
	private String token;

	public LoginOutDto(
			String token, 
            String usuario
            ) {
		this.token = token;
		this.usuario = usuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return usuario;
	}

	public void setUsername(String usuario) {
		this.usuario = usuario;
	}
	
}
