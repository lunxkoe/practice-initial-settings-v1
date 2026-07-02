package lunxkoe.practiceinitialsettingsv1.domain.user.service;

import lunxkoe.practiceinitialsettingsv1.domain.user.dto.CustomUserDetails;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.request.SignInRequest;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.request.UserCreateRequest;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.response.UserDto;
import lunxkoe.practiceinitialsettingsv1.domain.user.entity.User;
import lunxkoe.practiceinitialsettingsv1.domain.user.repository.UserRepository;
import lunxkoe.practiceinitialsettingsv1.global.exception.BusinessException;
import lunxkoe.practiceinitialsettingsv1.global.exception.ErrorCode;
import lunxkoe.practiceinitialsettingsv1.global.security.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입을 성공한다.")
    void signUpSuccess() {
        // given
        UserCreateRequest request = new UserCreateRequest("홍길동", "test@test.com", "password123!");

        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

        User savedUser = User.createUser(request.email(), request.password(), request.name(), null);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        UserDto response = authService.signUp(request);

        // then
        assertThat(response.email()).isEqualTo(request.email());
        assertThat(response.name()).isEqualTo(request.name());
        verify(userRepository).save(any(User.class)); // save 호출 여부 검증
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입 시 BusinessException이 발생한다.")
    void signUpFailDuplicatedEmail() {
        // given
        UserCreateRequest request = new UserCreateRequest("홍길동", "test@test.com", "password123!");
        given(userRepository.existsByEmail(anyString())).willReturn(true); // 이메일 중복 모킹

        // when & then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("로그인을 성공하고 JWT 토큰을 발급받는다.")
    void signInSuccess() {
        // given
        SignInRequest request = new SignInRequest("test@test.com", "password123!");

        // 가짜 유저 객체 생성
        User user = User.createUser("test@test.com", "encodedPassword", "홍길동", null);

        // ReflectionTestUtils를 사용해 강제로 가짜 UUID를 주입
        org.springframework.test.util.ReflectionTestUtils.setField(user, "id", java.util.UUID.randomUUID());

        // 시큐리티 인증 결과를 흉내 낼 CustomUserDetails 및 Authentication 객체 생성
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        // Mocking: authenticationManager가 호출되면 만들어둔 authentication 객체를 반환하도록 설정
        given(authenticationManager.authenticate(any())).willReturn(authentication);

        // Mocking: jwtProvider가 호출되면 가짜 토큰을 반환하도록 설정
        given(jwtProvider.createAccessToken(anyString(), anyString())).willReturn("mock.access.token");

        // when
        lunxkoe.practiceinitialsettingsv1.domain.user.dto.response.JwtDto response = authService.signIn(request);

        // then
        assertThat(response.accessToken()).isEqualTo("mock.access.token");
        assertThat(response.userDto().email()).isEqualTo("test@test.com");

        // 검증: 로직 내에서 매니저와 프로바이더가 정말로 호출되었는지 확인
        verify(authenticationManager).authenticate(any());
        verify(jwtProvider).createAccessToken(anyString(), anyString());
    }

    @Test
    @DisplayName("아이디나 비밀번호가 틀리면 BusinessException(INVALID_USERNAME_OR_PASSWORD)이 발생한다.")
    void signInFail() {
        // given
        SignInRequest request = new SignInRequest("test@test.com", "wrongPassword");

        // Mocking: authenticationManager가 예외(BadCredentialsException 등)를 던지도록 설정
        given(authenticationManager.authenticate(any()))
                .willThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        // when & then
        assertThatThrownBy(() -> authService.signIn(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.INVALID_USERNAME_OR_PASSWORD.getMessage());
    }

}