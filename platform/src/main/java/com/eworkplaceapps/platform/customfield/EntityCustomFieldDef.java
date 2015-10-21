//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.customfield;

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
 * This class encapsulates all data for ENTITY_CUSTOM_FIELD entity.
 * CUSTOM fields are user defined fields, Thease fields are created by user.
 * CUSTOM fields can be inculde/exclude by a user.
 */
public class EntityCustomFieldDef extends BaseEntity {
    private static final String ENTITY_CUSTOM_FIELD_ENTITY_NAME = "ENTITY_CUSTOM_FIELD";

    public EntityCustomFieldDef() {
        super(ENTITY_CUSTOM_FIELD_ENTITY_NAME);
    }

    public static EntityCustomFieldDef createEntity() {
        return new EntityCustomFieldDef();
    }

    /**
     * ------------------- Begin Property section -----------------------
     */

    private UUID parentEntityId = Utils.emptyUUID();
    private Integer fieldCode = 1;

    private String customLabel = "";

    private String watermark = "";

    private String hintText = "";

    private Integer displayOrder = 2;

    private Integer fieldClass = 1;

    private Integer fieldType = 1;

    private Integer fieldDataType = 1;

    private Integer entityType = 1;

    private Boolean include = true;

    private Boolean required = false;

    private UUID tenantId = Utils.emptyUUID();

    private String defaultValue = "";

    private String groupName;

    private Integer groupId = 1;

    private String applicationId = "";


    public void setFieldCode(Integer fieldCode) {
        setPropertyChanged(this.getFieldCode(), fieldCode);
        this.fieldCode = fieldCode;
    }

    public void setCustomLabel(String customLabel) {
        setPropertyChanged(this.getCustomLabel(), customLabel);
        this.customLabel = customLabel;
    }

    public void setWatermark(String watermark) {
        setPropertyChanged(this.getWatermark(), watermark);
        this.watermark = watermark;
    }

    public void setHintText(String hintText) {
        setPropertyChanged(this.getHintText(), hintText);
        this.hintText = hintText;
    }

    public void setDisplayOrder(Integer displayOrder) {
        setPropertyChanged(this.getDisplayOrder(), displayOrder);
        this.displayOrder = displayOrder;
    }

    public void setFieldClass(Integer fieldClass) {
        setPropertyChanged(this.getFieldClass(), fieldClass);
        this.fieldClass = fieldClass;
    }

    public void setFieldType(Integer fieldType) {
        setPropertyChanged(this.getFieldType(), fieldType);
        this.fieldType = fieldType;
    }

    public void setFieldDataType(Integer fieldDataType) {
        setPropertyChanged(this.getFieldDataType(), fieldDataType);
        this.fieldDataType = fieldDataType;
    }

    public void setEntityType(Integer entityType) {
        setPropertyChanged(this.getEntityType(), entityType);
        this.entityType = entityType;
    }

    public void setInclude(Boolean include) {
        setPropertyChanged(this.getInclude(), include);
        this.include = include;
    }

    public void setRequired(Boolean required) {
        setPropertyChanged(this.getRequired(), required);
        this.required = required;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.getTenantId(), tenantId);
        this.tenantId = tenantId;
    }

    public void setDefaultValue(String defaultValue) {
        setPropertyChanged(this.getDefaultValue(), defaultValue);
        this.defaultValue = defaultValue;
    }

    public void setGroupName(String groupName) {
        setPropertyChanged(this.getGroupName(), groupName);
        this.groupName = groupName;
    }

    public void setGroupId(Integer groupId) {
        setPropertyChanged(this.getGroupId(), groupId);
        this.groupId = groupId;
    }

    public void setApplicationId(String applicationId) {
        // setPropertyChanged(this.applicationId,applicationId);
        this.applicationId = applicationId;
    }

    public Integer getFieldCode() {
        return fieldCode;
    }

    public String getCustomLabel() {
        return customLabel;
    }

    public String getWatermark() {
        return watermark;
    }

    public String getHintText() {
        return hintText;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public Integer getFieldClass() {
        return fieldClass;
    }

    public Integer getFieldType() {
        return fieldType;
    }

    public Integer getFieldDataType() {
        return fieldDataType;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public Boolean getInclude() {
        return include;
    }

    public Boolean getRequired() {
        return required;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getGroupName() {
        return groupName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public UUID getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(UUID parentEntityId) {
        setPropertyChanged(this.parentEntityId, parentEntityId);
        this.parentEntityId = parentEntityId;
    }

    /**
     * It validate entityCustomField enity.
     *
     * @return Boolean
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<>();

        if (Utils.isEmptyOrNull(this.customLabel)) {
            String[] labelList = new String[]{"CustomLabel"};
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, labelList);
            message.add(AppMessage.NAME_REQUIRED);
        }

        if (!message.isEmpty()) {
            throw new EwpException(new EwpException(AppMessage.NAME_REQUIRED), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);

        }
        return true;
    }

    /**
     * Create the copy of an existing Task object.
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
        EntityCustomFieldDef entityCustomField = (EntityCustomFieldDef) entity;

        entityCustomField.entityId = this.entityId;
        entityCustomField.tenantId = this.getTenantId();
        entityCustomField.applicationId = this.getApplicationId();
        //entityCustomField.sourceId = this.sourceId
        entityCustomField.customLabel = this.getCustomLabel();
        entityCustomField.defaultValue = this.getDefaultValue();
        entityCustomField.entityType = this.getEntityType();
        //entityCustomField.sortNumber = this.sortNumber;
        entityCustomField.required = this.getRequired();
        entityCustomField.include = this.getInclude();
        entityCustomField.entityType = this.getEntityType();
        entityCustomField.fieldCode = this.getFieldCode();
        entityCustomField.fieldDataType = this.getFieldDataType();
        entityCustomField.fieldClass = this.getFieldClass();
        entityCustomField.fieldType = this.getFieldType();
        entityCustomField.groupId = this.getGroupId();
        entityCustomField.lastOperationType = this.lastOperationType;
        return entityCustomField;
    }
}
