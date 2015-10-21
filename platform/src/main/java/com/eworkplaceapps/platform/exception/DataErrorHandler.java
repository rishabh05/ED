//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/17/2015
//===============================================================================
package com.eworkplaceapps.platform.exception;

/**
 * It handles DataModule error.
 */
public class DataErrorHandler {

    private static DataErrorHandler dataHandler;

    // Create the singleton
    static {
        dataHandler = new DataErrorHandler();
    }

    private DataErrorHandler() {
    }

    // Return the singleton
    public static DataErrorHandler defaultDataErrorHandler() {
        return dataHandler;
    }
}
