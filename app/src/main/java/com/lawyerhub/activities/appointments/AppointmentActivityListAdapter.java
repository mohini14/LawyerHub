package com.lawyerhub.activities.appointments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lawyerhub.R;
import com.lawyerhub.enums.AppointmentType;
import com.lawyerhub.model.AppointmentModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointmentActivityListAdapter extends BaseAdapter {

    List<AppointmentModel> mItems;
    Context mContext;
    AppointmentType mAppointmentType;

    AppointmentActivityListAdapter(Context con, List<AppointmentModel> list, ListView listView, AppointmentType appointmentType) {
        mContext = con;
        mItems = list;
        mAppointmentType = appointmentType;

    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.appointment_listview_cell, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(mAppointmentType == AppointmentType.RECIEVED)
        viewHolder.appointmentTextView.setText(String.format("You have an appointment for %s on %s at %s from %s", mItems.get(position).getDescription(), mItems.get(position).getDate(), mItems.get(position).getTime(), mItems.get(position).getFromEmail()));
        else
            viewHolder.appointmentTextView.setText(String.format("You made an appointment for %s on %s at %s to %s", mItems.get(position).getDescription(), mItems.get(position).getDate(), mItems.get(position).getTime(), mItems.get(position).getToEmail()));


        return convertView;
    }

    public class ViewHolder {

        @BindView(R.id.appointment_textview)
        TextView appointmentTextView;

        ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
