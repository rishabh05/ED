//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Sourabh Agrawal
// Original DATE:  10/7/2015.
//===============================================================================
package com.eworkplaceapps.employeedirectory.common;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.utils.Utils;

import java.io.Serializable;
import java.util.UUID;

public class EventSubscription  extends BaseEntity implements Serializable {
    private static final String EventSubscription_Entity_Name = "EventSubscription";
    private String  parentEntityType = "", parentEntityId = "", subscribedEvents = "";
    private UUID ownerEmployeeId = Utils.emptyUUID();
    private UUID tenantId = Utils.emptyUUID();





     // Initilize the new instance of EventSubscription.
    public EventSubscription() {
        super(EventSubscription_Entity_Name);
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public UUID getOwnerEmployeeId() {
        return ownerEmployeeId;
    }

    public void setOwnerEmployeeId(UUID ownerEmployeeId) {
        this.ownerEmployeeId = ownerEmployeeId;
    }

    public EventSubscription(String entityName) {
        super(entityName);
    }

    public String getSubscribedEvents() {
        return subscribedEvents;
    }

    public void setSubscribedEvents(String subscribedEvents) {
        setPropertyChanged(this.subscribedEvents, subscribedEvents);
        this.subscribedEvents = subscribedEvents;
    }

    public String getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(String parentEntityId) {
        setPropertyChanged(this.parentEntityId, parentEntityId);
        this.parentEntityId = parentEntityId;
    }

    public String getParentEntityType() {
        return parentEntityType;
    }

    public void setParentEntityType(String parentEntityType) {
        setPropertyChanged(this.parentEntityType, parentEntityType);
        this.parentEntityType = parentEntityType;
    }





    /**
     * Create EventSubscription object and return created object.
     * @return
     */
    public static EventSubscription createEntity() {
        return new EventSubscription();
    }

    @Override
    public BaseEntity copyTo(BaseEntity entity) {
        /// If both entities are not same then return the entity.
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        EventSubscription eventSubscription = (EventSubscription) entity;
        eventSubscription.setTenantId(this.tenantId);
        eventSubscription.setOwnerEmployeeId(this.ownerEmployeeId);
        eventSubscription.setParentEntityId(this.parentEntityId);
        eventSubscription.setParentEntityType(this.parentEntityType);
        eventSubscription.setSubscribedEvents(this.subscribedEvents);
        return eventSubscription;
    }
}
