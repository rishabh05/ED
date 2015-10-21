package com.eworkplaceapps.platform.tenant;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.security.EntityAccess;
import com.eworkplaceapps.platform.security.RolePermission;
import com.eworkplaceapps.platform.security.RolePermissionDataService;
import com.eworkplaceapps.platform.utils.enums.PFEntityType;

import java.util.UUID;

import static com.eworkplaceapps.platform.tenant.TenantAccess.TenantOperation.ADD_TENANT;
import static com.eworkplaceapps.platform.tenant.TenantAccess.TenantOperation.DELETE_TENANT;
import static com.eworkplaceapps.platform.tenant.TenantAccess.TenantOperation.UPDATE_TENANT;
import static com.eworkplaceapps.platform.tenant.TenantAccess.TenantOperation.VIEW_TENANT;

/**
 * Created by admin on 4/20/2015.
 */
public class TenantAccess extends EntityAccess {

    /**
     * Defines constants for different possible operation of TENANT entity.
     */
    public enum TenantOperation {
        // Enum corresponding to view TENANT operation.
        VIEW_TENANT(0),
        // Enum corresponding to add TENANT operation.
        ADD_TENANT(1),
        // Enum corresponding to  update TENANT operation.
        UPDATE_TENANT(2),
        // Enum corresponding to delete TENANT operation.
        DELETE_TENANT(3);
        private int id;

        TenantOperation(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Check login user permission for given entity and operation.
     * // Operation enum value to check access permission on TENANT entity.
     * // Returns true if logged-in user has permission to perform required operation; otherwise returns false.
     * // Currently we assume that TENANT permission set is defined at tenant level.
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
            if (operation == VIEW_TENANT.getId()) {
                return permission.getViewOp();
            } else if (operation == ADD_TENANT.getId()) {
                return permission.getAddOp();
            } else if (operation == UPDATE_TENANT.getId()) {
                return permission.getUpdateOp();
            } else if (operation == DELETE_TENANT.getId()) {
                return permission.getDeleteOp();
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * GET access vector of task entity for all operation.
     * Returns permission bit array against defined set of task operation.
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
            accessVector[TenantOperation.VIEW_TENANT.getId()] = permission.getViewOp();
            accessVector[TenantOperation.ADD_TENANT.getId()] = permission.getAddOp();
            accessVector[TenantOperation.UPDATE_TENANT.getId()] = permission.getUpdateOp();
            accessVector[TenantOperation.DELETE_TENANT.getId()] = permission.getDeleteOp();
        }
        return accessVector;
    }

    private RolePermission getRolePermission(UUID userId, UUID tenantId) throws EwpException {
        RolePermission rolePermission = null;
        RolePermissionDataService rolePermissionDataService = new RolePermissionDataService();
        try {
            rolePermission = rolePermissionDataService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, PFEntityType.TENANT.getValue(), null);
            if (rolePermission != null) {
                return rolePermission;
            }
        } catch (EwpException e) {
            rolePermission = rolePermissionDataService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, PFEntityType.ALL.getValue(), null);
            return rolePermission;
        }
        return null;
    }

}
