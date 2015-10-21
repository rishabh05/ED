//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/20/2015
//===============================================================================
package com.eworkplaceapps.platform.helper.picklistitem;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.SqlUtils;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public class PickListItemData extends BaseData<PickListItem> {

    private String getSQL() {
        String sql = " SELECT * from PFPicklistItem ";
        return sql;
    }

    public BaseEntity createEntity() {
        return PickListItem.createEntity();
    }

    /**
     * GET PICK_LIST_ITEM Entity that matches the id value.
     * Returns PICK_LIST_ITEM an Entity object.
     *
     * @param id
     * @return
     */
    public PickListItem getEntity(Object id) throws EwpException {
        // Building select staement
        String sql = getSQL() + " where LOWER(PicklistItemId) = LOWER('" + (UUID) id + "')";
        // Execute select staement and return entity
        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the PICK_LIST_ITEM Entity record from database.
     * Returns Collection of PICK_LIST_ITEM Entity.
     *
     * @return
     */
    public List<PickListItem> getList() throws EwpException {
        String mySql = getSQL() + " Order By ModifiedDate DESC";
        return executeSqlAndGetEntityList(mySql);
    }

    /**
     * GET the records from the database and return ResultSet for given tenant id.
     *
     * @param tenantId
     * @return
     */

    public Cursor getPicklistListFromTenantAsResultSet(UUID tenantId) throws EwpException {
        String sql = getSQL() + " WHERE  LOWER(TenantId)=LOWER('" + tenantId + "') ";
        return super.executeSqlAndGetResultSet(sql);
    }

    public Cursor getSystemPicklist(String name) throws EwpException {
        String sql = getSQL() + " WHERE  Name='" + name + "' ";
        return super.executeSqlAndGetResultSet(sql);
    }

    /**
     * GET all the picklistitem Entity record from database.
     * Returns Collection of PICK_LIST_ITEM Entity.
     *
     * @param name
     * @return
     */
    public List<PickListItem> getPicklistFromName(String name) throws EwpException {
        String mySql = getSQL() + " WHERE  Name='" + name + "'";
        return executeSqlAndGetEntityList(mySql);
    }

    /**
     * @param parentId
     * @param ownerId
     * @param tenantId
     * @return
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public Cursor getCustomPicklist(UUID parentId, UUID ownerId, UUID tenantId) throws EwpException {
        String sql = getSQL() + " WHERE  LOWER(TenantId)=LOWER('" + tenantId + "') and LOWER(OwnerId)=LOWER('" + ownerId + "') and LOWER(ParentId)=LOWER('" + parentId + "')";
        return executeSqlAndGetResultSet(sql);
    }


    /**
     * @param name
     * @param ownerId
     * @param tenantId
     * @return
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public Cursor getTemplatePicklist(String name, UUID ownerId, UUID tenantId) throws EwpException {
        String sql = getSQL() + " WHERE  LOWER(TenantId)=LOWER('" + tenantId + "') and LOWER(OwnerId)=LOWER('" + ownerId + "') and Name='" + name + "'";
        return executeSqlAndGetResultSet(sql);
    }


    /**
     * DELETE PICK_LIST_ITEM Entity
     *
     * @param entity
     * @return
     */
    public void delete(PickListItem entity) throws EwpException {
        super.deleteRows("PFPicklistItem", "LOWER(PicklistItemId)=?", new String[]{entity.getEntityId().toString()});
    }

    @Override
    public long insertEntity(PickListItem entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("PicklistItemId", entity.getEntityId().toString());
        values.put("Name", entity.getName());
        values.put("TextValue", entity.getTextValue());
        values.put("IntValue", entity.getIntValue());
        values.put("Active", entity.isActive());
        values.put("IsDefault", entity.isDefault());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("OwnerId", entity.getOwnerId().toString());
        values.put("ParentId", entity.getParentId().toString());
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFPicklistItem", values);
    }

    @Override
    public void update(PickListItem entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("Name", entity.getName());
        values.put("TextValue", entity.getTextValue());
        values.put("IntValue", entity.getIntValue());
        values.put("Active", entity.isActive());
        values.put("IsDefault", entity.isDefault());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        entity.setUpdatedAt(new Date());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("PFPicklistItem", values, "LOWER(PicklistItemId)=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Checks for duplicate PICK_LIST_ITEM.
     * Return true if PICK_LIST_ITEM exits with same name and textvalue.
     * This method will be used to check duplicate picklist item in database.
     *
     * @param plItem
     * @return
     */
    public boolean isDuplicatePicklistItem(PickListItem plItem) throws EwpException {
        String sql = "SELECT * From PFPicklistItem where TextValue = '" + plItem.getTextValue() + "' and Name =  '" + plItem.getName() + "' ";
        sql += " and LOWER(PicklistItemId) = '" + (plItem.getEntityId()) + "' and LOWER(OwnerId) = LOWER('" + (plItem.getOwnerId()) + "') ";
        return SqlUtils.recordExists(sql);
    }

    /**
     * Set property value from  database ResultSet.
     */
    @Override
    public void setPropertiesFromResultSet(PickListItem entity, Cursor resultSet) {

        String id = resultSet.getString(resultSet.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String picklistItemId = resultSet.getString(resultSet.getColumnIndex("PickListItemId"));
        if (picklistItemId != null && !"".equals(picklistItemId)) {
            entity.setEntityId(UUID.fromString(picklistItemId));
        }

        String ownerId = resultSet.getString(resultSet.getColumnIndex("OwnerId"));
        if (ownerId != null && !"".equals(ownerId)) {
            entity.setOwnerId(UUID.fromString(ownerId));
        }

        String parentId = resultSet.getString(resultSet.getColumnIndex("ParentId"));
        if (parentId != null && !"".equals(parentId)) {
            entity.setParentId(UUID.fromString(parentId));
        }

        String name = resultSet.getString(resultSet.getColumnIndex("Name"));
        if (name != null) {
            entity.setName(name);
        }

        String textValue = resultSet.getString(resultSet.getColumnIndex("TextValue"));
        if (textValue != null) {
            entity.setTextValue(textValue);
        }

        String intValue = resultSet.getString(resultSet.getColumnIndex("IntValue"));
        if (intValue != null && !"".equals(intValue)) {
            entity.setIntValue(Integer.parseInt(intValue));
        }

//        boolean active = Boolean.parseBoolean(resultSet.getString(resultSet.getColumnIndex("ACTIVE")));
//        entity.setActive(active);

        boolean isDefault = Boolean.parseBoolean(resultSet.getString(resultSet.getColumnIndex("IsDefault")));
        entity.setDefault(isDefault);

        String systemDefined = resultSet.getString(resultSet.getColumnIndex("SystemDefined"));
        if (systemDefined != null && !"".equals(systemDefined)) {
            entity.setSystemDefined(Integer.parseInt(systemDefined));
        }

        String sortNumber = resultSet.getString(resultSet.getColumnIndex("SortNumber"));
        if (sortNumber != null && !"".equals(sortNumber)) {
            entity.setSortNumber(Integer.parseInt(sortNumber));
        }

        String createdAt = resultSet.getString(resultSet.getColumnIndex("CreatedDate"));
        if (createdAt != null && !"".equals(createdAt)) {
            entity.setCreatedAt(Utils.dateFromString(createdAt, true, true));
        }
        String updatedBy = resultSet.getString(resultSet.getColumnIndex("ModifiedBy"));
        if (updatedBy != null) {
            entity.setUpdatedBy(updatedBy);
        }

        String updatedAt = resultSet.getString(resultSet.getColumnIndex("ModifiedDate"));
        if (updatedAt != null && !"".equals(updatedAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updatedAt, true, true));
        }
        entity.setDirty(false);
    }
}
