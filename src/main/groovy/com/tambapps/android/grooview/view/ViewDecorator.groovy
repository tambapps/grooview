package com.tambapps.android.grooview.view

import android.app.Activity
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.tambapps.android.grooview.util.LayoutParamsUtils
import com.tambapps.android.grooview.util.Utils
import com.tambapps.android.grooview.util.ViewGroupChildren
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import org.codehaus.groovy.runtime.InvokerHelper
import org.codehaus.groovy.runtime.typehandling.GroovyCastException

/**
 * Decorator object that will executed underlying view method property calls in UI thread
 * Properties starting with '_' are treated as additional properties
 */
class ViewDecorator {
  private final _view
  private final Map additionalProperties = [:]

  ViewDecorator(_view) {
    this._view = _view
  }

  def invokeMethod(String name, args) {
    // replace decorator by actual object
    def convertedArgs = args.collect { it instanceof ViewDecorator ? it._view : it } as Object[]

    if (Looper.getMainLooper().isCurrentThread()) {
      try {
        return InvokerHelper.invokeMethod(_view, name, convertedArgs)
      } catch (Exception e) {
        // ignore IntelIJ warning. THIS IS IMPORTANT for differentiating static method call from
        ViewDecorator.errorDialog(_view, e)
      }
    } else {
      ((Activity) _view.context).runOnUiThread {
        try {
          InvokerHelper.invokeMethod(_view, name, convertedArgs)
        } catch(Exception e) {
          // ignore IntelIJ warning
          ViewDecorator.errorDialog(_view, e)
        }
      }
    }
  }

  def getProperty(String name) {
    if (name == '_view') {
      return _view
    } else if (name.startsWith('_')) {
      return additionalProperties[name]
    } else if (name == 'additionalProperties') {
      return additionalProperties
    }
    def value = InvokerHelper.getProperty(_view, name)
    return value instanceof View ? new ViewDecorator(value) : value
  }

  void setLayoutParamsProperty(String name, Object value) {
    additionalProperties[LayoutParamsUtils.LAYOUT_PARAMS_ATTRIBUTE_PREFIX + name] = value
  }

  void setProperty(String name, Object newValue) {
    if (name.startsWith('_')) {
      additionalProperties[name] = newValue
      return
    }
    if (Looper.getMainLooper().isCurrentThread()) {
      try {
        // ignore IntelIJ warning. THIS IS IMPORTANT!
        ViewDecorator.smartSetProperty(_view, name, newValue)
      } catch (Exception e) {
        // ignore IntelIJ warning
        ViewDecorator.errorDialog(_view, e)
      }
    } else {
      ((Activity) _view.context).runOnUiThread {
        try {
          // ignore IntelIJ warning. THIS IS IMPORTANT for differentiating static method call from
          // instance method call
          ViewDecorator.smartSetProperty(_view, name, newValue)
        } catch (Exception e) {
          // ignore IntelIJ warning
          ViewDecorator.errorDialog(_view, e)
        }
      }
    }
  }

  /**
   * Methods used for ViewGroups. It will add the chieldView in its own _view(Group). It will also
   * generate the right layout params
   * @param view
   */
  void addView(def childView) {
    if (Looper.getMainLooper().isCurrentThread()) {
      try {
        _view.addView(childView._view)
        LayoutParamsUtils.handleLayoutParamsProperties(childView.layoutParams, childView.additionalProperties)
      } catch (Exception e) {
        // ignore IntelIJ warning. THIS IS IMPORTANT for differentiating static method call from
        ViewDecorator.errorDialog(_view, e)
      }
    } else {
      ((Activity) _view.context).runOnUiThread {
        try {
          _view.addView(childView._view)
          LayoutParamsUtils.handleLayoutParamsProperties(childView.layoutParams, childView.additionalProperties)
        } catch(Exception e) {
          // ignore IntelIJ warning
          ViewDecorator.errorDialog(_view, e)
        }
      }
    }
  }

  private static void errorDialog(def _view, Exception e) {
    // didn't find androidx appcompat dependency so we have to do a little hack
    Log.e("Grooview", "error on main thread", e)
    Class.forName('androidx.appcompat.app.AlertDialog$Builder').newInstance(_view.context)
        .setTitle("An error occurred on main thread")
        .setMessage("${e.class.simpleName}: ${e.message}")
        .setPositiveButton("ok", null)
        .show()
  }

  private static void smartSetProperty(Object view, String property, def value) {
    if (property.endsWith("Color")) {
      value = Utils.color(value)
    } else if (value instanceof Closure) {
      value = decorateClosure(value)
    }
    InvokerHelper.setProperty(view, property, value)
  }

  // transform view args into ViewDecorator args
  private static Closure decorateClosure(final Closure closure) {
    return { Object[] args ->
      def convertedArgs = args.collect { it instanceof View ? new ViewDecorator(it) : it } as Object[]
      return closure(*convertedArgs)
    }
  }

  @Override
  boolean equals(Object obj) {
    if (obj == null) {
      return null
    }
    if (obj instanceof View) {
      return _view == obj
    } else if (obj instanceof ViewDecorator) {
      return _view == obj._view
    }
    return super.equals(obj)
  }

  @Override
  int hashCode() {
    return _view.hashCode()
  }

  // for viewGroups
  List getChildren() {
    return new ViewGroupChildren(_view as ViewGroup)
  }

  def asType(Class<?> target) {
    if (View.isAssignableFrom(target)) {
      return _view
    } else {
      throw new GroovyCastException('')
    }
  }

}
