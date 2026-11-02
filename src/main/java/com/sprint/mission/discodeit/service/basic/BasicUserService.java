package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto create(UserCreateRequest request) {
        // 파일 저장 전에 중복을 검사
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        UUID profileId = null;
        BinaryContentCreateRequest profile = request.getProfile();

        if (profile != null) {
            byte[] bytes = profile.getBytes();

            BinaryContent binaryContent = new BinaryContent(
                    profile.getFileName(),
                    (long) bytes.length,
                    profile.getContentType(),
                    bytes
            );

            profileId = binaryContentRepository.save(binaryContent).getId();
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                profileId
        );
        User savedUser = userRepository.save(user);

        UserStatus status = new UserStatus(savedUser.getId(), Instant.now());
        userStatusRepository.save(status);

        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserResponseDto update(UUID userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        UUID newProfileId = null;
        BinaryContentCreateRequest newProfile = request.getNewProfile();

        if (newProfile != null) {
            byte[] bytes = newProfile.getBytes();

            BinaryContent binaryContent = new BinaryContent(
                    newProfile.getFileName(),
                    (long) bytes.length,
                    newProfile.getContentType(),
                    bytes
            );
            newProfileId = binaryContentRepository.save(binaryContent).getId();

            // 이전 프로필 이미지는 삭제
            if (user.getProfileId() != null) {
                binaryContentRepository.deleteById(user.getProfileId());
            }
        }

        user.update(request.getNewUsername(), request.getNewEmail(), request.getNewPassword(), newProfileId);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        // 관련된 도메인도 같이 삭제
        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }
        userStatusRepository.deleteByUserId(userId);

        userRepository.deleteById(userId);
    }
}