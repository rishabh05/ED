//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.customfield;

import android.database.Cursor;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class will work as bridge in-between of EntityCustomFieldDef and UDFValueValues table. It will marge the UserDefined column valuea and Its type.
 * 1. CustomField store the user define custom field caption, value and data type, etc.
 * 2. It validate the custom field value by its define datatype as well as, It is also used to check required field.
 * It ia also validating the user defined field by datatype and required.
 * It hold the CUSTOM field label, value, its type.
 *
 * @author Parikshit
 */
public class CustomField {
    /**
     * It is caption of custom field.
     */
    private String labelName;
    /**
     * It donate the fieldCode of CUSTOM field.
     */
    private CustomFieldEnums.FieldCode fieldCode;
    /**
     * It donate the datatype(data type like Int, float,string) of Customfield value.
     */
    private CustomFieldEnums.FieldDataType dataType;
    /**
     * It diffrantiate the FieldType (like EMAIL, PICK_LIST, PHONE STRING) of Customfield value.
     */
    private CustomFieldEnums.FieldType fieldType;
    /**
     * It  is used to diffrantiate the custom, system, and builtin fields.
     */
    private CustomFieldEnums.FieldClass fieldClass;
    /**
     * Its value array.
     */
    private List<Object> value;
    /**
     * Its value text array.
     */
    private List<String> valueText;
    /**
     * Field is required.
     */
    private Boolean required = false;
    /**
     * Field has view rights.
     */
    private Boolean hasViewPermission = true;
    /**
     * Field has updated rights.
     */
    private Boolean hasUpdatePermission = true;

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public CustomFieldEnums.FieldCode getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(CustomFieldEnums.FieldCode fieldCode) {
        this.fieldCode = fieldCode;
    }

    public CustomFieldEnums.FieldDataType getDataType() {
        return dataType;
    }

    public void setDataType(CustomFieldEnums.FieldDataType dataType) {
        this.dataType = dataType;
    }

    public CustomFieldEnums.FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(CustomFieldEnums.FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public CustomFieldEnums.FieldClass getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(CustomFieldEnums.FieldClass fieldClass) {
        this.fieldClass = fieldClass;
    }

    public List<Object> getValue() {
        return value;
    }

    public void setValue(List<Object> value) {
        this.value = value;
    }

    public List<String> getValueText() {
        return valueText;
    }

    public void setValueText(List<String> valueText) {
        this.valueText = valueText;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getHasViewPermission() {
        return hasViewPermission;
    }

    public void setHasViewPermission(Boolean hasViewPermission) {
        this.hasViewPermission = hasViewPermission;
    }

    public Boolean getHasUpdatePermission() {
        return hasUpdatePermission;
    }

    public void setHasUpdatePermission(Boolean hasUpdatePermission) {
        this.hasUpdatePermission = hasUpdatePermission;
    }

    /**
     * Method is used to validate the custom field values as well as validate the custom field value from its datatype.
     *
     * @return Boolean
     */
    //@Override
    public Boolean validate() throws EwpException {

        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<>();

        if (this.required && (this.value == null || (this.value.isEmpty()))) {
            message.add("REQUIRED");
            String[] labelList = new String[]{this.labelName};
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, labelList);
        }
        List<Object> valueArray = this.value;
        if (valueArray != null && !valueArray.isEmpty()) {

            for (int i = 0; i < valueArray.size(); i++) {
                if (valueArray.get(i) instanceof String && !validateValueByDataType(valueArray.get(i))) {
                    String[] labelList = new String[]{this.labelName};
                    dicError.put(EnumsForExceptions.ErrorDataType.INVALID_FIELD_VALUE, labelList);
                    // dicError = EwpError.appendValueInDictionaryValueArray(dicError, key: ErrorDataType.INVALID_FIELD_VALUE, valueArray: [self.labelName!])
                }
            }
        }

        if (this.fieldType == CustomFieldEnums.FieldType.EMAIL) {
            String email = null;
            if (this.value.get(0) instanceof String) {
                email = this.value.get(0).toString();
            }

            if (this.value != null && !this.value.isEmpty() && (!Utils.isValidEmail(email))) {
                message.add(AppMessage.INVALID_EMAIL);
                String[] labelNameList = new String[]{this.labelName};
                dicError.put(EnumsForExceptions.ErrorDataType.INVALID_FIELD_VALUE, labelNameList);
            }
        }

        if (!message.isEmpty()) {
            throw new EwpException(new EwpException("Validation ERROR in CustomField"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
        return true;
    }

    /**
     * @param entityId   UUID
     * @param entityType int
     * @return Map<INTEGER, CustomField>
     * @throws EwpException
     */
    public static Map<Integer, CustomField> getLoggedInUserCustomFieldList(UUID entityId, int entityType) throws EwpException {
        EntityCustomFieldDataService service = new EntityCustomFieldDataService();
        Cursor resultSet = service.getViewableEntityCustomFieldAndPermissionListByUserId(EwpSession.getSharedInstance().getUserId(), EwpSession.getSharedInstance().getTenantId(), entityType);

        List<CustomField> customFieldInfoList = new ArrayList<CustomField>();
        // Loo- through all custom fields.
        while (resultSet.moveToNext()) {
            String id = resultSet.getString(resultSet.getColumnIndex("EntityFieldDetailsId"));
            CustomField customField = null;
            if (id == null) {
                continue;
            } else {
                Boolean canView = resultSet.getInt(resultSet.getColumnIndex("ViewField")) == 1 ? true : false;
                Boolean canUpdate = resultSet.getInt(resultSet.getColumnIndex("UpdateField")) == 1 ? true : false;
                // create the CustomField object and initilize with EntityCustomFieldDef object.
                customField = createAndInitCustomFieldInstanceFromEntityFieldResultSet(resultSet, canUpdate, canView);
            }
            customFieldInfoList.add(customField);
        }
        // UDF_VALUES table save the userdefined values with respact to entityid.
        // It is used to get the custom field values from UDFValue table. It will append the UDF(user defined field) value in customfields value object
        // with matched fieldcode.
        return getCustomFieldValuesByEntityIdAsCustomFieldDictonary(entityId, customFieldInfoList);
    }

    private boolean validateValueByDataType(Object value) {
        switch (this.dataType) {
            case INTEGER:
                return (Integer) value != null;
            case MONEY:
                return (Float) value != null;
            case DATE:
                return (Date) value != null;
            case NUMBER:
                return (Integer) value != null;
            case BOOL:
                return (boolean) value;
            default:
                break;
        }
        return true;
    }

    /**
     * @param resultSet Cursor
     * @param canUpdate Boolean
     * @param canView   Boolean
     * @return CustomField
     */
    private static CustomField createAndInitCustomFieldInstanceFromEntityFieldResultSet(Cursor resultSet, Boolean canUpdate, Boolean canView) {
        String fieldDataType = resultSet.getString(resultSet.getColumnIndex("FieldDataType"));
        String fieldType = resultSet.getString(resultSet.getColumnIndex("FieldType"));
        String customLbl = resultSet.getString(resultSet.getColumnIndex("CustomLabel"));
        String fieldCode = resultSet.getString(resultSet.getColumnIndex("EntityFieldId"));
        String fieldClass = resultSet.getString(resultSet.getColumnIndex("FieldClass"));
        CustomField customField = new CustomField();
        customField.hasUpdatePermission = canUpdate;
        customField.hasViewPermission = canView;
        CustomFieldEnums.FieldCode[] arrayFieldCode = CustomFieldEnums.FieldCode.values();
        customField.fieldCode = arrayFieldCode[Integer.parseInt(fieldCode)];
        CustomFieldEnums.FieldDataType[] arrayFieldDT = CustomFieldEnums.FieldDataType.values();
        customField.dataType = arrayFieldDT[Integer.parseInt(fieldDataType)];
        CustomFieldEnums.FieldType[] arrayFieldType = CustomFieldEnums.FieldType.values();
        customField.fieldType = arrayFieldType[Integer.parseInt(fieldType)];
        CustomFieldEnums.FieldClass[] arrayFieldClass = CustomFieldEnums.FieldClass.values();
        customField.fieldClass = arrayFieldClass[Integer.parseInt(fieldClass)];
        customField.labelName = (customLbl != null) ? customLbl : "";
        customField.value = new ArrayList<>();
        customField.valueText = new ArrayList<>();

        // Getting the default value and assigning it to property.
        String defaultValue = resultSet.getString(resultSet.getColumnIndex("DefaultValue"));
        if (defaultValue != null) {
            customField.value.add(defaultValue);
        }
        return customField;
    }

    /**
     * It is used to get admin custom fields list.
     *
     * @param entityCustomFieldList List<BaseEntity>
     * @return List<CustomField>
     */
    private static List<CustomField> getAdminCustomFields(List<BaseEntity> entityCustomFieldList) {
        List<CustomField> customFieldInfoList = new ArrayList<CustomField>();
        for (int i = 0; i < entityCustomFieldList.size(); i++) {
            EntityCustomFieldDef def = (EntityCustomFieldDef) entityCustomFieldList.get(i);

            CustomField customField = new CustomField();
            customField.hasUpdatePermission = true;
            customField.hasViewPermission = true;
            CustomFieldEnums.FieldCode[] arrayFieldCode = CustomFieldEnums.FieldCode.values();
            customField.fieldCode = arrayFieldCode[def.getFieldCode()];
            CustomFieldEnums.FieldDataType[] arrayFieldDT = CustomFieldEnums.FieldDataType.values();
            customField.dataType = arrayFieldDT[def.getFieldDataType()];
            customField.labelName = def.getCustomLabel();
            customField.value = new ArrayList<Object>();
            customField.valueText = new ArrayList<String>();
            customFieldInfoList.add(customField);
        }

        return customFieldInfoList;
    }

    /**
     * This method is used to get custom field value. Method will return the custom field value as dictionary.
     * Key will be id of custom column.
     *
     * @param entityId
     * @param activeCustomFields
     * @return Map<INTEGER, CustomField>
     */
    private static Map<Integer, CustomField> getCustomFieldValuesByEntityIdAsCustomFieldDictonary(UUID entityId, List<CustomField> activeCustomFields) throws EwpException {

        UDFValuesDataService service = new UDFValuesDataService();
         // If custom fields exist
        if (activeCustomFields != null && !activeCustomFields.isEmpty()) {
            Cursor resultSet = service.getEntityAsResultSet(entityId);
            return margeCustomFieldValueFromResultSet(resultSet, activeCustomFields);
        }
        return null;
    }

    /**
     * It will return dictonary of custom fields. FieldCode is a key of dictonary and CustomField(class object) as value .
     * CUSTOM field will give whole infomration of user define fields. Cusom field is mergining the EntityCustomFieldDef and its UDF_VALUES.
     *
     * @param resultSet          Cursor
     * @param activeCustomFields List<CustomField>
     * @return Map<INTEGER, CustomField>
     */
    private static Map<Integer, CustomField> margeCustomFieldValueFromResultSet(Cursor resultSet, List<CustomField> activeCustomFields) {
        Map<Integer, CustomField> udfValueDictonary = new HashMap<>();
        String fieldCode;
        CustomFieldEnums.FieldCode fCodeEnum;
        String value;
        String valueText;
        CustomField customField;
        while (resultSet.moveToNext()) {
            for (int i = 0; i < activeCustomFields.size(); i++) {
                fCodeEnum = activeCustomFields.get(i).fieldCode;
                fieldCode = fCodeEnum.toString();
                // Getting the customfieldinfo object if it is already added in dictionary.
                CustomField customField1 = udfValueDictonary.get(fCodeEnum.getId());
                if (customField1 != null) {
                    customField = customField1;
                } else {
                    //customField = CustomField()
                    customField = activeCustomFields.get(i);
                    customField.dataType = activeCustomFields.get(i).dataType;
                    customField.valueText = new ArrayList<>();
                    customField.value = new ArrayList<>(); //check
                }

                // Getting the picklist or user text.
                if (fieldCode.contains("UDFPicklist") || fieldCode.contains("UDFUser")) {
                    valueText = resultSet.getString(resultSet.getColumnIndex(fieldCode + "Text"));

                } else {
                    // Getting the udf string or number text.
                    valueText = resultSet.getString(resultSet.getColumnIndex(fieldCode));
                }
                // Getting the values.
                // In case of PICK_LIST and UDFUser field, Value will be ID.
                value = resultSet.getString(resultSet.getColumnIndex(fieldCode));

                customField.fieldCode = fCodeEnum;

                // Append the values in value array if value is not nil.
                if (value != null) {
                    customField.value.add(value);
                }

                // Append text in textvalue array if value is not nil.
                if (valueText != null) {
                    customField.valueText.add(valueText);
                }

                customField.required = activeCustomFields.get(i).required;
                udfValueDictonary.put(fCodeEnum.getId(), customField);
            }
        }
        return udfValueDictonary;
    }
}
