package com.tambapps.android.grooview

class Grooview {

  // useful customizable closure to set and use
  static Closure show

  static Object start(Object root, Closure closure) {
    return start(root.context, root, closure)
  }

  static Object start(Object context, Object root, Closure closure) {
    return start(new ViewBuilder(context, root), closure)
  }

  static Object start(ViewBuilder viewBuilder, Closure closure) {
    PixelsCategory.context = viewBuilder.androidContext
    closure.setDelegate(viewBuilder)
    closure.setResolveStrategy(Closure.DELEGATE_FIRST)
    return closure()
  }

}
