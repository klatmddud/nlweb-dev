package com.nlweb.user.facade;

import com.nlweb.user.dto.request.UpdateUserRequest;
import com.nlweb.user.service.*;
import com.nlweb.user.dto.response.UserResponse;
import com.nlweb.user.entity.User;
import com.nlweb.user.enums.UserSessionType;
import com.nlweb.user.log.UserLogger;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFacade {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    /** username으로 프로필 조회 */
    public UserResponse getProfile(UUID userId, String username) {
        try {
            User user = userQueryService.getById(userId);

            UserLogger.logSuccessUserEvent("get_user_profile", userId, username, null);
            
            return UserResponse.forProfile(user);
            
        } catch (Exception e) {
            UserLogger.logFailureUserEvent("get_user_profile", userId, username, e, null);
            throw e;
        }
    }

    /** 사용자 정보 업데이트 */
    @Transactional
    public UserResponse updateProfile(UUID userId, String username, UpdateUserRequest request) {
        try {
            User updatedUser = userCommandService.update(userId, request);

            UserLogger.logSuccessUserEvent("update_user_profile", userId, username, null);
            
            return UserResponse.forProfile(updatedUser);
            
        } catch (Exception e) {
            UserLogger.logFailureUserEvent("update_user_profile", userId, username, e, null);
            throw e;
        }
    }

    /** 사용자 프로필 삭제 */
    @Transactional
    public UserResponse deleteUser(UUID userId, String username) {
        try {
            User user = userCommandService.delete(userId);

            UserLogger.logSuccessUserEvent("soft_delete_user", userId, username, null);

            return UserResponse.forProfile(user);
        } catch (Exception e) {
            UserLogger.logFailureUserEvent("soft_delete_user", userId, username, e, null);
            throw e;
        }
    }

    /** 세션과 기수로 사용자 목록 조회 */
    public List<UserResponse> searchUsers(UserSessionType session, Integer batch, UUID searcherId, String searcherUsername) {
        Map<String, Object> details = createSearchLogDetails(session, batch);
        try {
            List<User> users = userQueryService.getBySessionAndBatch(session, batch);
            
            UserLogger.logSuccessUserEvent("search_users", searcherId, searcherUsername, details);
            
            return users.stream()
                    .map(UserResponse::forPublic)
                    .toList();
                    
        } catch (Exception e) {
            UserLogger.logFailureUserEvent("search_users", searcherId, searcherUsername, e, details);
            throw e;
        }
    }

    private Map<String, Object> createSearchLogDetails(UserSessionType session, Integer batch) {
        Map<String, Object> details = new HashMap<>();
        details.put("session", session != null ? session.toString() : null);
        details.put("batch", batch);
        return details;
    }

}
