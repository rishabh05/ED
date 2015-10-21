//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/18/2015
//===============================================================================
package com.eworkplaceapps.platform.address;

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
public class AddressData extends BaseData<Address> {

    /**
     * get address entity
     *
     * @return
     */
    @Override
    public Address createEntity() {
        return Address.createEntity();
    }

    /**
     * Get an entity that matched the id
     *
     * @param id Object
     * @return
     * @throws EwpException
     */
    public Address getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where LOWER(AddressId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Get all the Address Entity from database.
     * Return Collection of Address Entity.
     *
     * @return
     * @throws EwpException
     */
    public List<Address> getList() throws EwpException {
        String sql = getSQL();
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Get Address Entity that matches the id and return result as a FMResultSet.
     *
     * @param id
     * @return
     * @throws EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFAddress where LOWER(AddressId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFAddress";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * delete entity
     *
     * @param entity
     * @return
     * @throws EwpException
     */
    @Override
    public boolean deleteRows(Address entity) throws EwpException {
        return super.deleteRows("PFAddress", "LOWER(EntityId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * Method is used to Add/update/delete communication list.
     *
     * @param sourceId
     * @param sourceEntityType
     * @return
     */
    public boolean deleteAddressFromSourceEntityIdAndType(UUID sourceId, int sourceEntityType) {
        String sql = "DELETE From PFAddress Where LOWER(EntityId) = LOWER('" + sourceId.toString() + "') And EntityType = '" + sourceEntityType + "'";
        return super.executeNonQuerySuccess(sql);
    }


    /**
     * Generate sql string with minimum required fields for Address.
     *
     * @return String
     */
    private String getSQL() {
        String sql = "Select * from PFAddress";
        return sql;
    }


    @Override
    public void update(Address entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("AddressDetail", entity.getAddress1());
        values.put("Latitude", entity.getLatitude());
        values.put("Longitude", entity.getLongitude());
        values.put("IANATimeZoneId", entity.getIanaTimeZone());
        values.put("ApplicationId", entity.getApplicationId());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("TenantId", entity.getTenantId().toString());
        Date d = new Date();
        entity.setUpdatedAt(d);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
        super.update("PFAddress", values, "LOWER(AddressId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    @Override
    public long insertEntity(Address entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("AddressId", entity.getEntityId().toString());
        values.put("EntityId", entity.getSourceEntityId().toString());
        values.put("EntityType", entity.getSourceEntityType());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ApplicationId", entity.getApplicationId());
        values.put("AddressDetail", entity.getAddress1());
        values.put("Latitude", entity.getLatitude());
        values.put("Longitude", entity.getLongitude());
        values.put("IANATimeZoneId", entity.getIanaTimeZone());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("TenantId", entity.getTenantId().toString());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        return super.insert("PFAddress", values);
    }

    /**
     * Get address from entityid and entitytype.
     *
     * @param sourceEntityId
     * @param sourceEntityType
     * @return Address
     */
    public Address getAddressFromSourceEntityIdAndType(UUID sourceEntityId, int sourceEntityType) throws EwpException {
        String sql = getSQL() + " where (EntityId)= ('" + sourceEntityId.toString() + "') and EntityType= '" + sourceEntityType + "'";
        return executeSqlAndGetEntity(sql);
    }

    @Override
    public void setPropertiesFromResultSet(Address entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String addId = cursor.getString(cursor.getColumnIndex("AddressId"));
        if (addId != null && !"".equals(addId)) {
            entity.setEntityId(UUID.fromString(addId));
        }

        String address1 = cursor.getString(cursor.getColumnIndex("AddressDetail"));
        if (address1 != null) {
            entity.setAddress1(address1.replaceAll("''", "'"));
        }
//        String address2 = cursor.getString(cursor.getColumnIndex("Address2"));
//        if (address2 != null) {
//            entity.setAddress2(address2.replaceAll("''", "'"));
//        }
//        String address3 = cursor.getString(cursor.getColumnIndex("Address3"));
//        if (address3 != null) {
//            entity.setAddress3(address3.replaceAll("''", "'"));
//        }
//        String address4 = cursor.getString(cursor.getColumnIndex("Address4"));
//        if (address4 != null) {
//            entity.setAddress4(address4.replaceAll("''", "'"));
//        }

        double lat = cursor.getDouble(cursor.getColumnIndex("Latitude"));
        if (lat == 0)
            entity.setLatitude(-300.0);
            entity.setLatitude(lat);

        double lng = cursor.getDouble(cursor.getColumnIndex("Longitude"));
        if (lng == 0)
            entity.setLongitude(-300.0);
            entity.setLongitude(lng);

        String timeZone = cursor.getString(cursor.getColumnIndex("IANATimeZoneId"));
        if (timeZone != null && !"".equals(timeZone))
            entity.setIanaTimeZone(timeZone);

        String sourceEntityId = cursor.getString(cursor.getColumnIndex("EntityId"));
        if (sourceEntityId != null && !"".equals(sourceEntityId)) {
            entity.setSourceEntityId(UUID.fromString(sourceEntityId));
        }

        String entityType = cursor.getString(cursor.getColumnIndex("EntityType"));
        if (entityType != null && !"".equals(entityType)) {
            entity.setSourceEntityType(Integer.parseInt(entityType));
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
        if (updatedBy != null) {
            entity.setUpdatedBy(updatedBy);
        }

        String updatedAt = cursor.getString(cursor.getColumnIndex("ModifiedDate"));
        if (updatedAt != null && !"".equals(updatedAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updatedAt, true, true));
        }
        entity.setDirty(false);
    }
}
