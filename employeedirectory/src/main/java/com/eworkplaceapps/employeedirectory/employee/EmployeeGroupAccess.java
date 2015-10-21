//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/22/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.security.EntityAccess;
import com.eworkplaceapps.platform.security.RolePermission;
import com.eworkplaceapps.platform.security.RolePermissionDataService;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import java.util.UUID;


public class EmployeeGroupAccess extends EntityAccess {

    // Defines constants for different possible operation of EmployeeGroup entity.
    public enum EmployeeGroupOperation {
        // Enum corresponding to view EmployeeGroup operation.
        VIEW_EMPLOYEE_GROUP(0),
        // Enum corresponding to add EmployeeGroup operation.
        ADD_EMPLOYEE_GROUP(1),
        // Enum corresponding to  update EmployeeGroup operation.
        UPDATE_EMPLOYEE_GROUP(2),
        // Enum corresponding to delete EmployeeGroup operation.
        DELETE_EMPLOYEE_GROUP(3);
        private int id;

        EmployeeGroupOperation(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Check login user permission for given entity and operation.
     * Operation enum value to check access permission on EmployeeGroup entity.
     * Returns true if logged-in user has permission to perform required operation; otherwise returns false.
     * Currently we assume that EmployeeGroup permission set is defined at tenant level.
     *
     * @param operation
     * @param userId
     * @param tenantId
     * @return boolean
     */
    public boolean checkAccess(int operation, UUID userId, UUID tenantId) throws EwpException {
        RolePermission permission = getRolePermission(userId, tenantId);
        /// If user has no access.
        if (permission != null) {
            if (operation == EmployeeGroupOperation.VIEW_EMPLOYEE_GROUP.getId()) {
                return permission.getViewOp();
            } else if (operation == EmployeeGroupOperation.ADD_EMPLOYEE_GROUP.getId()) {
                return permission.getAddOp();
            } else if (operation == EmployeeGroupOperation.UPDATE_EMPLOYEE_GROUP.getId()) {
                return permission.getUpdateOp();
            } else if (operation == EmployeeGroupOperation.DELETE_EMPLOYEE_GROUP.getId()) {
                return permission.getDeleteOp();
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Get access vector of EmployeeGroup entity for all operation.
     * /// :returns: Returns permission bit array against defined set of EmployeeGroup operation.
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
     * It is used to get Role Permission object from userid and tenantid.
     * :param: userId
     * :param: tenantId
     * :returns: Returns permission bit array against defined set of task operation.
     *
     * @param userId
     * @param tenantId
     * @return RolePermission
     */
    private RolePermission getRolePermission(UUID userId, UUID tenantId) throws EwpException {
        RolePermission response;
        RolePermissionDataService rolePermissionService = new RolePermissionDataService();
        try {
            response = rolePermissionService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, EDEntityType.EMPLOYEE_GROUP.getId(), null);
            if (response != null) {
                return response;
            } else {
                return rolePermissionService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, EDEntityType.ALL_ED.getId(), null);
            }
        } catch (EwpException e) {
            response = rolePermissionService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, EDEntityType.ALL_ED.getId(), null);
            return response;
        }
    }

}