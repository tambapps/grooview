package com.tambapps.android.grooview.builder

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.tambapps.android.grooview.factory.LinearLayoutFactory
import com.tambapps.android.grooview.factory.TextViewFactory
import com.tambapps.android.grooview.factory.ViewFactory
import com.tambapps.android.grooview.util.IdMapper

class ViewBuilder extends FactoryBuilderSupport {

  static Object build(Context context, Object parent, Closure closure) {
    closure.setDelegate(new ViewBuilder(context, parent))
    closure.setResolveStrategy(Closure.DELEGATE_FIRST)
    return closure()
  }

  private final Context context
  private final Object root
  private final IdMapper idMapper = new IdMapper()

  ViewBuilder(Object root, boolean initialize = true) {
    this(root.context, root, initialize)
  }

  ViewBuilder(Context context, Object root, boolean init = true) {
    super(init)
    this.context = context
    this.root = root
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
    // TODO text alignement text direction constants
  }

  void registerViews() {
    registerFactory("view", new ViewFactory(context))
    registerFactory("textView", new TextViewFactory(context))
    registerFactory("linearLayout", new LinearLayoutFactory(context))
  }

  int generateId(String name, Object view) {
    return idMapper.generateId(name, view)
  }

  Integer toViewId(Object data) {
    if (data == null) return null
    switch (data) {
      case Integer:
        return data
      default:
        return idMapper[data.toString()]
    }
  }

  @Override
  protected void nodeCompleted(Object parent, Object node) {
    (parent ?: root).addView(node)
    super.nodeCompleted(parent, node)
  }

  @Override
  Object getProperty(String propertyName) {
    try {
      super.getProperty(propertyName)
    } catch (MissingPropertyException e) {
      def view = idMapper[root, propertyName]
      if (view == null) {
        throw e
      }
      return view
    }
  }
}
