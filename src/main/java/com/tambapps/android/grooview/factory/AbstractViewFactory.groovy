package com.tambapps.android.grooview.factory

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import org.codehaus.groovy.runtime.InvokerHelper

abstract class AbstractViewFactory extends AbstractFactory {

  private final Context context


  AbstractViewFactory(Context context) {
    this.context = context
  }

  @Override
  Object newInstance(FactoryBuilderSupport builder, Object name, Object value,
      Map attributes) throws InstantiationException, IllegalAccessException {
    Object view = newInstance(context)

    ObjectPropertySetter setter = new ObjectPropertySetter(view, attributes, builder)
    setter.with {
      // TODO handle pixel attributes, drawable attributes (handle URL, Files)
      //  handle margin, marginLeft, top, ... same for paddings
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
      handleProperty("background", Drawable)
      handleProperty("backgroundColor", int)
      handleProperty("backgroundDrawable", Drawable)
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
      handleProperty("foreground", Drawable)
      handleProperty("foregroundGravity", int)
      //handleProperty("foregroundTintBlendMode", android.graphics.BlendMode)
      handleProperty("foregroundTintList", ColorStateList)
      handleProperty("foregroundTintMode", PorterDuff.Mode)
      handleProperty("hapticFeedbackEnabled", boolean)
      handleProperty("hasTransientState", boolean)
      handleProperty("horizontalFadingEdgeEnabled", boolean)
      handleProperty("horizontalScrollBarEnabled", boolean)
      handleProperty("horizontalScrollbarThumbDrawable", Drawable)
      handleProperty("horizontalScrollbarTrackDrawable", Drawable)
      handleProperty("hovered", boolean)
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
      handleProperty("scrollBarSize", int)
      handleProperty("scrollBarStyle", int)
      //handleProperty("scrollCaptureCallback", android.view.ScrollCaptureCallback)
      handleProperty("scrollCaptureHint", int)
      handleProperty("scrollContainer", boolean)
      handleProperty("scrollX", int)
      handleProperty("scrollY", int)
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
      handleProperty("verticalScrollbarThumbDrawable", Drawable)
      handleProperty("verticalScrollbarTrackDrawable", Drawable)
      //handleProperty("viewTranslationCallback", android.view.translation.ViewTranslationCallback)
      handleProperty("visibility", Integer)
      handleProperty("willNotCacheDrawing", boolean)
      handleProperty("willNotDraw", boolean)
      //handleProperty("windowInsetsAnimationCallback", android.view.WindowInsetsAnimation.Callback)
      handleProperty("x", float)
      handleProperty("y", float)
      handleProperty("z", float)

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

    return view
  }

  // should return a View but we're using Object type for testing purpose
  protected abstract Object newInstance(Context context)

  protected Integer toPixels(def data) {
    switch (data) {
      case Integer:
        return data
      case String:
        String number = data.takeWhile {it.isDigit() }
        int unit = getComplexUnit(data.substring(number.size()))
        return (int) TypedValue.applyDimension(unit, number.toInteger(), context.resources.displayMetrics)
      default:
        throw new IllegalArgumentException("Type ${data.class.simpleName} is not handled for units")
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
      def value = attributes[mapPropertyName]
      if (value != null) {
        InvokerHelper.setProperty(view, objectPropertyName, converter(value))
      }
    }
  }
}
