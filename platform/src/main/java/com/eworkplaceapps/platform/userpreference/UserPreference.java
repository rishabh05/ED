//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/21/2015
//===============================================================================
package com.eworkplaceapps.platform.userpreference;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * USER_PREFERENCE
 */
public class UserPreference extends BaseEntity {
    private static String USER_PREFERENCE_ENTITY_NAME = "USER_PREFERENCE";

    public UserPreference() {
        super(USER_PREFERENCE_ENTITY_NAME);
    }

    /**
     * Create userPreference object and return created object.
     *
     * @return USER_PREFERENCE
     */
    public static UserPreference createEntity() {
        return new UserPreference();
    }

    private UUID tenantId = UUID.randomUUID();

    /**
     * Getter/setter for user id of userPreference
     */
    private UUID userId = UUID.randomUUID();
    private String applicationId, preferenceName1, preferenceName2, dataValue;
    private int dataType;

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        setPropertyChanged(this.userId, userId);
        this.userId = userId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        setPropertyChanged(this.applicationId, applicationId);
        this.applicationId = applicationId;
    }

    public String getPreferenceName1() {
        return preferenceName1;
    }

    public void setPreferenceName1(String preferenceName1) {
        setPropertyChanged(this.preferenceName1, preferenceName1);
        this.preferenceName1 = preferenceName1;
    }

    public String getPreferenceName2() {
        return preferenceName2;
    }

    public void setPreferenceName2(String preferenceName2) {
        setPropertyChanged(this.preferenceName2, preferenceName2);
        this.preferenceName2 = preferenceName2;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        setPropertyChanged(this.dataType, dataType);
        this.dataType = dataType;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        setPropertyChanged(this.dataValue, dataValue);
        this.dataValue = dataValue;
    }

    /**
     * It validates USER_PREFERENCE entity.
     *
     * @return Boolean
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        if (message.isEmpty()) {
            return true;
        } else {
            throw new EwpException(new EwpException("Validation Exception in USER_PREFERENCE"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

    @Override
    public BaseEntity copyTo(BaseEntity baseEntity) {
        // If both entities are not same then return the nil.
        if (!baseEntity.getEntityName().equals(this.entityName)) {
            return null;
        }
        UserPreference t = (UserPreference) baseEntity;
        t.entityId = this.entityId;
        t.userId = this.userId;
        t.applicationId = this.applicationId;
        t.preferenceName1 = this.preferenceName1;
        t.preferenceName2 = this.preferenceName2;
        t.dataType = this.dataType;
        t.dataValue = this.dataValue;
        t.createdAt = this.createdAt;
        t.createdBy = this.createdBy;
        t.updatedAt = this.updatedAt;
        t.updatedBy = this.updatedBy;
        t.tenantId = this.tenantId;
        return t;
    }
}
