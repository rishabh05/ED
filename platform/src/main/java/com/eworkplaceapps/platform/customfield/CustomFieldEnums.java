package com.eworkplaceapps.platform.customfield;

/**
 * The file contains all the enums related to custom fields
 * Created by Parikshit on 4/25/2015.
 */
public class CustomFieldEnums {

    /**
     * Enum FieldCode
     */
    public enum FieldCode {

        // STRING type user define field1.
        UDF_STRING_1(1),

        // STRING type user define field2.
        UDF_STRING_2(2),

        // STRING type user define field3.
        UDF_STRING_3(3),

        // STRING type user define field4.
        UDF_STRING_4(4),

        // STRING type user define field5.
        UDF_STRING_5(5),

        // STRING type user define field6.
        UDF_STRING_6(6),

        // STRING type user define field7.
        UDF_STRING_7(7),

        // STRING type user define field8.
        UDF_STRING_8(8),

        // STRING type user define field9.
        UDF_STRING_9(9),

        // STRING type user define field2.
        UDF_STRING_10(10),

        // Boolean type user define field1.
        UDF_BOOL_1(11),

        // Boolean type user define field2.
        UDF_BOOL_2(12),

        // Boolean type user define field1.
        UDF_BOOL_3(13),

        // Boolean type user define field1.
        UDF_BOOL_4(14),

        // Boolean type user define field1.
        UDF_BOOL_5(15),

        // INTEGER type user define field1.
        UDF_INT_1(16),

        // INTEGER type user define field2.
        UDF_INT_2(17),

        // DATE type user define field1.
        UDF_DATE_1(18),

        // DATE type user define field2.
        UDF_DATE_2(19),

        // NUMBER type user define field1.
        UDF_NUMBER_1(20),

        // NUMBER type user define field2.
        UDF_NUMBER_2(21),

        // MONEY type user define field1.
        UDF_MONEY_1(22),

        // MONEY type user define field2.
        UDF_MONEY_2(23),

        // TENANT_USER type user define field1.
        UDF_USER_ID_1(24),

        // TENANT_USER type user define field2.
        UDF_USER_ID_2(25),

        // PICK_LIST type user define field1.
        UDF_PICK_LIST_1(26),

        // PICK_LIST type user define field2.
        UDF_PICK_LIST_2(27),

        // PICK_LIST type user define field3.
        UDF_PICK_LIST_3(28),

        // PICK_LIST type user define field4.
        UDF_PICK_LIST_4(29),

        // PICK_LIST type user define field5.
        UDF_PICK_LIST_5(30),

        // PICK_LIST type user define field6.
        UDF_PICK_LIST_6(31),

        // PICK_LIST type user define field7.
        UDF_PICK_LIST_7(32),

        // PICK_LIST type user define field8.
        UDF_PICK_LIST_8(33),

        // PICK_LIST type user define field9.
        UDF_PICK_LIST_9(34),

        // PICK_LIST type user define field10.
        UDF_PICK_LIST_10(35),

        // Text type user define field1.
        UDF_TEXT_BLOCK_1(36),

        // Text type user define field2.
        UDF_TEXT_BLOCK_2(37),

        // Text type user define field3.
        UDF_TEXT_BLOCK_3(38),

        // Text type user define field4.
        UDF_TEXT_BLOCK_4(39),

        // Text type user define field5.
        UDF_TEXT_BLOCK_5(40),

        // Text type user define field6.
        UDF_TEXT_BLOCK_6(41),

        // Text type user define field7.
        UDF_TEXT_BLOCK_7(42),

        // Text type user define field8.

        UDF_TEXT_BLOCK_8(43),
        // Text type user define field9.

        UDF_TEXT_BLOCK_9(44),

        // Text type user define field10.
        UDF_TEXT_BLOCK_10(45),

        // EMAIL type fiel2.
        UDF_EMAIL_1(46),

        // EMAIL type field.
        UDF_EMAIL_2(47),

        // EMAIL type fiel2.
        UDF_EMAIL_3(48),

        // EMAIL type field.
        UDF_EMAIL_4(49),

        // EMAIL type fiel2.
        UDF_EMAIL_5(50),

        // PHONE type field.
        UDF_PHONE_1(51),

        UDF_PHONE_2(52),

        UDF_PHONE_3(53),

        UDF_PHONE_4(54),

        UDF_PHONE_5(55),

        // ADDRESS type field2

        UDF_ADDRESS_1(56),

        UDF_ADDRESS_2(57),

        UDF_ADDRESS_3(58),

        UDF_ADDRESS_4(59),

        UDF_ADDRESS_5(60);

        private int id;

        FieldCode(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String toString() {
            switch (this) {
                case UDF_STRING_1:
                    return "UDF_STRING_1";
                case UDF_STRING_2:
                    return "UDF_STRING_2";
                case UDF_STRING_3:
                    return "UDF_STRING_3";
                case UDF_STRING_4:
                    return "UDF_STRING_4";
                case UDF_STRING_5:
                    return "UDF_STRING_5";
                case UDF_STRING_6:
                    return "UDF_STRING_6";
                case UDF_STRING_7:
                    return "UDF_STRING_7";
                case UDF_STRING_8:
                    return "UDF_STRING_8";
                case UDF_STRING_9:
                    return "UDF_STRING_9";
                case UDF_STRING_10:
                    return "UDF_STRING_10";
                case UDF_BOOL_1:
                    return "UDF_BOOL_1";
                case UDF_BOOL_2:
                    return "UDF_BOOL_2";
                case UDF_BOOL_3:
                    return "UDF_BOOL_3";
                case UDF_BOOL_4:
                    return "UDF_BOOL_4";
                case UDF_BOOL_5:
                    return "UDF_BOOL_5";
                case UDF_INT_1:
                    return "UDF_INT_1";
                case UDF_INT_2:
                    return "UDF_INT_2";
                case UDF_DATE_1:
                    return "UDF_DATE_1";
                case UDF_DATE_2:
                    return "UDF_DATE_2";
                case UDF_NUMBER_1:
                    return "UDF_NUMBER_1";
                case UDF_NUMBER_2:
                    return "UDF_NUMBER_2";
                case UDF_MONEY_1:
                    return "UDF_MONEY_1";
                case UDF_MONEY_2:
                    return "UDF_MONEY_2";
                case UDF_USER_ID_1:
                    return "UDF_USER_ID_1";
                case UDF_USER_ID_2:
                    return "UDF_USER_ID_2";
                case UDF_PICK_LIST_1:
                    return "UDF_PICK_LIST_1";
                case UDF_PICK_LIST_2:
                    return "UDF_PICK_LIST_2";
                case UDF_PICK_LIST_3:
                    return "UDF_PICK_LIST_3";
                case UDF_PICK_LIST_4:
                    return "UDF_PICK_LIST_4";
                case UDF_PICK_LIST_5:
                    return "UDF_PICK_LIST_5";
                case UDF_PICK_LIST_6:
                    return "UDF_PICK_LIST_6";
                case UDF_PICK_LIST_7:
                    return "UDF_PICK_LIST_7";
                case UDF_PICK_LIST_8:
                    return "UDF_PICK_LIST_8";
                case UDF_PICK_LIST_9:
                    return "UDF_PICK_LIST_9";
                case UDF_PICK_LIST_10:
                    return "UDF_PICK_LIST_10";
                case UDF_TEXT_BLOCK_1:
                    return "UDF_TEXT_BLOCK_1";
                case UDF_TEXT_BLOCK_2:
                    return "UDF_TEXT_BLOCK_2";
                case UDF_TEXT_BLOCK_3:
                    return "UDF_TEXT_BLOCK_3";
                case UDF_TEXT_BLOCK_4:
                    return "UDF_TEXT_BLOCK_4";
                case UDF_TEXT_BLOCK_5:
                    return "UDF_TEXT_BLOCK_5";
                case UDF_TEXT_BLOCK_6:
                    return "UDF_TEXT_BLOCK_6";
                case UDF_TEXT_BLOCK_7:
                    return "UDF_TEXT_BLOCK_7";
                case UDF_TEXT_BLOCK_8:
                    return "UDF_TEXT_BLOCK_8";
                case UDF_TEXT_BLOCK_9:
                    return "UDF_TEXT_BLOCK_9";
                case UDF_TEXT_BLOCK_10:
                    return "UDF_TEXT_BLOCK_10";
                case UDF_EMAIL_1:
                    return "UDF_EMAIL_1";
                case UDF_EMAIL_2:
                    return "UDF_EMAIL_2";
                case UDF_EMAIL_3:
                    return "UDF_EMAIL_3";
                case UDF_EMAIL_4:
                    return "UDF_EMAIL_4";
                case UDF_EMAIL_5:
                    return "UDF_EMAIL_5";
                case UDF_PHONE_1:
                    return "UDF_PHONE_1";
                case UDF_PHONE_2:
                    return "UDF_PHONE_2";
                case UDF_PHONE_3:
                    return "UDF_PHONE_3";
                case UDF_PHONE_4:
                    return "UDF_PHONE_4";
                case UDF_PHONE_5:
                    return "UDF_PHONE_5";
                case UDF_ADDRESS_1:
                    return "UDF_ADDRESS_1";
                case UDF_ADDRESS_2:
                    return "UDF_ADDRESS_2";
                case UDF_ADDRESS_3:
                    return "UDF_ADDRESS_3";
                case UDF_ADDRESS_4:
                    return "UDF_ADDRESS_4";
                case UDF_ADDRESS_5:
                    return "UDF_ADDRESS_5";
                default:
                    return "";
            }

        }

    }

    /**
     * FieldDataType enums for UDF
     */
    public enum FieldType {

        // STRING type.
        NONE(0),

        // STRING type.
        STRING(1),

        // Boolean type.
        BOOL(2),

        // INTEGER type.
        INTEGER(3),

        // DATE type.
        DATE(4),

        // NUMBER type.
        NUMBER(5),

        // MONEY type.
        MONEY(6),

        // USER type.
        USER(7),

        // PICK_LIST type.
        PICK_LIST(8),

        // UDF_TEXT_BLOCK type.
        UDF_TEXT_BLOCK(9),

        // EMAIL type
        EMAIL(10),

        // PHONE type.
        PHONE(11),

        // ADDRESS type.
        ADDRESS(12);

        private int id;

        FieldType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Field DataType enums for UDF
     */
    public enum FieldDataType {

        // STRING type.
        STRING(1),

        // Boolean type.
        BOOL(2),

        // INTEGER type.
        INTEGER(3),

        // DATE type.
        DATE(4),

        // NUMBER type.
        NUMBER(5),

        // MONEY type.
        MONEY(6);

        private int id;

        FieldDataType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Field Class enum
     */
    public enum FieldClass {
        // SYSTEM type field that can not be chnaged.
        SYSTEM(1),

        // BUILT_IN type field are system field also but user can change their permission.
        BUILT_IN(2),

        // CUSTOM type field are user defined fields. USER can change their permission.
        CUSTOM(3);

        private int id;

        FieldClass(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * Defines constants for employee entity field group.
     */
    public enum EntityFieldGroup {
        /// NONE Group
        NONE(0),
        PERSONAL(1),
        PRIVATE(2),
        ADMIN(3);

        private int id;

        EntityFieldGroup(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String toString() {
            switch (this) {
                case PRIVATE:
                    return "PRIVATE";
                case ADMIN:
                    return "ADMIN";
                default:
                    return "PERSONAL";
            }
        }
    }
}
