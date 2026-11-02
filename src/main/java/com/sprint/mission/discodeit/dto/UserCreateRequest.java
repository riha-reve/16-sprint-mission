package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateRequest {
    private final String username;
    private final String email;
    private final String password;

    // null이면 프로필 없이 가입
    private final BinaryContentCreateRequest profile;
}