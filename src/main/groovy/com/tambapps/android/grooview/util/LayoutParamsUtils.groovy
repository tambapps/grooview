package com.tambapps.android.grooview.util

final class LayoutParamsUtils {

  static final String LAYOUT_PARAMS_ATTRIBUTE_PREFIX = "layoutParams_"

  static void handleLayoutParamsProperties(def layoutParams, Map attributes) {
    def width = attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'width') as Integer
    if (width != null) {
      layoutParams.width = width
    }
    def height = attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'height') as Integer
    if (height != null) {
      layoutParams.height = height
    }

    // margin margin
    def margins = (attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'margin') as List)
    if (margins != null) {
      if (margins.size() != 4) {
        throw new IllegalArgumentException("Margin should have 4 values: left, top, right and bottom")
      }
      layoutParams.setMargins(*(margins.collect {it as int}))
    }
    def marginStart = (attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'marginStart') ?: attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'marginLeft')) as Integer
    if (marginStart != null) layoutParams.leftMargin = marginStart
    def marginTop = attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'marginTop') as Integer
    if (marginTop != null) layoutParams.topMargin = marginTop
    def marginEnd = (attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'marginEnd') ?: attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'marginRight')) as Integer
    if (marginEnd != null) layoutParams.rightMargin = marginEnd
    def marginBottom = attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'marginBottom') as Integer
    if (marginBottom != null) layoutParams.bottomMargin = marginBottom

    // relative layout rules
    def rules = attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'rules')?.collect { it as Integer }
    for (rule in rules) {
      layoutParams.addRule(rule)
    }
  }
}
