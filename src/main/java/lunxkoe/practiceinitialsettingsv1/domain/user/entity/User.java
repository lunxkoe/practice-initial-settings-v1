package lunxkoe.practiceinitialsettingsv1.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lunxkoe.practiceinitialsettingsv1.global.entity.BaseTimeEntity;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean locked = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    private User(String email, String password, String name, Role role, Profile profile) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.locked = false;
        setProfile(profile);
    }

    public static User createUser(String email, String password, String name, Profile profile) {
        return new User(email, password, name, Role.USER, profile);
    }

    public static User createAdmin(String email, String password, String name, Profile profile) {
        return new User(email, password, name, Role.ADMIN, profile);
    }

    // == 연관관계 편의 메소드 ==
    protected void setProfile(Profile profile) {
        if (profile != null) {
            this.profile = profile;
            profile.assignUser(this);
        }
    }
}
