package de.calltopower.simpletodo.impl.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import de.calltopower.simpletodo.api.utils.STDUtils;
import de.calltopower.simpletodo.impl.model.STDUserDetailsImpl;
import de.calltopower.simpletodo.impl.properties.STDTokenProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

/**
 * Token utilities
 */
@Component
public class STDTokenUtils implements STDUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(STDTokenUtils.class);

    private STDTokenProperties tokenProperties;

    /**
     * Constructor
     * 
     * @param appProperties The application properties
     */
    @Autowired
    STDTokenUtils(STDTokenProperties appProperties) {
        this.tokenProperties = appProperties;
    }

    /**
     * Generates a new JWT
     * 
     * @param authentication The authentication
     * @return A JWT
     */
    public String generateJwtToken(Authentication authentication) {
        STDUserDetailsImpl userPrincipal = (STDUserDetailsImpl) authentication.getPrincipal();

        // @formatter:off
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + tokenProperties.getExpirationMs()))
                .signWith(Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes()), SignatureAlgorithm.HS512).compact();
        // @formatter:on
    }

    /**
     * Returns the username from a JWT
     * 
     * @param token The JWT
     * @return the username
     */
    public String getUserNameFromJwtToken(String token) {
        // @formatter:off
        String username = Jwts.parserBuilder()
                   .setSigningKey(Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes()))
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
        return username;
        // @formatter:on
    }

    /**
     * Validates a JWT
     * 
     * @param authToken The authentication token
     * @return True if successfully validated, false else
     */
    public boolean validateJwtToken(String authToken) {
        try {
            // @formatter:off
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes()))
                .build()
                .parseClaimsJws(authToken);
            // @formatter:on
            return true;
        } catch (SecurityException e) {
            LOGGER.error("Invalid token signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Token claims string is empty: {}", e.getMessage());
        }

        return false;
    }

}
