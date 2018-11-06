package com.lawyerhub.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lawyerhub.R;
import com.lawyerhub.Utiles.AppStringConstants;
import com.lawyerhub.Utiles.DownloadImageWithPicassa;
import com.lawyerhub.Utiles.SessionManager;
import com.lawyerhub.Utiles.Utility;
import com.lawyerhub.enums.UserRole;
import com.lawyerhub.model.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener,
        DownloadImageWithPicassa.PicassoSuccessfulCallInterface, OnCompleteListener<Void>, OnCanceledListener,
        Utility.AlertBoxOptionSectionManager {


    @BindView(R.id.toolbar_heading)
    TextView mToolBarHeading;

    @BindView(R.id.toolbar_second_buttton)
    ImageView mToolBarSecondButton;

    @BindView(R.id.userphoto_imageview)
    ImageView mCameraImageView;

    @BindView(R.id.name_edit_text)
    EditText mNameEditText;

    @BindView(R.id.toolbar_first_buttton)
    ImageView mToolBarFirstButon;

    @BindView(R.id.email_edit_text)
    EditText mEmailEditText;

    @BindView(R.id.role_edit_text)
    EditText mRoleEditText;

    @BindView(R.id.city_edit_text)
    EditText mCityEditText;

    @BindView(R.id.specialisation_edit_text)
    EditText mSpecialisationEditText;

    @BindView(R.id.specialisationWrapper)
    android.support.design.widget.TextInputLayout mSpecialisationWrapper;

    @BindView(R.id.feeWrapper)
    android.support.design.widget.TextInputLayout mFeeWrapper;

    @BindView(R.id.fee_edit_text)
    EditText mFeeEditText;

    private AlertDialog.Builder mRoleBuilder;


    private Unbinder mUnbinder;
    private UserModel mUser;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setUpLayout();
        setUpToolBar();
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    private void setUpToolBar() {
        mToolBarHeading.setText(R.string.edit_profile_heading);
        mToolBarHeading.setVisibility(View.VISIBLE);
        mToolBarFirstButon.setVisibility(View.VISIBLE);
        mToolBarFirstButon.setBackgroundResource(R.drawable.back_gray);
        mToolBarFirstButon.setOnClickListener(this);
        mToolBarSecondButton.setOnClickListener(this);
        mToolBarSecondButton.setBackgroundResource(R.drawable.check_gray);
        mToolBarSecondButton.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpLayout() {
        mUnbinder = ButterKnife.bind(this);
        setRoleInputType();
        mRoleEditText.setOnClickListener(this);
        setUserDataToEditText();
    }

    private void setUserDataToEditText() {
        mUser = SessionManager.getInstance().getCurrentUser();
        mEmailEditText.setText(mUser.getEmail());
        mNameEditText.setText(mUser.getName());
        mRoleEditText.setText(mUser.getRole().toString());
        if (mRoleEditText.getText().toString().equalsIgnoreCase(UserRole.LAWYER.toString())) {
            mSpecialisationWrapper.setVisibility(View.VISIBLE);
            mFeeWrapper.setVisibility(View.VISIBLE);
            mFeeEditText.setText(String.valueOf(mUser.getConsultationFee()));
            mSpecialisationEditText.setText(mUser.getSpecialisation());
        }
        mCityEditText.setText(mUser.getCity());
        mEmailEditText.setFocusable(false);
        mEmailEditText.setClickable(false);

        Utility.logData("the image url is ==== " + mUser.getPhoto());
        Utility.logData("the email id  ==== " + mUser.getEmail());

        new DownloadImageWithPicassa(mCameraImageView, mUser.getPhoto(), this).downloadImagesWithPicasso();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_second_buttton:
                updateUserDetails();
                break;
            case R.id.role_edit_text:
                roleEditTextClicked();
                break;

            case R.id.toolbar_first_buttton:
                onBackPressed();
                finish();
                break;
        }

    }

    private void updateUserDetails() {

        Utility.hideViewOnActivity(this);
        mUser.setEmail(mEmailEditText.getText().toString());
        mUser.setName(mNameEditText.getText().toString());
        mUser.setRole(UserRole.toMyEnum(mRoleEditText.getText().toString()));
        mUser.setCity(mCityEditText.getText().toString());

        if (mRoleEditText.getText().toString().equalsIgnoreCase(UserRole.LAWYER.toString())) {
            mUser.setSpecialisation(mSpecialisationEditText.getText().toString());
            mUser.setConsultationFee(Float.valueOf(mFeeEditText.getText().toString()));

        } else {
            mUser.setSpecialisation(null);
            mUser.setConsultationFee(AppStringConstants.FLOAT_DEFAULT_VALUE);
        }

        if (validateFields() && Utility.isInternetConnected()) {
            Utility.showLoader(this);

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child(AppStringConstants.USER_ENTITY).child(Utility.encodeEmail(mUser.getEmail())).
                    child(AppStringConstants.PROFILE_ENTITY).
                    setValue(mUser).addOnCompleteListener(this).addOnCanceledListener(this);
        }
    }


    @Override
    public void onPicassonCallDone() {

    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        SessionManager.getInstance().createUser(mUser);
        SessionManager.getInstance().setCurrentuserRole(mUser.getRole());
        Utility.hideLoader(this);
        Utility.showAlertBox(this, null, getString(R.string.user_profile_update_message), getString(R.string.app_name));

    }

    private boolean validateFields() {

        if (mUser.getName() == null || mUser.getName().equalsIgnoreCase(AppStringConstants.BLANK_STRING_CONST)) {
            Utility.showLongToast("Please Enter Name");
            return false;
        }

        if (mUser.getRole() == null) {
            Utility.showLongToast("Please Enter Role");
            return false;
        }

        if (mUser.getRole().toString().equalsIgnoreCase(UserRole.LAWYER.toString()) && (mUser.getSpecialisation() == null || mUser.getSpecialisation().equalsIgnoreCase(AppStringConstants.BLANK_STRING_CONST))) {
            Utility.showLongToast("Please Enter Specialization");
            return false;
        }

        if (mUser.getCity() == null || mUser.getCity().equalsIgnoreCase(AppStringConstants.BLANK_STRING_CONST)) {
            Utility.showLongToast("Please Enter City");
            return false;
        }

        return true;

    }

    @Override
    public void onCanceled() {
        Utility.hideLoader(this);

    }

    /**
     * Method to handle actions on gender edit text
     */
    private void roleEditTextClicked() {
        if (mRoleBuilder != null)
            mRoleBuilder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setRoleInputType() {

        mRoleBuilder = Utility.showAlertBoxWithMultipleOptions(this,
                this, Utility.getNames(UserRole.class), getString(R.string.select_your_role), AppStringConstants.ROLE_KEY);
        mRoleEditText.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public void optionSelected(String optionSelected, String key) {
        if (key.equalsIgnoreCase(AppStringConstants.ROLE_KEY)) {
            mRoleEditText.setText(optionSelected);
            if (optionSelected.equalsIgnoreCase(UserRole.LAWYER.toString())) {
                mSpecialisationWrapper.setVisibility(View.VISIBLE);
                mSpecialisationEditText.setText(mUser.getSpecialisation());
                mFeeWrapper.setVisibility(View.VISIBLE);
                mFeeEditText.setText(String.valueOf(mUser.getConsultationFee()));
            } else {
                mSpecialisationWrapper.setVisibility(View.GONE);
                mSpecialisationEditText.setText(null);
                mFeeWrapper.setVisibility(View.GONE);
                mFeeEditText.setText(null);
            }
        }

    }
}

