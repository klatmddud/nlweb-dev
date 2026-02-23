package com.nlweb.user.enums;

import lombok.Getter;

@Getter
public enum UserStatus {

    ACTIVE("활동중"),
    INACTIVE("비활동중"),
    SUSPENDED("정지됨"),
    DELETED("탈퇴됨");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public static UserStatus fromString(String statusStr) {
        if (statusStr == null) {
            return ACTIVE;
        }

        for (UserStatus status : UserStatus.values()) {
            if (status.name().equalsIgnoreCase(statusStr)) {
                return status;
            }
        }

        return ACTIVE;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isInactive() {
        return this == INACTIVE;
    }

    public boolean isSuspended() {
        return this == SUSPENDED;
    }

    public boolean isDeleted() {
        return this == DELETED;
    }

}
