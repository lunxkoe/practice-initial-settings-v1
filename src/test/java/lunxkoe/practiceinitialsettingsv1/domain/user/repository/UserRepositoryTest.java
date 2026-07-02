package lunxkoe.practiceinitialsettingsv1.domain.user.repository;

import jakarta.persistence.EntityManager;
import lunxkoe.practiceinitialsettingsv1.domain.user.entity.Gender;
import lunxkoe.practiceinitialsettingsv1.domain.user.entity.Profile;
import lunxkoe.practiceinitialsettingsv1.domain.user.entity.User;
import lunxkoe.practiceinitialsettingsv1.global.config.JpaAuditingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("User와 Profile을 저장하고 BaseTimeEntity가 정상 동작한다.")
    void saveUserWithProfileAndAuditing() {
        // given
        Profile profile = Profile.createProfile(
                Gender.MALE,
                LocalDate.of(2001, 1, 1),
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        User user = User.createUser(
                "test@example.com",
                null,
                "홍길동",
                profile
        );

        // when
        User savedUser = userRepository.save(user);
        em.flush();
        em.clear();

        User foundUser = userRepository.findById(savedUser.getId()).get();

        // then
        assertThat(foundUser.getId()).isNotNull();
        assertThat(foundUser.getCreatedAt()).isNotNull();
        assertThat(foundUser.getProfile()).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(savedUser.getProfile().getId());
        assertThat(foundUser.getProfile().getGender()).isEqualTo(Gender.MALE);
    }
}
