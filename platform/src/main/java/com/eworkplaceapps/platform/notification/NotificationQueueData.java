//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.notification;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.List;
import java.util.UUID;


public class NotificationQueueData extends BaseData<NotificationQueue> {

    /**
     * GET NOTIFICATION_QUEUE entity
     *
     * @return BaseEntity
     */
    public BaseEntity createEntity() {
        return NotificationQueue.createEntity();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object
     * @return NOTIFICATION_QUEUE
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public NotificationQueue getEntity(Object id) throws EwpException {
        String sql = "SELECT * From PFNotificationQueue where NotificationQueueId= '" + id.toString() + "'";

        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the NOTIFICATION_QUEUE Entity from database.
     * Return Collection of NOTIFICATION_QUEUE Entity.
     *
     * @return List<NOTIFICATION_QUEUE>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public List<NotificationQueue> getList() throws EwpException {
        String sql = "SELECT * From PFNotificationQueue";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET NOTIFICATION_QUEUE Entity that matches the id and return result as! a ResultSet.
     *
     * @param id Object
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFNotificationQueue where NotificationQueueId= '" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all NOTIFICATION_QUEUE Entity record from database and return result as! a ResultSet.
     *
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFNotificationQueue";
        return executeSqlAndGetResultSet(sql);
    }

    // DELETE NOTIFICATION_QUEUE entity.

    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("PFNotificationQueue", "NotificationQueueId=?", new String[]{id.toString()});
    }

    @Override
    public long insertEntity(NotificationQueue entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("NotificationQueueId", entity.getEntityId().toString());
        values.put("DeliveryType", entity.getDeliveryType());
        values.put("DeliverySubType", entity.getDeliverySubType());
        values.put("TargetInfo", entity.getTargetInfo());
        values.put("Message1", entity.getMessage1());
        values.put("Message2", entity.getMessage2());
        values.put("DeliveryTime", Utils.getUTCDateTimeAsString(entity.getDeliveryTime()));
        values.put("SenderId", entity.getSenderId().toString());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFNotificationQueue", values);
    }

    @Override
    public void update(NotificationQueue entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("DeliveryType", entity.getDeliveryType());
        values.put("DeliverySubType", entity.getDeliverySubType());
        values.put("TargetInfo", entity.getTargetInfo());
        values.put("Message1", entity.getMessage1());
        values.put("Message2", entity.getMessage2());
        values.put("DeliveryTime", entity.getDeliveryTime().toString());
        values.put("SenderId", entity.getSenderId().toString());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("PFNotificationQueue", values, "NotificationQueueId=?", new String[]{entity.getEntityId().toString()});
    }

    // Generate sql string with minimum required fields for NOTIFICATION_QUEUE.
    private String getSQL() {
        return " SELECT * FROM PFNotificationQueue ";
    }

    /**
     * It is used to get the current undelivered notification list from delivery and notification type.
     *
     * @return List<NOTIFICATION_QUEUE>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public List<NotificationQueue> getNotificationQueueFromCurrentDeliveryTime() throws EwpException {
        String sql = getSQL() + " WHERE DATETIME(DeliveryTime) <= DATETIME('now') ";

        return super.executeSqlAndGetEntityList(sql);
    }

    /**
     * @param entity    NOTIFICATION_QUEUE
     * @param resultSet Cursor
     * @throws EwpException
     */
    @Override
    public void setPropertiesFromResultSet(NotificationQueue entity, Cursor resultSet) throws EwpException {
        String id = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String employeeStatusGroupMemberId = resultSet.getString(resultSet.getColumnIndex("NotificationQueueId"));
        if (employeeStatusGroupMemberId != null && !"".equals(employeeStatusGroupMemberId)) {
            entity.setEntityId(UUID.fromString(employeeStatusGroupMemberId));
        }

        String deliverySubType = resultSet.getString(resultSet.getColumnIndex("DeliverySubType"));
        if (deliverySubType != null && !"".equals(deliverySubType)) {
            entity.setDeliverySubType(Integer.parseInt(deliverySubType));
        }

        String deliveryType = resultSet.getString(resultSet.getColumnIndex("DeliveryType"));
        if (deliveryType != null && !"".equals(deliveryType)) {
            entity.setDeliveryType(Integer.parseInt(deliveryType));
        }

        String targetInfo = resultSet.getString(resultSet.getColumnIndex("TargetInfo"));
        if (targetInfo != null && !"".equals(targetInfo)) {
            entity.setTargetInfo(targetInfo);
        }

        String message1 = resultSet.getString(resultSet.getColumnIndex("Message1"));
        if (message1 != null && !"".equals(message1)) {
            entity.setMessage1(message1);
        }

        String message2 = resultSet.getString(resultSet.getColumnIndex("Message2"));
        if (message2 != null) {
            entity.setMessage2(message2);
        }

        String senderId = resultSet.getString(resultSet.getColumnIndex("SenderId"));
        if (senderId != null) {
            entity.setSenderId(senderId);
        }

        String deliveryTime = resultSet.getString(resultSet.getColumnIndex("deliveryTime"));
        if (deliveryTime != null && !"".equals(deliveryTime)) {
            entity.setDeliveryTime(Utils.dateFromString(deliveryTime,true,true));
        }

        String createdBy = resultSet.getString(resultSet.getColumnIndex("CreatedBy"));
        if (createdBy != null && !"".equals(createdBy)) {
            entity.setCreatedBy(UUID.fromString(createdBy).toString());
        }

        String createdAt = resultSet.getString(resultSet.getColumnIndex("CreatedDate"));
        if (createdAt != null && !"".equals(createdAt)) {
            entity.setCreatedAt(Utils.dateFromString(createdAt,true,true));
        }

        String updatedBy = resultSet.getString(resultSet.getColumnIndex("ModifiedBy"));
        if (updatedBy != null && !"".equals(updatedBy)) {
            entity.setUpdatedBy(UUID.fromString(updatedBy).toString());
        }

        String updatedAt = resultSet.getString(resultSet.getColumnIndex("ModifiedDate"));
        if (updatedAt != null && !"".equals(updatedAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updatedAt,true,true));
        }
        entity.setDirty(false);
    }
}
