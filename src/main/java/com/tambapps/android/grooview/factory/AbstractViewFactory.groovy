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
    ObjectPropertySetter setter = new ObjectPropertySetter(node, attributes, builder)
    setter.with {
      // TODO remove 'simple' properties (the ones that don't need custom conversion) from here
      //  and let groovy handle them standard bean property matching for remaining attributes
      handleProperty("accessibilityDelegate", View.AccessibilityDelegate)
      handleProperty("accessibilityHeading", boolean)
      handleProperty("accessibilityLiveRegion", int)
      handleProperty("accessibilityPaneTitle", CharSequence)
      handleProperty("accessibilityTraversalAfter", int)
      handleProperty("accessibilityTraversalBefore", int)
      handleProperty("activated", boolean)
      handleProperty("allowClickWhenDisabled", boolean)
      handleProperty("alpha", float)
      handleProperty("animation", Animation)
      handleProperty("animationMatrix", Matrix)
      handleProperty("autofillHints", String[])
      //handleProperty("autofillId", android.view.autofill.AutofillId)
      handleProperty("background", this.&toDrawable)
      handleProperty("backgroundColor", int)
      handleProperty("backgroundDrawable", this.&toDrawable)
      handleProperty("backgroundResource", int)
      //handleProperty("backgroundTintBlendMode", android.graphics.BlendMode)
      handleProperty("backgroundTintList", ColorStateList)
      handleProperty("backgroundTintMode", PorterDuff.Mode)
      handleProperty("bottom", int)
      handleProperty("cameraDistance", float)
      handleProperty("childrenDrawingOrderEnabled", boolean)
      handleProperty("clickable", boolean)
      handleProperty("clipBounds", Rect)
      handleProperty("clipToOutline", boolean)
      //handleProperty("contentCaptureSession", android.view.contentcapture.ContentCaptureSession)
      handleProperty("contentDescription", CharSequence)
      handleProperty("contextClickable", boolean)
      handleProperty("decorFitsSystemWindows", boolean)
      handleProperty("defaultFocusHighlightEnabled", boolean)
      handleProperty("drawingCacheBackgroundColor", int)
      handleProperty("drawingCacheEnabled", boolean)
      handleProperty("drawingCacheQuality", int)
      handleProperty("duplicateParentStateEnabled", boolean)
      handleProperty("elevation", float)
      handleProperty("enabled", boolean)
      handleProperty("fadingEdgeLength", int)
      handleProperty("filterTouchesWhenObscured", boolean)
      handleProperty("fitInsetsTypes", int)
      handleProperty("fitsSystemWindows", boolean)
      handleProperty("focusable", boolean)
      handleProperty("focusableInTouchMode", boolean)
      handleProperty("focusedByDefault", boolean)
      handleProperty("forceDarkAllowed", boolean)
      handleProperty("foreground", this.&toDrawable)
      handleProperty("foregroundGravity", int)
      //handleProperty("foregroundTintBlendMode", android.graphics.BlendMode)
      handleProperty("foregroundTintList", ColorStateList)
      handleProperty("foregroundTintMode", PorterDuff.Mode)
      handleProperty("hapticFeedbackEnabled", boolean)
      handleProperty("hasTransientState", boolean)
      handleProperty("horizontalFadingEdgeEnabled", boolean)
      handleProperty("horizontalScrollBarEnabled", boolean)
      handleProperty("horizontalScrollbarThumbDrawable", this.&toDrawable)
      handleProperty("horizontalScrollbarTrackDrawable", this.&toDrawable)
      handleProperty("hovered", boolean)
      // TODO handle Strings for ids (use hashcode in the back) and allow to retrieve views by the ids (String given)
      handleProperty("id", int)
      handleProperty("importantForAccessibility", int)
      handleProperty("importantForAutofill", int)
      handleProperty("importantForContentCapture", int)
      handleProperty("keepScreenOn", boolean)
      handleProperty("keyboardNavigationCluster", boolean)
      handleProperty("labelFor", int)
      handleProperty("layerPaint", Paint)
      handleProperty("layoutDirection", int)
      handleProperty("layoutParams", ViewGroup.LayoutParams)
      handleProperty("left", int)
      handleProperty("longClickable", boolean)
      handleProperty("minimumHeight", int)
      handleProperty("minimumWidth", int)
      handleProperty("nestedScrollingEnabled", boolean)
      handleProperty("nextClusterForwardId", int)
      handleProperty("nextFocusDownId", int)
      handleProperty("nextFocusForwardId", int)
      handleProperty("nextFocusLeftId", int)
      handleProperty("nextFocusRightId", int)
      handleProperty("nextFocusUpId", int)
      //handleProperty("onApplyWindowInsetsListener", View.OnApplyWindowInsetsListener)
      //handleProperty("onCapturedPointerListener", View.OnCapturedPointerListener)
      handleProperty("onClickListener", View.OnClickListener)
      //handleProperty("onContextClickListener", View.OnContextClickListener)
      handleProperty("onCreateContextMenuListener", View.OnCreateContextMenuListener)
      handleProperty("onDragListener", View.OnDragListener)
      handleProperty("onFocusChangeListener", View.OnFocusChangeListener)
      handleProperty("onGenericMotionListener", View.OnGenericMotionListener)
      handleProperty("onHoverListener", View.OnHoverListener)
      handleProperty("onKeyListener", View.OnKeyListener)
      handleProperty("onLongClickListener", View.OnLongClickListener)
      //handleProperty("onScrollChangeListener", View.OnScrollChangeListener)
      handleProperty("onSystemUiVisibilityChangeListener", View.OnSystemUiVisibilityChangeListener)
      handleProperty("onTouchListener", View.OnTouchListener)
      handleProperty("outlineAmbientShadowColor", int)
      //handleProperty("outlineProvider", android.view.ViewOutlineProvider)
      handleProperty("outlineSpotShadowColor", int)
      handleProperty("overScrollMode", int)
      handleProperty("pivotX", float)
      handleProperty("pivotY", float)
      //handleProperty("pointerIcon", android.view.PointerIcon)
      handleProperty("pressed", boolean)
      //handleProperty("renderEffect", android.graphics.RenderEffect)
      handleProperty("revealOnFocusHint", boolean)
      handleProperty("right", int)
      handleProperty("rotation", float)
      handleProperty("rotationX", float)
      handleProperty("rotationY", float)
      handleProperty("saveEnabled", boolean)
      handleProperty("saveFromParentEnabled", boolean)
      handleProperty("scaleX", float)
      handleProperty("scaleY", float)
      handleProperty("screenReaderFocusable", boolean)
      handleProperty("scrollBarDefaultDelayBeforeFade", int)
      handleProperty("scrollBarFadeDuration", int)
      handleProperty("scrollBarSize", this.&toPixels)
      handleProperty("scrollBarStyle", this.&toPixels)
      //handleProperty("scrollCaptureCallback", android.view.ScrollCaptureCallback)
      handleProperty("scrollCaptureHint", int)
      handleProperty("scrollContainer", boolean)
      handleProperty("scrollX", this.&toPixels)
      handleProperty("scrollY", this.&toPixels)
      handleProperty("scrollbarFadingEnabled", boolean)
      handleProperty("selected", boolean)
      handleProperty("soundEffectsEnabled", boolean)
      handleProperty("stateDescription", CharSequence)
      //handleProperty("stateListAnimator", android.animation.StateListAnimator)
      // list of android Rect
      handleProperty("systemGestureExclusionRects", List)
      handleProperty("systemUiVisibility", int)
      handleProperty("tag", Object)
      handleProperty("textAlignment", int)
      handleProperty("textDirection", int)
      handleProperty("tooltipText", CharSequence)
      handleProperty("top", int)
      handleProperty("touchDelegate", TouchDelegate)
      handleProperty("transitionAlpha", float)
      handleProperty("transitionName", String)
      handleProperty("transitionVisibility", int)
      handleProperty("translationX", float)
      handleProperty("translationY", float)
      handleProperty("translationZ", float)
      handleProperty("verticalFadingEdgeEnabled", boolean)
      handleProperty("verticalScrollBarEnabled", boolean)
      handleProperty("verticalScrollbarPosition", int)
      handleProperty("verticalScrollbarThumbDrawable", this.&toDrawable)
      handleProperty("verticalScrollbarTrackDrawable", this.&toDrawable)
      //handleProperty("viewTranslationCallback", android.view.translation.ViewTranslationCallback)
      handleProperty("visibility", Integer)
      handleProperty("willNotCacheDrawing", boolean)
      handleProperty("willNotDraw", boolean)
      //handleProperty("windowInsetsAnimationCallback", android.view.WindowInsetsAnimation.Callback)
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

  private static class ObjectPropertySetter {
    private final Object view
    private final Map attributes
    private final Object delegate

    ObjectPropertySetter(Object view, Map attributes, Object delegate) {
      this.view = view
      this.attributes = attributes
      this.delegate = delegate
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
}
