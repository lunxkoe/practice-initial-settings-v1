package lunxkoe.practiceinitialsettingsv1.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.request.UserCreateRequest;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.response.UserDto;
import lunxkoe.practiceinitialsettingsv1.domain.user.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserDto response = authService.signUp(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
