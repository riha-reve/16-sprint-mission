package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequest {
    private final String newUsername;
    private final String newEmail;
    private final String newPassword;

    // null이면 프로필 이미지는 바꾸지 않음
    private final BinaryContentCreateRequest newProfile;
}