// Generated code from Butter Knife. Do not modify!
package com.lawyerhub.activities.mainActivity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lawyerhub.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.mListView = Utils.findRequiredViewAsType(source, R.id.main_activity_listView, "field 'mListView'", ListView.class);
    target.mToolBarHeading = Utils.findRequiredViewAsType(source, R.id.toolbar_heading, "field 'mToolBarHeading'", TextView.class);
    target.mToolBarSecondButton = Utils.findRequiredViewAsType(source, R.id.toolbar_second_buttton, "field 'mToolBarSecondButton'", ImageView.class);
    target.mToolBarFirstButton = Utils.findRequiredViewAsType(source, R.id.toolbar_first_buttton, "field 'mToolBarFirstButton'", ImageView.class);
    target.mSearchEditText = Utils.findRequiredViewAsType(source, R.id.search_edit_Text, "field 'mSearchEditText'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mListView = null;
    target.mToolBarHeading = null;
    target.mToolBarSecondButton = null;
    target.mToolBarFirstButton = null;
    target.mSearchEditText = null;
  }
}
