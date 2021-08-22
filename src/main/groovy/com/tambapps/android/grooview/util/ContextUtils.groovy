package com.tambapps.android.grooview.util

import android.R
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import groovy.transform.CompileStatic

@CompileStatic
class ContextUtils {

  static Drawable rippleDrawable(Context context) {
    TypedValue outValue = new TypedValue()
    Resources.Theme theme = context.getTheme()
    theme.resolveAttribute(R.attr.selectableItemBackground, outValue, true)
    return context.resources.getDrawable(outValue.resourceId, theme)
  }
}
