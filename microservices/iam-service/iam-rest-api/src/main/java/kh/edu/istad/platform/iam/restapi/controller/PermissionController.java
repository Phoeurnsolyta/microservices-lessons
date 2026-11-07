package kh.edu.istad.platform.iam.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.iam.domain.dto.PermissionFilterQuery;
import kh.edu.istad.platform.iam.domain.dto.UpdatePermissionCommand;
import kh.edu.istad.platform.iam.domain.usecase.permission.*;
import kh.edu.istad.platform.iam.restapi.dto.request.CreatePermissionRequest;
import kh.edu.istad.platform.iam.restapi.dto.request.UpdatePermissionRequest;
import kh.edu.istad.platform.iam.restapi.dto.response.PageResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.PermissionResponse;
import kh.edu.istad.platform.iam.restapi.mapper.PermissionWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final CreatePermissionUseCase createPermissionUseCase;
    private final GetPermissionUseCase getPermissionUseCase;
    private final ListPermissionsUseCase listPermissionsUseCase;
    private final UpdatePermissionUseCase updatePermissionUseCase;
    private final DeletePermissionUseCase deletePermissionUseCase;
    private final ActivatePermissionUseCase activatePermissionUseCase;
    private final DeactivatePermissionUseCase deactivatePermissionUseCase;
    private final PermissionWebMapper permissionWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('permission:create')")
    public PermissionResponse createPermission(
            @Valid @RequestBody CreatePermissionRequest request) {
        return permissionWebMapper
                .toResponse(
                        createPermissionUseCase.execute(permissionWebMapper.toCommand(request))
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('permission:read')")
    public PageResponse<PermissionResponse> listPermissions(
            @RequestParam(required = false) String resource,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        return permissionWebMapper
                .toPageResponse(listPermissionsUseCase
                        .execute(new PermissionFilterQuery(resource, search, page, size, sort))
                );
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('permission:read')")
    public PermissionResponse getPermission(@PathVariable UUID uuid) {
        return permissionWebMapper.toResponse(getPermissionUseCase.execute(uuid));
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasAuthority('permission:update')")
    public PermissionResponse updatePermission(
            @PathVariable UUID uuid, @Valid @RequestBody UpdatePermissionRequest request) {

        return permissionWebMapper.toResponse(
                updatePermissionUseCase.execute(
                        new UpdatePermissionCommand(uuid, request.description()))
        );
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('permission:delete')")
    public void deletePermission(@PathVariable UUID uuid) {
        deletePermissionUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('permission:update')")
    public void activatePermission(@PathVariable UUID uuid) {
        activatePermissionUseCase.execute(uuid);
    }

    @PostMapping("/{uuid}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('permission:update')")
    public void deactivatePermission(@PathVariable UUID uuid) {
        deactivatePermissionUseCase.execute(uuid);
    }
}
