package com.tambapps.android.grooview.factory

import com.tambapps.android.grooview.util.ContextUtils
import com.tambapps.android.grooview.view.ViewDecorator
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
    return newViewDecorator(newInstance(context))
  }

  protected def newViewDecorator(Object view) {
    return new ViewDecorator(view)
  }

  @Override
  boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
    def setter = new ObjectPropertySetter(builder, node, attributes)
    setter.with {
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
        view.setForeground(ContextUtils.rippleDrawable(builder.androidContext))
        view.setClickable(true)
      }

      // handling padding
      def paddings = (attributes.remove('padding') as List)
      if (paddings != null) {
        if (paddings.size() != 4) {
          throw new IllegalArgumentException("Padding should have 4 values: left, top, right and bottom")
        }
        view.setPadding(*(paddings.collect {it as int}))
      }
      def paddingStart = (attributes.remove('paddingStart') ?: attributes.remove('paddingLeft')) as Integer
      if (paddingStart != null) view.setPadding(paddingStart, view.paddingTop, view.paddingEnd, view.paddingBottom)
      def paddingTop = attributes.remove('paddingTop') as Integer
      if (paddingTop != null) view.setPadding(view.paddingStart, paddingTop, view.paddingEnd, view.paddingBottom)
      def paddingEnd = (attributes.remove('paddingEnd') ?: attributes.remove('paddingRight')) as Integer
      if (paddingEnd != null) view.setPadding(view.paddingStart, view.paddingTop, paddingEnd, view.paddingBottom)
      def paddingBottom = attributes.remove('paddingBottom') as Integer
      if (paddingBottom != null) view.setPadding(view.paddingStart, view.paddingTop, view.paddingEnd, paddingBottom)
      it
    }
    handleAdditionalNodeAttributes(builder, setter, attributes)
    return true
  }

  protected void handleAdditionalNodeAttributes(FactoryBuilderSupport builder,
                                                ObjectPropertySetter setter,
                                                Map attributes) {

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
      transformLayoutParamsProperties()
    }

    void handleProperty(String propertyName, Class<?> clazz) {
      handleProperty(propertyName) { DefaultGroovyMethods.asType(it, clazz) }
    }

    void handleProperty(String propertyName, Closure converter) {
      handleProperty(propertyName, propertyName, converter)
    }

    void handleProperty(String mapPropertyName, String objectPropertyName, Class<?> clazz) {
      handleProperty(mapPropertyName, objectPropertyName) { DefaultGroovyMethods.asType(it, clazz) }
    }

    void handleProperty(String mapPropertyName, String objectPropertyName, Closure converter) {
      def value = attributes.remove(mapPropertyName)
      if (value != null) {
        InvokerHelper.setProperty(view, objectPropertyName, converter(value))
      }
    }

    private void transformLayoutParamsProperties() {
      transformLayoutParamsProperty('width')
      transformLayoutParamsProperty('height')
      transformLayoutParamsProperty('margin')
      transformLayoutParamsProperty('marginStart')
      transformLayoutParamsProperty('marginLeft')
      transformLayoutParamsProperty('marginTop')
      transformLayoutParamsProperty('marginEnd')
      transformLayoutParamsProperty('marginRight')
      transformLayoutParamsProperty('marginBottom')
      transformLayoutParamsProperty('rules')
    }

    private void transformLayoutParamsProperty(String propertyName) {
      def value = attributes.remove(propertyName)
      if (value != null) {
        view.setLayoutParamsProperty(propertyName, value)
      }
    }
  }

  protected Map getDefaultProperties(FactoryBuilderSupport builder) {
    return [:]
  }
}
