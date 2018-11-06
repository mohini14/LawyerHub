package com.lawyerhub.activities.mainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lawyerhub.R;
import com.lawyerhub.Utiles.DownloadImageWithPicassa;
import com.lawyerhub.model.UserModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityListAdapter extends BaseAdapter implements DownloadImageWithPicassa.PicassoSuccessfulCallInterface, AdapterView.OnItemClickListener {

    List<UserModel> mItems;
    Context mContext;
    ItemClickInterface mListener;

    MainActivityListAdapter(List<UserModel> lawyerList, Context con, ListView listView, final ItemClickInterface lis) {
        this.mItems = lawyerList;
        mContext = con;
        mListener = lis;
        listView.setOnItemClickListener(this);
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
        MainActivityListViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.displayoption_listview_cell, parent, false);
            viewHolder = new MainActivityListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MainActivityListViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(mItems.get(position).getName().toUpperCase());
        viewHolder.plusImageView.setVisibility(View.VISIBLE);
        viewHolder.feeEditText.setText(String.format("$ %s", String.valueOf(mItems.get(position).getConsultationFee())));
        viewHolder.specialisationTextView.setText(mItems.get(position).getSpecialisation());

        new DownloadImageWithPicassa(viewHolder.plusImageView, mItems.get(position).getPhoto(), this).downloadImagesWithPicasso();

        // hide bottom view for very last cell
        if (position == mItems.size() - 1)
            viewHolder.bottomView.setVisibility(View.GONE);
        else viewHolder.bottomView.setVisibility(View.VISIBLE);

        return convertView;
    }

    @Override
    public void onPicassonCallDone() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.itemClick(position);
    }

    public class MainActivityListViewHolder {

        @BindView(R.id.displayoption_title_textView)
        TextView title;

        @BindView(R.id.displayoption_imageview)
        ImageView plusImageView;

        @BindView(R.id.displayoption_list_bottom_view)
        View bottomView;

        @BindView(R.id.displayoption_consultation_fee_textView)
        TextView feeEditText;

        @BindView(R.id.displayoption_specialisation_textView)
        TextView specialisationTextView;


        MainActivityListViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    public interface ItemClickInterface{
        void itemClick(int position);
    }
}
