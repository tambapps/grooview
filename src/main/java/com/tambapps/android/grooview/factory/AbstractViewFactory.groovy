package com.tambapps.android.grooview.factory

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import org.codehaus.groovy.runtime.InvokerHelper

import java.nio.file.Path

abstract class AbstractViewFactory extends AbstractFactory {

  private final Context context


  AbstractViewFactory(Context context) {
    this.context = context
  }

  @Override
  Object newInstance(FactoryBuilderSupport builder, Object name, Object value,
      Map attributes) throws InstantiationException, IllegalAccessException {
    return newInstance(context)
  }

  @Override
  boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
    new ObjectPropertySetter(builder, node, attributes).with {
      handleProperty("accessibilityDelegate", View.AccessibilityDelegate)
      handleProperty("accessibilityTraversalAfter", builder.&toViewId)
      handleProperty("accessibilityTraversalBefore", builder.&toViewId)
      handleProperty("animation", Animation)
      handleProperty("animationMatrix", Matrix)
      handleProperty("autofillHints", String[])
      handleProperty("background", this.&toDrawable)
      handleProperty("backgroundDrawable", this.&toDrawable)
      //handleProperty("backgroundTintBlendMode", android.graphics.BlendMode)
      handleProperty("backgroundTintList", ColorStateList)
      handleProperty("backgroundTintMode", PorterDuff.Mode)
      //handleProperty("contentCaptureSession", android.view.contentcapture.ContentCaptureSession)
      handleProperty("fadingEdgeLength", this.&toPixels)
      handleProperty("foreground", this.&toDrawable)
      //handleProperty("foregroundTintBlendMode", android.graphics.BlendMode)
      handleProperty("foregroundTintList", ColorStateList)
      handleProperty("foregroundTintMode", PorterDuff.Mode)
      handleProperty("horizontalScrollbarThumbDrawable", this.&toDrawable)
      handleProperty("horizontalScrollbarTrackDrawable", this.&toDrawable)
      handleProperty("id") { builder.generateId(it, view) }
      handleProperty("labelFor", builder.&toViewId)
      handleProperty("layerPaint", Paint)
      handleProperty("layoutParams", ViewGroup.LayoutParams)
      handleProperty("minimumHeight", this.&toPixels)
      handleProperty("minimumWidth", this.&toPixels)
      handleProperty("nextClusterForwardId", builder.&toViewId)
      handleProperty("nextFocusDownId", builder.&toViewId)
      handleProperty("nextFocusForwardId", builder.&toViewId)
      handleProperty("nextFocusLeftId", builder.&toViewId)
      handleProperty("nextFocusRightId", builder.&toViewId)
      handleProperty("nextFocusUpId", builder.&toViewId)
      handleProperty("scrollBarSize", this.&toPixels)
      handleProperty("scrollBarStyle", this.&toPixels)
      handleProperty("scrollX", this.&toPixels)
      handleProperty("scrollY", this.&toPixels)
      handleProperty("stateDescription", CharSequence)
      // list of android Rect
      handleProperty("systemGestureExclusionRects", List)
      handleProperty("verticalScrollbarPosition", this.&toPixels)
      handleProperty("verticalScrollbarThumbDrawable", this.&toDrawable)
      handleProperty("verticalScrollbarTrackDrawable", this.&toDrawable)
      handleProperty("x", this.&toPixels)
      handleProperty("y", this.&toPixels)
      handleProperty("z", this.&toPixels)

      // handling padding
      def paddings = attributes['padding'] as List
      if (paddings != null) {
        if (paddings.size() != 4) {
          throw new IllegalArgumentException("Padding should have 4 values: left, top, right bottom")
        }
        paddings = paddings.collect(this.&toPixels)
        view.setPadding(*paddings)
      }
      def paddingStart = toPixels(attributes['paddingStart'] ?: attributes['paddingLeft'])
      if (paddingStart) view.setPadding(paddingStart, view.paddingTop, view.paddingEnd, view.paddingBottom)
      def paddingTop = toPixels(attributes['paddingTop'])
      if (paddingTop) view.setPadding(view.paddingStart, paddingTop, view.paddingEnd, view.paddingBottom)
      def paddingEnd = toPixels(attributes['paddingEnd'] ?: attributes['paddingRight'])
      if (paddingEnd) view.setPadding(view.paddingStart, view.paddingTop, paddingEnd, view.paddingBottom)
      def paddingBottom = toPixels(attributes['paddingBottom'])
      if (paddingBottom) view.setPadding(view.paddingStart, view.paddingTop, view.paddingEnd, paddingBottom)
      /*
      TODO what to do about these properties with multiple parameters
      handleProperty("layerType", int, android.graphics.Paint)
      handleProperty("leftTopRightBottom", int, int, int, int)
      handleProperty("onReceiveContentListener", java.lang.String[], android.view.OnReceiveContentListener)
      handleProperty("padding", int, int, int, int)
      handleProperty("paddingRelative", int, int, int, int)
      handleProperty("scrollIndicators", int, int)

       */
    }
    return true
  }

  // should return a View but we're using Object type for testing purpose
  protected abstract Object newInstance(Context context)

  protected Integer toPixels(def data) {
    if (data == null) {
      return null
    }
    switch (data) {
      case Integer:
        return data
      case String:
        String number = data.takeWhile(Character.&isDigit)
        int unit = getComplexUnit(data.substring(number.size()))
        return (int) TypedValue.applyDimension(unit, number.toInteger(), context.resources.displayMetrics)
      default:
        throw new IllegalArgumentException("Type ${data.class.simpleName} is not handled for units")
    }
  }

  private Drawable toDrawable(def data) {
    if (data == null) {
      return null
    }
    switch (data) {
      case File:
      case Path:
        return Drawable.createFromPath(data.toString())
      case String:
        // let's assume it's an url
        def b = data.toURL().withInputStream {
          BitmapFactory.decodeStream(it)
        }
        return new BitmapDrawable(context.resources, b)
      default:
        throw new IllegalArgumentException("Cannot convert object of type ${data.class.simpleName} to Drawable")
    }
  }
  private static int getComplexUnit(String unit) {
    switch (unit) {
      case 'dp':
      case 'dip':
        return TypedValue.COMPLEX_UNIT_DIP
      case 'in':
        return TypedValue.COMPLEX_UNIT_IN
      case 'sp':
        return TypedValue.COMPLEX_UNIT_SP
      case 'px':
        return TypedValue.COMPLEX_UNIT_PX
      case 'mm':
        return TypedValue.COMPLEX_UNIT_MM
      case 'pt':
        return TypedValue.COMPLEX_UNIT_PT
      default:
        throw new IllegalArgumentException("Unknown unit $unit")
    }
  }

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
}
