//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.customfield;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class encapsulates all data for FieldPermission entity.
 * FieldPermission are used to defined fields permission.
 */
public class FieldPermission extends BaseEntity {
    private static final String FIELD_PERMISSION_ENTITY_NAME = "FieldPermission";

    public FieldPermission() {
        super(FIELD_PERMISSION_ENTITY_NAME);
    }

    public static FieldPermission createEntity() {
        return new FieldPermission();
    }

    private boolean system = false;
    private boolean viewField = false;
    private boolean updateField = false;
    private UUID tenantId = Utils.emptyUUID();
    private UUID entityFieldDetailsId = Utils.emptyUUID();
    private UUID rolePermissionId = Utils.emptyUUID();
    private String applicationId = "";

    public boolean isSystem() {
        return system;
    }

    public boolean isViewField() {
        return viewField;
    }

    public boolean isUpdateField() {
        return updateField;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public UUID getEntityFieldDetailsId() {
        return entityFieldDetailsId;
    }

    public UUID getRolePermissionId() {
        return rolePermissionId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setSystem(boolean system) {
        super.setPropertyChanged(this.system, system);
        this.system = system;
    }

    public void setViewField(boolean viewField) {
        super.setPropertyChanged(this.viewField, viewField);
        this.viewField = viewField;
    }

    public void setUpdateField(boolean updateField) {
        super.setPropertyChanged(this.updateField, updateField);
        this.updateField = updateField;
    }

    public void setTenantId(UUID tenantId) {
        super.setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public void setEntityFieldDetailsId(UUID entityFieldDetailsId) {
        super.setPropertyChanged(this.entityFieldDetailsId, entityFieldDetailsId);
        this.entityFieldDetailsId = entityFieldDetailsId;
    }

    public void setRolePermissionId(UUID rolePermissionId) {
        super.setPropertyChanged(this.rolePermissionId, rolePermissionId);
        this.rolePermissionId = rolePermissionId;
    }

    public void setApplicationId(String applicationId) {
        super.setPropertyChanged(this.applicationId, applicationId);
        this.applicationId = applicationId;
    }


    /**
     * It validate fieldPermission enity.
     *
     * @return Boolean
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<>();

        if (!message.isEmpty()) {
            throw new EwpException(new EwpException("Validation ERROR in CustomField"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
        return true;
    }

    /**
     * Create the copy of an existing FieldPermission object.
     *
     * @param entity BaseEntity
     * @return BaseEntity
     */
    @Override
    public BaseEntity copyTo(BaseEntity entity) {
        // If both entities are not same then return the entity.
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        FieldPermission fieldPermission = (FieldPermission) entity;
        fieldPermission.entityId = this.entityId;
        fieldPermission.tenantId = this.tenantId;
        fieldPermission.viewField = this.viewField;
        fieldPermission.updateField = this.updateField;
        fieldPermission.rolePermissionId = this.rolePermissionId;
        fieldPermission.entityFieldDetailsId = this.entityFieldDetailsId;
        fieldPermission.system = this.system;
        fieldPermission.lastOperationType = this.lastOperationType;
        fieldPermission.updatedAt = this.updatedAt;
        fieldPermission.updatedBy = this.updatedBy;
        fieldPermission.createdAt = this.createdAt;
        fieldPermission.createdBy = this.createdBy;
        return fieldPermission;
    }
}
