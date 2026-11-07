package kh.edu.istad.platform.iam.domain.usecase.permission;

import kh.edu.istad.platform.iam.domain.dto.PageResult;
import kh.edu.istad.platform.iam.domain.dto.PermissionFilterQuery;
import kh.edu.istad.platform.iam.domain.dto.PermissionResult;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListPermissionsUseCase {

    private final PermissionRepository permissionRepository;

    public PageResult<PermissionResult> execute(PermissionFilterQuery query) {
        return permissionRepository.findAll(query);
    }
}
