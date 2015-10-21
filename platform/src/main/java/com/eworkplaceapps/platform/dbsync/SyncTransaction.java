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
 * This is a data class for sql operations in one transaction.
 * A SyncTransaction item may consist of many SyncOp items, which are
 * sequenced in an array.
 */
public class SyncTransaction {

    // The id of the system on which this SQL transaction was! executed.
    private String deviceId = "";

    // Refer the corresponding SyncItem property
    private String syncTransactionId = "";

    // Refer the corresponding SyncItem property
    private int syncRowNumber = 0;

    // List of all SyncOp records that constitute a single SQL transaction.
    private List<SyncOp> syncOpList = new ArrayList<SyncOp>();

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSyncTransactionId() {
        return syncTransactionId;
    }

    public void setSyncTransactionId(String syncTransactionId) {
        this.syncTransactionId = syncTransactionId;
    }

    public int getSyncRowNumber() {
        return syncRowNumber;
    }

    public void setSyncRowNumber(int syncRowNumber) {
        this.syncRowNumber = syncRowNumber;
    }

    public List<SyncOp> getSyncOpList() {
        return syncOpList;
    }

    public void setSyncOpList(List<SyncOp> syncOpList) {
        this.syncOpList = syncOpList;
    }

    /**
     * Adds a SyncOp record to the list of constituent SyncOp records.
     *
     * @param syncOp
     */
    public void addSyncOp(SyncOp syncOp) {
        // If this is the first SyncItem being added, initialize fixed fields.
        if (this.syncOpList.isEmpty()) {
            // Init fields.
            this.deviceId = syncOp.getDeviceId();
            this.syncTransactionId = syncOp.getSyncTransactionId();
        }
        // ADD the column name/value pair to the dictionary.
        this.syncOpList.add(syncOp);
    }

    /**
     * Adds a list of SyncOp records to the list of constituent SyncOp records.
     *
     * @return
     */
    public void addSyncOp(List<SyncOp> opList) {
        // No-op if the input list is empty.
        if (opList.isEmpty()) {
            return;
        }
        // If this is the first SyncItem being added, initialize fixed fields.
        if (this.syncOpList.isEmpty()) {
            // Init fields from the first SyncOp item.
            this.deviceId = opList.get(0).getDeviceId();
            this.syncTransactionId = opList.get(0).getSyncTransactionId();
        }
        // ADD the column name/value pair to the dictionary.
        this.syncOpList.addAll(opList);
    }

        /*
      <SyncTransactionList>
        <SyncTransaction>
            <TransactionId></TransactionId>

            <SyncOpList>
                <SyncOp>
                    <DeviceId></DeviceId>
                    <TableName></TableName>
                    <PKName></PKName>
                    <PKValue></PKValue>
                    <OpType></OpType>
                    <Timestamp></Timestamp>
                    <ColumnValues>
                        <ColumnValue Name=""  Value=""></ColumnValue>
                    </ColumnValues>
                </SyncOp>
            </SyncOpList>

        </SyncTransaction>
    </SyncTransactionList>
   */

    /**
     * SYNC transaction xml
     *
     * @param xmlSerializer
     * @param syncTransaction
     */
    public static void toXmlWriter(XmlSerializer xmlSerializer, SyncTransaction syncTransaction) throws IOException, EwpException {
        // Start Root tag
        xmlSerializer.startTag("", "SyncTransaction");
        xmlSerializer.startTag("", "SyncTransactionId");
        xmlSerializer.text(syncTransaction.syncTransactionId);
        xmlSerializer.endTag("", "SyncTransactionId");
        SyncOp.listToXmlWriter(syncTransaction.syncOpList, xmlSerializer);
        xmlSerializer.endTag("", "SyncTransaction");
    }

    /**
     * SyncTransaction list xml
     *
     * @param list
     * @param xmlSerializer
     * @throws IOException
     * @throws EwpException
     */
    public static void listToXmlWriter(List<SyncTransaction> list, XmlSerializer xmlSerializer) throws IOException, EwpException {
        xmlSerializer.startTag("", "SyncTransactionList");
        for (SyncTransaction transaction : list) {
            toXmlWriter(xmlSerializer, transaction);
        }
        xmlSerializer.endTag("", "SyncTransactionList");
    }

    /**
     * parse the multiple SyncTransaction objects
     *
     * @param object
     * @return
     * @throws EwpException
     */
    public static List<SyncTransaction> parseXML(XmlPullParser object) throws EwpException {
        try {
            List<SyncTransaction> syncTransactions = new ArrayList<SyncTransaction>();
            int event = object.getEventType();
            String text = null;
            SyncTransaction syncTransaction = null;
            SyncOp syncOp = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = object.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("SyncTransaction".equals(name)) {
                            syncTransaction = new SyncTransaction();
                            syncTransactions.add(syncTransaction);
                        }
                        if ("SyncOp".equals(name)) {
                            syncOp = new SyncOp();
                            syncTransaction.syncOpList.add(syncOp);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = object.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if ("SyncTransactionId".equals(name)) {
                            syncTransaction.syncTransactionId = text;
                        } else if ("RowNumber".equals(name)) {
                            syncTransaction.syncRowNumber = Integer.parseInt(text);
                        } else if ("DeviceId".equals(name)) {
                            syncOp.setDeviceId(text);
                        } else if ("TableName".equals(name)) {
                            syncOp.setTableName(text);
                        } else if ("PKName".equals(name)) {
                            syncOp.setPkName(text);
                        } else if ("OpType".equals(name)) {
                            syncOp.setOpType(text);
                        } else if ("ColumnValue".equals(name)) {
                            syncOp.getColumnValues().put(object.getAttributeValue(null, "Name"), object.getAttributeValue(null, "Value"));
                        } else if ("SyncTransactionId".equals(name)) {
                            syncOp.setSyncTransactionId(text);
                        } else if ("CreatedTime".equals(name)) {
                            syncOp.setCreatedTime(Utils.dateFromString(text,true,true));
                        }
                        break;
                    default:
                        break;
                }
                event = object.next();
            }
            return syncTransactions;
        } catch (XmlPullParserException e) {
            EwpException ex = new EwpException(e);
            throw ex;
        } catch (IOException e) {
            EwpException ex = new EwpException(e);
            throw ex;
        }
    }

}
