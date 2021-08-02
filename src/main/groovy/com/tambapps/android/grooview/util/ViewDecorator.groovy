package com.tambapps.android.grooview.util

import android.app.Activity
import android.os.Looper
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
      InvokerHelper.invokeMethod(_view, name, convertedArgs)
    } else {
      ((Activity) _view.context).runOnUiThread {
        InvokerHelper.invokeMethod(_view, name, convertedArgs)
      }
    }
  }

  def getProperty(String name) {
    return name == '_view' ? _view : InvokerHelper.getProperty(_view, name)
  }

  void setProperty(String name, Object newValue) {
    if (Looper.getMainLooper().isCurrentThread()) {
      InvokerHelper.setProperty(_view, name, newValue)
    } else {
      ((Activity) _view.context).runOnUiThread {
        InvokerHelper.setProperty(_view, name, newValue)
      }
    }
  }

}
