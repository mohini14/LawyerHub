// Generated code from Butter Knife. Do not modify!
package com.lawyerhub.activities.appointments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lawyerhub.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AppointmentActivityListAdapter$ViewHolder_ViewBinding implements Unbinder {
  private AppointmentActivityListAdapter.ViewHolder target;

  @UiThread
  public AppointmentActivityListAdapter$ViewHolder_ViewBinding(AppointmentActivityListAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.appointmentTextView = Utils.findRequiredViewAsType(source, R.id.appointment_textview, "field 'appointmentTextView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AppointmentActivityListAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.appointmentTextView = null;
  }
}
