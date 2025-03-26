package wad.sentinel.security.utils;


import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import wad.sentinel.security.Constants;
import wad.sentinel.security.entity.UserDetailsImpl;

/**
 * Utilities related to authentication token
 * 
 */
@Component
public class SecurityUtils {

  private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

  /**
   * Generates token
   * 
   * @param authentication
   * @return
   */
  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS))
        .signWith(key(), SignatureAlgorithm.HS256)
        .claim("userId", userPrincipal.getId())
        .compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(Constants.ACCESS_TOKEN_SECRET));
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  /**
   * Validates token
   * 
   * @param authToken
   * @return true in case token is valid; false otherwise
   */
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      // Token is valid
      return true;

    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token. " + e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired. " + e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported. " + e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty. " + e.getMessage());
    }

    return false;
  }

  /**
   * Gets and returns the user name that has made the call, taken from the
   * security token.
   * In case the user is not valid or has not been authenticated, returns null.
   * 
   * @return
   */
  public static Long getUserId() {
    Long userId = null;
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (principal instanceof UserDetailsImpl) {
      UserDetailsImpl userDetails = (UserDetailsImpl) principal;
      userId = userDetails.getId();
    }

    return userId;
  }

  /**
   * Gets and returns the user name that has made the call, taken from the
   * security token.
   * In case the user is not valid or has not been authenticated, returns null.
   * 
   * @return
   */
  public static String getUserName() {
    String userName = null;
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (principal instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) principal;
      userName = userDetails.getUsername();
    }

    return userName;
  }

}
