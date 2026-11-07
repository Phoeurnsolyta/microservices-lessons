
CREATE TABLE user_roles
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    user_id     BIGINT                              NOT NULL,
    role_id     BIGINT                              NOT NULL,
    assigned_at TIMESTAMPTZ                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_by VARCHAR(100)                        NOT NULL,
    expires_at  TIMESTAMPTZ,
    revoked_at  TIMESTAMPTZ,
    revoked_by  VARCHAR(100),
    reason      VARCHAR(255),
    CONSTRAINT pk_user_roles PRIMARY KEY (id),
    CONSTRAINT fk_user_roles_on_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_on_role FOREIGN KEY (role_id) REFERENCES roles (id)
);


CREATE TABLE role_permissions
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    role_id       BIGINT                              NOT NULL,
    permission_id BIGINT                              NOT NULL,
    assigned_at   TIMESTAMPTZ                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_by   VARCHAR(100)                        NOT NULL,
    expires_at    TIMESTAMPTZ,
    revoked_at    TIMESTAMPTZ,
    revoked_by    VARCHAR(100),
    reason        VARCHAR(255),
    CONSTRAINT pk_role_permissions PRIMARY KEY (id),
    CONSTRAINT fk_role_permissions_on_role FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT fk_role_permissions_on_permission FOREIGN KEY (permission_id) REFERENCES permissions (id)
);


CREATE TABLE user_permissions
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    user_id       BIGINT                              NOT NULL,
    permission_id BIGINT                              NOT NULL,
    effect        VARCHAR(5)                          NOT NULL, -- GRANT or DENY
    assigned_at   TIMESTAMPTZ                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_by   VARCHAR(100)                        NOT NULL,
    expires_at    TIMESTAMPTZ,
    revoked_at    TIMESTAMPTZ,
    revoked_by    VARCHAR(100),
    reason        VARCHAR(255),
    CONSTRAINT pk_user_permissions PRIMARY KEY (id),
    CONSTRAINT fk_user_permissions_on_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_permissions_on_permission FOREIGN KEY (permission_id) REFERENCES permissions (id)
);

CREATE INDEX idx_user_roles_user_active ON user_roles (user_id) WHERE revoked_at IS NULL;
CREATE INDEX idx_role_perms_role_active ON role_permissions (role_id) WHERE revoked_at IS NULL;
CREATE INDEX idx_user_perms_user_active ON user_permissions (user_id) WHERE revoked_at IS NULL;