package kh.edu.istad.platform.iam.domain.entity;

import kh.edu.istad.platform.iam.domain.valueobject.PermissionEffect;

import java.time.Instant;

public class UserPermission {

    private Long id;
    private Long userId;
    private Long permissionId;
    private PermissionEffect effect;
    private Instant assignedAt;
    private String assignedBy;
    private Instant expiresAt;
    private Instant revokedAt;
    private String revokedBy;
    private String reason;

    private UserPermission(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.permissionId = builder.permissionId;
        this.effect = builder.effect;
        this.assignedAt = builder.assignedAt;
        this.assignedBy = builder.assignedBy;
        this.expiresAt = builder.expiresAt;
        this.revokedAt = builder.revokedAt;
        this.revokedBy = builder.revokedBy;
        this.reason = builder.reason;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isActive(Instant at) {
        if (this.revokedAt != null) return false;
        return this.expiresAt == null || this.expiresAt.isAfter(at);
    }

    public void revoke(String actorSub, String revocationReason) {
        this.revokedAt = Instant.now();
        this.revokedBy = actorSub;
        this.reason = revocationReason;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public PermissionEffect getEffect() {
        return effect;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public String getRevokedBy() {
        return revokedBy;
    }

    public String getReason() {
        return reason;
    }

    public static class Builder {
        private Long id;
        private Long userId;
        private Long permissionId;
        private PermissionEffect effect;
        private Instant assignedAt = Instant.now();
        private String assignedBy;
        private Instant expiresAt;
        private Instant revokedAt;
        private String revokedBy;
        private String reason;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder permissionId(Long permissionId) {
            this.permissionId = permissionId;
            return this;
        }

        public Builder effect(PermissionEffect effect) {
            this.effect = effect;
            return this;
        }

        public Builder assignedAt(Instant t) {
            this.assignedAt = t;
            return this;
        }

        public Builder assignedBy(String by) {
            this.assignedBy = by;
            return this;
        }

        public Builder expiresAt(Instant t) {
            this.expiresAt = t;
            return this;
        }

        public Builder revokedAt(Instant t) {
            this.revokedAt = t;
            return this;
        }

        public Builder revokedBy(String by) {
            this.revokedBy = by;
            return this;
        }

        public Builder reason(String r) {
            this.reason = r;
            return this;
        }

        public UserPermission build() {
            return new UserPermission(this);
        }
    }
}
