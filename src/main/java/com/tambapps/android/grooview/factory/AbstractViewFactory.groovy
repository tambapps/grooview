package com.tambapps.android.grooview.factory

import android.content.Context

abstract class AbstractViewFactory extends AbstractFactory {

  private Context context

  AbstractViewFactory(Context context) {
    this.context = context
  }

  @Override
  Object newInstance(FactoryBuilderSupport builder, Object name, Object value,
      Map attributes) throws InstantiationException, IllegalAccessException {
    Object view = newInstance(context)
    setAttributes(view, attributes)
    return view
  }

  // should return a View but we're using Object type for testing purpose
  protected abstract Object newInstance(Context context)

  protected void setAttributes(Object view, Map attributes) {
    // TODO handle int (pixels) and String (dp, sp, ...) for dimensions
    def visibility = attributes.visibility as Integer
    if (visibility != null) view.visibility = visibility
    def clickListener = properties.clickListener as Closure
    if (clickListener) {
      clickListener.setDelegate(view)
      clickListener.setResolveStrategy(Closure.DELEGATE_FIRST)
      view.onClickListener = {
        clickListener(view)
      }
    }
  }

  @Override
  boolean isLeaf() {
    return true
  }
}
