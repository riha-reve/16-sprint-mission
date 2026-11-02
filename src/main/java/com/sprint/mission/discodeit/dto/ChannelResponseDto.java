package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelResponseDto {
    private final UUID id;
    private final ChannelType type;
    private final String name;
    private final String description;
    // 가장 최근 메시지의 시간 (메시지가 없으면 null)
    private final Instant lastMessageAt;
    // PRIVATE 채널일 때만 참여자 id가 들어감 (PUBLIC이면 빈 리스트)
    private final List<UUID> participantIds;
}