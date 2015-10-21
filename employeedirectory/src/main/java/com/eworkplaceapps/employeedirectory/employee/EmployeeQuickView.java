//===============================================================================
// Copyright (c) 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 5/25/2015
//===============================================================================
package com.eworkplaceapps.employeedirectory.employee;

import android.database.Cursor;

import com.eworkplaceapps.platform.db.DatabaseOps;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.userentitylink.PFUserEntityLink;
import com.eworkplaceapps.platform.userentitylink.PFUserEntityLinkDataService;
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.Utils;
import com.eworkplaceapps.platform.utils.enums.CommunicationType;
import com.eworkplaceapps.platform.utils.enums.DatabaseOperationType;
import com.eworkplaceapps.platform.utils.enums.LinkType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class EmployeeQuickView implements Serializable {
    private UUID employeeId = Utils.emptyUUID();
    /// Getter for employee firstName
    private String firstName = "";
    public String lastName = "";
    public String loginEmail = "";
    /// Getter  for  profile picture
    public String picture = "";
    private int employeeStatus = 0;
    private String statusColor = "";
    /// Getter  for  jobTitle
    private String jobTitle = "", employeeStatusText = "";
    private boolean following = false;
    private String groupName = "", groupPicture = "";
    private DatabaseOperationType operationType = DatabaseOperationType.NONE;
    private UUID groupId = Utils.emptyUUID();
    private Map<String, String> moreDataDict = new HashMap<String, String>();
    private int employeeStatusColor;

    public Tuple.Tuple2<Integer, Integer, Integer> getStatusRGBColor() {
        String strArray[] = statusColor.split(",");
        if (strArray != null && strArray.length == 3) {
            int r = Integer.valueOf(strArray[0]);
            int g = Integer.valueOf(strArray[1]);
            int b = Integer.valueOf(strArray[2]);
            return new Tuple.Tuple2<Integer, Integer, Integer>(r, g, b);
        }
        return new Tuple.Tuple2<Integer, Integer, Integer>(0, 0, 0);
    }

    public void setEmployeeProperties(Cursor cursor, GroupBy groupBy, ColumnPropertyIndex columnPropertyIndex) {
        if (!groupBy.equals(GroupBy.NONE)) {
            if (!groupBy.equals(GroupBy.FAVOURITE) && !groupBy.equals(GroupBy.EMPLOYEE_STATUS)) {
                String groupName = cursor.getString(cursor.getColumnIndex(groupBy.getGroupBy()));
                if (groupName != null) {
                    this.groupName = groupName.replaceAll("''", "'");
                } else {
                    this.groupName = "";
                }
            }
            String groupId = cursor.getString(cursor.getColumnIndex(groupBy.getGroupBy() + "Id"));
            if (groupId != null) {
                this.groupId = UUID.fromString(groupId);
            } else {
                this.groupId = Utils.emptyUUID();
            }
        }
        if (groupBy.equals(GroupBy.LOCATION) || groupBy.equals(GroupBy.DEPARTMENT) || groupBy.equals(GroupBy.TEAM)) {
            this.groupPicture = cursor.getString(cursor.getColumnIndex(groupBy.getGroupBy() + "Picture"));
        }

/*
        String employeeId = cursor.getString(cursor.getColumnIndex("EmployeeId"));
        if (employeeId != null && !"".equals(employeeId)) {
            this.employeeId = UUID.fromString(employeeId);
        }
        String firstName = cursor.getString(cursor.getColumnIndex("FirstName"));
        if (firstName != null)
            this.firstName = firstName.replaceAll("''", "'");
        String lastName = cursor.getString(cursor.getColumnIndex("LastName"));
        if (lastName != null)
            this.lastName = lastName.replaceAll("''", "'");
        String jobTitle = cursor.getString(cursor.getColumnIndex("JobTitle"));
        this.jobTitle = jobTitle;
        int status = cursor.getInt(cursor.getColumnIndex("Status"));
        this.employeeStatus = status;
        String statusText = cursor.getString(cursor.getColumnIndex("StatusText"));
        if (statusText != null) {
            this.employeeStatusText = statusText.replaceAll("''", "'");
        } else {
            this.employeeStatusText = "";
        }
        String following = cursor.getString(cursor.getColumnIndex("Following"));
        this.following = "1".equals(following);
        String picture = cursor.getString(cursor.getColumnIndex("Picture"));*/


        String employeeId = cursor.getString(columnPropertyIndex.getEmpIdIndex());
        if (employeeId != null && !"".equals(employeeId)) {
            this.employeeId = UUID.fromString(employeeId);
        }
        if (columnPropertyIndex.getLoginEmailIndex() >= 0) {
            this.loginEmail = cursor.getString(columnPropertyIndex.getLoginEmailIndex());
        }
        String firstName = cursor.getString(columnPropertyIndex.getFirstNameIndex());
        if (firstName != null)
            this.firstName = firstName.replaceAll("''", "'");
        String lastName = cursor.getString(columnPropertyIndex.getLastNameIndex());
        if (lastName != null)
            this.lastName = lastName.replaceAll("''", "'");
        String jobTitle = cursor.getString(columnPropertyIndex.getJobTitleIndex());
        this.jobTitle = jobTitle;
        int status = cursor.getInt(columnPropertyIndex.getStatusIndex());
        this.employeeStatus = status;
        String statusText = cursor.getString(columnPropertyIndex.getStatusTextIndex());
        if (statusText != null) {
            this.employeeStatusText = statusText.replaceAll("''", "'");
        } else {
            this.employeeStatusText = "";
        }
        //this.statusColor
        String color = cursor.getString(columnPropertyIndex.getStatusColorIndex());
        if (statusText != null) {
            this.statusColor = color;
        }

        String following = cursor.getString(columnPropertyIndex.getFollowingIndex());
        this.following = "1".equals(following);
        String picture = cursor.getString(columnPropertyIndex.getPictureIndex());
        if ("null".equalsIgnoreCase(picture)) {
            picture = "";
        }
        this.picture = picture;
    }

    /**
     * Method is used to get employee and its short information as EmployeeQuickView list,
     * It is useful to display employee short information in employee, Location, Department list view UI.
     *
     * @param groupBy
     * @param moreData
     * @param groupId
     * @return List<EmployeeQuickView>
     * @throws EwpException
     */
    public static List<EmployeeQuickView> getEmployeeQuickViewList(GroupBy groupBy, String moreData, UUID groupId) throws EwpException {
        return getEmployeeQuickViewList(groupBy, moreData, "", groupId);
    }

    /**
     * Method is used to get employee and its short information as EmployeeQuickView list,
     * It is useful to display employee short information in employee, Location, Department list view UI.
     *
     * @param groupBy
     * @param moreData
     * @return List<EmployeeQuickView>
     */
    public static List<EmployeeQuickView> getEmployeeQuickViewList(GroupBy groupBy, String moreData, String searchValue, UUID groupId) throws EwpException {
        EmployeeDataService service = new EmployeeDataService();
        Cursor cursor = null;
        switch (groupBy) {
            case DEPARTMENT:
                cursor = service.getEmployeeListByDepartmentAsResultSet(groupId, searchValue);
                break;
            case LOCATION:
                cursor = service.getEmployeeListByLocation(groupId, searchValue);
                break;
            case TEAM:
                cursor = service.getEmployeeListByTeamAsResultSet(groupId, searchValue);
                break;
            default:
                cursor = service.searchEmployeeAndGetAsResultSet(searchValue);
                break;
        }
        List<EmployeeQuickView> empQuickViewList = new ArrayList<EmployeeQuickView>();
        if (cursor != null) {
            empQuickViewList = setPropertiesAndGetEmployeeViewList(cursor, groupBy, moreData);
        }
        return empQuickViewList;
    }

    /**
     * Method is used to get employee specific favorite employee list.
     * :param: otherData: It is a list of columns seperated by comma(,) for which we want to get
     * extra information other then employee.
     * For example in case favorite: UI coder should pass following column names CommunicationType,
     * CommunicationSubType and CommunicationValue.
     *
     * @param employeeId
     * @param groupBy
     * @param otherData
     * @return List<EmployeeQuickView>
     */
    public static List<EmployeeQuickView> getFavoriteEmployeeQuickViewList(UUID employeeId, GroupBy groupBy, String otherData) throws EwpException {
        EmployeeDataService service = new EmployeeDataService();
        Cursor cursor = null;
        List<EmployeeQuickView> empQuickViewList = new ArrayList<EmployeeQuickView>();
        switch (groupBy) {
            case FAVOURITE:
                cursor = service.getEmployeeFavoriteListAsResultSet(CommunicationType.PHONE, null);
                empQuickViewList = setPropertiesAndGetEmployeeViewList(cursor, groupBy, otherData);
                return empQuickViewList;
            default:
                return empQuickViewList;
        }
    }

    public static List<EmployeeQuickView> setPropertiesAndGetEmployeeViewList(Cursor cursor, GroupBy groupBy, String moreData) {
        List<EmployeeQuickView> viewEmployeeContactList = new ArrayList<EmployeeQuickView>();
        EmployeeQuickView empQuickView = null;
        List<String> strArray = new ArrayList<String>();
        if (moreData != null) {
            String[] array = moreData.split(",");
            for (String s : array) {
                strArray.add(s);
            }
        }
        ColumnPropertyIndex columnPropertyIndex = null;
        if (cursor.moveToFirst()) {
            do {
                if (columnPropertyIndex == null) {
                    columnPropertyIndex = new ColumnPropertyIndex(cursor.getColumnIndex("EmployeeId"),
                            cursor.getColumnIndex("FirstName"),
                            cursor.getColumnIndex("LastName"),
                            cursor.getColumnIndex("JobTitle"),
                            cursor.getColumnIndex("Status"),
                            cursor.getColumnIndex("StatusText"),
                            cursor.getColumnIndex("StatusColor"),
                            cursor.getColumnIndex("Following"),
                            cursor.getColumnIndex("Picture"),
                            cursor.getColumnIndex("LoginEmail"));
                }

                empQuickView = new EmployeeQuickView();
                // Mapping the QuickView Properties.
                empQuickView.setEmployeeProperties(cursor, groupBy, columnPropertyIndex);
                // Mapping the other required information with dictionary  as key/value.
                empQuickView.mapOtherDataProperties(strArray, cursor);
                viewEmployeeContactList.add(empQuickView);
            } while (cursor.moveToNext());
        }
        return viewEmployeeContactList;
    }

    public static List<EmployeeQuickView> setByStatusPropertiesAndGetEmployeeViewList(Cursor cursor, GroupBy groupBy, String moreData) {
        List<EmployeeQuickView> viewEmployeeContactList = new ArrayList<EmployeeQuickView>();
        EmployeeQuickView empQuickView = null;
        List<String> strArray = new ArrayList<String>();
        HashMap<String, String> list = new HashMap<String, String>();
        if (moreData != null) {
            String[] array = moreData.split(",");
            for (String s : array) {
                strArray.add(s);
            }
        }
        String date = "";
        ColumnPropertyIndex columnPropertyIndex = null;
        if (cursor.moveToFirst()) {
            do {
                if (columnPropertyIndex == null) {
                    columnPropertyIndex = new ColumnPropertyIndex(cursor.getColumnIndex("EmployeeId"),
                            cursor.getColumnIndex("FirstName"),
                            cursor.getColumnIndex("LastName"),
                            cursor.getColumnIndex("JobTitle"),
                            cursor.getColumnIndex("Status"),
                            cursor.getColumnIndex("StatusText"),
                            cursor.getColumnIndex("StatusColor"),
                            cursor.getColumnIndex("Following"),
                            cursor.getColumnIndex("Picture"),
                            cursor.getColumnIndex("LoginEmail"));
                    columnPropertyIndex.setModifiedDateIndex(cursor.getColumnIndex("ModifiedDate"));
                }
                String id = cursor.getString(columnPropertyIndex.getEmpIdIndex()).toLowerCase();
                String mDate = cursor.getString(columnPropertyIndex.getModifiedDateIndex());
                date = list.get(id);
                if(date  != null) {
                    Date date1 = Utils.dateFromString(mDate,true,true);
                    Date date2 = Utils.dateFromString(date,true,true);
                    if(date1.compareTo(date2) < 0){
                        continue;
                    }else{
                        for(int i = viewEmployeeContactList.size() - 1; i >= 0; i--) {
                            if (viewEmployeeContactList.get(i).employeeId.toString().equalsIgnoreCase(id) ){
                                viewEmployeeContactList.remove(i);
                                break;
                            }
                        }
                    }

                }

                list.put(id, mDate);
                empQuickView = new EmployeeQuickView();
                // Mapping the QuickView Properties.
                empQuickView.setEmployeeProperties(cursor, groupBy, columnPropertyIndex);
                // Mapping the other required information with dictionary  as key/value.
                empQuickView.mapOtherDataProperties(strArray, cursor);
                viewEmployeeContactList.add(empQuickView);
            } while (cursor.moveToNext());
        }
        return viewEmployeeContactList;
    }

    /**
     * Method is used to map employee other required information.
     * /// :param: strArray: It is a list of column to fetch data.
     * /// ResultSet: It is a data row.
     *
     * @param strArray
     * @param cursor
     */
    private void mapOtherDataProperties(List<String> strArray, Cursor cursor) {
        if (!strArray.isEmpty()) {
            this.moreDataDict = new HashMap<String, String>();
            String columnProperties = "";
            for (int i = strArray.size() - 1; i >= 0; i--) {
                columnProperties = strArray.get(i).trim();
                if (!"".equals(columnProperties)) {
                    // strArray
                    String value = cursor.getString(cursor.getColumnIndex(columnProperties));
                    if (value != null) {
                        this.moreDataDict.put(columnProperties, value.replaceAll("''", "'"));
                    } else {
                        this.moreDataDict.put(columnProperties, "");
                    }
                }
            }
        }
    }

    /**
     * Method is used to get employee specific follower employee list.
     * <p/>
     * :param: otherData: It is a list of columns seperated by comma(,) for which we want to get
     * extra information other then employee.
     *
     * @param userId
     * @param groupBy
     * @param otherData
     * @return List<EmployeeQuickView>
     * @throws EwpException
     */
    public static List<EmployeeQuickView> getFollowerEmployeeQuickViewList(UUID userId, GroupBy groupBy, String otherData) throws EwpException {
        EmployeeDataService service = new EmployeeDataService();
        Cursor cursor = service.getEmployeeFollowerAsResultSet(userId, "");
        List<EmployeeQuickView> empQuickViewList = setPropertiesAndGetEmployeeViewList(cursor, groupBy, otherData);
        return empQuickViewList;
    }

    /**
     * @param userId
     * @return List<EmployeeQuickView>
     */
    public static List<EmployeeQuickView> getTeamListFromUserId(UUID userId) throws EwpException {
        EmployeeDataService employeeDataService = new EmployeeDataService();
        Cursor cursor = employeeDataService.getMyTeamEmployeeListAsResultSet(userId);
        List<EmployeeQuickView> empQuickViewList = setPropertiesAndGetEmployeeViewList(cursor, GroupBy.TEAM, "");
        return empQuickViewList;
    }

    /// Mehtod is used to get employee status list.
    ///
    /// :param: otherData: It is a list of columns seperated by comma(,) for which we want to get
    /// extra information other then employee.
    /// For example in case my status: UI coder should pass following column names GroupByStatus,
    /// FirstGroup.
    public static List<EmployeeQuickView> getMyStatusListFromUserId(String otherData, String searchValue) throws EwpException {
        EmployeeStatusDataService service = new EmployeeStatusDataService();
        Cursor cursor = service.getMyStatusListAsResultSet(searchValue);
        if (cursor != null) {
            List<EmployeeQuickView> empQuickViewList = setPropertiesAndGetEmployeeViewList(cursor, GroupBy.EMPLOYEE_STATUS, otherData);
            return empQuickViewList;
        }
        return null;
    }

    /**
     * Method is used to arrange the favorite item order.
     *
     * @param userId
     * @param sortedList
     * @throws EwpException
     */
    public static void sortFavoriteItemOrder(UUID userId, List<EmployeeQuickView> sortedList) throws EwpException {
        PFUserEntityLinkDataService service = new PFUserEntityLinkDataService();
        List<PFUserEntityLink> pfUserEntityLinks = service.getUserEntityListFromSourceTypeAndLinkType(EwpSession.getSharedInstance().getTenantId(), LinkType.FAVOURITE.getId(), 31, userId);
        PFUserEntityLink userLink = null;
        DatabaseOps.defaultDatabase().beginTransaction();
        for (int i = 0; i < sortedList.size(); i++) {
            for (int j = 0; j < pfUserEntityLinks.size(); j++) {
                userLink = pfUserEntityLinks.get(j);
                if (userLink.getEntityId().equals(sortedList.get(i).getGroupId()) && userLink.getSortOrder() != i+1) {
                    int order = i + 1;
                    userLink.setSortOrder(order);
                    service.update(userLink);
                    pfUserEntityLinks.remove(j);
                    break;
                }
            }
        }
       service.rearrangeSortOrder(LinkType.FAVOURITE.getId(),0,null,userId);
        DatabaseOps.defaultDatabase().commitTransaction();
    }

    /// Mehtod is used to get all employee status list.
    ///
    /// :param: otherData: It is a list of columns seperated by comma(,) for which we want to get
    /// extra information other then employee.
    /// For example in case my status: UI coder should pass following column names GroupByStatus,
    /// FirstGroup.
    public static List<EmployeeQuickView> getAllEmployeeStatusListFromUserId(String otherData, String searchValue) throws EwpException {
        EmployeeStatusDataService service = new EmployeeStatusDataService();
        Cursor cursor = service.getTodayEmployeeStatusListInGroup(EwpSession.getSharedInstance().getTenantId(), searchValue);
        List<EmployeeQuickView> empQuickViewList = new ArrayList<EmployeeQuickView>();
        if (cursor != null) {
            empQuickViewList = setByStatusPropertiesAndGetEmployeeViewList(cursor, GroupBy.EMPLOYEE_STATUS, otherData);
        }
        return empQuickViewList;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(int employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getEmployeeStatusText() {
        return employeeStatusText;
    }

    public void setEmployeeStatusText(String employeeStatusText) {
        this.employeeStatusText = employeeStatusText;
    }

    public String getGroupPicture() {
        return groupPicture;
    }

    public void setGroupPicture(String groupPicture) {
        this.groupPicture = groupPicture;
    }

    public DatabaseOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(DatabaseOperationType operationType) {
        this.operationType = operationType;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeQuickView that = (EmployeeQuickView) o;

        return employeeId.equals(that.employeeId);

    }

    @Override
    public int hashCode() {
        return employeeId.hashCode();
    }

    public void setGroupName(String groupName) {

        this.groupName = groupName;
    }

    public Map<String, String> getMoreDataDict() {
        return moreDataDict;
    }

    public void setMoreDataDict(Map<String, String> moreDataDict) {
        this.moreDataDict = moreDataDict;
    }

    public void setEmployeeStatusColor(int color) {
        employeeStatusColor = color;
    }

    public int getEmployeeStatusColor() {
        return employeeStatusColor;
    }

    static class ColumnPropertyIndex {
        private int empIdIndex;
        private int firstNameIndex;
        private int lastNameIndex;
        private int jobTitleIndex;
        private int statusIndex;
        private int statusTextIndex;
        private int statusColorIndex;
        private int pictureIndex;
        private int followingIndex;
        private int loginEmailIndex;
        private int modifiedDateIndex;

        public ColumnPropertyIndex(int empIdIndex, int firstNameIndex, int lastNameIndex, int jobTitleIndex, int statusIndex, int statusTextIndex, int statusColorIndex, int followingIndex, int pictureIndex, int loginEmailIndex) {
            this.empIdIndex = empIdIndex;
            this.firstNameIndex = firstNameIndex;
            this.lastNameIndex = lastNameIndex;
            this.jobTitleIndex = jobTitleIndex;
            this.statusIndex = statusIndex;
            this.statusTextIndex = statusTextIndex;
            this.statusColorIndex = statusColorIndex;
            this.followingIndex = followingIndex;
            this.pictureIndex = pictureIndex;
            this.loginEmailIndex = loginEmailIndex;
        }

        public int getModifiedDateIndex() {
            return modifiedDateIndex;
        }

        public void setModifiedDateIndex(int modifiedDateIndex) {
            this.modifiedDateIndex = modifiedDateIndex;
        }

        public int getLoginEmailIndex() {
            return loginEmailIndex;
        }

        public int getEmpIdIndex() {
            return empIdIndex;
        }

        public int getFirstNameIndex() {
            return firstNameIndex;
        }

        public int getLastNameIndex() {
            return lastNameIndex;
        }

        public int getJobTitleIndex() {
            return jobTitleIndex;
        }

        public int getStatusTextIndex() {
            return statusTextIndex;
        }

        public int getStatusIndex() {
            return statusIndex;
        }

        public int getStatusColorIndex() {
            return statusColorIndex;
        }

        public int getPictureIndex() {
            return pictureIndex;
        }

        public int getFollowingIndex() {
            return followingIndex;
        }

    }

}
