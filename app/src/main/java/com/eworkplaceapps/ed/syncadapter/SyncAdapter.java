//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Shrey Sharma
// Original Date: 18 July 2015
//===============================================================================
package com.eworkplaceapps.ed.syncadapter;

import android.accounts.Account;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.eworkplaceapps.ed.R;
import com.eworkplaceapps.ed.preferences.EDPreferenceHelper;
import com.eworkplaceapps.ed.utils.Constants;
import com.eworkplaceapps.ed.utils.GPSTracker;
import com.eworkplaceapps.ed.utils.Utils;
import com.eworkplaceapps.platform.dbsync.SyncNetworkService;
import com.eworkplaceapps.platform.dbsync.SyncOp;
import com.eworkplaceapps.platform.dbsync.SyncRequest;
import com.eworkplaceapps.platform.dbsync.SyncTransaction;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.DeviceInfo;
import com.eworkplaceapps.platform.helper.DeviceInfoData;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.requesthandler.RequestCallback;
import com.eworkplaceapps.platform.requesthandler.ScreenRefreshListener;

import org.apache.log4j.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * sync adapter for performing background syncing
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter implements RequestCallback, ScreenRefreshListener {

    private SyncNetworkService syncNetworkService;
    private final Logger log = Logger.getLogger(SyncAdapter.class);
    private static final int NOTIFICATION_ID = 1;
    private final String TAG = "SyncAdapter";
    private GPSTracker gpsTracker;
    private String locale = "";
    public SyncAdapter(Context context, boolean autoInitialize) {

        super(context, autoInitialize);
        gpsTracker = new GPSTracker(context);
        try{
            TelephonyManager tm = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
            locale = tm.getNetworkCountryIso();
        }catch(Exception e){
            locale ="";
            e.printStackTrace();
        }

    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        log.info("SyncAdapter onPerformSync()");
        Log.d("SyncAdapter", " onPerformSync()");
        try {
            if (EwpSession.getSharedInstance().isUserLoggedIn()) {
                DeviceInfoData deviceInfoData = new DeviceInfoData();
                DeviceInfo deviceInfo = deviceInfoData.getUserInfoAsEntity();
                if (deviceInfo == null || !deviceInfo.isInitialized()) {
                    return;
                }
                syncNetworkService = new SyncNetworkService();
                EDPreferenceHelper.getPreferences().setSyncWorking(true);
                sendNotification("Connect Syncing", "Syncing Started");
                log.info("**************************Sync Started**********************");
                Log.d("SyncAdapter", "**************************Sync Started**********************");
                syncNetworkService.timeZoneChange(this, gpsTracker.getLatitude(), gpsTracker.getLongitude(),locale);
                syncNetworkService.syncMyDataWithServer_Part1(this);
            }
        } catch (EwpException e) {
            EDPreferenceHelper.getPreferences().setSyncWorking(false);
            log.error("SyncAdapter EWPException " + e);
            Log.e("SyncAdapter", "SyncAdapter EWPException " + e);
        } catch (IOException e) {
            EDPreferenceHelper.getPreferences().setSyncWorking(false);
            log.error("SyncAdapter IOException " + e);
            Log.e(TAG, "SyncAdapter IOException " + e);
        }
    }


    @Override
    public void onSuccess(String name, Object object) {
        if (Constants.CLIENT_DATA_TAG.equals(name)) {
            log.info("SyncAdapter onSuccess in " + name);
            Log.d(TAG, "ClientData onSuccess ");
            if (syncNetworkService != null) {
                try {
                    if (object instanceof String) {
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new StringReader(String.valueOf(object)));
                        syncNetworkService.syncMyDataWithServer_Part2(xpp);
                        sendNotification("Connect Syncing", "Device Data Sent");
                        syncNetworkService.syncWithServerData_Part1(this, this);
                    }
                } catch (EwpException e) {
                    EDPreferenceHelper.getPreferences().setSyncWorking(false);
                    log.error("SyncAdapter EWPException clientdata onSuccess()" + e);
                    Log.e(TAG, "SyncAdapter EWPException clientdata onSuccess()" + e);
                } catch (XmlPullParserException e) {
                    EDPreferenceHelper.getPreferences().setSyncWorking(false);
                    log.error("SyncAdapter XmlPullParserException clientdata onSuccess()" + e);
                    Log.e(TAG, "SyncAdapter XmlPullParserException clientdata onSuccess()" + e);
                } catch (IOException e) {
                    EDPreferenceHelper.getPreferences().setSyncWorking(false);
                    log.error("SyncAlarmReceiver IOException clientdata onSuccess()" + e);
                    Log.e(TAG, "SyncAdapter IOException clientdata onSuccess()" + e);
                }
            }
        } else if (Constants.SERVER_DATA_TAG.equals(name)) {
            EDPreferenceHelper.getPreferences().setSyncWorking(false);
            cancelNotification();
            log.info("SyncAdapter ServerData onSuccess in " + object);
            Log.d(TAG, "ServerData onSuccess " + object);

        } else if (Constants.TIMEZONE_DATA_TAG.equals(name)) {
            EwpSession.setTimeZoneInSession(syncNetworkService.timeZoneName);
            syncNetworkService.timeZoneName = "";
        }
    }

    @Override
    public void onFailure(String name, Object object) {
        if (Constants.CLIENT_DATA_TAG.equals(name)) {
            EDPreferenceHelper.getPreferences().setSyncWorking(false);
            sendNotification("Connect Syncing", "Sync Failed in ClientData");
            log.error("SyncAdapter ClientData onFailure in " + object);
            Log.e(TAG, "ClientData onFailure ");
        } else if (Constants.SERVER_DATA_TAG.equals(name)) {
            EDPreferenceHelper.getPreferences().setSyncWorking(false);
            sendNotification("Connect Syncing", "Sync Failed in ServerData");
            log.error("SyncAdapter ServerData onFailure in---- " + object);
            Log.e(TAG, "ServerData onFailure---- " + object);
            log.error("**************************Sync Ended With Failure**********************");
            Log.e(TAG, "**************************Sync Failure**********************");

        } else if (Constants.TIMEZONE_DATA_TAG.equals(name)) {
            syncNetworkService.timeZoneName = "";
            log.error("SyncAdapter ServerData onFailure in---- " + object);
            Log.e(TAG, "ServerData onFailure---- " + object);
        }
    }

    @Override
    public void updateProgress(String name, String msg, int progressCount) {

    }

    @Override
    public void onDataArrivalInSync(Object object) {
        if (object instanceof SyncRequest) {
            new AsyncSendSignal((SyncRequest) object).execute();
        }
    }

    /**
     * async task to generate intent signal for fragments to get auto refreshed
     */
    private class AsyncSendSignal extends AsyncTask<Void, Void, Void> {
        private SyncRequest syncRequest;

        public AsyncSendSignal(SyncRequest syncRequest) {
            this.syncRequest = syncRequest;
        }

        @Override
        protected Void doInBackground(Void... params) {
            boolean status = false;
            boolean other = false;
            for (SyncTransaction syncTransaction : syncRequest.getSyncTransactionList()) {
                for (SyncOp syncOp : syncTransaction.getSyncOpList()) {
                    //checks for new s Picture,FirstName, LastName, JobTitle,StatusText,Following
                    if ("EDEmployeeStatus".equalsIgnoreCase(syncOp.getTableName())) {
                        status = true;
                    }
                    if ("PFUserEntityLink".equalsIgnoreCase(syncOp.getTableName())) {
                        other = true;
                    }
                    boolean val1 = syncOp.getColumnValues().containsKey("Picture");
                    boolean val2 = syncOp.getColumnValues().containsKey("FirstName");
                    boolean val3 = syncOp.getColumnValues().containsKey("LastName");
                    boolean val4 = syncOp.getColumnValues().containsKey("JobTitle");
                    boolean val5 = syncOp.getColumnValues().containsKey("StatusText");
                    if (val1 || val2 || val3 || val4 || val5) {
                        other = true;
                    }
                }
            }
            if (status) {
                //refresh my status
                getContext().sendBroadcast(new Intent(Utils.REFRESH_STATUS_INTENT));
            }
            if (other) {
                //refresh all employees
                getContext().sendBroadcast(new Intent(Utils.REFRESH_INTENT));
                //refresh my favorites
                getContext().sendBroadcast(new Intent(Utils.REFRESH_FAV_INTENT));
                //refresh my teams
                getContext().sendBroadcast(new Intent(Utils.REFRESH_MY_TEAMS_INTENT));
                //refresh more
                //getContext().sendBroadcast(new Intent(Utils.REFRESH_MORE_INTENT));
            }
            return null;
        }
    }

    /**
     * Send simple notification using the NotificationCompat API.
     */
    public void sendNotification(String title, String message) {
        // Use NotificationCompat.Builder to set up our notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        //icon appears in device notification bar and right hand corner of notification
        builder.setSmallIcon(R.drawable.ic_launcher);
        // This intent is fired when notification is clicked
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);
        // Large icon appears on the left of the notification
        builder.setLargeIcon(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_launcher));
        // Content title, which appears in large type at the top of the notification
        builder.setContentTitle(title);
        // ticker text appears in status bar
        builder.setTicker(message);
        // Content text, which appears in smaller text below the title
        builder.setContentText(message);
        // The subtext, which appears under the text on newer devices.
        // This will show-up in the devices with Android 4.2 and above only
        //builder.setSubText("Tap to view documentation about notifications.");
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
        // Will display the notification in the notification bar
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

}
