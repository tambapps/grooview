package com.tambapps.android.grooview.util

import android.app.Activity
import android.app.AlertDialog
import android.os.Looper
import android.view.View
import org.codehaus.groovy.runtime.DefaultGroovyMethods
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
    // check if method exists before calling the main thread
    if (!_view.metaClass.respondsTo(name)) {
      throw new MissingMethodException(name, View, convertedArgs)
    }
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
    // will not particularly be executed in main thread so no need to check here
    return name == '_view' ? _view : InvokerHelper.getProperty(_view, name)
  }

  void setProperty(String name, Object newValue) {
    if (!DefaultGroovyMethods.hasProperty(_view, name)) {
      throw new MissingPropertyException(name, _view.class)
    }
    if (Looper.getMainLooper().isCurrentThread()) {
      try {
        InvokerHelper.setProperty(_view, name, newValue)
      } catch (Exception e) {
        errorDialog(e)
      }
    } else {
      ((Activity) _view.context).runOnUiThread {
        try {
          InvokerHelper.setProperty(_view, name, newValue)
        } catch (Exception e) {
          errorDialog(e)
        }
      }
    }
  }

  private void errorDialog(Exception e) {
    new AlertDialog.Builder(_view.context)
        .setTitle("An error occured on main thread")
        .setMessage("${e.class.simpleName}: ${e.message}")
        .setPositiveButton("ok", null)
        .show()
  }
}
