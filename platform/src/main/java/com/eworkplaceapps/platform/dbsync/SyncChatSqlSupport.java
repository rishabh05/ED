//===============================================================================
// copyright 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 7/23/2015
//===============================================================================
package com.eworkplaceapps.platform.dbsync;

/**
 * This is support class for SQL operation. All SQL operations required by other
 * classes are implemented here.
 */
public class SyncChatSqlSupport extends SyncSqlSupport {

    @Override
    public String getSyncItemLogTableName() {
        return "SyncChatItemLog";
    }

    @Override
    public String getSyncTimeLogTableName() {
        return "SyncChatTimeLog";
    }
}
