package com.nameslowly.coinauctions.common.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public enum UserRole {

    MASTER("ROLE_MASTER"), // 마스터
    USER("ROLE_USER"), // 경매 등록, 경매 입찰
    GUEST("ROLE_GUEST"); // 구경

    private final String authority;

    // Secured 처리시 하드코딩을 줄이기 위한 클래스
    public static class Authority {

        public static final String MASTER = "ROLE_MASTER";
        public static final String USER = "ROLE_USER";
        public static final String GUEST = "ROLE_GUEST";

    }

}
