//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.location;

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
public class LocationAccess extends EntityAccess {

    /**
     * Defines constants for different possible operation of Location entity.
     */
    public enum LocationOperation {
        // Enum corresponding to view Location operation.
        VIEW_LOCATION(0),
        // Enum corresponding to add Location operation.
        ADD_LOCATION(1),
        // Enum corresponding to  update Location operation.
        UPDATE_LOCATION(2),
        // Enum corresponding to delete Location operation.
        DELETE_LOCATION(3);

        private int id = 0;

        LocationOperation(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Check login user permission for given entity and operation.
     * Operation enum value to check access permission on Location entity.
     * Returns true if logged-in user has permission to perform required operation; otherwise returns false.
     * Currently we assume that Location permission set is defined at tenant level.
     *
     * @param operation
     * @param tenantId
     * @return boolean
     */
    public boolean checkAccess(int operation, UUID userId, UUID tenantId) throws EwpException {
        RolePermission permission = getRolePermission(userId, tenantId);
        if (permission != null) {
            if (operation == LocationOperation.VIEW_LOCATION.getId()) {
                return permission.getViewOp();
            } else if (operation == LocationOperation.ADD_LOCATION.getId()) {
                return permission.getAddOp();
            } else if (operation == LocationOperation.UPDATE_LOCATION.getId()) {
                return permission.getUpdateOp();
            } else if (operation == LocationOperation.DELETE_LOCATION.getId()) {
                return permission.getDeleteOp();
            } else {
                return false;
            }
        }
        return false;
    }


    /**
     * Get access vector of Location entity for all operation.
     * Returns permission bit array against defined set of Location operation.
     *
     * @param entityId
     * @param userId
     * @param tenantId
     * @return boolean[]
     */
    public boolean[] accessList(UUID entityId, UUID userId, UUID tenantId) throws EwpException {
        boolean[] accessVector = new boolean[4];
        RolePermission permission = getRolePermission(userId, tenantId);
        if (permission != null) {
            accessVector[LocationOperation.VIEW_LOCATION.getId()] = permission.getViewOp();
            accessVector[LocationOperation.ADD_LOCATION.getId()] = permission.getAddOp();
            accessVector[LocationOperation.UPDATE_LOCATION.getId()] = permission.getUpdateOp();
            accessVector[LocationOperation.DELETE_LOCATION.getId()] = permission.getDeleteOp();
        }
        return accessVector;
    }

    /**
     * @param userId
     * @param tenantId
     * @return RolePermission
     */

    private RolePermission getRolePermission(UUID userId, UUID tenantId) throws EwpException {
        RolePermissionDataService rolePermissionDataService = new RolePermissionDataService();
        RolePermission response;
        try {
            response = rolePermissionDataService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, EDEntityType.LOCATION.getId(), null);
            if (response != null) {
                return response;
            } else {
                return rolePermissionDataService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, EDEntityType.ALL_ED.getId(), null);
            }
        } catch (EwpException e) {
            response = rolePermissionDataService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, EDEntityType.ALL_ED.getId(), null);
            return response;
        }
    }
}
