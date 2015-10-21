//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.notification;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.List;
import java.util.UUID;

public class EventNotificationData extends BaseData<EventNotification> {

    /**
     * GET EVENT_NOTIFICATION entity
     *
     * @return EVENT_NOTIFICATION
     */
    @Override
    public EventNotification createEntity() {
        return EventNotification.createEntity();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object
     * @return EVENT_NOTIFICATION
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public EventNotification getEntity(Object id) throws EwpException {
        String sql = "SELECT * From PFEventNotification where EventNotificationId= '" + id.toString() + "'";

        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the EVENT_NOTIFICATION Entity from database.
     * Return Collection of EVENT_NOTIFICATION Entity.
     *
     * @return List<EVENT_NOTIFICATION>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public List<EventNotification> getList() throws EwpException {
        String sql = "SELECT * From PFEventNotification";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET EVENT_NOTIFICATION Entity that matches the id and return result as! a ResultSet.
     *
     * @param id Object
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFEventNotification where EventNotificationId= '" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all EVENT_NOTIFICATION Entity record from database and return result as! a ResultSet.
     *
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFEventNotification";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("PFEventNotification", "EventNotificationId=?", new String[]{id.toString()});
    }

    @Override
    public long insertEntity(EventNotification entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("EventNotificationId", entity.getEntityId().toString());
        values.put("OwnerId", entity.getOwnerId().toString());
        values.put("ParentEntityType", entity.getParentEntityType());
        values.put("EntityType", entity.getEntityType());
        values.put("ParentEntityId", entity.getParentEntityId().toString());
        values.put("EventTypeNo", entity.getEventTypeNo());
        values.put("ApplicationId", entity.getApplicationId());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFEventNotification", values);
    }

    @Override
    public void update(EventNotification entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("OwnerId", entity.getOwnerId().toString());
        values.put("ParentEntityType", entity.getParentEntityType());
        values.put("EntityType", entity.getEntityType());
        values.put("EventTypeNo", entity.getEventTypeNo());
        values.put("ParentEntityId", entity.getParentEntityId().toString());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("PFEventNotification", values, "EventNotificationId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Generate sql string with minimum required fields for EVENT_NOTIFICATION.
     *
     * @return STRING
     */
    private String getSQL() {
        return " SELECT * FROM PFEventNotification ";
    }

    public List<EventNotification> getEntityListByParentEntityTypeAndIdAndEventTypeNo(int parentEntityType, UUID parentEntityId, int eventTypeNo) throws EwpException {

        String sql = "SELECT * From PFEventNotification";
        sql += " WHERE ParentEntityType= '" + parentEntityType + "' AND ParentEntityId= '" + parentEntityId + "' " + " AND EventTypeNo='" + eventTypeNo + "' ";
        return executeSqlAndGetEntityList(sql);
    }

    public EventNotification getEventNotificationByOwnerAndEntityTypeAndEntityIdAndTenantIdAndAppId(UUID ownerId, int entityType, UUID entityId, UUID tenantId, String appId) throws EwpException {
        String sql = getSQL();
        sql += " Where OwnerId='" + ownerId.toString() + "' And EntityType=" + entityType + " AND ParentEntityId='" + entityId + "' And TenantId='" + ownerId + "' And ApplicationId= '" + appId + "' ";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * @param entity    EVENT_NOTIFICATION
     * @param resultSet Cursor
     * @throws EwpException
     */
    @Override
    public void setPropertiesFromResultSet(EventNotification entity, Cursor resultSet) throws EwpException {

        String id = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String employeeStatusGroupMemberId = resultSet.getString(resultSet.getColumnIndex("EventNotificationId"));
        if (employeeStatusGroupMemberId != null && !"".equals(employeeStatusGroupMemberId)) {
            entity.setEntityId(UUID.fromString(employeeStatusGroupMemberId));
        }

        String entityType = resultSet.getString(resultSet.getColumnIndex("EntityType"));
        if (entityType != null && !"".equals(entityType)) {
            entity.setEntityType(Integer.parseInt(entityType));
        }

        String parentEntityType = resultSet.getString(resultSet.getColumnIndex("ParentEntityType"));
        if (parentEntityType != null && !"".equals(parentEntityType)) {
            entity.setParentEntityType(Integer.parseInt(parentEntityType));
        }

        String eventTypeNo = resultSet.getString(resultSet.getColumnIndex("EventTypeNo"));
        if (eventTypeNo != null && !"".equals(eventTypeNo)) {
            entity.setEventTypeNo(Integer.parseInt(eventTypeNo));
        }

        String parentEntityId = resultSet.getString(resultSet.getColumnIndex("ParentEntityId"));
        if (parentEntityId != null && !"".equals(parentEntityId)) {
            entity.setParentEntityId(UUID.fromString(parentEntityId));
        }

        String createdBy = resultSet.getString(resultSet.getColumnIndex("CreatedBy"));
        if (createdBy != null && !"".equals(createdBy)) {
            entity.setCreatedBy(UUID.fromString(createdBy).toString());
        }

        String createdAt = resultSet.getString(resultSet.getColumnIndex("CreatedDate"));
        if (createdAt != null && !"".equals(createdAt)) {
            entity.setCreatedAt(Utils.dateFromString(createdAt, true, true));
        }

        String updatedBy = resultSet.getString(resultSet.getColumnIndex("ModifiedBy"));
        if (updatedBy != null) {
            entity.setUpdatedBy(UUID.fromString(updatedBy).toString());
        }

        String updatedAt = resultSet.getString(resultSet.getColumnIndex("ModifiedDate"));
        if (updatedAt != null && !"".equals(updatedAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updatedAt, true, true));
        }
        entity.setDirty(false);
    }

}
