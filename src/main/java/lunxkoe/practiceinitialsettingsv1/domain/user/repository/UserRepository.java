package lunxkoe.practiceinitialsettingsv1.domain.user.repository;

import lunxkoe.practiceinitialsettingsv1.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
