//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/29/2015
//===============================================================================
package com.eworkplaceapps.platform.context;


import android.content.Context;

import com.eworkplaceapps.platform.exception.EwpException;

/**
 * context helper class for getting context's instance
 */
public class ContextHelper {

    private static Context context;

    /**
     * set context
     *
     * @param context for initialization
     */
    public static void setContext(Context context) {
        ContextHelper.context = context;
    }

    /**
     * get context
     *
     * @return Context initialized
     * @throws EwpException
     */
    public static Context getContext() {
        return context;
    }
}
