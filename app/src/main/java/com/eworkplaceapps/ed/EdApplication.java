//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 12 June 2015
//===============================================================================
package com.eworkplaceapps.ed;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.widget.ImageView;

import com.eworkplaceapps.ed.utils.Utils;
import com.eworkplaceapps.platform.context.ContextHelper;
import com.eworkplaceapps.platform.logging.LogConfigurer;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * application class
 */
public class EdApplication extends MultiDexApplication {

    public static Typeface JOSE_FIN_SANS_BOLD;
    public static Typeface HELVETICA_NEUE;
    public static Typeface JOSE_FIN_SANS_SEMI_BOLD;

    @Override
    public void onCreate() {
        super.onCreate();
        JOSE_FIN_SANS_BOLD = Utils.getTypeface(this, 1);
        JOSE_FIN_SANS_SEMI_BOLD = Utils.getTypeface(this, 2);
        HELVETICA_NEUE = Utils.getTypeface(this, 3);
        ContextHelper.setContext(this);
        try {
            LogConfigurer.configure();
            LogConfigurer.initialize();
        } catch (IOException e) {
            LogConfigurer.error("EdApplication", e.getMessage());
            LogConfigurer.error("EdApplication", e.toString());
        }
        //initialize and create the image loader logic
        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }
        });
        Account account = new Account(Utils.ACCOUNT_NAME, Utils.ACCOUNT_TYPE);
        syncSetup(this, account);
        Utils.setAlarm(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    /**
     * method to create dummy account for background syncing, it will be used by sync adapter
     *
     * @param context
     * @param account
     */
    public static void syncSetup(Context context, Account account) {
        account = new Account(Utils.ACCOUNT_NAME, Utils.ACCOUNT_TYPE);
        AccountManager accountManager = AccountManager.get(context);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            ContentResolver.setIsSyncable(account, Utils.AUTHORITY, 1);
            ContentResolver.setMasterSyncAutomatically(true);
        }
        ContentResolver.addPeriodicSync(account, Utils.AUTHORITY, Bundle.EMPTY, Utils.SYNC_DURATION);
        ContentResolver.setSyncAutomatically(account, Utils.AUTHORITY, true);
    }
}
