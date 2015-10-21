//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Shrey Sharma
// Original Date: 7 july 2015
//===============================================================================
package com.eworkplaceapps.ed.receiver;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.eworkplaceapps.ed.EdApplication;
import com.eworkplaceapps.ed.utils.Utils;

import org.apache.log4j.Logger;

/**
 * Alarm receiver for syncing
 */
public class SyncAlarmReceiver extends BroadcastReceiver {

    private final Logger log = Logger.getLogger(SyncAlarmReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        log.info("SyncAlarmReceiver onReceive()");
        Log.d("SyncAlarmReceiver", "SyncAlarmReceiver onReceive()");
        Account account = new Account(Utils.ACCOUNT_NAME, Utils.ACCOUNT_TYPE);
        EdApplication.syncSetup(context, account);
        ContentResolver.requestSync(account, Utils.AUTHORITY, Bundle.EMPTY);
    }

}
