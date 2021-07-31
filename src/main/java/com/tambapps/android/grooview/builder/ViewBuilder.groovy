package com.tambapps.android.grooview.builder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView
import com.tambapps.android.grooview.factory.AbstractViewFactory;
import com.tambapps.android.grooview.factory.TextViewFactory
import com.tambapps.android.grooview.factory.ViewFactory;
import org.codehaus.groovy.runtime.InvokerHelper;

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

  public ViewBuilder textView2(Map<String, ?> properties) {
    TextView textView = new TextView(context);
    handleViewParams(textView, properties);
    Optional<String> optText = getString(properties, "text");
    optText.ifPresent(textView.&setText);
    // TODO textSize
    views.add(textView);
    return this;
  }

  private void handleViewParams(View view, Map<String, ?> properties) {
    // TODO handle int (pixels) and String (dp, sp, ...) for dimensions
    Optional<Integer> visibility = get(properties, "visibility", Integer.class);
    visibility.ifPresent(view.&setVisibility);
    Optional<Closure> clickListener = get(properties, "clickListener", Closure.class);
    clickListener.ifPresent( {
      it.setDelegate(view);
      it.setResolveStrategy(DELEGATE_FIRST)
      view.setOnClickListener {
        InvokerHelper.invokeClosure(it, [] as Object[])
      }
    });

  }

  private Optional<String> getString(Map<String, ?> properties, String propertyName) {
    Object o = properties.get(propertyName);
    return Optional.ofNullable(o).map(Object.&toString);
  }

  private <T> Optional<T> get(Map<String, ?> properties, String propertyName, Class<T> clazz) {
    return get(properties.get(propertyName), clazz);
  }

  private <T> Optional<T> get(Object o, Class<T> clazz) {
    if (!clazz.isInstance(o)) {
      return Optional.empty();
    }
    return Optional.of((T) o);
  }
}
