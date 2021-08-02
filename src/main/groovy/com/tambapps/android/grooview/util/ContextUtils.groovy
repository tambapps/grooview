package com.tambapps.android.grooview.util

import android.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue

class ContextUtils {

  static Drawable rippleDrawable(Context context) {
    TypedValue outValue = new TypedValue()
    context.getTheme().resolveAttribute(R.attr.selectableItemBackground, outValue, true)
    return context.resources.getDrawable(outValue.resourceId)
  }
}
