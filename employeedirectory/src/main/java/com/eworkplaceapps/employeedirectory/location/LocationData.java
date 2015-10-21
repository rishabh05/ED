//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.location;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.SqlUtils;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.SortingOrder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class LocationData extends BaseData<Location> {
    @Override
    public Location createEntity() {
        return Location.createEntity();
    }

    /**
     * Get an entity that matched the id
     *
     * @param id
     * @return Location
     * @throws EwpException
     */
    @Override
    public Location getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where (loc.LocationId)= ('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Get all the Location Entity from database.
     * Return Collection of Location Entity.
     *
     * @return List<Location>
     * @throws EwpException
     */
    @Override
    public List<Location> getList() throws EwpException {
        String sql = getSQL() + " ORDER BY LOWER(loc.Name) ";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Get Location Entity that matches the id and return result as a ResultSet.
     *
     * @param id
     * @return Cursor
     * @throws EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From Location where LOWER(LocationId)= LOWER('" + ((UUID) id).toString() + "') ";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Get all Location Entity record from database and return result as a ResultSet.
     *
     * @return Cursor
     * @throws EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From EDLocation";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Delete Location entity.
     *
     * @param entity
     * @throws EwpException
     */
    @Override
    public void delete(Location entity) throws EwpException {
        super.deleteRows("EDLocation", "LOWER(LocationId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    @Override
    public long insertEntity(Location entity) {
        ContentValues values = new ContentValues();
        entity.setTenantId(EwpSession.getSharedInstance().getTenantId());
        entity.setEntityId(UUID.randomUUID());
        values.put("LocationId", entity.getEntityId().toString());
        values.put("Name", entity.getName());
     //   values.put("Address", entity.getAddress());
//        values.put("Latitude", entity.getLatitude());
//        values.put("Longitude", entity.getLongitude());
//        values.put("IANATimeZoneId", entity.getIanaTimeZone());
        values.put("Manager", entity.getLocationCoordinator());
        values.put("Picture", entity.getPicture());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        long id = super.insert("EDLocation", values);
        return id;
    }

    @Override
    public void update(Location entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("Name", entity.getName());
    //    values.put("Address", entity.getAddress());
        values.put("Manager", entity.getLocationCoordinator());
        values.put("Picture", entity.getPicture());
        values.put("ModifiedBy", entity.getUpdatedBy());
//        values.put("Latitude", entity.getLatitude());
//        values.put("Longitude", entity.getLongitude());
//        values.put("IANATimeZoneId", entity.getIanaTimeZone());
        Date d = new Date();
        entity.setUpdatedAt(d);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
        values.put("TenantId", entity.getTenantId().toString());
        super.update("EDLocation", values, "LOWER(LocationId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * Method is used to get LOCATION list as ResultSet from tenantId
     *
     * @param tenantId
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getLocationListFromTenantIdAsResultSet(UUID tenantId) throws EwpException {
        String sql = getSQL() + " Where ";
        sql += " LOWER(loc.TenantId) =LOWER('" + tenantId.toString() + "')  ";
        sql += SqlUtils.buildSortClause(sql, "Name", SortingOrder.ASC);
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Method is used to get LOCATION list as ResultSet from tenantId
     *
     * @param tenantId
     * @return List<Location>
     * @throws EwpException
     */
    public List<Location> getLocationListFromTenantId(UUID tenantId) throws EwpException {
        String sql = getSQL() + " Where ";
        sql += " LOWER(loc.TenantId)=LOWER('" + tenantId.toString() + "')  ";
        sql += SqlUtils.buildSortClause(sql, "Name", SortingOrder.ASC);
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Generate sql string with minimum required fields for Location.
     *
     * @return String sql
     */
    private String getSQL() {
        // Generating the Location SQL Statement to get the Location detail. It will give the Location.
        String sql = "Select loc.*, emp.FullName as CoordinatorName,add1.AddressDetail, add1.AddressId from EDLocation As loc Inner Join EDEmployee as emp ON (loc.Manager) = (emp.EmployeeId) Left Join PFAddress as add1 ON (loc.Manager) = (add1.EntityId) ";
        return sql;
    }

    public boolean locationExists(UUID locId, String locationName, UUID tenantId) throws EwpException {
        String sql = "SELECT * From EDLocation ";
        return SqlUtils.recordExists(sql + "Where LOWER(Name) = LOWER('" + locationName + "') and LOWER(TenantId) = LOWER('" + tenantId.toString() + "') and LOWER(LocationId) <> LOWER('" + locId.toString() + "') ");
    }

    @Override
    public void setPropertiesFromResultSet(Location entity, Cursor cursor) throws EwpException {

        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String locationId = cursor.getString(cursor.getColumnIndex("LocationId"));
        if (locationId != null && !"".equals(locationId)) {
            entity.setEntityId(UUID.fromString(locationId));
        }

        String name = cursor.getString(cursor.getColumnIndex("Name"));
        if (name != null && !"".equals(name)) {
            entity.setName(name.replaceAll("''", "'"));
        }

        String managerId = cursor.getString(cursor.getColumnIndex("Manager"));
        if (managerId != null && !"".equals(managerId)) {
            entity.setLocationCoordinator(managerId);
        }

//        String address = cursor.getString(cursor.getColumnIndex("Address"));
//        if (address != null && !"".equals(address)) {
//            entity.setAddress(address.replaceAll("''", "'"));
//        }

        String managerName = cursor.getString(cursor.getColumnIndex("CoordinatorName"));
        if (managerName != null && !"".equals(managerName)) {
            entity.setLocationCoordinatorName(managerName);
        }

//        double lat = cursor.getDouble(cursor.getColumnIndex("Latitude"));
//        entity.setLatitude(lat);
//
//        double lng = cursor.getDouble(cursor.getColumnIndex("Longitude"));
//        entity.setLongitude(lng);
//
//        String timeZone = cursor.getString(cursor.getColumnIndex("IANATimeZoneId"));
//        if (timeZone != null && !"".equals(timeZone))
//            entity.setIanaTimeZone(timeZone);

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
        String logo = cursor.getString(cursor.getColumnIndex("Picture"));
        if ("null".equalsIgnoreCase(logo)) {
            logo = "";
        }
        entity.setPicture(logo);
        entity.setDirty(false);
    }
}
