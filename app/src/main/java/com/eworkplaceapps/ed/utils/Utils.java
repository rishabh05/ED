//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 12 June 2015
//===============================================================================
package com.eworkplaceapps.ed.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.eworkplaceapps.ed.EdApplication;
import com.eworkplaceapps.ed.R;
import com.eworkplaceapps.ed.fragment.BaseFragment;
import com.eworkplaceapps.ed.model.NoCaseComparator;
import com.eworkplaceapps.ed.receiver.SyncAlarmReceiver;
import com.eworkplaceapps.ed.view.ProgressWheel;
import com.eworkplaceapps.employeedirectory.employee.EmployeeAccess;
import com.eworkplaceapps.employeedirectory.employee.EmployeeQuickView;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.logging.LogConfigurer;
import com.eworkplaceapps.platform.utils.DateTimeHelper;
import com.eworkplaceapps.platform.utils.enums.EmailType;
import com.eworkplaceapps.platform.utils.enums.PhoneType;
import com.eworkplaceapps.platform.utils.enums.SocialMediaType;

import static com.eworkplaceapps.ed.utils.Constants.LIST_DISPLAY_DATE_FORMAT;
import static com.eworkplaceapps.ed.utils.Constants.LIST_DISPLAY_DATE_TIME_FORMAT;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

public class Utils {

    public static List<EmployeeQuickView> list_emp = new ArrayList<EmployeeQuickView>();
    public static List<EmployeeQuickView> myTeamsListEmp = new ArrayList<EmployeeQuickView>();
    public static final String MESSAGE = "message";
    public static final String REPORTS_TO_SELECT = "prevSelected";
    public static final String DEPARTMENT_SELECT = "prevDept";
    public static final String LOCATION_SELECT = "prevLocation";
    public static final String CONTACT_SELECT = "prevContactType";
    public static final String EMAIL_TYPE_SELECT = "prevEmailType";
    public static final String PERSONAL_PHONE_SELECT = "prevPersonalPhoneType";
    public static final String TEAMS_SELECT = "prevTeams";
    public static final String EMERGENCY_PHONE_SELECT = "prevEmergPhone";
    public static final String EMERGENCY_EMAIL_SELECT = "prevEmergEmail";
    public static final String TEAM_NAME = "teamName";
    public static final String INFO_TYPE = "type";
    public static final String INFO_TYPE_DEPARTMENT = "Department";
    public static final String INFO_TYPE_TEAM = "Team";
    public static final String INFO_TYPE_FOLLOWUP = "followup";
    public static final String INFO_TYPE_LOCATION = "Location";
    public static final String INFO_TYPE_STATUS = "Status Info";
    public static final String DELETE_TEAM = "Delete Team";
    public static final String DELETE_DEPARTMENT = "Delete Department";
    public static final String TEAM_EDITTEXT_HINT = "Team Name";
    public static final String DEPARTMENT_EDITTEXT_HINT = "Department Name";
    public static final String DELETE_LOCATION = "Delete Location";
    public static final String LOCATION_EDITTEXT_HINT = "Location Name";
    public static final String NEW_TEAM = "New Team";
    public static final String NEW_DEPARTMENT = "New Department";
    public static final String NEW_LOCATION = "New Location";
    public static final String INTENT_FROM = "from";
    public static final String PROFILE = "profile";
    public static final String MY_TEAMS = "myTeams";
    public static final String BY_LOCATION = "byLocation";
    public static final String BY_DEPARTMENT = "byDepartment";
    public static final String BY_TEAM = "byTeam";
    public static final String PERSONAL_PHONE = "personalPhone";
    public static final String SOCIAL_PROFILE = "socialProfile";
    public static final String SOCIAL_OR_PHONE = "socialOrPhone";
    public static final String SOCIAL_TITLE = "Social Profile";
    public static final String PERSONAL_PHONE_TITLE = "Phone";
    public static final String PRV_SELECTED_TEAM = "prvSelectedTeam";
    public static final String PRV_SELECTED_SOCIAL_PROFILE = "prvSelectedSocialProfile";
    public static final String PRV_SELECTED_PERSONAL_PHONE = "prvSelectedPersonalPhone";
    public static final String SELECTED_POSITION = "phnOrSocialProfileSelectPos";
    public static final String STORED_SET_VALUE = "storedSet";
    public static final String DEPARTMENT_ID = "departmentId";
    public static final String LOCATION_ID = "locationId";
    public static final String TEAM_ID = "teamId";
    public static final String SPECIALCHARACTER_PATTERN = "[^\\w]{1,}";
    public static final String NUMBER_PATTERN = "[0-9]{1,}";

    //syncing
    public static final long SYNC_DURATION = 120 * 1000;
    public final static String ACCOUNT_NAME = "Connect";
    public static final String ACCOUNT_TYPE = "com.eworkplaceapps.ed";
    public final static String AUTHORITY = "com.eworkplaceapps.ed.provider";

    //broadcast intent for refreshing all employees list
    public static final String REFRESH_INTENT = "refreshAllEmployees";
    //broadcast intent for refreshing favorite screen
    public static final String REFRESH_FAV_INTENT = "refreshFavorites";
    //broadcast intent for refreshing favorite screen
    public static final String REFRESH_MY_TEAMS_INTENT = "refreshMyTeams";
    // broadcast intent for refreshing status screen
    public static final String REFRESH_STATUS_INTENT = "refreshStatus";
    // broadcast intent for refreshing more screen
    public static final String REFRESH_MORE_INTENT = "refreshMore";
    public static final String REFRESH_VIEW_GROUP_INTENT = "refreshViewGroup";
    public static final String REFRESH = "Refresh";
    public static final String INFO = "info";
    public static final String DELETE = "Delete";

    //here 63+1=64, 124+2=126 and 62+2=64

    public static int DEP_POSITION = 3;
    public static int LOC_POSITION = 4;
    public static int TEAM_POSITION = 4;
    public static final int DRAWABLE_RIGHT = 2;
    public static final int PF_COMMUNICATION_LENGTH = 200;
    public static final int PF_ADDRESS_LENGTH = 200;
    public static final int EMPLOYEE_NAME_LENGTH = 200;
    public static final int PF_EMAIL_COMMUNICATION_LENGTH = 256;
    public static final int PF_PHONE_LENGTH = 50;

    //selected fragment position
    public static int FRAGMENT_POSITION = 0;

    public static boolean favoriteTabFlag = false;
    public static boolean moreTabFlag = false;
    public static boolean moreTabFollowUpsFlag = false;
    public static boolean isFollowed = true;
    public static String EMPLOYEE_LOGIN_ID = "employeeLoginId";
    public static boolean FROM_PROFILE = false;
    public static final String EMAIL_INTENT = "emailIntent";

    //Index of the alphabet in side index which should be in Teal color
    public static int textPosition;

    public static AlertDialog alertDialog;
    public static ProgressWheel loading;
    public static boolean isDataNotLoaded = false;
    public static ProgressWheel moreFragmentLoading;


    /**
     * @param progressDialog
     * @param title
     */
    public static void showProgress(ProgressDialog progressDialog, String title) {
        Spanned title2 = actionBarTitle(title);
        if (progressDialog != null) {
            progressDialog.setCancelable(false);
            progressDialog.setMessage(title2);
            progressDialog.setProgressStyle(ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
            progressDialog.show();
        }
    }

    /**
     * @param progressDialog
     */
    public static void hideProgress(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    /**
     * @param numbers number with sms will be sent
     * @return Intent
     */
    public static Intent smsIntent(String numbers) {
        Intent smsVIntent = new Intent(Intent.ACTION_VIEW);
        smsVIntent.setType("vnd.android-dir/mms-sms");
        smsVIntent.putExtra("address", numbers);
        return smsVIntent;
    }

    /**
     * @param number
     * @return Intent
     */
    public static Intent makeACall(String number) {
        String url = "tel:" + number;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        return intent;
    }

    /**
     * To send email
     *
     * @param emailId
     * @return Intent
     */
    public static void sendEmail(String[] emailId, Context context) {
        StringBuffer emailStr = new StringBuffer();
        for (String s : emailId) {
            emailStr.append(s).append(";");
        }
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + emailStr.toString()));
        Intent openInChooser = Intent.createChooser(emailIntent, context.getResources().getString(R.string.send_mail));
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resInfo = pm.queryIntentActivities(emailIntent, 0);
        if (resInfo != null && resInfo.size() > 0) {
            context.startActivity(openInChooser);
        } else {
            Utils.alertDialog(context, context.getResources().getString(R.string.no_email_client_title), context.getResources().getString(R.string.no_email_client_msg));
        }
    }

    /**
     * @param context
     * @param view
     */
    public static void showSoftKeyboard(Context context, View view) {
        try {
            if (view.requestFocus()) {
                InputMethodManager imm = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param view
     */
    public static void hideSoftKeyboard(Context context, View view) {
        try {
            if (view.requestFocus()) {
                final InputMethodManager inputMethodManager =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboardWithoutReq(Context context, View view) {
        try {
            final InputMethodManager inputMethodManager =
                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    /**
     * global method to hide keyboard
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * global method to hide keyboard for Dynamic field
     *
     * @param activity
     */
    public static void hideKeyboardForDynamicField(Activity activity, EditText edtTxt) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(edtTxt.getWindowToken(), 0);
    }

    /**
     * global method to hide keyboard for Dynamic field
     *
     * @param activity
     */
    public static void showKeyboardForDynamicField(Activity activity) {
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    /**
     * @param context Context
     * @param resId   resource id
     * @return Bitmap
     */
    public static Bitmap getBitmapFromResource(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(),
                resId);
    }

    /**
     * @param activity from where intent gets generate
     * @param cls      where intent have to proceed
     * @param finish   current activity
     */
    public static void intent(Activity activity, Class<?> cls, boolean finish) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        if (finish) {
            activity.finish();
        }
    }

    /**
     * @param context context
     * @param fontId  fontid
     * @return Typeface
     */
    public static Typeface getTypeface(Context context, int fontId) {
        Typeface typeface = null;
        switch (fontId) {
            case 1:
                typeface = Typeface.createFromAsset(context.getAssets(), "JosefinSans-Bold.ttf");
                break;
            case 2:
                typeface = Typeface.createFromAsset(context.getAssets(), "JosefinSans-SemiBold.ttf");
                break;
            case 3:
                typeface = Typeface.createFromAsset(context.getAssets(), "HelveticaNeue-Regular.ttf");
                break;
            default:
                typeface = Typeface.createFromAsset(context.getAssets(), "JosefinSans-Bold.ttf");
                break;
        }
        return typeface;
    }

    /**
     * @param title to set
     * @return title SpannableString
     */
    public static SpannableString actionBarTitle(String title) {
        SpannableString s = new SpannableString(title);
        s.setSpan(new CustomTypefaceSpan("", EdApplication.HELVETICA_NEUE), 0, s.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return s;
    }

    /**
     * @param string to get bold string
     * @return string SpannableString
     */
    public static SpannableString getBoldString(String string) {
        SpannableString s = new SpannableString(string);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return s;
    }

    /**
     * @param context
     * @param object
     */
    public static void log(Context context, String object) {
        Log.d(context.getClass().getName(), object);
    }


    /**
     * method for return color of status
     *
     * @param status
     * @return
     */
    public static int getStatusColor(Context context, String status) {

        switch (status) {
            case "Business Trip":
                return context.getApplicationContext().getResources().getColor(R.color.a);
            case "In Office":
                return context.getApplicationContext().getResources().getColor(R.color.b);
            case "Offsite Meeting":
                return context.getApplicationContext().getResources().getColor(R.color.c);
            case "On Vacation":
                return context.getApplicationContext().getResources().getColor(R.color.d);
            case "Out Sick":
                return context.getApplicationContext().getResources().getColor(R.color.e);
            case "Working Remotely":
                return context.getApplicationContext().getResources().getColor(R.color.f);
            case "Other":
                return context.getApplicationContext().getResources().getColor(R.color.g);
            default:
                return context.getResources().getColor(R.color.white);
        }
    }

    /**
     * method for return color of status
     *
     * @param status
     * @return
     */
    public static int getStatusColorFromService(Context context, String status) {

        switch (status) {
            case "Business Trip":
                return context.getApplicationContext().getResources().getColor(R.color.a);
            case "In Office":
                return context.getApplicationContext().getResources().getColor(R.color.b);
            case "Offsite Meeting":
                return context.getApplicationContext().getResources().getColor(R.color.c);
            case "On Vacation":
                return context.getApplicationContext().getResources().getColor(R.color.d);
            case "Out Sick":
                return context.getApplicationContext().getResources().getColor(R.color.e);
            case "Working Remotely":
                return context.getApplicationContext().getResources().getColor(R.color.f);
            case "Other":
                return context.getApplicationContext().getResources().getColor(R.color.g);
            default:
                return context.getResources().getColor(R.color.white);
        }
    }

    /**
     * method for set cursor color of EditText
     *
     * @param editText
     * @return
     */
    public static void setCursor(EditText editText) {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, R.drawable.cursor_color);
            editText.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);
        } catch (Exception ignored) {
            LogConfigurer.error("Utils", "" + ignored);
        }
    }

    /**
     * method for set cursor color of EditText for EditProfile only
     *
     * @param editText
     * @return
     */
    public static void setCursorDrawable(EditText editText) {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, R.drawable.cursor_color);
            editText.setBackgroundResource(R.drawable.edit_text_holo_light);
        } catch (Exception ignored) {
            LogConfigurer.error("Utils", "" + ignored);
            Log.d("Utils", "Exception-> " + ignored);
        }
    }

//    /**
//     * method to filter share client
//     *
//     * @param context
//     * @return
//     */
//    public static void onShareClick(Context context, String emailId) {
//        Intent emailIntent = new Intent();
//        emailIntent.setAction(Intent.ACTION_SENDTO);
//        emailIntent.setData(Uri.parse("mailto:" + emailId));
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Details");
//
//        PackageManager pm = context.getPackageManager();
//        emailIntent.setData(Uri.parse("mailto:" + ""));
//        List<ResolveInfo> resInfo = pm.queryIntentActivities(emailIntent, 0);
//        for (int i = 0; i < resInfo.size(); i++) {
//            ResolveInfo ri = resInfo.get(i);
//            String packageName = ri.activityInfo.packageName;
//            if (packageName.toLowerCase().contains("conversation")) {
//                emailIntent.setData(Uri.parse("mailto:" + "8989626215"));
//            } else {
//                emailIntent.setData(Uri.parse("mailto:" + emailId));
//            }
//        }
//        Intent openInChooser = Intent.createChooser(emailIntent, "Send mail...");
//        context.startActivity(openInChooser);
//    }

    /**
     * method to filter share client
     *
     * @param context
     * @return
     */
    public static void onShareClick01(Context context, String emailId, File vcfFile) {
        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/gm");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "mail@gmail.com");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(vcfFile));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.share_email_subject));
        Intent openInChooser = Intent.createChooser(emailIntent, context.getResources().getString(R.string.send_mail));
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resInfo = pm.queryIntentActivities(emailIntent, 0);
        if (resInfo != null && resInfo.size() > 0) {
            context.startActivity(openInChooser);
        } else {
            Utils.alertDialog(context, context.getResources().getString(R.string.no_email_client_title), context.getResources().getString(R.string.no_email_client_msg));
        }
    }


    /**
     * @param context
     * @param vcfFile
     */
    public static void shareFileThroughSMS(Context context, File vcfFile) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setPackage("com.android.mms");
        sendIntent.setType("text/x-vcard");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(vcfFile));
        context.startActivity(sendIntent);
    }

    /**
     * method for getting phone label
     *
     * @param communicationSubType
     * @return
     */
    public static String getPhoneLabel(int communicationSubType) {
        if (communicationSubType == PhoneType.HOME.getId()) {
            return PhoneType.getPhoneTypeListAsDictionary().get(PhoneType.HOME.getId());
        } else if (communicationSubType == PhoneType.MOBILE.getId()) {
            return PhoneType.getPhoneTypeListAsDictionary().get(PhoneType.MOBILE.getId());
        } else if (communicationSubType == PhoneType.OTHER.getId()) {
            return PhoneType.getPhoneTypeListAsDictionary().get(PhoneType.OTHER.getId());
        } else if (communicationSubType == PhoneType.WORK.getId()) {
            return PhoneType.getPhoneTypeListAsDictionary().get(PhoneType.WORK.getId());
        }
        return null;
    }

    /**
     * method for getting email label
     *
     * @param communicationSubType
     * @return
     */
    public static String getEmailLabel(int communicationSubType) {
        if (communicationSubType == EmailType.OTHER.getId()) {
            return EmailType.getEmailTypeListAsDictionary().get(EmailType.OTHER.getId());
        } else if (communicationSubType == EmailType.PERSONAL.getId()) {
            return EmailType.getEmailTypeListAsDictionary().get(EmailType.PERSONAL.getId());
        } else if (communicationSubType == EmailType.WORK.getId()) {
            return EmailType.getEmailTypeListAsDictionary().get(EmailType.WORK.getId());
        }
        return null;
    }

    /**
     * method for getting social label
     *
     * @param communicationSubType
     * @return
     */
    public static String getSocialLabel(int communicationSubType) {
        if (communicationSubType == SocialMediaType.SKYPE.getId()) {
            return SocialMediaType.getSocialMediaTypeListAsDictionary().get(SocialMediaType.SKYPE.getId());
        } else if (communicationSubType == SocialMediaType.WHATS_APP.getId()) {
            return SocialMediaType.getSocialMediaTypeListAsDictionary().get(SocialMediaType.WHATS_APP.getId());
        } else if (communicationSubType == SocialMediaType.TWITTER.getId()) {
            return SocialMediaType.getSocialMediaTypeListAsDictionary().get(SocialMediaType.TWITTER.getId());
        } else if (communicationSubType == SocialMediaType.FACEBOOK.getId()) {
            return SocialMediaType.getSocialMediaTypeListAsDictionary().get(SocialMediaType.FACEBOOK.getId());
        } else if (communicationSubType == SocialMediaType.LINKED_IN.getId()) {
            return SocialMediaType.getSocialMediaTypeListAsDictionary().get(SocialMediaType.LINKED_IN.getId());
        }
        return null;
    }

    public static String getTime() {
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date().getTime());
    }


    public static String getTimeZone() {
        SimpleDateFormat sdf2 = new SimpleDateFormat("h:mm a");
        String time = (sdf2.format(new Date())).replace("AM", "am").replace("PM", "pm");
        return time;
    }

    public static boolean validateTimezone(String timeZone) {
        String[] timezoneListId = TimeZone.getAvailableIDs();
        for (int i = 0; i <= timezoneListId.length; i++) {
            if (timezoneListId[i].equalsIgnoreCase(timeZone)) {
                return true;
            }
        }
        return false;
    }

    public static String getTimeByTimeZone(String timeZone) {
        SimpleDateFormat sdf2 = new SimpleDateFormat("h:mm a");
        sdf2.setTimeZone(TimeZone.getTimeZone(timeZone));
        String time = (sdf2.format(new Date())).replace("AM", "am").replace("PM", "pm");
        return time;
    }

    public static String getDaybyTimeZone(String timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        return sdf.format(new Date().getTime());
    }

    public static Bitmap getBitmap(String picture) {
        byte[] decodedString = Base64.decode(picture, Base64.DEFAULT);
        Bitmap decodedByte = null;
        try {
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodedByte;
    }

    /**
     * @param context
     * @param title
     * @param message
     */
    public static void alertDialog(final Context context, String title, String message) {
        Spanned spannedMsg = actionBarTitle(message);
        Spanned spannedTitle = actionBarTitle(title);
        //Spanned spannedOK = actionBarTitle("OK");
        Spanned spannedOK = Html.fromHtml("<font face = \"helvetica\"  >" + "OK" + "</font>");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        // set title
        alertDialogBuilder.setTitle(spannedTitle);

        // set dialog message
        alertDialogBuilder
                .setMessage(spannedMsg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(EdApplication.HELVETICA_NEUE);
    }

    /**
     * @return int unique view id;
     */
    public static int getUniqueViewId() {
        UUID uuid = UUID.randomUUID();
        long highBits = uuid.getMostSignificantBits();
        long lowBits = uuid.getLeastSignificantBits();
        return (int) (highBits + lowBits);
    }

    /**
     * return start date in required formate
     *
     * @param context
     * @param startDate
     * @return
     */
    public static String startDateStringFormat(Context context, Date startDate) {
        String formattedDate = null;
        try {
            DateFormat fmt = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            formattedDate = fmt.format(startDate);
        } catch (Exception e) {
            LogConfigurer.error("Utils", "" + e);
            Utils.log(context, "Utils->" + e);
        }
        return formattedDate.toString();
    }

    /**
     * return birth date in required formate
     *
     * @param context
     * @param birthDate
     * @return
     */
    public static String birthDateStringFormat(Context context, Date birthDate) {
        String stringMonth = null;
        String day = null;
        try {
            stringMonth = (String) android.text.format.DateFormat.format("MMMM", birthDate); //Jun
            day = (String) android.text.format.DateFormat.format("d", birthDate); //20
        } catch (Exception e) {
            LogConfigurer.error("Utils", "" + e);
            Utils.log(context, "Utils->" + e);
        }
        return stringMonth + " " + day;
    }

    /**
     * return birth date in required formate
     *
     * @param context
     * @param birthDate
     * @return
     */
    public static String birthDateStringFormat1(Context context, Date birthDate) {
        String formattedDate = null;
        try {
            DateFormat fmt = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            formattedDate = fmt.format(birthDate);
        } catch (Exception e) {
            LogConfigurer.error("Utils", "" + e);
            Utils.log(context, "Utils->" + e);
        }
        return formattedDate.toString();
    }

    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * set alarm to sync data
     *
     * @param context for setting alarms
     */
    public static void setAlarm(Context context) {
        Intent intentAlarm;
        AlarmManager alarmManager;
        intentAlarm = new Intent(context, SyncAlarmReceiver.class);
        alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        GregorianCalendar todayDate = new GregorianCalendar();
        alarmManager
                .setInexactRepeating(AlarmManager.RTC_WAKEUP, todayDate.getTime()
                        .getTime(), SYNC_DURATION, PendingIntent.getBroadcast(
                        context, 1, intentAlarm,
                        PendingIntent.FLAG_UPDATE_CURRENT));
        Log.d("Alarm", "Alarm Scheduled, Service Started");
    }

    /**
     * method to cancel alarm
     *
     * @param context
     */
    public static void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarm = new Intent(context, SyncAlarmReceiver.class);
        alarmManager.cancel(PendingIntent.getBroadcast(context, 1,
                intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    /**
     * method for return color
     *
     * @param status
     * @return int
     */
    public static int getStatusColorbyName(Context context, int status) {

        switch (status) {
            case 5:
                return context.getApplicationContext().getResources().getColor(R.color.a);
            case 1:
                return context.getApplicationContext().getResources().getColor(R.color.b);
            case 6:
                return context.getApplicationContext().getResources().getColor(R.color.c);
            case 4:
                return context.getApplicationContext().getResources().getColor(R.color.d);
            case 3:
                return context.getApplicationContext().getResources().getColor(R.color.e);
            case 2:
                return context.getApplicationContext().getResources().getColor(R.color.f);
            case 7:
                return context.getApplicationContext().getResources().getColor(R.color.g);
            default:
                return context.getResources().getColor(R.color.red);
        }
    }

    public static Date getDateObjFromStringDateTime(String format, String date, String time) {
        Date dateObj = null;
        StringBuffer sb = new StringBuffer();
        sb.append(date);
        sb.append(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
//            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            dateObj = simpleDateFormat.parse(sb.toString());
        } catch (ParseException e) {
            LogConfigurer.error("Utils", "" + e);
            Log.d("Utils", "Utils->" + e);
        }
        return dateObj;
    }

    public static String getDateTimeStringFromdateObj(String format, Date date) {
        String strDate = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            strDate = simpleDateFormat.format(date);
        } catch (Exception e) {
            LogConfigurer.error("Utils", "" + e);
            Log.d("Utils", "Utils->" + e);
        }
        return strDate;
    }

    public static Date getDateObjFromStringDateTime(String format, String datetime) {
        Date dateObj = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            dateObj = simpleDateFormat.parse(datetime);
        } catch (ParseException e) {
            LogConfigurer.error("Utils", "" + e);
            Log.d("Utils", "Utils->" + e);
        }
        return dateObj;
    }

    /**
     * checks whether an employee already exits in other department or location
     *
     * @param employeeQuickView employee view data
     * @return boolean
     */
    public static boolean alreadyThere(String type, EmployeeQuickView employeeQuickView, List<String> removedEmployeesId) {
        String value = "";
        if (removedEmployeesId.size() > 0) {
            if (!removedEmployeesId.contains(employeeQuickView.getEmployeeId().toString())) {
                if (Utils.INFO_TYPE_DEPARTMENT.equals(type)) {
                    value = employeeQuickView.getMoreDataDict().get("DepartmentId");
                } else if (Utils.INFO_TYPE_LOCATION.equals(type)) {
                    value = employeeQuickView.getMoreDataDict().get("LocationId");
                }
                if (!"".equals(value) && value.length() == 36) {
                    UUID id = UUID.fromString(value);
                    return !com.eworkplaceapps.platform.utils.Utils.emptyUUID().equals(id);
                }
            }
            return false;
        } else {
            if (Utils.INFO_TYPE_DEPARTMENT.equals(type)) {
                value = employeeQuickView.getMoreDataDict().get("DepartmentId");
            } else if (Utils.INFO_TYPE_LOCATION.equals(type)) {
                value = employeeQuickView.getMoreDataDict().get("LocationId");
            }
            if (!"".equals(value) && value.length() == 36) {
                UUID id = UUID.fromString(value);
                return !com.eworkplaceapps.platform.utils.Utils.emptyUUID().equals(id);
            }
            return false;
        }
    }

    /**
     * @return
     */
    public static int getProfileImageColor() {
        return Color.rgb(220, 220, 220);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        String path;
        if (inImage != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }
        return null;
    }

    public static float getDIPFromPixel(Context context, float dimenInPixel) {
        Resources res = context.getResources();
        float value = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimenInPixel, res.getDisplayMetrics());
        return value;
    }

    public static int getPxToDp(Context context, float dimenInPixel) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(dimenInPixel / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    /**
     * onFollowUpClick intent for follow ups
     *
     * @param type
     * @param employeeQuickView
     * @return
     */
    public static Intent getRefreshIntent(String type, EmployeeQuickView employeeQuickView) {
        Intent intent = new Intent(type);
        intent.putExtra(Utils.INFO_TYPE, REFRESH);
        intent.putExtra(Utils.MESSAGE, employeeQuickView);
        return intent;
    }

    /**
     * disable action bar animation using reflection
     *
     * @param actionBar
     */
    public static void disableABCShowHideAnimation(ActionBar actionBar) {
        try {
            actionBar.getClass().getDeclaredMethod("setShowHideAnimationEnabled", boolean.class).invoke(actionBar, false);
        } catch (Exception exception) {
            try {
                Field mActionBarField = actionBar.getClass().getSuperclass().getDeclaredField("mActionBar");
                mActionBarField.setAccessible(true);
                Object icsActionBar = mActionBarField.get(actionBar);
                Field mShowHideAnimationEnabledField = icsActionBar.getClass().getDeclaredField("mShowHideAnimationEnabled");
                mShowHideAnimationEnabledField.setAccessible(true);
                mShowHideAnimationEnabledField.set(icsActionBar, false);
                Field mCurrentShowAnimField = icsActionBar.getClass().getDeclaredField("mCurrentShowAnim");
                mCurrentShowAnimField.setAccessible(true);
                mCurrentShowAnimField.set(icsActionBar, null);
                //icsActionBar.getClass().getDeclaredMethod("setShowHideAnimationEnabled", boolean.class).invoke(icsActionBar, false);
            } catch (Exception e) {
                //....
            }
        }
    }

//    public static boolean isValidEmailId(String email) {
//        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
//    }

    /**
     * method for getting access list to get role's of employee
     */
    public static boolean employeeIsAdmin(Activity activity, UUID entityId, UUID userId, UUID tenantId) {
        boolean isAdmin = false;
        EmployeeAccess employeeAccess = new EmployeeAccess();
        try {
            if (EwpSession.getSharedInstance().isAccountAdmin()) {
                return true;
            }
//            boolean rolesArr[] = employeeAccess.accessList(entityId, userId, tenantId);
//
//            for (int i = 0; i < rolesArr.length; i++) {
//                if (rolesArr[i]) {
//                    isAdmin = true;
//                } else {
//                    isAdmin = false;
//                    break;
//                }
//            }

        } catch (Exception e) {
            Utils.log(activity, "ProfileActivity Exception-> " + e);
        }
        return isAdmin;
    }

    /**
     * method for change color of status bar (only for kitkat devices)
     *
     * @param activity
     * @param statusBar
     * @param color
     * @param shouldActionBarHeight
     */
    public static void setStatusBarColor(Activity activity, View statusBar, int color, boolean shouldActionBarHeight) {
        Window w = activity.getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //status bar height
        int actionBarHeight = getActionBarHeight(activity);
        int statusBarHeight = getStatusBarHeight(activity);
        //action bar height
        if (shouldActionBarHeight) {
            int bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, activity.getResources().getDisplayMetrics());
            statusBar.getLayoutParams().height = actionBarHeight + statusBarHeight + bottomMargin;
        } else {
            statusBar.getLayoutParams().height = statusBarHeight;
        }

        statusBar.setBackgroundColor(color);
    }

    /**
     * method to get action bar height
     *
     * @param activity
     * @return
     */
    public static int getActionBarHeight(Activity activity) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    /**
     * method to get status bar height
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * To get the date-time string of status to display
     *
     * @param item
     * @param isSystemPeriod
     * @return
     */
    public static String generateDateDisplayString(EmployeeQuickView item, boolean isSystemPeriod) {
        String startDate = item.getMoreDataDict().get("PeriodStartDate");
        String endDate = item.getMoreDataDict().get("PeriodEndDate");
        if (isSystemPeriod) {
            DateTimeHelper helper = new DateTimeHelper();

            String utcStartDateString = startDate.replace(" ", "T");
            String utcEndDateString = endDate.replace(" ", "T");

            Date periodStartDate = com.eworkplaceapps.platform.utils.Utils.dateFromString(utcStartDateString, false, false);
            Date periodEndDate = com.eworkplaceapps.platform.utils.Utils.dateFromString(utcEndDateString, false, false);

            int periodTimeCode = helper.getPeriodTimeCode(periodStartDate, periodEndDate);
            if (periodTimeCode == DateTimeHelper.allDay) {
                return "All day";
            } else if (periodTimeCode == DateTimeHelper.morning) {
                return "Morning";
            } else if (periodTimeCode == DateTimeHelper.afternoon) {
                return "Afternoon";
            }
        }
        String allDays = item.getMoreDataDict().get("AllDay");
        Date d;
        SimpleDateFormat df;
        d = com.eworkplaceapps.platform.utils.Utils.dateInDeviceTimeZoneFromString(startDate, false, false);
        if ("1".equalsIgnoreCase(allDays)) {
            df = new SimpleDateFormat(LIST_DISPLAY_DATE_FORMAT);
        } else {
            df = new SimpleDateFormat(LIST_DISPLAY_DATE_TIME_FORMAT);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(df.format(d).replace("AM", "am").replace("PM", "pm"));

        Date ed = com.eworkplaceapps.platform.utils.Utils.dateInDeviceTimeZoneFromString(endDate, false, false);
        if ("1".equalsIgnoreCase(allDays)) {
            df = new SimpleDateFormat(LIST_DISPLAY_DATE_FORMAT);
        } else {
            df = new SimpleDateFormat(LIST_DISPLAY_DATE_TIME_FORMAT);
        }
        if ("1".equalsIgnoreCase(allDays)) {
            if (df.format(ed).compareTo(df.format(d)) != 0) {
                sb.append("\n");
                sb.append(df.format(ed).replace("AM", "am").replace("PM", "pm"));
            }
        } else {
            sb.append("\n");
            sb.append(df.format(ed).replace("AM", "am").replace("PM", "pm"));
        }

        return sb.toString();
    }

    /*
 *   This method checks that app is installed on device or not
 *   @param uri
 */
    public static boolean checkAppInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    /**
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    public static <T> Collection<T> intersect(Collection<? extends T> a, Collection<? extends T> b) {
        Collection<T> result = new ArrayList<T>();

        for (T t : a) {
            if (b.remove(t)) result.add(t);
        }

        return result;
    }

    /**
     * dialog sheet of contacts/set photo option
     *
     * @param context
     * @param adapter
     * @param itemClickListener
     * @param cancelListener
     * @param cancelClickListener
     */
    public static void openDialogSheet(Context context, BaseAdapter adapter, AdapterView.OnItemClickListener itemClickListener, DialogInterface.OnCancelListener cancelListener, View.OnClickListener cancelClickListener) {

        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dialog_list, null);
        ListView listView = (ListView) view.findViewById(R.id.list);
        TextView cancel = (TextView) view.findViewById(R.id.id_cancel_text);
        cancel.setTypeface(EdApplication.HELVETICA_NEUE);

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(itemClickListener);
        cancel.setOnClickListener(cancelClickListener);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnCancelListener(cancelListener);
        // alertDialog.get
        Utils.alertDialog = alertDialog;

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.windowAnimations = R.style.PauseDialogAnimation;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        alertDialog.show();
    }

    /**
     * method to perform sorting for employee list
     */
    public static List<EmployeeQuickView> performSorting(List<EmployeeQuickView> employeeQuickViews) {
        Collections.sort(employeeQuickViews, new NoCaseComparator());
        List<EmployeeQuickView> dataList = new ArrayList<EmployeeQuickView>(employeeQuickViews);
        Pattern numberPattern = Pattern.compile(Utils.NUMBER_PATTERN);
        Pattern specialCharacterPattern = Pattern.compile(Utils.SPECIALCHARACTER_PATTERN);
        List<EmployeeQuickView> numList = new ArrayList<EmployeeQuickView>();

        for (EmployeeQuickView emp : dataList) {
            String fl = String.valueOf(emp.getFirstName().charAt(0));
            if (numberPattern.matcher(fl).matches() || specialCharacterPattern.matcher(fl).matches()) {
                numList.add(emp);
                employeeQuickViews.remove(emp);
            } else {
                break;
            }
        }
        employeeQuickViews.addAll(numList);
        return employeeQuickViews;
    }

    /**
     * method to perform sorting for group items
     *
     * @param childItems
     * @return
     */
    public static List<BaseFragment.ChildItem> performSortingForGroup(List<BaseFragment.ChildItem> childItems) {
        List<EmployeeQuickView> employeeQuickViews = new ArrayList<EmployeeQuickView>();
        List<BaseFragment.ChildItem> sortedChildItems = new ArrayList<>();
        for (int i = 0; i < childItems.size(); i++) {
            employeeQuickViews.add(childItems.get(i).employeeQuickView);
        }
        Collections.sort(employeeQuickViews, new NoCaseComparator());
        List<EmployeeQuickView> dataList = new ArrayList<EmployeeQuickView>(employeeQuickViews);
        Pattern numberPattern = Pattern.compile(Utils.NUMBER_PATTERN);
        Pattern specialCharacterPattern = Pattern.compile(Utils.SPECIALCHARACTER_PATTERN);
        List<EmployeeQuickView> numList = new ArrayList<EmployeeQuickView>();

        for (EmployeeQuickView emp : dataList) {
            String fl = String.valueOf(emp.getFirstName().charAt(0));
            if (numberPattern.matcher(fl).matches() || specialCharacterPattern.matcher(fl).matches()) {
                numList.add(emp);
                employeeQuickViews.remove(emp);
            } else {
                break;
            }
        }
        employeeQuickViews.addAll(numList);
        for (int j = 0; j < employeeQuickViews.size(); j++) {
            BaseFragment.ChildItem childItem = new BaseFragment.ChildItem();
            childItem.employeeQuickView = employeeQuickViews.get(j);
            sortedChildItems.add(childItem);
        }
        return sortedChildItems;
    }

    public static String calculateTimeDiff(String loggedInUserTime, String ProfileViewedUserTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date1 = null, date2 = null;
        int days = 0, hours = 0, min = 0;
        try {
            date1 = simpleDateFormat.parse(loggedInUserTime);
            date2 = simpleDateFormat.parse(ProfileViewedUserTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = date2.getTime() - date1.getTime();
        days = (int) (difference / (1000 * 60 * 60 * 24));
        hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        hours = (hours < 0 ? -hours : hours);
        Log.i("======= Hours", " :: " + hours);
        return "" + hours;
    }

    public static String getDiffBetTimeZone(String loggedInUserTimeZone, String viewdUserTimeZone) {
        int days = 0, hours = 0, min = 0;
        String totalDiff = null;
        TimeZone tz1 = TimeZone.getTimeZone(loggedInUserTimeZone);
        TimeZone tz2 = TimeZone.getTimeZone(viewdUserTimeZone);
        long timeDifference = tz1.getRawOffset() - tz2.getRawOffset() + tz1.getDSTSavings() - tz2.getDSTSavings();
        String signIs = checkSign(timeDifference);
        days = (int) (timeDifference / (1000 * 60 * 60 * 24));
        hours = (int) ((timeDifference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        min = (int) (timeDifference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        hours = (hours < 0 ? -hours : hours);
        Log.i("======= Hours", " :: " + hours);
        if (min >= 0 && min < 10) {
            totalDiff = hours + ":" + "0" + Math.abs(min);
        } else {
            totalDiff = hours + ":" + Math.abs(min);
        }
        if(totalDiff.equals("0:00")) return null;
        if ("positive".equals(signIs)) {
            return "+" + totalDiff;
        } else {
            return "-" + totalDiff;
        }
    }

    public static String checkSign(long number) {
        if (number == 0) return "positive";
        if (number >> 63 != 0) {
            return "negative";
        } else {
            return "positive";
        }
    }

}
