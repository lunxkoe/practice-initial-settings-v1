package lunxkoe.practiceinitialsettingsv1.global.security.jwt;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
public class TestJwtProvider {

    private final SecretKey key;

    public TestJwtProvider(String secretKey) {
        this.key = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    public String createAccessToken() {

        long pastTime1 = System.currentTimeMillis() - 2000;
        long pastTime2 = System.currentTimeMillis() - 1000;
        Date issuedDate = new Date(pastTime1);
        Date expiredDate = new Date(pastTime2);

        return Jwts.builder()
                .claim("role", "USER")
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(key)
                .compact();
    }
}
