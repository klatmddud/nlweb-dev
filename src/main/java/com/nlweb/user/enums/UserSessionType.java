package com.nlweb.user.enums;

import lombok.Getter;

@Getter
public enum UserSessionType {
    VOCAL("보컬"),
    GUITAR("기타"),
    BASS("베이스"),
    DRUM("드럼"),
    KEYBOARD("키보드"),
    NONE("없음");

    private final String description;

    UserSessionType(String description) {
        this.description = description;
    }

    public static UserSessionType fromString(String sessionStr) {
        if (sessionStr == null) {
            return NONE;
        }

        for (UserSessionType userSessionType : UserSessionType.values()) {
            if (userSessionType.name().equalsIgnoreCase(sessionStr)
                    || userSessionType.description.equalsIgnoreCase(sessionStr)) {
                return userSessionType;
            }
        }

        return NONE;
    }

    public boolean isVocal() {
        return this.equals(UserSessionType.VOCAL);
    }
}
