// Generated code from Butter Knife. Do not modify!
package com.lawyerhub.activities.appointments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lawyerhub.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AppointmentActivity_ViewBinding implements Unbinder {
  private AppointmentActivity target;

  @UiThread
  public AppointmentActivity_ViewBinding(AppointmentActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AppointmentActivity_ViewBinding(AppointmentActivity target, View source) {
    this.target = target;

    target.mToolBarHeading = Utils.findRequiredViewAsType(source, R.id.toolbar_heading, "field 'mToolBarHeading'", TextView.class);
    target.mToolBarSecondButton = Utils.findRequiredViewAsType(source, R.id.toolbar_second_buttton, "field 'mToolBarSecondButton'", ImageView.class);
    target.mToolBarFirstButon = Utils.findRequiredViewAsType(source, R.id.toolbar_first_buttton, "field 'mToolBarFirstButon'", ImageView.class);
    target.mListView = Utils.findRequiredViewAsType(source, R.id.appointment_list_view, "field 'mListView'", ListView.class);
    target.mAppointmentHeading = Utils.findRequiredViewAsType(source, R.id.appointment_title, "field 'mAppointmentHeading'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AppointmentActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolBarHeading = null;
    target.mToolBarSecondButton = null;
    target.mToolBarFirstButon = null;
    target.mListView = null;
    target.mAppointmentHeading = null;
  }
}
