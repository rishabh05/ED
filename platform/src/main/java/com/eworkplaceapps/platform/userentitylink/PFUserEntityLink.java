//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/19/2015
//===============================================================================
package com.eworkplaceapps.platform.userentitylink;

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
 * UserEntityLink class contain the UserEntityLink details.
 */
public class PFUserEntityLink extends BaseEntity {
    private static final String PF_USER_ENTITY_LINK = "PFUserEntityLink";

    public PFUserEntityLink() {
        super(PF_USER_ENTITY_LINK);
    }

    /**
     * Create UserEntityLink object and return created object.
     *
     * @return
     */
    public static PFUserEntityLink createEntity() {
        return new PFUserEntityLink();
    }

    private int sourceEntityType = 0, linkType = 0, intValue = 0;
    private UUID sourceEntityId = Utils.emptyUUID();
    private boolean boolValue = false;
    private String stringValue = "";
    private UUID reportTo = Utils.emptyUUID();
    private UUID tenantId = Utils.emptyUUID();
    private String applicationId = "";
    private int sortOrder = 0;

    public int getSourceEntityType() {
        return sourceEntityType;
    }

    public void setSourceEntityType(int sourceEntityType) {
        setPropertyChanged(this.sourceEntityType, sourceEntityType);
        this.sourceEntityType = sourceEntityType;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        setPropertyChanged(this.linkType, linkType);
        this.linkType = linkType;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        setPropertyChanged(this.intValue, intValue);
        this.intValue = intValue;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        setPropertyChanged(this.sortOrder, sortOrder);
        this.sortOrder = sortOrder;
    }

    public UUID getReportTo() {
        return reportTo;
    }

    public void setReportTo(UUID reportTo) {
        setPropertyChanged(this.reportTo, reportTo);
        this.reportTo = reportTo;
    }

    public UUID getSourceEntityId() {
        return sourceEntityId;
    }

    public void setSourceEntityId(UUID sourceEntityId) {
        setPropertyChanged(this.sourceEntityId, sourceEntityId);
        this.sourceEntityId = sourceEntityId;
    }

    public boolean isBoolValue() {
        return boolValue;
    }

    public void setBoolValue(boolean boolValue) {
        setPropertyChanged(this.boolValue, boolValue);
        this.boolValue = boolValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        setPropertyChanged(this.stringValue, stringValue);
        this.stringValue = stringValue;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        setPropertyChanged(this.applicationId, applicationId);
        this.applicationId = applicationId;
    }

    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        /// It is used to validate the first name null or empty.
        if (sourceEntityId.equals(Utils.emptyUUID())) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Source"});
            message.add("Source required");
        }
        if (message.size() == 0) {
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
        PFUserEntityLink userEntityLink = (PFUserEntityLink) entity;
        userEntityLink.setEntityId(this.entityId);
        userEntityLink.setTenantId(this.tenantId);
        userEntityLink.setSourceEntityId(this.sourceEntityId);
        userEntityLink.setSourceEntityType(this.sourceEntityType);
        userEntityLink.setLinkType(this.linkType);
        userEntityLink.setIntValue(this.intValue);
        userEntityLink.setStringValue(this.stringValue);
        userEntityLink.setBoolValue(this.boolValue);
        userEntityLink.setLastOperationType(this.lastOperationType);
        userEntityLink.setUpdatedAt(this.updatedAt);
        userEntityLink.setUpdatedBy(this.updatedBy);
        userEntityLink.setCreatedAt(this.createdAt);
        userEntityLink.setCreatedBy(this.createdBy);
        return userEntityLink;
    }
}
