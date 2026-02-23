package com.nlweb.user.repository;

import com.nlweb.user.entity.User;
import com.nlweb.user.enums.UserSessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /** id로 찾기 */
    Optional<User> findById(UUID id);

    /** username으로 찾기 */
    Optional<User> findByUsername(String username);

    /** email로 찾기 */
    Optional<User> findByEmail(String email);

    /** 기수로 찾기 */
    List<User> findByBatch(Integer batch);

    /** 세션으로 찾기 */
    List<User> findBySession(UserSessionType session);

    /** 세션과 기수로 찾기 */
    List<User> findBySessionAndBatch(UserSessionType session, Integer batch);

    /** username or email로 찾기 - 로그인용 */
    @Query("select u from User u where u.username = :identifier or u.email = :identifier")
    Optional<User> findByIdentifier(@Param("identifier") String identifier);

}
