//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.platform.entity;

import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;

import org.apache.http.MethodNotSupportedException;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * <p/>
 * The class is base class for all the entities in the application. ALL the entities extend the base entity.
 * ALL the common properties are added to the entity.
 */
public class BaseEntity implements Serializable{

    /**
     * Specify the operation type, Operation type as said the type of operation to perform like for add operation, set the operation type as ADD.
     * Used this variable, If we have some common methods to apply on add/edit/delete then at that method we can find out the operation on entity.
     */
    protected DatabaseOperationType lastOperationType = DatabaseOperationType.NONE;

    /**
     * Id of the user who created the record
     */
    protected String createdBy;

    /**
     * date time when the record is created
     */
    protected Date createdAt = new Date();

    /**
     * id of the users who update the record
     */
    protected String updatedBy;

    /**
     * date time for update
     */
    protected Date updatedAt = new Date();

    /**
     * Getter/setter for isDirty property.
     * isDirty is set to true if  any property is changed after the last save.
     */
    protected boolean isDirty;

    /**
     * The following set of properties are common to most entities. These are abstracted in to base class.
     * Getter entity id
     * Most entity tables have a UUID primary key. The key is generated at the SQL INSERT time.
     */
    protected UUID entityId = Utils.emptyUUID();

    /**
     * entity name the value will be assigned
     */
    protected String entityName;

    public BaseEntity(String entityName) {
        this.entityName = entityName;
    }

    //Getter and Setter methods for properties
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public DatabaseOperationType getLastOperationType() {
        return lastOperationType;
    }

    public void setLastOperationType(DatabaseOperationType lastOperationType) {
        this.lastOperationType = lastOperationType;
    }

    /**
     * The method set the properties which are common to all the entities.
     * The method should be called with super in sub classes copyTo method.
     *
     * @param baseEntity BaseEntity
     * @return BaseEntity
     */
    public BaseEntity copyTo(BaseEntity baseEntity) {
        baseEntity.createdAt = this.createdAt;
        baseEntity.updatedBy = this.createdBy;
        baseEntity.updatedAt = this.updatedAt;
        baseEntity.updatedBy = this.updatedBy;
        return baseEntity;
    }

    /**
     * Validate the entity.
     *
     * @return Boolean
     */
    public Boolean validate() throws EwpException {
       return true;
    }

    /**
     * This method is created as generic so that it can work with any kind of data type.
     * Sets isDirty to true if the property's old value is different from the new value.
     * If oldValue and newVal are null then isDirty property will not change.
     * If oldValue is null and newVal is not null then isDirty property will set as true.
     * If oldValue is not null and newVal is null then isDirty property will set as true.
     * If oldValue and newVal are not null and both values are same then isDirty property will not set.
     * If oldValue and newVal are not null and both values are not same then isDirty property will set as true.
     *
     * @param oldValue value that is already the property holds currently
     * @param newVal   new value that the property should hold
     * @param <T>      T is type parameter
     */
    public <T> void setPropertyChanged(T oldValue, T newVal) {
        if ( (oldValue == null && newVal != null) ||
                (oldValue != null && newVal == null) ||
                (oldValue != null && newVal != null && !oldValue.equals(newVal))) {
            isDirty = true;
        }
    }

}
