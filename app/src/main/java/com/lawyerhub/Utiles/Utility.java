package com.lawyerhub.Utiles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.lawyerhub.R;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utility {

    /**
     * Method shows tost on an activity with proper message
     *
     * @param text : text to be displayed
     */
    public static void showLongToast(String text) {
        if (text != null && text.trim().length() > 0 && MyApplication.getContext() != null)
            Toast.makeText(MyApplication.getContext(), text, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast( String text) {
        if (text != null && text.trim().length() > 0 && MyApplication.getContext() != null)
            Toast.makeText(MyApplication.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method are used to log data
     */
    public static void logData(Object obj) {
        Log.d(AppStringConstants.MESSAGE_EXCEPTION_TAG, AppStringConstants.MESSAGE_EXCEPTION_TAG + obj.toString());
    }

    /**
     * Method to log exceptions
     */
    public static void logExceptionData(Exception e) {
        Log.e(AppStringConstants.EXCEPTION_LOG_TAG, AppStringConstants.EXCEPTION_LOG_TAG + Log.getStackTraceString(e));
    }


    /**
     * Encode user email to use it as a Firebase key (Firebase does not allow "." in the key name)
     * Encoded email is also used as "userEmail", list and item "owner" value
     */
    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    /**
     * Method to show loader on screen
     *
     * @param activity : present activity
     */
    public static void showLoader(Activity activity) {
        long start = new Date().getTime();
        if (activity != null) {
            ViewGroup view = (ViewGroup) activity.getWindow().getDecorView().getRootView();
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View loader = activity.findViewById(R.id.loading_indicator);
            if (loader == null) {
                assert inflater != null;
                inflater.inflate(R.layout.custom_loader_progress, view, true);
                loader = activity.findViewById(R.id.loading_indicator);
            }
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            loader.setVisibility(View.VISIBLE);
        }
        long end = new Date().getTime();
        Utility.logData(MessageFormat.format("Show loader took {0}", end - start));

    }

    /**
     * Method to hide loader once functionality is done
     *
     * @param activity : current activity
     */
    public static void hideLoader(Activity activity) {
        try {
            if (activity != null) {
                View loader = activity.findViewById(R.id.loading_indicator);
                if (loader != null) {
                    loader.setVisibility(View.GONE);
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            } else
                Utility.logData("==cannot hide progress bar===");
        }catch (Exception e){
            Utility.logExceptionData(e);
        }
    }


    /**
     * Method shows Alert box with multiple options
     */
    public static AlertDialog.Builder showAlertBoxWithMultipleOptions(final Context context, final AlertBoxOptionSectionManager listener, final String[] optionList, String title, final String key) {
        AlertDialog.Builder builder = null;
        if (context != null) {
            builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);

            builder.setTitle(title);
            builder.setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }

            });
            builder.setItems(optionList, new DialogInterface.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (optionList.length > item && item >= 0) {
                        listener.optionSelected(optionList[item], key);
                        dialog.dismiss();
                    }
                }
            });
            builder.create();
        }
        return builder;
    }

    /**
     * Interface is used to handle selection on alert box amongst all options
     */
    public interface AlertBoxOptionSectionManager {
        void optionSelected(String optionSelected, String key);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }


    /**
     * Method is used to hide keybord from mentioned activity
     *
     * @param con : activity context
     */
    public static void hideViewOnActivity(Context con) {
        if (con != null) {
            Activity activity = (Activity) con;
            View view = activity.getCurrentFocus();
            if (view != null) {
                ((InputMethodManager) Objects.requireNonNull(activity.getSystemService(INPUT_METHOD_SERVICE))).
                        hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void hideViewOnActivity(final Activity activity, int id) {
        activity.findViewById(id).setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utility.hideViewOnActivity(activity);
                return false;
            }
        });
    }

    /**
     * Method shows alert box on an activity with proper message and also manages the action after after OK button is pressed
     *
     * @param message : message as heading of alert box
     */
    public static void showAlertBox(Context context, final AlertBoxOkInterface listener, String message, String title) {
        if (context != null) {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);

            // make background as irresponsive
            alertDialog.setCanceledOnTouchOutside(false);

            // Alert box OK button
            alertDialog.setButton((DialogInterface.BUTTON_POSITIVE), context.getString(R.string.ok_string),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // when some action needs to be done on OK button click
                            if (listener != null)
                                listener.okButtonOnAlertBoxPressed();
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    /**
     * The Method handles call back once OK button is pressed on AlertBox
     * Any activity or fragment implementing this fragment can override its method
     **/
    public interface AlertBoxOkInterface {
        void okButtonOnAlertBoxPressed();
    }


    /**
     * method to check internet availability
     *
     * @return bool value true if connected to internet otherwise false
     */
    private static boolean isNetworkAvailable() {
        Context con = MyApplication.getContext();
        NetworkInfo info = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
            info = connectivityManager.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    /**
     * method to toast getMessage if internet is not connected
     *
     * @return true if internet is connected
     */
    public static boolean isInternetConnected() {
        if (isNetworkAvailable()) {
            return true;
        } else {
            Utility.showLongToast(MyApplication.getContext().getString(R.string.check_internet));
            return false;
        }
    }


    /**
     * Method to show alert box with yes or no options
     *
     * @param context  : class context
     * @param listener : listener
     * @param message  : message on alert box
     */
    public static void showAlertBoxWithYesNoOption(Context context, final AlertBoxOkInterface listener, String message) {
        if (context != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            if (listener != null)
                                listener.okButtonOnAlertBoxPressed();
                            dialog.dismiss();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            };
            builder.setMessage(message).setPositiveButton(R.string.yes_option, dialogClickListener)
                    .setNegativeButton(R.string.no_option, dialogClickListener).show();
        }
    }


}
