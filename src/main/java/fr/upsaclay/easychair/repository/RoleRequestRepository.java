package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.RoleRequest;
import fr.upsaclay.easychair.model.enumates.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRequestRepository extends JpaRepository<RoleRequest, Long> {
    List<RoleRequest> findByUserId(Long userId);
    List<RoleRequest> findByConferenceId(Long conferenceId);
    List<RoleRequest> findByConferenceIdAndStatus(Long conferenceId, String status);
    Optional<RoleRequest> findByUserIdAndConferenceIdAndRoleType(Long userId, Long conferenceId, RoleType roleType);

    void deleteByConferenceId(Long conferenceId);}