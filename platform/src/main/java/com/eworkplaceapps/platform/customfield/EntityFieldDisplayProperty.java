//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.customfield;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.UUID;


public class EntityFieldDisplayProperty extends BaseEntity {

    private static final String ENTITY_FIELD_DISPLAY_ORDER_ENTITY_NAME = "EntityFieldDisplayOrder";

    public EntityFieldDisplayProperty() {
        super(ENTITY_FIELD_DISPLAY_ORDER_ENTITY_NAME);
    }

    /**
     * Create Task object and return created object.
     *
     * @return EntityFieldDisplayProperty
     */

    public static EntityFieldDisplayProperty createEntity() {
        return new EntityFieldDisplayProperty();
    }

    private Boolean system = false;
    private int entityType = 1;
    private int displayOrder = 1;
    private String fieldName = "";
    private UUID tenantId = Utils.emptyUUID();
    private String applicationId = "";

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        setPropertyChanged(this.system, system);
        this.system = system;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        setPropertyChanged(this.entityType, entityType);
        this.entityType = entityType;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        setPropertyChanged(this.displayOrder, displayOrder);
        this.displayOrder = displayOrder;
    }

    /**
     * @return STRING
     */
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        setPropertyChanged(this.fieldName, fieldName);
        this.fieldName = fieldName;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        setPropertyChanged(this.applicationId, applicationId);
        this.applicationId = applicationId;
    }

    /**
     * Create the copy of an existing EntityFieldDisplayOrder object.
     *
     * @param entity BaseEntity
     * @return BaseEntity
     */
    @Override
    public BaseEntity copyTo(BaseEntity entity) {
        // If both entities are not same then return the entity.
        if (entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        EntityFieldDisplayProperty fieldDisplayOrder = (EntityFieldDisplayProperty) entity;
        fieldDisplayOrder.entityId = this.entityId;
        fieldDisplayOrder.tenantId = this.tenantId;
        fieldDisplayOrder.displayOrder = this.displayOrder;
        fieldDisplayOrder.system = this.system;
        fieldDisplayOrder.fieldName = this.fieldName;
        fieldDisplayOrder.system = this.system;
        fieldDisplayOrder.applicationId = this.applicationId;
        fieldDisplayOrder.entityType = this.entityType;
        fieldDisplayOrder.lastOperationType = this.lastOperationType;
        fieldDisplayOrder.updatedAt = this.updatedAt;
        fieldDisplayOrder.updatedBy = this.updatedBy;
        fieldDisplayOrder.createdAt = this.createdAt;
        fieldDisplayOrder.createdBy = this.createdBy;
        return fieldDisplayOrder;
    }

}
