package lunxkoe.practiceinitialsettingsv1.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    private Integer tempSensitivity;

    private String profileImageUrl;

    // 기상청 위치 정보
    private Double lat;

    private Double lon;

    @Column(name = "grid_x")
    private Integer gridX;

    @Column(name = "grid_y")
    private Integer gridY;

    @Column(name = "location_names")
    private String locationNames; // 일단 JSON으로 넣을 것으로 예상

    private Profile(Gender gender, LocalDate birthDate, Integer tempSensitivity, String profileImageUrl, Double lat, Double lon, Integer gridX, Integer gridY, String locationNames) {
        this.gender = gender;
        this.birthDate = birthDate;
        this.tempSensitivity = tempSensitivity;
        this.profileImageUrl = profileImageUrl;
        this.lat = lat;
        this.lon = lon;
        this.gridX = gridX;
        this.gridY = gridY;
        this.locationNames = locationNames;
    }

    public static Profile createProfileInit() {
        return new Profile();
    }

    public static Profile createProfile(Gender gender, LocalDate birthDate, Integer tempSensitivity, String profileImageUrl, Double lat, Double lon, Integer gridX, Integer gridY, String locationNames) {
        return new Profile(gender, birthDate, tempSensitivity, profileImageUrl, lat, lon, gridX, gridY, locationNames);
    }

    // == 연관관계 편의 메서드 ==
    protected void assignUser(User user) {
        this.user = user;
    }
}
