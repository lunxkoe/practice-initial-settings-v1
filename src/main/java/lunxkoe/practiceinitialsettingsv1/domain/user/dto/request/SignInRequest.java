package lunxkoe.practiceinitialsettingsv1.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        String password
) {}
