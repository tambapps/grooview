package com.tambapps.android.grooview

import android.content.Context
import android.view.ViewGroup
import com.tambapps.android.grooview.view.ViewDecorator
import groovy.transform.CompileStatic

@CompileStatic
class Grooview {

  // useful customizable closure to set and use
  static Closure show

  static Object start(ViewDecorator root, Closure closure) {
    if (!(root.get_View() instanceof ViewGroup)) {
      throw new IllegalArgumentException("Root must be a view group")
    }
    return start((ViewGroup) root.get_View(), closure)
  }

  static Object start(ViewGroup root, Closure closure) {
    return start(root.context, root, closure)
  }

  static Object start(Context context, ViewDecorator root, Closure closure) {
    if (!(root.get_View() instanceof ViewGroup)) {
      throw new IllegalArgumentException("Root must be a view group")
    }
    return start(context, (ViewGroup) root.get_View(), closure)
  }

  static Object start(Context context, ViewGroup root, Closure closure) {
    return start(new ViewBuilder(context, root), closure)
  }

  static Object start(ViewBuilder viewBuilder, Closure closure) {
    PixelCategory.context = viewBuilder.androidContext
    closure.setDelegate(viewBuilder)
    closure.setResolveStrategy(Closure.DELEGATE_FIRST)
    return closure()
  }

}
