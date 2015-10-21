//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Sourabh Agrawal
// Original DATE: 19/08/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.List;
import java.util.UUID;


public class EmployeeStatusConfigData extends BaseData<EmployeeStatusConfig> {


    @Override
    public EmployeeStatusConfig createEntity() {
        return new EmployeeStatusConfig();
    }

    @Override
    public void delete(EmployeeStatusConfig entity) throws EwpException {
        String sql = "DELETE From EDStatusConfig Where LOWER(StatusConfigId) =  LOWER('" + entity.getEntityId().toString().toLowerCase() + "') ";
        super.executeNonQuerySuccess(sql);
    }

    public String deleteStatement(UUID id)  {
        String sql = "DELETE from EDStatusConfig LOWER(StatusConfigId) = LOWER('" + id.toString() + "') ";
        return sql;
    }

    @Override
    public long insertEntity(EmployeeStatusConfig entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        entity.setTenantId(EwpSession.getSharedInstance().getTenantId());
        values.put("StatusConfigId", entity.getEntityId().toString());
        values.put("Name", entity.getName().toString());
        values.put("RGBColor", entity.getColor().toString());
        values.put("PredefinedCode", entity.getPredefinedCode().toString());
        values.put("SortOrder", entity.getSortOrder());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("EDStatusConfig", values);
    }

    @Override
    public List<EmployeeStatusConfig> getList() throws EwpException {
        String sql  = getSQL() + " ORDER By SortOrder";
        return executeSqlAndGetEntityList(sql);
    }

    public List<EmployeeStatusConfig> getStatusList() throws EwpException  {
        String sql  = getSQL() + " ORDER By SortOrder";
        return executeSqlAndGetEntityList(sql);
    }

    // ------------------------ Begin Internal/Private Members ----------------

    /// Generate sql string with minimum required fields for StatusConfig.
    private String getSQL() {

        // Generating the StatusConfig SQL Statement to get the StatusConfig detail. It will give the StatusConfig details.
        String sql = "Select * from EDStatusConfig";

        return sql;
    }

    @Override
    // Set property value from  database FMResultSet.
    public void setPropertiesFromResultSet(EmployeeStatusConfig entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }
        String statusConfigId = cursor.getString(cursor.getColumnIndex("StatusConfigId"));
        if (statusConfigId != null && !"".equals(statusConfigId)) {
            entity.setEntityId(UUID.fromString(statusConfigId));
        }
        String name = cursor.getString(cursor.getColumnIndex("Name"));
        entity.setName(name);

        String color = cursor.getString(cursor.getColumnIndex("RGBColor"));
        entity.setColor(color);
        String textValue = cursor.getString(cursor.getColumnIndex("PredefineCode"));
        entity.setPredefinedCode(textValue);

        int sortOder = cursor.getInt(cursor.getColumnIndex("SortOrder"));
        entity.setSortOrder(sortOder);

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
//        if (entity.isSystemDefined() && entity.g){
//            if let status =  self.handle.toInt() {
//                systemHandleStatus = status
//            }
        entity.setDirty(false);
    }



}
