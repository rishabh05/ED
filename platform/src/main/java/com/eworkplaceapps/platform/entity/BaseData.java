//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/15/2015
//===============================================================================
package com.eworkplaceapps.platform.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.utils.Utils;

import org.apache.http.MethodNotSupportedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * It is a base class for all entity data layer.
 * It contain helper methods, and prepare and execute database command.
 * It has method for add/update/delete operation.
 * 1) It provides database operation for entity.
 * 2) Build sql statement for all operation.
 * 3) It also validates entity data.
 */
public abstract class BaseData<T extends BaseEntity> {

    public BaseData() {

    }

    /**
     * ADD an Entity object into database.
     * returns Newly added entity id.
     *
     * @param entity of type T
     * @return Object
     */
    public Object add(T entity) throws EwpException {
        if (EwpSession.getSharedInstance().getUserId() != null) {
            entity.setCreatedBy(EwpSession.getSharedInstance().getUserId().toString());
            entity.setUpdatedBy(EwpSession.getSharedInstance().getUserId().toString());
            entity.setUpdatedAt(new Date());
        }
        insertEntity(entity);
        return entity.getEntityId();
    }

    /**
     * UPDATE an Entity
     *
     * @param entity of type T
     * @throws EwpException
     */
    public void update(T entity) throws EwpException {
        throw new EwpException(new MethodNotSupportedException(" update not override in sub class"), EnumsForExceptions.ErrorType.NOT_IMPLEMENTED, null, EnumsForExceptions.ErrorModule.DATA, null, -1);
    }

    /**
     * DELETE an Entity.
     *
     * @param entity of type T
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public void delete(T entity) throws EwpException {
    }

    /**
     * DELETE an Entity using UUID
     *
     * @param id UUID
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public void delete(UUID id) throws EwpException {
    }

    /**
     * The method executes the query and return entity if executes properly else throw exception
     *
     * @param sql STRING
     * @return entity of type T
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public T executeSqlAndGetEntity(String sql) throws EwpException {
        Cursor cursor = null;
        DatabaseOps db = null;
        T entity = null;
        try {
            db = DatabaseOps.defaultDatabase();
            cursor = db.executeQuery(sql, null);
            //if cursor is null then throw exception
            if (cursor == null) {
                throw new EwpException(new EwpException("Database cursor is null while executing in executeSqlAndGetEntity"), EnumsForExceptions.ErrorType.DATABASE_ERROR, null, EnumsForExceptions.ErrorModule.DATA, null, -1);
            }
        } catch (Exception exception) {
            throw new EwpException(exception, EnumsForExceptions.ErrorType.DATABASE_ERROR, null, EnumsForExceptions.ErrorModule.DATA, null, -1);
        }
        // if entity found then return entity
        if (cursor != null && cursor.moveToFirst()) {
            do {
                entity = createEntity();
                // Set the value from ResultSet to entity
                setPropertiesFromResultSet(entity, cursor);
                cursor.close();
                return entity;
            } while (cursor.moveToNext());
        }
        return entity;
    }

    /**
     * It execute SQL query and returns Entity collections or null
     *
     * @param sql STRING
     * @return List<T>
     */
    public List<T> executeSqlAndGetEntityList(String sql) throws EwpException {
        Cursor resultSet = null;
        try {
            DatabaseOps db = DatabaseOps.defaultDatabase();
            resultSet = db.executeQuery(sql, null);
            if (resultSet == null) {
                throw new EwpException(new EwpException("Database cursor is null while executing in executeSqlAndGetEntityList"), EnumsForExceptions.ErrorType.DATABASE_ERROR, null,
                        EnumsForExceptions.ErrorModule.DATA, null, -1);
            }
        } catch (Exception e) {
            List<String> message = new ArrayList<String>();
            message.add(e.getMessage());
            throw new EwpException(e, EnumsForExceptions.ErrorType.DATABASE_ERROR, message,
                    EnumsForExceptions.ErrorModule.DATA, null, -1);
        }
        T entity = null;
        List<T> listEntity = new ArrayList<T>();
        // loop through the item and create the entity and set repactive properties.
        if (resultSet != null) {
            while (resultSet.moveToNext()) {
                // getting the entity instance
                entity = createEntity();
                // Set the entity properties value from resultset
                setPropertiesFromResultSet(entity, resultSet);
                listEntity.add(entity);
            }
            return listEntity;
        }
        return listEntity;
    }

    /**
     * It execute the SQL query returns ResultSet
     *
     * @param sql STRING
     * @return result set
     */
    public Cursor executeSqlAndGetResultSet(String sql) throws EwpException {
        DatabaseOps db = DatabaseOps.defaultDatabase();
        Cursor resultSet = db.executeQuery(sql, null);
        if (resultSet == null) {
            List<String> message = new ArrayList<String>();
            message.add("ResultSet is null");
            throw new EwpException(new EwpException("Resultset in null in executeSqlAndGetResultSet"), EnumsForExceptions.ErrorType.DATABASE_ERROR,
                    message, EnumsForExceptions.ErrorModule.DATA, null, -1);
        }
        return resultSet;
    }

    /**
     * It execute sql command and return no. of records affected by command.
     *
     * @param sql
     * @return int
     */
    public int executeNonQuery(String sql) {
        DatabaseOps db = DatabaseOps.defaultDatabase();
        db.executeStatements(sql);
        return db.changes();
    }

    /**
     * It will return true if execute successfully else false.
     *
     * @param sql
     * @return Boolean
     */
    public Boolean executeNonQuerySuccess(String sql) {
        Boolean isSuccess = false;
        try {
            DatabaseOps db = DatabaseOps.defaultDatabase();
            isSuccess = db.executeStatements(sql);
        } catch (Exception exception) {
            List<String> message = new ArrayList<String>();
            new EwpException(exception, EnumsForExceptions.ErrorType.DATABASE_ERROR,
                    message, EnumsForExceptions.ErrorModule.DATA, null, -1);
        }
        return isSuccess;
    }

    /**
     * update entity
     *
     * @param table       STRING
     * @param values      ContentValues
     * @param whereClause STRING
     * @param whereArgs   STRING []
     * @return
     */
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) throws EwpException {
        int rowsAffected = DatabaseOps.defaultDatabase().update(table, values, whereClause, whereArgs);
        return rowsAffected;
    }

    /**
     * insert into table
     *
     * @param table
     * @param values
     * @return
     */
    public long insert(String table, ContentValues values) {
        long id = DatabaseOps.defaultDatabase().insertEntity(table, values);
        return id;
    }

    /**
     * @param entity
     * @return boolean
     * @throws EwpException
     */
    public boolean deleteRows(T entity) throws EwpException {
        return false;
    }

    /**
     * deleteRows from db
     *
     * @param tableName
     * @param whereClause
     * @param args
     * @return boolean
     */
    public boolean deleteRows(String tableName, String whereClause, String[] args) throws EwpException {
        boolean hasSucceeded = false;
        try {
            DatabaseOps db = DatabaseOps.defaultDatabase();
            int removedRows = db.deleteStatement(tableName, whereClause, args);
            hasSucceeded = removedRows > 0 ? true : false;
        } catch (Exception exception) {
            List<String> message = new ArrayList<String>();
            new EwpException(exception, EnumsForExceptions.ErrorType.DATABASE_ERROR,
                    message, EnumsForExceptions.ErrorModule.DATA, null, -1);
        }
        return hasSucceeded;
    }

    /**
     * It execute sql and returns the number of rows affected by query.
     *
     * @return int
     */
    public int executeScalar() {
        return DatabaseOps.defaultDatabase().changes();
    }

    public void beginTransaction() {
        //there will be only one instance of DatabaseOps object and hence only one default database
        DatabaseOps db = DatabaseOps.defaultDatabase();
        db.beginTransaction();
    }

    public void rollbackTransaction() {
        //there will be only one instance of DatabaseOps object and hence only one default database
        DatabaseOps db = DatabaseOps.defaultDatabase();
        db.getWritableDatabase().endTransaction();
    }

    public void commitTransaction() {
        DatabaseOps db = DatabaseOps.defaultDatabase();
        db.commitTransaction();
    }

    /**
     * Create entity object and return.
     *
     * @param <T>
     * @return
     */
    public abstract <T extends BaseEntity> T createEntity();

    /**
     * Searches for an Entity that matches id.
     * returns A Entity object.
     *
     * @param id Object
     * @return T
     * @throws com.eworkplaceapps.platform.exception.EwpException
     */
    public <T extends BaseEntity> T getEntity(Object id) throws EwpException {
        // EwpError error = EwpError(EnumsForExceptions.ErrorType.NOT_IMPLEMENTED, new STRING[]{"getEntity not overrided."}, EnumsForExceptions.ErrorModule.DATA, null, -1);
        throw new EwpException(new MethodNotSupportedException(" getEntity not override in sub class"), EnumsForExceptions.ErrorType.NOT_IMPLEMENTED, null, EnumsForExceptions.ErrorModule.DATA, null, -1);

    }

    /**
     * Fetch all the Entity record from database.Collection of Entity.
     *
     * @param
     * @return List of type T
     * @throws EwpException
     */
    public List<? extends BaseEntity> getList() throws EwpException {
        throw new EwpException(new MethodNotSupportedException(" getList not override in sub class"), EnumsForExceptions.ErrorType.NOT_IMPLEMENTED, null, EnumsForExceptions.ErrorModule.DATA, null, -1);
    }

    /**
     * Searches for an Entity that matches id and return result as a Cursor.
     * returns A ResultSet populated with result.
     *
     * @return result set
     * @throws EwpException
     */
    public Cursor getEntityAsResultSet(Object id) throws EwpException {
        throw new EwpException(new MethodNotSupportedException(" getEntityAsResultSet not override in sub class"), EnumsForExceptions.ErrorType.NOT_IMPLEMENTED, null, EnumsForExceptions.ErrorModule.DATA, null, -1);
    }

    /**
     * Fetch all the Entity record from database and return result as a ResultSet.
     * returns A ResultSet populated with result.
     *
     * @return resultset
     * @throws EwpException
     */
    public Cursor getListAsResultSet() throws EwpException {
        throw new EwpException(new MethodNotSupportedException(" getListAsResultSet not override in sub class"), EnumsForExceptions.ErrorType.NOT_IMPLEMENTED, null, EnumsForExceptions.ErrorModule.DATA, null, -1);
    }

    /**
     * @param entity of type T
     * @return inserted on row
     * @throws EwpException
     */
    public long insertEntity(T entity) throws EwpException {
        throw new EwpException(new MethodNotSupportedException(" insert entity not override in sub class"), EnumsForExceptions.ErrorType.NOT_IMPLEMENTED, null, EnumsForExceptions.ErrorModule.DATA, null, -1);
    }

    /**
     * Sets all property values in the object from the given ResultSet.
     *
     * @param entity of type T
     * @param cursor {@see #android.database.Cursor} database cursor for database.
     */
    public void setPropertiesFromResultSet(T entity, Cursor cursor) throws EwpException {
        throw new EwpException(new MethodNotSupportedException(" method setPropertiesFromResultSet not override in sub class"), EnumsForExceptions.ErrorType.NOT_IMPLEMENTED, null, EnumsForExceptions.ErrorModule.NONE, null, -1);
    }
}
