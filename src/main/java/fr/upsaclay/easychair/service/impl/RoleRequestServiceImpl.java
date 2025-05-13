package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.RoleRequest;
import fr.upsaclay.easychair.model.enumates.RoleType;
import fr.upsaclay.easychair.repository.RoleRequestRepository;
import fr.upsaclay.easychair.service.RoleRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleRequestServiceImpl implements RoleRequestService {

    private final RoleRequestRepository roleRequestRepository;

    @Override
    public RoleRequest save(RoleRequest roleRequest) {
        return roleRequestRepository.save(roleRequest);
    }

    @Override
    public Optional<RoleRequest> findOne(Long id) {
        return roleRequestRepository.findById(id);
    }

    @Override
    public List<RoleRequest> findByUserId(Long userId) {
        return roleRequestRepository.findByUserId(userId);
    }

    @Override
    public List<RoleRequest> findByConferenceId(Long conferenceId) {
        return roleRequestRepository.findByConferenceId(conferenceId);
    }



    @Override
    public List<RoleRequest> findByConferenceIdAndStatus(Long conferenceId, String status) {
        return roleRequestRepository.findByConferenceIdAndStatus(conferenceId, status);
    }

    @Override
    public void delete(Long id) {
        roleRequestRepository.deleteById(id);
    }

    @Override
    public Optional<RoleRequest> findByUserIdAndConferenceIdAndRoleType(Long userId, Long conferenceId, RoleType roleType) {
        return roleRequestRepository.findByUserIdAndConferenceIdAndRoleType(userId, conferenceId, roleType);
    }
}