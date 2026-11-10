package kh.edu.istad.platform.iam;

import kh.edu.istad.platform.iam.domain.dto.RegisterUserCommand;
import kh.edu.istad.platform.iam.domain.dto.UserResult;
import kh.edu.istad.platform.iam.domain.entity.Role;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.PasswordHasher;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import kh.edu.istad.platform.iam.domain.usecase.user.RegisterUserUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Test
    @DisplayName("Password mismatch throws IamDomainException")
    void register_shouldThrowException_whenPasswordMismatch() {
        RegisterUserCommand command = new RegisterUserCommand(
                "choco", "choco@gmail.com", "Str0ng!Pass2026", "Different!Pass2026", "Cho", "Co", "km"
        );

        IamDomainException ex = assertThrows(IamDomainException.class, () -> registerUserUseCase.execute(command));
        assertThat(ex.getMessage()).isEqualTo("Password and confirmation password do not match");
    }

    @Test
    @DisplayName("Duplicate username throws IamDomainException")
    void register_shouldThrowException_whenUsernameAlreadyExists() {
        RegisterUserCommand command = new RegisterUserCommand(
                "choco", "new@istad.co", "Str0ng!Pass2026", "Str0ng!Pass2026", "Cho", "Co", "km"
        );

        when(userRepository.existsByUsername("choco")).thenReturn(true);

        IamDomainException ex = assertThrows(IamDomainException.class, () -> registerUserUseCase.execute(command));
        assertThat(ex.getMessage()).isEqualTo("Username is already taken");
    }

    @Test
    @DisplayName("Duplicate email throws IamDomainException")
    void register_shouldThrowException_whenEmailAlreadyExists() {
        RegisterUserCommand command = new RegisterUserCommand(
                "geto", "geto@gmail.com", "Str0ng!Pass2026", "Str0ng!Pass2026", "Suguru", "Geto", "km"
        );

        when(userRepository.existsByUsername("geto")).thenReturn(false);
        when(userRepository.existsByEmail("geto@gmail.com")).thenReturn(true);

        IamDomainException ex = assertThrows(IamDomainException.class, () -> registerUserUseCase.execute(command));
        assertThat(ex.getMessage()).isEqualTo("Email is already registered");
    }

    @Test
    @DisplayName("Valid registration encodes password, saves user, and assigns default USER role")
    void register_shouldSucceed_whenDataIsValid() {
        RegisterUserCommand command = new RegisterUserCommand(
                "choco", "choco@gmail.com", "Str0ng!Pass2026", "Str0ng!Pass2026", "Cho", "Co", "km"
        );

        when(userRepository.existsByUsername("choco")).thenReturn(false);
        when(userRepository.existsByEmail("choco@gmail.com")).thenReturn(false);
        when(passwordHasher.encode("Str0ng!Pass2026")).thenReturn("$2a$10$encodedHash");

        User savedUser = User.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .username("choco")
                .email("choco@gmail.com")
                .givenName("Cho")
                .familyName("Co")
                .locale("km")
                .enabled(true)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Role userRole = Role.builder()
                .id(2L)
                .uuid(UUID.randomUUID())
                .name("USER")
                .description("Standard user")
                .isActive(true)
                .build();
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        // When
        UserResult result = registerUserUseCase.execute(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("choco");
        assertThat(result.email()).isEqualTo("choco@gmail.com");
        assertThat(result.roles()).hasSize(1);
        assertThat(result.roles().get(0).name()).isEqualTo("USER");

        verify(userRepository).save(any(User.class));
        verify(userRepository).saveUserRole(any());
    }
}