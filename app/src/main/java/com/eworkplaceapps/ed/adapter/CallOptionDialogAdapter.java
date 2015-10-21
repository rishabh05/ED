package com.eworkplaceapps.ed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eworkplaceapps.ed.EdApplication;
import com.eworkplaceapps.ed.R;
import com.eworkplaceapps.platform.communication.Communication;
import com.eworkplaceapps.platform.utils.enums.CommunicationType;
import com.eworkplaceapps.platform.utils.enums.EmailType;
import com.eworkplaceapps.platform.utils.enums.PhoneType;

import java.util.List;

/**
 * adapter class for handling bottom dialog
 */
public class CallOptionDialogAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Communication> communicationList;
    private Context mContext;
    private String type = "";

    public CallOptionDialogAdapter(Context context, List<Communication> communicationList, String type) {
        layoutInflater = LayoutInflater.from(context);
        this.communicationList = communicationList;
        this.mContext = context;
        this.type = type;
    }

    @Override
    public int getCount() {
        return communicationList.size();
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
        Communication comm = communicationList.get(position);
        if (comm.getCommunicationType() == CommunicationType.EMAIL) {
            String type = EmailType.getEmailTypeListAsDictionary().get(comm.getCommunicationSubType());
            if (type != null)
                type = type.toLowerCase();
            viewHolder.textCallType.setText(type);
        } else if (comm.getCommunicationType() == CommunicationType.PHONE) {
            String type = PhoneType.getPhoneTypeListAsDictionary().get(comm.getCommunicationSubType());
            if (type != null)
                type = type.toLowerCase();
            viewHolder.textCallType.setText(type);
        }
        if ("phone".equalsIgnoreCase(comm.getValue())) {
            viewHolder.textCallValue.setTextColor(mContext.getResources().getColor(R.color.lightGrey));
        } else if ("email".equalsIgnoreCase(comm.getValue())) {
            viewHolder.textCallValue.setTextColor(mContext.getResources().getColor(R.color.lightGrey));
        } else if ("sms".equalsIgnoreCase(comm.getValue())) {
            viewHolder.textCallValue.setTextColor(mContext.getResources().getColor(R.color.lightGrey));
        } else if ("cancel".equalsIgnoreCase(comm.getValue())) {
            viewHolder.textCallValue.setTextColor(mContext.getResources().getColor(R.color.color_dark_red));
            viewHolder.textCallType.setText("");
        } else if ("call".equalsIgnoreCase(comm.getValue())) {
            viewHolder.textCallValue.setTextColor(mContext.getResources().getColor(R.color.lightGrey));
        } else if ("Add to Favorite".equalsIgnoreCase(comm.getValue())) {
            viewHolder.textCallValue.setTextColor(mContext.getResources().getColor(R.color.lightGrey));
            viewHolder.textCallType.setText("");
        } else {
            viewHolder.textCallValue.setTextColor(mContext.getResources().getColor(R.color.lightTeal));
        }
        viewHolder.textCallType.setTextColor(mContext.getResources().getColor(R.color.orange));
        viewHolder.textCallValue.setText(comm.getValue());
        if (!"all".equalsIgnoreCase(type) && comm.isFavorite()) {
            viewHolder.textCallType.setTextColor(mContext.getResources().getColor(R.color.lightGrey));
            viewHolder.textCallValue.setTextColor(mContext.getResources().getColor(R.color.lightGrey));
        }
        return view;
    }

    static class ViewHolder {
        TextView textCallType;
        TextView textCallValue;
    }
}
