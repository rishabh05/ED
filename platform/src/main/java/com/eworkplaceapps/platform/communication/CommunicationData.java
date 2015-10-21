//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/18/2015
//===============================================================================
package com.eworkplaceapps.platform.communication;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.SqlUtils;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.CommunicationType;
import com.eworkplaceapps.platform.utils.enums.LinkType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class CommunicationData extends BaseData<Communication> {

    @Override
    public Communication createEntity() {
        return Communication.createEntity();
    }


    /**
     * Get an entity that matched the id
     *
     * @param id Object
     * @return
     * @throws EwpException
     */
    @Override
    public Communication getEntity(Object id) throws EwpException {
        String sql = getSQL() + " where LOWER(CommunicationId) = LOWER('" + ((UUID) id).toString() + "') ";
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Get all the Communication Entity from database.
     * Return Collection of Communication Entity.
     *
     * @return
     * @throws EwpException
     */
    @Override
    public List<Communication> getList() throws EwpException {
        String sql = getSQL();
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Get Communication Entity that matches the id and return result as a ResultSet.
     *
     * @param id
     * @return
     * @throws EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFCommunication where LOWER(CommunicationId)= LOWER('" + ((UUID) id).toString() + "')";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Get all Communication Entity record from database and return result as a ResultSet.
     *
     * @return
     * @throws EwpException
     */
    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFCommunication";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Delete Communication entity.
     *
     * @param entity
     * @return
     * @throws EwpException
     */
    @Override
    public void delete(Communication entity) throws EwpException {
        super.deleteRows("PFCommunication", "LOWER(CommunicationId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }


    /**
     * Method is used to Add/update/delete communication list.
     *
     * @param sourceId
     * @param sourceEntityType
     * @return
     */
    public boolean deleteCommunicationListFromSourceEntityIdAndType(UUID sourceId, int sourceEntityType) throws EwpException {
        return super.deleteRows("PFCommunication", "LOWER(EntityId)=? and EntityType=?", new String[]{sourceId.toString().toLowerCase(), String.valueOf(sourceEntityType)});
    }

    @Override
    public long insertEntity(Communication entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("CommunicationId", entity.getEntityId().toString());
        values.put("EntityId", entity.getSourceEntityId().toString());
        values.put("EntityType", entity.getSourceEntityType());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("Type", entity.getCommunicationType().getId());
        values.put("SubType", entity.getCommunicationSubType());
        values.put("Value", entity.getValue());
        values.put("GroupBy", entity.getGroupBy());
        values.put("TenantId", entity.getTenantId().toString());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ApplicationId", entity.getApplicationId());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        return super.insert("PFCommunication", values);
    }

    @Override
    public void update(Communication entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("Type", entity.getCommunicationType().getId());
        values.put("SubType", entity.getCommunicationSubType());
        values.put("Value", entity.getValue());
        values.put("GroupBy", entity.getGroupBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ApplicationId", entity.getApplicationId());
        Log.d(this.getClass().getName(), "" + Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        Date d = new Date();
        entity.setUpdatedAt(d);
        Log.d(this.getClass().getName(), "" + Utils.getUTCDateTimeAsString(d));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(d));
        super.update("PFCommunication", values, "LOWER(CommunicationId)=?", new String[]{entity.getEntityId().toString().toLowerCase()});
    }

    /**
     * Generate sql string with minimum required fields for Communication.
     *
     * @return String
     */
    private String getSQL() {
        String tenantUserId = EwpSession.getSharedInstance().getUserId().toString();
        // Generating the Communication SQL Statement to get the Communication detail. It will give the Communication.
        String sql = "Select comm.*, link.EntityId as FavoriteId from PFCommunication as comm ";
        sql += "Left outer join PFUserentityLink As link on (comm.CommunicationId) = (link.EntityId) and ";
        sql += "(link.createdBy) = ('" + tenantUserId + "') and link.LinkType= " + LinkType.FAVOURITE.getId() + " ";
        return sql;
    }

    /**
     * Get communication from entityid and entitytype.
     *
     * @param sourceEntityId
     * @param sourceEntityType
     * @return
     */
    public List<Communication> getCommunicationListFromSourceEntityIdAndType(UUID sourceEntityId, int sourceEntityType) throws EwpException {
        String sql = getSQL() + " where (comm.EntityId) = ('" + sourceEntityId.toString() + "') and comm.EntityType= " + sourceEntityType + "";
        sql +="  ORDER By DATETIME(comm.CreatedDate)";
        return executeSqlAndGetEntityList(sql);
    }

    /**
     * Get communication from entityid and entitytype.
     *
     * @param sourceEntityId
     * @param sourceEntityType
     * @return Cursor
     * @throws EwpException
     */
    public Cursor getCommunicationListFromSourceEntityIdAndTypeAsResultSet(UUID sourceEntityId, int sourceEntityType) throws EwpException {
        String sql = getSQL() + " where LOWER(comm.EntityId) = LOWER('" + sourceEntityId.toString() + "') and comm.EntityType= '" + sourceEntityType + "' ";
        return executeSqlAndGetResultSet(sql);
    }


    public Cursor getCommunicationListFromSourceEntityIdTypeAndCommunicationTypeAsResultSet(UUID sourceEntityId, int sourceEntityType, int type) throws EwpException {
        String sql = getSQL() + " where LOWER(comm.EntityId) = LOWER('" + sourceEntityId.toString() + "') and comm.EntityType= '" + sourceEntityType + "' and Type = '" + type + "' ";
        return executeSqlAndGetResultSet(sql);
    }

    /**
     * Method is used to find an row exist with type or subtype.
     *
     * @param sourceEntityId
     * @param type           Communication type is mandatory to pass as parameter.
     * @param subType        Communication subtype is an optional parameter to pass as parameter.
     * @return boolean
     * @throws EwpException
     */
    public boolean hasCommunicationInTypeAndSubtype(UUID sourceEntityId, int type, int subType) throws EwpException {
        String sql = getSQL();
        sql += " where LOWER(comm.EntityId)= LOWER('" + sourceEntityId.toString() + "') and Type = '" + type + "' ";
        if (subType != 0) {
            sql += "And SubType = '" + subType + "'";
        }
        return SqlUtils.recordExists(sql);
    }

    /**
     * Method is used to find a row from type or subtype.
     *
     * @param sourceEntityId
     * @param type           Communication type is mandatory to pass as parameter.
     * @param subType        Communication subtype is an optional parameter to pass as parameter.
     * @return List<Communication>
     * @throws EwpException
     */
    public List<Communication> getCommunicationListFromTypeAndSubtype(UUID sourceEntityId, int type, int subType) throws EwpException {
        String sql = getSQL();
        sql += " where (comm.EntityId) = ('" + sourceEntityId.toString() + "') and Type = '" + type + "' ";
        if (subType != 0) {
            sql += "And SubType = '" + subType + "'";
        }
        return executeSqlAndGetEntityList(sql);
    }

    @Override
    public void setPropertiesFromResultSet(Communication entity, Cursor cursor) throws EwpException {
        String id = cursor.getString(cursor.getColumnIndex("TenantId"));
        if (id != null && !"".equals(id)) {
            entity.setTenantId(UUID.fromString(id));
        }

        String communicationId = cursor.getString(cursor.getColumnIndex("CommunicationId"));
        if (communicationId != null && !"".equals(communicationId)) {
            entity.setEntityId(UUID.fromString(communicationId));
        }

        int type = cursor.getInt(cursor.getColumnIndex("Type"));
        if (type == 1) {
            entity.setCommunicationType(CommunicationType.PHONE);
        } else if (type == 2) {
            entity.setCommunicationType(CommunicationType.EMAIL);
        } else if (type == 3) {
            entity.setCommunicationType(CommunicationType.SOCIAL);
        }
        String subType = cursor.getString(cursor.getColumnIndex("SubType"));
        if (subType != null && !"".equals(subType)) {
            entity.setCommunicationSubType(Integer.parseInt(subType));
        }

        String sourceEntityId = cursor.getString(cursor.getColumnIndex("EntityId"));
        if (sourceEntityId != null && !"".equals(sourceEntityId)) {
            entity.setSourceEntityId(UUID.fromString(sourceEntityId));
        }
        if (cursor.getColumnIndex("GroupBy") >= 0) {
            int grpBy = cursor.getInt(cursor.getColumnIndex("GroupBy"));
            entity.setGroupBy(grpBy);
        }
        String entityType = cursor.getString(cursor.getColumnIndex("EntityType"));
        if (entityType != null && !"".equals(entityType)) {
            entity.setSourceEntityType(Integer.parseInt(entityType));
        }

        String value = cursor.getString(cursor.getColumnIndex("Value"));
        if (value != null && !"".equals(value)) {
            entity.setValue(value.replaceAll("''", "'"));
        }

        String applicationId = cursor.getString(cursor.getColumnIndex("ApplicationId"));
        if (applicationId != null && !"".equals(applicationId)) {
            entity.setApplicationId(applicationId);
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

        String favorite = cursor.getString(cursor.getColumnIndex("FavoriteId"));
        if (favorite != null && !"".equals(favorite)) {
            entity.setFavorite(!Utils.emptyUUID().equals(UUID.fromString(favorite)));
        }

        String updatedAt = cursor.getString(cursor.getColumnIndex("ModifiedDate"));
        if (updatedAt != null && !"".equals(updatedAt)) {
            entity.setUpdatedAt(Utils.dateFromString(updatedAt, true, true));
        }

        entity.setDirty(false);
    }
}
