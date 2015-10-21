package com.eworkplaceapps.ed.dataloader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.eworkplaceapps.ed.adapter.AllEmployeesListAdapter;
import com.eworkplaceapps.ed.adapter.ParentAdapter;
import com.eworkplaceapps.ed.model.NoCaseComparator;
import com.eworkplaceapps.ed.utils.Utils;
import com.eworkplaceapps.employeedirectory.employee.EmployeeDataService;
import com.eworkplaceapps.employeedirectory.employee.EmployeeQuickView;
import com.eworkplaceapps.employeedirectory.employee.EmployeeStatusDataService;
import com.eworkplaceapps.employeedirectory.employee.GroupBy;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.logging.LogConfigurer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * data loader for loading cursor in background
 * Created by Shrey on 31/07/2015.
 */
public class DataLoader extends CursorLoader {
    private static final String TAG = "DataLoader";
    private GroupBy groupBy;
    private String moreData, searchValue;
    private UUID groupId;
    private List<EmployeeQuickView> employeeQuickViewList;
    private List<Object[]> alphabet = new ArrayList<Object[]>();
    private Map<String, Integer> sections = new HashMap<String, Integer>();
    private List<ParentAdapter.Row> rows;

    public DataLoader(Context context) {
        super(context);
    }

    public DataLoader(Context context, GroupBy groupBy, String moreData, String searchValue, UUID groupId) {
        super(context);
        this.groupBy = groupBy;
        this.moreData = moreData;
        this.searchValue = searchValue;
        this.groupId = groupId;
        employeeQuickViewList = new ArrayList<EmployeeQuickView>();
    }

    @Override
    public Cursor loadInBackground() {
        try {
            EmployeeDataService service = new EmployeeDataService();
            Cursor cursor = null;
            //special case for follow ups
            if (groupBy == GroupBy.NONE && "FollowUps".equals(searchValue)) {
                cursor = service.getEmployeeFollowerAsResultSet(EwpSession.getSharedInstance().getUserId(), "");
                employeeQuickViewList = EmployeeQuickView.setPropertiesAndGetEmployeeViewList(cursor, groupBy, moreData);
                return cursor;
            }
            //special case for status
            if (groupBy == GroupBy.EMPLOYEE_STATUS) {
                EmployeeStatusDataService employeeStatusDataService = new EmployeeStatusDataService();
                cursor = employeeStatusDataService.getTodayEmployeeStatusListInGroup(EwpSession.getSharedInstance().getTenantId(), searchValue);
                employeeQuickViewList = EmployeeQuickView.setPropertiesAndGetEmployeeViewList(cursor, GroupBy.EMPLOYEE_STATUS, moreData);
                return cursor;
            }
            // for other cases like department,location, teams and all employee data
            switch (groupBy) {
                case DEPARTMENT:
                    cursor = service.getEmployeeListByDepartmentAsResultSet(groupId, searchValue);
                    employeeQuickViewList = EmployeeQuickView.setPropertiesAndGetEmployeeViewList(cursor, groupBy, moreData);
                    break;
                case LOCATION:
                    cursor = service.getEmployeeListByLocation(groupId, searchValue);
                    employeeQuickViewList = EmployeeQuickView.setPropertiesAndGetEmployeeViewList(cursor, groupBy, moreData);
                    break;
                case TEAM:
                    cursor = service.getEmployeeListByTeamAsResultSet(groupId, searchValue);
                    employeeQuickViewList = EmployeeQuickView.setPropertiesAndGetEmployeeViewList(cursor, groupBy, moreData);
                    break;
                default:
                    cursor = service.searchEmployeeAndGetAsResultSet(searchValue);
                    employeeQuickViewList = EmployeeQuickView.setPropertiesAndGetEmployeeViewList(cursor, groupBy, moreData);
                    setSectionAndRows(employeeQuickViewList);
                    break;
            }
            return cursor;
        } catch (EwpException ex) {
            LogConfigurer.error(TAG, "EwpException-->" + ex);
            Log.d(TAG, "EwpException-->" + ex);
        }
        return null;
    }

    public GroupBy getGroupBy() {
        return groupBy;
    }

    public String getMoreData() {
        return moreData;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public List<EmployeeQuickView> getEmployeeQuickViewList() {
        return employeeQuickViewList;
    }

    public List<ParentAdapter.Row> getRows() {
        return rows;
    }


    /**
     * prepares list with sections and rows
     */
    public void setSectionAndRows(List<EmployeeQuickView> employeeQuickViewList) {
        if (employeeQuickViewList != null) {
            rows = new ArrayList<AllEmployeesListAdapter.Row>();
            Collections.sort(employeeQuickViewList, new NoCaseComparator());
            Pattern numberPattern = Pattern.compile(Utils.NUMBER_PATTERN);
            Pattern specialCharacterPattern = Pattern.compile(Utils.SPECIALCHARACTER_PATTERN);
            List<EmployeeQuickView> dataList = new ArrayList<EmployeeQuickView>(employeeQuickViewList);
            List<EmployeeQuickView> numList = new ArrayList<EmployeeQuickView>();
            for (EmployeeQuickView emp : dataList) {
                String fl = String.valueOf(emp.getFirstName().charAt(0));
                if (numberPattern.matcher(fl).matches() || specialCharacterPattern.matcher(fl).matches()) {
                    numList.add(emp);
                    employeeQuickViewList.remove(emp);
                } else {
                    break;
                }
            }
            employeeQuickViewList.addAll(numList);
            int start = 0;
            int end = 0;
            String previousLetter = null;
            Object[] tmpIndexItem = null;

            for (EmployeeQuickView e : employeeQuickViewList) {
                if (e.getFirstName() != null && !"".equals(e.getFirstName())) {
                    String firstLetter = e.getFirstName().toUpperCase().substring(0, 1);
                    if (numberPattern.matcher(firstLetter).matches() || specialCharacterPattern.matcher(firstLetter).matches()) {
                        firstLetter = "#";
                    }
                    if (previousLetter != null && !firstLetter.equals(previousLetter)) {
                        end = rows.size() - 1;
                        tmpIndexItem = new Object[3];
                        tmpIndexItem[0] = previousLetter;
                        tmpIndexItem[1] = start;
                        tmpIndexItem[2] = end;
                        alphabet.add(tmpIndexItem);
                        start = end + 1;
                    }
                    if (!firstLetter.equals(previousLetter)) {
                        rows.add(new AllEmployeesListAdapter.Section(firstLetter));
                        sections.put(firstLetter, start);
                    }
                    rows.add(new AllEmployeesListAdapter.Item(e));
                    previousLetter = firstLetter;
                }
            }
            if (previousLetter != null) {
                tmpIndexItem = new Object[3];
                tmpIndexItem[0] = previousLetter;
                tmpIndexItem[1] = start;
                tmpIndexItem[2] = rows.size() - 1;
                alphabet.add(tmpIndexItem);
            }
        }
    }
}