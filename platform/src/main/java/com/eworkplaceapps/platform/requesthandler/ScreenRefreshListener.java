package com.eworkplaceapps.platform.requesthandler;

/**
 * Created by Shrey on 7/27/2015.
 * screen refresh listener notifies its registers to generate a message for all the alive screens to get refreshed
 * if there happens to arrive data during sync from server.
 * <p/>
 */
public interface ScreenRefreshListener {

    /**
     * call back method for notifying screens about data arrival during sync from server
     *
     * @param object can be of any type
     */
    public void onDataArrivalInSync(Object object);
}
