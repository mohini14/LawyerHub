package com.lawyerhub.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawyerhub.R;
import com.lawyerhub.Utiles.AppStringConstants;
import com.lawyerhub.Utiles.DatePickerFragment;
import com.lawyerhub.Utiles.MyApplication;
import com.lawyerhub.Utiles.TimePickerFragment;
import com.lawyerhub.Utiles.Utility;
import com.lawyerhub.model.AppointmentModel;
import com.lawyerhub.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookAppointmentActivity extends AppCompatActivity implements View.OnClickListener, Utility.AlertBoxOkInterface {

    private Unbinder mUnbinder;

    private UserModel mSelectedUser;

    @BindView(R.id.toolbar_heading)
    TextView mToolBarHeading;


    @BindView(R.id.toolbar_second_buttton)
    ImageView mToolBarSecondButton;

    @BindView(R.id.toolbar_first_buttton)
    ImageView mToolBarFirstButon;

    @BindView(R.id.time_edit_text)
    EditText mTimeEditText;

    @BindView(R.id.book_app_title)
    TextView mTitle;

    @BindView(R.id.description_edit_text)
    EditText mDescriptionEditText;


    @BindView(R.id.date_edit_text)
    EditText mDateEditText;
    private DatePickerFragment mCalendarDialogFragment;
    private TimePickerFragment mTimeDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointmen);

        if (getIntent() != null)
            mSelectedUser = (UserModel) getIntent().getSerializableExtra(AppStringConstants.CHOOSEN_LAWYER_KEY);

        setUpLayout();
        setUpToolBar();
    }

    private void setUpToolBar() {
        mToolBarHeading.setText(R.string.book_appointment_lowercase_string);
        mToolBarHeading.setVisibility(View.VISIBLE);
        mToolBarFirstButon.setVisibility(View.VISIBLE);
        mToolBarFirstButon.setBackgroundResource(R.drawable.back_gray);
        mToolBarFirstButon.setOnClickListener(this);
        mToolBarSecondButton.setVisibility(View.VISIBLE);
        mToolBarSecondButton.setBackgroundResource(R.drawable.check_gray);
        mToolBarSecondButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    private void setUpLayout() {
        mUnbinder = ButterKnife.bind(this);
        mTimeEditText.setOnClickListener(this);
        mDateEditText.setOnClickListener(this);
        setDateInputType();
        setTimeInputType();

        setLawyerData();

    }

    private void setDateInputType() {
        //creating bundle to pass data to dateFragment
        Bundle args = new Bundle();
        args.putInt(DatePickerFragment.EDIT_TEXT_FIELD, R.id.date_edit_text);
        mCalendarDialogFragment = new DatePickerFragment();
        mCalendarDialogFragment.setArguments(args);
        mDateEditText.setInputType(mCalendarDialogFragment.getId());
    }

    private void setTimeInputType() {
        //creating bundle to pass data to dateFragment
        Bundle args = new Bundle();
        args.putInt(TimePickerFragment.EDIT_TEXT_FIELD, R.id.time_edit_text);
        mTimeDialogFragment = new TimePickerFragment();
        mTimeDialogFragment.setArguments(args);
        mTimeEditText.setInputType(mTimeDialogFragment.getId());

    }

    private void timeEditTextClicked() {
        if (getSupportFragmentManager().findFragmentByTag(AppStringConstants.TIME_PICKER_FRAG) == null)
            mTimeDialogFragment.show(getSupportFragmentManager(), AppStringConstants.TIME_PICKER_KEY);
    }

    /**
     * Method lets user to select birth date
     */
    private void dateEditTextClicked() {
        if (getSupportFragmentManager().findFragmentByTag(AppStringConstants.DATE_PICKER_FRAGMENT_TAG) == null)
            mCalendarDialogFragment.show(getSupportFragmentManager(), AppStringConstants.DATE_PICKER_KEY);
    }

    private void setLawyerData() {
        if (mSelectedUser != null)
            mTitle.setText(String.format("%s\n%s", getString(R.string.book_appointment_title_string), mSelectedUser.getName()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_first_buttton:
                onBackPressed();
                finish();
                break;
            case R.id.toolbar_second_buttton:
                if (!mTimeEditText.getText().toString().equalsIgnoreCase(AppStringConstants.BLANK_STRING_CONST)
                        && !mDateEditText.getText().toString().equalsIgnoreCase(AppStringConstants.BLANK_STRING_CONST)
                        && !mDescriptionEditText.getText().toString().equalsIgnoreCase(AppStringConstants.BLANK_STRING_CONST))
                    Utility.showAlertBoxWithYesNoOption(this, this, String.format("%s %s ?", getString(R.string.appointment_confirmation_message), mSelectedUser.getName()));
                else
                    Utility.showLongToast(getString(R.string.all_fields_required_messages));
                break;

            case R.id.date_edit_text:
                dateEditTextClicked();
                break;
            case R.id.time_edit_text:
                timeEditTextClicked();
                break;
            default:
                break;
        }

    }

    @Override
    public void okButtonOnAlertBoxPressed() {
        Utility.showLoader(this);
        Utility.hideViewOnActivity(this);

        final boolean[] bookingSuccess = {false, false};

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();

        List<AppointmentModel> appointmentLists = new ArrayList<>();
        AppointmentModel newAppointment = new AppointmentModel();
        newAppointment.setTime(mTimeEditText.getText().toString());
        newAppointment.setDate(mDateEditText.getText().toString());
        newAppointment.setFromEmail(currentUser.getEmail());
        newAppointment.setToEmail(mSelectedUser.getEmail());
        newAppointment.setDescription(mDescriptionEditText.getText().toString());


        try {

            DatabaseReference cuurentUserRootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference currentUserAppointmentRef = cuurentUserRootRef.child(AppStringConstants.USER_ENTITY).
                    child(Utility.encodeEmail(currentUser.getEmail())).
                    child(AppStringConstants.APPOINTMENT_ENTITY);


            currentUserAppointmentRef
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        HashMap<String, AppointmentModel> map = new HashMap<>();

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            map.put(UUID.randomUUID().toString(), newAppointment);
                            if (dataSnapshot.getValue() != null) {
                                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                                    AppointmentModel model = childDataSnapshot.getValue(AppointmentModel.class);
                                    map.put(UUID.randomUUID().toString(), model);

                                }

                            }
//
                            currentUserAppointmentRef.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    bookingSuccess[0] = true;
                                    hideLoaders(bookingSuccess);

                                }
                            }).addOnCanceledListener(new OnCanceledListener() {
                                @Override
                                public void onCanceled() {
                                    hideLoaders(bookingSuccess);

                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            hideLoaders(bookingSuccess);

                        }
                    });


            DatabaseReference lawyerRootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference laweyAppointmentRef = lawyerRootRef.child(AppStringConstants.USER_ENTITY).
                    child(Utility.encodeEmail(mSelectedUser.getEmail())).
                    child(AppStringConstants.APPOINTMENT_ENTITY);


            laweyAppointmentRef
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            HashMap<String, AppointmentModel> map = new HashMap<>();

                            map.put(UUID.randomUUID().toString(), newAppointment);

                            if (dataSnapshot.getValue() != null) {
                                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                                    AppointmentModel model = childDataSnapshot.getValue(AppointmentModel.class);
                                    map.put(UUID.randomUUID().toString(), model);

                                }

                            }

                            laweyAppointmentRef.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    bookingSuccess[1] = true;
                                    hideLoaders(bookingSuccess);
                                }
                            }).addOnCanceledListener(new OnCanceledListener() {
                                @Override
                                public void onCanceled() {
                                    Utility.showLongToast(getString(R.string.something_went_wrong_message));
                                    hideLoaders(bookingSuccess);

                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                hideLoaders(bookingSuccess);
                        }


                    });
        } catch (Exception e) {
            hideLoaders(bookingSuccess);
            Utility.logExceptionData(e);
        } finally {
            hideLoaders(bookingSuccess);
        }
    }

    private void hideLoaders(boolean[] bookingSuccess) {
        Utility.hideLoader(this);
        if (bookingSuccess[0] && bookingSuccess[1])
            Utility.showAlertBox(this, null, "Appointment Booked Successfully", getString(R.string.app_name));
    }

}