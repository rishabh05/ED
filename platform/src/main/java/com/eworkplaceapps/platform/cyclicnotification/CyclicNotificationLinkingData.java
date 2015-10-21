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

public class CyclicNotificationLinkingData extends BaseData<CyclicNotificationLinking> {

    /**
     * GET CYCLIC_NOTIFICATION_LINKING entity
     *
     * @return CYCLIC_NOTIFICATION_LINKING
     */
    public CyclicNotificationLinking createEntity() {
        return CyclicNotificationLinking.createEntity();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object type
     * @return CYCLIC_NOTIFICATION_LINKING
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    @Override
    public CyclicNotificationLinking getEntity(Object id) throws EwpException {
        String sql = "SELECT * From PFCyclicNotificationLinking where CyclicNotificationLinkingId= '" + id.toString() + "'";

        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the CYCLIC_NOTIFICATION_LINKING Entity from database.
     * Return Collection of CYCLIC_NOTIFICATION_LINKING Entity.
     *
     * @return List<CYCLIC_NOTIFICATION_LINKING>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    @Override
    public List<CyclicNotificationLinking> getList() throws EwpException {
        String sql = "SELECT * From PFCyclicNotificationLinking";

        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET CYCLIC_NOTIFICATION_LINKING Entity that matches the id and return result as! a FMResultSet.
     *
     * @param id Object type
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFCyclicNotificationLinking where CyclicNotificationLinkingId= '" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all CYCLIC_NOTIFICATION_LINKING Entity record from database and return result as! a FMResultSet.
     *
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFCyclicNotificationLinking";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * DELETE CYCLIC_NOTIFICATION_LINKING entity.
     *
     * @param id UUID
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("PFCyclicNotificationLinking", "CyclicNotificationLinkingId=?", new String[]{id.toString()});
    }

    @Override
    public long insertEntity(CyclicNotificationLinking entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("CyclicNotificationLinkingId", entity.getEntityId().toString());
        values.put("SourceEntityType", entity.getSourceEntityType());
        values.put("SourceEntityId", entity.getSourceEntityId().toString());
        values.put("ApplicationId", entity.getApplicationId());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFCyclicNotificationLinking", values);
    }

    @Override
    public void update(CyclicNotificationLinking entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("SourceEntityType", entity.getSourceEntityType());
        values.put("SourceEntityId", entity.getSourceEntityId().toString());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("PFCyclicNotificationLinking", values, "CyclicNotificationLinkingId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Generate sql string with minimum required fields for CYCLIC_NOTIFICATION_LINKING.
     *
     * @return STRING
     */
    private String getSQL() {
        return " SELECT * FROM PFCyclicNotificationLinking ";
    }

    /**
     * @param resultSet , entity
     * @throws EwpException
     */
    @Override
    public void setPropertiesFromResultSet(CyclicNotificationLinking entity, Cursor resultSet) throws EwpException {

        String id = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String employeeStatusGroupMemberId = resultSet.getString(resultSet.getColumnIndex("CyclicNotificationLinkingId"));
        if (employeeStatusGroupMemberId != null && !"".equals(employeeStatusGroupMemberId)) {
            entity.setEntityId(UUID.fromString(employeeStatusGroupMemberId));
        }

        String sourceEntityId = resultSet.getString(resultSet.getColumnIndex("SourceEntityId"));
        if (sourceEntityId != null && !"".equals(sourceEntityId)) {
            entity.setSourceEntityId(UUID.fromString(sourceEntityId));
        }

        String sourceEntityType = resultSet.getString(resultSet.getColumnIndex("SourceEntityType"));
        if (sourceEntityType != null && !"".equals(sourceEntityType)) {
            entity.setSourceEntityType(Integer.parseInt(sourceEntityType));
        }

        String createdBy = resultSet.getString(resultSet.getColumnIndex("CreatedBy"));
        if (createdBy != null && !"".equals(createdBy)) {
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
