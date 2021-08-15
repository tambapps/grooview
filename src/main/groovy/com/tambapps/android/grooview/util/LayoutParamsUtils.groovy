package com.tambapps.android.grooview.util

import android.view.View
import com.tambapps.android.grooview.view.ViewDecorator

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
    def margins = attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'margin')
    if (margins instanceof Number) {
      margins = [ margins ] * 4
    }
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

    // linear layout properties
    def weight = attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'weight') as Float
    if (weight != null) {
      layoutParams.weight = weight
    }
    def layoutGravity = attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'layoutGravity') as Integer
    if (layoutGravity != null) {
      layoutParams.layoutGravity = layoutGravity
    }

    // TODO document relative layout rules
    //  rules are a List<Integer|Map<Integer, Object>, meaning a list of verbs (e.g RelativeLayout.ALIGN_RIGHT)
    //  and/or a mapping between a verb and a view/view id
    // relative layout rules
    def rules = attributes.remove(LAYOUT_PARAMS_ATTRIBUTE_PREFIX + 'rules')
    for (rule in rules) {
      if (rule instanceof Map) {
        for (subjectedRule in rule) {
          layoutParams.addRule(subjectedRule.key,
              (subjectedRule.value instanceof ViewDecorator) || (subjectedRule.value instanceof View) ? subjectedRule.value.id : subjectedRule.value as Integer)
        }
      } else {
        layoutParams.addRule(rule as Integer)
      }
    }
  }
}
