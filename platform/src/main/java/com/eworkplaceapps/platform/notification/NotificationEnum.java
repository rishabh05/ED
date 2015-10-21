//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Parikshit Patel
// Original DATE: 4/27/2015.
//===============================================================================
package com.eworkplaceapps.platform.notification;

/**
 *
 */
public class NotificationEnum {
    public enum NotificationTypeEnum {
        EVENT(1),
        AD_HOC(2),
        CYCLIC(3);
        private int id;

        NotificationTypeEnum(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public enum NotificationDeliveryType {
        EMAIL(1),
        SMS(2),
        LOCAL_NOTIFICATION(3);
        private int id;

        NotificationDeliveryType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public enum LocalNotificationType {
        BANNER(1),
        ALERT(2),
        BADGE(3);

        private int id;

        LocalNotificationType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    // Notification Recipient Type Enum.
    public enum NotificationRecipientType {
        // The user who is part of system and can perform any operation according to application role..
        INTERNAL_RECIPIENT(1),
        // This recipient is external email.
        EXTERNAL_EMAIL_RECIPIENT(2),
        // This recipient is a pseudo user and have its type that are specific to application.
        // Like in Issue Tracker Assig
        PSEUDO_RECIPIENT(3);

        private int id;

        NotificationRecipientType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    // It will be used with UNPREPARED_NOTIFICATION_QUEUE.
    public enum NotificationProcessStatusEnum {
        // Initially it will be pedning
        PENDING(1),
        // When notification transfer to NOTIFICATION_QUEUE table then status set as PROCESSED in UNPREPARED_NOTIFICATION_QUEUE table.
        PROCESSED(2),
        // If any error occured in transfer of notification from UNPREPARED_NOTIFICATION_QUEUE to NOTIFICATION_QUEUE table then Process will set as ERROR in
        // UNPREPARED_NOTIFICATION_QUEUE table.
        ERROR(3);

        private int id;

        NotificationProcessStatusEnum(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public enum CyclicType {
        REMINDER(1),
        REPETITION(2);

        private int id;

        CyclicType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public enum CyclicSubType {
        AD_HOC(1),
        CYCLIC(2);

        private int id;

        CyclicSubType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    // Specifies the cyclical type repetition category.
    public enum CyclicPattern {
        // Repeat In Time Units
        //[Description("Regular")]
        IN_TIME_UNITS(2),

        // Repeat DAILY
        //[Description("DAILY")]
        DAILY(3),

        // Repeat weekly
        //[Description("Weekly")]
        WEEKDAYS(4),

        // Repeat month days
        //[Description("Monthly by DAY")]
        MONTH_DAYS(5),

        // Repeat month dates
        //[Description("Monthly by DATE")]
        MONTH_DATES(6);

        private int id;

        CyclicPattern(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    // Specifies the time unit.
    public enum FrequencyUnit {

        // Repeat MINUTE
        // "Seconds"
        SECOND(0),

        // Repeat MINUTE
        // "Minutes"
        MINUTE(1),

        // Repeat Hourly
        // "Hours"
        HOUR(2),

        // Repeat DAILY
        // "Days"
        DAY(3),

        // Repeat Weekly
        // "Weeks"
        WEEK(4),

        // Repeat Monthly
        // "Months"
        MONTH(5),

        // Repeat Quartely
        QUARTER(6),

        // Repeat Annul
        // "Years"
        YEAR(7);

        private int id;

        FrequencyUnit(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    // Specifies the day mask
    public enum DayMask {

        // "Sunday"
        SUNDAY_MASK(1),

        // "Monday"
        MONDAY_MASK(2),

        // "Tuesday"
        TUESDAY_MASK(4),

        // "Wednesday"
        WEDNESDAY_MASK(8),

        // "Thursday"
        THURSDAY_MASK(16),

        // "Friday"
        FRIDAY_MASK(32),

        // "Saturday"
        SATURDAY_MASK(64);

        private int id;

        DayMask(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }


    // Specifies the day of month mask
    public enum DayOfMonthMask {

        // "FIRST Sunday"
        FIRST_SUNDAY_OF_MONTH_MASK(1),

        // "SECOND Sunday"
        SECOND_SUNDAY_OF_MONTH_MASK(2),

        // "THIRD Sunday"
        THIRD_SUNDAY_OF_MONTH_MASK(4),

        // "Fourth Sunday"
        FOURTH_SUNDAY_OF_MONTH_MASK(8),

        // "Fifth Sunday"
        FIFTH_SUNDAY_OF_MONTH_MASK(16),

        // "Last Sunday"
        LAST_SUNDAY_OF_MONTH_MASK(32),

        // "FIRST Monday"
        FIRST_MONDAY_OF_MONTH_MASK(64),

        // "SECOND Monday"
        SECOND_MONDAY_OF_MONTH_MASK(128),

        // "THIRD Monday"
        THIRD_MONDAY_OF_MONTH_MASK(256),

        // "Fourth Monday"
        FOURTH_MONDAY_OF_MONTH_MASK(512),

        // "Fifth Monday"
        FIFTH_MONDAY_OF_MONTH_MASK(1024),

        // "Last Monday"
        LAST_MONDAY_OF_MONTH_MASK(2048),

        // "FIRST Tuesday"
        FIRST_TUESDAY_OF_MONTH_MASK(4096),

        // "SECOND Tuesday"
        SECOND_TUESDAY_OF_MONTH_MASK(8192),

        // "THIRD Tuesday"
        THIRD_TUESDAY_OF_MONTH_MASK(16384),

        // "Fourth Tuesday"
        FOURTH_TUESDAY_OF_MONTH_MASK(32768),

        // "Fifth Tuesday"
        FIFTH_TUESDAY_OF_MONTH_MASK(65536),

        // "Last Tuesday"
        LAST_TUESDAY_OF_MONTH_MASK(131072),

        // "FIRST Wednesday"
        FIRST_WEDNESDAY_OF_MONTH_MASK(262144),

        //"SECOND Wednesday"
        SECOND_WEDNESDAY_OF_MONTH_MASK(524288),

        // "THIRD Wednesday"
        THIRD_WEDNESDAY_OF_MONTH_MASK(1048576),

        // "Fourth Wednesday"
        FOURTH_WEDNESDAY_OF_MONTH_MASK(2097152),

        // "Fifth Wednesday"
        FIFTH_WEDNESDAY_OF_MONTH_MASK(4194304),

        // "Last Wednesday"
        LAST_WEDNESDAY_OF_MONTH_MASK(8388608),

        // "FIRST Thursday"
        FIRST_THURSDAY_OF_MONTH_MASK(16777216),

        //"SECOND Thursday"
        SECOND_THURSDAY_OF_MONTH_MASK(33554432),

        THIRD_THURSDAY_OF_MONTH_MASK(67108864),

        FOURTH_THURSDAY_OF_MONTH_MASK(134217728),

        FIFTH_THURSDAY_OF_MONTH_MASK(268435456),

        LAST_THURSDAY_OF_MONTH_MASK(536870912),

        FIRST_FRIDAY_OF_MONTH_MASK(1073741824),

        SECOND_FRIDAY_OF_MONTH_MASK(2147483648L),

        THIRD_FRIDAY_OF_MONTH_MASK(4294967296L),

        FOURTH_FRIDAY_OF_MONTH_MASK(8589934592L),

        FIFTH_FRIDAY_OF_MONTH_MASK(17179869184L),

        LAST_FRIDAY_OF_MONTH_MASK(34359738368L),

        FIRST_SATURDAY_OF_MONTH_MASK(68719476736L),

        SECOND_SATURDAY_OF_MONTH_MASK(137438953472L),

        THIRD_SATURDAY_OF_MONTH_MASK(274877906944L),

        FOURTH_SATURDAY_OF_MONTH_MASK(5497558138881L),

        FIFTH_SATURDAY_OF_MONTH_MASK(1099511627776L),

        LAST_SATURDAY_OF_MONTH_MASK(2199023255552L),

        FIRST_WEEKDAY_OF_MONTH_MASK(4398046511104L),

        SECOND_WEEKDAY_OF_MONTH_MASK(8796093022208L),

        THIRD_WEEKDAY_OF_MONTH_MASK(17592186044416L),

        FOURTH_WEEKDAY_OF_MONTH_MASK(35184372088832L),

        FIFTH_WEEKDAY_OF_MONTH_MASK(70368744177664L),

        LAST_WEEKDAY_OF_MONTH_MASK(140737488355328L),

        FIRST_WEEKEND_OF_MONTH_MASK(281474976710656L),

        SECOND_WEEKEND_OF_MONTH_MASK(562949953421312L),

        THIRD_WEEKEND_OF_MONTH_MASK(1125899906842624L),

        FOURTH_WEEKEND_OF_MONTH_MASK(2251799813685248L),

        FIFTH_WEEKEND_OF_MONTH_MASK(4503599627370496L),

        LAST_WEEKEND_OF_MONTH_MASK(9007199254740992L);

        private long id;

        DayOfMonthMask(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }

// Specifies the DATE of month mask.

    public enum DateOfMonthMask {

        FIRST(1),

        SECOND(2),

        THIRD(4),

        FOUR(8),

        FIVE(16),

        SIX(32),

        SEVEN(64),

        EIGHT(128),

        NINE(256),

        TEN(512),

        ELEVEN(1024),

        TWELVE(2048),

        THIRTEEN(4096),

        FOURTEEN(8192),

        FIFTEEN(16384),

        SIXTEEN(32768),

        SEVENTEEN(65536),

        EIGHTEEN(131072),

        NINETEEN(262144),

        TWENTY(524288),

        TWENTY_ONE(1048576),

        TWENTY_TWO(2097152),

        TWENTY_THREE(4194304),

        TWENTY_FOUR(8388608),

        TWENTY_FIVE(16777216),

        TWENTY_SIX(33554432),

        TWENTY_SEVEN(67108864),

        TWENTY_EIGHT(134217728),

        TWENTY_NINE(268435456),

        THIRTY(536870912),

        THIRTY_ONE(1073741824);

        private long id;

        DateOfMonthMask(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }
}