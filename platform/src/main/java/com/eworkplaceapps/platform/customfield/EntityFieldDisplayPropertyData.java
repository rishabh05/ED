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

public class EntityFieldDisplayPropertyData extends BaseData<EntityFieldDisplayProperty> {

    /**
     * @return EntityFieldDisplayProperty
     */
    @Override
    public EntityFieldDisplayProperty createEntity() {
        return new EntityFieldDisplayProperty();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object
     * @return EntityFieldDisplayProperty
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    @Override
    public EntityFieldDisplayProperty getEntity(Object id) throws EwpException {
        // Building select statement
        String sql = "SELECT * From PFEntityFieldDisplayProperty where EntityFieldDisplayPropertyId='" + ((UUID) id).toString() + "'";
        // Executes select statement and return ROLE entity
        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the ENTITY_CUSTOM_FIELD Entity from database.
     * Return Collection of ENTITY_CUSTOM_FIELD Entity.
     *
     * @return List<EntityFieldDisplayProperty>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    @Override
    public List<EntityFieldDisplayProperty> getList() throws EwpException {
        String sql = "SELECT * From PFEntityFieldDisplayProperty";

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
        String sql = "SELECT * From PFEntityFieldDisplayProperty where EntityFieldDisplayPropertyId= '" + id.toString() + "'";
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
        String sql = "SELECT * From PFEntityFieldDisplayProperty";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * DELETE ENTITY_CUSTOM_FIELD entity.
     *
     * @param entityId UUID
     * @return STRING
     */

    public String buildDeleteStatement(UUID entityId) {
        return "DELETE From PFEntityFieldDisplayProperty Where EntityFieldDisplayPropertyId = '" + (entityId) + "'";
    }

    /**
     * @param entityType int
     * @param tenantId   UUID
     * @return int
     * @throws EwpException
     */
    public int getMaxDisplayOrder(int entityType, UUID tenantId) throws EwpException {
        String sql = getSQL() + " FP.TenantId = '" + tenantId.toString() + "' FP.EntityType = '" + entityType + "' ";

        Cursor resultSet = super.executeSqlAndGetResultSet(sql);
        if (resultSet != null) {
            resultSet.moveToFirst();
            while (resultSet.moveToNext()) {
                String displayOrder = resultSet.getString(resultSet.getColumnIndex("DisplayOrder"));
                if (displayOrder != null) {
                    return Integer.valueOf(displayOrder);
                }
            }
        }
        return 0;
    }

    @Override
    public long insertEntity(EntityFieldDisplayProperty entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("EntityFieldDisplayPropertyId", entity.getEntityId().toString());
        values.put("EntityType", entity.getEntityType());
        values.put("ApplicationId", entity.getApplicationId());
        values.put("FieldName", entity.getFieldName());
        values.put("DisplayOrder", entity.getDisplayOrder());
        values.put("System", entity.getSystem());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFEntityFieldDisplayProperty", values);
    }

    @Override
    public void update(EntityFieldDisplayProperty entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("FieldName", entity.getFieldName());
        values.put("DisplayOrder", entity.getDisplayOrder());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("TenantId", entity.getTenantId().toString());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        super.update("PFEntityFieldDisplayProperty", values, "EntityFieldDisplayPropertyId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Set property value from  database ResultSet.
     *
     * @param cursor,entity EntityFieldDisplayProperty
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public void setPropertiesFromResultSet(EntityFieldDisplayProperty entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String entityFieldDisplayPropertyId = cursor.getString(cursor.getColumnIndex("EntityFieldDisplayPropertyId"));
        if (entityFieldDisplayPropertyId != null && !"".equals(entityFieldDisplayPropertyId)) {
            entity.setEntityId(UUID.fromString(entityFieldDisplayPropertyId));
        }

        String applicationId = cursor.getString(cursor.getColumnIndex("ApplicationId"));
        if (applicationId != null) {
            entity.setApplicationId(applicationId);
        }

        String entityType = cursor.getString(cursor.getColumnIndex("EntityType"));
        if (entityType != null && !"".equals(entityType)) {
            entity.setEntityType(Integer.parseInt(entityType));
        }

        String displayOrder = cursor.getString(cursor.getColumnIndex("DisplayOrder"));
        if (displayOrder != null && !"".equals(displayOrder)) {
            entity.setDisplayOrder(Integer.parseInt(displayOrder));
        }

        String fieldName = cursor.getString(cursor.getColumnIndex("FieldName"));
        if (fieldName != null) {
            entity.setFieldName(fieldName);
        }
        Boolean system = cursor.getInt(cursor.getColumnIndex("SYSTEM")) == 1;
        if (system != null) {
            entity.setSystem(system);
        }
        String createdDateStr = cursor.getString(cursor.getColumnIndex("CreatedDate"));
        if (createdDateStr != null && !"".equals(createdDateStr)) {
            entity.setCreatedAt(Utils.dateFromString(createdDateStr, true, true));
        }

        String updatedBy = cursor.getString(cursor.getColumnIndex("ModifiedBy"));
        if (updatedBy != null && !"".equals(updatedBy)) {
            entity.setUpdatedBy(UUID.fromString(updatedBy).toString());
        }
        String modifiedDateStr = cursor.getString(cursor.getColumnIndex("CreatedDate"));
        if (modifiedDateStr != null && !"".equals(modifiedDateStr)) {
            entity.setUpdatedAt(Utils.dateFromString(modifiedDateStr, true, true));
        }
        entity.setDirty(false);
    }

    /**
     * Generate sql string with minimum required fields for PFEntityFieldDisplayProperty.
     *
     * @return STRING
     */
    private String getSQL() {
        return " SELECT FP.* FROM PFEntityFieldDisplayProperty As FP Inner Join PFEntityCustomField As EC On FP.FieldName = EC.EntityCustomFieldId ";
    }

}
