package kh.edu.istad.platform.iam.domain.usecase.user;

import kh.edu.istad.platform.iam.domain.dto.ChangePasswordCommand;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.PasswordHasher;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final CurrentActorProvider currentActorProvider;

    public void execute(ChangePasswordCommand command) {

        User user = userRepository.findByUuid(command.userUuid())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IamDomainException("User not found"));

        if (!passwordHasher.matches(command.currentPassword(),
                user.getPasswordHash())) {
            throw new IamDomainException("Current password is incorrect");
        }

        if (!command.newPassword().equals(command.confirmPassword())) {
            throw new IamDomainException("New password and confirm password do not match");
        }

        String actor = currentActorProvider.getCurrentActorSub();
        user.changePassword(passwordHasher.encode(command.newPassword()), actor);

        userRepository.save(user);
    }
}
