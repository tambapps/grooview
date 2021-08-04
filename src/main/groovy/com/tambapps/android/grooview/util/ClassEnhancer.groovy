package com.tambapps.android.grooview.util

import android.view.ViewGroup

class ClassEnhancer {

  static void enhanceClasses() {
    ViewGroup.metaClass.getChildren = { ->
      def views = []
      for (i in 0..<delegate.childCount) views << new ViewDecorator(delegate.getChildAt(i))
      return views
    }

  }
}
