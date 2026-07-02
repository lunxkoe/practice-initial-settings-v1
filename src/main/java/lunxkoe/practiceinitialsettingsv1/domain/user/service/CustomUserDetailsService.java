package lunxkoe.practiceinitialsettingsv1.domain.user.service;

import lombok.RequiredArgsConstructor;
import lunxkoe.practiceinitialsettingsv1.domain.user.dto.CustomUserDetails;
import lunxkoe.practiceinitialsettingsv1.domain.user.entity.User;
import lunxkoe.practiceinitialsettingsv1.domain.user.repository.UserRepository;
import lunxkoe.practiceinitialsettingsv1.global.exception.BusinessException;
import lunxkoe.practiceinitialsettingsv1.global.exception.ErrorCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new CustomUserDetails(foundUser);
    }
}
