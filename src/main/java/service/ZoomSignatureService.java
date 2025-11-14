package service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * The type Zoom signature service.
 */
@Service
public class ZoomSignatureService {

    @Value("${zoom.bot.client.id}")
    private String clientId;

    @Value("${zoom.bot.client.secret}")
    private String clientSecret;

    /**
     * Generate signature string.
     *
     * @param meetingNumber the meeting number
     * @param role          the role
     * @return the string
     */
    public String generateSignature(String meetingNumber, int role) {
        long iat = System.currentTimeMillis() / 1000L;
        long exp = iat + 2 * 60 * 60; // Token valid

        SecretKey key = Keys.hmacShaKeyFor(clientSecret.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("sdkKey", clientId)
                .claim("mn", meetingNumber)
                .claim("role", role)
                .claim("iat", iat)
                .claim("exp", exp)
                .claim("appKey", clientId)
                .claim("tokenExp", exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return  token;
    }

    /**
     * Gets client id.
     *
     * @return the client id
     */
    public String getClientId() {
        return clientId;
    }
}
