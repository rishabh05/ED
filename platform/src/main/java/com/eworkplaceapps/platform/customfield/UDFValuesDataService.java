//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.customfield;

import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseData;
import com.eworkplaceapps.platform.entity.BaseDataService;
import com.eworkplaceapps.platform.exception.EwpException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

/**
 * It expand the BaseDataService to provide services to UDF_VALUES entities.
 * UDF_VALUES represent the custom column values.
 * It is used for mapping the custom column value with custom column key.
 */
public class UDFValuesDataService extends BaseDataService<UDFValues> {

    UDFValuesData dataDelegate = new UDFValuesData();

    /**
     * Initializes a new instance of the TaskDataService class.
     */
    public UDFValuesDataService() {
        super("UDF_VALUES");
    }

    @Override
    public BaseData getDataClass() {
        return dataDelegate;
    }

    //----------------------- Begin Class Methods ---------------

    /**
     * This method is used to get custom field value. Method will return the custom field value as dictionary. Key will be id of custom column.
     *
     * @param entityId UUID
     * @param activeCustomFields List<EntityCustomFieldDef>
     * @return Map<INTEGER, STRING>
     */
    public Map<Integer, String> getUDFValuesByEntityIdAsDictonary(UUID entityId, List<EntityCustomFieldDef> activeCustomFields) throws EwpException {
        // If custom fields exist
        if (activeCustomFields != null && !activeCustomFields.isEmpty()) {
            Cursor resultSet = getEntityAsResultSet(entityId);
            return mapUDFValueDictonaryFromResultSet(resultSet, activeCustomFields);
        }
        return null;
    }

    public Cursor getUDFValuesFromUDFFieldAsResultSet(UUID entityId, List<EntityFieldValueDetails> activeCustomFields) throws EwpException {
        return dataDelegate.getUDFValuesFromUDFFieldAsResultSet(entityId, activeCustomFields);
    }

    /**
     * This method will return dictonary of custom field value. FieldCode is key of dictonary.
     *
     * @param issueCustomValuesResultSet Cursor
     * @param activeCustomFields List<EntityCustomFieldDef>
     * @return Map<INTEGER, STRING>
     */
    private Map<Integer, String> mapUDFValueDictonaryFromResultSet(Cursor issueCustomValuesResultSet, List<EntityCustomFieldDef> activeCustomFields) {
        Map<Integer, String> udfValueDictonary = null;
        CustomFieldEnums.FieldCode fCodeEnum;
        String fieldCode;
        String value = null;
        for (int i = 0; i < activeCustomFields.size(); i++) {
            CustomFieldEnums.FieldCode[] array = CustomFieldEnums.FieldCode.values();
            fCodeEnum = array[activeCustomFields.get(i).getFieldCode()];
            fieldCode = fCodeEnum.toString();
            while (issueCustomValuesResultSet.moveToNext()) {
                if (fieldCode.contains("UDFPicklist") || fieldCode.contains("UDFUser")) {
                    value = issueCustomValuesResultSet.getString(issueCustomValuesResultSet.getColumnIndex(fieldCode + "Text"));
                }
                value = (value == null ? issueCustomValuesResultSet.getString(issueCustomValuesResultSet.getColumnIndex(fieldCode)) : issueCustomValuesResultSet.getString(issueCustomValuesResultSet.getColumnIndex(fieldCode)) + ";#" + value);
                Integer val = null;
                if(activeCustomFields.get(i).getFieldCode() != null) {
                    val =  activeCustomFields.get(i).getFieldCode();
            }
                udfValueDictonary.put(val, value);
            }
        }
        return udfValueDictonary;
    }

    public void loadString(String csvFilePath) throws FileNotFoundException {
        //STRING content = STRING(contentsOfFile: csvFilePath, encoding:NSUTF8StringEncoding, error: &error)
        String content = new Scanner(new File("csvFilePath"), "UTF-8").useDelimiter("\\A").next();
        String[] lines = content.split("\n");
        for (String line : lines) {
            String[] field = line.split(",");
        }
    }


}
