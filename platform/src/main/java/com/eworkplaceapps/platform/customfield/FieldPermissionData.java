//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.customfield;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.List;
import java.util.UUID;

/**
 * FieldPermissionData class provide the methods to get the permission on CUSTOM fields (user defined fields).
 * Field permission data provide the custom field permissions.
 * Field Pemrission work in reverse order, It contains non-accessible field information.
 */

public class FieldPermissionData extends BaseData<FieldPermission> {

    /**
     * @return FieldPermission
     */
    @Override
    public FieldPermission createEntity() {
        return new FieldPermission();
    }

    /**
     * @param id Object
     * @return FieldPermission
     * @throws EwpException
     */
    @Override
    public FieldPermission getEntity(Object id) throws EwpException {
        String sql = "SELECT * From PFEntityFieldPermission where EntityFieldPermissionId= '" + id.toString() + "'";

        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the ENTITY_CUSTOM_FIELD Entity from database.
     * Return Collection of ENTITY_CUSTOM_FIELD Entity.
     *
     * @return List<FieldPermission>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public List<FieldPermission> getList() throws EwpException {
        String sql = "SELECT * From PFEntityFieldPermission";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET ENTITY_CUSTOM_FIELD Entity that matches the id and return result as! a ResultSet.
     *
     * @param id Object
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFEntityFieldPermission where EntityFieldPermissionId= '" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all ENTITY_CUSTOM_FIELD Entity record from database and return result as! a ResultSet.
     *
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFEntityFieldPermission";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * DELETE ENTITY_CUSTOM_FIELD entity.
     *
     * @param id UUID
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public void delete(UUID id) throws EwpException {
        super.deleteRows("PFEntityFieldPermission", "EntityFieldPermissionId=?", new String[]{id.toString()});
    }

    /**
     * Generate sql string with minimum required fields for ENTITY_CUSTOM_FIELD.
     *
     * @return STRING
     */
    private String getSQL() {
        return " SELECT FP.* FROM PFEntityFieldPermission As FP Inner Join PFEntityFieldDetails As EC On FP.EntityFieldDetailsId = EC.EntityFieldDetailsId ";
    }

    @Override
    public long insertEntity(FieldPermission entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("EntityFieldPermissionId", entity.getEntityId().toString());
        values.put("ViewField", entity.isViewField());
        values.put("UpdateField", entity.isUpdateField());
        values.put("EntityFieldDetailsId", entity.getEntityFieldDetailsId().toString());
        values.put("RolePermissionId", entity.getRolePermissionId().toString());
        values.put("CreatedBy", entity.getCreatedBy().toString());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFEntityFieldPermission", values);
    }

    @Override
    public void update(FieldPermission entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("ViewField", entity.isViewField());
        values.put("UpdateField", entity.isUpdateField());
        values.put("RolePermissionId", entity.getRolePermissionId().toString());
        values.put("CreatedBy", entity.getCreatedBy().toString());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("TenantId", entity.getTenantId().toString());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        super.update("PFEntityFieldPermission", values, "EntityFieldPermissionId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Set property value from  database ResultSet.
     *
     * @param cursor Cursor, entity FieldPermission
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public void setPropertiesFromResultSet(FieldPermission entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String fieldPermissionId = cursor.getString(cursor.getColumnIndex("EntityFieldPermissionId"));
        if (fieldPermissionId != null && !"".equals(fieldPermissionId)) {
            entity.setEntityId(UUID.fromString(fieldPermissionId));
        }

        String rolePermissionId = cursor.getString(cursor.getColumnIndex("RolePermissionId"));
        if (rolePermissionId != null && !"".equals(rolePermissionId)) {
            entity.setRolePermissionId(UUID.fromString(rolePermissionId));
        }

        String entityFieldDetailsId = cursor.getString(cursor.getColumnIndex("EntityFieldDetailsId"));
        if (entityFieldDetailsId != null && !"".equals(entityFieldDetailsId)) {
            entity.setEntityFieldDetailsId(UUID.fromString(entityFieldDetailsId));
        }

        Boolean viewField = cursor.getInt(cursor.getColumnIndex("ViewField")) == 1;
        entity.setViewField(viewField);

        Boolean updateField = cursor.getInt(cursor.getColumnIndex("UpdateField")) == 1;
        if (!"".equals(updateField)) {
            entity.setUpdateField(updateField);
        }
        String createdDateStr = cursor.getString(cursor.getColumnIndex("CreatedDate"));
        if (createdDateStr != null && !"".equals(createdDateStr)) {
            entity.setCreatedAt(Utils.dateFromString(createdDateStr, true, true));
        }
        String updatedBy = cursor.getString(cursor.getColumnIndex("ModifiedBy"));
        if (updatedBy != null && !"".equals(updatedBy)) {
            entity.setUpdatedBy(UUID.fromString(updatedBy).toString());//check
        }
        String modifiedDateStr = cursor.getString(cursor.getColumnIndex("ModifiedDate"));
        if (modifiedDateStr != null && !"".equals(modifiedDateStr)) {
            entity.setUpdatedAt(Utils.dateFromString(modifiedDateStr, true, true));
        }
        entity.setDirty(false);
    }

}
