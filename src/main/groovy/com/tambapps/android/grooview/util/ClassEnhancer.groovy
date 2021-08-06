package com.tambapps.android.grooview.util

import android.view.ViewGroup
import com.tambapps.android.grooview.view.ViewDecorator

class ClassEnhancer {

  static void enhanceClasses() {
    ViewGroup.metaClass.getChildren = { ->
      def views = []
      for (i in 0..<delegate.childCount) views << new ViewDecorator(delegate.getChildAt(i))
      return views
    }

  }
}
