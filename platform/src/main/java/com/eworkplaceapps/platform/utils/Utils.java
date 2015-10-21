//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/15/2015
//===============================================================================
package com.eworkplaceapps.platform.utils;


import android.content.Context;
import android.net.ParseException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.androidquery.AQuery;
import com.eworkplaceapps.platform.exception.EwpException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The Utils class contains all the utility methods that are used across the project.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,64}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,126}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,62}" +
                    ")+"
    );

    /**
     * The method is used to split the string based on provided delimiter.
     * It then return the list of string obtained after splitting the string.
     *
     * @param script    STRING that needs to be split
     * @param delimeter Delimiter that is used to split the string
     * @return List of string that are obtained by splitting the string.
     */
    public static List<String> splitSqlScript(String script, char delimeter) {
        List<String> statements = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        boolean inLiteral = false;
        char[] content = script.toCharArray();
        for (int i = 0; i < script.length(); i++) {
            if (content[i] == '"') {
                inLiteral = !inLiteral;
            }
            if (content[i] == delimeter && !inLiteral) {
                if (sb.length() > 0) {
                    statements.add(sb.toString().trim());
                    sb = new StringBuilder();
                }
            } else {
                sb.append(content[i]);
            }
        }
        if (sb.length() > 0) {
            statements.add(sb.toString().trim());
        }
        return statements;
    }

    /**
     * @param in   InputStream
     * @param outs OutputStream
     * @throws IOException
     */
    public static void writeExtractedFileToDisk(InputStream in, OutputStream outs) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            outs.write(buffer, 0, length);
        }
        outs.flush();
        outs.close();
        in.close();
    }

    /**
     * convert file into zip
     *
     * @param zipFileStream
     * @return ZipInputStream
     * @throws IOException
     */
    public static ZipInputStream getFileFromZip(InputStream zipFileStream) throws IOException {
        ZipInputStream zis = new ZipInputStream(zipFileStream);
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            Log.w(TAG, "extracting file: '" + ze.getName() + "'...");
            return zis;
        }
        return null;
    }

    /**
     * It get the file stream as input and return the contents in form of STRING.
     *
     * @param is InputStream of the source from where the contents needs to be read.
     * @return STRING of contents
     */
    public static String convertStreamToString(InputStream is) {
        return new Scanner(is).useDelimiter("\\A").next();
    }

    /**
     * Utility method to find if the given string is empty or null
     *
     * @param val STRING to be checked
     * @return true if the string is null or empty
     */
    public static boolean isEmptyOrNull(String val) {
        return val == null || "".equals(val);
    }


    /**
     * @param context Context
     * @return the unique IMEI for device
     */
    public static String getDeviceIMEI(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }

    /**
     * empty uuid
     *
     * @return empty UUID
     */
    public static UUID emptyUUID() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    /**
     * @param email string email
     * @return boolean if email is valid or not
     */
    public static boolean isValidEmail(final String email) {
//        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    /**
     * @param date
     * @return STRING from date
     */
    public static String stringFromDate(Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(date);
        return utcTime;
    }

    public static String dateAsStringWithoutUTC(Date d) {
        final SimpleDateFormat sdf = new SimpleDateFormat(Utils.DATE_FORMAT);
        final String utcTime = sdf.format(d);
        return utcTime;
    }

    public static Date getUTCDateTimeAsDate() {
        return dateFromString(getUTCDateTimeAsString(), true, true);
    }

    public static String getUTCDateTimeAsString() {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());
        return utcTime;
    }

    public static String getUTCDateTimeAsString(Date d) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(d);
        return utcTime;
    }

    public static Date dateFromString(String StrDate, boolean milliSec, boolean utc) {
        Date dateToReturn = null;
        if (StrDate!=null && !StrDate.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            //    dateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            try {
                dateToReturn = (Date) dateFormat.parse(StrDate);
            } catch (Exception e) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                //   dateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
                try {
                    dateToReturn = (Date) dateFormat.parse(StrDate);
                } catch (java.text.ParseException e1) {
                }
            }
        }
        return dateToReturn;
    }

    public static Date utcDateFromString(String StrDate) {
        Date dateToReturn = null;
        SimpleDateFormat utcDateParse = new SimpleDateFormat(DATE_FORMAT);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        try {
            utcDateParse.setTimeZone(TimeZone.getTimeZone("UTC"));
            //String s = utcDateParse.format(dateFormatter.parse(StrDate));
            dateToReturn = dateFormatter.parse(StrDate);
        } catch (Exception e) {
            utcDateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            try {
                dateToReturn = utcDateParse.parse(StrDate);
            } catch (java.text.ParseException e1) {
            }
        }
        return dateToReturn;
    }

    public static Date dateInDeviceTimeZoneFromString(String StrDate, boolean milliSec, boolean utc) {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
        try {
            dateToReturn = (Date) dateFormat.parse(StrDate);
        } catch (Exception e) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            dateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            try {
                dateToReturn = (Date) dateFormat.parse(StrDate);
            } catch (java.text.ParseException e1) {
            }
        }
        return dateToReturn;
    }

    /**
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> returnParams(String data)
            throws UnsupportedEncodingException {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("json", data));
        HttpEntity entity = new UrlEncodedFormEntity(pairs, "UTF-8");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(AQuery.POST_ENTITY, entity);
        return params;
    }

    /**
     * @param response
     * @return
     * @throws Exception
     */
    public static String convertResponseToString(HttpResponse response) {
        InputStream in = null;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            in = response.getEntity().getContent();
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String serverResponse = null;
            while ((serverResponse = reader.readLine()) != null) {
                sb.append(serverResponse);
            }
        } catch (IllegalStateException e) {

        } catch (IOException e) {

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            }
        }
        String str = sb.toString();
        return str;
    }

    public static Date dateFromUTCToLocal(String Strdate) {
        DateFormat localFormat = null;
        Date date = null;
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = utcFormat.parse(Strdate);
            return date;
        } catch (Exception e) {
        }
        return null;
    }

    // Compares the value of first NSDate instance to a second NSDate value
    // and returns an integer that indicates whether first instance is earlier than,
    // the same as, or later than the second System.DateTime value.
    // date1: The first date to be compare.
    // date2: The second date to compare.
    // A signed number indicating the relative values of first instance and the second parameter.
    // Value Description Less than zero means first instance is earlier than
    // value. Zero means first instance is the same as! second value.
    // Greater than zero means first instance is later than second value.
    public static int compareDatesWithMinutes(Date date1, Date date2) {

        /*String date1WithMinutesStr = Utils.dateAsStringWithoutUTC(date1);
        String date2WithMinutesStr = Utils.dateAsStringWithoutUTC(date2);

        Date date1WithMinutes = Utils.dateFromString(date1WithMinutesStr, false, true);
        Date date2WithMinutes = Utils.dateFromString(date2WithMinutesStr, false, true);*/

        if (date1.compareTo(date2) > 0) {
            // Date1 after date2
            return 1;
        } else if (date1.compareTo(date2) < 0) {
            // Date1 before date2
            return -1;
        }

        // 0 Menas! date are equal
        return 0;
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    /**
     * Method to split the address string into an array based on new line character '\n'.
     * @param address The address string which has to be split into array values.
     * @return
     */

    public static List<String> splitAddressIntoParts(String address)  {
        ArrayList<String> strAddress = new ArrayList<String>();
        if(address==null || address.equals("")) {
            ArrayList<String> list = new ArrayList<String>();
            list.add("");
            list.add("");
            list.add("");
            list.add("");
            return list;
        }
        String strArray[] = address.split("\\r?\\n");
        for (int i = 0; i < strArray.length; i++) {
            if (strArray[i]!=null && !strArray[i].toString().isEmpty()) {
                    strAddress.add(strArray[i]);
            }
        }
        for (int i = strArray.length;i<4; i++) {
            strAddress.add("");
        }

         return strAddress;
    }

    /**
     * Method to join the address array values into a string based on new line character '\n'.
     * @param addressParts The address array whose values are to be joined.
     * @return
     */
    public static String joinAddressParts(ArrayList<String> addressParts)  {
        String address = "";
        for (int i = 0; i < addressParts.size(); i++) {
            // Ignore if string is null or empty
            if (addressParts.get(i)!=null && !addressParts.get(i).isEmpty()) {
                if (!address.isEmpty()) {
                    address += "\n" +   addressParts.get(i);
                }
                else {
                    address += addressParts.get(i);
                }
            }
        }
        return address;
    }

}
