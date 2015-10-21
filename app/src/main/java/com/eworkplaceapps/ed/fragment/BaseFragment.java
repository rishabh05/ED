//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Shrey Sharma
// Original Date: 16 June 2015
//===============================================================================
package com.eworkplaceapps.ed.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eworkplaceapps.ed.EdApplication;
import com.eworkplaceapps.ed.R;
import com.eworkplaceapps.ed.adapter.CallOptionDialogAdapter;
import com.eworkplaceapps.ed.adapter.ParentAdapter;
import com.eworkplaceapps.ed.listeners.FollowUpsListener;
import com.eworkplaceapps.ed.utils.ExpandAnimation;
import com.eworkplaceapps.ed.utils.Utils;
import com.eworkplaceapps.ed.view.GroupListView;
import com.eworkplaceapps.employeedirectory.employee.Employee;
import com.eworkplaceapps.employeedirectory.employee.EmployeeDataService;
import com.eworkplaceapps.employeedirectory.employee.EmployeeQuickView;
import com.eworkplaceapps.platform.communication.Communication;
import com.eworkplaceapps.platform.communication.CommunicationDataService;
import com.eworkplaceapps.platform.exception.EwpException;
import com.eworkplaceapps.platform.helper.EwpSession;
import com.eworkplaceapps.platform.logging.LogConfigurer;
import com.eworkplaceapps.platform.utils.Tuple;
import com.eworkplaceapps.platform.utils.enums.CommunicationType;
import com.eworkplaceapps.platform.utils.enums.EmailType;
import com.eworkplaceapps.platform.utils.enums.PhoneType;
import com.mikepenz.materialdrawer.view.BezelImageView;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.eworkplaceapps.ed.utils.Utils.INFO_TYPE_DEPARTMENT;
import static com.eworkplaceapps.ed.utils.Utils.INFO_TYPE_LOCATION;
import static com.eworkplaceapps.ed.utils.Utils.INFO_TYPE_TEAM;

/**
 * Created by Shrey on 6/16/2015.
 */
public class BaseFragment extends Fragment implements Animation.AnimationListener, AdapterView.OnItemClickListener {
    private final Logger log = Logger.getLogger(BaseFragment.class);
    protected static final String FOOTER = "Footer";
    protected Animation animSlideUp, animSideDown;
    private LinearLayout selected = null;
    public static LinearLayout selectedLast = null;

    private ImageView followUpIcon;
    private EmployeeQuickView employeeQuickView;
    private FollowUpsListener followUpsListener;
    protected List<GroupItem> groupItemList;
    private long lastClick;
    public boolean isAdmin = false;
    protected int[] groupStatus;
    public List<UUID> groupIdList;
    protected List<GroupItem> groupItemSearchList;
    public static Activity activity;
    private long childClickedAt = -1;
    private int groupClickedAt = -1;
    public static EmployeeQuickView selectedEmpQuickView = null;
    public static int selectedPos = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        animationInitialization();
        activity = getActivity();
        groupIdList = new ArrayList<>();
        groupItemSearchList = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        // hide keypad
//        Utils.hideKeyboard(getActivity());
    }

    /**
     * define access role
     */
    public void applyRole() {
        EmployeeDataService employeeDataService = new EmployeeDataService();
        Employee loggedInEmployee = null;
        try {
            loggedInEmployee = employeeDataService.getEmployeeFromUserId(EwpSession.getSharedInstance().getUserId());
        } catch (EwpException e) {
            Log.d("AllFragment", "EwpException-->" + e);
            LogConfigurer.error("AllFragment", "EwpException-->" + e);
        }
        if (Utils.employeeIsAdmin(getActivity(), loggedInEmployee.getEntityId(), EwpSession.getSharedInstance().getUserId(), EwpSession.getSharedInstance().getTenantId())) {
            isAdmin = true;
        } else {
            isAdmin = false;
        }
    }

    /**
     * @param followUpsListener
     */
    public void setFollowUpsListener(FollowUpsListener followUpsListener) {
        this.followUpsListener = followUpsListener;
    }


    private void animationInitialization() {
        animSlideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        animSideDown = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_down);
        animSlideUp.setAnimationListener(this);
        animSideDown.setAnimationListener(this);
    }

    /**
     * to show dialog for choose options for action
     */
    public void chooseFrom(Context context, List<Communication> communicationList, String whereFrom) {
        showDialog(context, communicationList, whereFrom);
    }

    /**
     * to perform action of call/email/sms
     *
     * @param clickedAppName
     * @param whereFrom
     */
    public static void dialogActions(Context context, String clickedAppName, String whereFrom) {
        if (!"".equals(clickedAppName) && !"sms".equalsIgnoreCase(clickedAppName) && !"email".equalsIgnoreCase(clickedAppName) && !"phone".equalsIgnoreCase(clickedAppName)) {
            Utils.alertDialog.dismiss();
            if ("CALL".equalsIgnoreCase(whereFrom)) {
                Intent intent = Utils.makeACall(clickedAppName);
                context.startActivity(intent);
            } else if ("SMS".equalsIgnoreCase(whereFrom)) {
                Intent smsIntent = Utils.smsIntent(clickedAppName);
                context.startActivity(smsIntent);
            } else if ("EMAIL".equalsIgnoreCase(whereFrom)) {
                Utils.sendEmail(new String[]{clickedAppName}, context);
            } else {
                Utils.alertDialog.dismiss();
            }
        }
    }

    /**
     * to open dialog for contact list
     *
     * @param context
     * @param communicationList
     * @param whereFrom
     */
    public void showDialog(final Context context, final List<Communication> communicationList, final String whereFrom) {

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView textView = (TextView) view.findViewById(R.id.id_call_value);
                String clickedAppName = textView.getText().toString();
                dialogActions(context, clickedAppName, whereFrom);
            }
        };

        View.OnClickListener cancelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.alertDialog.dismiss();
            }
        };
        //cListener to set white background of row after cancel dialog
        DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        };
        CallOptionDialogAdapter callOptionAdapter = new CallOptionDialogAdapter(context, communicationList, "all");
        Utils.openDialogSheet(context, callOptionAdapter, itemClickListener, cancelListener, cancelClickListener);
    }

    /**
     * open chat
     *
     * @param context
     * @param employeeDataService
     * @param employeeQuickView
     */
    protected void chatCallListener(Context context, EmployeeDataService employeeDataService, EmployeeQuickView employeeQuickView) {
        // perform action for chat
        Toast.makeText(getActivity(), "Perform chat action", Toast.LENGTH_LONG).show();
    }

    /**
     * if a user has only one phone number, method will directly make a call on to it otherwise a list
     * of all the numbers is displayed for user to choose from
     *
     * @param context
     * @param employeeDataService
     * @param employeeQuickView
     */
    protected void phoneCallListener(Context context, EmployeeDataService employeeDataService, EmployeeQuickView employeeQuickView) {
        try {
            List<Communication> communicationList;
            communicationList = employeeDataService.getEmployeePhoneList(employeeQuickView.getEmployeeId(), null);
            if (communicationList.size() == 1) {
                // make direct call
                Intent intent = Utils.makeACall(communicationList.get(0).getValue());
                startActivity(intent);
            } else {
                // display list
                chooseFrom(context, communicationList, "CALL");
            }
        } catch (EwpException e) {
            log.debug("BaseFragment EwpException-> " + e);
            Log.d("BaseFragment", "BaseFragment EwpException-> " + e);
        }
    }

    /**
     * if user has single mobile number, an intent to compose message will be fired otherwise a list with
     * more than one phone numbers is displayed
     *
     * @param context
     * @param employeeDataService
     * @param employeeQuickView
     */
    protected void smsListener(Context context, EmployeeDataService employeeDataService, EmployeeQuickView employeeQuickView) {
        try {
            List<Communication> communicationList;
            communicationList = employeeDataService.getEmployeePhoneList(employeeQuickView.getEmployeeId(), PhoneType.MOBILE);
            if (communicationList.size() == 1) {
                // if only one phone then sms ef
                Intent smsIntent = Utils.smsIntent(communicationList.get(0).getValue());
                startActivity(smsIntent);
            } else {
                //display list to choose from
                chooseFrom(context, communicationList, "SMS");
            }
        } catch (EwpException e) {
            log.debug("BaseFragment EwpException-> " + e);
            Log.d("BaseFragment", "BaseFragment EwpException-> " + e);
        }
    }

    /**
     * if user has single email, an intent to compose email will be fired otherwise a list with
     * more than one email is displayed
     *
     * @param context
     * @param employeeDataService
     * @param employeeQuickView
     */
    protected void emailListener(Context context, EmployeeDataService employeeDataService, EmployeeQuickView employeeQuickView) {
        try {
            CommunicationDataService communicationDataService = new CommunicationDataService();
            Employee employee = (Employee) employeeDataService.getEntity(employeeQuickView.getEmployeeId());
            //Employee me = employeeDataService.getEmployeeFromUserId(EwpSession.getSharedInstance().getUserId());
            List<Communication> communicationList = communicationDataService.getCommunicationListFromTypeAndSubtype(employeeQuickView.getEmployeeId(), CommunicationType.EMAIL.getId(), 0);
            Communication comm = new Communication();
            comm.setValue(employee.getLoginEmail());
            comm.setCommunicationSubType(EmailType.WORK.getId());
            comm.setCommunicationType(CommunicationType.EMAIL);
            communicationList.add(comm);
            if (communicationList.size() == 1) {
                Utils.sendEmail(new String[]{employee.getLoginEmail()}, context);
            } else {
                chooseFrom(context, communicationList, "EMAIL");
            }
        } catch (EwpException e) {
            log.debug("BaseFragment EwpException-> " + e);
            Log.d("BaseFragment", "BaseFragment EwpException-> " + e);
        }
    }

    /**
     * follow/ un-follow employee and reflect it on view
     *
     * @param employeeDataService
     * @param employeeQuickView
     */
    protected void followUnFollow(EmployeeDataService employeeDataService, EmployeeQuickView employeeQuickView) {
        try {
            if (employeeQuickView.isFollowing()) {
                employeeDataService.markAsFollowUp(employeeQuickView.getEmployeeId(), false);
                employeeQuickView.setFollowing(false);
                followUpIcon.setVisibility(View.INVISIBLE);
            } else {
                employeeDataService.markAsFollowUp(employeeQuickView.getEmployeeId(), true);
                employeeQuickView.setFollowing(true);
                followUpIcon.setVisibility(View.VISIBLE);
            }
            if (followUpsListener != null) {
                followUpsListener.onFollowUpClick(employeeQuickView);
            }

        } catch (EwpException e) {
            log.debug("BaseFragment EwpException-> " + e);
            Log.d("BaseFragment", "BaseFragment EwpException-> " + e);
        }
    }

    /**
     * invalidate orange panel
     */
    protected void invalidateView() {
        groupClickedAt = -1;
        childClickedAt = -1;
    }

    /**
     * common method,gets called from all employees, myteams, by team,location,department and follow ups
     * allow user to perform five different options available on orange action bar which gets expand on the
     * click event of list view
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    protected void whenItemClickedFromList(final Context context, final AdapterView<?> parent, final View view, final int position, long id, ChildItem childItem) {
        groupClickedAt = position;
        childClickedAt = id;
        parent.postDelayed(new Runnable() {
            public void run() {
                if (position == parent.getAdapter().getCount() - 1) {
                    ((ListView) parent).smoothScrollToPositionFromTop(position, 50, 500);
                    selected.requestFocus();
                }
            }
        }, 100L);
        if (parent.getItemAtPosition(position) instanceof ParentAdapter.Item) {
            employeeQuickView = ((ParentAdapter.Item) parent.getItemAtPosition(position)).employeeQuickView;
            selectedEmpQuickView = employeeQuickView;
            selectedPos = position;

            int height = ((ListView) parent).getHeight();
            int itemHeight = ((ListView) parent).getChildAt(0).getHeight();
            if (position == ((ListView) parent).getLastVisiblePosition() || position == ((ListView) parent).getLastVisiblePosition() - 1) {
                ((ListView) parent).smoothScrollToPositionFromTop(position, 500, 500);
            } else {
                if (position == ((ListView) parent).getFirstVisiblePosition()) {
                    ((ListView) parent).smoothScrollToPositionFromTop(position, 50, 500);
                }
            }
        } else if (parent.getItemAtPosition(position) instanceof EmployeeQuickView) {
            employeeQuickView = (EmployeeQuickView) parent.getItemAtPosition(position);
            int height = ((ListView) parent).getHeight();
            int itemHeight = ((ListView) parent).getChildAt(0).getHeight();
            if (position == ((ListView) parent).getLastVisiblePosition() || position == ((ListView) parent).getLastVisiblePosition() - 1) {
                ((ListView) parent).smoothScrollToPositionFromTop(position, 500, 500);
            } else {
                if (position == ((ListView) parent).getFirstVisiblePosition()) {
                    ((ListView) parent).smoothScrollToPositionFromTop(position, 50, 500);
                }
            }
        } else if (groupItemList != null && position < groupItemList.size()
                && groupItemList.get(position) instanceof GroupItem) {
            if (childItem != null) {
                employeeQuickView = childItem.employeeQuickView;
                Object lastObject = parent.getAdapter().getItem(parent.getLastVisiblePosition());
                Object lastSecondObject = parent.getAdapter().getItem(parent.getLastVisiblePosition() - 1);
                Object firstObject = parent.getAdapter().getItem(parent.getFirstVisiblePosition());
                if (lastObject instanceof ChildItem) {
                    if (((ChildItem) lastObject).employeeQuickView.equals(employeeQuickView) && ((ChildItem) lastObject).employeeQuickView.getGroupId().equals(employeeQuickView.getGroupId())) {
                        applyDelayThenScroll(parent, parent.getLastVisiblePosition(), 500, 500);
                    }
                }
                if (lastSecondObject instanceof ChildItem) {
                    if (((ChildItem) lastSecondObject).employeeQuickView.equals(employeeQuickView) && ((ChildItem) lastSecondObject).employeeQuickView.getGroupId().equals(employeeQuickView.getGroupId())) {
                        applyDelayThenScroll(parent, parent.getLastVisiblePosition() - 1, 500, 500);
                    }
                }
                if (firstObject instanceof ChildItem) {
                    if (((ChildItem) firstObject).employeeQuickView.equals(employeeQuickView) && ((ChildItem) firstObject).employeeQuickView.getGroupId().equals(employeeQuickView.getGroupId())) {
                        applyDelayThenScroll(parent, parent.getFirstVisiblePosition(), 50, 500);
                    }
                }
            }
        } else {
            return;
        }
        if (employeeQuickView == null) {
            return;
        }
        // previously selected item
        if (selected != null && selected.isShown()) {
            ExpandAnimation expandAni = new ExpandAnimation(selected, 400);
            selected.startAnimation(expandAni);
            selected = null;
        }
        final EmployeeDataService employeeDataService = new EmployeeDataService();
        selected = ((LinearLayout) view.findViewById(R.id.id_option_panel));
        selectedLast = selected;
        ImageButton phoneCall = (ImageButton) view.findViewById(R.id.id_phone_call);
        ImageButton chat = (ImageButton) view.findViewById(R.id.id_chat);
        ImageButton phoneSms = (ImageButton) view.findViewById(R.id.id_phone_sms);
        ImageButton email = (ImageButton) view.findViewById(R.id.id_email);
        followUpIcon = (ImageView) view.findViewById(R.id.id_followup_flag);
        ImageButton followup = (ImageButton) view.findViewById(R.id.id_followup);
        ExpandAnimation expandAni = new ExpandAnimation(selected, 400);
        // Starts animation on the hidden layout
        if (selected == null) {
            return;
        }
        selected.startAnimation(expandAni);
        Tuple.Tuple2<Boolean, Boolean, String> phoneTuple = null;
        try {
            phoneTuple = employeeDataService.isEmployeePhoneAndMobileExist(employeeQuickView.getEmployeeId());
            if (phoneTuple != null) {
                if (!phoneTuple.getT1()) {  // sms icon will disable if mobile no. not exist
                    phoneSms.setEnabled(false);
                    phoneSms.setColorFilter(context.getResources().getColor(R.color.lightGrey));
                } else { // sms icon will enable if mobile no. exist
                    phoneSms.setEnabled(false);
                    phoneSms.setColorFilter(context.getResources().getColor(R.color.lightGrey));
                }
                if (!phoneTuple.getT1() && !phoneTuple.getT2()) { // call icon will disable if contact no. not exist
                    phoneCall.setEnabled(false);
                    phoneCall.setColorFilter(context.getResources().getColor(R.color.lightGrey));
                } else { // call icon will enable if contact no. exist
                    phoneCall.setEnabled(false);
                    phoneCall.setColorFilter(context.getResources().getColor(R.color.lightGrey));
                }
            }
        } catch (EwpException e) {
            log.debug("BaseFragment EwpException-> " + e);
            Log.d("BaseFragment", "BaseFragment EwpException-> " + e);
        }
        followup.setEnabled(false);
        followup.setColorFilter(context.getResources().getColor(R.color.lightGrey));

        email.setEnabled(false);
        email.setColorFilter(context.getResources().getColor(R.color.lightGrey));
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatCallListener(context, employeeDataService, employeeQuickView);
            }
        });

        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneCallListener(context, employeeDataService, employeeQuickView);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailListener(context, employeeDataService, employeeQuickView);
            }
        });
        phoneSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsListener(context, employeeDataService, employeeQuickView);
            }
        });
        followup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followUnFollow(employeeDataService, employeeQuickView);
            }
        });
    }

    public void applyDelayThenScroll(final AdapterView<?> parent, final int position, final int offSet, final int duration) {
        parent.postDelayed(new Runnable() {
            public void run() {
                ((GroupListView) parent).smoothScrollToPositionFromTop(position, offSet, duration);
            }
        }, 100L);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        long now = System.currentTimeMillis();
        if (lastClick != 0 && now - lastClick <= 500) {
            return;
        } else {
            lastClick = now;
        }
        whenItemClickedFromList(getActivity(), parent, view, position, id, null);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    /**
     * class for creating Group data (Header data)
     */
    public static class GroupItem implements Serializable {
        public String title;
        public UUID id;
        public String location;
        public String groupLogo;
        public List<ChildItem> items = new ArrayList<ChildItem>();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GroupItem groupItem = (GroupItem) o;
            return !(id != null ? !id.equals(groupItem.id) : groupItem.id != null);
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }

    /**
     * class for creating Child data (Child data for particular Header)
     */
    public static class ChildItem implements Serializable {
        public EmployeeQuickView employeeQuickView;
    }

    /**
     * class for holding Child view component
     */
    protected static class ChildHolder {
        TextView firstName, lastName, title, status, anythingElse, timeStamp;
        LinearLayout dataPanel;
        BezelImageView profileImageView;
        ImageView imgFlagView;
        View viewDivider;
        FrameLayout footerPanel;
        LinearLayout footerLine;
        View panelView;
    }

    /**
     * class for holding Group view component
     */
    protected static class GroupHolder {
        TextView title;
        TextView groupTitle;
        TextView txtAddress1;
        TextView txtAddress;
        ImageButton imgPlus;
        BezelImageView imgGroupLogo;
        RelativeLayout titlePanel, imgGroupLogoLayout;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    public class LocationDeptListAdapter extends GroupListView.AnimatedExpandableListAdapter implements Filterable {
        private LayoutInflater inflater;
        private View convertView1 = null;
        private GroupHolder holder;
        private List<GroupItem> items;
        private GroupListView expandableListView;
        private String intentType = "";
        private boolean shouldCollapse = true;

        public LocationDeptListAdapter(Context context, GroupListView expandableListView) {
            inflater = LayoutInflater.from(context);
            this.expandableListView = expandableListView;
            shouldCollapse = true;
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
            groupStatus = new int[items.size()];
            setListEvent();
        }

        public void shouldCollapse(boolean shouldCollapseList) {
            shouldCollapse = shouldCollapseList;
        }

        public List<GroupItem> getData() {
            return this.items;
        }

        public void setType(String intentType) {
            this.intentType = intentType;
        }

        public String getType() {
            return intentType;
        }

        private void setListEvent() {
            expandableListView
                    .setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int position) {
                            // check the length of array
                            if (groupStatus.length > position) {
                                groupStatus[position] = 1;
                            }
                        }
                    });

            expandableListView
                    .setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                        @Override
                        public void onGroupCollapse(int position) {
                            // check the length of array
                            if (groupStatus.length > position) {
                                groupStatus[position] = 0;
                            }
                        }
                    });
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            ChildItem childItem = null;
            //check for the given position or index is in list or not
            if (items.size() > groupPosition && items.get(groupPosition).items.size() > childPosition) {
                childItem = items.get(groupPosition).items.get(childPosition);
            }
            return childItem;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.row_title);
                holder.title.setTypeface(EdApplication.HELVETICA_NEUE);
                holder.status = (TextView) convertView.findViewById(R.id.status);
                holder.status.setTypeface(EdApplication.HELVETICA_NEUE);
                holder.profileImageView = (BezelImageView) convertView.findViewById(R.id.profile_image_view);
                holder.imgFlagView = (ImageView) convertView.findViewById(R.id.id_followup_flag);
                holder.firstName = (TextView) convertView.findViewById(R.id.first_name);
                holder.firstName.setTypeface(EdApplication.HELVETICA_NEUE, Typeface.BOLD);
                holder.lastName = (TextView) convertView.findViewById(R.id.last_name);
                holder.lastName.setTypeface(EdApplication.HELVETICA_NEUE);
                holder.dataPanel = (LinearLayout) convertView.findViewById(R.id.dataPanel);
                holder.footerPanel = (FrameLayout) convertView.findViewById(R.id.footerPanel);
                holder.viewDivider = convertView.findViewById(R.id.divider);
                holder.footerLine = (LinearLayout) convertView.findViewById(R.id.id_footer_line);
                holder.panelView = (View) convertView.findViewById(R.id.id_option_panel);

                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            if (item != null && item.employeeQuickView != null) {
                if (FOOTER.equalsIgnoreCase(item.employeeQuickView.getFirstName())) {
                    holder.footerPanel.setVisibility(View.VISIBLE);
                    holder.dataPanel.setVisibility(View.GONE);
                } else {
                    String fName = item.employeeQuickView.getFirstName();
                    String lName = item.employeeQuickView.getLastName();
                    boolean val = com.eworkplaceapps.platform.utils.Utils.emptyUUID().equals(item.employeeQuickView.getEmployeeId());
                    if (fName != null && lName != null && !"".equals(fName) && !"".equals(lName) && !val) {
                        holder.footerPanel.setVisibility(View.GONE);
                        holder.dataPanel.setVisibility(View.VISIBLE);
                        holder.firstName.setText(item.employeeQuickView.getFirstName());
                        holder.lastName.setText(item.employeeQuickView.getLastName());
                        holder.title.setText(item.employeeQuickView.getJobTitle());
                        holder.status.setText(item.employeeQuickView.getEmployeeStatusText());
//                    holder.status.setTextColor(Utils.getStatusColor(getActivity(), item.employeeQuickView.getEmployeeStatusText()));
                        holder.status.setTextColor(Color.rgb(item.employeeQuickView.getStatusRGBColor().getT1(), item.employeeQuickView.getStatusRGBColor().getT2(), item.employeeQuickView.getStatusRGBColor().getT3()));

                        String byteArray = item.employeeQuickView.getPicture();
                        if (item.employeeQuickView.getPicture() == null || "".equals(item.employeeQuickView.getPicture())) {
                            holder.profileImageView.setTag(childPosition);
                        } else {
                            holder.profileImageView.setTag(byteArray);
                        }
                        if (item.employeeQuickView.isFollowing()) {
                            holder.imgFlagView.setVisibility(View.VISIBLE);
                        } else {
                            holder.imgFlagView.setVisibility(View.INVISIBLE);
                        }
               /*     holder.status.setTextColor(Utils.getStatusColor(getActivity(),
                            item.employeeQuickView.getEmployeeStatusText()));*/
//                    holder.status.setTextColor(Color.rgb(item.employeeQuickView.getStatusRGBColor().getT1(), item.employeeQuickView.getStatusRGBColor().getT2(), item.employeeQuickView.getStatusRGBColor().getT3()));

                    }
                }
                if (childPosition < items.get(groupPosition).items.size() - 2) {
                    holder.viewDivider.setVisibility(View.VISIBLE);
                } else {
                    holder.viewDivider.setVisibility(View.GONE);
                }
            }


            // Resets the toolbar to be closed and condition
            //check the clicked position to remain open on clicking on open and close icon of header
            if (!(childClickedAt == childPosition && groupClickedAt == groupPosition)) {
                View panelView = convertView.findViewById(R.id.id_option_panel);
                ((LinearLayout.LayoutParams) panelView.getLayoutParams()).bottomMargin = -40;
                panelView.setVisibility(View.GONE);
            }
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            // check the length of array
            if (items.size() > groupPosition) {
                return items.get(groupPosition).items.size();
            } else {
                return 0;
            }
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            if (items != null && items.size() > groupPosition)
                return items.get(groupPosition);
            return null;
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
            final GroupItem item = getGroup(groupPosition);
            convertView1 = convertView;
            if (convertView1 == null) {
                holder = new GroupHolder();
                convertView1 = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView1.findViewById(R.id.textTitle);
                holder.title.setTypeface(EdApplication.HELVETICA_NEUE, Typeface.BOLD);
                holder.groupTitle = (TextView) convertView1.findViewById(R.id.textTitle1);
                holder.groupTitle.setTypeface(EdApplication.HELVETICA_NEUE, Typeface.BOLD);
                holder.txtAddress1 = (TextView) convertView1.findViewById(R.id.text_address1);
                holder.txtAddress1.setTypeface(EdApplication.HELVETICA_NEUE, Typeface.BOLD);
                holder.txtAddress = (TextView) convertView1.findViewById(R.id.text_address);
                holder.txtAddress.setTypeface(EdApplication.HELVETICA_NEUE, Typeface.BOLD);
                holder.imgPlus = (ImageButton) convertView1.findViewById(R.id.id_expand_view);
                holder.imgGroupLogo = (BezelImageView) convertView1.findViewById(R.id.imgGroup);
                holder.titlePanel = (RelativeLayout) convertView1.findViewById(R.id.titlePanel);
                holder.imgGroupLogoLayout = (RelativeLayout) convertView1.findViewById(R.id.lay_rel_img);
                convertView1.setTag(holder);
            } else {
                holder = (GroupHolder) convertView1.getTag();
            }
            if (groupPosition < groupStatus.length && groupStatus[groupPosition] == 0) {
                holder.imgPlus.setVisibility(View.VISIBLE);
                holder.title.setVisibility(View.GONE);
                holder.txtAddress.setVisibility(View.GONE);
                holder.groupTitle.setVisibility(View.VISIBLE);
                holder.imgPlus.setBackgroundResource(R.drawable.circle_button_background);
                holder.imgPlus.setImageDrawable(getResources().getDrawable(R.drawable.arrow_expand));
                if (INFO_TYPE_LOCATION.equals(getType())) {
                    holder.txtAddress1.setVisibility(View.VISIBLE);
                } else {
                    holder.txtAddress1.setVisibility(View.GONE);
                }

            } else {
                holder.imgPlus.setBackgroundResource(R.drawable.up_button_style);
                holder.imgPlus.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_up));
                holder.title.setVisibility(View.VISIBLE);
                holder.txtAddress.setVisibility(View.VISIBLE);
                holder.groupTitle.setVisibility(View.GONE);
                holder.txtAddress1.setVisibility(View.GONE);
            }
            if (item != null) {
                String picture = item.groupLogo;
                holder.imgGroupLogo.setTag(picture);
                if (picture != null && !"".equals(picture)) {

                } else if (INFO_TYPE_DEPARTMENT.equals(intentType)) {
                    holder.imgGroupLogo.setImageDrawable(getResources().getDrawable(R.drawable.default_department));
                } else if (INFO_TYPE_TEAM.equals(intentType)) {
                    holder.imgGroupLogo.setImageDrawable(getResources().getDrawable(R.drawable.default_teams));
                } else if (INFO_TYPE_LOCATION.equals(intentType)) {
                    holder.imgGroupLogo.setImageDrawable(getResources().getDrawable(R.drawable.default_location));
                }
                holder.title.setText(item.title);
                holder.groupTitle.setText(item.title);
                if (INFO_TYPE_LOCATION.equals(getType())) {
                    holder.txtAddress.setText(item.location);
                    holder.txtAddress1.setText(item.location);
                } else {
                    holder.txtAddress.setVisibility(View.GONE);
                }
            }
            holder.imgPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invalidateView();
                    if (expandableListView.isGroupExpanded(groupPosition)) {
//                        if (shouldCollapse) {
                        convertView1 = inflater.inflate(R.layout.group_item, parent, false);
                        convertView1.setTag(holder);
                        expandableListView.collapseGroupWithAnimation(groupPosition);
                        if (item != null) {
                            groupIdList.remove(item.id);
                        }
//                        }
                    } else {
                        convertView1 = inflater.inflate(R.layout.group_item, parent, false);
                        convertView1.setTag(holder);
                        expandableListView.expandGroupWithAnimation(groupPosition);
                        if (item != null) {
                            groupIdList.add(item.id);
                        }
                    }
                }
            });
            holder.imgGroupLogo.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                 /*   Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
                    if (INFO_TYPE_DEPARTMENT.equals(intentType)) {
                        intent.putExtra(INFO_TYPE, INFO_TYPE_DEPARTMENT);
                        intent.putExtra(INTENT_FROM, BY_DEPARTMENT);
                        intent.putExtra(TEAM_NAME, item.id.toString());
                        startActivity(intent);
                    } else if (INFO_TYPE_TEAM.equals(intentType)) {
                        intent.putExtra(INFO_TYPE, INFO_TYPE_TEAM);
                        intent.putExtra(INTENT_FROM, BY_TEAM);
                        intent.putExtra(TEAM_NAME, item.id.toString());
                        startActivity(intent);
                    } else if (INFO_TYPE_LOCATION.equals(intentType)) {
                        intent.putExtra(INFO_TYPE, INFO_TYPE_LOCATION);
                        intent.putExtra(INTENT_FROM, BY_LOCATION);
                        intent.putExtra(TEAM_NAME, item.id.toString());
                        startActivity(intent);
                    }*/
                }
            });
            if (groupIdList != null) {
                for (UUID id : groupIdList) {
                    if (id.equals(item.id) && !expandableListView.isGroupExpanded(groupPosition)) {
                        holder.imgPlus.setBackgroundResource(R.drawable.up_button_style);
                        holder.imgPlus.setImageResource(R.drawable.ic_action_up);
                        expandableListView.expandGroup(groupPosition);
                    }
                }
            }
            return convertView1;
        }

        public void clearExpandedList() {
            groupIdList.clear();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

        @Override
        public Filter getFilter() {
            return null;
        }
    }

    /**
     * method prepares group list for more sections views which contains headers and children,
     * list is prepared on the basis of three categories, for team,location and department we take group name or
     * group id to group employees whereas in status we use status text for grouping. Also at the last index of every
     * child we add footer to render custom view according to the need
     *
     * @param id                    if id=1 it means grouping is performing on status else id=0 its
     *                              for team,location or department
     * @param employeeQuickViewList list of employees from database
     */

    protected void prepareData(int id, List<EmployeeQuickView> employeeQuickViewList) {
        String previous = "", val = "";
        GroupItem groupItem = null;
        for (int i = 0; i < employeeQuickViewList.size(); i++) {
            EmployeeQuickView employee = employeeQuickViewList.get(i);
            val = employee.getGroupId().toString();
            if (id == 1) {
                val = employee.getEmployeeStatusText();
            }
            if (previous.equals(val)) {
                if (!com.eworkplaceapps.platform.utils.Utils.emptyUUID().equals(employee.getEmployeeId())) {
                    ChildItem childItem = new ChildItem();
                    childItem.employeeQuickView = employee;
                    if (id == 2) {
                        EmployeeDataService empData = new EmployeeDataService();
                        UUID empId = null;
                        try {
                            empId = empData.getEmployeeFromUserId(EwpSession.getSharedInstance().getUserId()).getEntityId();
                        } catch (EwpException e) {
                            e.printStackTrace();
                        }

                        if (!(employee.getEmployeeId().equals(empId)))
                            groupItem.items.add(childItem);
                    } else {
                        groupItem.items.add(childItem);
                    }
                }
            } else {
              /*  if (groupItem != null && groupItem.items != null) {
                    List<ChildItem> sortedChildItem = Utils.performSortingForGroup(groupItem.items);
                    groupItem.items.clear();
                    groupItem.items.addAll(sortedChildItem);
                }*/

                groupItem = new GroupItem();
                if (id == 1) {
                    previous = employee.getEmployeeStatusText();
                    groupItem.title = employee.getEmployeeStatusText();
                } else {
                    previous = employee.getGroupId().toString();
                    groupItem.title = employee.getGroupName();
                }
                groupItem.id = employee.getGroupId();
                groupItem.groupLogo = employee.getGroupPicture();
                if (!com.eworkplaceapps.platform.utils.Utils.emptyUUID().equals(employee.getEmployeeId())) {
                    ChildItem childItem = new ChildItem();
                    childItem.employeeQuickView = employee;
                    // check for the loggedIn user is an part of Team so we don't have to show this employee in list
                    if (id == 2) {
                        EmployeeDataService empData = new EmployeeDataService();
                        UUID empId = null;
                        try {
                            empId = empData.getEmployeeFromUserId(EwpSession.getSharedInstance().getUserId()).getEntityId();
                        } catch (EwpException e) {
                            e.printStackTrace();
                        }
                        if (!(employee.getEmployeeId().equals(empId)))
                            groupItem.items.add(childItem);
                    } else {
                        groupItem.items.add(childItem);
                    }
                }
                groupItemList.add(groupItem);
            }
            if (i < employeeQuickViewList.size() - 1) {
                if (id == 1) {
                    // sorting
                    // possibility of sorting
                    List<ChildItem> sortedChildItem = Utils.performSortingForGroup(groupItem.items);
                    groupItem.items.clear();
                    groupItem.items.addAll(sortedChildItem);
                    if (!employeeQuickViewList.get(i).getEmployeeStatusText().equals(employeeQuickViewList.get(i + 1).getEmployeeStatusText())) {
                        ChildItem footer = new ChildItem();
                        footer.employeeQuickView = new EmployeeQuickView();
                        footer.employeeQuickView.setFirstName(FOOTER);
                        int statusColorValue = getColorCode(employeeQuickViewList.get(i));
                        footer.employeeQuickView.setEmployeeStatusColor(statusColorValue);
                        groupItem.items.add(footer);
                    }
                } else {
                    // sorting
                    // possibility of sorting
                    List<ChildItem> sortedChildItem = Utils.performSortingForGroup(groupItem.items);
                    groupItem.items.clear();
                    groupItem.items.addAll(sortedChildItem);
                    if (!employeeQuickViewList.get(i).getGroupId().toString().equals(employeeQuickViewList.get(i + 1).getGroupId().toString())) {
                        ChildItem footer = new ChildItem();
                        footer.employeeQuickView = new EmployeeQuickView();
                        footer.employeeQuickView.setFirstName(FOOTER);
                        groupItem.items.add(footer);
                    }
                }
            } else {
                ChildItem footer = new ChildItem();
                footer.employeeQuickView = new EmployeeQuickView();
                footer.employeeQuickView.setFirstName(FOOTER);
                int statusColorValue = getColorCode(employeeQuickViewList.get(i));
                footer.employeeQuickView.setEmployeeStatusColor(statusColorValue);
                groupItem.items.add(footer);
            }
        }
    }

    /**
     * To get the status color code from RGB
     *
     * @param employee
     * @return
     */
    private int getColorCode(EmployeeQuickView employee) {
        Tuple.Tuple2<Integer, Integer, Integer> statusColorTuple = employee.getStatusRGBColor();
        int red = statusColorTuple.getT1();
        int green = statusColorTuple.getT2();
        int blue = statusColorTuple.getT3();
        return Color.rgb(red, green, blue);
    }

    /**
     * method to refresh Adapter for Team on follow up click
     *
     * @param employeeQuickView
     */
    protected void refreshToLocDeptTeamAdapter(LocationDeptListAdapter adapter, List<GroupItem> filteredItem, GroupListView groupListView, EditText searchView, String intentFor, EmployeeQuickView employeeQuickView) {
        //check for the adapter is null or not
        if (adapter != null) {
            int numberOfGroups = adapter.getGroupCount();
            // loop to get expanded group count
            boolean[] groupExpandedArray = new boolean[numberOfGroups];
            for (int i = 0; i < numberOfGroups; i++) {
                groupExpandedArray[i] = groupListView.isGroupExpanded(i);
            }
            String searchStr = searchView.getText().toString();
            // check for the search string if search string is not blank then show record accordingly
            if (searchStr.equals("")) {
                for (GroupItem grp : groupItemList) {
                    for (ChildItem child : grp.items) {
                        if (employeeQuickView.getEmployeeId().equals(child.employeeQuickView.getEmployeeId())) {
                            child.employeeQuickView.setFollowing(employeeQuickView.isFollowing());
                        }
                    }
                }
                // first notyfy to adapter
                adapter.notifyDataSetChanged();
                adapter.setData(groupItemList);
            } else {
                for (GroupItem grp : groupItemList) {
                    for (ChildItem child : grp.items) {
                        if (employeeQuickView.getEmployeeId().equals(child.employeeQuickView.getEmployeeId())) {
                            child.employeeQuickView.setFollowing(employeeQuickView.isFollowing());
                        }
                    }
                }
                // Check for filtered item is null or not
                if (filteredItem != null) {
                    for (GroupItem grp : filteredItem) {
                        for (ChildItem child : grp.items) {
                            if (employeeQuickView.getEmployeeId().equals(child.employeeQuickView.getEmployeeId())) {
                                child.employeeQuickView.setFollowing(employeeQuickView.isFollowing());
                            }
                        }
                    }
                    // first notyfy to adapter
                    adapter.notifyDataSetChanged();
                    adapter.setData(filteredItem);
                }
            }
            adapter.setType(intentFor);
            adapter.notifyDataSetChanged();
            // loop to open expanded group after notify the adapter
            for (int i = 0; i < groupExpandedArray.length; i++) {
                if (groupExpandedArray[i]) {
                    groupListView.expandGroup(i);
                }
            }
        }
    }
}
