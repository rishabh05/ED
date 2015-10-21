//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/28/2015
//===============================================================================
package com.eworkplaceapps.platform.dbsync;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a data class for sync conflict information.
 * It is generated on the local device during syncing, and processed by
 * the remote with the reply sent from local to remote.
 */
public class SyncConflict {

    public enum SyncConflictType {
        DUPLICATE_INSERT(0),      // A duplicate found during INSERT
        UPDATE_UPDATED(1),          // A record update was! updated earlier by another device
        UPDATE_DELETED(2),          // A record update for a deleted record
        UPDATE_NEVER_INSERTED(3),    // A record update that was! never created
        DELETE_UPDATED(4);          // A record deletion that was! updated by another device
        private int id;

        SyncConflictType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        /**
         * SyncConflictType to STRING
         *
         * @return
         */
        public String toString() {
            switch (this) {
                case DUPLICATE_INSERT:
                    return "DuplicateInsert";
                case UPDATE_UPDATED:
                    return "UpdateUpdated";
                case UPDATE_DELETED:
                    return "UpdateDeleted";
                case UPDATE_NEVER_INSERTED:
                    return "UpdateNeverInserted";
                case DELETE_UPDATED:
                    return "DeleteUpdated";
                default:
                    return "";
            }
        }

        /**
         * STRING to SyncConflictType
         *
         * @param strSyncConflictType
         * @return
         */
        public static SyncConflictType toSyncConflictType(String strSyncConflictType) {
            switch (strSyncConflictType) {
                case "DuplicateInsert":
                    return SyncConflictType.DUPLICATE_INSERT;
                case "UpdateUpdated":
                    return SyncConflictType.UPDATE_UPDATED;
                case "UpdateDeleted":
                    return SyncConflictType.UPDATE_DELETED;
                case "DeleteUpdated":
                    return SyncConflictType.DELETE_UPDATED;
                default:
                    return SyncConflictType.DUPLICATE_INSERT;
            }
        }
    }

    // Conflict type
    SyncConflictType conflictType = SyncConflictType.DUPLICATE_INSERT;

    // The original sync op record from remote that generated a conflict on the local
    // device. This information will be used by the remote to resolve the conflict.
    // Note that requester's Sql Transaction information is not maintained here.
    static SyncOp originalSyncOp = new SyncOp();

    public SyncConflict() {

    }

    public SyncConflict(SyncConflictType conflictType, SyncOp op) {
        this.conflictType = conflictType;
        this.originalSyncOp = op;
    }

    // -------------------- XML --------------------
    /* XML:
    * <SyncConflict>
    *   <ConflictType></ConflictType>
    *   <SyncOp>
    *       <DeviceId></DeviceId>
    *       <TableName></TableName>
    *       <pkName></pkName>
    *       <pkValue></pkValue>
    *       <opType></opType>
    *       <syncTransactionId></syncTransactionId>
    *       <timestamp></timestamp>
    *
    *       <ColumnValues>
    *           <ColumnValue Name="" Value=""></ColumnValue>
    *       </ColumnValues>
    *
    *   </SyncOp>
    *
    * </SyncConflict>
    */

    /**
     * @param xmlSerializer
     * @param syncConflict
     */
    public static void toXmlWriter(XmlSerializer xmlSerializer, SyncConflict syncConflict) throws IOException {
        xmlSerializer.startTag("", "SyncConflict");
        xmlSerializer.startTag("", "ConflictType");
        xmlSerializer.text(syncConflict.conflictType.toString());
        xmlSerializer.endTag("", "ConflictType");
        SyncOp.toXmlWriter(xmlSerializer, originalSyncOp);
        xmlSerializer.endTag("", "SyncConflict");
    }

    public static void listToXmlWriter(List<SyncConflict> syncConflictList, XmlSerializer xmlSerializer) throws IOException {
        xmlSerializer.startTag("", "SyncConflictList");

        for (SyncConflict syncConflict : syncConflictList) {
            toXmlWriter(xmlSerializer, syncConflict);
        }
        xmlSerializer.endTag("", "SyncConflictList");
    }

    /**
     * parse to sync conflict list
     *
     * @param object
     * @return
     * @throws EwpException
     */
    public static List<SyncConflict> parseXML(XmlPullParser object) throws EwpException {
        try {
            List<SyncConflict> syncConflictList = new ArrayList<SyncConflict>();
            int event = object.getEventType();
            String text = null;
            SyncConflict syncConflict = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = object.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("SyncConflict".equals(name)) {
                            syncConflict = new SyncConflict();
                            syncConflictList.add(syncConflict);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = object.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if ("DeviceId".equals(name)) {
                            syncConflict.conflictType = SyncConflictType.toSyncConflictType(text);
                        } else if ("DeviceId".equals(name)) {
                            syncConflict.originalSyncOp.setDeviceId(text);
                        } else if ("TableName".equals(name)) {
                            syncConflict.originalSyncOp.setTableName(text);
                        } else if ("PKName".equals(name)) {
                            syncConflict.originalSyncOp.setPkName(text);
                        } else if ("OpType".equals(name)) {
                            syncConflict.originalSyncOp.setOpType(text);
                        } else if ("ColumnValue".equals(name)) {
                            syncConflict.originalSyncOp.getColumnValues().put(object.getAttributeValue(null, "Name"), object.getAttributeValue(null, "Value"));
                        } else if ("SyncTransactionId".equals(name)) {
                            syncConflict.originalSyncOp.setSyncTransactionId(text);
                        } else if ("CreatedTime".equals(name)) {
                            syncConflict.originalSyncOp.setCreatedTime(Utils.dateFromString(text,true,true));
                        }
                        break;
                    default:
                        break;
                }
                event = object.next();
            }
            return syncConflictList;
        } catch (XmlPullParserException e) {
            EwpException ex = new EwpException(e);
            throw ex;
        } catch (IOException e) {
            EwpException ex = new EwpException(e);
            throw ex;
        }
    }


}
