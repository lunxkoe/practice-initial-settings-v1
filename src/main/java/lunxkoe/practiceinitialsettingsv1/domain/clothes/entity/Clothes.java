package lunxkoe.practiceinitialsettingsv1.domain.clothes.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lunxkoe.practiceinitialsettingsv1.domain.user.entity.User;
import lunxkoe.practiceinitialsettingsv1.global.entity.BaseTimeEntity;

import java.util.UUID;

@Entity
@Table(name = "clothes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clothes extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "clothes_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClothesType type;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String sourceUrl;

    private Clothes(User owner, String name, ClothesType type, String imageUrl, String sourceUrl) {
        this.owner = owner;
        this.name = name;
        this.type = type;
        this.imageUrl = imageUrl;
        this.sourceUrl = sourceUrl;
    }

    public static Clothes createClothes(User owner, String name, ClothesType type, String imageUrl, String sourceUrl) {
        return new Clothes(owner, name, type, imageUrl, sourceUrl);
    }
}
