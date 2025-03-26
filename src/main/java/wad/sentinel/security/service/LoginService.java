package wad.sentinel.security.service;

import wad.sentinel.security.dto.LoginInDto;
import wad.sentinel.security.dto.LoginOutDto;

public interface LoginService {

    public LoginOutDto login(LoginInDto loginRequest);
}