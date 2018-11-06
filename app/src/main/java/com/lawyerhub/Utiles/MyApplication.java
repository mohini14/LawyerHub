package com.lawyerhub.Utiles;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Class is used to maintain Application's context through out the application
 */

public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;

        //Previous versions of Firebase
        Firebase.setAndroidContext(this);


        //Newer version of Firebase
        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }

    public static Context getContext(){
        return sContext;
    }
}
