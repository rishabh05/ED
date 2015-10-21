//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/29/2015
//===============================================================================
package com.eworkplaceapps.platform.requesthandler;

/**
 * call back handler for handling ajax responses
 */
public interface RequestCallback {

    /**
     * Called, When ajax response in successfully transformed into the desired object
     *
     * @param name   string call name returned from ajax response on success
     * @param object object returned from ajax response on success
     */
    public void onSuccess(String name, Object object);

    /**
     * Called, When there happens any kind of error, exception or failure in getting ajax response from the server
     *
     * @param name   string call name returned from ajax response on failure
     * @param object returned from ajax response on failure
     */
    public void onFailure(String name, Object object);

    /**
     * can be used to get progress
     *
     * @param msg           msg for progress
     * @param name          string call name
     * @param progressCount number of processes completed
     */
    public void updateProgress(String name, String msg, int progressCount);
}
