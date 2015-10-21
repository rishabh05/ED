//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/29/2015
//===============================================================================

package com.eworkplaceapps.platform.cyclicnotification;


import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.List;
import java.util.UUID;

public class CyclicNotificationDetailsData extends BaseData<CyclicNotificationDetails> {

    /**
     * GET CYCLIC_NOTIFICATION_DETAILS entity
     *
     * @return CYCLIC_NOTIFICATION_DETAILS
     */
    @Override
    public CyclicNotificationDetails createEntity() {
        return CyclicNotificationDetails.createEntity();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object type
     * @return CYCLIC_NOTIFICATION_DETAILS
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public CyclicNotificationDetails getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where CyclicNotificationDetailsId= '" + id.toString() + "'";

        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the CYCLIC_NOTIFICATION_DETAILS Entity from database.
     * Return Collection of CYCLIC_NOTIFICATION_DETAILS Entity.
     *
     * @return List<CYCLIC_NOTIFICATION_DETAILS>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public List<CyclicNotificationDetails> getList() throws EwpException {
        String sql = "SELECT * From PFCyclicNotificationDetails";

        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET CYCLIC_NOTIFICATION_DETAILS Entity that matches the id and return result as! a ResultSet.
     *
     * @param id Object type
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = getSQL() + " where CyclicNotificationDetailsId= '" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all CYCLIC_NOTIFICATION_DETAILS Entity record from database and return result as! a ResultSet.
     *
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFCyclicNotificationDetails";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * DELETE CYCLIC_NOTIFICATION_DETAILS entity.
     *
     * @param id UUID
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("PFCyclicNotificationDetails", "CyclicNotificationDetailsId=?", new String[]{id.toString()});
    }

    /**
     * Generate sql string with minimum required fields for CYCLIC_NOTIFICATION_DETAILS.
     *
     * @return STRING
     */

    private String getSQL() {
        return " SELECT * FROM PFCyclicNotificationDetails cd Inner join PFCyclicNotification cn on cn.CyclicNotificationId = cd.CyclicNotificationId ";
    }

    public List<CyclicNotificationDetails> getCyclicNotificationDetailsListByCyclicNotificationId(UUID cyclicNotificationId) throws EwpException {
        String sql = getSQL();
        sql += " Where cd.CyclicNotificationId= '" + cyclicNotificationId.toString() + "' order by Subscriptionday";
        return executeSqlAndGetEntityList(sql);
    }

    @Override
    public long insertEntity(CyclicNotificationDetails entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("CyclicNotificationDetailsId", entity.getEntityId().toString());
        values.put("SubscriptionDay", entity.getSubscriptionDay());
        values.put("SubscriptionDateTime", entity.getSubscriptionDateTime().toString());
        values.put("CyclicNotificationId", entity.getCyclicNotificationId().toString());
        values.put("ApplicationId", entity.getApplicationId());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFCyclicNotificationDetails", values);
    }

    @Override
    public void update(CyclicNotificationDetails entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("SubscriptionDay", entity.getSubscriptionDay());
        values.put("SubscriptionDateTime", entity.getSubscriptionDateTime().toString());
        values.put("CyclicNotificationId", entity.getCyclicNotificationId().toString());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("PFCyclicNotificationDetails", values, "CyclicNotificationDetailsId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * @param resultSet cursor Obj
     * @throws EwpException
     */
    @Override
    public void setPropertiesFromResultSet(CyclicNotificationDetails entity, Cursor resultSet) throws EwpException {

        String id = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String employeeStatusGroupMemberId = resultSet.getString(resultSet.getColumnIndex("CyclicNotificationDetailsId"));
        if (employeeStatusGroupMemberId != null && !"".equals(employeeStatusGroupMemberId)) {
            entity.setEntityId(UUID.fromString(employeeStatusGroupMemberId));
        }

        String subscriptionDay = resultSet.getString(resultSet.getColumnIndex("SubscriptionDay"));
        if (subscriptionDay != null && !"".equals(subscriptionDay)) {
            entity.setSubscriptionDay(Integer.parseInt(subscriptionDay));
        }

        String subscriptionDateTime = resultSet.getString(resultSet.getColumnIndex("SubscriptionDateTime"));
        if (subscriptionDateTime != null && !"".equals(subscriptionDateTime)) {
            entity.setSubscriptionDateTime(Utils.dateFromString(subscriptionDateTime, true, true));
        }

        String cyclicNotificationId = resultSet.getString(resultSet.getColumnIndex("CyclicNotificationId"));
        if (cyclicNotificationId != null && !"".equals(cyclicNotificationId)) {
            entity.setCyclicNotificationId(UUID.fromString(cyclicNotificationId));
        }

        String createdBy = resultSet.getString(resultSet.getColumnIndex("CreatedBy"));
        if (createdBy != null) {
            entity.setCreatedBy(UUID.fromString(createdBy).toString());
        }

        String createdAt = resultSet.getString(resultSet.getColumnIndex("CreatedDate"));
        if (createdAt != null && !"".equals(createdAt)) {
            entity.setCreatedAt(Utils.dateFromString(createdAt, true, true));
        }

        String applicationId = resultSet.getString(resultSet.getColumnIndex("ApplicationId"));
        if (applicationId != null) {
            entity.setApplicationId(applicationId);
        }

        String updatedBy = resultSet.getString(resultSet.getColumnIndex("ModifiedBy"));
        if (updatedBy != null && !"".equals(updatedBy)) {
            entity.setUpdatedBy(UUID.fromString(updatedBy).toString());
        }

        String updatedAt = resultSet.getString(resultSet.getColumnIndex("ModifiedDate"));
        if (updatedAt != null && !"".equals(updatedAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updatedAt, true, true));
        }
        entity.setDirty(false);
    }
}
