package kh.edu.istad.platform.iam.domain.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class User {

    private Long id;
    private UUID uuid;
    private String username;
    private String email;
    private boolean emailVerified;
    private String passwordHash;
    private String givenName;
    private String familyName;
    private String middleName;
    private String nickname;
    private String gender;
    private LocalDate birthdate;
    private String profileUrl;
    private String pictureUrl;
    private String websiteUrl;
    private String zoneinfo;
    private String locale;
    private String phoneNumber;
    private boolean phoneNumberVerified;
    private String addressStreetAddress;
    private String addressLocality;
    private String addressRegion;
    private String addressPostalCode;
    private String addressCountry;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private int failedLoginAttempts;
    private Instant lastLoginAt;
    private Instant passwordChangedAt;
    private boolean isDeleted;
    private Instant deletedAt;
    private String deletedBy;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private Long version;

    private User(Builder builder) {
        this.id = builder.id;
        this.uuid = builder.uuid;
        this.username = builder.username;
        this.email = builder.email != null ? builder.email.toLowerCase() : null;
        this.emailVerified = builder.emailVerified;
        this.passwordHash = builder.passwordHash;
        this.givenName = builder.givenName;
        this.familyName = builder.familyName;
        this.middleName = builder.middleName;
        this.nickname = builder.nickname;
        this.gender = builder.gender;
        this.birthdate = builder.birthdate;
        this.profileUrl = builder.profileUrl;
        this.pictureUrl = builder.pictureUrl;
        this.websiteUrl = builder.websiteUrl;
        this.zoneinfo = builder.zoneinfo;
        this.locale = builder.locale != null ? builder.locale : "en";
        this.phoneNumber = builder.phoneNumber;
        this.phoneNumberVerified = builder.phoneNumberVerified;
        this.addressStreetAddress = builder.addressStreetAddress;
        this.addressLocality = builder.addressLocality;
        this.addressRegion = builder.addressRegion;
        this.addressPostalCode = builder.addressPostalCode;
        this.addressCountry = builder.addressCountry;
        this.enabled = builder.enabled;
        this.accountNonExpired = builder.accountNonExpired;
        this.accountNonLocked = builder.accountNonLocked;
        this.credentialsNonExpired = builder.credentialsNonExpired;
        this.failedLoginAttempts = builder.failedLoginAttempts;
        this.lastLoginAt = builder.lastLoginAt;
        this.passwordChangedAt = builder.passwordChangedAt;
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

    public String getFullName() {
        if (givenName == null && familyName == null) return username;
        if (givenName == null) return familyName;
        if (familyName == null) return givenName;
        return givenName + " " + familyName;
    }

    public boolean hasPassword() {
        return passwordHash != null && !passwordHash.isBlank();
    }

    public void recordSuccessLogin() {
        this.failedLoginAttempts = 0;
        this.lastLoginAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void recordFailedLogin(int maxThreshold) {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= maxThreshold) {
            this.accountNonLocked = false;
        }
        this.updatedAt = Instant.now();
    }

    public void changePassword(String newPasswordHash, String actorSub) {
        this.passwordHash = newPasswordHash;
        this.passwordChangedAt = Instant.now();
        this.updatedAt = Instant.now();
        this.updatedBy = actorSub;
    }

    public void softDelete(String actorSub) {
        this.isDeleted = true;
        this.deletedAt = Instant.now();
        this.deletedBy = actorSub;
        this.enabled = false;
        this.updatedAt = Instant.now();
        this.updatedBy = actorSub;
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getZoneinfo() {
        return zoneinfo;
    }

    public String getLocale() {
        return locale;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public String getAddressStreetAddress() {
        return addressStreetAddress;
    }

    public String getAddressLocality() {
        return addressLocality;
    }

    public String getAddressRegion() {
        return addressRegion;
    }

    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public Instant getPasswordChangedAt() {
        return passwordChangedAt;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public static class Builder {
        private Long id;
        private UUID uuid;
        private String username;
        private String email;
        private boolean emailVerified;
        private String passwordHash;
        private String givenName;
        private String familyName;
        private String middleName;
        private String nickname;
        private String gender;
        private LocalDate birthdate;
        private String profileUrl;
        private String pictureUrl;
        private String websiteUrl;
        private String zoneinfo;
        private String locale = "en";
        private String phoneNumber;
        private boolean phoneNumberVerified;
        private String addressStreetAddress;
        private String addressLocality;
        private String addressRegion;
        private String addressPostalCode;
        private String addressCountry;
        private boolean enabled = true;
        private boolean accountNonExpired = true;
        private boolean accountNonLocked = true;
        private boolean credentialsNonExpired = true;
        private int failedLoginAttempts = 0;
        private Instant lastLoginAt;
        private Instant passwordChangedAt;
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

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder emailVerified(boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }

        public Builder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder givenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        public Builder familyName(String familyName) {
            this.familyName = familyName;
            return this;
        }

        public Builder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder birthdate(LocalDate birthdate) {
            this.birthdate = birthdate;
            return this;
        }

        public Builder profileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
            return this;
        }

        public Builder pictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
            return this;
        }

        public Builder websiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
            return this;
        }

        public Builder zoneinfo(String zoneinfo) {
            this.zoneinfo = zoneinfo;
            return this;
        }

        public Builder locale(String locale) {
            this.locale = locale;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder phoneNumberVerified(boolean verified) {
            this.phoneNumberVerified = verified;
            return this;
        }

        public Builder addressStreetAddress(String s) {
            this.addressStreetAddress = s;
            return this;
        }

        public Builder addressLocality(String l) {
            this.addressLocality = l;
            return this;
        }

        public Builder addressRegion(String r) {
            this.addressRegion = r;
            return this;
        }

        public Builder addressPostalCode(String p) {
            this.addressPostalCode = p;
            return this;
        }

        public Builder addressCountry(String c) {
            this.addressCountry = c;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder accountNonExpired(boolean v) {
            this.accountNonExpired = v;
            return this;
        }

        public Builder accountNonLocked(boolean v) {
            this.accountNonLocked = v;
            return this;
        }

        public Builder credentialsNonExpired(boolean v) {
            this.credentialsNonExpired = v;
            return this;
        }

        public Builder failedLoginAttempts(int attempts) {
            this.failedLoginAttempts = attempts;
            return this;
        }

        public Builder lastLoginAt(Instant t) {
            this.lastLoginAt = t;
            return this;
        }

        public Builder passwordChangedAt(Instant t) {
            this.passwordChangedAt = t;
            return this;
        }

        public Builder isDeleted(boolean deleted) {
            this.isDeleted = deleted;
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

        public User build() {
            return new User(this);
        }
    }
}
