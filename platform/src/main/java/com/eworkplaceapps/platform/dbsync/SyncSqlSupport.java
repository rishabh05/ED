//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/28/2015
//===============================================================================
package com.eworkplaceapps.platform.dbsync;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.SqlUtils;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This is support class for SQL operation. ALL SQL operations required by other
 * classes are implemented here.
 */
public class SyncSqlSupport {
    // Underlying DatabaseOps class for this class
    private DatabaseOps db = DatabaseOps.defaultDatabase();

    /**
     * Execute SELECT query and return ResultSet
     *
     * @param sql
     * @return
     * @throws EwpException
     */
    private Cursor executeQuery(String sql) throws EwpException {
        Cursor resultSet = db.executeQuery(sql, null);
        if (resultSet == null) {
            throw new EwpException(new EwpException("Database ERROR"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, null, EnumsForExceptions.ErrorModule.SYNC, null, 0);
        } else {
            return resultSet;
        }
    }

    /**
     * Execute non-quesr SQL statements (INSERT/UPDATE/DELETE) and return the number
     * of records updated (which is mainly used for error detection.)
     *
     * @param sql
     * @return
     */
    public int executeNonQuery(String sql) {
        db.executeStatements(sql);
        return db.changes();
    }

    /**
     * Execute non-quesr SQL statements (INSERT/UPDATE/DELETE) and return the number
     * of records updated (which is mainly used for error detection.)
     *
     * @param syncOp
     * @return
     */
    public int executeInsertQuery(SyncOp syncOp) {
        ContentValues contentValues = generateContentValuesForInsert(syncOp.getPkName(), syncOp.getPkValue(), syncOp.getColumnValues());
        return (int) db.insertEntity(syncOp.getTableName(), contentValues);
    }

    /**
     * @param syncOp
     * @return
     */
    public int executeUpdateQuery(SyncOp syncOp) {
        ContentValues values = generateContentValuesForUpdate(syncOp.getPkName(), syncOp.getColumnValues());
        int rowsAffected = db.update(syncOp.getTableName(), values, syncOp.getPkName() + "=?", new String[]{syncOp.getPkValue()});
        return rowsAffected;
    }

    /**
     * @param syncOp
     * @return
     */
    public int executeDeleteQuery(SyncOp syncOp) {
        int rowsDeleted = db.deleteStatement(syncOp.getTableName(), syncOp.getPkName() + "=?", new String[]{syncOp.getPkValue()});
        return rowsDeleted;
    }


    /**
     * Begin transaction
     *
     * @return
     */
    public void beginTransaction() {
        db.beginTransaction();
    }

    /**
     * Commit transaction
     */
    public void commitTransaction() {
        db.commitTransaction();
    }

    /**
     * Rollback transaction
     *
     * @return
     */
    public void rollbackTransaction() {
        db.getWritableDatabase().endTransaction();
    }


    /**
     * GET the list of SyncItems matching the filtering criteria, ordered by sequence
     * in which they were entered.
     *
     * @param includeDeviceId
     * @param afterSyncTime
     * @param toSyncTime
     * @return
     */
    public List<SyncItem> getSyncItemList(String includeDeviceId, Date afterSyncTime, Date toSyncTime) throws EwpException {
        String s;
        String whereClause = "";
        if (includeDeviceId != null && !"".equals(includeDeviceId)) {
            if (whereClause.isEmpty()) {
                whereClause = "WHERE";
            }
            s = includeDeviceId;
            whereClause += " (LOWER(DeviceId) == LOWER('" + s + "'))";
        }
        if (afterSyncTime != null) {
            if (whereClause.isEmpty()) {
                whereClause = "WHERE";
            } else {
                whereClause += " AND ";
            }
            s = Utils.dateAsStringWithoutUTC(afterSyncTime);
            whereClause += " (createdTime > '" + s + "')";
        }
        // toSyncTime Where clause
        if (toSyncTime != null) {
            if (whereClause.isEmpty()) {
                whereClause = "WHERE";
            } else {
                whereClause += " AND ";
            }
            s = Utils.getUTCDateTimeAsString(toSyncTime);
            whereClause += " (createdTime <= '" + s + "')";
        }
        // Query
        String sql = "SELECT * From " + getSyncItemLogTableName() + " " + whereClause + " ORDER BY RowId ASC";
        Cursor resultSet = executeQuery(sql);
        // Loop and create SyncItem array
        List<SyncItem> list = new ArrayList<SyncItem>();
        if (resultSet != null) {
            // Loop through the item and create SyncItem and set properties.
            while (resultSet.moveToNext()) {
                SyncItem item = new SyncItem();
                item.setPropertiesFromResultSet(resultSet);
                list.add(item);
            }
            resultSet.close();
        } else {
            Log.e(this.getClass().getName(), "");
        }
        return list;
    }


    /**
     * GET ModifiedDate value for a give data record. Modified date is a standard
     * column in all data tables.
     *
     * @param tableName
     * @param pkName
     * @param pkValue
     * @return
     */
    public Date getModifiedDateForRecord(String tableName, String pkName, String pkValue) {

        // Generate query and execute it.
        String s = pkValue;
        String sql = "SELECT ModifiedDate FROM " + tableName + " WHERE LOWER(" + pkName + ") = LOWER('" + s + "')";
        Cursor resultSet = db.executeQuery(sql, null);
        // If ModifiedDate columns exists, get its value
        boolean existsInTable = true;
        Date date = null;
        if (resultSet != null) {
            if (resultSet.moveToNext()) {
                String data = resultSet.getString(resultSet.getColumnIndex("ModifiedDate"));
                Date date1 = Utils.dateFromString(data, true, true);
                if (date1 != null) {
                    date = date1;
                }
            }
            resultSet.close();
        } else {
            existsInTable = false;
        }
        return date;
    }

    /**
     * Checks if a data record exists
     *
     * @param tableName
     * @param pkName
     * @param pkValue
     * @return
     * @throws EwpException
     */
    public boolean recordExists(String tableName, String pkName, String pkValue) throws EwpException {
        // Generate query and execute it.
        String s = pkValue;
        String sql = "SELECT * FROM " + tableName + " WHERE LOWER(" + pkName + ") = LOWER('" + s + "')";
        return SqlUtils.recordExists(sql, db);
    }


    /**
     * @param pkName
     * @param pkValue
     * @param columnValues
     * @return
     */
    public ContentValues generateContentValuesForInsert(String pkName, String pkValue, Map<String, String> columnValues) {
        ContentValues values = new ContentValues();
        values.put(pkName, pkValue);
        Set<String> set = columnValues.keySet();
        Iterator<String> iterator = set.iterator();
        String textValue, key;
        while (iterator.hasNext()) {
            key = iterator.next();
            textValue = columnValues.get(key);
            if ("~NULL~".equals(textValue)) {
                textValue = null;
            }
            values.put(key, textValue);
        }
        return values;
    }

    /**
     * @param columnValues
     * @return
     */
    public ContentValues generateContentValuesForUpdate(String pkName, Map<String, String> columnValues) {
        ContentValues values = new ContentValues();
        Set<String> set = columnValues.keySet();
        Iterator<String> iterator = set.iterator();
        String textValue, key;
        while (iterator.hasNext()) {
            key = iterator.next();
            textValue = columnValues.get(key);
            if ("~NULL~".equals(textValue)) {
                textValue = null;
            }
            // do not update primary key
            if (!key.equals(pkName)) {
                values.put(key, textValue);
            }
        }
        return values;
    }


    /**
     * Generates DELETE SQL
     *
     * @param op
     * @return
     */
    public String generateDELETEStatement(SyncOp op) {
        String sql = generateDELETEStatement(op.getTableName(), op.getPkName(), op.getPkValue());
        return sql;
    }

    /**
     * Generates DELETE SQL
     *
     * @param tableName
     * @param pkName
     * @param pkValue
     * @return
     */
    public String generateDELETEStatement(String tableName, String pkName, String pkValue) {
        // Set DELETE query and execute it.
        String s = pkValue;
        String sql = "DELETE FROM " + tableName + " WHERE LOWER(" + pkName + ")=LOWER('" + s + "')";
        return sql;
    }

    /**
     * Checks if SyncItem contains row for the given filter. Note that there may be more
     * than one row.
     *
     * @param opType
     * @param tableName
     * @param pkName
     * @param pkValue
     * @param deviceId
     * @return
     * @throws EwpException
     */
    public boolean syncItemExistsForOp(String opType, String tableName, String pkName, String pkValue, String deviceId) throws EwpException {
        String s = "";
        // Build WHERE clause
        s = pkValue;
        String ws = "LOWER(OpType)=LOWER('" + opType + "') AND LOWER(TableName)=LOWER('" + tableName + "') AND LOWER(PKName)=LOWER('" + pkName + "') AND LOWER(PKValue)=LOWER('" + s + "')";
        if (deviceId != null) {
            s = deviceId;
            // To check duplicacy of a item. We always insert server or local device id in sync log so we do not required
            /// to check deviceid. It was a bug Amit.
//            ws = "LOWER(DeviceId)=LOWER('" + s + "') AND " + ws;
        }
        String sql = "SELECT * FROM " + getSyncItemLogTableName() +" " + "WHERE " + ws;
        boolean b = SqlUtils.recordExists(sql);
        return b;
    }

    /**
     * Gets device Id for the last SyncItem record matching the given filter properties.
     *
     * @param opType
     * @param tableName
     * @param pkName
     * @param pkValue
     * @return
     * @throws EwpException
     */
    public String syncItemDeviceIdForOp(String opType, String tableName, String pkName, String pkValue) throws EwpException {
        String s = "";
        // Build query
        s = pkValue;
        String ws = "LOWER(OpType)=LOWER('" + opType + "') AND LOWER(TableName)=LOWER('" + tableName + "') AND LOWER(PKName)=LOWER('" + pkName + "') AND LOWER(PKValue)=LOWER('" + s + "')";
        String sql = "SELECT * FROM "+ getSyncItemLogTableName() + " " + " WHERE " + ws + " ORDER BY RowId ASC";
        // Execute query
        Cursor resultSet = executeQuery(sql);
        // Loop and create SyncItem array
        // Keep updating deviceId until the end to get the last value
        String deviceId = null;
        if (resultSet != null) {
            while (resultSet.moveToNext()) {
                deviceId = resultSet.getString(resultSet.getColumnIndex("DeviceId"));
            }
            resultSet.close();
        }
        return deviceId;
    }

    /**
     * Gets SyncTime for a given DeviceId row from SyncTimeLog
     *
     * @param deviceId
     * @param receive
     * @return
     */
    public Date getLastSyncTime(String deviceId, boolean receive) {
        // Build query
        String s = deviceId;
        String sql = "SELECT * FROM SyncTimeLog WHERE LOWER(DeviceId) = LOWER('" + s + "')";
        Cursor resultSet = db.executeQuery(sql, null);
        // GET LastSyncTime from the first and only row
        Date date = null;
        if (resultSet != null && resultSet.moveToFirst()) {
            // Use receive or send time column.
            String cName = (receive) ? "ReceiveSyncTime" : "SendSyncTime";
            date = Utils.dateFromString(resultSet.getString(resultSet.getColumnIndex(cName)), true, true);
            resultSet.close();
        }
        return date;
    }


    /**
     * Updates the LastSyncTime column for the given DeviceId record in SyncItem
     *
     * @param receive
     */
    public boolean updateSyncTime(String deviceId, Date syncTime, boolean receive) throws EwpException {
        String sql = "";

        // Escape data field values
        String s1 = deviceId;
        String s2 = "";
       /* if (receive) {
            s2 = Utils.dateAsStringWithoutUTC(syncTime);
        } else {
            s2 = Utils.dateAsStringWithoutUTC(syncTime);
        }*/
        s2 = Utils.dateAsStringWithoutUTC(syncTime);
        // Check if it exists
        sql = "SELECT * FROM " + getSyncTimeLogTableName() + " WHERE LOWER(DeviceId) = LOWER('" + s1 + "')";
        boolean response = SqlUtils.recordExists(sql, db);
        boolean isSuccess = true;
        // If the record exists, update LastSyncTime
        if (response) {
            // Use receive or send time column.
            String cName = (receive) ? "ReceiveSyncTime" : "SendSyncTime";
            sql = "UPDATE " + getSyncTimeLogTableName() + " SET " + cName + " = '" + s2 + "' WHERE LOWER(DeviceId) = LOWER('" + s1 + "')";
            isSuccess = db.executeStatements(sql);
        } else {
            UUID s3 = UUID.randomUUID();
            if (receive) {
                sql = "INSERT INTO "+ getSyncTimeLogTableName()+ " (RowId, DeviceId, ReceiveSyncTime, SendSyncTime) VALUES ('" + s3.toString() + "', '" + s1 + "', '" + s2 + "', '0')";
            } else {
                sql = "INSERT INTO "+ getSyncTimeLogTableName()+ " (RowId, DeviceId, ReceiveSyncTime, SendSyncTime) VALUES ('" + s3 + "', '" + s1 + "', '0', '" + s2 + "')";
            }
            isSuccess = db.executeStatements(sql);
        }
        return isSuccess;
    }


    /**
     * Gets DevicedId from SyncTriggerData
     *
     * @return
     */
    public String getTriggerDeviceId() {
        // Build query
        String sql = "SELECT * FROM SyncTriggerData WHERE PK = 'IdData'";
        Cursor resultSet = db.executeQuery(sql, null);
        // GET DeviceId from the first and only row
        String s = "";
        if (resultSet != null && resultSet.moveToNext()) {
            s = resultSet.getString(resultSet.getColumnIndex("DeviceId"));
            resultSet.close();
        }
        return s;
    }

    /**
     * Updates DeviceId column for the current device id.
     *
     * @param deviceId
     */
    public boolean setTriggerDeviceId(String deviceId) {
        // Assumption: The row PK=IdData always exists in the table.
        String sql = "UPDATE SyncTriggerData SET DeviceId = LOWER('" + deviceId + "') WHERE PK = 'IdData'";
        return db.executeStatements(sql);
    }


    public String getSyncTimeLogTableName() {
        return "SyncTimeLog";
    }


    public String getSyncItemLogTableName() {
        return "SyncItemLog";
    }

    /**
     *
     * @param remoteSyncOp
     * @param deviceId
     * @return
     * @throws EwpException
     */
    public SyncOp getLatestLocalSyncOp(SyncOp remoteSyncOp,String deviceId) throws EwpException {
        // Query
        String whereClause = " WHERE LOWER(" + remoteSyncOp.getPkName() + ") = LOWER('" + remoteSyncOp.getPkValue() + "')";
        String  sql =  "SELECT * FROM " + remoteSyncOp.getTableName()  +(whereClause);

        Cursor resultSet = db.executeQuery(sql,null);
        // Loop and create SyncItem array
            // Loop through the item and create SyncItem and set properties.
        if (resultSet != null) {
                while (resultSet.moveToNext()) {
                    return setAndGetColumnValue(resultSet, remoteSyncOp,deviceId);
                }
            }

        return null;
    }

    /**
     *
     * @param resultSet
     * @param remoteSyncOp
     * @param deviceId
     * @return
     */
    private SyncOp setAndGetColumnValue(Cursor resultSet,SyncOp remoteSyncOp,String deviceId ) {
        int colCount = resultSet.getColumnCount();
        String colName;
        SyncItem item;
        SyncOp syncOp = new SyncOp();
        syncOp.setSyncTransactionId(remoteSyncOp.getSyncTransactionId());
        // Loop and create SyncItem array
        ArrayList<SyncItem> list = new ArrayList<SyncItem> ();

        for (int i = 0; i < colCount; i++) {
            colName = resultSet.getColumnName(i);
            if("version".equals(colName.toLowerCase()) || "WorkingHours".toLowerCase().equals(colName.toLowerCase())){
                continue;
            }
            item = new SyncItem();
            item.setDeviceId(deviceId);
            item.setTableName(remoteSyncOp.getTableName());
            item.setPkName(remoteSyncOp.getPkName());
            item.setPkValue(remoteSyncOp.getPkValue());
            item.setSyncTransactionId(remoteSyncOp.getSyncTransactionId());
            item.setColName(colName);
            String value = resultSet.getString(resultSet.getColumnIndex(colName));
            if(value == null){
                value = "" ;
            }
            item.setOpType(remoteSyncOp.getOpType());
            item.setColValue(value);
                    //list.append(item)
            syncOp.addSyncItem(item);
        }

        return syncOp;
    }
}
