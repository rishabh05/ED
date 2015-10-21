package com.eworkplaceapps.platform.notification;

// Still to do:
// **** IN_TIME_UNITS -> TIME_SERIES
// Doc public methods
// **** Past results
// Rename mask last weekend or doc
//
// Timezone support
// Internationalization - different calendars
// Develop test cases
// Create unit tests
// EVENT based repetition -- I believe this will be implemented by
//  transforming event-based requirements in to the RepetitionRules
//  data. So it may not result in any updates here.

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/*
* This class defines repetition rules (for any action) and applies the rules to
* calculate when the next repetition trigger will occur at.
*
* There are two types of repetitions:
* 1. Ad-hoc -- The repetition times are listed. Select the next repetition time from the list.
* 2. CYCLIC -- The repetition occurs periodically according to some rule based on the calendar.
*
* The CYCLIC repetition type may be defined in the following ways:
* 1. TIME_SERIES -- repetition occurs at every so many units of time. Like, every 2 minutes
*    or every 5 days.
* 2. DAILY -- Occurs every day
*    Instead of every day it could be every <n>th day.
* 3. WEEKDAYS -- Occurs on defined days of every week. Like, every Monday and Thursday.
*    Instead of every week it could be every <n>th week.
* 4. MONTH_DAYS -- Occurs on defined days of a month. Like every 1st Monday, 3rd Tuesday etc.
*    Or 5th Friday or Last Thursday. The 5th day may not occur in a given month.
*    The last day may fall in the 4th or 5th week of a month.
*    Instead of every month it could be every <n>th month.
* 5. MONTH_DATES -- Occurs on defined dates of a month. Like 3, 10, 21 of a month. Or 31 of
*    of a month, which may not occur for some months. Or Last date of a month.
*    Instead of every month it could be every <n>th month.
*
* The repetition period is determined by Start and End dates of the period. Any date result
* beyond the End date is forced to be nil.
*
* The trigger days are calculated in sequence of calls: firstTriggerAt call first,
* and then repeatedly nextTriggerAt with lastTriggerAt parameter calls. It is
* important to use this pattern to get correct results. If the parameter lastTriggerAt
* is incorrect, the results will be incorrect.
*
* For cyclical types, the repetition calculations are performed in two parts: FIRST calculate
* on which days/dates the repetition occurs, then, in the second part, attach the times of
* the day. The user specifies the times of the day. If none is specified, the repetition
* does not occur.
*
* The calendar calculations require the use of user's calendar. By default it is set to the
* system's current calendar for the login user.  ** TBD **
*
* Internally the dates are kept as UTC dates. Where the user provides dates or receives date
* results, dates are converted from or to in user's timezone.  ** TBD **
*
* Internationalization - ** TBD **
* This implementation is based on following assertions:
* (1) A year is divided in to months. A quarter is three months long.
* (2) There may be any number of full or partial months in a year.
* (3) A month may have any number days greater than 7 days (one week).
* (4) A month may have any number of full or partial weeks.
* (5) A week is always 7 days long.
*
* Weekday Ordinals: The class assigns ordinal numbers to weekdays used in the calculations
* and in positioning mask bits. The assigned ordinals are:
* Sunday -    1
* Monday -    2
* Tuesday -   3
* Wednesday - 4
* Thursday -  5
* Friday -    6
* Saturday -  7
*
*/
public class RepetitionRules {

    // ---- Enums ---------------------------------

    // REPETITION type
    public enum RepetitionType {
        AD_HOC(0),
        CYCLIC(1);
        private int id;

        RepetitionType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    // Types of Cyclical repetitions
    public enum CyclicalType {
        TIME_SERIES(0),
        DAILY(1),
        WEEKDAYS(2),
        MONTH_DAYS(3),
        MONTH_DATES(4);
        private int id;

        CyclicalType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    // Time unit list
    public enum TimeUnit {
        SECOND(0),
        MINUTE(1),
        HOUR(2),
        DAY(3),
        WEEK(4),
        MONTH(5),
        QUARTER(6),
        YEAR(7);

        private int id;

        TimeUnit(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    // ---- Constants ---------------------------------

    /*
    * Weekday cyclical type uses a mask to set which weekdays to use. The mask positions
    * are by Weekday ordinals as defined above from right to left in the mask value.
    */
    private int sundayMask = 1;
    private int mondayMask = 1 << 1;
    private int tuesdayMask = 1 << 2;
    private int wednesdayMask = 1 << 3;
    private int thursdayMask = 1 << 4;
    private int fridayMask = 1 << 5;
    private int saturdayMask = 1 << 6;

    /*
    * MONTH_DATES cyclical type uses a mask to set which dates of the month to use.
    * The mask for a date is calculated as (1 << date-1). The date 1 mask is 1.
    * The Last date mask bit position is 32.
    */
    private int lastMonthDateMaskPosition = 32;
    private int lastMonthDateMask = 1 << 31; // Position 32

    /*
    * MONTH_DAYS cyclical type mask uses a mask to set which days of the month to use.
    * It is partitioned in to days. Each day uses 6 positions: 1st, 2nd, 3rd, 4th,
    * 5th and Last week. Weekday and Weekend blocks follow the seven days of the week.
    */

    private int firstSundayOfMonthMask = 1;
    private int secondSundayOfMonthMask = 1 << 1;
    private int thirdSundayOfMonthMask = 1 << 2;
    private int fourthSundayOfMonthMask = 1 << 3;
    private int fifthSundayOfMonthMask = 1 << 4;
    private int lastSundayOfMonthMask = 1 << 5;

    private int firstMondayOfMonthMask = 1 << 6;
    private int secondMondayOfMonthMask = 1 << 7;
    private int thirdMondayOfMonthMask = 1 << 8;
    private int fourthMondayOfMonthMask = 1 << 9;
    private int fifthMondayOfMonthMask = 1 << 10;
    private int lastMondayOfMonthMask = 1 << 11;

    private int firstTuesdayOfMonthMask = 1 << 12;
    private int secondTuesdayOfMonthMask = 1 << 13;
    private int thirdTuesdayOfMonthMask = 1 << 14;
    private int fourthTuesdayOfMonthMask = 1 << 15;
    private int fifthTuesdayOfMonthMask = 1 << 16;
    private int lastTuesdayOfMonthMask = 1 << 17;

    private int firstWednesdayOfMonthMask = 1 << 18;
    private int secondWednesdayOfMonthMask = 1 << 19;
    private int thirdWednesdayOfMonthMask = 1 << 20;
    private int fourthWednesdayOfMonthMask = 1 << 21;
    private int fifthWednesdayOfMonthMask = 1 << 22;
    private int lastWednesdayOfMonthMask = 1 << 23;

    private int firstThursdayOfMonthMask = 1 << 24;
    private int secondThursdayOfMonthMask = 1 << 25;
    private int thirdThursdayOfMonthMask = 1 << 26;
    private int fourthThursdayOfMonthMask = 1 << 27;
    private int fifthThursdayOfMonthMask = 1 << 28;
    private int lastThursdayOfMonthMask = 1 << 29;

    private int firstFridayOfMonthMask = 1 << 30;
    private int secondFridayOfMonthMask = 1 << 31;
    private int thirdFridayOfMonthMask = 1 << 32;
    private int fourthFridayOfMonthMask = 1 << 33;
    private int fifthFridayOfMonthMask = 1 << 34;
    private int lastFridayOfMonthMask = 1 << 35;

    private int firstSaturdayOfMonthMask = 1 << 36;
    private int secondSaturdayOfMonthMask = 1 << 37;
    private int thirdSaturdayOfMonthMask = 1 << 38;
    private int fourthSaturdayOfMonthMask = 1 << 39;
    private int fifthSaturdayOfMonthMask = 1 << 40;
    private int lastSaturdayOfMonthMask = 1 << 41;

    private int firstWeekdayOfMonthMask = 1 << 42;
    private int secondWeekdayOfMonthMask = 1 << 43;
    private int thirdWeekdayOfMonthMask = 1 << 44;
    private int fourthWeekdayOfMonthMask = 1 << 45;
    private int fifthWeekdayOfMonthMask = 1 << 46;
    private int lastWeekdayOfMonthMask = 1 << 47;

    private int firstWeekendOfMonthMask = 1 << 48;
    private int secondWeekendOfMonthMask = 1 << 49;
    private int thirdWeekendOfMonthMask = 1 << 50;
    private int fourthWeekendOfMonthMask = 1 << 51;
    private int fifthWeekendOfMonthMask = 1 << 52;
    private int lastWeekendOfMonthMask = 1 << 53;

    // For MonthDay cyclical type we need to know what days are considered to be weekends.
    // By default, they are Sunday and Saturday.
    // If weekend day is not used (one or both) use 0.
    private int weekendDayOrdinal1 = 1;  // Sunday
    private int weekendDayOrdinal2 = 7; // Saturday

    // The calendar to use
    private Calendar theCalendar;

    // Two bookend dates for the rules
    private Date startDate;
    private Date endDate;

    private RepetitionType repetitionType = RepetitionType.AD_HOC;

    // Cyclical fields
    private CyclicalType cyclicalType = CyclicalType.TIME_SERIES;
    private TimeUnit cyclicalTimeUnit = TimeUnit.HOUR;
    // Every <cyclicalInternal> (<n>) period. Applicable to all
    // cyclical types.
    private int cyclicalInterval = 0;
    // Mask used for Weekly, MONTH_DATES and MONTH_DAYS types.
    private long cyclicalDayMask = 0;

    /*
    * TriggerPartList is an array of NSDate. It is used in two different ways:
    * (1) AD_HOC repetition: It stores the date/time when the repetition is to occur.
    * (2) Cyclical repetition: It stores the times of the day when repetition is to occur.
    * This array is ALWAYS sorted. For case (2) it uses the same date for all items
    * so that the array will sort correctly by time part.
    *
    * Since the array is always sorted, a helper method is provided to add an element
    * to the array. That is safe to use instead of setting the array outside.
    * For case (2) the helper method uses the base date 1/1/2000.
    */

    private List<Date> triggerPartList;

    /// Adds a trigger date to the array and then sorts the array. For Cyclical type,
    /// it forces the date to be 1/1/2000.
    ///
    /// :param: triggerPart The trigger date/time value to be added
    /// :param: timeOnly The trigger part is used for time part only. The reference date of
    ///   1/1/2000 is used in the date part.
    public void addTriggerPart(Date triggerPart, Boolean timeOnly) {
        if (timeOnly) {
            // Force the date to 1/1/2000
            Calendar cal = Calendar.getInstance();
            cal.setTime(triggerPart);
            // comps.year = 2000
            cal.set(Calendar.YEAR, 2000);
            // comps.month = 1
            cal.set(Calendar.MONTH, 1);
            // comps.day = 1
            cal.set(Calendar.DAY_OF_YEAR, 1);
            triggerPart = cal.getTime();

        }
        // ADD and sort
        triggerPartList.add(triggerPart);
        Collections.sort(triggerPartList);
    }

    public RepetitionRules() {
        // Use the system defined current calendar
        theCalendar = Calendar.getInstance();//.currentCalendar()
        // StartDate = Now
        startDate = new Date();
        // EndDate = Far in future
        theCalendar.add(Calendar.YEAR, 100);// Adding 100 years to current date
        endDate = theCalendar.getTime();
        triggerPartList = new ArrayList<>();
    }

    /// Call this method to get the first trigger. This will handle the special case
    /// of the first time differently for different cyclical types.
    /// The reason the first time is different than other times is that in the case of next
    /// trigger the calculated value is ALWAYS later than the last trigger.
    /// For some types the first time is obviously StartDate, like for TIME_SERIES type.
    /// For other times the first time is >= StartDate because we pick the earliest time
    /// for which the rules are met and that may not be the earliest time. E.g.,
    /// StartDate = 8/8/2014, which is a Friday. The Weekday rule may be on Saturdays. So
    /// the first date instance for this will be 8/9/2014. There are many similar cases.
    /// @returns FIRST trigger date. Nil if none exists.
    public Date firstTriggerAt() {
        Date trigger = null;

        switch (repetitionType) {
            case AD_HOC:
                trigger = this.firstAdhocTriggerAt();
                break;
            case CYCLIC:
                trigger = this.firstCyclicTriggerAt();
                break;
            default:
                break;
        }

        // It is possible that trigger may be past endDate. Return nil
        if (trigger != null && endDate.after(trigger)) {
            trigger = null;
        }
        return trigger;
    }

    // The case of first trigger result requiring calculations is handled by making
    // nextRiggerAt call with lastTriggerAt = StartDate - 1 sec, so that if StartDate
    // matches any rule, the result (first trigger) will beStartDate. The 1 sec time
    // difference is assumed to be too small for any trigger to occur.

    private Date firstAdhocTriggerAt() {
        // Seed lastTriggerAt = StartDate - 1 sec

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.SECOND, -1);
        Date date = cal.getTime();

        Date trigger = nextAdhocTriggerAt(date);
        return trigger;
    }

    private Date firstCyclicTriggerAt() {
        Date trigger = null;

        switch (cyclicalType) {
            case TIME_SERIES:
                // Obviously StartDate
                trigger = startDate;
                break;
            case DAILY:
            case WEEKDAYS:
            case MONTH_DAYS:
            case MONTH_DATES:
                // Seed lastTriggerAt = StartDate - 1 sec
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.SECOND, -1);
                Date date = cal.getTime();

                trigger = nextCalendarCyclicTriggerAt(date);
                break;
            default:
                break;
        }

        return trigger;
    }

    /// Given the last trigger, calculate the next trigger date/time
    /// Aparam
    public Date nextTriggerAt(Date lastTriggerAt) {
        Date trigger = null;

        switch (repetitionType) {
            case AD_HOC:
                trigger = this.nextAdhocTriggerAt(lastTriggerAt);
                break;
            case CYCLIC:
                trigger = this.nextCyclicTriggerAt(lastTriggerAt);
                break;
            default:
                break;
        }

        // It is possible that triggerDateTime may be past endDate. Return nil
        if (trigger != null && endDate.after(trigger)) {
            trigger = null;
        }
        return trigger;
    }

    private Date nextAdhocTriggerAt(Date fromDate) {
        // If triggerPartList is empty, return nil
        if (triggerPartList.isEmpty()) {
            return null;
        }

        // Loop over triggerPartList and stop at the first item > lastTriggerAt.
        // Note that triggerPartList is sorted.
        for (Date trigger : triggerPartList) {
            if (fromDate.after(trigger)) {
                return trigger;
            }
        }

        // This means lastTriggerAt is past all trigger times. Return nil
        return null;
    }

    private Date nextCyclicTriggerAt(Date fromDate) {
        Date trigger = null;
        switch (cyclicalType) {
            case TIME_SERIES:
                trigger = nextTimeSeriesCyclicTriggerAt(fromDate);
                break;
            case DAILY:
            case WEEKDAYS:
            case MONTH_DAYS:
            case MONTH_DATES:
                trigger = nextCalendarCyclicTriggerAt(fromDate);
                break;
            default:
                break;
        }
        return trigger;
    }

    private Date nextTimeSeriesCyclicTriggerAt(Date fromDate) {
        // Setup time unit and time interval
        // We let NSCalendar and NSDateComponents do the heavy lifting.
        Calendar cal = Calendar.getInstance();
        switch (cyclicalTimeUnit) {
            case SECOND:
                cal.set(Calendar.SECOND, this.cyclicalInterval);
                break;
            case MINUTE:
                cal.set(Calendar.MINUTE, this.cyclicalInterval);
                break;
            case HOUR:
                cal.set(Calendar.HOUR, this.cyclicalInterval);
                break;
            case DAY:
                cal.set(Calendar.DAY_OF_YEAR, this.cyclicalInterval);
                break;
            case WEEK:
                cal.set(Calendar.WEEK_OF_YEAR, this.cyclicalInterval);
                break;
            case MONTH:
                cal.set(Calendar.MONTH, this.cyclicalInterval);
                break;
            // case QUARTER:
            //  cal.set(Calendar.,this.cyclicalInterval * 3);
            // break;
            case YEAR:
                cal.set(Calendar.YEAR, this.cyclicalInterval);
                break;
            default:
                break;
        }

        // ADD calendar components to lastTriggerDateTime in an NSCalendar
        Date trigger = cal.getTime();
        return trigger;
    }

    private Date nextCalendarCyclicTriggerAt(Date fromDate) {
        // If triggerPartList is empty, return nil
        if (triggerPartList.isEmpty()) {
            return null;
        }
        // This is done in two steps.
        // FIRST step: GET ALL possible triggers for the current period spanning the
        // givendate. In that list find the closest future date.
        // SECOND step: If the answer in the first step is nil, that means the given
        // date (fromDate) is the LAST trigger in its current period. So, in this
        // second step, increment the period by one unit (DAY, WEEK or MONTH depending
        // on the cyclical type), get ALL possible triggers for the future period and
        // then locate the closest future date as the next trigger.
        //
        // The above approach reuses the same code for both steps.

        Date trigger = null;
        List<Date> periodTriggers;

        // FIRST step: current period
        periodTriggers = periodCyclicTriggers(fromDate, true);
        trigger = matchFromDateTimeInTriggers(fromDate, periodTriggers);

        // trigger is nil
        if (trigger == null) {
            // SECOND step: next future period
            periodTriggers = periodCyclicTriggers(fromDate, false);
            trigger = matchFromDateTimeInTriggers(fromDate, periodTriggers);
        }
        return trigger;
    }

    // GET ALL possible triggers in a given period.
    private List<Date> periodCyclicTriggers(Date fromDate, Boolean currentPeriod) {
        List<Date> triggers = new ArrayList<Date>();

        // If not current period, first a date in the next future period.
        if (!currentPeriod) {
            fromDate = calculateNextPeriodDateTime(fromDate);
        }

        // GET all possible triggers in the given period
        switch (cyclicalType) {
            case DAILY:
                triggers = periodDailyTriggers(fromDate);
                break;
            case WEEKDAYS:
                triggers = periodWeekdayTriggers(fromDate);
                break;
            case MONTH_DATES:
                triggers = periodMonthDatesTriggers(fromDate);
                break;
            case MONTH_DAYS:
                triggers = periodMonthDaysTriggers(fromDate);
                break;
            default:
                break;
        }
        return triggers;
    }

    // Calculate next future period.
    // Return a date (any date) in the next future period
    private Date calculateNextPeriodDateTime(Date fromDate) {
        Date nextPeriodDateTime = new Date();

        // Break down fromDate in to its calendar components.

        // Setup the period unit as a calendar component. Take in t account
        // cyclicalInterval.

        Calendar cal = Calendar.getInstance();

        switch (cyclicalType) {
            case DAILY:
                cal.set(Calendar.DAY_OF_YEAR, cyclicalInterval);
                break;
            case WEEKDAYS:
                cal.set(Calendar.DAY_OF_YEAR, cyclicalInterval * 7);
                break;
            case MONTH_DATES:
            case MONTH_DAYS:
                cal.set(Calendar.MONTH, cyclicalInterval);
                break;
            default:
                break;
        }

        // Calculate next future period date by adding the period unit to fromDate
        // as calendar components and convert it to a date.
        nextPeriodDateTime = cal.getTime();

        //println("From: \(fromDate); Type: \(cyclicalType); Interval: \(cyclicalInterval); NextDateTime: \(nextPeriodDateTime)")

        return nextPeriodDateTime;
    }

    private List<Date> periodDailyTriggers(Date fromDate) {
        // This is pretty easy to do. The date is given (fromDate), so just
        // append time from triggerPartList. That is it.

        List<Date> triggers = new ArrayList<Date>();
        generateAndAppendTriggers(fromDate, triggers);

        //printTriggerList("periodDailyTriggers", triggers: triggers)

        return triggers;
    }

    private List<Date> periodWeekdayTriggers(Date fromDate) {
        List<Date> triggers = new ArrayList<>();

       /* // Calculate the weekday for fromDate.
        let flags: NSCalendarUnit = .WeekdayCalendarUnit
        var comps = theCalendar.components(flags, fromDate: fromDate)
        let fromDay = comps.weekday

        // cyclicalMask is a mask for the weekdays, 7 bits. Loop over
        // them and process the bits that are on
        var mask: UInt64 = 1

        for i in 1...7 {
            if cyclicalDayMask & mask != 0 {
                // This weekday has the bit on.
                // Calculate its delta in days from fromDate and then calculate
                // date for this day.
                let tComps = NSDateComponents()
                tComps.day = i - fromDay
                let date = theCalendar.dateByAddingComponents(tComps, toDate: fromDate, options: NSCalendarOptions.init(0))
                // GET the triggers for this date
                generateAndAppendTriggers(date!, triggers: &triggers)
            }
            mask = mask << 1
        }

        //printTriggerList("periodWeekdayTriggers", triggers: triggers)
        return triggers*/
        return triggers;
    }

    private List<Date> periodMonthDatesTriggers(Date fromDate) {
        List<Date> dateList = new ArrayList<Date>();
       /* var triggers: [NSDate] = []

        // Break down fromDate in to its calendar components.
        let flags: NSCalendarUnit = .YearCalendarUnit | .MonthCalendarUnit | .DayCalendarUnit | .HourCalendarUnit | .MinuteCalendarUnit | .SecondCalendarUnit
        var comps = theCalendar.components(flags, fromDate: fromDate)

        // GET fromDate's month's last date.
        let lastDate: Int = numberOfDaysInMonth(fromDate)

        // Loop for 31 (max days in a month) PLUS 1 (for Last DAY bit in cyclicalMask) = 32

        var mask: UInt64 = 1

        for i in 1...32 {
            if i > 1 {
                mask = mask << 1
            }

            if cyclicalDayMask & mask != 0 {
                // This day has the bit on.
                var day = i;

                // Is this Last DAY?
                if i == 32 {
                    // We handle Last day as follows: LastDay bit in the mask
                    // is really a place holder for lastDate (variable calculated earlier).
                    // But it is possible that cyclicalMask may already have lastDate bit on.
                    // E.g., in the month of April, in cyclicalMask, bit 30 (for date 30)
                    // and bit 32 (for LastDay) may on. In that case, lastDate bit has already
                    // been processed, and processing for LastDay bit should be skipped.
                    day = lastDate
                    var mask1 = RepetitionRules.generateMask(lastDate)
                    if (cyclicalDayMask & mask1 != 0) {
                        continue
                    }
                }
                else if i > lastDate {
                    // Skip the dates after lastDate because they don't occur in the given month.
                    continue
                }

                // Calculate the date for the selected date of the month.
                var tComps = theCalendar.components(flags, fromDate: fromDate)
                tComps.day = day
                let date = theCalendar.dateFromComponents(tComps)
                // GET the triggers for this date
                generateAndAppendTriggers(date!, triggers: &triggers)
            }
        }

        //printTriggerList("periodMonthDatesTriggers", triggers: triggers)

        return triggers*/
        return dateList;
    }

    private List<Date> periodMonthDaysTriggers(Date fromDate) {
        List<Date> dateList = new ArrayList<Date>();
       /* var triggers: [NSDate] = []

        // GET fromDate's month's last date.
        let lastDate: Int = numberOfDaysInMonth(fromDate)

        // FIRST date of the month.
        let first = firstDateOfMonth(fromDate)

        // Break down fromDate in to its calendar components.
        // Calxulate the weekday of date 1 in this month.
        let flags: NSCalendarUnit = .YearCalendarUnit | .MonthCalendarUnit | .DayCalendarUnit | .HourCalendarUnit | .MinuteCalendarUnit | .SecondCalendarUnit | .WeekdayCalendarUnit
        var comps = theCalendar.components(flags, fromDate: first)
        var dayOrdinal = comps.weekday

        // Loop for the number of days in a month.
        // In the loop, count the week number and keep track of the weekday.
        // Construct the mask for the loop day and theck if any of the bits is on.
        // Note that the constructed mask may have more than one bit on.
        // For example, Fourth Monday and Last Monday may fall on the same day.
        // Also count weekday number and weekend number to generate the bit mask.
        // Last Weekday and Weekend are tested specially in a loop for the last 7
        // days of the month.

        var weekdayOrdinal: Int = 0
        var weekendOrdinal: Int = 0

        var date = first
        comps = NSDateComponents()
        comps.day = 1

        for i in 1...lastDate {
            if i > 1 {
                // Calculate the date for this day
                date = theCalendar.dateByAddingComponents(comps, toDate: date, options: NSCalendarOptions.init(0))!
            }

            // WeekOrdinal from 1 to 5
            let weekOrdinal: Int = (i-1) / 7 + 1

            var finalMask: UInt64 = 0
            var mask: UInt64 = 0

            // n is the bit position in the mask.
            // Review the bit mask positions for this cyclical type as documented
            // above.
            // Each weekday is allocated a block of six bits. The last bit in
            // that block is for Last.
            var n: Int = (dayOrdinal-1) * 6 + (weekOrdinal-1) + 1
            finalMask = RepetitionRules.generateMask(n)

            // Now for Last days.
            if i > (lastDate-7) {
                // Set the Last bit for the weekday
                n = (dayOrdinal - 1) * 6 + 6
                mask = RepetitionRules.generateMask(n)
                finalMask = finalMask | mask
            }

            if isWeekday(dayOrdinal) {
                // Count weekdays.
                weekdayOrdinal++
                // Only up to 5 weekdays allowed.
                if weekdayOrdinal <= 5 {
                    // Calculate the mask
                    n = 7 * 6 + weekdayOrdinal
                    mask = RepetitionRules.generateMask(n)
                    finalMask = finalMask | mask
                }
                // Check for the last weekday
                else {
                    if isLastWeekdayOrWeekend(i, dayOrdinal: dayOrdinal, lastDate: lastDate, matchWeekday: true) {
                        // Calculate the mask
                        n = 7 * 6 + 6
                        mask = RepetitionRules.generateMask(n)
                        finalMask = finalMask | mask
                    }
                }
            }
            else {
                // Count weekends.
                weekendOrdinal++
                // Only up to 5 weekends allowed.
                if weekendOrdinal <= 5 {
                    // Calculate the mask
                    n = 8 * 6 + weekendOrdinal
                    mask = RepetitionRules.generateMask(n)
                    finalMask = finalMask | mask
                }
                // Check for the last weekend
                else {
                    if isLastWeekdayOrWeekend(i, dayOrdinal: dayOrdinal, lastDate: lastDate, matchWeekday: true) {
                        // Calculate the mask
                        n = 8 * 6 + 6
                        mask = RepetitionRules.generateMask(n)
                        finalMask = finalMask | mask
                    }
                }
            }

            // UPDATE the weekday ordinal number
            dayOrdinal = (dayOrdinal < 7) ? ++dayOrdinal : 1

            if cyclicalDayMask & finalMask != 0 {
                // GET the triggers for this date
                generateAndAppendTriggers(date, triggers: &triggers)
            }
        }

        //printTriggerList("periodMonthDaysTriggers", triggers: triggers)

        return triggers;*/
        return dateList;
    }

    // Helper function to check if th egiven month day, day, is the last weekday
    // or weekend of the month
    private Boolean isLastWeekdayOrWeekend(int day, int dayOrdinal, int lastDate, Boolean matchWeekday) {
        Boolean result = false;

        // To be done only if this the last week of the month
        if (day > (lastDate - 7)) {
            // last stores the last weekday or weekend SO FAR processed.
            // It will be processed for up to last 7 days of the month.
            // (This depends on the value of day.
            // At the end of the loop for last week, it will end up
            // storing the last weekday or weekend.
            int last = 0;

            // Loop for the remaining days in the month
            for (int i = day; i <= lastDate; i++) {
                // If it is weekday or weekend, update last accordingly.
                Boolean b = (matchWeekday) ? isWeekday(dayOrdinal) : isWeekend(dayOrdinal);
                if (b) {
                    last = day + i - 1;
                }

                // UPDATE the weekday ordinal number
                dayOrdinal = (dayOrdinal < 7) ? ++dayOrdinal : 1;
            }

            // true only if the last weekday/weekend counter = day
            if (day == last) {
                result = true;
            }
        }

        return result;
    }

    // Calculate the number of days in a month (month for the given date)
    public int numberOfDaysInMonth(Date date) {
        // This is done as follows. Let us assume the given date-day is 'n' and the month
        // is 'm'.
        // (1) UPDATE the date to 1st of the month m.
        // (2) ADD one month to it. DATE becomes 1st of the month m+1.
        // (3) Subtract one day from this. DATE becomes <last-day> of the month m.
        // The last day number is the number of days in the month.

        // FIRST day of the month
        Date tDate = firstDateOfMonth(date);
        // ADD one month
        Calendar cal = Calendar.getInstance();
        cal.setTime(tDate);
        cal.set(Calendar.MONTH, 1);
        // Subtract one day
        cal.add(Calendar.DAY_OF_YEAR, -1);

        // The calculated date in to its calendar components

        int n = cal.get(Calendar.DAY_OF_YEAR);
        return n;
    }

    // Calculate the first date of the given month in the given date.
    public Date firstDateOfMonth(Date date) {
        // The given date in to its calendar components
        Calendar cal = Calendar.getInstance();
        // Set the day to 1
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date first = cal.getTime();
        return first;
    }

    // Is the given day ordinal a weekday?
    private Boolean isWeekday(int dayOrdinal) {
        // It is a weekday if it is not a weekend
        return !isWeekend(dayOrdinal);
    }

    // Is the given day a weekend day?
    private Boolean isWeekend(int dayOrdinal) {
        // Check from the two weekend day ordinals set for this instance.
        Boolean b = false;
        if ((getUnsignedInt(dayOrdinal) == weekendDayOrdinal1) || (getUnsignedInt(dayOrdinal) == weekendDayOrdinal2)) {
            b = true;
        }
        return b;
    }

    /**
     * get uint from int
     *
     * @param x
     * @return
     */
    public static long getUnsignedInt(int x) {
        return x & 0x00000000ffffffffL;
    }

    // Given the time list in triggerPartList (only time components), change the date
    // of these components to the given date, and append them to triggers array.
    private void generateAndAppendTriggers(Date forDate, List<Date> triggers) {
        Calendar gCal = Calendar.getInstance();
        gCal.setTime(forDate);
        // Given date in to its calendar components
        // Loop for each time part
        for (int j = 0; j <= triggerPartList.size() - 1; j++) {
            // triggerPartList item in to its calendar components
            Date dateTime = triggerPartList.get(j);
            Calendar tCal = Calendar.getInstance();
            tCal.setTime(dateTime);
            // Foce the triggerPartList components to have the same year/month/date
            // as the given date compoments
            tCal.set(Calendar.YEAR, gCal.get(Calendar.YEAR));
            tCal.set(Calendar.MONTH, gCal.get(Calendar.MONTH));
            tCal.set(Calendar.DAY_OF_YEAR, gCal.get(Calendar.DAY_OF_YEAR));
            // Convert components to date
            Date trigger = tCal.getTime();

            // If not nil, add
            if (trigger != null) {
                triggers.add(trigger);
            }
        }
    }

    // Find the first date in triggers array that is later than the given date.
    // Triggers array is always sorted.
    private Date matchFromDateTimeInTriggers(Date date, List<Date> triggers) {
        Date matchedTrigger = null;

        for (Date trigger : triggers) {
            if (trigger.before(date)) {
                matchedTrigger = trigger;
                break;
            }
        }
        return matchedTrigger;
    }

    /*
    func isValidDateTime(comps: NSDateComponents) -> BOOL {
    let calendar: NSCalendar = NSCalendar.currentCalendar()
    var dateTime: NSDate? = calendar.dateFromComponents(comps)
    let b: BOOL = (dateTime) ? true : false
    return b;
    }
    */

    // Generate a mask for a bit in the given position.
    // Note that this should not be required. But because
    // Swift compiler seems to have a problem with << expression,
    // we are doing it here.
    /*private class func generateMask(int position) -> UInt64 {
       *//* if (position <= 0) {
            return 0;
        }
        else if (position == 1) {
            return 1;
        }

        var mask: UInt64 = 1
        for i in 2...position {
            mask = mask << 1
        }
        return mask*//*
    }*/

    // Debug: Print the trigger list
    private void printTriggerList(String listName, List<Date> triggers) {
        Log.d(this.getClass().getName(), "List: " + listName + "; count: " + triggers.size() + "");

        //let calendar: NSCalendar = NSCalendar.currentCalendar()
        int n = 0;
        for (Date trigger : triggers) {
            Log.d(this.getClass().getName(), "  " + (++n) + "): " + trigger);
        }
    }

    public int getSundayMask() {
        return sundayMask;
    }

    public void setSundayMask(int sundayMask) {
        this.sundayMask = sundayMask;
    }

    public int getMondayMask() {
        return mondayMask;
    }

    public void setMondayMask(int mondayMask) {
        this.mondayMask = mondayMask;
    }

    public int getTuesdayMask() {
        return tuesdayMask;
    }

    public void setTuesdayMask(int tuesdayMask) {
        this.tuesdayMask = tuesdayMask;
    }

    public int getWednesdayMask() {
        return wednesdayMask;
    }

    public void setWednesdayMask(int wednesdayMask) {
        this.wednesdayMask = wednesdayMask;
    }

    public int getThursdayMask() {
        return thursdayMask;
    }

    public void setThursdayMask(int thursdayMask) {
        this.thursdayMask = thursdayMask;
    }

    public int getFridayMask() {
        return fridayMask;
    }

    public void setFridayMask(int fridayMask) {
        this.fridayMask = fridayMask;
    }

    public int getSaturdayMask() {
        return saturdayMask;
    }

    public void setSaturdayMask(int saturdayMask) {
        this.saturdayMask = saturdayMask;
    }

    public int getLastMonthDateMaskPosition() {
        return lastMonthDateMaskPosition;
    }

    public void setLastMonthDateMaskPosition(int lastMonthDateMaskPosition) {
        this.lastMonthDateMaskPosition = lastMonthDateMaskPosition;
    }

    public int getLastMonthDateMask() {
        return lastMonthDateMask;
    }

    public void setLastMonthDateMask(int lastMonthDateMask) {
        this.lastMonthDateMask = lastMonthDateMask;
    }

    public int getFirstSundayOfMonthMask() {
        return firstSundayOfMonthMask;
    }

    public void setFirstSundayOfMonthMask(int firstSundayOfMonthMask) {
        this.firstSundayOfMonthMask = firstSundayOfMonthMask;
    }

    public int getSecondSundayOfMonthMask() {
        return secondSundayOfMonthMask;
    }

    public void setSecondSundayOfMonthMask(int secondSundayOfMonthMask) {
        this.secondSundayOfMonthMask = secondSundayOfMonthMask;
    }

    public int getThirdSundayOfMonthMask() {
        return thirdSundayOfMonthMask;
    }

    public void setThirdSundayOfMonthMask(int thirdSundayOfMonthMask) {
        this.thirdSundayOfMonthMask = thirdSundayOfMonthMask;
    }

    public int getFourthSundayOfMonthMask() {
        return fourthSundayOfMonthMask;
    }

    public void setFourthSundayOfMonthMask(int fourthSundayOfMonthMask) {
        this.fourthSundayOfMonthMask = fourthSundayOfMonthMask;
    }

    public int getFifthSundayOfMonthMask() {
        return fifthSundayOfMonthMask;
    }

    public void setFifthSundayOfMonthMask(int fifthSundayOfMonthMask) {
        this.fifthSundayOfMonthMask = fifthSundayOfMonthMask;
    }

    public int getLastSundayOfMonthMask() {
        return lastSundayOfMonthMask;
    }

    public void setLastSundayOfMonthMask(int lastSundayOfMonthMask) {
        this.lastSundayOfMonthMask = lastSundayOfMonthMask;
    }

    public int getFirstMondayOfMonthMask() {
        return firstMondayOfMonthMask;
    }

    public void setFirstMondayOfMonthMask(int firstMondayOfMonthMask) {
        this.firstMondayOfMonthMask = firstMondayOfMonthMask;
    }

    public int getSecondMondayOfMonthMask() {
        return secondMondayOfMonthMask;
    }

    public void setSecondMondayOfMonthMask(int secondMondayOfMonthMask) {
        this.secondMondayOfMonthMask = secondMondayOfMonthMask;
    }

    public int getThirdMondayOfMonthMask() {
        return thirdMondayOfMonthMask;
    }

    public void setThirdMondayOfMonthMask(int thirdMondayOfMonthMask) {
        this.thirdMondayOfMonthMask = thirdMondayOfMonthMask;
    }

    public int getFourthMondayOfMonthMask() {
        return fourthMondayOfMonthMask;
    }

    public void setFourthMondayOfMonthMask(int fourthMondayOfMonthMask) {
        this.fourthMondayOfMonthMask = fourthMondayOfMonthMask;
    }

    public int getFifthMondayOfMonthMask() {
        return fifthMondayOfMonthMask;
    }

    public void setFifthMondayOfMonthMask(int fifthMondayOfMonthMask) {
        this.fifthMondayOfMonthMask = fifthMondayOfMonthMask;
    }

    public int getLastMondayOfMonthMask() {
        return lastMondayOfMonthMask;
    }

    public void setLastMondayOfMonthMask(int lastMondayOfMonthMask) {
        this.lastMondayOfMonthMask = lastMondayOfMonthMask;
    }

    public int getFirstTuesdayOfMonthMask() {
        return firstTuesdayOfMonthMask;
    }

    public void setFirstTuesdayOfMonthMask(int firstTuesdayOfMonthMask) {
        this.firstTuesdayOfMonthMask = firstTuesdayOfMonthMask;
    }

    public int getSecondTuesdayOfMonthMask() {
        return secondTuesdayOfMonthMask;
    }

    public void setSecondTuesdayOfMonthMask(int secondTuesdayOfMonthMask) {
        this.secondTuesdayOfMonthMask = secondTuesdayOfMonthMask;
    }

    public int getThirdTuesdayOfMonthMask() {
        return thirdTuesdayOfMonthMask;
    }

    public void setThirdTuesdayOfMonthMask(int thirdTuesdayOfMonthMask) {
        this.thirdTuesdayOfMonthMask = thirdTuesdayOfMonthMask;
    }

    public int getFourthTuesdayOfMonthMask() {
        return fourthTuesdayOfMonthMask;
    }

    public void setFourthTuesdayOfMonthMask(int fourthTuesdayOfMonthMask) {
        this.fourthTuesdayOfMonthMask = fourthTuesdayOfMonthMask;
    }

    public int getFifthTuesdayOfMonthMask() {
        return fifthTuesdayOfMonthMask;
    }

    public void setFifthTuesdayOfMonthMask(int fifthTuesdayOfMonthMask) {
        this.fifthTuesdayOfMonthMask = fifthTuesdayOfMonthMask;
    }

    public int getLastTuesdayOfMonthMask() {
        return lastTuesdayOfMonthMask;
    }

    public void setLastTuesdayOfMonthMask(int lastTuesdayOfMonthMask) {
        this.lastTuesdayOfMonthMask = lastTuesdayOfMonthMask;
    }

    public int getFirstWednesdayOfMonthMask() {
        return firstWednesdayOfMonthMask;
    }

    public void setFirstWednesdayOfMonthMask(int firstWednesdayOfMonthMask) {
        this.firstWednesdayOfMonthMask = firstWednesdayOfMonthMask;
    }

    public int getSecondWednesdayOfMonthMask() {
        return secondWednesdayOfMonthMask;
    }

    public void setSecondWednesdayOfMonthMask(int secondWednesdayOfMonthMask) {
        this.secondWednesdayOfMonthMask = secondWednesdayOfMonthMask;
    }

    public int getThirdWednesdayOfMonthMask() {
        return thirdWednesdayOfMonthMask;
    }

    public void setThirdWednesdayOfMonthMask(int thirdWednesdayOfMonthMask) {
        this.thirdWednesdayOfMonthMask = thirdWednesdayOfMonthMask;
    }

    public int getFourthWednesdayOfMonthMask() {
        return fourthWednesdayOfMonthMask;
    }

    public void setFourthWednesdayOfMonthMask(int fourthWednesdayOfMonthMask) {
        this.fourthWednesdayOfMonthMask = fourthWednesdayOfMonthMask;
    }

    public int getFifthWednesdayOfMonthMask() {
        return fifthWednesdayOfMonthMask;
    }

    public void setFifthWednesdayOfMonthMask(int fifthWednesdayOfMonthMask) {
        this.fifthWednesdayOfMonthMask = fifthWednesdayOfMonthMask;
    }

    public int getLastWednesdayOfMonthMask() {
        return lastWednesdayOfMonthMask;
    }

    public void setLastWednesdayOfMonthMask(int lastWednesdayOfMonthMask) {
        this.lastWednesdayOfMonthMask = lastWednesdayOfMonthMask;
    }

    public int getFirstThursdayOfMonthMask() {
        return firstThursdayOfMonthMask;
    }

    public void setFirstThursdayOfMonthMask(int firstThursdayOfMonthMask) {
        this.firstThursdayOfMonthMask = firstThursdayOfMonthMask;
    }

    public int getSecondThursdayOfMonthMask() {
        return secondThursdayOfMonthMask;
    }

    public void setSecondThursdayOfMonthMask(int secondThursdayOfMonthMask) {
        this.secondThursdayOfMonthMask = secondThursdayOfMonthMask;
    }

    public int getThirdThursdayOfMonthMask() {
        return thirdThursdayOfMonthMask;
    }

    public void setThirdThursdayOfMonthMask(int thirdThursdayOfMonthMask) {
        this.thirdThursdayOfMonthMask = thirdThursdayOfMonthMask;
    }

    public int getFourthThursdayOfMonthMask() {
        return fourthThursdayOfMonthMask;
    }

    public void setFourthThursdayOfMonthMask(int fourthThursdayOfMonthMask) {
        this.fourthThursdayOfMonthMask = fourthThursdayOfMonthMask;
    }

    public int getFifthThursdayOfMonthMask() {
        return fifthThursdayOfMonthMask;
    }

    public void setFifthThursdayOfMonthMask(int fifthThursdayOfMonthMask) {
        this.fifthThursdayOfMonthMask = fifthThursdayOfMonthMask;
    }

    public int getLastThursdayOfMonthMask() {
        return lastThursdayOfMonthMask;
    }

    public void setLastThursdayOfMonthMask(int lastThursdayOfMonthMask) {
        this.lastThursdayOfMonthMask = lastThursdayOfMonthMask;
    }

    public int getFirstFridayOfMonthMask() {
        return firstFridayOfMonthMask;
    }

    public void setFirstFridayOfMonthMask(int firstFridayOfMonthMask) {
        this.firstFridayOfMonthMask = firstFridayOfMonthMask;
    }

    public int getSecondFridayOfMonthMask() {
        return secondFridayOfMonthMask;
    }

    public void setSecondFridayOfMonthMask(int secondFridayOfMonthMask) {
        this.secondFridayOfMonthMask = secondFridayOfMonthMask;
    }

    public int getThirdFridayOfMonthMask() {
        return thirdFridayOfMonthMask;
    }

    public void setThirdFridayOfMonthMask(int thirdFridayOfMonthMask) {
        this.thirdFridayOfMonthMask = thirdFridayOfMonthMask;
    }

    public int getFourthFridayOfMonthMask() {
        return fourthFridayOfMonthMask;
    }

    public void setFourthFridayOfMonthMask(int fourthFridayOfMonthMask) {
        this.fourthFridayOfMonthMask = fourthFridayOfMonthMask;
    }

    public int getFifthFridayOfMonthMask() {
        return fifthFridayOfMonthMask;
    }

    public void setFifthFridayOfMonthMask(int fifthFridayOfMonthMask) {
        this.fifthFridayOfMonthMask = fifthFridayOfMonthMask;
    }

    public int getLastFridayOfMonthMask() {
        return lastFridayOfMonthMask;
    }

    public void setLastFridayOfMonthMask(int lastFridayOfMonthMask) {
        this.lastFridayOfMonthMask = lastFridayOfMonthMask;
    }

    public int getFirstSaturdayOfMonthMask() {
        return firstSaturdayOfMonthMask;
    }

    public void setFirstSaturdayOfMonthMask(int firstSaturdayOfMonthMask) {
        this.firstSaturdayOfMonthMask = firstSaturdayOfMonthMask;
    }

    public int getSecondSaturdayOfMonthMask() {
        return secondSaturdayOfMonthMask;
    }

    public void setSecondSaturdayOfMonthMask(int secondSaturdayOfMonthMask) {
        this.secondSaturdayOfMonthMask = secondSaturdayOfMonthMask;
    }

    public int getThirdSaturdayOfMonthMask() {
        return thirdSaturdayOfMonthMask;
    }

    public void setThirdSaturdayOfMonthMask(int thirdSaturdayOfMonthMask) {
        this.thirdSaturdayOfMonthMask = thirdSaturdayOfMonthMask;
    }

    public int getFourthSaturdayOfMonthMask() {
        return fourthSaturdayOfMonthMask;
    }

    public void setFourthSaturdayOfMonthMask(int fourthSaturdayOfMonthMask) {
        this.fourthSaturdayOfMonthMask = fourthSaturdayOfMonthMask;
    }

    public int getFifthSaturdayOfMonthMask() {
        return fifthSaturdayOfMonthMask;
    }

    public void setFifthSaturdayOfMonthMask(int fifthSaturdayOfMonthMask) {
        this.fifthSaturdayOfMonthMask = fifthSaturdayOfMonthMask;
    }

    public int getLastSaturdayOfMonthMask() {
        return lastSaturdayOfMonthMask;
    }

    public void setLastSaturdayOfMonthMask(int lastSaturdayOfMonthMask) {
        this.lastSaturdayOfMonthMask = lastSaturdayOfMonthMask;
    }

    public int getFirstWeekdayOfMonthMask() {
        return firstWeekdayOfMonthMask;
    }

    public void setFirstWeekdayOfMonthMask(int firstWeekdayOfMonthMask) {
        this.firstWeekdayOfMonthMask = firstWeekdayOfMonthMask;
    }

    public int getSecondWeekdayOfMonthMask() {
        return secondWeekdayOfMonthMask;
    }

    public void setSecondWeekdayOfMonthMask(int secondWeekdayOfMonthMask) {
        this.secondWeekdayOfMonthMask = secondWeekdayOfMonthMask;
    }

    public int getThirdWeekdayOfMonthMask() {
        return thirdWeekdayOfMonthMask;
    }

    public void setThirdWeekdayOfMonthMask(int thirdWeekdayOfMonthMask) {
        this.thirdWeekdayOfMonthMask = thirdWeekdayOfMonthMask;
    }

    public int getFourthWeekdayOfMonthMask() {
        return fourthWeekdayOfMonthMask;
    }

    public void setFourthWeekdayOfMonthMask(int fourthWeekdayOfMonthMask) {
        this.fourthWeekdayOfMonthMask = fourthWeekdayOfMonthMask;
    }

    public int getFifthWeekdayOfMonthMask() {
        return fifthWeekdayOfMonthMask;
    }

    public void setFifthWeekdayOfMonthMask(int fifthWeekdayOfMonthMask) {
        this.fifthWeekdayOfMonthMask = fifthWeekdayOfMonthMask;
    }

    public int getLastWeekdayOfMonthMask() {
        return lastWeekdayOfMonthMask;
    }

    public void setLastWeekdayOfMonthMask(int lastWeekdayOfMonthMask) {
        this.lastWeekdayOfMonthMask = lastWeekdayOfMonthMask;
    }

    public int getFirstWeekendOfMonthMask() {
        return firstWeekendOfMonthMask;
    }

    public void setFirstWeekendOfMonthMask(int firstWeekendOfMonthMask) {
        this.firstWeekendOfMonthMask = firstWeekendOfMonthMask;
    }

    public int getSecondWeekendOfMonthMask() {
        return secondWeekendOfMonthMask;
    }

    public void setSecondWeekendOfMonthMask(int secondWeekendOfMonthMask) {
        this.secondWeekendOfMonthMask = secondWeekendOfMonthMask;
    }

    public int getThirdWeekendOfMonthMask() {
        return thirdWeekendOfMonthMask;
    }

    public void setThirdWeekendOfMonthMask(int thirdWeekendOfMonthMask) {
        this.thirdWeekendOfMonthMask = thirdWeekendOfMonthMask;
    }

    public int getFourthWeekendOfMonthMask() {
        return fourthWeekendOfMonthMask;
    }

    public void setFourthWeekendOfMonthMask(int fourthWeekendOfMonthMask) {
        this.fourthWeekendOfMonthMask = fourthWeekendOfMonthMask;
    }

    public int getFifthWeekendOfMonthMask() {
        return fifthWeekendOfMonthMask;
    }

    public void setFifthWeekendOfMonthMask(int fifthWeekendOfMonthMask) {
        this.fifthWeekendOfMonthMask = fifthWeekendOfMonthMask;
    }

    public int getLastWeekendOfMonthMask() {
        return lastWeekendOfMonthMask;
    }

    public void setLastWeekendOfMonthMask(int lastWeekendOfMonthMask) {
        this.lastWeekendOfMonthMask = lastWeekendOfMonthMask;
    }

    public int getWeekendDayOrdinal1() {
        return weekendDayOrdinal1;
    }

    public void setWeekendDayOrdinal1(int weekendDayOrdinal1) {
        this.weekendDayOrdinal1 = weekendDayOrdinal1;
    }

    public int getWeekendDayOrdinal2() {
        return weekendDayOrdinal2;
    }

    public void setWeekendDayOrdinal2(int weekendDayOrdinal2) {
        this.weekendDayOrdinal2 = weekendDayOrdinal2;
    }

    public Calendar getTheCalendar() {
        return theCalendar;
    }

    public void setTheCalendar(Calendar theCalendar) {
        this.theCalendar = theCalendar;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public RepetitionType getRepetitionType() {
        return repetitionType;
    }

    public void setRepetitionType(RepetitionType repetitionType) {
        this.repetitionType = repetitionType;
    }

    public CyclicalType getCyclicalType() {
        return cyclicalType;
    }

    public void setCyclicalType(CyclicalType cyclicalType) {
        this.cyclicalType = cyclicalType;
    }

    public TimeUnit getCyclicalTimeUnit() {
        return cyclicalTimeUnit;
    }

    public void setCyclicalTimeUnit(TimeUnit cyclicalTimeUnit) {
        this.cyclicalTimeUnit = cyclicalTimeUnit;
    }

    public int getCyclicalInterval() {
        return cyclicalInterval;
    }

    public void setCyclicalInterval(int cyclicalInterval) {
        this.cyclicalInterval = cyclicalInterval;
    }

    public long getCyclicalDayMask() {
        return cyclicalDayMask;
    }

    public void setCyclicalDayMask(long cyclicalDayMask) {
        this.cyclicalDayMask = cyclicalDayMask;
    }

    public List<Date> getTriggerPartList() {
        return triggerPartList;
    }

    public void setTriggerPartList(List<Date> triggerPartList) {
        this.triggerPartList = triggerPartList;
    }
}
