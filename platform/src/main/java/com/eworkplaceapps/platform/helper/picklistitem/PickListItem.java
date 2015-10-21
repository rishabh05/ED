//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/20/2015
//===============================================================================
package com.eworkplaceapps.platform.helper.picklistitem;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class PickListItem extends BaseEntity {

    private static String PICKLIST_ITEM_ENTITY_NAME = "PICK_LIST_ITEM";

    protected PickListItem() {
        super(PICKLIST_ITEM_ENTITY_NAME);
    }

    /**
     * Create PICK_LIST_ITEM object and return created object.
     */
    public static PickListItem createEntity() {
        return new PickListItem();
    }


    private UUID tenantId = UUID.randomUUID();

    private String name;
    // Getter/setter  for ownerId
    private UUID ownerId;
    // Getter/Setter for Assigned To
    private UUID parentId;
    // Getter/setter for active
    private boolean active = false;
    // Getter/setter for isDefault
    private boolean isDefault = false;
    // Getter/setter for systemDefined
    private int systemDefined = 0;
    // Getter/setter for intValue
    private int intValue = 0;
    private String textValue = "";
    // Getter/setter for task status
    private int sortNumber = 0;

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public void setName(String name) {
        setPropertyChanged(this.name, name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOwnerId(UUID ownerId) {
        setPropertyChanged(this.ownerId, ownerId);
        this.ownerId = ownerId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setParentId(UUID parentId) {
        setPropertyChanged(this.parentId, parentId);
        this.parentId = parentId;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setActive(boolean active) {
        setPropertyChanged(this.active, active);
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setDefault(boolean isDefault) {
        setPropertyChanged(this.isDefault, isDefault);
        this.isDefault = isDefault;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setSystemDefined(int systemDefined) {
        setPropertyChanged(this.systemDefined, systemDefined);
        this.systemDefined = systemDefined;
    }

    public int getSystemDefined() {
        return systemDefined;
    }

    public void setIntValue(int intValue) {
        setPropertyChanged(this.intValue, intValue);
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        setPropertyChanged(this.textValue, textValue);
        this.textValue = textValue;
    }

    public int getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(int sortNumber) {
        setPropertyChanged(this.sortNumber, sortNumber);
        this.sortNumber = sortNumber;
    }

    /**
     * It validate picklist enity.
     *
     * @return
     */
    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        if ("".equals(this.textValue)) {
            message.add(AppMessage.NAME_REQUIRED);
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Name"});
        }
        if (message.isEmpty()) {
            //  return EwpError.createSuccess(EnumsForExceptions.ErrorModule.DATA_SERVICE);
            return true;
        } else {
            throw new EwpException(new EwpException("Validation error in PICK_LIST_ITEM"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

    /**
     * Create the copy of an existing Task object.
     *
     * @param entity
     * @return
     */
    public BaseEntity copyTo(BaseEntity entity) {
        super.copyTo(entity);
        // If both entities are not same then return the entity.
        if (entity.getEntityName() != this.entityName) {
            return null;
        }
        PickListItem plItem = (PickListItem) entity;
        plItem.entityId = this.entityId;
        plItem.tenantId = this.tenantId;
        plItem.name = this.name;
        plItem.ownerId = this.ownerId;
        plItem.parentId = this.parentId;
        plItem.active = this.active;
        plItem.isDefault = this.isDefault;
        plItem.systemDefined = this.systemDefined;
        plItem.textValue = this.textValue;
        plItem.intValue = this.intValue;
        return plItem;
    }
}
