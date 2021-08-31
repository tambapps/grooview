package com.tambapps.android.grooview.util

import android.view.View
import android.widget.RelativeLayout
import com.tambapps.android.grooview.view.ViewDecorator

final class LayoutParamsUtils {

  static final String LAYOUT_PARAMS_ATTRIBUTE_PREFIX = "layoutParams_"

  static void handleLayoutParamsProperties(def layoutParams, Map attributes) {
    def width = attributes.remove('width') as Integer
    if (width != null) {
      layoutParams.width = width
    }
    def height = attributes.remove('height') as Integer
    if (height != null) {
      layoutParams.height = height
    }

    // margin margin
    def margins = attributes.remove('margin')
    if (margins instanceof Number) {
      margins = [ margins ] * 4
    }
    if (margins != null) {
      if (margins.size() != 4) {
        throw new IllegalArgumentException("Margin should have 4 values: left, top, right and bottom")
      }
      layoutParams.setMargins(*(margins.collect {it as int}))
    }
    def marginStart = (attributes.remove('marginStart') ?: attributes.remove('marginLeft')) as Integer
    if (marginStart != null) layoutParams.leftMargin = marginStart
    def marginTop = attributes.remove('marginTop') as Integer
    if (marginTop != null) layoutParams.topMargin = marginTop
    def marginEnd = (attributes.remove('marginEnd') ?: attributes.remove('marginRight')) as Integer
    if (marginEnd != null) layoutParams.rightMargin = marginEnd
    def marginBottom = attributes.remove('marginBottom') as Integer
    if (marginBottom != null) layoutParams.bottomMargin = marginBottom

    // linear layout properties
    def weight = attributes.remove('weight') as Float
    if (weight != null) {
      layoutParams.weight = weight
    }
    def layoutGravity = attributes.remove('layoutGravity') as Integer
    if (layoutGravity != null) {
      layoutParams.layoutGravity = layoutGravity
    }


    if (layoutParams instanceof RelativeLayout.LayoutParams) {
      handleRelativeLayoutParamsProperties(layoutParams, attributes)
    }
    // relative layout rules

  }

  private static void handleRelativeLayoutParamsProperties(RelativeLayout.LayoutParams layoutParams, Map attributes) {
    // TODO document relative layout rules
    //  rules are a List<Integer|Map<Integer, Object>, meaning a list of verbs (e.g RelativeLayout.ALIGN_RIGHT)
    //  and/or a mapping between a verb and a view/view id
    def rules = attributes.remove('rules')
    for (rule in rules) {
      if (rule instanceof Map) {
        for (subjectedRule in rule) {
          layoutParams.addRule((Integer) subjectedRule.key,
              (Integer) ((subjectedRule.value instanceof ViewDecorator) || (subjectedRule.value instanceof View) ? subjectedRule.value.id : subjectedRule.value))
        }
      } else {
        layoutParams.addRule(rule as Integer)
      }
    }

    // TODO document this also
    // TODO test this
    // or you can enter rules as view properties
    if (attributes.remove('centerInParent')) {
      layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
    }
    if (attributes.remove('alignParentStart') || attributes.remove('alignParentLeft')) {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
    }
    if (attributes.remove('alignParentTop')) {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
    }
    if (attributes.remove('alignParentEnd') || attributes.remove('alignParentRight')) {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
    }
    if (attributes.remove('alignParentBottom')) {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
    }
    handleRelativeLayoutParamRule(layoutParams, attributes, 'alignStart', RelativeLayout.ALIGN_START)
    handleRelativeLayoutParamRule(layoutParams, attributes, 'alignLeft', RelativeLayout.ALIGN_START)
    handleRelativeLayoutParamRule(layoutParams, attributes, 'alignTop', RelativeLayout.ALIGN_TOP)
    handleRelativeLayoutParamRule(layoutParams, attributes, 'alignEnd', RelativeLayout.ALIGN_END)
    handleRelativeLayoutParamRule(layoutParams, attributes, 'alignRight', RelativeLayout.ALIGN_RIGHT)
    handleRelativeLayoutParamRule(layoutParams, attributes, 'alignBottom', RelativeLayout.ALIGN_BOTTOM)
    handleRelativeLayoutParamRule(layoutParams, attributes, 'centerHorizontal', RelativeLayout.CENTER_HORIZONTAL)
    handleRelativeLayoutParamRule(layoutParams, attributes, 'centerVertical', RelativeLayout.CENTER_VERTICAL)
    handleRelativeLayoutParamRule(layoutParams, attributes, 'startOf', RelativeLayout.START_OF)
    handleRelativeLayoutParamRule(layoutParams, attributes, 'leftOf', RelativeLayout.LEFT_OF)
    handleRelativeLayoutParamRule(layoutParams, attributes, 'endOf', RelativeLayout.END_OF)
    handleRelativeLayoutParamRule(layoutParams, attributes, 'rightOf', RelativeLayout.RIGHT_OF)
  }

  private static void handleRelativeLayoutParamRule(RelativeLayout.LayoutParams layoutParams, Map attributes, String name, int rule) {
    def viewId = attributes.remove(name)
    if (viewId != null && viewId instanceof Integer) {
      layoutParams.addRule(rule, viewId)
    }
  }
}
