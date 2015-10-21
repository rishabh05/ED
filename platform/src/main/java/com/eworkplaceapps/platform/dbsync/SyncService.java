//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/28/2015
//===============================================================================
package com.eworkplaceapps.platform.dbsync;

import android.util.Log;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.AppConfig;
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.eworkplaceapps.platform.dbsync.SyncConflict.SyncConflictType.UPDATE_DELETED;
import static com.eworkplaceapps.platform.dbsync.SyncConflict.SyncConflictType.UPDATE_UPDATED;

/**
 * This is main service class that executes three services:
 * Generate SYNC Request
 * Process SYNC Request
 * Process SYNC Reply
 * <p/>
 * Overall workflow is as follows:
 * Node1 generates a SyncRequest with its SyncItem data, and sends it to Node2
 * Node2 processes SYNC Request and generate a SYNC Reply and sends it to Node1
 * Node1 processes SYNC Reply and resolves conflicts.
 * <p/>
 * Note that terms 'local' and 'remote' are used to describe the node that initiates
 * the workflow step. E.g., in step 1 above, Node1 is local and Node2 is remote.
 * However, in step 2, Node2 is local and Node1 is remote.
 */
public class SyncService {
    // SQL Support class needed for all SQL ops
    private SyncSqlSupport sqlSupport;
    // DATA provider for the service
    private SyncDataProvider dataProvider;
    // Last sync time for the requester's device
    private Date lastRemoteReceiveSyncTime;
    // Remote request being processed
    private SyncRequest remoteRequest = new SyncRequest();
    // The createdTime value for the last iteration fully processed
    private Date lastIterationSyncTime = new Date();

    public SyncService() throws EwpException {
        this.sqlSupport = new SyncSqlSupport();
        this.dataProvider = new SyncDataProvider(sqlSupport);
    }
    public SyncService(SyncSqlSupport sqlSupport,AppConfig appConfig) throws EwpException {
        this.sqlSupport = sqlSupport;
        this.dataProvider =  new SyncDataProvider(sqlSupport,appConfig);
    }

    public SyncDataProvider getDataProvider() {
        return dataProvider;
    }

    /**
     * Generates SyncRequest to be sent to remote
     * If afterSyncTime is nil, that test is skipped.
     * If toSyncTime is nil, that test is skipped.
     *
     * @param myDeviceId
     * @param afterSyncTime
     * @param toSyncTime
     * @return
     */
    public SyncRequest generateRemoteRequest(String myDeviceId, Date afterSyncTime, Date toSyncTime) throws EwpException {
        // GET the SyncItem list
        Log.d(this.getClass().getName(), " Generating SyncItemList");
        List<SyncItem> syncItemList = dataProvider.getSyncItemList(myDeviceId, afterSyncTime, toSyncTime);
        Log.d(this.getClass().getName(), " Generating SyncOpList");
        List<SyncOp> syncOpList = dataProvider.generateSyncOpList(syncItemList);
        // GET SynTransaction list from SyncOp
        Log.d(this.getClass().getName(), " Generating SyncTransactionList ");
        List<SyncTransaction> transList = dataProvider.generateSyncTransactionList(syncOpList);
        Log.d(this.getClass().getName(), " Generating SyncOpList ");
        Log.d(this.getClass().getName(), " Generating SyncTransactionList ");
        Log.d(this.getClass().getName(), " Generating SyncItemList ");
        // Set up SyncRequest data
        SyncRequest request = new SyncRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setMyDeviceId(dataProvider.getMyDeviceId());
        request.setMyDatabaseVersion(dataProvider.getDatabaseVersion());
        request.setMyApplicationVersion(dataProvider.getAppConfig().getApplicationSuiteVersion());
        // If afterSyncTime is nil, set it to some distant past time.
        Date tAfterSyncTime = Utils.dateFromString("2001-01-01 00:00:00.000", true, true);
        if (afterSyncTime != null) {
            tAfterSyncTime = afterSyncTime;
        }
        request.setAfterSyncTime(tAfterSyncTime);
        // If toSyncTime is nil, set it to last SyncItem CreatedTime.
        // If transList is empty, set it to afterSyncTime.
        if (toSyncTime != null) {
            request.setToSyncTime(toSyncTime);
        }
        // TransList
        request.setSyncTransactionList(transList);
        return request;
    }

    /**
     * Processes the SyncRequest and returns the SyncReply
     *
     * @param remoteRequest
     * @return
     * @throws EwpException
     */
    public SyncReply processRemoteRequest(SyncRequest remoteRequest) throws EwpException {
        // Database version check
        boolean b = dataProvider.validateDatabaseVersion(remoteRequest.getMyDatabaseVersion());
        if (!b) {
            throw new EwpException(new EwpException("Database version are not same"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, null, EnumsForExceptions.ErrorModule.SYNC, null, 0);
        }
        // Set instance vars
        this.remoteRequest = remoteRequest;
        Tuple.Tuple2<Boolean, List<SyncConflict>, Integer> tuple = syncLocalWithRemote();
        List<SyncConflict> syncConflictList = tuple.getT2();
        // Set up SyncReply
        SyncReply reply = new SyncReply();
        reply.setLastSyncRowNumber(tuple.getT3());
        reply.setPartialSync(tuple.getT1());
        reply.setRequestId(remoteRequest.getRequestId());
        //  Map error type and message
        reply.setStatus(0);
        reply.setMessage("");
        reply.setConflictList(syncConflictList);
        // SYNC times
        reply.setAfterSyncTime(remoteRequest.getAfterSyncTime());
        reply.setToSyncTime(this.lastIterationSyncTime);
        // Set local device id.
        reply.setMyDeviceId(dataProvider.getMyDeviceId());

        return reply;
    }

    /**
     * syncLocalWithRemote
     *
     * @return
     * @throws EwpException
     */
    public Tuple.Tuple2<Boolean, List<SyncConflict>, Integer> syncLocalWithRemote() throws EwpException {
        // GET Remote device's last sync time
        this.lastRemoteReceiveSyncTime = sqlSupport.getLastSyncTime(remoteRequest.getMyDeviceId(), true);
        // GET TransList
        List<SyncTransaction> transList = this.remoteRequest.getSyncTransactionList();
        // The first occurrence of an error aborts the syncing process.
        boolean partialSync = false;
        int lastTransNumber = 0;
        // Contains all conflicts generated, ordered by original SyncItemLog order
        List<SyncConflict> fullConflictList = new ArrayList<SyncConflict>();
        if (transList.isEmpty()) {
            lastIterationSyncTime = remoteRequest.getToSyncTime();
            sqlSupport.updateSyncTime(remoteRequest.getMyDeviceId(), this.lastIterationSyncTime, true);
        }
        // Loop for each transaction. Note that a transaction is a unit of complete work.
        // Any error during a transaction results in the transaction roolback.
        // Conflicts are not errors.
        for (SyncTransaction remoteTrans : transList) {
            // Conflict list generated during transaction.
            List<SyncConflict> transConflictList = new ArrayList<SyncConflict>();
            // Mark the last CreatedTime in the op loop
            Date lastOpSyncTime = new Date();
            // Begin Transaction on local
            sqlSupport.beginTransaction();
            // Loop for all SyncOps in a transaction.
            for (SyncOp remoteSyncOp : remoteTrans.getSyncOpList()) {
                SyncConflict opConflict = null;
                boolean customHandled = false;
                // During syncing, records will be inserted/updated/deleted.
                // Their corresponding entries in SyncItemLog table to record
                // requester's device id, because that is the source of operations.
                // Save Trigger DeviceId and set it to request.myDeviceId.
                String triggerDeviceId = sqlSupport.getTriggerDeviceId();
                sqlSupport.setTriggerDeviceId(remoteRequest.getMyDeviceId());
                // If no custom handling, do the default handling
                if (!customHandled) {
                    if (SyncItem.SyncOpType.INSERT.equalsIgnoreCase(remoteSyncOp.getOpType())) {
                        opConflict = insertRemoteToLocal(remoteSyncOp);

                    } else if (SyncItem.SyncOpType.UPDATE.equalsIgnoreCase(remoteSyncOp.getOpType())) {
                        SyncConflict syncConflict = findConflictBySyncOp(fullConflictList, transConflictList, remoteSyncOp);
                        if(syncConflict!=null && syncConflict.conflictType == SyncConflict.SyncConflictType.UPDATE_UPDATED) {
                            removeConflict(fullConflictList, transConflictList, remoteSyncOp);
                            opConflict = updateRemoteToLocal(remoteSyncOp);
                        }
                        else  if(syncConflict!=null && syncConflict.conflictType == SyncConflict.SyncConflictType.UPDATE_DELETED) {
                            // Ignore
                        }
                        else {
                            opConflict = updateRemoteToLocal(remoteSyncOp);
                        }
                    } else if (SyncItem.SyncOpType.DELETE.equalsIgnoreCase(remoteSyncOp.getOpType())) {
                        opConflict = deleteRemoteToLocal(remoteSyncOp);
                    } else {
                        break;
                    }
                }
                // Now restore the trigger device id.
                sqlSupport.setTriggerDeviceId(triggerDeviceId);
                // UPDATE sync time
                lastOpSyncTime = remoteSyncOp.getCreatedTime();
                // Store conflict in the TransConflict array
                if (opConflict != null) {
                    transConflictList.add(opConflict);
                }
            }
            // Record it in the instance var for use in creating SyncReply
            this.lastIterationSyncTime = lastOpSyncTime;
            // Record the last sync time in the Device row
            sqlSupport.updateSyncTime(remoteRequest.getMyDeviceId(), lastOpSyncTime, true);
            lastTransNumber = remoteTrans.getSyncRowNumber();
            // Commit the transaction
            sqlSupport.commitTransaction();
            // Accumulate trans conflicts
            fullConflictList.addAll(transConflictList);
        }
        Boolean partialSync1 = partialSync;
        Integer ltNo = lastTransNumber;
        return Tuple.tuple2(partialSync1, fullConflictList, ltNo);
    }

    /**
     * insertRemoteToLocal
     *
     * @param remoteOp
     * @return
     * @throws EwpException
     */
    private SyncConflict insertRemoteToLocal(SyncOp remoteOp) throws EwpException {

        // INSERT Operation:
        // Record ~exist: on local (as determined by TableName/PKName/PKValue)
        //   INSERT remote record to local
        // Record exists:
        //   Valid possibility: Client1 synced with client2; Client2 synced with server;
        //     Client1 synced with server
        //     (In our code, local corresponds to server and remote to Client1)
        //     This is tested by checking INSERT Op in SyncItem for the given record ids
        //     and remote deviceId.
        //   ~Valid possibility:
        //     Conflict- DUPLICATE_INSERT

        //EwpError error = new EwpError();
        SyncConflict opConflict = null;
        boolean response = sqlSupport.recordExists(remoteOp.getTableName(), remoteOp.getPkName(), remoteOp.getPkValue());

        boolean exists = response;

        // Record does not exist
        if (!exists) {
            //STRING sql = sqlSupport.generateINSERTStatement(remoteOp);
            int n = sqlSupport.executeInsertQuery(remoteOp);
            if (n < 1) {
                Log.d(this.getClass().getName(), "---------- insertRemoteToLocal : DATABASE_ERROR ----------");
            }
        } else {
            // Already inserted by the same device
            if (sqlSupport.syncItemExistsForOp("INSERT", remoteOp.getTableName(), remoteOp.getPkName(), remoteOp.getPkValue(), remoteOp.getDeviceId())) {
                // OK. Skip insert.
            } else {  // Already inserted by another device
                // Its condition because we do not keep picklistitem and pingmessage in syncitem log at iOS
                if (isContainInsertTrigger(remoteOp)) {
                    //   error.errorType = EnumsForExceptions.ErrorType.VALIDATION_ERROR;
                }
                //Logger.defaultLogger.logMessage("******* insertRemoteToLocal : VALIDATION_ERROR *******")
            }
        }

        return opConflict;
    }

    // changed by amit 17-oct
    private boolean isContainInsertTrigger(SyncOp remoteOp){
        if (!"PFPicklistItem".equalsIgnoreCase(remoteOp.getTableName()) && !"PFUserPreference".equalsIgnoreCase(remoteOp.getTableName())
                && !"EDPingMessage".equalsIgnoreCase(remoteOp.getTableName()) && !"PFTenantUser".equalsIgnoreCase(remoteOp.getTableName())
                && !"edemployee".equalsIgnoreCase(remoteOp.getTableName())
                && !"ITPermission".equalsIgnoreCase(remoteOp.getTableName()) && !"PFRole".equalsIgnoreCase(remoteOp.getTableName())
                && !"PFRoleLinking".equalsIgnoreCase(remoteOp.getTableName()) && !"PFRolePermission".equalsIgnoreCase(remoteOp.getTableName())) {
            //   error.errorType = EnumsForExceptions.ErrorType.VALIDATION_ERROR;
        }
        return false;
    }

    /**
     * updateRemoteToLocal
     *
     * @param remoteOp
     * @return
     * @throws EwpException
     */
    private SyncConflict updateRemoteToLocal(SyncOp remoteOp) throws EwpException {
        // UPDATE Operation:
        // Record exists: on local (as determined by TableName/PKName/PKValue)
        //   GET LastSyncType for remote device on local
        //   GET record ModifiedDate on local
        //   (a) ModifiedDate <= LastSyncTime --> record was modified on local before
        //       the last sync with remote
        //   (b) ModifiedDate > LastSyncTime --> record was modified on local after
        //       the last sync with remote
        //   a: OK to UPDATE
        //   b: If record modified by same remote, then it means both modifications
        //      from same device.
        //     b1 (b true):
        //       c1: ModifiedTime <= Op CreatedTime --> OK to UPDATE
        //       c2: ModifiedTime > Op CreatedTime --> No-op. Skip.
        //     b2 (b false): Conflict - UPDATE_UPDATED
        // Record ~Exist: on local (as determinced by TableName/PKName/PKValue)
        //   Record deleted on local (Checked in SyncItem): Conflict- UPDATE_DELETED
        //   Record never inserted on local (Checked in SyncItem): Conflict- UPDATE_NEVER_INSERTED
        // EwpError error = new EwpError();
        SyncConflict opConflict = null;
        boolean ok = false;
        boolean response = sqlSupport.recordExists(remoteOp.getTableName(), remoteOp.getPkName(), remoteOp.getPkValue());

        boolean exists = response;
        // Record exists
        if (exists) {
            // Check last modified date
            Date modifiedDate = sqlSupport.getModifiedDateForRecord(remoteOp.getTableName(), remoteOp.getPkName(), remoteOp.getPkValue());
            String index = remoteOp.getColumnValues().get("ModifiedDate");
            if (index == null) {
                Log.d(this.getClass().getName(), "SYNC item has not found ModifiedDate, So we are skipping the below table data to sync.");
                //Logger.defaultLogger.logError("Database table name: " + remoteOp.tableName + ", PrimaryKeyName: " + remoteOp.pkName + ", PrimaryKeyValue: " + remoteOp.pkValue)
                String val = "SyncModifiedDate table name: " + remoteOp.getTableName() + ", PrimaryKeyName: " + remoteOp.getPkName() + ", PrimaryKeyValue: " + remoteOp.getPkValue();
                Log.d(this.getClass().getName(), "SYNC item has not found ModifiedDate, So we are skipping the below table data to sync. " + val);
                // println(val)
                return opConflict;
            }
            String deviceId = sqlSupport.syncItemDeviceIdForOp("UPDATE", remoteOp.getTableName(), remoteOp.getPkName(), remoteOp.getPkValue());
            Date remoteModifiedDate = Utils.dateFromString(index, true, true);
            if ((modifiedDate != null && modifiedDate.compareTo(remoteModifiedDate) <= 0) || remoteOp.getTableName().toLowerCase().equals("PFTenantUser".toLowerCase())) {
                ok = true;
            } else if (deviceId == remoteRequest.getMyDeviceId()) {
                // skip
            } else { // Remote modified date less then local
                SyncOp syncOp = sqlSupport.getLatestLocalSyncOp(remoteOp, "");
                if (syncOp != null) {
                    opConflict = new SyncConflict(UPDATE_UPDATED, syncOp);
                }
                else {
                    opConflict = new SyncConflict(UPDATE_UPDATED, remoteOp);
                }
            }
        } else {  // Record does not exist
            // Check if record deleted
            if (sqlSupport.syncItemExistsForOp("DELETE", remoteOp.getTableName(), remoteOp.getPkName(), remoteOp.getPkValue(), null)) {
                // Conflict
                if (!remoteOp.getTableName().equalsIgnoreCase("EDEmployee") ) {
                    opConflict = new SyncConflict(UPDATE_DELETED, remoteOp);
                }
            }
        }
        // UPDATE
        if (ok) {
            //STRING sql = sqlSupport.generateUPDATEStatement(remoteOp);
            int n = sqlSupport.executeUpdateQuery(remoteOp);
            //int n = sqlSupport.executeNonQuery(sql);
            if (n != 1) {
                // throw new EwpException(new EwpException("Database ERROR"), EnumsForExceptions.ErrorType.DATABASE_ERROR, null, EnumsForExceptions.ErrorModule.SYNC, null, 0);
                Log.d(this.getClass().getName(), "---------- updateRemoteToLocal : Record updated failed for ----------" + remoteOp.getPkValue());
            } else {
                //Logger.defaultLogger.logMessage("******* updateRemoteToLocal : Record updated for ******* \(remoteOp.pkValue)" )
            }
        }
        return opConflict;
    }

    /**
     * deleteRemoteToLocal
     *
     * @param remoteOp
     * @return
     * @throws EwpException
     */
    private SyncConflict deleteRemoteToLocal(SyncOp remoteOp) throws EwpException {
        // DELETE Operation:
        // Record exists: on local (as determined by TableName/PKName/PKValue)
        //   GET LastSyncType for remote device on local
        //   GET record ModifiedDate on local
        //   (a) ModifiedDate <= LastSyncTime --> record was modified on local before the last sync with remote
        //   (b) ModifiedDate > LastSyncTime --> record was modified on local after the last sync with remote
        //   a: OK to DELETE
        //   b: If record modified by same remote, then it means both update/delete from same device.
        //     b1: b true:
        //       c1: ModifiedTime <= Op CreatedTime --> OK to DELETE
        //       c2: ModifiedTime > Op CreatedTime --> No-op. Skip.
        //     b2: b false: Conflict - DELETE_UPDATED
        // Record ~Exist: on local (as determined by TableName/PKName/PKValue)
        //   Skip
        //
        //   Hari:  Asha's Note:
        //   INSERT Operation by this client must have generate some conflict –
        //   (this can be checked by the SysItemLog , if the RowID does not exist
        //   that implies that row was never inserted in the DB) –
        //   this should be recorded as Retry for the next time as we do not know
        //   what client action will be.
        SyncConflict opConflict = null;
        boolean ok = false;
        boolean response = sqlSupport.recordExists(remoteOp.getTableName(), remoteOp.getPkName(), remoteOp.getPkValue());

        boolean exists = response;
        // Record exists
        if (exists) {
            ok = true;
        } else { // Record does not exist
        }
        // DELETE
        if (ok) {
            int n = sqlSupport.executeDeleteQuery(remoteOp);
            if (n < 1) {
                Log.d(this.getClass().getName(), "---------- deleteRemoteToLocal : Deletion failed for ---------- " + remoteOp.getPkValue());
            }
        }
        return opConflict;
    }

    /**
     * DELETE local record
     *
     * @param op
     * @return
     * @throws EwpException
     */
    private boolean deleteLocalRecord(SyncOp op) throws EwpException {
        String sql = sqlSupport.generateDELETEStatement(op);
        int n = sqlSupport.executeNonQuery(sql);
        if (n != 1) {
            throw new EwpException(new EwpException("Database ERROR"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, null, EnumsForExceptions.ErrorModule.SYNC, null, 0);
        }
        return true;
    }

    /**
     * Process SyncReply
     *
     * @param remoteReply
     * @return
     * @throws EwpException
     */
    public boolean processRemoteReply(SyncReply remoteReply) throws EwpException {
        // UPDATE SyncTimes
        boolean isSuccess = sqlSupport.updateSyncTime(remoteReply.getMyDeviceId(), remoteReply.getToSyncTime(), false);
        String triggerDeviceId = "";
        if (remoteReply.getConflictList().size() > 0) {
            triggerDeviceId = sqlSupport.getTriggerDeviceId();
            sqlSupport.setTriggerDeviceId(this.dataProvider.getAppConfig().getServerDeviceId());
        }

        for (SyncConflict conflict : remoteReply.getConflictList()) {
            boolean customHandled = false;
            // If no custom handling, do the default handling
            switch (conflict.conflictType) {
                case DUPLICATE_INSERT:
                    sqlSupport.executeDeleteQuery(conflict.originalSyncOp);
                    break;
                case UPDATE_UPDATED:
                    // How to update?
                    updateFromServerConfilctRow(conflict.originalSyncOp, "");
                    break;
                case UPDATE_DELETED:
                    sqlSupport.executeDeleteQuery(conflict.originalSyncOp);
                    break;
                case DELETE_UPDATED:
                    break;
                default:
                    break;
            }
        }

        if (remoteReply.getConflictList().size() > 0) {
            sqlSupport.setTriggerDeviceId(triggerDeviceId);
        }
        return true;
    }

    /**
     * @return
     */
    public boolean inSyncing() {
        return false;
    }

    /**
     *
     * @param fullConflictList
     * @param transConflictList
     * @param remoteSyncOp
     * @return
     */
    private SyncConflict findConflictBySyncOp(List<SyncConflict> fullConflictList,List<SyncConflict> transConflictList,SyncOp remoteSyncOp){
        boolean found = false;
        // if already added in conflict item then
        for (int i = transConflictList.size()-1; i >= 0; i--) {
            if (transConflictList.get(i).originalSyncOp.getPkValue().equalsIgnoreCase(remoteSyncOp.getPkValue())) {
                return transConflictList.get(i);
            }
        }

        // if already added in conflict item then
        for (int i = fullConflictList.size()-1; i >= 0; i--) {
            if (fullConflictList.get(i).originalSyncOp.getPkValue().equalsIgnoreCase(remoteSyncOp.getPkValue())) {
                return fullConflictList.get(i);
            }
        }
        return null;
    }

    /**
     *
     * @param fullConflictList
     * @param transConflictList
     * @param conflictSyncOp
     */
    private void removeConflict(List<SyncConflict> fullConflictList,List<SyncConflict> transConflictList,SyncOp conflictSyncOp) {
        boolean found = false;
        // if already added in conflict item then
        for (int i = transConflictList.size()-1; i >= 0; i--) {
            if (transConflictList.get(i).originalSyncOp.getPkValue().equalsIgnoreCase(conflictSyncOp.getPkValue())) {
                transConflictList.remove(i);
                found = true;
                break;
            }
        }

        if (found ){
            // if already added in conflict item then
            for (int i = fullConflictList.size()-1; i >= 0; i--) {
                if (fullConflictList.get(i).originalSyncOp.getPkValue().equalsIgnoreCase(conflictSyncOp.getPkValue())) {
                    fullConflictList.remove(i);
                    break;
                }
            }
        }

        //return sqlSupport.getSyncConflictItemRows(conflictSyncOp.tableName, pkColumnName: conflictSyncOp.pkName, pkValue: conflictSyncOp.pkValue, deviceId: clientDeviceId)
    }
    /// Method will update the client row from from server row.
    private void updateFromServerConfilctRow(SyncOp remoteOp, String serverId ) {
        sqlSupport.executeUpdateQuery(remoteOp);
    }
}
