package kh.edu.istad.platform.iam.domain.usecase.role;

import kh.edu.istad.platform.iam.domain.dto.PageResult;
import kh.edu.istad.platform.iam.domain.dto.RoleFilterQuery;
import kh.edu.istad.platform.iam.domain.dto.RoleSummary;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListRolesUseCase {

    private final RoleRepository roleRepository;

    public PageResult<RoleSummary> execute(RoleFilterQuery query) {
        return roleRepository.findAll(query);
    }
}
