//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class EmployeeContactData extends BaseData<EmployeeContact> {
    @Override
    public EmployeeContact createEntity() {
        return EmployeeContact.createEntity();
    }

    /**
     * Get an entity that matched the id
     *
     * @param id
     * @return EmployeeContact
     * @throws EwpException
     */
    @Override
    public EmployeeContact getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where LOWER(EmployeeContactId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Get all the EmployeeContact Entity from database.
     * Return Collection of EmployeeContact Entity.
     *
     * @return List<EmployeeContact>
     * @throws EwpException
     */
    @Override
    public List<EmployeeContact> getList() throws EwpException {
        return executeSqlAndGetEntityList(getSQL());
    }

    /**
     * Get EmployeeContact Entity that matches the id and return result as a ResultSet.
     *
     * @param id
     * @return Cursor
     * @throws EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From EDEmployeeContact where LOWER(EmployeeContactId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Get all EmployeeContact Entity record from database and return result as a ResultSet.
     *
     * @return Cursor
     * @throws EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        return executeSqlAndGetResultSet(getSQL());
    }

    /**
     * Delete EmployeeContact entity.
     *
     * @param entity
     * @throws EwpException
     */
    @Override
    public void delete(EmployeeContact entity) throws EwpException {
        deleteRows("EDEmployeeContact", "LOWER(EmployeeContactId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * @param entity EmployeeContact
     * @return long row id
     */
    @Override
    public long insertEntity(EmployeeContact entity) {
        entity.setEntityId(UUID.randomUUID());
        ContentValues values = new ContentValues();
        values.put("EmployeeContactId", entity.getEntityId().toString());
        values.put("EmployeeId", entity.getSourceEntityId().toString());
        values.put("ContactName", entity.getName());
        values.put("ContactType", entity.getContactType());
        values.put("PrimaryContact", entity.isPrimaryContact());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("EDEmployeeContact", values);
    }

    /**
     * @param entity
     * @throws EwpException
     */
    @Override
    public void update(EmployeeContact entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("ContactName", entity.getName());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("PrimaryContact", entity.isPrimaryContact());
        entity.setUpdatedAt(new Date());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("EDEmployeeContact", values, "LOWER(EmployeeContactId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * Generate sql string with minimum required fields for EmployeeContact.
     *
     * @return string
     */
    private String getSQL() {
        return "Select * from EDEmployeeContact";
    }

    /**
     * Get employeeContact from entityid.
     *
     * @param sourceEntityId
     * @return List<EmployeeContact>
     * @throws EwpException
     */
    public List<EmployeeContact> getEmployeeContactListFromSourceEntityIdAndType(UUID sourceEntityId) throws EwpException {
        String sql = getSQL() + " where LOWER(EmployeeId)= ('" + sourceEntityId.toString().toLowerCase() + "')";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * @param sourceEntityId
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getEmployeeContactListFromSourceEntityIdAndTypeAsResultSet(UUID sourceEntityId) throws EwpException {
        String sql = getSQL() + " where LOWER(EmployeeId)= LOWER('" + sourceEntityId.toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public void setPropertiesFromResultSet(EmployeeContact entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String addId = cursor.getString(cursor.getColumnIndex("EmployeeContactId"));
        if (addId != null && !"".equals(addId)) {
            entity.setEntityId(UUID.fromString(addId));
        }

        String contactName = cursor.getString(cursor.getColumnIndex("ContactName"));
        if (contactName != null) {
            entity.setName(contactName);
        }

        String entityId = cursor.getString(cursor.getColumnIndex("EmployeeId"));
        if (entityId != null && !"".equals(entityId)) {
            entity.setSourceEntityId(UUID.fromString(entityId));
        }
        String primaryContact = cursor.getString(cursor.getColumnIndex("PrimaryContact"));
        entity.setPrimaryContact("1".equals(primaryContact));

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
