package com.tambapps.android.grooview.builder

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tambapps.android.grooview.factory.LinearLayoutFactory
import com.tambapps.android.grooview.factory.TextViewFactory
import com.tambapps.android.grooview.factory.ViewFactory

class ViewBuilder extends FactoryBuilderSupport {

  static Object build(Context context, ViewGroup parent, Closure closure) {
    closure.setDelegate(new ViewBuilder(context, parent))
    closure.setResolveStrategy(Closure.DELEGATE_FIRST)
    return closure();
  }

  // TODO remove view parent(?)
  private final ViewGroup parent
  private final Context context

  final List<View> views = new ArrayList<>();

  ViewBuilder(ViewGroup parent, boolean initialize = true) {
    this(parent.context, parent, initialize)
  }

  ViewBuilder(Context context, ViewGroup parent, boolean init = true) {
    super(init)
    this.context = context
    this.parent = parent
    initialize()
  }

  private void initialize() {
    setVariable("vertical", LinearLayout.VERTICAL)
    setVariable("horizontal", LinearLayout.HORIZONTAL)
    setVariable("visible", View.VISIBLE)
    setVariable("invisible", View.INVISIBLE)
    setVariable("gone", View.GONE)
    setVariable("left", Gravity.LEFT)
    setVariable("start", Gravity.START)
    setVariable("top", Gravity.TOP)
    setVariable("right", Gravity.RIGHT)
    setVariable("end", Gravity.END)
    setVariable("bottom", Gravity.BOTTOM)
    setVariable("center", Gravity.CENTER)
    setVariable("center_horizontal", Gravity.CENTER_HORIZONTAL)
    setVariable("center_vertical", Gravity.CENTER_VERTICAL)
  }

  void registerViews() {
    registerFactory("view", new ViewFactory(context))
    registerFactory("textView", new TextViewFactory(context))
    registerFactory("linearLayout", new LinearLayoutFactory(context))
  }

}
