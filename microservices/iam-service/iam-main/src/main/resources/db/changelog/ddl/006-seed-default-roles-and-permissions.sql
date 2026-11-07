-- liquibase formatted sql

INSERT INTO roles (uuid, name, description, is_system, is_active, created_by, updated_by)
VALUES
    (gen_random_uuid(), 'ADMIN', 'Super administrator with full system management permissions', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'USER', 'Default standard application user', TRUE, TRUE, 'system', 'system');


INSERT INTO permissions (uuid, name, resource, action, description, is_system, is_active, created_by, updated_by)
VALUES
    (gen_random_uuid(), 'user:create', 'user', 'create', 'Create a new user', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'user:read', 'user', 'read', 'Read user profiles and details', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'user:update', 'user', 'update', 'Update user profiles', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'user:delete', 'user', 'delete', 'Delete users', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'user:assign', 'user', 'assign', 'Assign roles and permissions to users', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'role:create', 'role', 'create', 'Create a new role', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'role:read', 'role', 'read', 'Read role details', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'role:update', 'role', 'update', 'Update role details', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'role:delete', 'role', 'delete', 'Delete roles', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'role:assign', 'role', 'assign', 'Assign permissions to roles', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'permission:create', 'permission', 'create', 'Create a new permission', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'permission:read', 'permission', 'read', 'Read permission details', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'permission:update', 'permission', 'update', 'Update permission details', TRUE, TRUE, 'system', 'system'),
    (gen_random_uuid(), 'permission:delete', 'permission', 'delete', 'Delete permissions', TRUE, TRUE, 'system', 'system');


INSERT INTO role_permissions (role_id, permission_id, assigned_by, reason)
SELECT r.id, p.id, 'system', 'Default admin permissions'
FROM roles r
         CROSS JOIN permissions p
WHERE r.name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id, assigned_by, reason)
SELECT r.id, p.id, 'system', 'Default user permissions'
FROM roles r
         JOIN permissions p ON p.name IN ('user:read')
WHERE r.name = 'USER';