//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/18/2015
//===============================================================================
package com.eworkplaceapps.platform.communication;

import com.eworkplaceapps.platform.entity.BaseEntity;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.AppMessage;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.CommunicationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Communication class contain the communication details information.
 * A contact can be communication by Phone, Email, Mobile. So that entity contains the phone, email, mobile information.
 */
public class Communication extends BaseEntity implements Serializable {
    private static final String COMMUNICATION_ENTITY_NAME = "Communication";

    private UUID sourceEntityId = Utils.emptyUUID();
    private int sourceEntityType = -1, communicationSubType = 0;
    private String value;
    private UUID tenantId = Utils.emptyUUID();
    private CommunicationType communicationType = CommunicationType.PHONE;
    private String applicationId = "";
    private boolean favorite = false;
    private int groupBy = 1;

    public UUID getSourceEntityId() {
        return sourceEntityId;
    }

    public void setSourceEntityId(UUID sourceEntityId) {
        setPropertyChanged(this.sourceEntityId, sourceEntityId);
        this.sourceEntityId = sourceEntityId;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        setPropertyChanged(this.favorite, favorite);
        this.favorite = favorite;
    }

    public int getSourceEntityType() {
        return sourceEntityType;
    }

    public void setSourceEntityType(int sourceEntityType) {
        setPropertyChanged(this.sourceEntityType, sourceEntityType);
        this.sourceEntityType = sourceEntityType;
    }

    public int getCommunicationSubType() {
        return communicationSubType;
    }

    public void setCommunicationSubType(int communicationSubType) {
        setPropertyChanged(this.communicationSubType, communicationSubType);
        this.communicationSubType = communicationSubType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        setPropertyChanged(this.value, value);
        this.value = value;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        setPropertyChanged(this.tenantId, tenantId);
        this.tenantId = tenantId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(CommunicationType communicationType) {
        setPropertyChanged(this.communicationType, communicationType);
        this.communicationType = communicationType;
    }

    public int getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(int groupBy) {
        setPropertyChanged(this.groupBy, groupBy);
        this.groupBy = groupBy;
    }

    public Communication() {
        super(COMMUNICATION_ENTITY_NAME);
    }

    /**
     * Create Communication object and return created object.
     *
     * @return
     */
    public static Communication createEntity() {
        return new Communication();
    }

    /**
     * It validate communication required fields.
     */
    @Override
    public Boolean validate() throws EwpException {
        List<String> message = new ArrayList<String>();
        Map<EnumsForExceptions.ErrorDataType, String[]> dicError = new HashMap<EnumsForExceptions.ErrorDataType, String[]>();
        /// It is used to validate the first name null or empty.
        if (this.value == null || "".equals(this.value)) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED, new String[]{"Value"});
            message.add(AppMessage.REQUIRED_FIELD);
        }
        /// It is used to validate the email null or empty.
        if (this.sourceEntityId.equals(Utils.emptyUUID())) {
            dicError.put(EnumsForExceptions.ErrorDataType.REQUIRED,
                    new String[]{"SourceEntityId"});
            message.add(AppMessage.REQUIRED_FIELD);
        } else if (sourceEntityType == -1) {
            dicError.put(EnumsForExceptions.ErrorDataType.INVALID_FIELD_VALUE, new String[]{"sourceEntityType"});
            message.add(AppMessage.REQUIRED_FIELD);
        }
        if (message.size() == 0) {
            return true;
        } else {
            throw new EwpException(new EwpException(""), EnumsForExceptions.ErrorType.VALIDATION_ERROR, message, EnumsForExceptions.ErrorModule.DATA_SERVICE, dicError, 0);
        }
    }

    /**
     * Create the copy of an existing communication object.
     *
     * @param entity
     * @return
     */
    public Communication copyTo(Communication entity) {
        /// If both entities are not same then return the entity.
        if (!entity.getEntityName().equals(this.entityName)) {
            return null;
        }
        Communication communication = (Communication) entity;
        communication.setEntityId(this.entityId);
        communication.setTenantId(this.tenantId);
        communication.setCommunicationType(this.communicationType);
        communication.setCommunicationSubType(this.communicationSubType);
        communication.setSourceEntityId(this.sourceEntityId);
        communication.setSourceEntityType(this.sourceEntityType);
        communication.setValue(this.value);
        communication.setLastOperationType(this.lastOperationType);
        communication.setUpdatedAt(this.updatedAt);
        communication.setUpdatedBy(this.updatedBy);
        communication.setCreatedAt(this.createdAt);
        communication.setCreatedBy(this.createdBy);
        communication.isDirty = false;
        return communication;
    }

    /**
     *
     * @param communication
     * @return
     * @throws JSONException
     */
    public static JSONArray getCommunicationListAsDictionary(List<Communication> communication) throws JSONException {
        JSONArray dictArray = new JSONArray();
        for (int i = 0; i < communication.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Id", communication.get(i).entityId.toString());
            jsonObject.put("Type", communication.get(i).communicationType.getId());
            jsonObject.put("SubType", communication.get(i).communicationSubType);
            jsonObject.put("Value", communication.get(i).value.toString());
            jsonObject.put("Favorite", communication.get(i).favorite);
            jsonObject.put("OperationType", communication.get(i).lastOperationType.getId());

            dictArray.put(jsonObject);
        }

        return dictArray;
    }
    public static JSONArray getCommunicationListAsDictionary(List<Communication> communication,boolean addGroupBy) throws JSONException {
        JSONArray dictArray = new JSONArray();
        for (int i = 0; i < communication.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Id", communication.get(i).entityId.toString());
            jsonObject.put("Type", communication.get(i).communicationType.getId());
            jsonObject.put("SubType", communication.get(i).communicationSubType);
            jsonObject.put("Value", communication.get(i).value.toString());
            jsonObject.put("Favorite", communication.get(i).favorite);
            jsonObject.put("OperationType", communication.get(i).lastOperationType.getId());
            if (addGroupBy) {
                jsonObject.put("GroupBy", communication.get(i).getGroupBy());
            }
            dictArray.put(jsonObject);
        }

        return dictArray;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Communication that = (Communication) o;
        return !(entityId != null ? !entityId.equals(that.entityId) : that.entityId != null);

    }

    @Override
    public int hashCode() {
        return entityId != null ? entityId.hashCode() : 0;
    }
}
