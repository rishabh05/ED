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
import com.eworkplaceapps.platform.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class encapsulates all data for EMPLOYEE entity.
 * This class contain the ENTITY_CUSTOM_FIELD values saveed by a user.
 */

public class UDFValues extends BaseEntity {
    public static final String UDF_VALUES_ENTITY_NAME = "UDF_VALUES";

    public UDFValues() {
        super(UDF_VALUES_ENTITY_NAME);
    }

    /**
     * Create UDF_VALUES object and return created object.
     *
     * @return
     */

    public static UDFValues createEntity() {
        return new UDFValues();
    }

    // ------------------- Begin Property section -----------------------

    private String udfString1 = "";

    private String udfString2 = "";
    private String udfString3 = "";

    private String udfString4 = "";

    private String udfString5 = "";

    private String udfString6 = "";

    private String udfString7 = "";

    private String udfString8 = "";

    private String udfString9 = "";

    private String udfString10 = "";

    // -----------------------  Begin PICK_LIST ---------------------

    private UUID udfPickList1 = Utils.emptyUUID();

    private UUID udfPickList2 = Utils.emptyUUID();

    private UUID udfPickList3 = Utils.emptyUUID();

    private UUID udfPickList4 = Utils.emptyUUID();

    private UUID udfPickList5 = Utils.emptyUUID();


    private UUID udfPickList6 = Utils.emptyUUID();

    private UUID udfPickList7 = Utils.emptyUUID();

    private UUID udfPickList8 = Utils.emptyUUID();

    private UUID udfPickList9 = Utils.emptyUUID();

    private UUID udfPickList10 = Utils.emptyUUID();

    // -----------------------  End PICK_LIST ---------------------

    // ----------------------- Begin UDFBool ---------------------

    private Boolean udfBool1 = false;

    private Boolean udfBool2 = false;

    private Boolean udfBool3 = false;

    private Boolean udfBool4 = false;

    private Boolean udfBool5 = false;

    // ----------------------- End UDFBool ---------------------

    // ------------  Begin UDFInt/Float/Double/DATE ------------

    private int udfInteger1;

    private int udfInteger2;

    private float udfNumber1;

    private float udfNumber2;

    private float udfFloat1;

    private float udfFloat2;

    private float udfMoney1;

    private float udfMoney2;

    private Date udfDate1=new Date();
    private Date udfDate2=new Date();
    private String applicationId = "";

    // ---------------  End UDFInt/Float/Double/DATE -------------

    // The Getter/setter property represents uniqueness for tenant.
    private UUID tenantId = Utils.emptyUUID();

    private UUID mainEntityId = Utils.emptyUUID();

    public String getUdfString1() {
        return udfString1;
    }

    public void setUdfString1(String udfString1) {
        setPropertyChanged(this.udfString1, udfString1);
        this.udfString1 = udfString1;
    }

    public String getUdfString2() {
        return udfString2;
    }

    public void setUdfString2(String udfString2) {
        setPropertyChanged(this.udfString2, udfString2);
        this.udfString2 = udfString2;
    }

    public String getUdfString3() {
        return udfString3;
    }

    public void setUdfString3(String udfString3) {
        setPropertyChanged(this.udfString3, udfString3);
        this.udfString3 = udfString3;
    }

    public String getUdfString4() {
        return udfString4;
    }

    public void setUdfString4(String udfString4) {
        setPropertyChanged(this.udfString4, udfString4);
        this.udfString4 = udfString4;
    }

    public String getUdfString5() {

        return udfString5;
    }

    public void setUdfString5(String udfString5) {
        setPropertyChanged(this.udfString5, udfString5);
        this.udfString5 = udfString5;
    }

    public String getUdfString6() {
        return udfString6;
    }

    public void setUdfString6(String udfString6) {
        setPropertyChanged(this.udfString6, udfString6);
        this.udfString6 = udfString6;
    }

    public String getUdfString7() {
        return udfString7;
    }

    public void setUdfString7(String udfString7) {
        setPropertyChanged(this.udfString7, udfString7);
        this.udfString7 = udfString7;
    }

    public String getUdfString8() {
        return udfString8;
    }

    public void setUdfString8(String udfString8) {
        setPropertyChanged(this.udfString8, udfString8);
        this.udfString8 = udfString8;
    }

    public String getUdfString9() {
        return udfString9;
    }

    public void setUdfString9(String udfString9) {
        setPropertyChanged(this.udfString9, udfString9);
        this.udfString9 = udfString9;
    }

    public String getUdfString10() {
        return udfString10;
    }

    public void setUdfString10(String udfString10) {
        setPropertyChanged(this.udfString10, udfString10);
        this.udfString10 = udfString10;
    }

    public UUID getUdfPickList1() {
        return udfPickList1;
    }

    public void setUdfPickList1(UUID udfPickList1) {
        setPropertyChanged(this.udfPickList1, udfPickList1);
        this.udfPickList1 = udfPickList1;
    }

    public UUID getUdfPickList2() {
        return udfPickList2;
    }

    public void setUdfPickList2(UUID udfPickList2) {
        setPropertyChanged(this.udfPickList2, udfPickList2);
        this.udfPickList2 = udfPickList2;
    }

    public UUID getUdfPickList3() {
        return udfPickList3;
    }

    public void setUdfPickList3(UUID udfPickList3) {
        setPropertyChanged(this.udfPickList3, udfPickList3);
        this.udfPickList3 = udfPickList3;
    }

    public UUID getUdfPickList4() {
        return udfPickList4;
    }

    public void setUdfPickList4(UUID udfPickList4) {
        setPropertyChanged(this.udfPickList4, udfPickList4);
        this.udfPickList4 = udfPickList4;
    }

    public UUID getUdfPickList5() {
        return udfPickList5;
    }

    public void setUdfPickList5(UUID udfPickList5) {
        setPropertyChanged(this.udfPickList5, udfPickList5);
        this.udfPickList5 = udfPickList5;
    }

    public UUID getUdfPickList6() {
        return udfPickList6;
    }

    public void setUdfPickList6(UUID udfPickList6) {
        setPropertyChanged(this.udfPickList6, udfPickList6);
        this.udfPickList6 = udfPickList6;
    }

    public UUID getUdfPickList7() {
        return udfPickList7;
    }

    public void setUdfPickList7(UUID udfPickList7) {
        setPropertyChanged(this.udfPickList7, udfPickList7);
        this.udfPickList7 = udfPickList7;
    }

    public UUID getUdfPickList8() {
        return udfPickList8;
    }

    public void setUdfPickList8(UUID udfPickList8) {
        setPropertyChanged(this.udfPickList8, udfPickList8);
        this.udfPickList8 = udfPickList8;
    }

    public UUID getUdfPickList9() {
        return udfPickList9;
    }

    public void setUdfPickList9(UUID udfPickList9) {
        setPropertyChanged(this.udfPickList9, udfPickList9);
        this.udfPickList9 = udfPickList9;
    }

    public UUID getUdfPickList10() {
        return udfPickList10;
    }

    public void setUdfPickList10(UUID udfPickList10) {
        setPropertyChanged(this.udfPickList10, udfPickList10);
        this.udfPickList10 = udfPickList10;
    }

    public Boolean getUdfBool1() {
        return udfBool1;
    }

    public void setUdfBool1(Boolean udfBool1) {
        setPropertyChanged(this.udfBool1, udfBool1);
        this.udfBool1 = udfBool1;
    }

    public Boolean getUdfBool2() {
        return udfBool2;
    }

    public void setUdfBool2(Boolean udfBool2) {
        setPropertyChanged(this.udfBool2, udfBool2);
        this.udfBool2 = udfBool2;
    }

    public Boolean getUdfBool3() {
        return udfBool3;
    }

    public void setUdfBool3(Boolean udfBool3) {
        setPropertyChanged(this.udfBool3, udfBool3);
        this.udfBool3 = udfBool3;
    }

    public Boolean getUdfBool4() {
        return udfBool4;
    }

    public void setUdfBool4(Boolean udfBool4) {
        setPropertyChanged(this.udfBool4, udfBool4);
        this.udfBool4 = udfBool4;
    }

    public Boolean getUdfBool5() {
        return udfBool5;
    }

    public void setUdfBool5(Boolean udfBool5) {
        setPropertyChanged(this.udfBool5, udfBool5);
        this.udfBool5 = udfBool5;
    }

    public int getUdfInteger1() {
        return udfInteger1;
    }

    public void setUdfInteger1(int udfInteger1) {
        setPropertyChanged(this.udfInteger1, udfInteger1);
        this.udfInteger1 = udfInteger1;
    }

    public int getUdfInteger2() {
        return udfInteger2;
    }

    public void setUdfInteger2(int udfInteger2) {
        setPropertyChanged(this.udfInteger2, udfInteger2);
        this.udfInteger2 = udfInteger2;
    }

    public float getUdfNumber1() {
        return udfNumber1;
    }

    public void setUdfNumber1(float udfNumber1) {
        setPropertyChanged(this.udfNumber1, udfNumber1);
        this.udfNumber1 = udfNumber1;
    }

    public float getUdfNumber2() {
        return udfNumber2;
    }

    public void setUdfNumber2(float udfNumber2) {
        setPropertyChanged(this.udfNumber2, udfNumber2);
        this.udfNumber2 = udfNumber2;
    }

    public float getUdfFloat1() {
        return udfFloat1;
    }

    public void setUdfFloat1(float udfFloat1) {
        setPropertyChanged(this.udfFloat1, udfFloat1);
        this.udfFloat1 = udfFloat1;
    }

    public float getUdfFloat2() {
        return udfFloat2;
    }

    public void setUdfFloat2(float udfFloat2) {
        setPropertyChanged(this.udfFloat2, udfFloat2);
        this.udfFloat2 = udfFloat2;
    }

    public float getUdfMoney1() {
        return udfMoney1;
    }

    public void setUdfMoney1(float udfMoney1) {
        setPropertyChanged(this.udfMoney1, udfMoney1);
        this.udfMoney1 = udfMoney1;
    }

    public float getUdfMoney2() {
        return udfMoney2;
    }

    public void setUdfMoney2(float udfMoney2) {
        setPropertyChanged(this.udfMoney2, udfMoney2);
        this.udfMoney2 = udfMoney2;
    }

    public Date getUdfDate1() {
        return udfDate1;
    }

    public void setUdfDate1(Date udfDate1) {
        setPropertyChanged(this.udfDate1, udfDate1);
        this.udfDate1 = udfDate1;
    }

    public Date getUdfDate2() {
        return udfDate2;
    }

    public void setUdfDate2(Date udfDate2) {
        setPropertyChanged(this.udfDate2, udfDate2);
        this.udfDate2 = udfDate2;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getMainEntityId() {
        return mainEntityId;
    }

    public void setMainEntityId(UUID mainEntityId) {
        this.mainEntityId = mainEntityId;
    }

    /**
     * It validate entityCustomField enity.
     *
     * @return Boolean
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<>();
        if (!message.isEmpty()) {
            throw new EwpException(new EwpException("Validation ERROR in CustomField"), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
        return true;
    }

    /**
     * Create the copy of an existing Task object.
     *
     * @param entity BaseEntity
     * @return BaseEntity
     */
    @Override
    public BaseEntity copyTo(BaseEntity entity) {
        // If both entities are not same then return the entity.
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        UDFValues udfValues = (UDFValues) entity;
        udfValues.entityId = this.entityId;
        udfValues.mainEntityId = this.mainEntityId;
        udfValues.tenantId = this.tenantId;
        udfValues.lastOperationType = this.lastOperationType;
        udfValues.updatedAt = this.updatedAt;
        udfValues.updatedBy = this.updatedBy;
        udfValues.createdAt = this.createdAt;
        udfValues.createdBy = this.createdBy;
        udfValues.applicationId = this.applicationId;
        udfValues.udfString1 = this.udfString1;
        udfValues.udfString2 = this.udfString2;
        udfValues.udfString3 = this.udfString3;
        udfValues.udfString4 = this.udfString4;
        udfValues.udfString5 = this.udfString5;
        udfValues.udfString6 = this.udfString6;
        udfValues.udfString7 = this.udfString7;
        udfValues.udfString8 = this.udfString8;
        udfValues.udfString9 = this.udfString9;
        udfValues.udfString10 = this.udfString10;
        udfValues.udfPickList1 = this.udfPickList1;
        udfValues.udfPickList2 = this.udfPickList2;
        udfValues.udfPickList3 = this.udfPickList3;
        udfValues.udfPickList4 = this.udfPickList4;
        udfValues.udfPickList5 = this.udfPickList5;
        udfValues.udfPickList6 = this.udfPickList6;
        udfValues.udfPickList7 = this.udfPickList7;
        udfValues.udfPickList8 = this.udfPickList8;
        udfValues.udfPickList9 = this.udfPickList9;
        udfValues.udfPickList10 = this.udfPickList10;
        udfValues.udfBool1 = this.udfBool1;
        udfValues.udfBool2 = this.udfBool2;
        udfValues.udfBool3 = this.udfBool3;
        udfValues.udfBool4 = this.udfBool4;
        udfValues.udfBool5 = this.udfBool5;
        udfValues.udfDate1 = this.udfDate1;
        udfValues.udfDate2 = this.udfDate2;
        udfValues.udfInteger1 = this.udfInteger1;
        udfValues.udfInteger2 = this.udfInteger2;
        udfValues.udfFloat1 = this.udfFloat1;
        udfValues.udfFloat2 = this.udfFloat2;
        udfValues.udfNumber1 = this.udfNumber1;
        udfValues.udfNumber2 = this.udfNumber2;
        udfValues.udfMoney1 = this.udfMoney1;
        udfValues.udfMoney2 = this.udfMoney2;
        return udfValues;
    }

    /**
     * This method is used to get custom field value. Method will return the custom field value as EntityFieldValueDetails array.
     * // EntityFieldValueDetails provide the detail about custom fields, like customfield is required or not, its datatype, caption etc.
     *
     * @param entityId
     * @param activeCustomFields
     * @return List<EntityFieldValueDetails>
     * @throws EwpException
     */
    public static List<EntityFieldValueDetails> mapFromUDFFieldValues(UUID entityId, List<EntityFieldValueDetails> activeCustomFields) throws EwpException {
        UDFValuesDataService service = new UDFValuesDataService();

        Map<Integer, EntityFieldValueDetails> udfValueDictionary = new HashMap<Integer, EntityFieldValueDetails>();
        // If custom fields exist
        if (activeCustomFields != null && !activeCustomFields.isEmpty()) {
            Cursor cursor = service.getUDFValuesFromUDFFieldAsResultSet(entityId, activeCustomFields);
            // If any error occured then return from here.
            if (cursor == null) {
                return null;
            }
            return mapCustomFieldValueFromEntityFieldValueDetails(cursor, activeCustomFields);
        }
        return activeCustomFields;
    }


    /**
     * It will return EntityFieldValueDetails list with value.
     * // EntityFieldValueDetails will give whole infomration of user define fields. it is setting the UDF value in value property
     * // of EntityFieldValueDetails object.
     *
     * @param cursor
     * @param activeCustomFields
     * @return List<EntityFieldValueDetails>
     */
    private static List<EntityFieldValueDetails> mapCustomFieldValueFromEntityFieldValueDetails(Cursor cursor, List<EntityFieldValueDetails> activeCustomFields) {
        String fieldCode = "";
        CustomFieldEnums.FieldCode fCodeEnum;
        String value = "";
        String valueText = "";
        EntityFieldValueDetails customField;
        List<EntityFieldValueDetails> entityFieldValueDetailsList = new ArrayList<EntityFieldValueDetails>();
        boolean hasCustomFields = false;
        while (cursor.moveToNext()) {
            for (int i = 0; i < activeCustomFields.size(); i++) {
                // For system or Built-in field do not need to set value.
                if (activeCustomFields.get(i).getFieldClass() != CustomFieldEnums.FieldClass.CUSTOM.getId()) {
                    entityFieldValueDetailsList.add(activeCustomFields.get(i));
                    continue;
                }
                hasCustomFields = true;
                fCodeEnum = CustomFieldEnums.FieldCode.values()[activeCustomFields.get(i).getFieldClass()];
                fieldCode = fCodeEnum.toString();
                // Getting the customfieldinfo objet.
                customField = activeCustomFields.get(i);
                customField.setValuesText(new ArrayList<Object>());
                customField.setValues(new ArrayList<Object>());
                // Getting the picklist or user text.
                if (fieldCode.contains("UDFPicklist") || fieldCode.contains("UDFUser")) {
                    valueText = cursor.getString(cursor.getColumnIndex(fieldCode + "Text"));
                } else {
                    // Getting the udf string or number text.
                    valueText = cursor.getString(cursor.getColumnIndex(fieldCode));
                }
                // Getting the values.
                // In case of Picklist and UDFUser field, Value will be ID.
                value = cursor.getString(cursor.getColumnIndex(fieldCode));
                // Append the values in value array if value is not nil.
                if (value != null && !"".equals(value)) {
                    customField.getValues().add(value);
                }
                // Append text in textvalue array if value is not nil.
                if (valueText != null && !"".equals(valueText)) {
                    customField.getValuesText().add(valueText);
                }
                // Setting the UDFValues id.
                String id = cursor.getString(cursor.getColumnIndex(("EntityId")));
                if (id != null && !"".equals(id)) {
                    customField.setMainEntityId(UUID.fromString(id));
                }
                // Add the field if there is value.
                if (!customField.getValues().isEmpty() || !customField.getValuesText().isEmpty()) {
                    entityFieldValueDetailsList.add(customField);
                }
            }
        }
        // If custom fields has no data, But those are avialble then filter out them.
        if (!hasCustomFields) {
            for (int i = 0; i < activeCustomFields.size(); i++) {
                // For system or Built-in field do not need to set value.
                if (activeCustomFields.get(i).getFieldClass() != CustomFieldEnums.FieldClass.CUSTOM.getId()) {
                    entityFieldValueDetailsList.add(activeCustomFields.get(i));
                }
            }
        }
        return entityFieldValueDetailsList;
    }
}
