package kh.edu.istad.platform.iam.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.iam.domain.dto.AssignPermissionsToRoleCommand;
import kh.edu.istad.platform.iam.domain.dto.PermissionAssignmentResult;
import kh.edu.istad.platform.iam.domain.dto.RoleFilterQuery;
import kh.edu.istad.platform.iam.domain.dto.UpdateRoleCommand;
import kh.edu.istad.platform.iam.domain.usecase.role.*;
import kh.edu.istad.platform.iam.restapi.dto.request.AssignPermissionsToRoleRequest;
import kh.edu.istad.platform.iam.restapi.dto.request.CreateRoleRequest;
import kh.edu.istad.platform.iam.restapi.dto.request.UpdateRoleRequest;
import kh.edu.istad.platform.iam.restapi.dto.response.PageResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.RoleResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.RoleSummaryResponse;
import kh.edu.istad.platform.iam.restapi.mapper.RoleWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final CreateRoleUseCase createRoleUseCase;
    private final GetRoleUseCase getRoleUseCase;
    private final ListRolesUseCase listRolesUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final ActivateRoleUseCase activateRoleUseCase;
    private final DeactivateRoleUseCase deactivateRoleUseCase;
    private final AssignPermissionsToRoleUseCase assignPermissionsToRoleUseCase;
    private final RevokePermissionFromRoleUseCase revokePermissionFromRoleUseCase;
    private final RoleWebMapper roleWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('role:create')")
    public RoleResponse createRole(
            @Valid @RequestBody CreateRoleRequest request) {
        return roleWebMapper
                .toResponse(
                        createRoleUseCase.execute(roleWebMapper.toCommand(request))
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('role:read')")
    public PageResponse<RoleSummaryResponse> listRoles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        return roleWebMapper.toPageResponse(
                listRolesUseCase.execute(
                        new RoleFilterQuery(search, active, page, size, sort))
        );
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('role:read')")
    public RoleResponse getRole(@PathVariable UUID uuid) {
        return roleWebMapper.toResponse(getRoleUseCase.execute(uuid));
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasAuthority('role:update')")
    public RoleResponse updateRole(
            @PathVariable UUID uuid, @Valid @RequestBody UpdateRoleRequest request) {
        return roleWebMapper.toResponse(
                updateRoleUseCase.execute(
                        new UpdateRoleCommand(uuid, request.description()))
        );
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role:delete')")
    public void deleteRole(@PathVariable UUID uuid) {
        deleteRoleUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role:update')")
    public void activateRole(@PathVariable UUID uuid) {
        activateRoleUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role:update')")
    public void deactivateRole(@PathVariable UUID uuid) {
        deactivateRoleUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/permissions")
    @PreAuthorize("hasAuthority('role:assign')")
    public List<PermissionAssignmentResult> assignPermissions(
            @PathVariable UUID uuid,
            @Valid @RequestBody AssignPermissionsToRoleRequest request
    ) {
        return assignPermissionsToRoleUseCase
                .execute(new AssignPermissionsToRoleCommand(
                        uuid,
                        request.permissionUuids(),
                        request.expiresAt(),
                        request.reason()
        ));
    }

    @DeleteMapping("/{uuid}/permissions/{permissionUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role:assign')")
    public void revokePermission(
            @PathVariable UUID uuid,
            @PathVariable UUID permissionUuid,
            @RequestParam(required = false, defaultValue = "Revoked by administrator") String reason
    ) {
        revokePermissionFromRoleUseCase.execute(uuid, permissionUuid, reason);
    }
}


