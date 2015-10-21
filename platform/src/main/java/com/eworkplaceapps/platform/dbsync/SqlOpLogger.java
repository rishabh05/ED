//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/28/2015
//===============================================================================
package com.eworkplaceapps.platform.dbsync;

import android.content.ContentValues;

import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.utils.Utils;

/**
 * Wrapper class for application calls for SQL ops.
 * It performs standard actions before/after a SQL op needed for syncing.
 */
public class SqlOpLogger {

    // Underlying DatabaseOps class for this class
    DatabaseOps db = DatabaseOps.defaultDatabase();

    /**
     * Wraps a BeginTransaction call
     */
    public void logBeginTransaction() {
        // Begin DB transaction
        db.beginTransaction();
        // Need to generate BEGIN_TRANS marker in SyncItemLog table.
        SyncItem syncItem = new SyncItem();
        // ALL values default except the following.
        syncItem.setDeviceId(Utils.emptyUUID().toString());
        syncItem.setOpType(SyncItem.SyncOpType.BEGIN_TRANS);
        // Write it in the table
        insertSyncItem(syncItem);
    }

    /**
     * Wraps a commit transaction.
     */
    public void logCommitTransaction() {
        // Commit DB transaction
        db.commitTransaction();
        // Need to generate BEGIN_TRANS marker in SyncItemLog table.
        SyncItem syncItem = new SyncItem();
        // ALL values default except the following.
        syncItem.setDeviceId(Utils.emptyUUID().toString());
        syncItem.setOpType(SyncItem.SyncOpType.END_TRANS);
        // Write it in the table
        insertSyncItem(syncItem);
    }

    /**
     * Inserts a SyncItem in to SyncItemLog table
     *
     * @param syncItem
     */
    private void insertSyncItem(SyncItem syncItem) {
        ContentValues values = new ContentValues();
        values.put("DeviceId", syncItem.getDeviceId());
        values.put("TableName", syncItem.getTableName());
        values.put("PKName", syncItem.getPkName());
        values.put("PKValue", syncItem.getPkValue());
        values.put("OpType", syncItem.getOpType());
        values.put("ColName", syncItem.getColName());
        values.put("ColValue", syncItem.getColValue());
        db.insertEntity("SyncItemLog", values);
    }
}
