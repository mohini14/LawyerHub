package com.lawyerhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawyerhub.R;
import com.lawyerhub.Utiles.AppStringConstants;
import com.lawyerhub.Utiles.SessionManager;
import com.lawyerhub.Utiles.Utility;
import com.lawyerhub.activities.mainActivity.MainActivity;
import com.lawyerhub.enums.UserRole;
import com.lawyerhub.model.UserModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, FirebaseAuth.AuthStateListener {
    @BindView(R.id.login_with_google)
    SignInButton mSignInButton;

    private static final int RC_SIGN_IN = 12;

    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    UserModel mUser;
    Unbinder mUnBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (SessionManager.getInstance().isUserLogin()) {
                startMainActivity();
                finish();
        }
        setUpLayout();
    }

    @Override
    protected void onDestroy() {
        mUnBinder.unbind();
        Utility.hideLoader(this);
        super.onDestroy();
    }

    private void createFirebaseAuth() {
        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Utility.hideLoader(this);
    }

    private void setUpLayout() {
        mUnBinder = ButterKnife.bind(this);
        mSignInButton.setOnClickListener(this);
        configureSignup();
        createFirebaseAuth();
    }

    /**
     * Method is used to connect to google api client
     */
    private void configureSignup() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.getResources().getString(R.string.web_client_id))
                .requestEmail().build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mGoogleApiClient.connect();
    }

    /**
     * Method is used to on sign in button click
     */
    private void signIn() {
        Utility.showLoader(this);
        mGoogleApiClient.clearDefaultAccountAndReconnect();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Method to recieve results for google account
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            System.out.println("result status code" + result.getStatus());
            if (result.isSuccess()) {
                onSucessGoogleLogin(result);

            } else {
                // Google Sign In failed, update UI appropriately
                Utility.logData("Login unsuccessful");
                Utility.showShortToast("Login unsuccessful");
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onSucessGoogleLogin(GoogleSignInResult result) {
        // Google Sign in was successful, save Token and state
        // then authenticate with firebase

        GoogleSignInAccount account = result.getSignInAccount();

        mUser = new UserModel();
        mUser.createUser(Objects.requireNonNull(account).getIdToken(), account.getDisplayName(), account.getEmail(), Objects.requireNonNull(account.getPhotoUrl()).toString(), account.getPhotoUrl());
//        SessionManager.getInstance().createUser(mUser);
        if (mUser.getIdToken() != null) {
            AuthCredential credential = GoogleAuthProvider.getCredential(mUser.getIdToken(), null);
            firebaseAuthWithGoogle(credential);
        }


    }

    //After a successful sign into Google, this method now authenticates the user with Firebase
    private void firebaseAuthWithGoogle(AuthCredential credential) {


        try {
//        showProgressDialog();
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Utility.logData("signInWithCredential:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Utility.logData("signInWithCredential" + task.getException().getMessage());
                                task.getException().printStackTrace();
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
//                            createUserInFirebaseHelper(mUser);

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


                                mDatabase.child(AppStringConstants.USER_ENTITY).
                                        child(Utility.encodeEmail(mUser.getEmail())).child(AppStringConstants.PROFILE_ENTITY).
                                        addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                try {
                                                    if (snapshot.getValue() != null) {
                                                        try {

                                                            mUser = snapshot.getValue(UserModel.class);
                                                            SessionManager.getInstance().createUser(mUser);
                                                            SessionManager.getInstance().setCurrentuserRole(mUser.getRole());


                                                            Utility.logData("logging user \n" + Objects.requireNonNull(mUser).toString());


                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        UserModel model = new UserModel();
                                                        model.setEmail(mUser.getEmail());
                                                        model.setName(mUser.getName());
                                                        model.setPhoto(mUser.getPhoto());
                                                        model.setIdToken(mUser.getIdToken());
                                                        model.setRole(UserRole.USER);
                                                        model.setCity(mUser.getCity());
                                                        model.setSpecialisation(mUser.getSpecialisation());
                                                        mDatabase.child(AppStringConstants.USER_ENTITY).
                                                                child(Utility.encodeEmail(model.getEmail())).child(AppStringConstants.PROFILE_ENTITY).
                                                                setValue(model);
                                                        SessionManager.getInstance().createUser(model);
                                                        SessionManager.getInstance().setCurrentuserRole(model.getRole());


                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }

                                        });


                                startMainActivity();


                                Utility.showLongToast("Login successful");


                            }
//                        hideProgressDialog();
                        }
                    });
        }catch (Exception e){
            Utility.logExceptionData(e);
            Utility.hideLoader(this);
        }
    }

    private void startMainActivity() {

        Utility.hideLoader(this);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_with_google)
            signIn();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (mAuthListener != null) {
        mAuth.removeAuthStateListener(this);

    }



    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //if user is signed in, we call a helper method to save the user details to Firebase
        if (user != null && mUser != null) {
            mUser.createUser(mUser.getIdToken(), user.getDisplayName(), user.getEmail(), Objects.requireNonNull(user.getPhotoUrl()).toString(), user.getPhotoUrl());

            Utility.logData("signed in user" + user.getUid());
        } else {
            // User is signed out
            Utility.logData("signed out user");
        }
    }
}
