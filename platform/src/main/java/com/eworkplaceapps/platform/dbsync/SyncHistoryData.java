//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/28/2015
//===============================================================================
package com.eworkplaceapps.platform.dbsync;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by admin on 4/28/2015.
 */
public class SyncHistoryData extends BaseData<SyncHistory> {

    private String getSQL() {
        String sql = " SELECT * from SyncHistory";
        return sql;
    }

    /**
     * GET the  SyncHistory entity
     *
     * @return
     */
    public SyncHistory createEntity() {
        return SyncHistory.createEntity();
    }

    @Override
    public SyncHistory getEntity(Object id) throws EwpException {
        // Building select statement
        String sql = getSQL() + " where RowId = '" + ((UUID) id).toString() + "'";
        // Executes select statement and return SyncHistory entity
        return executeSqlAndGetEntity(sql);
    }

    /**
     * Fetch all the SyncHistory Entity record from database.
     * returns Collection of SyncHistory Entity.
     *
     * @return
     */
    public List<SyncHistory> getList() throws EwpException {
        String mySql = "SELECT * From SyncHistory ";
        return executeSqlAndGetEntityList(mySql);
    }

    /**
     * Fetch all the SyncHistory Entity record from database and return as resultSet.
     *
     * @return
     */
    public Cursor getListAsResultSet() throws EwpException {
        String sql = getSQL();
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public long insertEntity(SyncHistory entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("RowId", entity.getEntityId().toString());
        values.put("ActionStartTime", Utils.getUTCDateTimeAsString(entity.getActionStartTime()));
        values.put("Action", entity.getAction());
        values.put("ActionEndTime", Utils.getUTCDateTimeAsString(entity.getActionEndTime()));
        values.put("RemoteDeviceName", entity.getRemoteDeviceName());
        values.put("RemoteDeviceId", entity.getRemoteDeviceId());
        values.put("StatusCode", entity.getStatusCode());
        values.put("StatusMessage", entity.getStatusMessage());
        values.put("ReqAfterSyncTime", Utils.getUTCDateTimeAsString(entity.getReqAfterSyncTime()));
        values.put("ReqToSyncTime", Utils.getUTCDateTimeAsString(entity.getReqToSyncTime()));
        values.put("ActualAfterSyncTime", Utils.getUTCDateTimeAsString(entity.getActualAfterSyncTime()));
        values.put("ActualToSyncTime", Utils.getUTCDateTimeAsString(entity.getActualToSyncTime()));
        values.put("Partial", entity.isPartial());
        values.put("NumTrans", entity.getNumTrans());
        values.put("NumOps", entity.getNumOps());
        values.put("NumItems", entity.getNumItems());
        values.put("Conflicts", entity.isConflicts());
        values.put("SyncRequestData", entity.getSyncRequestData());
        values.put("SyncReplyData", entity.getSyncReplyData());
        long id = super.insert("SyncHistory", values);
        return id;
    }

    @Override
    public void update(SyncHistory entity) throws EwpException {
        ContentValues values = new ContentValues();
        Date d = new Date();
        if (entity.getActionStartTime() != null) {
            d = entity.getActionStartTime();
        }
        values.put("ActionStartTime", Utils.getUTCDateTimeAsString(d));
        values.put("Action", entity.getAction());
        if (entity.getActionEndTime() != null) {
            d = entity.getActionEndTime();
        }
        values.put("ActionEndTime", Utils.getUTCDateTimeAsString(d));
        values.put("RemoteDeviceName", entity.getRemoteDeviceName());
        values.put("RemoteDeviceId", entity.getRemoteDeviceId());
        values.put("StatusCode", entity.getStatusCode());
        values.put("StatusMessage", entity.getStatusMessage());
        if (entity.getReqAfterSyncTime() != null) {
            d = entity.getReqAfterSyncTime();
        }
        values.put("ReqAfterSyncTime", Utils.getUTCDateTimeAsString(d));
        if (entity.getReqToSyncTime() != null) {
            d = entity.getReqToSyncTime();
        }
        values.put("ReqToSyncTime", Utils.getUTCDateTimeAsString(d));
        if (entity.getActualAfterSyncTime() != null) {
            d = entity.getActualAfterSyncTime();
        }
        values.put("ActualAfterSyncTime", Utils.getUTCDateTimeAsString(d));
        if (entity.getActualToSyncTime() != null) {
            d = entity.getActualToSyncTime();
        }
        values.put("ActualToSyncTime", Utils.getUTCDateTimeAsString(d));
        values.put("Partial", entity.isPartial());
        values.put("NumTrans", entity.getNumTrans());
        values.put("NumOps", entity.getNumOps());
        values.put("NumItems", entity.getNumItems());
        values.put("Conflicts", entity.isConflicts());
        values.put("SyncRequestData", entity.getSyncRequestData());
        values.put("SyncReplyData", entity.getSyncReplyData());
        int id = super.update("SyncHistory", values, "RowId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Set property value from  database ResultSet.
     *
     * @param resultSet
     */
    @Override
    public void setPropertiesFromResultSet(SyncHistory syncHistory, Cursor resultSet) {
        String action = resultSet.getString(resultSet.getColumnIndex("Action"));
        if (action != null) {
            syncHistory.setAction(action);
        }

        String actionStartTime = resultSet.getString(resultSet.getColumnIndex("ActionStartTime"));
        if (actionStartTime != null && !"".equals(actionStartTime)) {
            syncHistory.setActionStartTime(Utils.dateFromString(actionStartTime, true, true));
        }

        String actionEndTime = resultSet.getString(resultSet.getColumnIndex("ActionEndTime"));
        if (actionEndTime != null) {
            syncHistory.setActionEndTime(Utils.dateFromString(actionEndTime, true, true));
        }

        String reqAfterSyncTime = resultSet.getString(resultSet.getColumnIndex("ReqAfterSyncTime"));
        if (reqAfterSyncTime != null && !"".equals(reqAfterSyncTime)) {
            syncHistory.setReqAfterSyncTime(Utils.dateFromString(reqAfterSyncTime, true, true));
        }

        String reqToSyncTime = resultSet.getString(resultSet.getColumnIndex("ReqToSyncTime"));
        if (reqToSyncTime != null) {
            syncHistory.setReqToSyncTime(Utils.dateFromString(reqToSyncTime, true, true));
        }

        String remoteDeviceName = resultSet.getString(resultSet.getColumnIndex("RemoteDeviceName"));
        if (remoteDeviceName != null) {
            syncHistory.setRemoteDeviceName(remoteDeviceName);
        }

        String status = resultSet.getString(resultSet.getColumnIndex("StatusCode"));
        if (status != null) {
            syncHistory.setStatusCode(status);
        }

        String statusMessage = resultSet.getString(resultSet.getColumnIndex("StatusMessage"));
        if (statusMessage != null) {
            syncHistory.setStatusMessage(statusMessage);
        }

        String actualAfterSyncTime = resultSet.getString(resultSet.getColumnIndex("ActualAfterSyncTime"));
        if (actualAfterSyncTime != null && !"".equals(actualAfterSyncTime)) {
            syncHistory.setActualAfterSyncTime(Utils.dateFromString(actualAfterSyncTime, true, true));
        }

        String actualToSyncTime = resultSet.getString(resultSet.getColumnIndex("ActualToSyncTime"));
        if (actualToSyncTime != null && !"".equals(actualToSyncTime)) {
            syncHistory.setActualToSyncTime(Utils.dateFromString(actualToSyncTime, true, true));
        }

        boolean partial = Boolean.parseBoolean(resultSet.getString(resultSet.getColumnIndex("Partial")));
        syncHistory.setPartial(partial);

        String numTrans = resultSet.getString(resultSet.getColumnIndex("NumTrans"));
        if (numTrans != null && !"".equals(numTrans)) {
            syncHistory.setNumTrans(Integer.parseInt(numTrans));
        }

        String numOps = resultSet.getString(resultSet.getColumnIndex("NumOps"));
        if (numOps != null && !"".equals(numOps)) {
            syncHistory.setNumOps(Integer.parseInt(numOps));
        }

        String numItems = resultSet.getString(resultSet.getColumnIndex("NumItems"));
        if (numItems != null && !"".equals(numItems)) {
            syncHistory.setNumItems(Integer.parseInt(numItems));
        }

        boolean conflicts = Boolean.parseBoolean(resultSet.getString(resultSet.getColumnIndex("Conflicts")));
        syncHistory.setConflicts(conflicts);

        String syncRequestData = resultSet.getString(resultSet.getColumnIndex("SyncRequestData"));
        if (syncRequestData != null) {
            syncHistory.setSyncRequestData(syncRequestData);
        }

        String syncReplyData = resultSet.getString(resultSet.getColumnIndex("SyncReplyData"));
        if (syncReplyData != null) {
            syncHistory.setSyncReplyData(syncReplyData);
        }
        syncHistory.setDirty(false);
    }
}
