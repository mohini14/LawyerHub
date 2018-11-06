package com.lawyerhub.activities.appointments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawyerhub.R;
import com.lawyerhub.Utiles.AppStringConstants;
import com.lawyerhub.Utiles.SessionManager;
import com.lawyerhub.Utiles.Utility;
import com.lawyerhub.enums.AppointmentType;
import com.lawyerhub.model.AppointmentModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AppointmentActivity extends AppCompatActivity implements View.OnClickListener {

    private Unbinder mUnbinder;

    @BindView(R.id.toolbar_heading)
    TextView mToolBarHeading;

    @BindView(R.id.toolbar_second_buttton)
    ImageView mToolBarSecondButton;

    @BindView(R.id.toolbar_first_buttton)
    ImageView mToolBarFirstButon;

    @BindView(R.id.appointment_list_view)
    ListView mListView;

    @BindView(R.id.appointment_title)
    TextView mAppointmentHeading;

    List<AppointmentModel> mAppointmentList;
    AppointmentType mAppointmentType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        if (getIntent() != null)
            mAppointmentType = (AppointmentType) getIntent().getSerializableExtra(AppStringConstants.APPOINTMENT_TYPE_KEY);

        setUpLayout();
        setUpToolBar();
    }

    private void setUpToolBar() {
        mToolBarHeading.setText(R.string.appointments);
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
        if(mAppointmentType == AppointmentType.RECIEVED) {
            mAppointmentHeading.setText("You have recieved following appointments");
        }else
            mAppointmentHeading.setText("You have made following appointments to Lawyers");
        fetchAppointments();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_first_buttton) {
            onBackPressed();
            finish();
        }

    }

    private void fetchAppointments() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(AppStringConstants.USER_ENTITY).child(Utility.encodeEmail(SessionManager.getInstance().getCurrentUser().getEmail())).
                child(AppStringConstants.APPOINTMENT_ENTITY).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mAppointmentList = new ArrayList<>();
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                            AppointmentModel model = childDataSnapshot.getValue(AppointmentModel.class);
                            if (model != null && mAppointmentType.equals(AppointmentType.MADE)) {
                                if (model.getFromEmail().equalsIgnoreCase(SessionManager.getInstance().getCurrentUser().getEmail()))
                                    mAppointmentList.add(model);


                                // appointements made to lawyer
                            } else if (model != null && mAppointmentType.equals(AppointmentType.RECIEVED)) {
                                if (model.getToEmail().equalsIgnoreCase(SessionManager.getInstance().getCurrentUser().getEmail())) {
                                    mAppointmentList.add(model);
                                }
                            }
                        }

                        setAdapterForListView();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void setAdapterForListView() {
        if(mAppointmentList != null && mAppointmentList.size() > 0)
        mListView.setAdapter(new AppointmentActivityListAdapter(this, mAppointmentList, mListView, mAppointmentType));
        else
            mAppointmentHeading.setText("No Appointments found");

    }
}
