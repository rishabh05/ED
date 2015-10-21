//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.security.RolePermission;
import com.eworkplaceapps.platform.security.RolePermissionDataService;
import com.eworkplaceapps.platform.utils.enums.EDEntityType;

import java.util.UUID;

import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.ADD_EMPLOYEE;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.DELETE_EMPLOYEE;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.MANAGE_EMPLOYEE_DEPARTMENT;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.MANAGE_EMPLOYEE_LOCATION;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.MANAGE_EMPLOYEE_TEAM;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.UPDATE_EMPLOYEE;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.UPDATE_REPORTSTO;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.UPDATE_STARTDATE;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.VIEW_EMERGENCYCONTACT;
import static com.eworkplaceapps.employeedirectory.employee.EmployeeAccess.EmployeeOperation.*;


public class EmployeeAccess {

    // Defines constants for different possible operation of EMPLOYEE entity.
    public static enum EmployeeOperation {
        // Enum corresponding to view EMPLOYEE operation.
        VIEW_EMPLOYEE(0),
        // Enum corresponding to add EMPLOYEE operation.
        ADD_EMPLOYEE(1),
        // Enum corresponding to  update EMPLOYEE operation.
        UPDATE_EMPLOYEE(2),
        // Enum corresponding to delete EMPLOYEE operation.
        DELETE_EMPLOYEE(3),
        // Enum corresponding to manage employee department operation.
        MANAGE_EMPLOYEE_DEPARTMENT(4),
        // Enum corresponding to manage employee location operation.
        MANAGE_EMPLOYEE_LOCATION(5),
        // Enum corresponding to manage employee team operation.
        MANAGE_EMPLOYEE_TEAM(6),
        //Enum corresponding to update report operation.
        UPDATE_REPORTSTO(7),
        //Enum corresponding to update start date operation.
        UPDATE_STARTDATE(8),
        //Enum corresponding to view emergency contact operation.
        VIEW_EMERGENCYCONTACT(9);

        private int id;

        EmployeeOperation(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }


    /**
     * Check login user permission for given entity and operation.
     * Operation enum value to check access permission on EMPLOYEE entity.
     * Returns true if logged-in user has permission to perform required operation; otherwise returns false.
     * Currently we assume that EMPLOYEE permission set is defined at tenant level.
     *
     * @param operation
     * @param userId
     * @param tenantId
     * @return
     */
    public boolean checkAccess(int operation, UUID userId, UUID tenantId) throws EwpException {
        final RolePermission permission = getRolePermission(userId, tenantId);
        // If user has no access.
        if (permission != null) {
            // Check for permission bit against requested operation.
            if (operation == VIEW_EMPLOYEE.getId()) {
                return permission.getViewOp();
            } else if (operation == EmployeeAccess.EmployeeOperation.ADD_EMPLOYEE.getId()) {
                return permission.getAddOp();
            } else if (operation == EmployeeOperation.UPDATE_EMPLOYEE.getId()) {
                return permission.getUpdateOp();
            } else if (operation == EmployeeOperation.DELETE_EMPLOYEE.getId()) {
                return permission.getDeleteOp();
            } else {
                return false;
            }
        }
        return false;
    }


    // GET access vector of EMPLOYEE entity for all operation.
// Returns permission bit array against defined set of EMPLOYEE operation.
    public boolean[] accessList(UUID entityId, UUID userId, UUID tenantId) throws EwpException {
        boolean[] accessVector = new boolean[10];
        RolePermission permission = getRolePermission(userId, tenantId);
        if (entityId != null && !entityId.toString().isEmpty()) {
            EmployeeDataService service = new EmployeeDataService();
            BaseEntity entity = service.getEntity(entityId);
            if (entity != null) {
                Employee emp = (Employee) entity;
                if (emp.getTenantUserId().toString().equalsIgnoreCase(userId.toString())) {
                    accessVector[EmployeeOperation.VIEW_EMPLOYEE.getId()] = true;
                    accessVector[EmployeeOperation.UPDATE_EMPLOYEE.getId()] = true;
                    accessVector[EmployeeOperation.DELETE_EMPLOYEE.getId()] = false;
                    accessVector[EmployeeOperation.VIEW_EMERGENCYCONTACT.getId()] = true;
                    if (permission != null) {
                        if (permission.getAddOp() != null)
                            accessVector[EmployeeOperation.ADD_EMPLOYEE.getId()] = permission.getAddOp();
                        else
                            accessVector[EmployeeOperation.ADD_EMPLOYEE.getId()] = false;


                        // New access persmission
                        if (permission.getExtOp1() != null)
                            accessVector[EmployeeOperation.MANAGE_EMPLOYEE_DEPARTMENT.getId()] = permission.getExtOp1();
                        else
                            accessVector[EmployeeOperation.MANAGE_EMPLOYEE_DEPARTMENT.getId()] = false;
                        if (permission.getExtOp2() != null)
                            accessVector[EmployeeOperation.MANAGE_EMPLOYEE_LOCATION.getId()] = permission.getExtOp2();
                        else
                            accessVector[EmployeeOperation.MANAGE_EMPLOYEE_LOCATION.getId()] = false;
                        if (permission.getExtOp3() != null)
                            accessVector[EmployeeOperation.MANAGE_EMPLOYEE_TEAM.getId()] = permission.getExtOp3();
                        else
                            accessVector[EmployeeOperation.MANAGE_EMPLOYEE_TEAM.getId()] = false;

                        accessVector[EmployeeOperation.UPDATE_REPORTSTO.getId()] = EwpSession.getSharedInstance().isAccountAdmin();
                        accessVector[EmployeeOperation.UPDATE_STARTDATE.getId()] = EwpSession.getSharedInstance().isAccountAdmin();
                    }

                    return accessVector;
                }
            }
        }
        if (permission != null) {
            if (permission.getViewOp() != null)
                accessVector[VIEW_EMPLOYEE.getId()] = permission.getViewOp();
            else
                accessVector[VIEW_EMPLOYEE.getId()] = false;
            if (permission.getAddOp() != null)
                accessVector[EmployeeOperation.ADD_EMPLOYEE.getId()] = permission.getAddOp();
            else
                accessVector[EmployeeOperation.ADD_EMPLOYEE.getId()] = false;
            if (permission.getUpdateOp() != null)
                accessVector[EmployeeOperation.UPDATE_EMPLOYEE.getId()] = permission.getUpdateOp();
            else
                accessVector[EmployeeOperation.UPDATE_EMPLOYEE.getId()] = false;
            if (permission.getDeleteOp() != null)
                accessVector[EmployeeOperation.DELETE_EMPLOYEE.getId()] = permission.getDeleteOp();
            else
                accessVector[EmployeeOperation.DELETE_EMPLOYEE.getId()] = false;

            // New access persmission
            if (permission.getExtOp1() != null)
                accessVector[EmployeeOperation.MANAGE_EMPLOYEE_DEPARTMENT.getId()] = permission.getExtOp1();
            else
                accessVector[EmployeeOperation.MANAGE_EMPLOYEE_DEPARTMENT.getId()] = false;
            if (permission.getExtOp2() != null)
                accessVector[EmployeeOperation.MANAGE_EMPLOYEE_LOCATION.getId()] = permission.getExtOp2();
            else
                accessVector[EmployeeOperation.MANAGE_EMPLOYEE_LOCATION.getId()] = false;
            if (permission.getExtOp3() != null)
                accessVector[EmployeeOperation.MANAGE_EMPLOYEE_TEAM.getId()] = permission.getExtOp3();
            else
                accessVector[EmployeeOperation.MANAGE_EMPLOYEE_TEAM.getId()] = false;

            if (permission.getExtOp4() != null)
                accessVector[EmployeeOperation.VIEW_EMERGENCYCONTACT.getId()] = permission.getExtOp4();
            else
                accessVector[EmployeeOperation.VIEW_EMERGENCYCONTACT.getId()] = false;

            accessVector[EmployeeOperation.UPDATE_REPORTSTO.getId()] = EwpSession.getSharedInstance().isAccountAdmin();
            accessVector[EmployeeOperation.UPDATE_STARTDATE.getId()] = EwpSession.getSharedInstance().isAccountAdmin();
        }

        return accessVector;
    }

    private RolePermission getRolePermission(UUID userId, UUID tenantId) throws EwpException {
        RolePermission response;
        try {
            RolePermissionDataService RolePermissionService = new RolePermissionDataService();
            response = RolePermissionService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId,
                    tenantId, EwpSession.EMPLOYEE_APPLICATION_ID, EDEntityType.EMPLOYEE.getId(), null);
            if (response == null) {
                response = RolePermissionService.getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID,
                        EDEntityType.ALL_ED.getId(), null);
            }
            return response;
        } catch (EwpException e) {
            response = new RolePermissionDataService().getRolePermissionByUserAndTenantAndApplicationAndEntity(userId, tenantId, EwpSession.EMPLOYEE_APPLICATION_ID,
                    EDEntityType.ALL_ED.getId(), null);
            return response;
        }
    }

}
