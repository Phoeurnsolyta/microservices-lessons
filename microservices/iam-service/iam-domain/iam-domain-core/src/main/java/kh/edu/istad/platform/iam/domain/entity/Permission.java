package kh.edu.istad.platform.iam.domain.entity;

import kh.edu.istad.platform.iam.domain.exception.SystemResourceImmutableException;

import java.time.Instant;
import java.util.UUID;

public class Permission {

    private Long id;
    private UUID uuid;
    private String name;
    private String resource;
    private String action;
    private String description;
    private boolean isSystem;
    private boolean isActive;
    private boolean isDeleted;
    private Instant deletedAt;
    private String deletedBy;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private Long version;

    private Permission(Builder builder) {
        this.id = builder.id;
        this.uuid = builder.uuid;
        this.name = builder.name;
        this.resource = builder.resource;
        this.action = builder.action;
        this.description = builder.description;
        this.isSystem = builder.isSystem;
        this.isActive = builder.isActive;
        this.isDeleted = builder.isDeleted;
        this.deletedAt = builder.deletedAt;
        this.deletedBy = builder.deletedBy;
        this.createdAt = builder.createdAt;
        this.createdBy = builder.createdBy;
        this.updatedAt = builder.updatedAt;
        this.updatedBy = builder.updatedBy;
        this.version = builder.version;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static String computeName(String resource, String action) {
        return (resource.trim() + ":" + action.trim()).toLowerCase();
    }

    public void validateModifiable() {
        if (this.isSystem) {
            throw new SystemResourceImmutableException("Permission", this.name);
        }
    }

    public void softDelete(String actorSub) {
        validateModifiable();
        this.isDeleted = true;
        this.isActive = false;
        this.deletedAt = Instant.now();
        this.deletedBy = actorSub;
        this.updatedAt = Instant.now();
        this.updatedBy = actorSub;
    }

    public void updateDescription(String newDescription, String actorSub) {
        validateModifiable();
        this.description = newDescription;
        this.updatedAt = Instant.now();
        this.updatedBy = actorSub;
    }


    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Long getVersion() {
        return version;
    }

    public static class Builder {
        private Long id;
        private UUID uuid;
        private String name;
        private String resource;
        private String action;
        private String description;
        private boolean isSystem = false;
        private boolean isActive = true;
        private boolean isDeleted = false;
        private Instant deletedAt;
        private String deletedBy;
        private Instant createdAt;
        private String createdBy;
        private Instant updatedAt;
        private String updatedBy;
        private Long version = 0L;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder resource(String r) {
            this.resource = r;
            return this;
        }

        public Builder action(String a) {
            this.action = a;
            return this;
        }

        public Builder description(String d) {
            this.description = d;
            return this;
        }

        public Builder isSystem(boolean s) {
            this.isSystem = s;
            return this;
        }

        public Builder isActive(boolean a) {
            this.isActive = a;
            return this;
        }

        public Builder isDeleted(boolean d) {
            this.isDeleted = d;
            return this;
        }

        public Builder deletedAt(Instant t) {
            this.deletedAt = t;
            return this;
        }

        public Builder deletedBy(String by) {
            this.deletedBy = by;
            return this;
        }

        public Builder createdAt(Instant t) {
            this.createdAt = t;
            return this;
        }

        public Builder createdBy(String by) {
            this.createdBy = by;
            return this;
        }

        public Builder updatedAt(Instant t) {
            this.updatedAt = t;
            return this;
        }

        public Builder updatedBy(String by) {
            this.updatedBy = by;
            return this;
        }

        public Builder version(Long version) {
            this.version = version;
            return this;
        }

        public Permission build() {
            return new Permission(this);
        }
    }
}
