package com.tambapps.android.grooview.factory

import android.content.Context
import android.view.View
import android.widget.TextView
import groovy.transform.InheritConstructors

@InheritConstructors
class TextViewFactory extends AbstractViewFactory {

  @Override
  View newInstance(Context context) {
    return new TextView(context)
  }

}
