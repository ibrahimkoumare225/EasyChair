package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.RoleRequest;
import fr.upsaclay.easychair.model.enumates.RoleType;

import java.util.List;
import java.util.Optional;

public interface RoleRequestService {
    RoleRequest save(RoleRequest roleRequest);
    Optional<RoleRequest> findOne(Long id);
    List<RoleRequest> findByUserId(Long userId);
    List<RoleRequest> findByConferenceId(Long conferenceId);
    List<RoleRequest> findByConferenceIdAndStatus(Long conferenceId, String status);
    void delete(Long id);
    void deleteByConferenceId(Long conferenceId);
    Optional<RoleRequest> findByUserIdAndConferenceIdAndRoleType(Long userId, Long conferenceId, RoleType roleType);
}