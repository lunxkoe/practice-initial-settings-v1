package lunxkoe.practiceinitialsettingsv1.domain.user.dto.response;

import lombok.Builder;
import lunxkoe.practiceinitialsettingsv1.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserDto(
        UUID id,
        LocalDateTime createdAt,
        String email,
        String name,
        String role,
        boolean locked
) {
    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .locked(user.isLocked())
                .build();
    }
}
