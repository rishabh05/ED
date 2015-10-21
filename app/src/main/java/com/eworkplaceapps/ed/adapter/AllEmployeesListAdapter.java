package com.eworkplaceapps.ed.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eworkplaceapps.ed.EdApplication;
import com.eworkplaceapps.ed.R;
import com.eworkplaceapps.ed.fragment.AllFragment;
import com.eworkplaceapps.ed.quickscroll.Scrollable;
import com.eworkplaceapps.ed.utils.Utils;
import com.mikepenz.materialdrawer.view.BezelImageView;
import com.nhaarman.listviewanimations.util.Swappable;

import java.util.Arrays;
import java.util.List;

/**
 * Adapter for inflating xml for all employee listview
 */
public class AllEmployeesListAdapter extends ParentAdapter implements Filterable, Swappable, Scrollable {
    ViewHolder holder = null;
    private Typeface indexTypeFace = null;
    private ListView adListView;
    // context object
    private Context mContext;
    private Boolean flag2 = false;
    private List<Row> rows;
    private String specialChar = "";
    int selectedIndex = -1;

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        Row firstItem = rows.set(positionOne, getItem(positionTwo));
        notifyDataSetChanged();
        rows.set(positionTwo, firstItem);
    }

    public AllEmployeesListAdapter(Context context, Typeface indexTypeFace) {
        this.mContext = context;
        this.indexTypeFace = indexTypeFace;
    }

    public void setListToAd(ListView adListView) {
        this.adListView = adListView;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Row getItem(int position) {
        Row row = null;
        // check the position
        if (rows.size() > position) {
            row = rows.get(position);
        }
        return row;
    }

    @Override
    public long getItemId(int position) {
        // check for null
        if (getItem(position) != null) {
            return getItem(position).hashCode();
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Section) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (getItemViewType(position) == 0) { // Item
            if (view == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.row_item, parent, false);
            }
            final Item item = (Item) getItem(position);
            holder.divider = view.findViewById(R.id.divider);
            holder.firstName = (TextView) view.findViewById(R.id.first_name);
            holder.firstName.setTypeface(EdApplication.HELVETICA_NEUE, Typeface.BOLD);
            holder.lastName = (TextView) view.findViewById(R.id.last_name);
            holder.lastName.setTypeface(EdApplication.HELVETICA_NEUE);
            holder.title = (TextView) view.findViewById(R.id.row_title);
            holder.title.setTypeface(EdApplication.HELVETICA_NEUE);
            holder.status = (TextView) view.findViewById(R.id.status);
            holder.status.setTypeface(EdApplication.HELVETICA_NEUE);
            holder.imgFollowUpIcon = (ImageView) view.findViewById(R.id.id_followup_flag);
            holder.mCurrentProfileView = (BezelImageView) view.findViewById(R.id.profile_image_view);
            holder.panelView = (View) view.findViewById(R.id.id_option_panel);
            //check for the divider visible or hide at the top of header
            if (position < rows.size() - 1) {
                if (rows.get(position) instanceof Item && rows.get(position + 1) instanceof Item) {
                    String val1 = ((Item) rows.get(position)).employeeQuickView.getFirstName();
                    String val2 = ((Item) rows.get(position + 1)).employeeQuickView.getFirstName();
                    if ("#".equals(specialChar)) {
                        holder.divider.setVisibility(View.VISIBLE);
                    } else {
                        if (val1.toLowerCase().charAt(0) == val2.toLowerCase().charAt(0)) {
                            holder.divider.setVisibility(View.VISIBLE);  //divider visible
                        } else {
                            holder.divider.setVisibility(View.GONE);
                        }
                    }
                } else {
                    holder.divider.setVisibility(View.GONE); //divider hide
                }
            }

            String fName = item.employeeQuickView.getFirstName();
            String lName = item.employeeQuickView.getLastName();
            boolean val = com.eworkplaceapps.platform.utils.Utils.emptyUUID().equals(item.employeeQuickView.getEmployeeId());
            if (fName != null && lName != null && !"".equals(fName) && !"".equals(lName) && !val) {
                holder.firstName.setText(item.employeeQuickView.getFirstName());
                holder.lastName.setText(item.employeeQuickView.getLastName());
                holder.title.setText(item.employeeQuickView.getJobTitle());
                String status = item.employeeQuickView.getEmployeeStatusText();
                if (item.employeeQuickView.isFollowing()) {
                    holder.imgFollowUpIcon.setVisibility(View.VISIBLE);
                } else {
                    holder.imgFollowUpIcon.setVisibility(View.GONE);
                }
                holder.status.setText(status);
//                holder.status.setTextColor(Utils.getStatusColor(mContext, status));
//                Utils.getStatusColorFromService(mContext, status, item.employeeQuickView.getStatusRGBColor());

                holder.status.setTextColor(Color.rgb(item.employeeQuickView.getStatusRGBColor().getT1(), item.employeeQuickView.getStatusRGBColor().getT2(), item.employeeQuickView.getStatusRGBColor().getT3()));

            }
            // Resets the toolbar to be closed
//            if (BaseFragment.selectedEmpQuickView != null && BaseFragment.selectedEmpQuickView.getEmployeeId().toString().equals(item.employeeQuickView.getEmployeeId().toString())) {
            /*    View panelView = view.findViewById(R.id.id_option_panel);
                ExpandAnimation expandAni = new ExpandAnimation(panelView, 400);
                panelView.startAnimation(expandAni);
                panelView.setVisibility(View.VISIBLE);*/
             /*   View panelView = view.findViewById(R.id.id_option_panel);
                ((LinearLayout.LayoutParams) panelView.getLayoutParams()).bottomMargin = 40;
                panelView.setVisibility(View.VISIBLE);*/

//            } else {
          /*  if (BaseFragment.selectedLast != null && BaseFragment.selectedPos == position && BaseFragment.selectedLast.getParent() == view) {

            } else {*/
            ((LinearLayout.LayoutParams) holder.panelView.getLayoutParams()).bottomMargin = -40;
            holder.panelView.setVisibility(View.GONE);
//            }
//            }


        } else { // Section
            if (view == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.row_section, parent, false);
            }
            Section section = (Section) getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.textView1);
            textView.setTypeface(EdApplication.HELVETICA_NEUE, Typeface.BOLD);
            textView.setText(section.text);
            if ("#".equals(section.text)) {
                specialChar = "#";
            }
        }
        return view;
    }

    public void updateResults() {
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView firstName;
        public TextView lastName;
        public TextView title;
        public TextView status;
        public ImageView imgFollowUpIcon;
        public BezelImageView mCurrentProfileView;
        public View divider;
        public View panelView;
    }


    @Override
    public Filter getFilter() {
        return AllFragment.getAllFragmentInstance().new ListFilter();
    }


    @Override
    public String getIndicatorForPosition(int arg0, int arg1) {
        if (rows.get(arg0) instanceof Item) {
            return String.valueOf(((Item) rows.get(arg0)).employeeQuickView.getFirstName().charAt(0));
        } else if (rows.get(arg0) instanceof Section) {
            return ((Section) rows.get(arg0)).text;
        } else {
            return "";
        }
    }

    @Override
    public int getScrollPosition(int arg0, int arg1) {
        List<String> alphabet = Arrays.asList(mContext.getResources().getStringArray(R.array.alphabet));
        int i = rows.size();
        //this loop to store position of last text of index which is exist in employee list
        for (int j = 0; j < rows.size(); j++) {
            for (String alpha : alphabet) {
                if (rows.get(j) instanceof Section && alpha.equals(((Section) rows.get(j)).text)) {
                    Utils.textPosition = alphabet.indexOf(alpha);
                }
            }
        }
        if (arg0 < alphabet.size() && arg0 > -1) {
            for (i = 0; i < rows.size(); i++) {
                if (rows.get(i) instanceof Section) {
                    if (alphabet.get(arg0).equals(((Section) rows.get(i)).text)) {
                        Utils.textPosition = arg0;
                        return i;
                    }
                }
            }
            for (i = 0; i < rows.size(); i++) {
                if (rows.get(i) instanceof Section) {
                    for (int j = 0; arg0 + j < alphabet.size(); j++) {
                        if (alphabet.get(arg0 + j).equals(((Section) rows.get(i)).text)) {
                            Utils.textPosition = arg0 + j;
                            return i;
                        }
                    }
                }
            }
        }
        return i;
    }
}