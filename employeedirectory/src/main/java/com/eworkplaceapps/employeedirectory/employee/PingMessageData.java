//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/22/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.List;
import java.util.UUID;

/**
 *
 */
public class PingMessageData extends BaseData<PingMessage> {
    @Override
    public PingMessage createEntity() {
        return PingMessage.createEntity();
    }

    @Override
    public PingMessage getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where emp.PingMessageId = '" + ((UUID) id).toString() + "'";
        return executeSqlAndGetEntity(sql);
    }

    @Override
    public List<PingMessage> getList() throws EwpException {
        String sql = getSQL() + " ORDER BY DATETIME(pm.SentTime) ";
        return executeSqlAndGetEntityList(sql);
    }

    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From EDPingMessage where PingMessageId= '" + ((UUID) id).toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From EDPingMessage";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public void delete(PingMessage entity) throws EwpException {
        super.deleteRows("EDPingMessage", "PingMessageId", new String[]{entity.getEntityId().toString()});
    }

    @Override
    public long insertEntity(PingMessage entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("PingMessageId", UUID.randomUUID().toString());
        values.put("Message", entity.getMessage());
        values.put("FromId", entity.getFromId().toString());
        values.put("ToId", entity.getToId().toString());
        values.put("SentTime", entity.getSentTime().toString());
        values.put("ParentPingMessageId", entity.getParentPingMessageId().toString());
        values.put("CreatedBy", entity.getCreatedBy().toString());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate",Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("EDPingMessage", values);
    }

    @Override
    public void update(PingMessage entity) throws EwpException {
        ContentValues values = new ContentValues();
        super.update("EDPingMessage", values, "", new String[]{});
    }

    /**
     * Get all the PingMessage Entity that matches the message sender and receiverid.
     * Return Collection of PingMessage Entity.
     *
     * @param messageSenderId
     * @param messageReceiverId
     * @return List<PingMessageData>
     */
    public List<PingMessage> getPingMessageListFromSenderAndReceiverId(UUID messageSenderId, UUID messageReceiverId) throws EwpException {
        String sql = getSQL();
        if (messageSenderId != null && messageReceiverId != null) {
            sql += " WHERE FromId = '" + messageSenderId.toString() + "' AND  ToId = '" + messageReceiverId.toString() + "' ";
        } else if (messageSenderId != null) {
            sql += " WHERE FromId = '" + messageSenderId.toString() + "' ";
        } else if (messageReceiverId != null) {
            sql += " WHERE ToId = '" + messageReceiverId.toString() + "' ";
        }
        sql += " ORDER BY DATETIME(pm.SentTime) DESC";
        return executeSqlAndGetEntityList(sql);
    }

    private String getSQL() {
        /// Generating the PingMessage SQL Statement to get the PingMessage detail. It will give the PingMessage detail with status.
        String sql = "Select pm.* ";
        sql += "from EDPingMessage As pm ";
        return sql;
    }

    @Override
    public void setPropertiesFromResultSet(PingMessage entity, Cursor cursor) throws EwpException {

        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }
        String employeeId = cursor.getString(cursor.getColumnIndex("PingMessageId"));
        if (employeeId != null && !"".equals(employeeId)) {
            entity.setEntityId(UUID.fromString(employeeId));
        }
        String message = cursor.getString(cursor.getColumnIndex("Message"));
        if (message != null) {
            entity.setMessage(message);
        }
        String fromId = cursor.getString(cursor.getColumnIndex("FromId"));
        if (fromId != null && !"".equals(fromId)) {
            entity.setFromId(UUID.fromString(fromId));
        }
        String toId = cursor.getString(cursor.getColumnIndex("ToId"));
        if (toId != null && !"".equals(toId)) {
            entity.setToId(UUID.fromString(toId));
        }
        String parentPingMessageId = cursor.getString(cursor.getColumnIndex("ParentPingMessageId"));
        if (parentPingMessageId != null && !"".equals(parentPingMessageId)) {
            entity.setParentPingMessageId(UUID.fromString(parentPingMessageId));
        }
        String date = cursor.getString(cursor.getColumnIndex("SentTime"));
        if (date != null && !"".equals(date)) {
            entity.setSentTime(Utils.dateFromString(date, true, true));
        }
        String createdBy = cursor.getString(cursor.getColumnIndex("CreatedBy"));
        if (createdBy != null && !"".equals(createdBy)) {
            entity.setCreatedBy(createdBy);
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
        entity.setDirty(false);
    }
}
