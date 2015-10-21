//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/28/2015
//===============================================================================
package com.eworkplaceapps.platform.dbsync;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.AppConfig;
import com.eworkplaceapps.platform.helper.AppConfigData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class provides data operation and transformation support to SyncService
 * class.
 */
public class SyncDataProvider {

    // The device id of this device.
    private String myDeviceId = "";

    private AppConfig appConfig;

    private String databaseVersion = "";

    // Sql support
    private SyncSqlSupport sqlSupport;

    /**
     * @param sqlSupport
     * @throws EwpException
     */
    public SyncDataProvider(SyncSqlSupport sqlSupport) throws EwpException {
        this.sqlSupport = sqlSupport;
        appConfig = new AppConfigData().getAppConfig();
        // GET current device id from the current device.
        this.myDeviceId = appConfig.getClientDeviceId();
        this.databaseVersion = appConfig.getDatabaseVersion();
    }
    /**
     * @param sqlSupport
     * @param appConfig
     * @throws EwpException
     */
    public SyncDataProvider(SyncSqlSupport sqlSupport,AppConfig appConfig) throws EwpException {
        this.sqlSupport = sqlSupport;
        this.appConfig = appConfig;
        // GET current device id from the current device.
        this.myDeviceId = appConfig.getClientDeviceId();
        this.databaseVersion = appConfig.getDatabaseVersion();
    }
    /**
     * @return
     */
    public String getMyDeviceId() {
        return myDeviceId;
    }

    /**
     * @return
     */
    public String getServerDeviceId() {
        String s = appConfig.getServerDeviceId();
        return s;
    }

    /**
     * @param deviceId
     * @return
     */
    public Date lastReceiveTime(String deviceId) {
        Date date = sqlSupport.getLastSyncTime(deviceId, true);
        return date;
    }

    /**
     * @param deviceId
     * @return
     */
    public Date lastSendTime(String deviceId) {
        Date date = sqlSupport.getLastSyncTime(deviceId, false);
        return date;
    }


    /**
     * Validate if the local database version matches the remote database version
     * for syncing.
     *
     * @param requestDatabaseVersion
     * @return
     */
    public boolean validateDatabaseVersion(String requestDatabaseVersion) {
        String myDatabaseVersion = appConfig.getDatabaseVersion();
        boolean b = myDatabaseVersion.equals(requestDatabaseVersion);
        return b;
    }

    /**
     * GET the SyncItem list, bounded by createdTime limits, excluding the items for a given device id.
     * Note that the relationship '>' is used for afterSyncTime, and '<=" for toSyncTime.
     * If excludeDeviceId is nil, that test is skipped.
     * If afterSyncTime is nil, that test is skipped.
     * If toSyncTime is nil, that test is skipped.
     *
     * @param includeDeviceId
     * @param afterSyncTime
     * @param toSyncTime
     * @return
     * @throws EwpException
     */
    public List<SyncItem> getSyncItemList(String includeDeviceId, Date afterSyncTime, Date toSyncTime) throws EwpException {
        List<SyncItem> list = sqlSupport.getSyncItemList(includeDeviceId, afterSyncTime, toSyncTime);
        return list;
    }

    /**
     * For SQLite (Markers used to group)
     * Generates SyncOp list from the given SyncItem list.
     *
     * @param syncItemList
     * @return
     */
    public List<SyncOp> generateSyncOpList(List<SyncItem> syncItemList) {
        // Init opList
        List<SyncOp> opList = new ArrayList<SyncOp>();
        // Return empty if no input data
        if (syncItemList.isEmpty()) {
            return opList;
        }
         /*A new SyncOp record is created everytime a BEGIN_OP marker is detected.
         The group closes on the next END_OP marker.
         Every SyncOp gets a transaction id. A new transaction id is
         assigned on detecting a BEGIN_TRANS op, and used until the
         next END_TRANS op. For free floating SyncOps (not bounded
         inside a Begin/End Trans pair), a new transaction id is to
         each SyncOp.
         SyncTransactionId is a required field. It is required at the next
         Transaction grouping level. It is required to be unique within
         the opList being generated here. So we can start it with 1, and auto
         increment by 1.*/
        boolean processingOpMarker = false; // To record the marker processing state
        SyncOp currentSyncOp = new SyncOp();
        boolean processingTransaction = false;
        int currentTransactionId = 0;
        for (SyncItem item : syncItemList) {
            // Within the processingOpMarker
            if (processingOpMarker) {
                // Check if to end the current op
                if (SyncItem.SyncOpType.END_OP.equalsIgnoreCase(item.getOpType())) {
                    // Skip the empty case. Possible for an UPDATE op which has no change in col values.
                    boolean skip = false;
                    if ((SyncItem.SyncOpType.INSERT.equalsIgnoreCase(currentSyncOp.getOpType())) || (SyncItem.SyncOpType.UPDATE.equalsIgnoreCase(currentSyncOp.getOpType()))) {
                        skip = currentSyncOp.getColumnValues().isEmpty();
                    }
                    if (!skip) {
                        opList.add(currentSyncOp);
                    }
                    processingOpMarker = false;
                } else if ((SyncItem.SyncOpType.INSERT.equalsIgnoreCase(item.getOpType())) || (SyncItem.SyncOpType.UPDATE.equalsIgnoreCase(item.getOpType())) ||
                        (SyncItem.SyncOpType.DELETE.equalsIgnoreCase(item.getOpType()))) {
                    // ADD the SyncItem data to the current SyncOp
                    item.setSyncTransactionId(String.valueOf(currentTransactionId));
                    currentSyncOp.addSyncItem(item);
                }
                continue;
            }
            if (SyncItem.SyncOpType.BEGIN_OP.equalsIgnoreCase(item.getOpType())) {
                currentSyncOp = new SyncOp();
                processingOpMarker = true;
                // If not within processingTransaction, then it is free standing op.
                if (!processingTransaction) {
                    currentTransactionId++;
                }
                continue;
            }
            if (SyncItem.SyncOpType.BEGIN_TRANS.equalsIgnoreCase(item.getOpType())) {
                processingTransaction = true;
                currentTransactionId++;
                continue;
            }
            if (SyncItem.SyncOpType.END_TRANS.equalsIgnoreCase(item.getOpType())) {
                processingTransaction = false;
                continue;
            }
        }
        return opList;
    }

    /**
     * Generates SyncTransaction list from the given SyncOp list.
     *
     * @param opList
     * @return
     */
    public List<SyncTransaction> generateSyncTransactionList(List<SyncOp> opList) {
        // Init transList
        List<SyncTransaction> transList = new ArrayList<SyncTransaction>();
        // Return empty if no input data
        if (opList.isEmpty()) {
            return transList;
        }

        // Following two variables are used to keep track of current state.
        // The property, syncTransactionId, in SyncOp is the key for SyncTransaction, so when
        // it changes, a new SyncOpTransaction instance is needed.
        // currentSyncTrans instance holds all SyncOp data constituting the current
        // trans instance.
        String currentSyncTransId = "0";
        SyncTransaction currentSyncTrans = new SyncTransaction(); // Init needed by the compiler

        for (SyncOp op : opList) {
            // Check if a new SyncOp instance is to be started.
            boolean startNextTrans = currentSyncTransId != op.getSyncTransactionId();
            // Yes, a new Trans instance
            if (startNextTrans) {
                // If this is not the first time, add the current Trans to the list.
                if (!"0".equals(currentSyncTransId)) {
                    transList.add(currentSyncTrans);
                }

                // Reset state vars: transId and trans. Note that this works for the first time also.
                currentSyncTransId = op.getSyncTransactionId();
                currentSyncTrans = new SyncTransaction();
                currentSyncTrans.setSyncTransactionId(currentSyncTransId);
                currentSyncTrans.setDeviceId(op.getDeviceId());
            }
            // ADD the SyncOp data to the current SyncTransaction
            currentSyncTrans.getSyncOpList().add(op);
        }
        // The last interaction trans to be added.
        transList.add(currentSyncTrans);
        return transList;
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public String getDatabaseVersion() {
        return databaseVersion;
    }

    public SyncSqlSupport getSqlSupport() {
        return sqlSupport;
    }
}
