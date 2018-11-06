package com.lawyerhub.activities.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lawyerhub.R;
import com.lawyerhub.Utiles.AppStringConstants;
import com.lawyerhub.Utiles.ApplicationSearchManager;
import com.lawyerhub.Utiles.SessionManager;
import com.lawyerhub.Utiles.Utility;
import com.lawyerhub.activities.EditProfileActivity;
import com.lawyerhub.activities.LawyerDetailsActivity;
import com.lawyerhub.activities.LoginActivity;
import com.lawyerhub.activities.appointments.AppointmentActivity;
import com.lawyerhub.enums.AppointmentType;
import com.lawyerhub.enums.UserRole;
import com.lawyerhub.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainActivityListAdapter.ItemClickInterface, ApplicationSearchManager.ApplicationSearchManagerInterface, Utility.AlertBoxOkInterface {

    @BindView(R.id.main_activity_listView)
    ListView mListView;

    @BindView(R.id.toolbar_heading)
    TextView mToolBarHeading;

    @BindView(R.id.toolbar_second_buttton)
    ImageView mToolBarSecondButton;

    @BindView(R.id.toolbar_first_buttton)
    ImageView mToolBarFirstButton;

    List<UserModel> mNonFilteredLawyerList;
    List<UserModel> mFilteredLawyerList;

    Unbinder mUnbinder;

    @BindView(R.id.search_edit_Text)
    EditText mSearchEditText;

    int mClickedItemTag;


    private boolean mDoubleBackToExitPressedOnce = false;
    private static final int BACK_PRESS_TIME = 2000;

    MainActivityListAdapter mListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpLayout();
        setUpToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpLayout();
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    private void setUpLayout() {
        mUnbinder = ButterKnife.bind(this);
        mNonFilteredLawyerList = new ArrayList<>();
        mFilteredLawyerList = new ArrayList<>();
        mListViewAdapter = new MainActivityListAdapter(mFilteredLawyerList, this, mListView, this);
        mListView.setAdapter(mListViewAdapter);

        new ApplicationSearchManager("Search lawyers by name, city, specialization", mSearchEditText, this);
        fetchLawyerList();
    }

    @Override
    public void onBackPressed() {
        handleDoubleBackExit();
    }

    /**
     * Method to avoid accidental back press on HOME SCREEN
     */
    private void handleDoubleBackExit() {
        if (mDoubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        mDoubleBackToExitPressedOnce = true;
        Utility.showShortToast(getString(R.string.press_double_back_message));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDoubleBackToExitPressedOnce = false;
            }
        }, BACK_PRESS_TIME);
    }

    private void setUpToolBar() {
        mToolBarHeading.setText(R.string.main_activity_toolbar_heading);
        mToolBarSecondButton.setOnClickListener(this);
        mToolBarSecondButton.setTag(R.drawable.hamburger_icon);
        mToolBarSecondButton.setBackgroundResource(R.drawable.hamburger_icon);
        mToolBarSecondButton.setVisibility(View.VISIBLE);
        mToolBarFirstButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_second_buttton) {
            if (mToolBarSecondButton.getTag() == Integer.valueOf(R.drawable.cross_gray)) {
                mToolBarSecondButton.setTag(R.drawable.hamburger_icon);
                mToolBarSecondButton.setBackgroundResource(R.drawable.hamburger_icon);
            } else if (mToolBarSecondButton.getTag() == Integer.valueOf(R.drawable.hamburger_icon)) {
                mToolBarSecondButton.setTag(R.drawable.cross_gray);
                mToolBarSecondButton.setBackgroundResource(R.drawable.cross_gray);
            }
            menuOnNavigationBar();
        }
    }


    private void menuOnNavigationBar() {

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(MainActivity.this, mToolBarSecondButton);
        //Inflating the Popup using xml file

        UserRole role = SessionManager.getInstance().getCurrentUserRole();
        if (role == UserRole.LAWYER) {
            popup.getMenuInflater()
                    .inflate(R.menu.lawyer_home_menu, popup.getMenu());
        } else {
            popup.getMenuInflater()
                    .inflate(R.menu.user_home_menu, popup.getMenu());
        }

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_profile) {
                    Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.appointments) {
                    Intent intent = new Intent(MainActivity.this, AppointmentActivity.class);
                    intent.putExtra(AppStringConstants.APPOINTMENT_TYPE_KEY, AppointmentType.RECIEVED);
                    startActivity(intent);

                } else if (item.getItemId() == R.id.appointment_history) {
                    Intent intent = new Intent(MainActivity.this, AppointmentActivity.class);
                    intent.putExtra(AppStringConstants.APPOINTMENT_TYPE_KEY, AppointmentType.MADE);
                    startActivity(intent);

                } else if (item.getItemId() == R.id.log_out) {
                    mClickedItemTag = R.id.log_out;
                    logout();
                } else if (item.getItemId() == R.id.delete_account) {
                    mClickedItemTag = R.id.delete_account;
                    deleteAccount();

                }

                return true;
            }
        });

        popup.show(); //showing popup menu

    }

    private void deleteAccount() {
        Utility.showAlertBoxWithYesNoOption(this, this, "Do you want to delete account?");

    }

    private void logout() {
        Utility.showAlertBoxWithYesNoOption(this, this, "Do you want to logout?");

    }

    private void fetchLawyerList() {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(AppStringConstants.USER_ENTITY).
                addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mNonFilteredLawyerList = new ArrayList<>();

                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                            UserModel model = childDataSnapshot.child((AppStringConstants.PROFILE_ENTITY)).getValue(UserModel.class);

                            if (model != null && Objects.requireNonNull(model).getRole() == UserRole.LAWYER && !model.getEmail().equalsIgnoreCase(SessionManager.getInstance().getCurrentUser().getEmail()))
                                mNonFilteredLawyerList.add(model);
                        }

                        mFilteredLawyerList.clear();
                        mFilteredLawyerList.addAll(mNonFilteredLawyerList);
                        showLawyerInListView();

                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void showLawyerInListView() {
        if (mSearchEditText != null) {
            if (mListViewAdapter != null && mNonFilteredLawyerList != null && mNonFilteredLawyerList.size() > 0) {
                mListViewAdapter.notifyDataSetChanged();
                mSearchEditText.setVisibility(View.VISIBLE);
            } else
                mSearchEditText.setVisibility(View.GONE);
        }
    }

    @Override
    public void itemClick(int position) {
        if (position < mNonFilteredLawyerList.size()) {
            Intent intent = new Intent(this, LawyerDetailsActivity.class);
            intent.putExtra(AppStringConstants.CHOOSEN_LAWYER_KEY, mNonFilteredLawyerList.get(position));
            startActivity(intent);
        }
    }

    @Override
    public void onTextChangeInSearchViewRecieved(String query) {
        try {
            if (query.equalsIgnoreCase(AppStringConstants.BLANK_STRING_CONST)) {

                mFilteredLawyerList.clear();
                mFilteredLawyerList.addAll(mNonFilteredLawyerList);
            } else {
                mFilteredLawyerList.clear();
                for (int i = 0; i < mNonFilteredLawyerList.size(); i++) {
                    if (mNonFilteredLawyerList.get(i).getName().toLowerCase().contains(query) || mNonFilteredLawyerList.get(i).getCity().toLowerCase().contains(query)
                            || mNonFilteredLawyerList.get(i).getSpecialisation().toLowerCase().contains(query)) {
                        mFilteredLawyerList.add(mNonFilteredLawyerList.get(i));
                    }
                }

            }
            mListViewAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Utility.logExceptionData(e);
        }
    }

    private void openLoginScreen() {
        Utility.hideLoader(this);
        SessionManager.getInstance().clearUserData();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void okButtonOnAlertBoxPressed() {
        if (mClickedItemTag == R.id.log_out) {
            openLoginScreen();
        } else if (mClickedItemTag == R.id.delete_account) {
            Utility.showLoader(this);

            try {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child(AppStringConstants.USER_ENTITY).child(Utility.encodeEmail(SessionManager.getInstance().getCurrentUser().getEmail())).equalTo(Utility.encodeEmail(SessionManager.getInstance().getCurrentUser().getEmail()));

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().removeValue();
                        openLoginScreen();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(AppStringConstants.MESSAGE_EXCEPTION_TAG, "onCancelled", databaseError.toException());
                    }
                });
            } catch (Exception e) {
                Utility.logExceptionData(e);
                Utility.hideLoader(this);
            }
        }
    }
}

