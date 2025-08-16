package com.thekade.nopolin.appointment_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Long extractUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Authentication not found");
        }

        // For now, we'll use a simple approach
        // In a real implementation, you might want to store user ID in the JWT claims
        // or have a separate service to get user ID from username
        String username = authentication.getName();
        
        // This is a placeholder implementation
        // You should implement proper user ID extraction based on your JWT structure
        // For example, if your JWT contains user ID in claims:
        // return extractUserIdFromJwtClaims(authentication);
        
        // For now, return a default value - replace with actual implementation
        return 1L;
    }

    public Long extractUserIdFromJwtClaims(Authentication authentication) {
        // This method would extract user ID from JWT claims
        // Implementation depends on how you structure your JWT tokens
        try {
            // Example implementation - adjust based on your JWT structure
            String token = extractTokenFromAuthentication(authentication);
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            
            // Assuming user ID is stored in claims as "userId"
            return claims.get("userId", Long.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract user ID from JWT", e);
        }
    }

    private String extractTokenFromAuthentication(Authentication authentication) {
        // This method would extract the JWT token from the authentication object
        // Implementation depends on how you store the token in authentication
        throw new UnsupportedOperationException("Token extraction not implemented");
    }
}
