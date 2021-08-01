package com.tambapps.android.grooview.factory

import android.content.Context
import android.widget.LinearLayout
import groovy.transform.InheritConstructors

@InheritConstructors
class LinearLayoutFactory extends AbstractViewGroupFactory {

  @Override
  protected Object newInstance(Context context) {
    return new LinearLayout(context)
  }
}
