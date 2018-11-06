package com.lawyerhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lawyerhub.R;
import com.lawyerhub.Utiles.AppStringConstants;
import com.lawyerhub.Utiles.DownloadImageWithPicassa;
import com.lawyerhub.model.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LawyerDetailsActivity extends AppCompatActivity implements View.OnClickListener, DownloadImageWithPicassa.PicassoSuccessfulCallInterface {

    private Unbinder mUnbinder;

    private UserModel mSelectedUser;

    @BindView(R.id.toolbar_heading)
    TextView mToolBarHeading;


    @BindView(R.id.toolbar_second_buttton)
    ImageView mToolBarSecondButton;

    @BindView(R.id.toolbar_first_buttton)
    ImageView mToolBarFirstButon;

    @BindView(R.id.static_lawyer_email_edittext)
    TextView mEmailTitle;

    @BindView(R.id.lawyer_email_edittext)
    TextView mEmailTextView;


    @BindView(R.id.lawyerphoto_imageview)
    ImageView mLawyerPhotoImageView;

    @BindView(R.id.static_lawyer_name_edittext)
    TextView mNameTitle;

    @BindView(R.id.lawyer_name_edittext)
    TextView mNameTextView;

    @BindView(R.id.static_lawyer_spec_edittext)
    TextView mSpecTitle;

    @BindView(R.id.lawyer_spec_edittext)
    TextView mSpecTextView;

    @BindView(R.id.static_lawyer_city_edittext)
    TextView mCityTitle;

    @BindView(R.id.lawyer_city_edittext)
    TextView mCityTextView;

    @BindView(R.id.lawyer_fee_edittext)
    TextView mFeeeTextView;

    @BindView(R.id.book_appointment_button)
    Button mBookAppointmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_details);

        if (getIntent() != null)
            mSelectedUser = (UserModel) getIntent().getSerializableExtra(AppStringConstants.CHOOSEN_LAWYER_KEY);

        setUpLayout();
        setUpToolBar();
    }

    private void setUpToolBar() {
        mToolBarHeading.setText(R.string.lawyer_details_heading);
        mToolBarHeading.setVisibility(View.VISIBLE);
        mToolBarFirstButon.setVisibility(View.VISIBLE);
        mToolBarFirstButon.setBackgroundResource(R.drawable.back_gray);
        mToolBarFirstButon.setOnClickListener(this);
        mToolBarSecondButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    private void setUpLayout() {
        mUnbinder = ButterKnife.bind(this);
        mBookAppointmentButton.setOnClickListener(this);
        setLawyerData();

    }

    private void setLawyerData() {
        if (mSelectedUser != null) {
            mEmailTextView.setText(mSelectedUser.getEmail());
            mNameTextView.setText(mSelectedUser.getName().toUpperCase());
            mSpecTextView.setText(mSelectedUser.getSpecialisation());
            mCityTextView.setText(mSelectedUser.getCity());
            mFeeeTextView.setText(String.format("$ %s", String.valueOf(mSelectedUser.getConsultationFee())));

            if (!mSelectedUser.getPhoto().equalsIgnoreCase(AppStringConstants.BLANK_STRING_CONST))
                new DownloadImageWithPicassa(mLawyerPhotoImageView, mSelectedUser.getPhoto(), this).downloadImagesWithPicasso();
            else
                mLawyerPhotoImageView.setImageDrawable(getDrawable(R.drawable.judge));


        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_first_buttton) {
            onBackPressed();
            finish();
        } else if (v.getId() == R.id.book_appointment_button) {
            Intent intent = new Intent(this, BookAppointmentActivity.class);
            intent.putExtra(AppStringConstants.CHOOSEN_LAWYER_KEY, mSelectedUser);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onPicassonCallDone() {

    }
}
