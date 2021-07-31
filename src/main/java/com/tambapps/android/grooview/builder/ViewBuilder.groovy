package com.tambapps.android.grooview.builder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tambapps.android.grooview.factory.TextViewFactory
import com.tambapps.android.grooview.factory.ViewFactory

class ViewBuilder extends FactoryBuilderSupport {

  static Object build(Context context, Closure closure) {
    closure.setDelegate(new ViewBuilder(context))
    closure.setResolveStrategy(Closure.DELEGATE_FIRST)
    return closure();
  }

  private final ViewGroup parent;
  private final Context context;

  final List<View> views = new ArrayList<>();

  /*
  ViewBuilder(ViewGroup parent, boolean initialize = true) {
    this(parent, parent.getContext(), initialize)
  }

   */

  ViewBuilder(Context context, boolean init = true) {
    super(init)
    this.context = context;
    initialize();
  }

  private void initialize() {
    setVariable("vertical", LinearLayout.VERTICAL);
    setVariable("horizontal", LinearLayout.HORIZONTAL);
    setVariable("visible", View.VISIBLE);
    setVariable("invisible", View.INVISIBLE);
    setVariable("gone", View.GONE);
  }

  void registerViews() {
    registerFactory("view", new ViewFactory(context))
    registerFactory("textView", new TextViewFactory(context))
  }

  @Override
  protected void setNodeAttributes(Object node, Map attributes) {
    // We're doing nothing on purpose because node attributes should be set by
    // Factories
  }
  // TODO add Color functions like in groovyfx

}
