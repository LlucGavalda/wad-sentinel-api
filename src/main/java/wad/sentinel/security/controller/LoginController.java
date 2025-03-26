package wad.sentinel.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wad.sentinel.security.Constants;
import wad.sentinel.security.dto.LoginInDto;
import wad.sentinel.security.dto.LoginOutDto;
import wad.sentinel.security.service.LoginService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(Constants.URL_LOGIN)
public class LoginController {
    
    @Autowired
    LoginService entityService;

    /**
     * Sing in request.
     * Returns authentication token.
     * 
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginInDto loginRequest,
            HttpServletResponse response
            ) {
        LoginOutDto dto = entityService.login(loginRequest);
        
        // Get the token and only return it in the header
        response.addHeader("Authorization", "Bearer "+dto.getToken());
        
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
