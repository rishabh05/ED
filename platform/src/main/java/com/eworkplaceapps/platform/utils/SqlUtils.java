//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/18/2015
//===============================================================================
package com.eworkplaceapps.platform.utils;

import android.database.Cursor;

import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.exception.enums.EnumsForExceptions;
import com.eworkplaceapps.platform.utils.enums.SortingOrder;

import java.util.ArrayList;
import java.util.List;


public class SqlUtils {

    /**
     * It provide to make sorting sql.
     * returns Sql string with field to sort and its order.
     *
     * @param sql           STRING
     * @param sortFieldName STRING
     * @param sortingOrder  SortingOrder
     * @return STRING sql
     */
    public static String buildSortClause(String sql, String sortFieldName, SortingOrder sortingOrder) {
        sql = "";
        if (sortingOrder != SortingOrder.NONE) {
            sql += " Order by " + sortFieldName + " " + sortingOrder.toString();
        }
        return sql;
    }


    /**
     * To check the item exist in database or not. If record found in database then return true otherwise return false.
     *
     * @param sql STRING
     * @return boolean representing whether the record exists or not
     */
    public static boolean recordExists(String sql) throws EwpException {
        DatabaseOps db = DatabaseOps.defaultDatabase();
        Cursor resultSet = db.executeQuery(sql, null);
        List<String> messages = new ArrayList<>();
        if (resultSet != null) {
            return resultSet.moveToFirst();
        } else {
            messages.add("No Record Found");
            throw new EwpException(new EwpException("Database ERROR"), EnumsForExceptions.ErrorType.DATABASE_ERROR, messages, EnumsForExceptions.ErrorModule.NONE, null, 0);
        }
    }

    /**
     * To check the item exist in database or not. If record found in database then return true otherwise return false.
     *
     * @param sql STRING
     * @param db  DatabaseOps
     * @return boolean representing whether the record exists or not
     */
    public static boolean recordExists(String sql, DatabaseOps db) throws EwpException {
        Cursor resultSet = db.executeQuery(sql, null);
        if (resultSet == null) {
            List<String> messages = new ArrayList<>();
            messages.add("No Record Exists");
            throw new EwpException(new EwpException("Database ERROR"), EnumsForExceptions.ErrorType.DATABASE_ERROR, messages, EnumsForExceptions.ErrorModule.NONE, null, 0);
        } else {
            if (!resultSet.moveToFirst()) {
                return false;
            }
            resultSet.close();
            return true;
        }
    }
}
