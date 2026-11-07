package kh.edu.istad.platform.iam.domain.entity;

import kh.edu.istad.platform.iam.domain.valueobject.IdentityProvider;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class Identity {

    private Long id;
    private UUID uuid;
    private Long userId;
    private IdentityProvider provider;
    private String providerSubject;
    private String email;
    private boolean emailVerified;
    private String displayName;
    private String username;
    private String pictureUrl;
    private Map<String, Object> attributes;
    private Instant linkedAt;
    private Instant lastLoginAt;
    private boolean isDeleted;
    private Instant deletedAt;
    private String deletedBy;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private Long version;

    private Identity(Builder builder) {
        this.id = builder.id;
        this.uuid = builder.uuid;
        this.userId = builder.userId;
        this.provider = builder.provider;
        this.providerSubject = builder.providerSubject;
        this.email = builder.email;
        this.emailVerified = builder.emailVerified;
        this.displayName = builder.displayName;
        this.username = builder.username;
        this.pictureUrl = builder.pictureUrl;
        this.attributes = builder.attributes;
        this.linkedAt = builder.linkedAt;
        this.lastLoginAt = builder.lastLoginAt;
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

    public void markLogin() {
        this.lastLoginAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void unlink(String actorSub) {
        this.isDeleted = true;
        this.deletedAt = Instant.now();
        this.deletedBy = actorSub;
        this.updatedAt = Instant.now();
        this.updatedBy = actorSub;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Long getUserId() {
        return userId;
    }

    public IdentityProvider getProvider() {
        return provider;
    }

    public String getProviderSubject() {
        return providerSubject;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsername() {
        return username;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Instant getLinkedAt() {
        return linkedAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
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
        private Long userId;
        private IdentityProvider provider;
        private String providerSubject;
        private String email;
        private boolean emailVerified = false;
        private String displayName;
        private String username;
        private String pictureUrl;
        private Map<String, Object> attributes;
        private Instant linkedAt;
        private Instant lastLoginAt;
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

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder provider(IdentityProvider provider) {
            this.provider = provider;
            return this;
        }

        public Builder providerSubject(String s) {
            this.providerSubject = s;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder emailVerified(boolean verified) {
            this.emailVerified = verified;
            return this;
        }

        public Builder displayName(String name) {
            this.displayName = name;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder pictureUrl(String url) {
            this.pictureUrl = url;
            return this;
        }

        public Builder attributes(Map<String, Object> attrs) {
            this.attributes = attrs;
            return this;
        }

        public Builder linkedAt(Instant t) {
            this.linkedAt = t;
            return this;
        }

        public Builder lastLoginAt(Instant t) {
            this.lastLoginAt = t;
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

        public Identity build() {
            return new Identity(this);
        }
    }
}
