package ru.itis.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import ru.itis.models.User;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;

    @Value("${jwt.token.secret}")
    private String secret;

    private String encodedSecret;

    @PostConstruct
    public void init() {
        encodedSecret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(User user) {

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("id", user.getId());
        claims.put("state", user.getUserState().getValue());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, encodedSecret)
                .compact();
    }

    public boolean validateToken(HttpServletRequest req, HttpServletResponse resp) {
        String token = resolveToken(req);
        if (token != null) {
            try {
                Jws<Claims> claims = Jwts.parser().setSigningKey(encodedSecret).parseClaimsJws(token);
                return !claims.getBody().getExpiration().before(new Date());
            } catch (Exception e) {
              return false;
            }
        }
        return false;
    }

    public String resolveToken(HttpServletRequest req) {
        String reqToken = req.getHeader("X-Authorization");
        String cookieToken = null;
        if (WebUtils.getCookie(req, "X-Authorization") != null) {
            cookieToken = WebUtils.getCookie(req, "X-Authorization").getValue();
        }
        if (reqToken != null && reqToken.startsWith("Bearer_")) {
            return reqToken.substring(7);
        } else if (cookieToken != null && cookieToken.startsWith("Bearer_")) {
            return cookieToken.substring(7);
        }
        return null;
    }

    public String getSubject(HttpServletRequest req) {
        String token = resolveToken(req);
        Jws<Claims> claims = Jwts.parser().setSigningKey(encodedSecret).parseClaimsJws(token);
        return claims.getBody().getSubject();
    }
}
