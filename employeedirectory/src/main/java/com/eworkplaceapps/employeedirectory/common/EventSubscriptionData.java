//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Sourabh Agrawal
// Original DATE:  10/7/2015.
//===============================================================================
package com.eworkplaceapps.employeedirectory.common;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.employeedirectory.department.Department;
import com.eworkplaceapps.employeedirectory.location.Location;
import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EventSubscriptionData extends BaseData<EventSubscription> {


    @Override
    public EventSubscription createEntity() {
        return EventSubscription.createEntity();
    }
    /**
     * Get all the EventSubscription Entity from database.
     * @return List<EventSubscription>
     * @throws EwpException
     */
    @Override
    public List<EventSubscription> getList() throws EwpException {
        String sql = getSQL() + " ORDER By SortOrder ";
        return executeSqlAndGetEntityList(sql);
    }
    /**
     * Delete EventSubscription entity.
     * @param entity
     * @throws EwpException
     */
    @Override
    public void delete(EventSubscription entity) throws EwpException {
        super.deleteRows("EventSubscription", "LOWER(EventSubscriptionId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }


    @Override
    public long insertEntity(EventSubscription entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        entity.setTenantId(EwpSession.getSharedInstance().getTenantId());
        values.put("EventSubscriptionId", entity.getEntityId().toString());
        values.put("OwnerEmployeeId", entity.getOwnerEmployeeId().toString());
        values.put("ParentEntityType", entity.getParentEntityType());
        values.put("ParentEntityId", entity.getParentEntityId());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("TenantId", entity.getTenantId().toString());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        long id = super.insert("EventSubscription", values);
        return id;
    }

    @Override
    public void update(EventSubscription entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("OwnerEmployeeId", entity.getOwnerEmployeeId().toString());
        values.put("SubscribedEvents", entity.getSubscribedEvents());
        Date d = new Date();
        entity.setUpdatedAt(d);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
        super.update("EventSubscription", values, "LOWER(EventSubscriptionId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }
    /**
     * Generate sql string with minimum required fields for EventSubscription.
     *
     * @return string sql
     */
    private String getSQL() {
        // Generating the EventSubscription SQL Statement to get the EventSubscription detail. It will give the EventSubscription details.
        String sql = "Select *from EventSubscription";
        return sql;
    }
        @Override
    public void setPropertiesFromResultSet(EventSubscription entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String ownerEmployeeId = cursor.getString(cursor.getColumnIndex("ownerEmployeeId"));
        if (ownerEmployeeId != null && !"".equals(ownerEmployeeId)) {
            entity.setEntityId(UUID.fromString(ownerEmployeeId));
        }

        String parentEntityType = cursor.getString(cursor.getColumnIndex("parentEntityType"));
        if (parentEntityType != null && !"".equals(parentEntityType)) {
            entity.setParentEntityType(parentEntityType);
        }

        String parentEntityId = cursor.getString(cursor.getColumnIndex("parentEntityId"));
        if (parentEntityId != null && !"".equals(parentEntityId)) {
            entity.setParentEntityId(parentEntityId);
        }

        String subscribedEvents = cursor.getString(cursor.getColumnIndex("subscribedEvents"));
        if (subscribedEvents != null && !"".equals(subscribedEvents)) {
            entity.setSubscribedEvents(subscribedEvents);
        }


        String createdAt = cursor.getString(cursor.getColumnIndex("CreatedDate"));
        if (createdAt != null && !"".equals(createdAt)) {
            entity.setCreatedAt(Utils.dateFromString(createdAt, true, true));
        }

        String updatedBy = cursor.getString(cursor.getColumnIndex("ModifiedBy"));
        if (updatedBy != null && !"".equals(updatedBy)) {
            entity.setUpdatedBy(updatedBy);
        }

        String updatedAt = cursor.getString(cursor.getColumnIndex("ModifiedDate"));
        if (updatedAt != null && !"".equals(updatedAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updatedAt, true, true));
        }

    }
}
