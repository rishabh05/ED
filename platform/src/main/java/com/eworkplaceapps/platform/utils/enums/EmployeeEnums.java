//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 4/14/2015
//===============================================================================
package com.eworkplaceapps.platform.utils.enums;


public class EmployeeEnums {

    public static enum EDGroupEventNotification {
        AN_EMPLOYEE_ADDED(1);
        private int id;

        EDGroupEventNotification(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static enum EDAdhocNotification {
        PING(1),
        WELCOME_SIGN_UP_TENANT(2),
        WELCOME_SIGN_UP_EMPLOYEE(3),
        RESET_PASSWORD_REQUEST(4),
        CHANGE_PASSWORD(5);
        private int id;

        EDAdhocNotification(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static enum EDCyclicNotification {
        WEEKLY_DIGEST(1),
        ALMOST_FINISHED(2),
        ACCOUNT_EXPIRATION_REMINDER(3);
        private int id;

        EDCyclicNotification(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static enum EDEmployeePseudoUser {
        REPORT_TO(1);
        private int id;

        EDEmployeePseudoUser(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static enum EDCyclicNotificationActionId {
        SEND_WEEK_EMAIL(1);
        private int id;

        EDCyclicNotificationActionId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
