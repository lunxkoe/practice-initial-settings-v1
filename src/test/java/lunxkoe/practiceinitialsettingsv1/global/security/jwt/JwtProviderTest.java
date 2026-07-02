package lunxkoe.practiceinitialsettingsv1.global.security.jwt;

import io.jsonwebtoken.Claims;
import lunxkoe.practiceinitialsettingsv1.domain.user.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderTest {

    private JwtProvider jwtProvider;
    private TestJwtProvider testJwtProvider;

    private final String testSecret = "practice-initial-settings-v1-secret-key-must-be-at-least-32-bytes-long";

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider(testSecret, 3600, 604800);
        testJwtProvider = new TestJwtProvider(testSecret);
    }

    @Test
    @DisplayName("액세스 토큰을 성공적으로 생성하고 검증한다.")
    void createAndValidateAccessToken() {
        // given
        String userId = UUID.randomUUID().toString();
        Role role = Role.USER;

        // when
        String token = jwtProvider.createAccessToken(userId, role.name());
        boolean isValid = jwtProvider.validateToken(token);
        Claims claims = jwtProvider.getClaims(token);

        // then
        assertThat(token).isNotBlank();
        assertThat(isValid).isTrue();
        assertThat(claims.getSubject()).isEqualTo(userId);
        assertThat(claims.get("role")).isEqualTo(role.name());
    }

    @Test
    @DisplayName("잘못된 토큰 검증은 실패한다.")
    void invalidateWrongToken() {
        // given
        String wrongToken = "sdfasfasdfasdfasdfasdfasdfa";

        // when
        boolean isValid = jwtProvider.validateToken(wrongToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("만료된 토큰 검증은 실패한다.")
    void invalidateExpirationToken() {
        // given
        String wrongToken = testJwtProvider.createAccessToken();

        // when
        boolean isValid = jwtProvider.validateToken(wrongToken);

        // then
        assertThat(isValid).isFalse();
    }
}