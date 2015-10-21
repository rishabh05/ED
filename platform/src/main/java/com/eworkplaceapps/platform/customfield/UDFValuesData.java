//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.customfield;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.List;
import java.util.UUID;

/**
 * It contain user define field value and text with respact to each user defined field (customfield).
 */
public class UDFValuesData extends BaseData<UDFValues> {

    @Override
    public UDFValues createEntity() {
        return UDFValues.createEntity();
    }

    /**
     * GET an entity that matched the id
     *
     * @param id Object
     * @return UDF_VALUES
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public UDFValues getEntity(Object id) throws EwpException {
        // Building select statement
        String sql = "SELECT * From PFUDFValues where EntityId='" + id.toString() + "'";
        // Executes select statement and return ROLE entity
        return executeSqlAndGetEntity(sql);
    }

    /**
     * GET all the UDF_VALUES Entity from database.
     * Return Collection of UDF_VALUES Entity.
     *
     * @return List<UDF_VALUES>
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public List<UDFValues> getList() throws EwpException {
        String mySql = "SELECT * From PFUDFValues";
        return executeSqlAndGetEntityList(mySql);
    }

    /**
     * GET UDF_VALUES Entity that matches the id and return result as! a ResultSet.
     *
     * @param id Object
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        String sql = "SELECT * From PFUDFValues where EntityId='" + id.toString() + "'";
        return executeSqlAndGetResultSet(sql);
    }

    @Override
    public Cursor getListAsResultSet() throws EwpException {
        String sql = "SELECT * From PFUDFValues";
        return super.executeSqlAndGetResultSet(sql);
    }

    @Override
    public void delete(UDFValues entity) throws EwpException {
        super.deleteRows("PFUDFValues", "EntityId=?", new String[]{entity.getEntityId().toString()});
    }

    @Override
    public long insertEntity(UDFValues entity) throws EwpException {
        ContentValues values = new ContentValues();
        entity.setEntityId(UUID.randomUUID());
        values.put("EntityId", entity.getEntityId().toString());
        values.put("UDFString1", entity.getUdfString1());
        values.put("UDFString2", entity.getUdfString2());
        values.put("UDFString3", entity.getUdfString3());
        values.put("UDFString4", entity.getUdfString4());
        values.put("UDFString5", entity.getUdfString5());
        values.put("UDFString6", entity.getUdfString6());
        values.put("UDFString7", entity.getUdfString7());
        values.put("UDFString8", entity.getUdfString8());
        values.put("UDFString9", entity.getUdfString9());
        values.put("UDFString10", entity.getUdfString10());
        values.put("UDFPickList1", entity.getUdfPickList1().toString());
        values.put("UDFPickList2", entity.getUdfPickList2().toString());
        values.put("UDFPickList3", entity.getUdfPickList3().toString());
        values.put("UDFPickList4", entity.getUdfPickList4().toString());
        values.put("UDFPickList5", entity.getUdfPickList5().toString());
        values.put("UDFPickList6", entity.getUdfPickList6().toString());
        values.put("UDFPickList7", entity.getUdfPickList7().toString());
        values.put("UDFPickList8", entity.getUdfPickList8().toString());
        values.put("UDFPickList9", entity.getUdfPickList9().toString());
        values.put("UDFPickList10", entity.getUdfPickList10().toString());
        values.put("UDFInteger1", entity.getUdfInteger1());
        values.put("UDFInteger2", entity.getUdfInteger2());
        values.put("UDFFloat1", entity.getUdfFloat1());
        values.put("UDFFloat2", entity.getUdfFloat2());
        values.put("UDFNumber1", entity.getUdfNumber1());
        values.put("UDFNumber2", entity.getUdfNumber2());
        values.put("UDFMoney1", entity.getUdfMoney1());
        values.put("UDFMoney2", entity.getUdfMoney2());
        values.put("UDFDate1", entity.getUdfDate1().toString());
        values.put("UDFDate2", entity.getUdfDate2().toString());
        values.put("CreatedBy", entity.getCreatedBy());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("CreatedDate", Utils.getUTCDateTimeAsString(entity.getCreatedAt()));
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        values.put("TenantId", entity.getTenantId().toString());
        return super.insert("PFUDFValues", values);
    }

    @Override
    public void update(UDFValues entity) throws EwpException {
        ContentValues values = new ContentValues();
        values.put("UDFString1", entity.getUdfString1());
        values.put("UDFString2", entity.getUdfString2());
        values.put("UDFString3", entity.getUdfString3());
        values.put("UDFString4", entity.getUdfString4());
        values.put("UDFString5", entity.getUdfString5());
        values.put("UDFString6", entity.getUdfString6());
        values.put("UDFString7", entity.getUdfString7());
        values.put("UDFString8", entity.getUdfString8());
        values.put("UDFString9", entity.getUdfString9());
        values.put("UDFString10", entity.getUdfString10());
        values.put("UDFPickList1", entity.getUdfPickList1().toString());
        values.put("UDFPickList2", entity.getUdfPickList2().toString());
        values.put("UDFPickList3", entity.getUdfPickList3().toString());
        values.put("UDFPickList4", entity.getUdfPickList4().toString());
        values.put("UDFPickList5", entity.getUdfPickList5().toString());
        values.put("UDFPickList6", entity.getUdfPickList6().toString());
        values.put("UDFPickList7", entity.getUdfPickList7().toString());
        values.put("UDFPickList8", entity.getUdfPickList8().toString());
        values.put("UDFPickList9", entity.getUdfPickList9().toString());
        values.put("UDFPickList10", entity.getUdfPickList10().toString());
        values.put("UDFInteger1", entity.getUdfInteger1());
        values.put("UDFInteger2", entity.getUdfInteger2());
        values.put("UDFFloat1", entity.getUdfFloat1());
        values.put("UDFFloat2", entity.getUdfFloat2());
        values.put("UDFNumber1", entity.getUdfNumber1());
        values.put("UDFNumber2", entity.getUdfNumber2());
        values.put("UDFMoney1", entity.getUdfMoney1());
        values.put("UDFMoney2", entity.getUdfMoney2());
        values.put("UDFDate1", entity.getUdfDate1().toString());
        values.put("UDFDate2", entity.getUdfDate2().toString());
        values.put("ModifiedBy", entity.getUpdatedBy());
        values.put("ModifiedDate", Utils.getUTCDateTimeAsString(entity.getUpdatedAt()));
        super.update("PFUDFValues", values, "EntityId=?", new String[]{entity.getEntityId().toString()});
    }

    /**
     * Generate sql string with minimum required fields for UDF_VALUES.
     *
     * @return STRING
     */
    private String getSQL() {
        String sql = "select udf.UDF_STRING_1, udf.UDF_STRING_2,udf.UDF_STRING_3, udf.UDF_STRING_4, udf.UDF_STRING_5,";
        sql += "udf.UDF_STRING_6, udf.UDF_STRING_7,udf.UDF_STRING_8, udf.UDF_STRING_9, udf.UDF_STRING_10, ";
        sql += "udf.UDF_BOOL_1, udf.UDF_BOOL_2,udf.UDF_BOOL_3, udf.UDF_BOOL_4, udf.UDF_BOOL_5, udf.UDF_INT_1, udf.UDF_INT_2, ";
        sql += "udf.UDF_DATE_1 AS UDF_DATE_1, udf.UDF_DATE_2 AS UDF_DATE_2, ";
        sql += "udf.UDF_NUMBER_1, udf.UDF_NUMBER_2, ";
        sql += "udf.UDF_MONEY_1, udf.UDoney2, ";
        sql += "udf.UDF_USER_ID_1,udfc1.FullName AS UDFUserId1Text, udf.UDF_USER_ID_2,udfc2.FullName AS UDFUserId2Text,";
        sql += "udf.UDFPickList1, udfp1.TextValue AS UDFPickList1Text, udf.UDFPickList2,udfp2.TextValue AS UDFPickList2Text, ";
        sql += "udf.UDFPickList3, udfp3.TextValue AS UDFPickList3Text, udf.UDFPickList4,udfp4.TextValue AS UDFPickList4Text, ";
        sql += "udf.UDFPickList5, udfp5.TextValue AS UDFPickList5Text,udf.UDFPickList6, udfp6.TextValue AS UDFPickList6Text, ";
        sql += "udf.UDFPickList7, udfp7.TextValue AS UDFPickList7Text,udf.UDFPickList8, udfp8.TextValue AS UDFPickList8Text, ";
        sql += "udf.UDFPickList9, udfp9.TextValue AS UDFPickList9Text,udf.UDFPickList10, udfp10.TextValue AS UDFPickList10Text, ";
        sql += "udf.TextBlock1,udf.TextBlock2,udf.TextBlock3,udf.TextBlock4,udf.TextBlock5,udf.TextBlock6, ";
        sql += "udf.TextBlock7,udf.TextBlock8,udf.TextBlock9,udf.TextBlock10,udf.[Version] ";
        sql += "FROM PFUDFValues AS UDF LEFT OUTER JOIN ";
        sql += "PFTenantUser AS udfu1 ON udfu1.UserId = udf.UDF_USER_ID_1 LEFT OUTER JOIN ";
        sql += "PFTenantUser AS udfu2 ON udfu2.UserId = udf.UDF_USER_ID_2 LEFT OUTER JOIN ";
        sql += "PFPicklistItem AS udfp1 ON udfp1.PicklistItemId = udf.UDFPickList1 LEFT OUTER JOIN ";
        sql += "PFPicklistItem AS udfp2 ON udfp2.PicklistItemId = udf.UDFPickList2 LEFT OUTER JOIN ";
        sql += "PFPicklistItem AS udfp3 ON udfp3.PicklistItemId = udf.UDFPickList3 LEFT OUTER JOIN ";
        sql += "PFPicklistItem AS udfp4 ON udfp4.PicklistItemId = udf.UDFPickList4 LEFT OUTER JOIN ";
        sql += "PFPicklistItem AS udfp5 ON udfp5.PicklistItemId = udf.UDFPickList5 LEFT OUTER JOIN ";
        sql += "PFPicklistItem AS udfp6 ON udfp6.PicklistItemId = udf.UDFPickList6 LEFT OUTER JOIN ";
        sql += "PFPicklistItem AS udfp7 ON udfp7.PicklistItemId = udf.UDFPickList7 LEFT OUTER JOIN ";
        sql += "PFPicklistItem AS udfp8 ON udfp8.PicklistItemId = udf.UDFPickList8 LEFT OUTER JOIN ";
        sql += "PFPicklistItem AS udfp9 ON udfp9.PicklistItemId = udf.UDFPickList9 LEFT OUTER JOIN ";
        sql += "PFPicklistItem AS udfp10 ON udfp10.PicklistItemId = udf.UDFPickList10 ";

        return sql;
    }


    /**
     * It is used to get customfield value with respact to EntityFields.
     *
     * @param entityId           UUID
     * @param activeCustomFields List<EntityFieldValueDetails>
     * @return Cursor
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public Cursor getUDFValuesFromUDFFieldAsResultSet(UUID entityId, List<EntityFieldValueDetails> activeCustomFields) throws EwpException {
        String column = "";
        String join = "";
        int i = 0;
        String sql = "SELECT UDF.EntityId, ";

        // It is generating the dynamic sql query for included custom column value
        for (int j = 0; j < activeCustomFields.size(); j++) {
            // If the column are not custom coulmn then do not exclude it.
            if (activeCustomFields.get(j).getFieldClass() != CustomFieldEnums.FieldClass.CUSTOM.getId()) {
                continue;
            }

            // Getting the UDFFieldValue sql table column name. for example UDString1 enum, column name is UDF_STRING_1 in UDFFieldValue sql table.

            CustomFieldEnums.FieldDataType[] array = CustomFieldEnums.FieldDataType.values();
            CustomFieldEnums.FieldDataType dataType = array[activeCustomFields.get(j).getFieldCode()];


            String fieldCodeString = dataType.toString();

            // If column is picklist or user column then UDF_VALUES table contain the id of fieldvalue.
            // To get display string we need to join with picklist or user table.
            if (fieldCodeString.contains("UDFPicklist")) {
                i++;
                // str is used to give alias, Means to accesscolumn by alieas, for example str value is udf1 then
                // to access a PFPicklistItem table column  by udf1.PicklistItemId.
                String str = "udf" + i;
                join += " LEFT JOIN PFPicklistItem AS " + str + " ON " + str + ".PicklistItemId =  UDF." + fieldCodeString;

                // Text value will be access by fieldcose+Text for example UDF_PICK_LIST_1 text value will be acces by UDFPicklist1Text.
                column += " " + str + ".TextValue AS " + fieldCodeString + "Text , ";
            } else if (fieldCodeString.contains("UDFUser")) {
                i++;
                // str is used to give alias, Means to accesscolumn by alieas, for example str value is udf1 then
                // to access a PFTenantUser table column  by udf1.UserId.
                String str = "udf" + i;

                // Text value will be access by fieldcode+Text for example PFTenantUser fullname text value will be acces by UDFUser1Text.
                join += " LEFT JOIN  PFTenantUser AS " + str + " ON " + str + ".UserId =  UDF." + fieldCodeString;
                column += " " + str + ".FullName AS " + fieldCodeString + "Text , ";
            }

            // Columns will always included.
            column += " UDF." + fieldCodeString + ", ";
        }

        // Each columns are appending comma at the end, we are excluding last comma(,) from last column.
        if (column.length() > 2) {
            column = column.substring(0, column.length() - 2);
        }

        sql += " " + column + " FROM PFUDFValues AS UDF " + join + " where UDF.EntityId= '" + entityId.toString() + "'";

        return executeSqlAndGetResultSet(sql);
    }

    /**
     * @param entity UDF_VALUES
     * @param cursor {@see #android.database.Cursor} database cursor for database.
     * @throws EwpException
     */
    @Override
    public void setPropertiesFromResultSet(UDFValues entity, Cursor cursor) throws EwpException {

        entity.setDirty(false);
    }
}
