package com.nlweb.admin.facade;

import com.nlweb.admin.dto.object.AdminObject;
import com.nlweb.admin.dto.request.UpdateMyRoleRequest;
import com.nlweb.admin.dto.response.AdminResponse;
import com.nlweb.admin.service.AdminCommandService;
import com.nlweb.admin.service.AdminQueryService;
import com.nlweb.core.security.NlwebUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminFacade {

    private final AdminCommandService adminCommandService;
    private final AdminQueryService adminQueryService;

    public List<AdminResponse> getAllAdmins(NlwebUserDetails principal) {
        validateAdminPrincipal(principal);

        return adminQueryService.getAll().stream()
                .map(AdminResponse::from)
                .toList();
    }

    @Transactional
    public AdminResponse updateMyRole(UpdateMyRoleRequest request, NlwebUserDetails principal) {
        validateAdminPrincipal(principal);

        AdminObject adminObject = adminCommandService.updateRole(principal.getUsername(), request.role());
        return AdminResponse.forUpdateRole(adminObject);
    }

    private void validateAdminPrincipal(NlwebUserDetails principal) {
        if (principal == null) {
            throw new AccessDeniedException("인증이 필요합니다.");
        }

        if (!principal.isAdmin()) {
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }
    }
}
