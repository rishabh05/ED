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
import java.util.Date;
import java.util.List;

/**
 * This is a data class for sync reply generated after processing a sync request.
 * The SyncReply will be processed by the request sender to update its
 * sync log, and to resolve any conflicts generated during syncing.
 */
public class SyncReply {

    // The request id sent in SyncRequest
    private String requestId = "";

    // The deviced id of the node that processed the syncing. (Receiver of SyncRequest)
    private String myDeviceId = "";

    private int lastSyncRowNumber = 0;

    // SyncRequest processing status and status message
    private int status = 0;
    private String message = "";

    // The SyncTime bounds within which the syncing was processed.
    // In case of error, this may be shorter than the bounds sent by the requester.
    // In case of no error, it should be the same as the requester's bounds.
    private Date afterSyncTime = new Date();
    private Date toSyncTime = new Date();

    // A flag to indicate if it was partial syncing. (In case of error.)
    private boolean partialSync = false;

    // The list of conflicts generated during syncing, in the order of timestamp.
    private List<SyncConflict> conflictList = new ArrayList<SyncConflict>();

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMyDeviceId() {
        return myDeviceId;
    }

    public void setMyDeviceId(String myDeviceId) {
        this.myDeviceId = myDeviceId;
    }

    public int getLastSyncRowNumber() {
        return lastSyncRowNumber;
    }

    public void setLastSyncRowNumber(int lastSyncRowNumber) {
        this.lastSyncRowNumber = lastSyncRowNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getAfterSyncTime() {
        return afterSyncTime;
    }

    public void setAfterSyncTime(Date afterSyncTime) {
        this.afterSyncTime = afterSyncTime;
    }

    public Date getToSyncTime() {
        return toSyncTime;
    }

    public void setToSyncTime(Date toSyncTime) {
        this.toSyncTime = toSyncTime;
    }

    public boolean isPartialSync() {
        return partialSync;
    }

    public void setPartialSync(boolean partialSync) {
        this.partialSync = partialSync;
    }

    public List<SyncConflict> getConflictList() {
        return conflictList;
    }

    public void setConflictList(List<SyncConflict> conflictList) {
        this.conflictList = conflictList;
    }

    /* XML:

    <RemoteSyncReply>
        <RequestId></RequestId>
        <MyDeviceId></MyDeviceId>
        <Status></Status>
        <Message></Message>
        <FromSyncTime></FromSyncTime>
        <ToSyncTime></ToSyncTime>
        <PartialSync></PartialSync>
        <SyncConflictList>
        ...
        </SyncConflictList>
    </RemoteSyncReply>
    */

    /**
     * SYNC Reply xml
     *
     * @param xmlSerializer
     * @param syncReply
     * @throws IOException
     */
    public static void toXmlWriter(XmlSerializer xmlSerializer, SyncReply syncReply) throws IOException {
        // Start Root tag
        xmlSerializer.startTag("", "RemoteSyncReply");
        xmlSerializer.startTag("", "RequestId");
        xmlSerializer.text(syncReply.getRequestId());
        xmlSerializer.endTag("", "RequestId");
        xmlSerializer.startTag("", "MyDeviceId");
        xmlSerializer.text(syncReply.getMyDeviceId());
        xmlSerializer.endTag("", "MyDeviceId");
        xmlSerializer.startTag("", "Status");
        xmlSerializer.text(syncReply.getStatus() + "");
        xmlSerializer.endTag("", "Status");
        xmlSerializer.startTag("", "Message");
        xmlSerializer.text(syncReply.getMessage());
        xmlSerializer.endTag("", "Message");
        xmlSerializer.startTag("", "AfterSyncTime");
        String s = Utils.dateAsStringWithoutUTC(syncReply.getAfterSyncTime());
        xmlSerializer.text(s);
        xmlSerializer.endTag("", "AfterSyncTime");
        xmlSerializer.startTag("", "ToSyncTime");
        String k = Utils.dateAsStringWithoutUTC(syncReply.getToSyncTime());
        xmlSerializer.text(k);
        xmlSerializer.endTag("", "ToSyncTime");
        xmlSerializer.startTag("", "PartialSync");
        xmlSerializer.text(syncReply.isPartialSync() + "");
        xmlSerializer.endTag("", "PartialSync");
        SyncConflict.listToXmlWriter(syncReply.conflictList, xmlSerializer);
        xmlSerializer.endTag("", "RemoteSyncReply");
    }

    public static SyncReply parseXml(XmlPullParser object) throws EwpException {
        try {

            int event = object.getEventType();
            String text = null;
            SyncReply syncReply = null;
            SyncConflict syncConflict = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = object.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("RemoteSyncReply".equals(name)) {
                            syncReply = new SyncReply();
                        }
                        if ("SyncConflict".equals(name)) {
                            syncConflict = new SyncConflict();
                            syncReply.getConflictList().add(syncConflict);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = object.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if ("RequestId".equals(name)) {
                            syncReply.setRequestId(text);
                        } else if ("MyDeviceId".equals(name)) {
                            syncReply.setMyDeviceId(text);
                        } else if ("Status".equals(name)) {
                            syncReply.setStatus(Integer.parseInt(text));
                        } else if ("Message".equals(name)) {
                            syncReply.setMessage(text);
                        } else if ("AfterSyncTime".equals(name)) {
                            syncReply.setAfterSyncTime(Utils.dateFromString(text, true, true));
                        } else if ("ToSyncTime".equals(name)) {
                            syncReply.setToSyncTime(Utils.dateFromString(text, true, true));
                        } else if ("PartialSync".equals(name)) {
                            syncReply.setPartialSync("true".equals(text) || "True".equals(text));
                        } else if ("ConflictType".equals(name)) {
                            syncConflict.conflictType = SyncConflict.SyncConflictType.toSyncConflictType(text);
                        } else if ("DeviceId".equals(name)) {
                            syncConflict.originalSyncOp.setDeviceId(text);
                        } else if ("TableName".equals(name)) {
                            syncConflict.originalSyncOp.setTableName(text);
                        } else if ("PKName".equals(name)) {
                            syncConflict.originalSyncOp.setPkName(text);
                        }else if ("PKValue".equals(name)) {
                            syncConflict.originalSyncOp.setPkValue(text);
                        }
                        else if ("OpType".equals(name)) {
                            syncConflict.originalSyncOp.setOpType(text);
                        } else if ("ColumnValue".equals(name)) {
                            syncConflict.originalSyncOp.getColumnValues().put(object.getAttributeValue(null, "Name"), object.getAttributeValue(null, "Value"));
                        } else if ("SyncTransactionId".equals(name)) {
                            syncConflict.originalSyncOp.setSyncTransactionId(text);
                        } else if ("CreatedTime".equals(name)) {
                            syncConflict.originalSyncOp.setCreatedTime(Utils.dateFromString(text, true, true));
                        }
                        break;
                    default:
                        break;
                }
                event = object.next();
            }
            return syncReply;
        } catch (XmlPullParserException e) {
            EwpException ex = new EwpException(e);
            throw ex;
        } catch (IOException e) {
            EwpException ex = new EwpException(e);
            throw ex;
        }
    }
}
