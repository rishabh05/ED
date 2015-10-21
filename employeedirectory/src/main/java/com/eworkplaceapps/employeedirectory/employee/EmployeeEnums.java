//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/21/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

/**
 *
 */
public class EmployeeEnums {

    public enum EDGroupEventNotification {
        AN_EMPLOYEE_ADDED(1);
        private int id;

        EDGroupEventNotification(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public enum EDAdhocNotification {
        PING(1),
        WELCOME_SIGNUP_TENANT(2),
        WELCOME_SIGNUP_EMPLOYEE(3),
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

    public enum EDCyclicNotification {
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

    public enum EDEmployeePseudoUser {
        REPORT_TO(1);
        private int id;

        EDEmployeePseudoUser(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public enum EDCyclicNotificationActionId {
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
