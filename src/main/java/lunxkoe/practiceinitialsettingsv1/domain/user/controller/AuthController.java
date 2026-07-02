package lunxkoe.practiceinitialsettingsv1.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.request.SignInRequest;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.response.JwtDto;
import lunxkoe.practiceinitialsettingsv1.domain.user.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtDto> signIn(@Valid @ModelAttribute SignInRequest request) {
        JwtDto response = authService.signIn(request);
        // Refresh Token 로직(쿠키 등)은 추후 추가
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
