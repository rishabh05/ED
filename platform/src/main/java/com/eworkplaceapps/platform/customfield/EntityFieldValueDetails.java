//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/22/2015
//===============================================================================

package com.eworkplaceapps.platform.customfield;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * It will work as bridge between EntityCustomFieldDetail, Entity class, UDF values.
 * It will contain the value and value that are used with a entity.
 * It will contains the custom field as well as Entity Field value.
 */
public class EntityFieldValueDetails extends EntityCustomFieldDef {
    public EntityFieldValueDetails() {
        super();
    }

    public static EntityFieldValueDetails createEntity() {
        return new EntityFieldValueDetails();
    }

    // Its value array.
    private List<Object> values=new ArrayList<Object>();
    private List<Object> valuesText=new ArrayList<Object>();
    private String entityVersion;
    // It is a id of UDFFieldValue or a Entity id.
    // If fields are CUSTOM then it will contains the UDF_VALUES id. Or, In case of Entity, It will contains the EntityId.
    private UUID mainEntityId = Utils.emptyUUID();

    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<>();

        if (super.getRequired() && (this.values == null || (this.values.size() <= 0))) {
            message.add("REQUIRED");
            String[] labelList = new String[]{super.getCustomLabel()};
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, labelList);
        }

        // It is used to validate the value from its datatype.

        List<Object> valueArray = this.values;

        if (valueArray != null && !valueArray.isEmpty()) {

            for (int i = 0; i < valueArray.size(); i++) {
                if (valueArray.get(i) instanceof String && !validateValueByDataType(valueArray.get(i))) {
                    String[] labelList = new String[]{super.getCustomLabel()};
                    dicError.put(EnumsForExceptions.ErrorDataType.INVALID_FIELD_VALUE, labelList);
                    // dicError = EwpError.appendValueInDictionaryValueArray(dicError, key: ErrorDataType.INVALID_FIELD_VALUE, valueArray: [self.labelName!])
                }
            }
        }

        // validate the email
        if (super.getFieldType() == CustomFieldEnums.FieldType.EMAIL.getId() && this.values != null && this.values.size() > 0 && !Utils.isValidEmail(this.values.get(0).toString())) {
            message.add(AppMessage.INVALID_EMAIL);
            String[] labelNameList = new String[]{this.getCustomLabel()};
            dicError.put(EnumsForExceptions.ErrorDataType.INVALID_FIELD_VALUE, labelNameList);
        }

        if (!message.isEmpty()) {
            throw new EwpException(new EwpException("Validation ERROR in CustomField"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
        return true;
    }

    private boolean validateValueByDataType(Object value) {
        CustomFieldEnums.FieldDataType[] array = CustomFieldEnums.FieldDataType.values();
        CustomFieldEnums.FieldDataType dataType = array[getFieldDataType() - 1];

        switch (dataType) {
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

    public UUID getMainEntityId() {
        return mainEntityId;
    }

    public void setMainEntityId(UUID mainEntityId) {
        this.mainEntityId = mainEntityId;
    }

    public String getEntityVersion() {
        return entityVersion;
    }

    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
    public List<Object> getValuesText() {
        return valuesText;
    }

    public void setValuesText(List<Object> valuesText) {
        this.valuesText = valuesText;
    }
}
