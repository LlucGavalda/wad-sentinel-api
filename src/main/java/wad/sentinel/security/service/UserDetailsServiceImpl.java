package wad.sentinel.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wad.sentinel.security.entity.UserAuth;
import wad.sentinel.security.entity.UserDetailsImpl;
import wad.sentinel.security.repository.UserAuthRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserAuthRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	  UserAuth user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

}