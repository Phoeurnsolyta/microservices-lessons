package kh.edu.istad.platform.iam.domain.usecase.user;

import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordLoginAttemptUseCase {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private final UserRepository userRepository;

    public void execute(String usernameOrEmail, boolean success) {
        userRepository.findByUsernameOrEmail(
                usernameOrEmail.toLowerCase()).ifPresent(user -> {
            if (success) {
                user.recordSuccessLogin();
            } else {
                user.recordFailedLogin(MAX_FAILED_ATTEMPTS);
            }

            userRepository.save(user);
        });
    }
}
