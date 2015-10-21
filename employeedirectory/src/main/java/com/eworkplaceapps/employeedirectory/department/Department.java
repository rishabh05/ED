//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.department;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Department class contain the DEPARTMENT details.
 */
public class Department extends BaseEntity implements Serializable {

    private static final String DEPARTMENT_ENTITY_NAME = "Department";
    private String name = "", headOfDepartmentName = "", picture = "", description = "";
    private UUID headOfDepartment = Utils.emptyUUID();
    private UUID tenantId = Utils.emptyUUID();

    public Department() {
        super(DEPARTMENT_ENTITY_NAME);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        setPropertyChanged(this.name, name);
        this.name = name;
    }

    public String getHeadOfDepartmentName() {
        return headOfDepartmentName;
    }

    public void setHeadOfDepartmentName(String headOfDepartmentName) {
        setPropertyChanged(this.headOfDepartmentName, headOfDepartment);
        this.headOfDepartmentName = headOfDepartmentName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        setPropertyChanged(this.picture, picture);
        this.picture = picture;
    }

    public UUID getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(UUID headOfDepartment) {
        setPropertyChanged(this.headOfDepartment, headOfDepartment);
        this.headOfDepartment = headOfDepartment;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        setPropertyChanged(this.description, description);
        this.description = description;
    }

    /**
     * Create Department object and return created object.
     *
     * @return Department reference
     */
    public static Department createEntity() {
        return new Department();
    }

    /**
     * It validate DEPARTMENT required fields.
     *
     * @return
     * @throws EwpException
     */
    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        /// It is used to validate the first name null or empty.
        if ("".equals(this.name)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Name"});
            message.add(AppMessage.NAME_REQUIRED);
        }

        /// It is used to check the max charecter allowed in firstName.
        if (this.name.length() > 200) {
            dicError.put(EnumsForExceptions.ErrorDataType.LENGTH, new String[]{"Name"});
            message.add(AppMessage.LENGTH_ERROR);
        }

        if (this.headOfDepartment == null || this.headOfDepartment.equals(Utils.emptyUUID())) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Manager"});
            message.add(AppMessage.REQUIRED_FIELD);
        }

        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException(""), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

    @Override
    public BaseEntity copyTo(BaseEntity entity) {
        /// If both entities are not same then return the entity.
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        Department department = (Department) entity;
        department.setEntityId(this.entityId);
        department.setTenantId(this.tenantId);
        department.setName(this.name);
        department.setHeadOfDepartment(this.headOfDepartment);
        department.setHeadOfDepartmentName(this.headOfDepartmentName);
        department.setLastOperationType(this.lastOperationType);
        department.setPicture(this.picture);
        department.setDescription(this.description);
        department.setUpdatedAt(this.updatedAt);
        department.setUpdatedBy(this.updatedBy);
        department.setCreatedAt(this.createdAt);
        department.setCreatedBy(this.createdBy);
        return department;
    }
}

