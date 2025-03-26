package wad.sentinel.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import wad.sentinel.security.dto.LoginInDto;
import wad.sentinel.security.dto.LoginOutDto;
import wad.sentinel.security.entity.UserDetailsImpl;
import wad.sentinel.security.repository.UserAuthRepository;
import wad.sentinel.security.utils.SecurityUtils;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	SecurityUtils jwtUtils;

	@Autowired
	UserAuthRepository userRepository;

    	/**
	 * Login with user and password.
	 * Returns the corresponding token that will be accepted in further calls.
	 * 
	 */
    @Override
	public LoginOutDto login(LoginInDto loginRequest) {

		LoginOutDto loginOut = new LoginOutDto(null, null);

			UsernamePasswordAuthenticationToken tok = new UsernamePasswordAuthenticationToken(
					loginRequest.getUsuario(),
					loginRequest.getPassword());
			Authentication authentication = authenticationManager.authenticate(tok);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			loginOut.setUsername(userDetails.getUsername());
			loginOut.setToken(jwt);
			// LoginOutDto loginOut = new LoginOutDto(jwt, userDetails.getUsername());

			return loginOut;

	}
}
