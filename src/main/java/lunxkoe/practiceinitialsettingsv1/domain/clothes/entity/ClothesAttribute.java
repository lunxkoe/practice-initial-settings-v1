package lunxkoe.practiceinitialsettingsv1.domain.clothes.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
        name = "clothes_attrs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"clothes_id", "def_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothesAttribute {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "clothes_attrs_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothes_id", nullable = false)
    private Clothes clothes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "def_id", nullable = false)
    private ClothesAttributeDef definition;

    @Column(name = "attr_value", nullable = false)
    private String value;

    private ClothesAttribute(Clothes clothes, ClothesAttributeDef definition, String value) {
        this.clothes = clothes;
        this.definition = definition;
        this.value = value;
    }

    public static ClothesAttribute createAttribute(Clothes clothes, ClothesAttributeDef definition, String value) {
        return new ClothesAttribute(clothes, definition, value);
    }
}
