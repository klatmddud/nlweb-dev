package com.nlweb.admin.facade;

import com.nlweb.amho.service.AmhoQueryService;
import com.nlweb.amho.exception.InvalidAmhoException;
import com.nlweb.admin.dto.request.CreateAdminRequest;
import com.nlweb.admin.entity.Admin;
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
@Transactional
public class AdminFacade {

    private final AdminCommandService adminCommandService;
    private final AdminQueryService adminQueryService;
    private final AmhoQueryService amhoQueryService;

    /** 집부 권한 부여 */
    public AdminResponse grantAdminAuthority(NlwebUserDetails principal, CreateAdminRequest request) {
        if (!request.amhoCode().equals(amhoQueryService.getActiveAmho().getAdminCode())) {
            throw new InvalidAmhoException("유효하지 않은 등록 코드입니다.");
        }

        return AdminResponse.forProfile(
                adminCommandService.create(principal.getUserId())
        );
    }

    /** 모든 집부 반환 */
    @Transactional(readOnly = true)
    public List<AdminResponse> getAllAdmins(NlwebUserDetails principal) {
        validateAdminPrincipal(principal);

        return adminQueryService.getAll().stream()
                .map(AdminResponse::forProfile)
                .toList();
    }

    /** 내 집부 역할 변경 */
    public AdminResponse updateMyRole(UpdateMyRoleRequest request, NlwebUserDetails principal) {
        validateAdminPrincipal(principal);

        Admin admin = adminCommandService.updateRole(principal.getUsername(), request.role());
        return AdminResponse.forProfile(admin);
    }

    private void validateAdminPrincipal(NlwebUserDetails principal) {
        if (principal == null) {
            throw new AccessDeniedException("인증이 필요합니다.");
        }

        if (!principal.isAdmin()) {
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }
    }

    /** 내 집부 권한 박탈 */
    public void revokeMyAuthority(NlwebUserDetails principal) {
        adminCommandService.delete(principal.getUsername());
    }
}
