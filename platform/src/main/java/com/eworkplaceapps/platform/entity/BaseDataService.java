//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/17/2015
//===============================================================================
package com.eworkplaceapps.platform.entity;

import android.database.Cursor;
import android.util.Log;

import com.eworkplaceapps.platform.cache.EwpCache;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * It allows to create, delete, and update entities. It is used to retrieve individual as well as list of entities.
 * 1) This class implements standard business logic and operations to do on entity.
 * 2) It also validate the entity.
 */
public abstract class BaseDataService<T extends BaseEntity> {

    private static final String TAG = "BaseDataService";
    protected Boolean applySecurity = true;
    private String entityName;

    /**
     * Initializing the data service entity-type
     *
     * @param entityName of STRING
     */
    public BaseDataService(String entityName) {
        this.entityName = entityName;
    }

    /**
     * Return the entity data class. This method must be override in subclass.
     *
     * @return BaseData<T>
     */
    public abstract BaseData<T> getDataClass();

    /**
     * GET an entity for given entity id
     *
     * @param id object
     * @return entity of type T
     */
    public T getEntity(Object id) throws EwpException {
        T entity = getFromCache(id);
        if (entity == null) {
            entity = getDataClass().getEntity(id);
            return entity;
        }
        return entity;
    }

    /**
     * GET the ResultSet for given id.
     *
     * @param id Object
     * @return result set
     */
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        Cursor resultSet = getDataClass().getEntityAsResultSet(id);
        return resultSet;
    }

    /**
     * GET entity list for an entity.
     *
     * @return List<T>
     */
    public List<? extends BaseEntity> getList() throws EwpException {
        List<? extends BaseEntity> entityList = null;
        entityList = getDataClass().getList();
        return entityList;
    }

    /**
     * It validate an entity and returns result as EwpError object variable.
     * Returns success(ErrorType) for successfully validation and array for error message.
     *
     * @param entity of type BaseEntity
     */
    public void validateOnAddAndUpdate(BaseEntity entity) throws EwpException {
        entity.validate();
    }

    /**
     * ADD an Entity
     *
     * @return Object
     */
    public Object add(T entity) throws EwpException {
        entity.setLastOperationType(DatabaseOperationType.ADD);
        validateOnAddAndUpdate(entity);
        Object result = getDataClass().add(entity);
        //addToCache(entity);
        return result;
    }

    /**
     * UPDATE an entity
     *
     * @param entity of type T
     */
    public void update(T entity) throws EwpException {
        if(entity.isDirty) {
            entity.setLastOperationType(DatabaseOperationType.UPDATE);

                validateOnAddAndUpdate(entity);

            getDataClass().update(entity);
            //updateInCache(entity);
        }
    }

    /**
     * Delete an entity
     *
     * @param entity
     */
    public void delete(T entity) throws EwpException {

        entity.setLastOperationType(DatabaseOperationType.DELETE);
        getDataClass().delete(entity);
        removeFromCache(entity);
    }

    /**
     * Returns the cached object of type Entity.
     * If entity found in cache return entity, otherwise return nil.
     *
     * @param entityId
     * @return
     */
    public T getFromCache(Object entityId) {
        return (T) EwpCache.instance().get((UUID) entityId);
    }

    /**
     * UPDATE the entity object into cache.
     * This method called after each call of ADD/UPDATE method.
     * To implement the add/update logics to cache entity this method must be override
     * in respective entity dataservice class otherwise this method do nothing.
     *
     * @param entity of type T
     */
    public void updateInCache(T entity) {
        EwpCache.instance().put(entity.entityId, entity);
    }

    /**
     * Remove the entity object into cache.
     * This method called after each call of DELETE method.
     * To implement the delete logics to remove cached entity this method must be override in entity dataservice class.
     * Otherwise this method do nothing.
     *
     * @param entity of type T
     */
    public void removeFromCache(T entity) {
        EwpCache.instance().remove(entity.entityId);
    }

    /**
     * add entity to cache
     *
     * @param entity
     */
    public void addToCache(T entity) {
        EwpCache.instance().put(entity.getEntityId(), entity);
    }
}
