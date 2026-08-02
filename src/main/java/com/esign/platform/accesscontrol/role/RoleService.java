package com.esign.platform.accesscontrol.role;

import java.util.List;
import java.util.UUID;

import com.esign.platform.accesscontrol.role.dto.CreateRoleRequestDTO;
import com.esign.platform.accesscontrol.role.dto.RoleResponseDTO;
import com.esign.platform.accesscontrol.role.dto.UpdateRoleRequestDTO;

public interface RoleService {

    RoleResponseDTO createRole(CreateRoleRequestDTO request);

    RoleResponseDTO updateRole(UUID roleId,
            UpdateRoleRequestDTO request);

    RoleResponseDTO getRoleById(UUID roleId);

    List<RoleResponseDTO> getAllRoles();

    List<RoleResponseDTO> getGlobalRoles();

    List<RoleResponseDTO> getRolesByOrganization(
            UUID organizationId);

    void deleteRole(UUID roleId);

}