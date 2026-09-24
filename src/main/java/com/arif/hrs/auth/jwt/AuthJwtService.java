package com.arif.hrs.auth.jwt;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.arif.hrs.auth.constant.AuthConstant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthJwtService {
  private final Logger log = LoggerFactory.getLogger(getClass());

  @Value("${security.jwt.secret-key}")
  private String secretKey;

  @Value("${security.jwt.expiration-time}")
  private Long expirationTime;

  @Value("${security.jwt.refresh.expiration.time}")
  private Long refreshExpirationTime;

  public String createToken(Map<String, Object> additionalClaims, String username, Long expirationTime) {
    return Jwts.builder()
        .setClaims(additionalClaims)
        .setSubject(username)
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(Date.from(Instant.now().plusSeconds(expirationTime)))
        .signWith(getSigninKey(), SignatureAlgorithm.HS256).compact();
  }

  private Long getExpirationTime() {
    return expirationTime;
  }

  private Long getRefreshExpirationTime() {
    return refreshExpirationTime;
  }

  private Key getSigninKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractUserName(String token) {
    return claims(token, Claims::getSubject);
  }

  public Object extractAdditionalClaims(String token, String key) {
    return claims(token, c -> c.get(key));
  }

  public Date extractExpirationTime(String token) {
    return claims(token, Claims::getExpiration);
  }

  public Boolean validateToken(String token, UserDetails user) {

    log.info("validate token");

    try {

      String userName = extractUserName(token);
      Date tokenExpiration = extractExpirationTime(token);

      return userName.equals(user.getUsername()) && Instant.now().isBefore(tokenExpiration.toInstant());
    } catch (Exception e) {
      log.error("validation fail", e);
      return false;
    }
  }

  private <T> T claims(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = Jwts.parserBuilder().setSigningKey(getSigninKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    return claimsResolver.apply(claims);
  }

  public String createJwtToken(String username, String email) {
    log.info("create jwt token");

    Map<String, Object> additionalClaims = new HashMap<String, Object>();
    additionalClaims.put(AuthConstant.CLAIMS_EMAIL, email);

    return createToken(additionalClaims, username, getExpirationTime());
  }

  public String createRefreshToken(String userName, String email) {
    log.info("create refresh token");

    Map<String, Object> additionalClaims = new HashMap<String, Object>();
    additionalClaims.put(AuthConstant.CLAIMS_EMAIL, email);

    return createToken(additionalClaims, userName, getRefreshExpirationTime());
  }

  public String fetchTokenValue(String token) {

    if (token == null) {
      return null;
    }
    if (token.startsWith(AuthConstant.BEARER_PREFIX)) {
      return token.substring(AuthConstant.BEARER_PREFIX.length()).trim();
    }
    return token.trim();
  }
}
