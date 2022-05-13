package com.tambapps.android.grooview

import android.content.res.Resources

class PixelCategory {

  // should be initialized by the code using this category

  /* TODO use the below code, that doesn't require context
  // dp to px
val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Float.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
// sp to px
val Int.sp: Int get() = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()
val Float.sp: Int get() = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()
   */
  static int getPx(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_PX, context)
  }

  static int getDp(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_DIP, context)
  }

  static int getDip(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_DIP, context)
  }

  static int getSp(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_SP, context)
  }

  static int getPt(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_SP, context)
  }

  static int getIn(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_IN, context)
  }
  static int getMm(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_MM, context)
  }

  static int convert(Number number, int unit, Object context) {
    return (int) TypedValue.applyDimension(unit, number.toInteger(), context.resources.displayMetrics)
  }

}
