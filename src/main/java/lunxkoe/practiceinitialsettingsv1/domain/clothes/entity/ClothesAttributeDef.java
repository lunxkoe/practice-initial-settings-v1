package lunxkoe.practiceinitialsettingsv1.domain.clothes.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lunxkoe.practiceinitialsettingsv1.global.entity.BaseTimeEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "attribute_defs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothesAttributeDef extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "attribute_defs_id")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    // Hibernate 6 (Spring Boot 3+) JSON 매핑 기능 활용
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "selectable_values", columnDefinition = "json")
    private List<String> selectableValues = new ArrayList<>();

    private ClothesAttributeDef(String name, List<String> selectableValues) {
        this.name = name;
        this.selectableValues = selectableValues;
    }

    public static ClothesAttributeDef createDefinition(String name, List<String> selectableValues) {
        return new ClothesAttributeDef(name, selectableValues);
    }
}
