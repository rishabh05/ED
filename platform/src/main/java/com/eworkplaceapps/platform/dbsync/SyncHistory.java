//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/28/2015
//===============================================================================
package com.eworkplaceapps.platform.dbsync;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;

import java.util.Date;

/**
 * This class encapsulates all data for SyncHistory entity.
 */
public class SyncHistory extends BaseEntity {

    private static final String SYNC_HISTORY_ENTITY_NAME = "SyncHistory";

    public SyncHistory() {
        super(SYNC_HISTORY_ENTITY_NAME);
    }

    /**
     * Create SyncHistory object and return created object.
     *
     * @return
     */
    public static SyncHistory createEntity() {
        return new SyncHistory();
    }

    // Getter/setter for Action
    private String action;

    /// Getter/Setter for  ActionStartTime
    private Date actionStartTime=new Date();

    /// Getter/Setter for  ActionEndTime
    private Date actionEndTime=new Date();

    private String remoteDeviceName;

    private String remoteDeviceId = "";

    private String statusCode;

    private String statusMessage;

    /// Getter/Setter for  ReqAfterSyncTime
    private Date reqAfterSyncTime=new Date();

    /// Getter/Setter for  requied To SyncTime
    private Date reqToSyncTime=new Date();

    private Date actualAfterSyncTime=new Date();

    private Date actualToSyncTime=new Date();

    private boolean partial = false;

    private int numTrans;

    private int numOps;

    private int numItems;

    private boolean conflicts = false;

    private String syncRequestData;

    private String syncReplyData;

    public static String getSyncHistoryEntityName() {
        return SYNC_HISTORY_ENTITY_NAME;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        setPropertyChanged(this.action, action);
        this.action = action;
    }

    public Date getActionStartTime() {
        return actionStartTime;
    }

    public void setActionStartTime(Date actionStartTime) {
        setPropertyChanged(this.actionStartTime, actionStartTime);
        this.actionStartTime = actionStartTime;
    }

    public Date getActionEndTime() {
        return actionEndTime;
    }

    public void setActionEndTime(Date actionEndTime) {
        setPropertyChanged(this.actionEndTime, actionEndTime);
        this.actionEndTime = actionEndTime;
    }

    public String getRemoteDeviceName() {
        return remoteDeviceName;
    }

    public void setRemoteDeviceName(String remoteDeviceName) {
        setPropertyChanged(this.remoteDeviceName, remoteDeviceName);
        this.remoteDeviceName = remoteDeviceName;
    }

    public String getRemoteDeviceId() {
        return remoteDeviceId;
    }

    public void setRemoteDeviceId(String remoteDeviceId) {
        setPropertyChanged(this.remoteDeviceId, remoteDeviceId);
        this.remoteDeviceId = remoteDeviceId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        setPropertyChanged(this.statusCode, statusCode);
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        setPropertyChanged(this.statusMessage, statusMessage);
        this.statusMessage = statusMessage;
    }

    public Date getReqAfterSyncTime() {
        return reqAfterSyncTime;
    }

    public void setReqAfterSyncTime(Date reqAfterSyncTime) {
        setPropertyChanged(this.reqAfterSyncTime, reqAfterSyncTime);
        this.reqAfterSyncTime = reqAfterSyncTime;
    }

    public Date getReqToSyncTime() {
        return reqToSyncTime;
    }

    public void setReqToSyncTime(Date reqToSyncTime) {
        setPropertyChanged(this.reqToSyncTime, reqToSyncTime);
        this.reqToSyncTime = reqToSyncTime;
    }

    public Date getActualAfterSyncTime() {
        return actualAfterSyncTime;
    }

    public void setActualAfterSyncTime(Date actualAfterSyncTime) {
        setPropertyChanged(this.actualAfterSyncTime, actualAfterSyncTime);
        this.actualAfterSyncTime = actualAfterSyncTime;
    }

    public Date getActualToSyncTime() {
        return actualToSyncTime;
    }

    public void setActualToSyncTime(Date actualToSyncTime) {
        setPropertyChanged(this.actualToSyncTime, actualToSyncTime);
        this.actualToSyncTime = actualToSyncTime;
    }

    public boolean isPartial() {
        return partial;
    }

    public void setPartial(boolean partial) {
        setPropertyChanged(this.partial, partial);
        this.partial = partial;
    }

    public int getNumTrans() {
        return numTrans;
    }

    public void setNumTrans(int numTrans) {
        setPropertyChanged(this.numTrans, numTrans);
        this.numTrans = numTrans;
    }

    public int getNumOps() {
        return numOps;
    }

    public void setNumOps(int numOps) {
        setPropertyChanged(this.numOps, numOps);
        this.numOps = numOps;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        setPropertyChanged(this.numItems, numItems);
        this.numItems = numItems;
    }

    public boolean isConflicts() {
        return conflicts;
    }

    public void setConflicts(boolean conflicts) {
        setPropertyChanged(this.conflicts, conflicts);
        this.conflicts = conflicts;
    }

    public String getSyncRequestData() {
        return syncRequestData;
    }

    public void setSyncRequestData(String syncRequestData) {
        setPropertyChanged(this.syncRequestData, syncRequestData);
        this.syncRequestData = syncRequestData;
    }

    public String getSyncReplyData() {
        return syncReplyData;
    }

    public void setSyncReplyData(String syncReplyData) {
        setPropertyChanged(this.syncReplyData, syncReplyData);
        this.syncReplyData = syncReplyData;
    }

    /**
     * @param action
     * @param syncReplyData
     * @param syncRequestData
     * @param actionStartTime
     * @param actionEndTime
     * @param deviceId
     * @param statusMessage
     * @param statusCode
     */
    public static void addHistory(String action, String syncReplyData, String syncRequestData, Date actionStartTime, Date actionEndTime, String deviceId, String statusMessage, int statusCode) throws EwpException {
        SyncHistory syncHistory = new SyncHistory();
        syncHistory.setRemoteDeviceId(deviceId);
        syncHistory.setAction(action);
        syncHistory.setSyncReplyData(syncReplyData);
        syncHistory.setSyncRequestData(syncRequestData);
        syncHistory.setActionStartTime(actionStartTime);
        syncHistory.setActionEndTime(actionEndTime);
        syncHistory.setRemoteDeviceName("Server");
        syncHistory.setStatusMessage(statusMessage);
        syncHistory.setStatusCode(statusCode + "");
        SyncHistoryData sData = new SyncHistoryData();
        sData.add(syncHistory);
    }
}
