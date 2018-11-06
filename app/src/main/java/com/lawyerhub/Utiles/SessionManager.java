package com.lawyerhub.Utiles;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.lawyerhub.enums.UserRole;
import com.lawyerhub.model.UserModel;

public class SessionManager {


    private static final String USER_NAME_KEY = "user_name";
    private static final String USER_EMAIL_KEY = "email";
    private static final String IMAGE_URL_KEY = "image_url";
    private static final String AUTH_TOKEN_KEY = "auth_token";
    private static final String IS_LOGIN_KEY = "login_key";
    private static final String ROLE_KEY = "role_key";
    private static final String SPECILIZATION_KEY = "spec_key";
    private static final String CITY_KEY = "city_key";
    private static final String FEE_KEY = "fee_key";


    private static SessionManager sInstance = null;

    // Shared Preferences object
    private SharedPreferences mSharedPreferance;

    // Editor for Shared preferences
    private SharedPreferences.Editor mSharedPrefarenceEditor;

    private static final String PREF_NAME = "AppPrefarence";


    /**
     * Session manager is kept as singleton
     *
     * @return : self instance
     */
    @SuppressLint("CommitPrefEdits")
    public static SessionManager getInstance() {
        synchronized (SessionManager.class) {
            if (sInstance == null) {
                sInstance = new SessionManager();
                sInstance.mSharedPreferance = MyApplication.getContext().getSharedPreferences(PREF_NAME, 0);
                sInstance.mSharedPrefarenceEditor = sInstance.mSharedPreferance.edit();
                sInstance.mSharedPrefarenceEditor.apply();
            }
            return sInstance;
        }
    }

    public void createUser(UserModel user) {

        mSharedPrefarenceEditor.putString(USER_EMAIL_KEY, user.getEmail());
        mSharedPrefarenceEditor.putString(USER_NAME_KEY, user.getName());
        mSharedPrefarenceEditor.putString(IMAGE_URL_KEY, user.getPhoto());
        mSharedPrefarenceEditor.putString(AUTH_TOKEN_KEY, user.getIdToken());
        mSharedPrefarenceEditor.putString(ROLE_KEY, user.getRole().toString());
        mSharedPrefarenceEditor.putString(SPECILIZATION_KEY, user.getSpecialisation());
        mSharedPrefarenceEditor.putString(CITY_KEY, user.getCity());
        mSharedPrefarenceEditor.putFloat(FEE_KEY, user.getConsultationFee());
        mSharedPrefarenceEditor.putBoolean(IS_LOGIN_KEY, true);

        // commit changes
        mSharedPrefarenceEditor.commit();

    }

    public UserModel getCurrentUser() {
        UserModel model = new UserModel();
        model.setRole(UserRole.toMyEnum(mSharedPreferance.getString(ROLE_KEY, UserRole.USER.toString())));
        model.setPhoto(mSharedPreferance.getString(IMAGE_URL_KEY, AppStringConstants.BLANK_STRING_CONST));
        model.setName(mSharedPreferance.getString(USER_NAME_KEY, AppStringConstants.BLANK_STRING_CONST));
        model.setIdToken(mSharedPreferance.getString(AUTH_TOKEN_KEY, AppStringConstants.BLANK_STRING_CONST));
        model.setEmail(mSharedPreferance.getString(USER_EMAIL_KEY, AppStringConstants.BLANK_STRING_CONST));
        model.setCity(mSharedPreferance.getString(CITY_KEY, AppStringConstants.BLANK_STRING_CONST));
        model.setSpecialisation(mSharedPreferance.getString(SPECILIZATION_KEY, AppStringConstants.BLANK_STRING_CONST));
        model.setConsultationFee(mSharedPreferance.getFloat(FEE_KEY, 0.0f));

        return model;
    }

    /**
     * Method is used to clear all data from shared preferance
     */
    public void clearUserData() {
        mSharedPrefarenceEditor.remove(AUTH_TOKEN_KEY);
        mSharedPrefarenceEditor.remove(USER_NAME_KEY);
        mSharedPrefarenceEditor.remove(IMAGE_URL_KEY);
        mSharedPrefarenceEditor.remove(SPECILIZATION_KEY);
        mSharedPrefarenceEditor.remove(ROLE_KEY);
        mSharedPrefarenceEditor.remove(FEE_KEY);
        mSharedPrefarenceEditor.remove(CITY_KEY);
        mSharedPrefarenceEditor.putBoolean(IS_LOGIN_KEY, false);


        mSharedPrefarenceEditor.remove(PREF_NAME);
        mSharedPrefarenceEditor.commit();
    }

    public boolean isUserLogin(){
        return mSharedPreferance.getBoolean(IS_LOGIN_KEY, false);
    }

    public UserRole getCurrentUserRole() {
        return UserRole.toMyEnum(mSharedPreferance.getString(ROLE_KEY, AppStringConstants.BLANK_STRING_CONST));
    }

    public void setCurrentuserRole(UserRole role) {
        mSharedPrefarenceEditor.putString(ROLE_KEY, role.toString());

    }

}
