//===============================================================================
//  2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.department;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.security.EntityAccess;
import com.eworkplaceapps.platform.security.RolePermission;
import com.eworkplaceapps.platform.security.RolePermissionDataService;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import java.util.UUID;

/**
 *
 */
public class DepartmentAccess extends EntityAccess {
    /**
     * Defines constants for different possible operation of Department entity.
     */
    public enum DepartmentOperation {
        // Enum corresponding to view Department operation.
        VIEW_DEPARTMENT(0),
        // Enum corresponding to add Department operation.
        ADD_DEPARTMENT(1),
        // Enum corresponding to  update Department operation.
        UPDATE_DEPARTMENT(2),
        // Enum corresponding to delete Department operation.
        DELETE_DEPARTMENT(3);

        private int id = 0;

        DepartmentOperation(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Check login user permission for given entity and operation.
     * Operation enum value to check access permission on Department entity.
     * Returns true if logged-in user has permission to perform required operation; otherwise returns false.
     * Currently we assume that Department permission set is defined at tenant level.
     *
     * @param operation
     * @param userId
     * @param tenantId
     * @return boolean
     */
    @Override
    public boolean checkAccess(int operation, UUID userId, UUID tenantId) throws EwpException {
        RolePermission permission = getRolePermission(userId, tenantId);
        if (permission != null) {
            if (operation == DepartmentOperation.VIEW_DEPARTMENT.getId()) {
                return permission.getViewOp();
            } else if (operation == DepartmentOperation.ADD_DEPARTMENT.getId()) {
                return permission.getAddOp();
            } else if (operation == DepartmentOperation.UPDATE_DEPARTMENT.getId()) {
                return permission.getUpdateOp();
            } else if (operation == DepartmentOperation.DELETE_DEPARTMENT.getId()) {
                return permission.getDeleteOp();
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Get access vector of Department entity for all operation.
     * Returns permission bit array against defined set of Department operation.
     *
     * @param entityId
     * @param userId
     * @param tenantId
     * @return int[]
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
     * @param userId
     * @param tenantId
     * @return RolePermission
     */
    private RolePermission getRolePermission(UUID userId, UUID tenantId) throws EwpException {
        RolePermissionDataService RolePermissionService = new RolePermissionDataService();
        RolePermission response = RolePermissionService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, EDEntityType.DEPARTMENT.getId(), null);
        if (response == null) {
            return RolePermissionService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, EDEntityType.ALL_ED.getId(), null);
        }
        return response;
    }

}
