
//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Sourabh Agrawal
// Original DATE: 13/08/2015
//===============================================================================
package com.eworkplaceapps.platform.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeHelper {

    // Tuple.Tuple2<Date, Date, String> todaysDate;
    Tuple.Tuple2<Date, Date, String> todayMorningDate;
    Tuple.Tuple2<Date, Date, String> todayAfternoonDate;
    Tuple.Tuple2<Date, Date, String> todayAllDay;
    Tuple.Tuple2<Date, Date, String> tomorrowAllDay;
    Tuple.Tuple2<Date, Date, String> futureDate;
    Tuple.Tuple2<String, String, String> date, time;
    //-------------------- Begin Period Codes --------------------

    public static final int custom = 0;
    public static final int morning = 1;
    public static final int afternoon = 2;
    public static final int allDay = 3;
    private static final int calenderAllDay = 4;
    //---------------------- End Period Codes ---------------------

    //-------------------- Begin Period Groups --------------------

    public static final int today = 1;
    public static final int tomorrow = 2;
    public static final int future = 3;

    //---------------------- End Period Groups ---------------------
    public boolean fallingInbetweenCurrentTime(Date utcPeriodStartDate, Date utcPeriodEndDate, Date currentDatetime) {
        int value1 = Utils.compareDatesWithMinutes(utcPeriodStartDate, currentDatetime);
        int value2 = Utils.compareDatesWithMinutes(utcPeriodEndDate, currentDatetime);
        if (value1 <= 0 && value2 < 0) {
            return true;
        }
        if (value1 <= 0) {
            return true;
        }
        return false;
    }

    /// Gets the period group and code.
    /// utcPeriodStartDate: The local period start date.
    /// utcPeriodEndDate: The local period end date.
    /// returns The tuples period groups and period code.
    public Tuple.Tuple2<Integer, Integer, String> getPeriodGroupAndCode(Date utcPeriodStartDate, Date utcPeriodEndDate) {
        int groupCode = 0;
        int periodCode = 0;

        Tuple.Tuple2<Date, Date, String> todaysDate = getPeriod(today, allDay);
        Tuple.Tuple2<Date, Date, String> todaysDate2 = getPeriod(today, calenderAllDay);
        Tuple.Tuple2<Date, Date, String> tomorrowDate = getPeriod(tomorrow, allDay);
        Tuple.Tuple2<Date, Date, String> tomorrowDate2 = getPeriod(tomorrow, allDay);

        if (fallingInbetweenCurrentTime(utcPeriodStartDate, utcPeriodEndDate, todaysDate.getT2()) || fallingInbetweenCurrentTime(utcPeriodStartDate, utcPeriodEndDate, todaysDate2.getT2())) {
            // Today
            groupCode = today;
        } else if (fallingInbetweenCurrentTime(utcPeriodStartDate, utcPeriodEndDate, tomorrowDate.getT2()) || fallingInbetweenCurrentTime(utcPeriodStartDate, utcPeriodEndDate, tomorrowDate2.getT2())) {
            // Tomorrow
            groupCode = tomorrow;
        } else {
            // Future
            groupCode = future;
        }

        // Getting period code as per utc date.
        periodCode = getPeriodTimeCode(utcPeriodStartDate, utcPeriodEndDate);

        return new Tuple.Tuple2<Integer, Integer, String>(groupCode, periodCode, "");
    }

    /// Method is used to get datetime by passing Period day code and period time code.
    /// Period day code  are Today, Tomorrow, Future, Custom.
    /// Period time code are Morning, Yesterday, All Day, Custom.
    /// Time will be in following form
    /// Morning :   Start Time   " 08:00:00"   End Time  " 11:59:00"
    /// Afternoon:  Start Time " 12:00:00"    End Time  " 17:00:00"
    /// All day:       Start Time " 00:00:00"    End Time  " 23:59:00"
    /// For example
    /// Today day period and period time code is Allday, then date will be: 2015-08-18 00:00:00 - 2015-08-18 23:59:00
    /// Today day period and period time code is Morning, then date will be: 2015-08-18 08:00:00 - 2015-08-18 11:59:00
    /// Today day period and period time code is Afternoon, then date will be: 2015-08-18 12:00:00 - 2015-08-18 17:00:00
    public Tuple.Tuple2<Date, Date, String> getPeriod(int groupCode, int periodCode) {
        date = getPeriodDateByPeriodGroup(groupCode);
        time = getPeriodTimeByPeriodCode(periodCode);

        String s = (date.getT1() + time.getT1());
        String e = (date.getT2() + time.getT2());

        String utcStart = Utils.getUTCDateTimeAsString(Utils.dateFromString(s, false, false));
        String utcEnd = Utils.getUTCDateTimeAsString(Utils.dateFromString(e, false, false));

        Date utcDate = Utils.dateFromString(utcStart, false, false);
        Date utcEndDate = Utils.dateFromString(utcEnd, false, false);

        return new Tuple.Tuple2<Date, Date, String>(utcDate, utcEndDate, "");
    }

    public Tuple.Tuple2<String, String, String> getPeriodDateByPeriodGroup(int groupCode) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        switch (groupCode) {
            case tomorrow:
                try {
                    date = dateFormat.parse(strDate);
                    strDate = dateFormat.format(Utils.addDays(date, 1));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case future:
                try {
                    date = dateFormat.parse(strDate);
                    strDate = dateFormat.format(Utils.addDays(date, 2));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return new Tuple.Tuple2<String, String, String>(strDate, strDate, "");

    }

    /// Method is used to get time as per period time code.
    /// Period time code can be Morning, Yesterday, All Day, Custom.
    /// It will return the start and end time.
    /// Time will be in following form
    /// Morning :   Start Time   " 08:00:00"   End Time  " 11:59:00"
    /// Afternoon:  Start Time " 12:00:00"    End Time  " 17:00:00"
    /// All day:       Start Time " 00:00:00"    End Time  " 23:59:00"
    public Tuple.Tuple2<String, String, String> getPeriodTimeByPeriodCode(int periodCode) {
        String s = "T08:00:00.000";
        String e = "T17:00:00.000";
        switch (periodCode) {
            case morning:
                s = "T08:00:00.000";
                e = "T12:00:00.000";
                break;
            case afternoon:
                s = "T12:00:00.000";
                e = "T17:00:00.000";
                break;
            case calenderAllDay:
                s = "T00:00:00.000";
                e = "T23:59:00.000";
                break;
            default:
                break;
        }
        return new Tuple.Tuple2<String, String, String>(s, e, "");
    }

    /// Method is used to het period time code by passing UTC date.
    /// It will return Period time code. Period time code can be Morning, Yesterday, All Day, Custom.
    public int getPeriodTimeCode(Date utcPeriodStartDate, Date utcPeriodEndDate) {
        //Today morning
        if (todayMorningDate == null)
            todayMorningDate = getPeriod(today, morning);
        int value1 = Utils.compareDatesWithMinutes(utcPeriodStartDate, todayMorningDate.getT1());
        int value2 = Utils.compareDatesWithMinutes(utcPeriodEndDate, todayMorningDate.getT2());
        if (value1 == 0 && value2 == 0) {
            return morning;
        }

        //today afternoon
        if (todayAfternoonDate == null)
            todayAfternoonDate = getPeriod(today, afternoon);
        value1 = Utils.compareDatesWithMinutes(utcPeriodStartDate, todayAfternoonDate.getT1());
        value2 = Utils.compareDatesWithMinutes(utcPeriodEndDate, todayAfternoonDate.getT2());
        if (value1 == 0 && value2 == 0) {
            return afternoon;
        }

        //today allDay
        if (todayAllDay == null)
            todayAllDay = getPeriod(today, allDay);
        value1 = Utils.compareDatesWithMinutes(utcPeriodStartDate, todayAllDay.getT1());
        value2 = Utils.compareDatesWithMinutes(utcPeriodEndDate, todayAllDay.getT2());
        if (value1 == 0 && value2 == 0) {
            return allDay;
        }

        todayAllDay = getPeriod(today, calenderAllDay);
        value1 = Utils.compareDatesWithMinutes(utcPeriodStartDate, todayAllDay.getT1());
        value2 = Utils.compareDatesWithMinutes(utcPeriodEndDate, todayAllDay.getT2());

        // custom dates
        if (value1 <= 0 && (value2 >= 0 || value2 < 0)) {
            return custom;
        }

        // Tomorrow
        if (tomorrowAllDay == null)
            tomorrowAllDay = getPeriod(tomorrow, allDay);
        value1 = Utils.compareDatesWithMinutes(utcPeriodStartDate, tomorrowAllDay.getT1());
        value2 = Utils.compareDatesWithMinutes(utcPeriodEndDate, tomorrowAllDay.getT2());
        if (value1 >= 0 && value2 <= 0) {
            return allDay;
        }

        todayAllDay = getPeriod(tomorrow, calenderAllDay);
        value1 = Utils.compareDatesWithMinutes(utcPeriodStartDate, todayAllDay.getT1());
        value2 = Utils.compareDatesWithMinutes(utcPeriodEndDate, todayAllDay.getT2());

        // custom dates
        if (value1 <= 0 && (value2 >= 0 || value2 < 0)) {
            return custom;
        }

        /*todaysDate = getPeriod(tomorrow, morning);
        value1 = Utils.compareDatesWithMinutes(utcPeriodStartDate, todaysDate.getT1());
        value2 = Utils.compareDatesWithMinutes(utcPeriodEndDate, todaysDate.getT2());

        if (value1 >= 0 && value2 == 0) {
            return morning;
        }

        // Tomorrow morning
        todaysDate = getPeriod(tomorrow, afternoon);
        value1 = Utils.compareDatesWithMinutes(utcPeriodStartDate, todaysDate.getT1());
        value2 = Utils.compareDatesWithMinutes(utcPeriodEndDate, todaysDate.getT2());
        if (value1 >= 0 && value2 == 0) {
            return afternoon;
        }*/

        // Tomorrow allDay


        /*if (value1 <= 0 && (value2 >= 0 || value2 < 0)) {
            return custom;
        }

        // future
        todaysDate = getPeriod(future, morning);
        value1 = Utils.compareDatesWithMinutes(utcPeriodStartDate, todaysDate.getT1());
        value2 = Utils.compareDatesWithMinutes(utcPeriodEndDate, todaysDate.getT1());

        if (value1 == 0 && value2 == 0) {
            return morning;
        }

        todaysDate = getPeriod(future, afternoon);
        value1 = Utils.compareDatesWithMinutes(utcPeriodStartDate, todaysDate.getT1());
        value2 = Utils.compareDatesWithMinutes(utcPeriodEndDate, todaysDate.getT2());
        if (value1 == 0 && value2 == 0) {
            return afternoon;
        }

        if (futureDate == null)
            futureDate = getPeriod(future, allDay);
        value1 = Utils.compareDatesWithMinutes(utcPeriodStartDate, futureDate.getT1());
        value2 = Utils.compareDatesWithMinutes(utcPeriodEndDate, futureDate.getT2());
        if (value1 == 0 && value2 == 0) {
            return allDay;
        }*/

        return custom;
    }

}
