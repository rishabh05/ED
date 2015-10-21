package com.eworkplaceapps.ed.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eworkplaceapps.ed.EdApplication;
import com.eworkplaceapps.ed.HomeActivity;
import com.eworkplaceapps.ed.R;
import com.eworkplaceapps.ed.adapter.AllEmployeesListAdapter;
import com.eworkplaceapps.ed.adapter.ParentAdapter;
import com.eworkplaceapps.ed.dataloader.DataLoader;
import com.eworkplaceapps.ed.listeners.FollowUpsListener;
import com.eworkplaceapps.ed.listeners.FragmentBackPressedListener;
import com.eworkplaceapps.ed.listeners.IShowedFragment;
import com.eworkplaceapps.ed.model.NoCaseComparator;
import com.eworkplaceapps.ed.quickscroll.QuickScroll;
import com.eworkplaceapps.ed.utils.Utils;
import com.eworkplaceapps.ed.view.ProgressWheel;
import com.eworkplaceapps.employeedirectory.employee.EmployeeQuickView;
import com.eworkplaceapps.employeedirectory.employee.GroupBy;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.logging.LogConfigurer;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * fragment class for all employee screen
 */
public class AllFragment extends BaseFragment implements View.OnClickListener, FragmentBackPressedListener, FollowUpsListener, IShowedFragment, AdapterView.OnItemLongClickListener, View.OnTouchListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ID = 501;
    private AccountHeader.Result headerResult = null;
    private Drawer.Result result = null;
    private AllEmployeesListAdapter adapter;
    private List<Object[]> alphabet = new ArrayList<Object[]>();
    private Map<String, Integer> sections = new HashMap<String, Integer>();
    private LinearLayout searchWidgetLayout;
    private ImageView searchBackPress;
    private EditText mSearchView;
    private LinearLayout newLayoutRef;
    private View view;
    private MenuItem search;
    public static RelativeLayout relativePanel;
    public static AllFragment allFragment;
    private ListView mListView;
    private List<EmployeeQuickView> employeeQuickViewList;
    private LinearLayout layout;
    private RefreshReceiver refreshReceiver;
    private ProgressWheel loading;
    private TextView noDataText;
    private List<ParentAdapter.Row> rows;
    private Parcelable state;
    private static ImageButton addContact;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all, container, false);
        allFragment = this;
        setHasOptionsMenu(true);
        init();

        setFollowUpsListener(this);
        initializeLists();
        //new AllEmployeesTask(true).execute();
        loading.setVisibility(View.VISIBLE);
        getActivity().getSupportLoaderManager().initLoader(ID, null, this);
        IntentFilter intentFilter = new IntentFilter(Utils.REFRESH_INTENT);
        refreshReceiver = new RefreshReceiver();
        getActivity().registerReceiver(refreshReceiver, intentFilter);
        return view;
    }

    private void initializeLists() {
        employeeQuickViewList = new ArrayList<EmployeeQuickView>();
        rows = new ArrayList<ParentAdapter.Row>();
    }

    /**
     * method to draw side index (alphabetic index), its basically to return layout
     *
     * @param context
     * @param typeface
     * @return
     */
    public static View createAlphabetTrack(Context context, Typeface typeface) {
        LinearLayout layout = new LinearLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (30 * context.getResources().getDisplayMetrics().density), LinearLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        final LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.weight = 1;
        final int height = context.getResources().getDisplayMetrics().heightPixels;
        int iterate = 0;
        if (height >= 1024) {
            iterate = 1;
            layout.setWeightSum(26);
        } else {
            iterate = 2;
            layout.setWeightSum(13);
        }
        for (char character = 'A'; character <= 'Z'; character += iterate) {
            layout.addView(getTextView(context, Character.toString(character), typeface, textParams));
        }
        layout.addView(getTextView(context, "#", typeface, textParams));
        return layout;
    }

    /**
     * method to create text for side index bar
     *
     * @param context
     * @param text
     * @param typeface
     * @param textParams
     * @return
     */
    private static TextView getTextView(Context context, String text, Typeface typeface, LinearLayout.LayoutParams textParams) {
        final TextView textview = new TextView(context);
        textview.setLayoutParams(textParams);
        textview.setTypeface(typeface, Typeface.BOLD);
        textview.setTag(R.color.lightGrey);
        textview.setTextColor(context.getResources().getColor(R.color.lightGrey));
        textview.setGravity(Gravity.CENTER_HORIZONTAL);
        textview.setText(text);
        return textview;
    }

    /**
     * method to return current fragment instance
     *
     * @return
     */
    public static AllFragment getAllFragmentInstance() {
        return allFragment;
    }

    /**
     * method to prepare list of employee quickview
     *
     * @throws EwpException
     */
    public void prepareListData() throws EwpException {
        employeeQuickViewList = EmployeeQuickView.getEmployeeQuickViewList(GroupBy.NONE, "", "", null);
    }

    /**
     * method to show search widget on clicking on search icon at actionbar
     */
    public void showMySearchWidget() {
        searchWidgetLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);//Menu Resource, Menu
        search = menu.findItem(R.id.action_search);
        search.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (refreshReceiver != null) {
            getActivity().unregisterReceiver(refreshReceiver);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                ((HomeActivity) getActivity()).getSupportActionBar().hide();
                ((HomeActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                ((HomeActivity) getActivity()).hideTabs();
                Utils.showSoftKeyboard(getActivity(), mSearchView);
                showMySearchWidget();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onResume() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
        super.onResume();
    }

  /*  @Override
    public void onSaveInstanceState(Bundle outState) {
        if (result != null) {
            outState = result.saveInstanceState(outState);
        }
        if (headerResult != null) {
            outState = headerResult.saveInstanceState(outState);
        }
        String searchText = mSearchView.getText().toString();
        if (searchText != null && searchText.length() > 0) {
            outState.putString("constraint", searchText);
        }
        super.onSaveInstanceState(outState);
    }*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mSearchView.addTextChangedListener(filterTextWatcher);
        mSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    if (!hasFocus) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                } catch (Exception e) {

                }
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * inner class to handle search widget events
     */
    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            System.out.println("AfterTextChanged");
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            System.out.println("BeforeTextChanged");
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str = s.toString().trim();
            if (adapter != null && str != null)
                adapter.getFilter().filter(str);
            if (!"".equals(mSearchView.getText().toString())) {
                mSearchView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black, 0, R.drawable.ic_close_black, 0);
                mSearchView.setOnTouchListener(AllFragment.this);
            } else {
                mSearchView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black, 0, 0, 0);
                mSearchView.setOnTouchListener(null);
            }
        }
    };

    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }
    });

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.search_view:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (motionEvent.getRawX() >= (mSearchView.getRight() - mSearchView.getCompoundDrawables()[Utils.DRAWABLE_RIGHT].getBounds().width())) {
                        mSearchView.setText("");
                        mSearchView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black, 0, 0, 0);
                        return true;
                    } else {
                        Utils.showSoftKeyboard(getActivity(), mSearchView);
                        return false;
                    }
                }
                return false;
            default:
                if (mSearchView != null) {
                    Utils.hideSoftKeyboardWithoutReq(getActivity(), mSearchView);
                }
                return false;
        }
    }


    @Override
    public void onShowedFragment() {
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle(Utils.actionBarTitle(getResources().getString(R.string.all_employee_title)));
        Log.d(this.getClass().getName(), "Me 1");
    }

    @Override
    public void onFollowUpClick(EmployeeQuickView employeeQuickView) {
        //refresh fav screen
        getActivity().sendBroadcast(Utils.getRefreshIntent(Utils.REFRESH_FAV_INTENT, employeeQuickView));
        //refresh my teams
        getActivity().sendBroadcast(Utils.getRefreshIntent(Utils.REFRESH_MY_TEAMS_INTENT, employeeQuickView));
        //refresh more
        getActivity().sendBroadcast(Utils.getRefreshIntent(Utils.REFRESH_MORE_INTENT, employeeQuickView));
    }

    @Override
    public void onFragmentBackPressed() {
        if (!((HomeActivity) getActivity()).getSupportActionBar().isShowing()) {
            searchBackPress();
        } else {
            ((HomeActivity) getActivity()).finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new DataLoader(getActivity(), GroupBy.NONE, "LocationId,DepartmentId", "", null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        loading.setVisibility(View.GONE);
        if (data != null && loader instanceof DataLoader) {
            DataLoader allLoader = (DataLoader) loader;
            employeeQuickViewList = allLoader.getEmployeeQuickViewList();
            rows = allLoader.getRows();
            adapter.setRows(rows);
            String searchString = mSearchView.getText().toString();
            if (searchString != null && !"".equals(searchString)) {
                adapter.getFilter().filter(searchString);
            } else {
                mListView.setAdapter(adapter);
            }
            onViewCreated();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * class for filter result according to text entered in search widget
     */
    public class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String constraintStr = constraint.toString().trim().toLowerCase(Locale.getDefault());
            FilterResults result = new FilterResults();
            String jobtitle = "";
            if (constraint != null && constraint.toString().length() > 0) {
                List<EmployeeQuickView> filterItems = new ArrayList<EmployeeQuickView>();
                synchronized (this) {
                    for (EmployeeQuickView item : employeeQuickViewList) {
                        jobtitle = "";
                        if (item.getJobTitle() != null) {
                            jobtitle = item.getJobTitle().toLowerCase();
                        }
                        String fullName = item.getFirstName() + " " + item.getLastName();
                        //String fullNameWithoutSp = item.getFirstName() + item.getLastName();
                        if (item.getFirstName().toLowerCase(Locale.getDefault()).contains(constraintStr)
                                || item.getLastName().toLowerCase(Locale.getDefault()).contains(constraintStr)
                                || jobtitle.contains(constraintStr)
                                || item.getEmployeeStatusText().toLowerCase().contains(constraintStr)
                                || fullName.toLowerCase(Locale.getDefault()).contains(constraintStr)) {
                            filterItems.add(item);
                        }
                    }
                    result.count = filterItems.size();
                    result.values = filterItems;
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<EmployeeQuickView> filtered = (ArrayList<EmployeeQuickView>) results.values;
            // sort array and extract sections in background Thread
            if (filtered != null) {
                if (filtered.isEmpty()) {
                    noDataText.setVisibility(View.VISIBLE);
                } else {
                    noDataText.setVisibility(View.GONE);
                }
                setAdapter(filtered);
            } else {
                noDataText.setVisibility(View.GONE);
                setAdapter(employeeQuickViewList);
            }
        }
    }

    /**
     * method to initialize view component
     */
    private void init() {
        addContact = (ImageButton) view.findViewById(R.id.addButton);
        addContact.setOnClickListener(this);
        mSearchView = (EditText) view.findViewById(R.id.search_view);
        mSearchView.setTypeface(EdApplication.HELVETICA_NEUE);
        mSearchView.setHint(Utils.actionBarTitle(getResources().getString(R.string.search_hint)));
        mListView = (ListView) view.findViewById(R.id.list_view);
        searchWidgetLayout = (LinearLayout) view.findViewById(R.id.id_searchWidget);
        loading = (ProgressWheel) view.findViewById(R.id.loading);
        relativePanel = (RelativeLayout) view.findViewById(R.id.relativePanel);
        searchBackPress = (ImageView) view.findViewById(R.id.id_searchViewBackPressed);
        setLayoutRef(searchWidgetLayout);
        searchWidgetLayout.setVisibility(View.GONE);
        adapter = new AllEmployeesListAdapter(getActivity(), EdApplication.HELVETICA_NEUE);
        QuickScroll fastTrack = (QuickScroll) view.findViewById(R.id.quickscroll);
        layout = (LinearLayout) createAlphabetTrack(getActivity(), EdApplication.HELVETICA_NEUE);
        fastTrack.init(-1, mListView, layout, adapter, QuickScroll.STYLE_NONE);
        fastTrack.setFixedSize(2);
        relativePanel.addView(layout);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        searchBackPress = (ImageView) view.findViewById(R.id.id_searchViewBackPressed);
        searchBackPress.setOnClickListener(this);
        noDataText = (TextView) view.findViewById(R.id.tvNoDataMsg);
        noDataText.setTypeface(EdApplication.HELVETICA_NEUE);
        mListView.setOnTouchListener(this);
        mSearchView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }


    /**
     * method for initialize employee list
     */
    private void initLists() {
        try {
            prepareListData();
        } catch (EwpException e) {
            Log.d("AllFragment", "EwpException-->" + e);
            LogConfigurer.error("AllFragment", "EwpException-->" + e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_searchViewBackPressed:
                searchBackPress();
                break;
            case R.id.addButton:
                Toast.makeText(getActivity(), "Add button clicked", Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * method to handle search view back press event
     */
    public void searchBackPress() {
        noDataText.setVisibility(View.GONE);
        searchWidgetLayout.setVisibility(View.GONE);
        mSearchView.setText("");
        mSearchView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black, 0, 0, 0);
        Utils.hideSoftKeyboard(getActivity(), mSearchView);
        ((HomeActivity) getActivity())
                .showMyActionBar();
        ((HomeActivity) getActivity())
                .showMyTabs();
    }

    /**
     * method to set adapter on listview
     *
     * @param employees
     */
    private void setAdapter(List<EmployeeQuickView> employees) {
        getListForAdapter(employees);
        mListView.setAdapter(adapter);
    }

    public void setLayoutRef(LinearLayout searchWidgetLayout) {
        this.newLayoutRef = searchWidgetLayout;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        return true;
    }

    class AllEmployeesTask extends AsyncTask<Void, Void, Void> {

        boolean flag = false;

        public AllEmployeesTask(boolean flag) {
            this.flag = flag;
        }

        @Override
        protected void onPreExecute() {
            if (flag)
                loading.setVisibility(View.VISIBLE);
            mListView.setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            initLists();
            getListForAdapter(employeeQuickViewList);
            return null;
        }

        @Override
        protected void onPostExecute(Void type) {
            if (flag) {
                loading.setVisibility(View.GONE);
            }
            mListView.setEnabled(true);
            mListView.setAdapter(adapter);
            setAdapter(employeeQuickViewList);
            //     mListView.setSelection(Utils.SELECTED_LISTVIEW_POSITION);
        }
    }

    private void getListForAdapter(List<EmployeeQuickView> employees) {
        if (employees != null) {
            Collections.sort(employees, new NoCaseComparator());
            Pattern numberPattern = Pattern.compile(Utils.NUMBER_PATTERN);
            Pattern specialCharacterPattern = Pattern.compile(Utils.SPECIALCHARACTER_PATTERN);
            List<EmployeeQuickView> dataList = new ArrayList<EmployeeQuickView>(employees);
            List<EmployeeQuickView> numList = new ArrayList<EmployeeQuickView>();
            for (EmployeeQuickView emp : dataList) {
                String fl = String.valueOf(emp.getFirstName().charAt(0));
                if (numberPattern.matcher(fl).matches() || specialCharacterPattern.matcher(fl).matches()) {
                    numList.add(emp);
                    employees.remove(emp);
                } else {
                    break;
                }
            }
            employees.addAll(numList);
            List<AllEmployeesListAdapter.Row> rows = new ArrayList<AllEmployeesListAdapter.Row>();
            int start = 0;
            int end = 0;
            String previousLetter = null;
            Object[] tmpIndexItem = null;

            for (EmployeeQuickView e : employees) {
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
            adapter.setRows(rows);
            adapter.notifyDataSetChanged();
        }
    }

    public class RefreshReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String type = intent.getStringExtra(Utils.INFO_TYPE);
                String info = intent.getStringExtra(Utils.INFO);
                if (Utils.REFRESH.equals(type)) {
                    EmployeeQuickView emp = (EmployeeQuickView) intent.getSerializableExtra(Utils.MESSAGE);
                    if (emp != null && Utils.DELETE.equals(info) && employeeQuickViewList.contains(emp)) {
                        int index = employeeQuickViewList.indexOf(emp);
                        EmployeeQuickView val = employeeQuickViewList.remove(index);
                        getListForAdapter(employeeQuickViewList);
                        if (adapter != null) {
                            // check for the search string first, if search string is not null then filter records according to this
                            String searchString = mSearchView.getText().toString();
                            if (searchString != null && !"".equals(searchString)) {
                                adapter.getFilter().filter(searchString);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        }
                        return;
                    }
                    if (emp != null) {
                        if (employeeQuickViewList != null && employeeQuickViewList.contains(emp)) {
                            employeeQuickViewList.get(employeeQuickViewList.indexOf(emp)).setFollowing(emp.isFollowing());
                            employeeQuickViewList.get(employeeQuickViewList.indexOf(emp)).setEmployeeStatusText(emp.getEmployeeStatusText());
                        }
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                        return;
                    }
                }
            }
            // new AllEmployeesTask(true).execute();
            getActivity().getSupportLoaderManager().restartLoader(ID, null, AllFragment.this);
        }
    }

    public List<EmployeeQuickView> getEmployeeQuickViewList() {
        return employeeQuickViewList;
    }

    public List<ParentAdapter.Row> getRows() {
        return rows;
    }

    @Override
    public void onPause() {
        // Save ListView state @ onPause
        Log.d("AllFragment-------->", "saving listView state @ onPause");
        state = mListView.onSaveInstanceState();
        super.onPause();
    }

    public void onViewCreated() {
        // Restore previous state (including selected item index and scroll position)
        if (state != null) {
            Log.d("AllFragment-------->", "trying to restore listView state..");
            mListView.onRestoreInstanceState(state);
        }
    }
}
