package com.tambapps.android.grooview.view

import android.app.Activity
import android.os.Looper
import android.util.Log
import android.view.View
import com.tambapps.android.grooview.util.Utils
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * Decorator object that will executed underlying view method property calls in UI thread
 */
class ViewDecorator {
  private final _view

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
        errorDialog(e)
      }
    } else {
      ((Activity) _view.context).runOnUiThread {
        try {
          InvokerHelper.invokeMethod(_view, name, convertedArgs)
        } catch(Exception e) {
          errorDialog(e)
        }
      }
    }
  }

  def getProperty(String name) {
    if (name == '_view') {
      return _view
    }
    def value = InvokerHelper.getProperty(_view, name)
    return value instanceof View ? new ViewDecorator(value) : value
  }

  void setProperty(String name, Object newValue) {
    if (Looper.getMainLooper().isCurrentThread()) {
      try {
        smartSetProperty(_view, name, newValue)
      } catch (Exception e) {
        errorDialog(e)
      }
    } else {
      ((Activity) _view.context).runOnUiThread {
        try {
          smartSetProperty(_view, name, newValue)
        } catch (Exception e) {
          errorDialog(e)
        }
      }
    }
  }

  private void errorDialog(Exception e) {
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

}
