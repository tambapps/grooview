package com.tambapps.android.grooview.factory

import com.tambapps.android.grooview.util.ContextUtils
import com.tambapps.android.grooview.util.ViewDecorator
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import org.codehaus.groovy.runtime.InvokerHelper

abstract class AbstractViewFactory extends AbstractFactory {

  private final Object context

  AbstractViewFactory(Object context) {
    this.context = context
  }

  @Override
  Object newInstance(FactoryBuilderSupport builder, Object name, Object value,
      Map attributes) throws InstantiationException, IllegalAccessException {
    return new ViewDecorator(newInstance(context))
  }

  @Override
  boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
    new ObjectPropertySetter(builder, node, attributes).with {
      def defaultProperties = getDefaultProperties(builder)
      if (defaultProperties) {
        for (entry in defaultProperties) {
          InvokerHelper.setProperty(node, entry.key.toString(), entry.value)
        }
      }
      handleProperty("autofillHints", String[])
      handleProperty("id") { builder.generateId(it, view) }
      handleProperty("labelFor", builder.&toViewId)

      // me-made properties TODO document them
      if (attributes.remove("rippleEffect")) {
        view.setForeground(ContextUtils.rippleDrawable(context))
      }

      // handling padding
      def paddings = (attributes['padding'] as List)
      if (paddings != null) {
        if (paddings.size() != 4) {
          throw new IllegalArgumentException("Padding should have 4 values: left, top, right bottom")
        }
        view.setPadding(*(paddings.collect {it as int}))
      }
      def paddingStart = (attributes['paddingStart'] ?: attributes['paddingLeft']) as Integer
      if (paddingStart) view.setPadding(paddingStart, view.paddingTop, view.paddingEnd, view.paddingBottom)
      def paddingTop = attributes['paddingTop'] as Integer
      if (paddingTop) view.setPadding(view.paddingStart, paddingTop, view.paddingEnd, view.paddingBottom)
      def paddingEnd = (attributes['paddingEnd'] ?: attributes['paddingRight']) as Integer
      if (paddingEnd) view.setPadding(view.paddingStart, view.paddingTop, paddingEnd, view.paddingBottom)
      def paddingBottom = attributes['paddingBottom'] as Integer
      if (paddingBottom) view.setPadding(view.paddingStart, view.paddingTop, view.paddingEnd, paddingBottom)
      it
    }
    return true
  }

  // should return a View but we're using Object type for testing purpose
  protected abstract Object newInstance(Object context)

  @Override
  boolean isLeaf() {
    return true
  }

  protected static class ObjectPropertySetter {
    private final Object delegate
    private final Object view
    private final Map attributes

    ObjectPropertySetter(Object delegate, Object view, Map attributes) {
      this.delegate = delegate
      this.view = view
      this.attributes = attributes
    }

    void handleProperty(String propertyName, Class<?> clazz) {
      handleProperty(propertyName) { DefaultGroovyMethods.asType(it, clazz) }
    }

    void handleProperty(String propertyName, Closure converter) {
      handleProperty(propertyName, propertyName, converter)
    }

    void handleProperty(String mapPropertyName, String objectPropertyName, Closure converter = CLOSURE_CONFIGURER) {
      def value = attributes.remove(mapPropertyName)
      if (value != null) {
        InvokerHelper.setProperty(view, objectPropertyName, converter(value))
      }
    }
  }

  @Override
  void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
    super.setParent(builder, parent, child)
  }

  protected Map getDefaultProperties(FactoryBuilderSupport builder) {
    return [:]
  }
}
