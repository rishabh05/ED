//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 12 June 2015
//===============================================================================
package com.eworkplaceapps.ed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.eworkplaceapps.ed.EdApplication;
import com.eworkplaceapps.ed.R;
import com.eworkplaceapps.ed.model.ProfileInfoContact;

import java.util.List;

/**
 * adapter class for handling bottom dialog for communication's
 */
public class CommunicationDialogAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<ProfileInfoContact> pictureOptionList;
    private Context mContext;

    public CommunicationDialogAdapter(Context context, List<ProfileInfoContact> pictureOptionList) {
        layoutInflater = LayoutInflater.from(context);
        this.pictureOptionList = pictureOptionList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return pictureOptionList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.call_option_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textCallType = (TextView) view.findViewById(R.id.id_call_type);
            viewHolder.textCallType.setTypeface(EdApplication.HELVETICA_NEUE);
            viewHolder.textCallValue = (TextView) view.findViewById(R.id.id_call_value);
            viewHolder.textCallValue.setTypeface(EdApplication.HELVETICA_NEUE);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textCallType.setText(pictureOptionList.get(position).getContactType());
        viewHolder.textCallValue.setText(pictureOptionList.get(position).getContactValue());

        if (viewHolder.textCallType.getText() != null && viewHolder.textCallType.getText().toString().equalsIgnoreCase("cancel")) {
            viewHolder.textCallType.setTextColor(mContext.getResources().getColor(R.color.color_dark_red));
        } else if (viewHolder.textCallType.getText() != null && viewHolder.textCallType.getText().toString().equalsIgnoreCase("call")) {
            viewHolder.textCallType.setTextColor(mContext.getResources().getColor(R.color.lightGrey));

        } else if (viewHolder.textCallType.getText() != null && viewHolder.textCallType.getText().toString().equalsIgnoreCase("Add to Favorites")) {
            viewHolder.textCallType.setTextColor(mContext.getResources().getColor(R.color.lightGrey));

        } else {
            viewHolder.textCallType.setTextColor(mContext.getResources().getColor(R.color.orange));

        }

        return view;
    }

    static class ViewHolder {
        TextView textCallType;
        TextView textCallValue;
    }
}
