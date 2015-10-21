//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.notification;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.UUID;

/**
 * EVENT_NOTIFICATION will save the nofication data according to user configuration.
 * whenever user configure the setting upon add/update a entity or a data is updated by some 1 then EVENT_NOTIFICATION will save
 * that data in EVENT_NOTIFICATION table.
 */
public class EventNotification extends BaseEntity {

    static String EventNotification_Entity_Name = "PFEventNotification";

    public EventNotification() {
        super(EventNotification_Entity_Name);
    }

    // Create EVENT_NOTIFICATION object and return created object.
    public static EventNotification createEntity() {
        return new EventNotification();
    }

    // Notification definition owner id.
    // If notification is shared among user its empty otherwise keeps owner entity id.
    private UUID ownerId = Utils.emptyUUID();

    // Parenat Entity type where notification is linked.
    private int parentEntityType = 0;
    // Entity type where notification is linked.
    private int entityType = 0;

    // Notification entity id.
    private UUID parentEntityId = Utils.emptyUUID();

    // EVENT type that causes this notification to be trigger.
    private int eventTypeNo = 0;

    private String applicationId = "";

    private UUID tenantId;

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        setPropertyChanged(ownerId, this.ownerId);
        this.ownerId = ownerId;
    }

    public int getParentEntityType() {
        return parentEntityType;
    }

    public void setParentEntityType(int parentEntityType) {
        setPropertyChanged(parentEntityType, this.parentEntityType);
        this.parentEntityType = parentEntityType;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        setPropertyChanged(entityType, this.entityType);
        this.entityType = entityType;
    }

    public UUID getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(UUID parentEntityId) {
        setPropertyChanged(parentEntityId, this.parentEntityId);
        this.parentEntityId = parentEntityId;
    }

    public int getEventTypeNo() {
        return eventTypeNo;
    }

    public void setEventTypeNo(int eventTypeNo) {
        setPropertyChanged(eventTypeNo, this.eventTypeNo);
        this.eventTypeNo = eventTypeNo;
    }

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
}
