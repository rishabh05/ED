//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.cyclicnotification;


import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationAttachmentData extends BaseData<NotificationAttachment> {

    /**
     * GET NOTIFICATION_ATTACHMENT entity
     */
    @Override
    public NotificationAttachment createEntity() {
        return NotificationAttachment.createEntity();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object type
     * @return NOTIFICATION_ATTACHMENT
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public NotificationAttachment getEntity(Object id) throws EwpException {
        String sql = "SELECT * From PFNotificationAttachment where NotificationAttachmentId= '" + id.toString() + "'";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the PFNotificationAttachment Entity from database.
     * Return Collection of NOTIFICATION_ATTACHMENT Entity.
     *
     * @return List<NOTIFICATION_ATTACHMENT>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public List<NotificationAttachment> getList() throws EwpException {
        String sql = "SELECT * From PFNotificationAttachment";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET PFNotificationAttachment Entity that matches the id and return result as! a ResultSet.
     *
     * @param id Object type
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFNotificationAttachment where NotificationAttachmentId= '" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all PFNotificationAttachment Entity record from database and return result as! a ResultSet.
     *
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFNotificationAttachment";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * DELETE PFNotificationAttachment entity.
     *
     * @param id UUID
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("PFNotificationAttachment", "NotificationAttachmentId=?", new String[]{id.toString()});
    }

    @Override
    public long insertEntity(NotificationAttachment entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("NotificationAttachmentId", entity.getEntityId().toString());
        values.put("Filename", entity.getFileName());
        values.put("FileString", entity.getFileString());
        values.put("FilePath", entity.getFilePath());
        values.put("EmbeddedAttachment", entity.getEmbeddedAttachment());
        values.put("NotificationQueueId", entity.getNotificationQueueId().toString());
        values.put("ApplicationId", entity.getApplicationId());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFNotificationAttachment", values);
    }

    @Override
    public void update(NotificationAttachment entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("NotificationQueueId", entity.getNotificationQueueId().toString());
        values.put("Filename", entity.getFileName());
        values.put("FileString", entity.getFileString());
        values.put("FilePath", entity.getFilePath());
        values.put("EmbeddedAttachment", entity.getEmbeddedAttachment());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("PFNotificationAttachment", values, "NotificationAttachmentId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Generate sql string with minimum required fields for PFNotificationAttachment.
     *
     * @return STRING
     */
    private String getSQL() {
        return " SELECT * FROM PFNotificationAttachment ";
    }

    public List<NotificationAttachment> getListByNotificationQueueId(UUID notificationQueueid) throws EwpException {
        String sql = getSQL() + " where NotificationQueueid = '" + notificationQueueid + "' ";

        return executeSqlAndGetNotificationAttachmentList(sql);
    }

    /**
     * It execute SQL query and returns NOTIFICATION_ATTACHMENT collections or nil.
     *
     * @param sql STRING
     * @return List<NOTIFICATION_ATTACHMENT>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public List<NotificationAttachment> executeSqlAndGetNotificationAttachmentList(String sql) throws EwpException {
        DatabaseOps db = DatabaseOps.defaultDatabase();
        Cursor resultSet = db.executeQuery(sql, null);

        // loop through the item and create the entity and set repactive properties.
        List<NotificationAttachment> listEntity = new ArrayList<>();
        if (resultSet != null) {
            while (resultSet.moveToNext()) {
                // getting the entity instance
                NotificationAttachment entity = new NotificationAttachment();
                // Set the entity properties value from resultset
                setPropertiesFromResultSet(entity, resultSet);
                listEntity.add(entity);
            }
            return listEntity;
        }
        return listEntity;
    }

    /**
     * @param entity    NOTIFICATION_ATTACHMENT
     * @param resultSet Cursor
     * @throws EwpException
     */
    @Override
    public void setPropertiesFromResultSet(NotificationAttachment entity, Cursor resultSet) throws EwpException {

        String id = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id).toString());
        }
        String employeeStatusGroupMemberId = resultSet.getString(resultSet.getColumnIndex("NotificationAttachmentId"));
        if (employeeStatusGroupMemberId != null && !"".equals(employeeStatusGroupMemberId)) {
            entity.setEntityId(UUID.fromString(employeeStatusGroupMemberId));
        }

        String filename = resultSet.getString(resultSet.getColumnIndex("Filename"));
        if (filename != null) {
            entity.setFileName(filename);
        }

        String file = resultSet.getString(resultSet.getColumnIndex("FileString"));
        if (file != null) {
            entity.setFileString(file);
        }

        String filePath = resultSet.getString(resultSet.getColumnIndex("FilePath"));
        if (filePath != null) {
            entity.setFilePath(filePath);
        }

        Boolean embeddedAttachment = resultSet.getInt(resultSet.getColumnIndex("EmbeddedAttachment")) == 1;
        if (embeddedAttachment != null) {
            entity.setEmbeddedAttachment(embeddedAttachment);
        }

        String notificationQueueId = resultSet.getString(resultSet.getColumnIndex("NotificationQueueId"));
        if (notificationQueueId != null && !"".equals(notificationQueueId)) {
            entity.setNotificationQueueId(UUID.fromString(notificationQueueId));
        }

        String applicationId = resultSet.getString(resultSet.getColumnIndex("ApplicationId"));
        if (applicationId != null) {
            entity.setApplicationId(applicationId);
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
        if (updatedBy != null && !"".equals(updatedBy)) {
            entity.setUpdatedBy(UUID.fromString(updatedBy).toString());
        }

        String updateAt = resultSet.getString(resultSet.getColumnIndex("ModifiedDate"));
        if (updateAt != null && !"".equals(updateAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updateAt, true, true));
        }
        entity.setDirty(false);
    }
}
