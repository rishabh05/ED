//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/26/2015
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is a data class for sql operations recorded in SyncItem. This class is an
 * aggregate of SyncItem records that constitute a single SQL operation. The aggregate
 * is the list of all column names with values that are set by the INSERT/UPDATE
 * SQL operation.
 * <p/>
 * Every SQL statement that updates data in the database is recorded in this
 * table. This table is a log of such updates and is the foundation of sync operations.
 * An INSERT or UPDATE statement may set values in multiple columns. A separate SyncItem
 * record is generated for each column name and value pair.
 * As described below SyncOp records are tagged with IDs to group them at higher
 * levels.
 */
public class SyncOp {
    // The id of the system on which this SQL op was! executed.
    private String deviceId = "";
    // SQL table and PK data to access/create the row.
    private String tableName = "";
    private String pkName = "";
    private String pkValue = "";
    // Op type
    private String opType = SyncItem.SyncOpType.INSERT;
    // A dictionary of all column names and values which are set in the operation.
    private Map<String, String> columnValues = new HashMap<String, String>();
    // Refer the corresponding SyncItem property
    private String syncTransactionId = "";
    // DATE/Time when this operatioon was! executed
    private Date createdTime = new Date();

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public String getPkValue() {
        return pkValue;
    }

    public void setPkValue(String pkValue) {
        this.pkValue = pkValue;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public Map<String, String> getColumnValues() {
        return columnValues;
    }

    public void setColumnValues(Map<String, String> columnValues) {
        this.columnValues = columnValues;
    }

    public String getSyncTransactionId() {
        return syncTransactionId;
    }

    public void setSyncTransactionId(String syncTransactionId) {
        this.syncTransactionId = syncTransactionId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * Adds a SyncItem record to the column name/value pair dictionary.
     *
     * @param syncItem
     */
    public void addSyncItem(SyncItem syncItem) {
        // If this is the first SyncItem being added, initialize fixed fields.
        if (this.columnValues.isEmpty()) {
            // Init fields.
            this.deviceId = syncItem.getDeviceId();
            this.tableName = syncItem.getTableName();
            this.pkName = syncItem.getPkName();
            this.pkValue = syncItem.getPkValue();
            this.opType = syncItem.getOpType();
            this.syncTransactionId = syncItem.getSyncTransactionId();
        }
        // ADD the column name/value pair to the dictionary.
        this.columnValues.put(syncItem.getColName(), syncItem.getColValue());
        // Updated to the last SyncItem entry in an op.
        this.createdTime = syncItem.getCreatedTime();
    }

      /* XML:
    * <SyncOp>
    *   <DeviceId></DeviceId>
    *   <TableName></TableName>
    *   <pkName></pkName>
    *   <pkValue></pkValue>
    *   <opType></opType>
    *   <syncTransactionId></syncTransactionId>
    *   <createdTime></createdTime>
    *   <ColumnValues>
    *     <ColumnValue Name="" Value=""></ColumnValue>
    *   </ColumnValues>
    * </SyncOp>
    */

    /**
     * creates xml Object for sync item
     *
     * @param syncOp
     * @return
     * @throws java.io.IOException
     */
    public static void toXmlWriter(XmlSerializer xmlSerializer, SyncOp syncOp) throws IOException {
        xmlSerializer.startTag("", "SyncOp");
        xmlSerializer.startTag("", "DeviceId");
        xmlSerializer.text(syncOp.getDeviceId());
        xmlSerializer.endTag("", "DeviceId");
        xmlSerializer.startTag("", "TableName");
        xmlSerializer.text(syncOp.getTableName());
        xmlSerializer.endTag("", "TableName");
        xmlSerializer.startTag("", "PKName");
        xmlSerializer.text(syncOp.getPkName());
        xmlSerializer.endTag("", "PKName");
        xmlSerializer.startTag("", "PKValue");
        xmlSerializer.text(syncOp.getPkValue());
        xmlSerializer.endTag("", "PKValue");
        xmlSerializer.startTag("", "OpType");
        xmlSerializer.text(syncOp.getOpType());
        xmlSerializer.endTag("", "OpType");
        xmlSerializer.startTag("", "CreatedTime");
        String s = "";
        if (syncOp.getCreatedTime() != null) {
            s = Utils.dateAsStringWithoutUTC(syncOp.getCreatedTime());
        }
        xmlSerializer.text(s);
        xmlSerializer.endTag("", "CreatedTime");
        xmlSerializer.startTag("", "SyncTransactionId");
        xmlSerializer.text(syncOp.getSyncTransactionId());
        xmlSerializer.endTag("", "SyncTransactionId");

        Map<String, String> map = syncOp.getColumnValues();
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        xmlSerializer.startTag("", "ColumnValues");
        while (iterator.hasNext()) {
            String key = iterator.next();
            xmlSerializer.startTag("", "ColumnValue");
            xmlSerializer.attribute("", "Name", key);
            if (map.get(key) == null || map.get(key).isEmpty() || "null".equalsIgnoreCase(map.get(key)))
                xmlSerializer.attribute("", "Value", "~NULL~");
            else{
                xmlSerializer.attribute("", "Value", map.get(key));
            }
            xmlSerializer.endTag("", "ColumnValue");
        }
        xmlSerializer.endTag("", "ColumnValues");
        xmlSerializer.endTag("", "SyncOp");
    }

    /**
     * sync op list xml
     *
     * @param syncOpList
     * @param xmlSerializer
     * @return
     * @throws IOException
     */
    public static void listToXmlWriter(List<SyncOp> syncOpList, XmlSerializer xmlSerializer) throws EwpException, IOException {
        xmlSerializer.startTag("", "SyncOpList");
        for (SyncOp syncOp : syncOpList) {
            toXmlWriter(xmlSerializer, syncOp);
        }
        xmlSerializer.endTag("", "SyncOpList");
    }

    /**
     * parse to get SyncOp list
     *
     * @param object
     * @return
     * @throws EwpException
     */
    public static List<SyncOp> parseXML(XmlPullParser object) throws EwpException {
        try {
            List<SyncOp> syncOpList = new ArrayList<SyncOp>();
            int event = object.getEventType();
            String text = null;
            SyncOp syncOp = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = object.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if ("SyncOp".equals(name)) {
                            syncOp = new SyncOp();
                            syncOpList.add(syncOp);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = object.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if ("DeviceId".equals(name)) {
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
                            syncOp.setCreatedTime(Utils.dateFromString(text, true, true));
                        }
                        break;
                    default:
                        break;
                }
                event = object.next();
            }
            return syncOpList;
        } catch (XmlPullParserException e) {
            EwpException ex = new EwpException(e);
            throw ex;
        } catch (IOException e) {
            EwpException ex = new EwpException(e);
            throw ex;
        }
    }
}
