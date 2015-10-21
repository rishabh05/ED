//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.cyclicnotification;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.UUID;


public class CyclicNotificationLinking extends BaseEntity {

    static String CyclicNotificationLinking_Entity_Name = "PFCyclicNotificationLinking";

    public CyclicNotificationLinking() {
        super(CyclicNotificationLinking_Entity_Name);
    }


    private UUID sourceEntityId = Utils.emptyUUID();
    private UUID cyclicNotificationId = Utils.emptyUUID();
    private String applicationId = "";
    private UUID tenantId;

    public static CyclicNotificationLinking createEntity() {
        return new CyclicNotificationLinking();
    }

    private int sourceEntityType = 0;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public int getSourceEntityType() {
        return sourceEntityType;
    }

    public void setSourceEntityType(int sourceEntityType) {
        setPropertyChanged(sourceEntityType, this.sourceEntityType);
        this.sourceEntityType = sourceEntityType;
    }

    public UUID getSourceEntityId() {
        return sourceEntityId;
    }

    public void setSourceEntityId(UUID sourceEntityId) {
        setPropertyChanged(sourceEntityId, this.sourceEntityId);
        this.sourceEntityId = sourceEntityId;
    }

    public UUID getCyclicNotificationId() {
        return cyclicNotificationId;
    }

    public void setCyclicNotificationId(UUID cyclicNotificationId) {
        setPropertyChanged(cyclicNotificationId, this.cyclicNotificationId);
        this.cyclicNotificationId = cyclicNotificationId;
    }
}
