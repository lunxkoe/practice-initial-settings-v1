package lunxkoe.practiceinitialsettingsv1.domain.user.service;

import lombok.RequiredArgsConstructor;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.CustomUserDetails;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.request.SignInRequest;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.request.UserCreateRequest;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.response.JwtDto;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.response.UserDto;
import lunxkoe.practiceinitialsettingsv1.domain.user.entity.Profile;
import lunxkoe.practiceinitialsettingsv1.domain.user.entity.User;
import lunxkoe.practiceinitialsettingsv1.domain.user.repository.UserRepository;
import lunxkoe.practiceinitialsettingsv1.global.exception.BusinessException;
import lunxkoe.practiceinitialsettingsv1.global.exception.ErrorCode;
import lunxkoe.practiceinitialsettingsv1.global.security.jwt.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserDto signUp(UserCreateRequest request) {
        // 이메일 중복 검증
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        // 빈 Profile 생성
        Profile newProfile = Profile.createProfileInit();

        // 회원 생성
        User newUser = User.createUser(
                request.email(),
                encodedPassword,
                request.name(),
                newProfile
        );

        User savedUser = userRepository.save(newUser);

        return UserDto.from(savedUser);
    }

    @Transactional
    public JwtDto signIn(SignInRequest request) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.username(), request.password());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authToken);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 토큰 발급
        User foundUser = userDetails.getUser();
        String accessToken = jwtProvider.createAccessToken(foundUser.getId().toString(), foundUser.getRole().name());

        return JwtDto.builder()
                .userDto(UserDto.from(foundUser))
                .accessToken(accessToken)
                .build();
    }
}
