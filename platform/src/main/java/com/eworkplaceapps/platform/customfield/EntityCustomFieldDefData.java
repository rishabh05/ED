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

public class EntityCustomFieldDefData extends BaseData<EntityCustomFieldDef> {

    @Override
    public EntityCustomFieldDef createEntity() {
        return new EntityCustomFieldDef();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object
     * @return EntityCustomFieldDef
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public EntityCustomFieldDef getEntity(Object id) throws EwpException {
        String sql = "SELECT * From PFEntityFieldDetails where EntityFieldDetailsId= '" + ((UUID) id).toString() + "'";

        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the ENTITY_CUSTOM_FIELD Entity from database.
     * Return Collection of ENTITY_CUSTOM_FIELD Entity.
     *
     * @return List<EntityCustomFieldDef>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public List<EntityCustomFieldDef> getList() throws EwpException {
        String sql = "SELECT * From PFEntityFieldDetails";

        return executeSqlAndGetEntityList(sql);
    }

    /**
     * GET ENTITY_CUSTOM_FIELD Entity that matches the id and return result as a ResultSet.
     *
     * @param id Object
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFEntityFieldDetails where EntityFieldDetailsId= '" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all ENTITY_CUSTOM_FIELD Entity record from database and return result as a ResultSet.
     *
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFEntityFieldDetails";
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
        super.deleteRows("PFEntityFieldDetails", "EntityFieldDetailsId=?", new String[]{id.toString()});
    }

    /**
     * Method is used to get custom field list as ResultSet from entityId
     *
     * @param entityType int
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    public Cursor getEntityCustomFieldListByEntityTypeAsResultSet(int entityType) throws EwpException {
        String sql = getSQL();
        sql += "EntityType = '" + entityType + "' Order By DisplayOrder";

        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Method is used to get custom field list as ResultSet from entityId
     *
     * @param entityType int
     * @param tenantId   UUID
     * @return List<EntityCustomFieldDef>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */

    public List<EntityCustomFieldDef> getEntityCustomFieldListByEntityTypeAsEntityList(int entityType, UUID tenantId) throws EwpException {
        String sql = getSQL();
        sql += "EntityType = '" + entityType + "' and TenantId = '" + tenantId.toString() + "' Order By DisplayOrder";

        return super.executeSqlAndGetEntityList(sql);
    }

    /**
     * @param userId     UUID
     * @param entityType int
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getNonViewableEntityCustomFieldListByUserId(UUID userId, int entityType) throws EwpException {
        String sql = "select cf.*, fp.* from PFEntityFieldDetails As cf Inner Join PFEntityFieldPermission As fp  on ";
        sql += "cf.EntityFieldDetailsId = fp.EntityFieldDetailsId Inner Join PFRolePermission As rp ";
        sql += "On rp.RolePermissionId = fp.RolePermissionId Inner Join PFRoleLinking As rl on ";
        sql += "rp.RoleId = rl.RoleId where rl.UserId = '" + userId.toString() + "' ";
        sql += "and cf.EntityType = '" + entityType + "' ";

        return executeSqlAndGetResultSet(sql);
    }

    /**
     * @param userId     UUID
     * @param tenantId   UUID
     * @param entityType int
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getViewableEntityCustomFieldAndPermissionListByUserId(UUID userId, UUID tenantId, int entityType) throws EwpException {
        String sql = "select cf.*, fp.* from PFEntityFieldDetails As cf Inner Join PFEntityFieldPermission As fp  on ";
        sql += "cf.EntityFieldDetailsId = fp.EntityFieldDetailsId Inner Join PFRolePermission As rp ";
        sql += "On rp.RolePermissionId = fp.RolePermissionId Inner Join PFRoleLinking As rl on ";
        sql += "rp.RoleId = rl.RoleId where rl.UserId = '" + userId.toString() + "' and cf.TenantId ='" + tenantId.toString() + "' ";
        sql += "and cf.EntityType = '" + entityType + "' and fp.ViewField = '1' ORDER BY cf.GroupId, cf.DisplayOrder";

        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public long insertEntity(EntityCustomFieldDef entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("EntityFieldDetailsId", entity.getEntityId().toString());
        values.put("CustomLabel", entity.getCustomLabel());
        values.put("FieldClass", entity.getFieldClass());
        values.put("Include", entity.getInclude());
        values.put("Required", entity.getRequired());
        values.put("DefaultValue", entity.getDefaultValue());
        values.put("EntityType", entity.getEntityType());
        values.put("EntityFieldId", entity.getFieldCode());
        values.put("FieldDataType", entity.getFieldDataType());
        values.put("Watermark", entity.getWatermark());
        values.put("HintText", entity.getHintText());
        values.put("DisplayOrder", entity.getDisplayOrder());
        values.put("ParentEntityId", entity.getParentEntityId().toString());
        values.put("ApplicationId", entity.getApplicationId());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFEntityFieldDetails", values);
    }

    @Override
    public void update(EntityCustomFieldDef entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("CustomLabel", entity.getCustomLabel());
        values.put("Watermark", entity.getWatermark());
        values.put("HintText", entity.getHintText());
        values.put("DisplayOrder", entity.getDisplayOrder());
        values.put("Include", entity.getInclude());
        values.put("Required", entity.getRequired());
        values.put("DefaultValue", entity.getDefaultValue());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("PFEntityFieldDetails", values, "EntityFieldDetailsId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Generate sql string with minimum required fields for ENTITY_CUSTOM_FIELD.
     *
     * @return STRING
     */
    private String getSQL() {
        return " SELECT * FROM PFEntityFieldDetails ";
    }

    /**
     * Set property value from  database ResultSet.
     *
     * @param cursor {@see #android.database.Cursor} database cursor for database.
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public void setPropertiesFromResultSet(EntityCustomFieldDef entity, Cursor cursor) throws EwpException {

        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String entityCustomFieldId = cursor.getString(cursor.getColumnIndex("EntityFieldDetailsId"));
        if (entityCustomFieldId != null && !"".equals(entityCustomFieldId)) {
            entity.setEntityId(UUID.fromString(entityCustomFieldId));
        }

        String applicationId = cursor.getString(cursor.getColumnIndex("ApplicationId"));
        if (applicationId != null) {
            entity.setApplicationId(applicationId);
        }

        String customLbl = cursor.getString(cursor.getColumnIndex("CustomLabel"));
        if (customLbl != null) {
            entity.setCustomLabel(customLbl);
        }

        String order = cursor.getString(cursor.getColumnIndex("DisplayOrder"));
        if (order != null && !"".equals(order)) {
            entity.setDisplayOrder(Integer.parseInt(order));
        }

        String parentEntityId = cursor.getString(cursor.getColumnIndex("ParentEntityId"));
        if (parentEntityId != null && !"".equals(parentEntityId)) {
            entity.setParentEntityId(UUID.fromString(parentEntityId));
        }

        String watermark = cursor.getString(cursor.getColumnIndex("Watermark"));
        if (watermark != null) {
            entity.setWatermark(watermark);
        }

        String hintText = cursor.getString(cursor.getColumnIndex("HintText"));
        if (hintText != null) {
            entity.setHintText(hintText);
        }
        String groupName = cursor.getString(cursor.getColumnIndex("GroupId"));
        if (groupName != null && !"".equals(groupName)) {
            entity.setGroupId(Integer.parseInt(groupName));
        }
        String fieldCode = cursor.getString(cursor.getColumnIndex("EntityFieldId"));
        if (fieldCode != null && !"".equals(fieldCode)) {
            entity.setFieldCode(Integer.parseInt(fieldCode));
        }
        String fieldDataType = cursor.getString(cursor.getColumnIndex("FieldDataType"));
        if (fieldDataType != null && !"".equals(fieldDataType)) {
            entity.setFieldDataType(Integer.parseInt(fieldDataType));
        }
        String fieldType = cursor.getString(cursor.getColumnIndex("FieldType"));
        if (fieldType != null && !"".equals(fieldType)) {
            entity.setFieldType(Integer.parseInt(fieldType));
        }
        String fieldClass = cursor.getString(cursor.getColumnIndex("FieldClass"));
        if (fieldClass != null) {
            entity.setFieldClass(Integer.parseInt(fieldClass));
        }
        Boolean include = cursor.getInt(cursor.getColumnIndex("Include")) == 1;
        if (include != null) {
            entity.setInclude(include);
        }

        Boolean required = cursor.getInt(cursor.getColumnIndex("REQUIRED")) == 1;
        if (required != null) {
            entity.setRequired(required);
        }

        String entityType = cursor.getString(cursor.getColumnIndex("EntityType"));
        if (entityType != null && !"".equals(entityType)) {
            entity.setEntityType(Integer.parseInt(entityType));
        }

        String defaultValue = cursor.getString(cursor.getColumnIndex("DefaultValue"));
        if (defaultValue != null) {
            entity.setDefaultValue(defaultValue);
        }

        String createdBy = cursor.getString(cursor.getColumnIndex("CreatedBy"));
        if (createdBy != null && !"".equals(createdBy)) {
            entity.setCreatedBy(UUID.fromString(createdBy).toString());//check
        }
        String strDate = cursor.getString(cursor.getColumnIndex("CreatedDate"));
        if (strDate != null && !"".equals(strDate)) {
            entity.setCreatedAt(Utils.dateFromString(strDate, true, true));
        }

        String updatedBy = cursor.getString(cursor.getColumnIndex("ModifiedBy"));
        if (updatedBy != null && !"".equals(updatedBy)) {
            entity.setUpdatedBy(UUID.fromString(updatedBy).toString());//check
        }
        String strModifiedDate = cursor.getString(cursor.getColumnIndex("ModifiedDate"));
        if (strModifiedDate != null && !"".equals(strModifiedDate)) {
            entity.setUpdatedAt(Utils.dateFromString(strModifiedDate, true, true));
        }
        entity.setDirty(false);
    }
}
