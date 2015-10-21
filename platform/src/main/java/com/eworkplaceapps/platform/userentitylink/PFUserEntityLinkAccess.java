//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/19/2015
//===============================================================================
package com.eworkplaceapps.platform.userentitylink;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.security.EntityAccess;
import com.eworkplaceapps.platform.security.RolePermission;
import com.eworkplaceapps.platform.security.RolePermissionDataService;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;
import com.eworkplaceapps.platform.utils.enums.PFEntityType;

import java.util.UUID;

import static com.eworkplaceapps.platform.userentitylink.PFUserEntityLinkAccess.PFUserEntityLinkOperation.*;
import static com.eworkplaceapps.platform.userentitylink.PFUserEntityLinkAccess.PFUserEntityLinkOperation.VIEW_PF_USER_ENTITY_LINK;

/**
 *
 */
public class PFUserEntityLinkAccess extends EntityAccess {

    /// Defines constants for different possible operation of PFUserEntityLink entity.
    public enum PFUserEntityLinkOperation {
        /// Enum corresponding to view PFUserEntityLink operation.
        VIEW_PF_USER_ENTITY_LINK(0),
        /// Enum corresponding to add PFUserEntityLink operation.
        ADD_PF_USER_ENTITY_LINK(1),
        /// Enum corresponding to  update PFUserEntityLink operation.
        UPDATE_PF_USER_ENTITY_LINK(2),
        /// Enum corresponding to delete PFUserEntityLink operation.
        DELETE_PF_USER_ENTITY_LINK(3);
        private int id;

        PFUserEntityLinkOperation(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Check login user permission for given entity and operation.
     * Operation enum value to check access permission on PFUserEntityLink entity.
     * Returns true if logged-in user has permission to perform required operation; otherwise returns false.
     * Currently we assume that PFUserEntityLink permission set is defined at tenant level.
     *
     * @param operation
     * @param userId
     * @param tenantId
     * @return
     */
    public boolean checkAccess(int operation, UUID userId, UUID tenantId) throws EwpException {
        RolePermission permission = getRolePermission(userId, tenantId);
        // If user has no access.
        if (permission != null) {
            // Check for permission bit against requested operation.
            if (operation == VIEW_PF_USER_ENTITY_LINK.getId()) {
                // For View PFUserEntityLink Operation
                return permission.getViewOp();
            } else if (operation == ADD_PF_USER_ENTITY_LINK.getId()) {
                // For Add PFUserEntityLink Operation
                return permission.getAddOp();
            } else if (operation == UPDATE_PF_USER_ENTITY_LINK.getId()) {
                // For Update PFUserEntityLink Operation
                return permission.getUpdateOp();
            } else if (operation == DELETE_PF_USER_ENTITY_LINK.getId()) {
                // For Delete PFUserEntityLink Operation
                return permission.getDeleteOp();
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Get access vector of PFUserEntityLink entity for all operation.
     * Returns permission bit array against defined set of PFUserEntityLink operation.
     *
     * @param entityId
     * @param userId
     * @param tenantId
     * @return boolean[]
     */
    @Override
    public boolean[] accessList(UUID entityId, UUID userId, UUID tenantId) throws EwpException {
        boolean[] accessVector = new boolean[4];
        RolePermission permission = getRolePermission(userId, tenantId);
        if (permission != null) {
            accessVector[0] = permission.getViewOp();
            accessVector[1] = permission.getAddOp();
            accessVector[2] = permission.getUpdateOp();
            accessVector[3] = permission.getDeleteOp();
        }
        return accessVector;
    }

    /**
     * Method is used to get role permission from userid and tenantid.
     *
     * @param userId
     * @param tenantId
     * @return
     */
    private RolePermission getRolePermission(UUID userId, UUID tenantId) throws EwpException {
        RolePermissionDataService RolePermissionService = new RolePermissionDataService();
        RolePermission response;
        try {
            response = RolePermissionService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, PFEntityType.USER_ENTITY_LINK.getValue(), null);
            if (response != null) {
                return response;
            }
        } catch (EwpException e) {
            response = RolePermissionService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, EDEntityType.ALL_ED.getId(), null);
            return response;
        }
        return null;
    }
}
