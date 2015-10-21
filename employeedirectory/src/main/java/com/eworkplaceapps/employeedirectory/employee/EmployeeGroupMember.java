//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class encapsulates all data for EmployeeGroupMember entity.
 */
public class EmployeeGroupMember extends BaseEntity {
    private static final String EMPLOYEE_GROUP_MEMBER_ENTITY_NAME = "EmployeeGroupMember";
    private UUID employeeId = Utils.emptyUUID();
    private UUID employeeGroupId = Utils.emptyUUID();
    private UUID tenantId = Utils.emptyUUID();

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        setPropertyChanged(this.employeeId, employeeId);
        this.employeeId = employeeId;
    }

    public UUID getEmployeeGroupId() {
        return employeeGroupId;
    }

    public void setEmployeeGroupId(UUID employeeGroupId) {
        setPropertyChanged(this.employeeGroupId, employeeGroupId);
        this.employeeGroupId = employeeGroupId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public EmployeeGroupMember() {
        super(EMPLOYEE_GROUP_MEMBER_ENTITY_NAME);
    }

    /**
     * Create Task object and return created object.
     *
     * @return EmployeeGroupMember
     */
    public static EmployeeGroupMember createEntity() {
        return new EmployeeGroupMember();
    }

    /// It validate employee enity.

    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();


        if (this.employeeId.equals(Utils.emptyUUID())) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"EmployeeId"});
            message.add(AppMessage.REFERENCE_ID_REQUIRED);
        }

        if (this.employeeGroupId.equals(Utils.emptyUUID())) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"EmployeeGroupId"});
            message.add(AppMessage.REFERENCE_ID_REQUIRED);
        }
        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException("Validation Error"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

    @Override
    public BaseEntity copyTo(BaseEntity entity) {
        /// If both entities are not same then return the entity.
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        EmployeeGroupMember employee = (EmployeeGroupMember) entity;
        employee.setEntityId(this.entityId);
        employee.setTenantId(this.tenantId);
        employee.setEmployeeGroupId(this.employeeGroupId);
        employee.setEmployeeId(this.employeeId);
        employee.setUpdatedAt(this.updatedAt);
        employee.setUpdatedBy(this.updatedBy);
        employee.setCreatedAt(this.createdAt);
        employee.setCreatedBy(this.createdBy);
        return employee;
    }
}
