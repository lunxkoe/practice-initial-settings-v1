package lunxkoe.practiceinitialsettingsv1.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.request.UserCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test") // H2 등 테스트용 DB 프로필 적용
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("유효하지 않은 입력값으로 회원가입 시 400 에러와 필드별 예외 메시지를 반환한다.")
    void signUpFailWithInvalidInput() throws Exception {
        // given: 이메일 형식이 틀리고, 비밀번호가 8자 미만인 잘못된 요청
        UserCreateRequest request = new UserCreateRequest("홍길동", "invalid-email", "short");

        // when & then: @Valid 검증에 실패하여 GlobalExceptionHandler가 처리해야 함
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exceptionName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.details.email").exists())
                .andExpect(jsonPath("$.details.password").exists());
    }

    @Test
    @DisplayName("존재하지 않는 계정으로 로그인 시 BusinessException 에러 응답을 반환한다.")
    void signInFailWithWrongUser() throws Exception {
        // when & then: 컨트롤러에 @ModelAttribute로 바인딩되므로 multipart form-data 형식으로 전송
        mockMvc.perform(multipart("/api/auth/sign-in")
                        .param("username", "notfound@test.com")
                        .param("password", "wrongPassword"))
                // 에러 코드를 BAD_REQUEST(400) 또는 UNAUTHORIZED(401) 중 무엇으로 설정했는지에 따라 is4xxClientError() 로 유연하게 검증
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.exceptionName").value("BusinessException"))
                .andExpect(jsonPath("$.message").exists());
    }
}