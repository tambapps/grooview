package com.tambapps.android.grooview.factory

import android.content.Context
import android.view.View
import groovy.transform.InheritConstructors

@InheritConstructors
class ViewFactory extends AbstractViewFactory {

  @Override
  View newInstance(Context context, Object name, Map attributes) {
    return new View(context)
  }


}
