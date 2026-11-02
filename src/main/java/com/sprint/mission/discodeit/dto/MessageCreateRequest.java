package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MessageCreateRequest {
    private final String content;
    private final UUID channelId;
    private final UUID authorId;

    // 비어 있거나 null이면 첨부파일 없이 전송
    private final List<BinaryContentCreateRequest> attachments;
}