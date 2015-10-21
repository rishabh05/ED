//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/19/2015
//===============================================================================
package com.eworkplaceapps.platform.userentitylink;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.SqlUtils;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.LinkType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class PFUserEntityLinkData extends BaseData<PFUserEntityLink> {


    @Override
    public PFUserEntityLink createEntity() {
        return PFUserEntityLink.createEntity();
    }

    /**
     * Get an entity that matched the id
     *
     * @param id Object
     * @return
     * @throws EwpException
     */
    @Override
    public PFUserEntityLink getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where LOWER(UserEntityLinkId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Get all the PFUserEntityLink Entity from database.
     * Return Collection of PFUserEntityLink Entity.
     *
     * @return
     * @throws EwpException
     */
    @Override
    public List<PFUserEntityLink> getList() throws EwpException {
        return executeSqlAndGetEntityList(getSQL());
    }

    /**
     * Get PFUserEntityLink Entity that matches the id and return result as a ResultSet.
     *
     * @param id
     * @return
     * @throws EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFUserEntityLink where LOWER(UserEntityLinkId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Delete PFUserEntityLink entity.
     *
     * @param entity
     * @return
     * @throws EwpException
     */
    @Override
    public boolean deleteRows(PFUserEntityLink entity) throws EwpException {
        return super.deleteRows("PFUserEntityLink", "LOWER(UserEntityLinkId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * Get all PFUserEntityLink Entity record from database and return result as a ResultSet.
     *
     * @return
     * @throws EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From EDPFUserEntityLink";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Method is used to delete UserEntityLink from source type, source id, linktype and tenantid.
     * :param: tenantId: Id of logged in tenant.
     * :param: LinkType: Type of link. (How a userentity link from a entity) for example employee may save more then one link type like "Following", "Favorite".
     * :param: sourceEntityType: It is source type for which entity, we have created the UserEntityLink.
     * :param: sourceEntityId: It is source id for which entity, we have created the UserEntityLink.
     *
     * @param tenantId
     * @param linkType
     * @param sourceEntityType
     * @param entityId
     * @param createdById
     */
    public void deleteUserEntityFromSourceIdSourceTypeAndLinkType(UUID tenantId, int linkType, int sourceEntityType, UUID entityId, UUID createdById) throws EwpException {
        deleteRows("PFUserEntityLink", "LOWER(EntityId)=? and EntityType=? and LinkType=? and LOWER(TenantId)=? and LOWER(CreatedBy)=?", new String[]{entityId.toString().toLowerCase(), String.valueOf(sourceEntityType), String.valueOf(linkType), tenantId.toString().toLowerCase(), createdById.toString().toLowerCase()});
    }

    @Override
    public void delete(PFUserEntityLink entity) throws EwpException {
        super.deleteRows("PFUserEntityLink", "LOWER(UserEntityLinkId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    @Override
    public long insertEntity(PFUserEntityLink entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("UserEntityLinkId", entity.getEntityId().toString());
        values.put("EntityId", entity.getSourceEntityId().toString());
        values.put("EntityType", entity.getSourceEntityType());
        values.put("LinkType", entity.getLinkType());
        values.put("BoolValue", entity.isBoolValue());
        values.put("IntValue", entity.getIntValue());
        values.put("StringValue", entity.getStringValue());
        values.put("SortOrder", entity.getSortOrder());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("ApplicationId", entity.getApplicationId());
        values.put("TenantId", entity.getTenantId().toString());
        long id = super.insert("PFUserEntityLink", values);
        return id;
    }

    @Override
    public void update(PFUserEntityLink entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("EntityId", entity.getSourceEntityId().toString());
        values.put("EntityType", entity.getSourceEntityType());
        values.put("LinkType", entity.getLinkType());
        values.put("BoolValue", entity.isBoolValue());
        values.put("IntValue", entity.getIntValue());
        values.put("SortOrder", entity.getSortOrder());
        values.put("StringValue", entity.getStringValue());
        values.put("ModifiedBy", entity.getUpdatedBy());
        Date d = new Date();
        entity.setUpdatedAt(d);
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
        super.update("PFUserEntityLink", values, "LOWER(UserEntityLinkId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /// Method is used to get max sortorder from source type, linktype and tenantid.
    /// :param: tenantId: Id of logged in tenant.
    /// :param: LinkType: Type of link. (How a userentity link from a entity) for example employee may save more then one link type like "Following", "Favorite".
    /// :param: sourceEntityType: It is source type for which entity, we have created the UserEntityLink.
    public int getMaxSortOrderFromSourceTypeAndLinkType(UUID tenantId, int linkType, int sourceEntityType, UUID createdById) throws EwpException {
        String sql = "select Count(SortOrder) AS OrderIndex from PFUserEntityLink ";
        // OrderIndex
        sql += " Where EntityType='" + sourceEntityType + "' And LinkType ='" + linkType + "' And LOWER(TenantId)=LOWER('" + tenantId.toString() + "')  and LOWER(CreatedBy) = LOWER('" + createdById.toString() + "') ";
        Cursor cursor = executeSqlAndGetResultSet(sql);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("OrderIndex"));
        }
        return 0;
    }

    /**
     * Method is used to get user list as ResultSet from tenantId
     *
     * @param tenantId
     * @return
     */
    public Cursor getPFUserEntityLinkListTenantIdAsResultSet(UUID tenantId) throws EwpException {
        String sql = getSQL() + " Where ";
        sql += " LOWER(TenantId)=LOWER('" + tenantId.toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Generate sql string with minimum required fields for PFUserEntityLink.
     *
     * @return
     */
    private String getSQL() {
        /// Generating the PFUserEntityLink SQL Statement to get the PFUserEntityLink.
        return "Select * from PFUserEntityLink ";
    }

    /**
     * Method is used to get UserEntityLink from source type, source id, linktype and tenantid.
     * :param: tenantId: Id of logged in tenant.
     * :param: LinkType: Type of link. (How a userentity link from a entity) for example employee may save more then one link type like "Following", "Favorite".
     * :param: sourceEntityType: It is source type for which entity, we have created the UserEntityLink.
     * :param: sourceEntityId: It is source id for which entity, we have created the UserEntityLink.
     *
     * @param tenantId
     * @param linkType
     * @param sourceEntityType
     * @param entityId
     * @param createdById
     * @return
     */
    public PFUserEntityLink getUserEntityFromSourceIdSourceTypeAndLinkType(UUID tenantId, int linkType, int sourceEntityType, UUID entityId, UUID createdById) throws EwpException {
        String sql = getSQL();
        sql += " Where LOWER(EntityId)=LOWER('" + entityId.toString() + "') And EntityType='" + sourceEntityType + "' And LinkType='" + linkType + "' And LOWER(TenantId)=LOWER('" + tenantId.toString() + "')  and LOWER(CreatedBy) = LOWER('" + createdById.toString() + "')";
        return executeSqlAndGetEntity(sql);
    }

    public boolean isEmployeeFollowing(UUID sourceEntityId) throws EwpException {
        String sql = getSQL();
        String loggedInUserId = EwpSession.getSharedInstance().getUserId().toString();
        sql += "where LOWER(EntityId)=LOWER('" + sourceEntityId.toString() + "') And LinkType='" + LinkType.FOLLOW_UP.getId() + "' And LOWER( CreatedBy)=LOWER('" + loggedInUserId + "')";
        return SqlUtils.recordExists(sql);
    }

    public boolean isUserEntityLinkExist(UUID sourceEntityId, int linkType) throws EwpException {
        String sql = getSQL();
        String loggedInUserId = EwpSession.getSharedInstance().getUserId().toString();
        sql += "where LOWER(EntityId)=LOWER('" + sourceEntityId.toString() + "') And LinkType='" + linkType + "' And LOWER(CreatedBy)=LOWER('" + loggedInUserId + "')";
        return SqlUtils.recordExists(sql);
    }

    /// Method is used to get UserEntityLink from source type, linktype and tenantid.
    /// :param: tenantId: Id of logged in tenant.
    /// :param: LinkType: Type of link. (How a userentity link from a entity) for example employee may save more then one link type like "Following", "Favorite".
    /// :param: sourceEntityType: It is source type for which entity, we have created the UserEntityLink.
    public List<PFUserEntityLink> getUserEntityListFromSourceTypeAndLinkType(UUID tenantId, int linkType, int sourceEntityType, UUID createdById) throws EwpException {
        String sql = getSQL();
        // OrderIndex
        sql += " Where EntityType='" + sourceEntityType + "' And LinkType='" + linkType + "' And LOWER(TenantId)=LOWER('" + tenantId.toString() + "')  and LOWER(CreatedBy) = LOWER('" + createdById.toString() + "') ORDER By SortOrder";
        return executeSqlAndGetEntityList(sql);
    }

    @Override
    public void setPropertiesFromResultSet(PFUserEntityLink entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String userEntityLinkId = cursor.getString(cursor.getColumnIndex("UserEntityLinkId"));
        if (userEntityLinkId != null && !"".equals(userEntityLinkId)) {
            entity.setEntityId(UUID.fromString(userEntityLinkId));
        }

        String name = cursor.getString(cursor.getColumnIndex("StringValue"));
        if (name != null && !"".equals(name)) {
            entity.setStringValue(name);
        }

        String intVal = cursor.getString(cursor.getColumnIndex("IntValue"));
        if (intVal != null && !"".equals(intVal)) {
            entity.setIntValue(Integer.parseInt(intVal));
        }

        boolean boolVal = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("BoolValue")));
        entity.setBoolValue(boolVal);

        String sourceEntityId = cursor.getString(cursor.getColumnIndex("EntityId"));
        if (sourceEntityId != null && !"".equals(sourceEntityId)) {
            entity.setSourceEntityId(UUID.fromString(sourceEntityId));
        }

        int sortOrder = cursor.getInt(cursor.getColumnIndex("SortOrder"));
        entity.setSortOrder(sortOrder);

        int sourceEntityType = cursor.getInt(cursor.getColumnIndex("EntityType"));
        entity.setSourceEntityType(sourceEntityType);

        int linkType = cursor.getInt(cursor.getColumnIndex("LinkType"));
        entity.setLinkType(linkType);

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


//    public void rearrangeSortOrder(int linkType, int sourceEntityType, UUID sourceEntityId, UUID createdBy) throws EwpException {
//        String sql = " Select UserEntityLinkId, EntityId,SortOrder from PFUserEntityLink ";
//        // OrderIndex
//        if (createdBy != null) {
//            sql += " Where EntityType='" + sourceEntityType + "' And LinkType='" + linkType + "' and EntityId = '" + sourceEntityId.toString() + "' and CreatedBy='" + createdBy.toString() + "' ORDER By SortOrder";
//        } else {
//            sql += " Where EntityType='" + sourceEntityType + "' And LinkType='" + linkType + "' and EntityId = '"+sourceEntityId.toString()+"' ORDER By SortOrder, CreatedBy COLLATE NOCASE";
//        }
//        Cursor cursor = executeSqlAndGetResultSet(sql);
//        String lastUserId = null;
//        int index = 1;
//        String updateBy = EwpSession.getSharedInstance().getUserId().toString();
//        if (cursor != null && cursor.moveToFirst()) {
//            while (cursor.moveToNext()) {
//                String sourceEntity_Id = cursor.getString(1);
//                int sortOrder = cursor.getInt(2);
//                if (sourceEntityId != null) {
//                    lastUserId = sourceEntity_Id;
//                }
//                if (lastUserId.toLowerCase().equals(sourceEntity_Id.toLowerCase())) {
//                    index = 1;
//                }
//                if (sortOrder == index) {
//                    index = +1;
//                    continue;
//                }
//                String userEntityLinkId = cursor.getString(0);
////                String updateSql = "UPDATE PFUserEntityLink SET ";
//                ContentValues values = new ContentValues();
//                values.put("SortOrder", String.valueOf(index));
//                values.put("ModifiedBy", updateBy);
//                Date d = new Date();
//                values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
////                updateSql += " WHERE UserEntityLinkId ='" + userEntityLinkId + "' ";
//                super.update("PFUserEntityLink", values, "UserEntityLinkId=?", new String[]{userEntityLinkId});
//
//            }
//
//
//        }
//
//    }

    /**
     * @param sql
     * @throws EwpException
     */
        public void rearrangeSortOrder(String sql) throws EwpException {
        Cursor cursor = executeSqlAndGetResultSet(sql);
        String lastUserId = "";
        int index = 1;
        String updateBy = EwpSession.getSharedInstance().getUserId().toString();
        if (cursor != null ) {
            while (cursor.moveToNext()) {
                String sourceEntity_Id = cursor.getString(1);
                int sortOrder = cursor.getInt(2);

                if (!lastUserId.toLowerCase().equals(sourceEntity_Id.toLowerCase())) {
                    index = 1;
                }
                lastUserId = sourceEntity_Id;
                if (sortOrder == index) {
                    index += 1;
                    continue;
                }
                String userEntityLinkId = cursor.getString(0);
                ContentValues values = new ContentValues();
                values.put("SortOrder", String.valueOf(index));
                values.put("ModifiedBy", updateBy);
                Date d = new Date();
                values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
                super.update("PFUserEntityLink", values, "UserEntityLinkId=?", new String[]{userEntityLinkId});
                index += 1;
            }


        }

    }

    /**
     * Method is used to sort item from source type, linktype and sourceEntityId and created by.
     * @param linkType Type of link. (How a userentity link from a entity) for example employee may save more then one link type like "Following", "Favorite".
     * @param createdBy Id of user.s Who created the UserEntityLink.
     * @throws EwpException
     */
    public void rearrangeUsersSortOrder(int linkType,ArrayList<String> createdBy)throws EwpException  {
        String sql = " Select UserEntityLinkId, user.CreatedBy,user.SortOrder from PFUserEntityLink as user ";
        sql += " Inner join PFCommunication as comm ON (user.EntityId)  = (comm.CommunicationId) ";

        String list = "" ;
        for (int i = createdBy.size() - 1; i >= 0; i--) {
            if ("".equalsIgnoreCase(createdBy.get(i)) ){
                continue;
            }
            if (list == "")  {
                list = "'" + createdBy.get(i) + "'";
            }
            else {
                list += ",'" + createdBy.get(i) + "'";
            }
        }
        sql += " Where user.LinkType='" + linkType + "' And user.CreatedBy in ('" + linkType + "') ORDER By user.CreatedBy, SortOrder";
        rearrangeSortOrder(sql);
    }

    /**
     * Method is used to get user list who has made a item favorite.
     * @param linkType Type of link. (How a userentity link from a entity) for example employee may save more then one link type like "Following", "Favorite".
     * @param sourceEntityId It is id of source entity type for which entity, we have created the UserEntityLink.
     * @return
     * @throws EwpException
     */
    public ArrayList<String> getFavoriteUserList(int linkType, UUID sourceEntityId) throws EwpException {
        String sql = " Select user.CreatedBy from PFUserEntityLink where user.LinkType='" + linkType + "' and user.EntityId = '" + sourceEntityId.toString() + "' ";
        Cursor cursor = super.executeSqlAndGetResultSet(sql);
        /// if entity found then return entity
        ArrayList<String> list = new ArrayList<String> ();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                /// Set the value from FMResultSet to entity
                String value = cursor.getString(0);
                list.add(value);
            }
        }
        return list;
    }
    /**
     * Method is used to sort item from source type, linktype and sourceEntityId and created by.
     *
     * @param linkType         Type of link. (How a userentity link from a entity) for example employee may save more then one link type like "Following", "Favorite".
     * @param sourceEntityType It is source type for which entity, we have created the UserEntityLink.
     * @param sourceEntityId   It is source id for which entity, we have created the UserEntityLink.
     * @param createdBy        Id of logged in user.Who created the UserEntityLink.
     * @throws EwpException
     */

    public void rearrangeSortOrder(int linkType, int sourceEntityType, UUID sourceEntityId, UUID createdBy) throws EwpException {
        String sql = " Select UserEntityLinkId, user.CreatedBy, SortOrder from PFUserEntityLink as user ";
        sql += " Inner join PFCommunication as comm ON (user.EntityId)  = (comm.CommunicationId) ";

        // OrderIndex
        if (createdBy != null &&  sourceEntityId != null ){
            sql += " Where user.EntityType='" + sourceEntityType + "' And user.LinkType='" + linkType + "' and user.EntityId = '" + sourceEntityId.toString() + "' and user.CreatedBy='" + createdBy.toString() + "' ORDER By SortOrder";
        }
        // This is condiion when communication is deleted and need to sort all items with all user.
        else if (sourceEntityId == null && createdBy == null ){
            sql += " Where user.LinkType='" + linkType + "'  ORDER By user.CreatedBy,SortOrder";
        }
        else if (sourceEntityId == null && createdBy != null ){
            sql += " Where user.LinkType='" + linkType + "' and user.CreatedBy='" + createdBy.toString() + "'  ORDER By SortOrder";
        }
        else if ( createdBy != null) {
            sql += " Where user.EntityType='" + sourceEntityType + "' And user.LinkType='" + linkType + "' and user.EntityId = '" + sourceEntityId.toString() + "' and user.CreatedBy='" + createdBy.toString() + "' ORDER By SortOrder";
        } else {
            sql += " Where user.EntityType='" + sourceEntityType + "' And user.LinkType='" + linkType + "' and user.EntityId = '"+sourceEntityId.toString()+"' ORDER By user.CreatedBy, SortOrder ";

        }

         rearrangeSortOrder(sql);
    }

    public boolean deleteUserEntityFromSourceEntityId(UUID sourceEntityId) throws EwpException {
        String sql = "DELETE From PFUserEntityLink Where";
        sql += " user.EntityId='"+sourceEntityId.toString()+"'";
        return super.executeNonQuerySuccess(sql);
    }
}
