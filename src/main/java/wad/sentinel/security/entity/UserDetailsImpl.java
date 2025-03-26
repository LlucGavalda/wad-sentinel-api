package wad.sentinel.security.entity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * This class implements authorisation user details, as a subset of user data.
 * It can handle not only user data but other useful data like roles.
 * 
 * An instance of this entity will be stored as 'principal' of the authorisation token.
 *
 */
public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String usuario;
  
  @JsonIgnore
  private String password;
  private Collection<? extends GrantedAuthority> authorities;

  /**
   * Convenience constructor from all the fields.
   * 
   * @param id
   * @param usuario
   * @param password
   * @param authorities
   */
  public UserDetailsImpl(String usuario, String password,
      Collection<? extends GrantedAuthority> authorities) {
    this.usuario = usuario;
    this.password = password;
    this.authorities = authorities;
  }

  
  /**
   * Converts the user into a UserDetailsImpl with all the user's data
   * 
   * @param user
   * @return
   */
  public static UserDetailsImpl build(UserAuth user) {
    List<GrantedAuthority> authorities = null;
    
    return new UserDetailsImpl(
        user.getUsuario(), 
        user.getPassword(), 
        authorities
        );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return usuario;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}