package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private final UUID id;
    private final String username;
    private final String email;
    private final UUID profileId;
    private final boolean online;
    // password는 포함하지 않음
}