package kh.edu.istad.platform.iam.domain.usecase.role;

import kh.edu.istad.platform.iam.domain.dto.RoleResult;
import kh.edu.istad.platform.iam.domain.dto.UpdateRoleCommand;
import kh.edu.istad.platform.iam.domain.entity.Role;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateRoleUseCase {

    private final RoleRepository roleRepository;
    private final GetRoleUseCase getRoleUseCase;
    private final CurrentActorProvider currentActorProvider;

    public RoleResult execute(UpdateRoleCommand command) {

        Role role = roleRepository.findByUuid(command.roleUuid())
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IamDomainException("Role not found"));

        role.updateDescription(
                command.description(), currentActorProvider.getCurrentActorSub()
        );

        roleRepository.save(role);
        return getRoleUseCase.execute(role.getUuid());
    }
}
