//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/28/2015
//===============================================================================
package com.eworkplaceapps.platform.dbsync;

import com.eworkplaceapps.platform.exception.EwpErrorHandler;

/**
 * It handles SYNC Service module error.
 */
public class SyncErrorHandler extends EwpErrorHandler {
    // Create the singleton
    private static SyncErrorHandler dataHandler;

    static {
        dataHandler = new SyncErrorHandler();
    }

    // Return the singleton
    public static SyncErrorHandler defaultSyncErrorHandler() {
        return dataHandler;
    }
}
