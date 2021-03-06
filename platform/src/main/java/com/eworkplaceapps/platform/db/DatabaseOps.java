//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/15/2015
//===============================================================================
package com.eworkplaceapps.platform.db;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.eworkplaceapps.platform.context.ContextHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * For every SqLite connection, possibly to a different SqLite file,
 * create a DatabaseOps object. For the default ewApps database use
 * openDefaultDatabase() to create its interface object.
 */
public class DatabaseOps extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseOps";
    private static final String SP_KEY_DB_VER = "db_version";
    private static final Integer DATABASE_VERSION = 1;
    private static final String DB_NAME = "ewAppsData.db";
    private static DatabaseOps databaseOps = null;
    private SQLiteDatabase sqLiteDatabase;
    private String path = "/data/data/";
    private int lastAffectedRows = 0;

    private DatabaseOps() {
        super(ContextHelper.getContext(), DB_NAME, null, DATABASE_VERSION);
        path = path + ContextHelper.getContext().getPackageName() + "/databases/";
        initialize();
    }

    /**
     * Return the ewApps default database.
     *
     * @return DatabaseOps instance
     */
    public static DatabaseOps defaultDatabase() {
        if (databaseOps == null) {
            databaseOps = new DatabaseOps();
        }
        return databaseOps;
    }

    /**
     * insert entity in database
     *
     * @param tableName STRING
     * @param values    ContentValues
     * @return inserted row id
     */
    public long insertEntity(String tableName, ContentValues values) {
        sqLiteDatabase = this.getWritableDatabase();
        long id = sqLiteDatabase.insert(tableName, null, values);
        lastAffectedRows = (int) id;
        return id;
    }

    /**
     * Lifted from DB class reference (executeStatements:) :
     * This executes a series of SQL statements that are combined in a single string (e.g. the SQL generated by the sqlite3 command line .dump command). This accepts no value parameters, but rather simply expects a single string with multiple SQL statements, each terminated with a semicolon. This uses sqlite3_exec.
     * Returns success/fail status.
     *
     * @param sql STRING
     * @return boolean
     */
    public boolean executeStatements(String sql) {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
        return true;
    }

    /**
     * deletes all rows from database
     *
     * @param tableName   STRING
     * @param whereClause STRING
     * @param args        STRING
     * @return num of rows deleted
     */
    public int deleteStatement(String tableName, String whereClause, String[] args) {
        sqLiteDatabase = this.getWritableDatabase();
        int removedRows = sqLiteDatabase.delete(tableName, whereClause, args);
        lastAffectedRows = removedRows;
        return removedRows;
    }

    /**
     * Lifted from databaseOps class reference (change) :
     * The number of rows changed by prior SQL statement.
     * This function returns the number of database rows that were changed or inserted or deleted by the most recently completed SQL statement on the database connection specified by the first parameter. Only changes that are directly specified by the INSERT, UPDATE, or DELETE statement are counted.
     *
     * @return int
     */
    public int changes() {
        return lastAffectedRows;
    }


    /**
     * /* Lifted from databaseOps class reference (executeQuery:) :
     * Executing queries returns an ResultSet object if successful, and nil upon failure. Like executing updates, there is a variant that accepts an NSError ** parameter. Otherwise you should use the lastErrorMessage and lastErrorMessage methods to determine why a query failed.
     * In order to iterate through the results of your query, you use a while() loop. You also need to “step” (via [ResultSet next]) from one record to the other.
     * This method employs sqlite3_bind for any optional value parameters. This properly escapes any characters that need escape sequences (e.g. quotation marks), which eliminates simple SQL errors as well as protects against SQL injection attacks. This method natively handles NSString, NSNumber, NSNull, NSDate, and NSData objects. ALL other object types will be interpreted as text values using the object’s description method.
     * <p/>
     * Note that if there are no arguments, that part in the call will be missing.
     * <p/>
     * Returns ResultSet.
     *
     * @param sql     STRING
     * @param objects Object[]
     * @return ResultSet
     */
    public Cursor executeQuery(String sql, Object[] objects) {
        return this.getReadableDatabase().rawQuery(sql, null);
    }

    /**
     * Begin transaction mode.
     * Return success/fail status.
     */
    public void beginTransaction() {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
    }

    public SQLiteDatabase beginTransaction01() {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        return sqLiteDatabase;
    }

    /**
     * Begin deferred transaction mode.
     * In Deferred transaction lock is acquired only when write is performed.
     * In contrast, IMMEDIATE transaction acquires a lock immediately.
     * This difference is relevant to concurrent access to the SqLite.
     * If ewApps uses multiple threads, this might become relevant.
     * Return success/fail status.
     *
     * @return boolean
     */
    public boolean beginDeferredTransaction() {
        sqLiteDatabase = this.getWritableDatabase();
        return true;
    }

    public SQLiteDatabase beginDeferredTransaction01() {
        sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase;
    }

    /**
     * Commit transactions.
     * Return success/fail status.
     */
    public void commitTransaction() {
        sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase.inTransaction()) {
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
    }

    /**
     * Is in transaction mode?
     * Return yes/no status.
     *
     * @return boolean
     */
/*    public boolean inTransaction() {
        sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.inTransaction();
    }*/

    /**
     * Returns if the given ResultSet is nil or empty.
     * Useful to check if any data from databaseOps is to be processed.
     *
     * @param cursor ResultSet
     * @return boolean
     */
    public boolean isResultSetEmpty(Cursor cursor) {
        return cursor.moveToFirst();
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.d(TAG, "createDataBase()");
                throw new IOException("ERROR copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = path + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (SQLiteException e) {
            Log.d(TAG, "sqlException in checkDataBase()");
            // database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {
        // OPEN your local database as the input stream
        InputStream myInput = ContextHelper.getContext().getAssets().open(DB_NAME);
        // Path to the just created empty database
        String outFileName = path + DB_NAME;
        // OPEN the empty database as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        // transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        if (databaseExists()) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(ContextHelper.getContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(SP_KEY_DB_VER, DATABASE_VERSION);
            editor.apply();
        }
    }

    /**
     * Returns true if database file exists, false otherwise.
     *
     * @return boolean whether db exists or not
     */
    private boolean databaseExists() {
        File dbFile = ContextHelper.getContext().getDatabasePath(DB_NAME);
        return dbFile.exists();
    }

    /**
     * Initializes database. Creates database if doesn't exist.
     */
    private void initialize() {
        if (databaseExists()) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(ContextHelper.getContext());
            int dbVersion = prefs.getInt(SP_KEY_DB_VER, 1);
            if (DATABASE_VERSION != dbVersion) {
                File dbFile = ContextHelper.getContext().getDatabasePath(DB_NAME);
                if (!dbFile.delete()) {
                    Log.w(TAG, "Unable to update database");
                }
            }
        }
        if (!databaseExists()) {
            try {
                createDataBase();
            } catch (IOException e) {
                Log.d(TAG, "ERROR Copying Database");
            }
        }
    }

    /**
     * update entity
     *
     * @param table       STRING
     * @param values      ContentValues
     * @param whereClause STRING
     * @param whereArgs   STRING[]
     * @return num of rows updated
     */
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        sqLiteDatabase = this.getWritableDatabase();
        int rowsAffected = sqLiteDatabase.update(table, values, whereClause, whereArgs);
        lastAffectedRows = rowsAffected;
        return rowsAffected;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
