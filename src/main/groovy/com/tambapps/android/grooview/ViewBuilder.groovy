package com.tambapps.android.grooview


import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.AbsListView
import android.widget.AbsSeekBar
import android.widget.AbsSpinner
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ExpandableListView
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.GridView
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.TimePicker
import android.widget.ToggleButton
import android.widget.VideoView
import com.tambapps.android.grooview.factory.ReflectViewFactory
import com.tambapps.android.grooview.factory.ReflectViewGroupFactory
import com.tambapps.android.grooview.util.IdMapper
import org.codehaus.groovy.runtime.InvokerHelper

import java.nio.file.Path

class ViewBuilder extends FactoryBuilderSupport {

  final Object androidContext
  private final Object root
  private final IdMapper idMapper = new IdMapper()
  // map of View class -> default properties (name -> property)
  final Map<Class, Map<String, Object>> defaultViewProperties = [:]

  ViewBuilder(Object root) {
    this(root.context, root)
  }

  ViewBuilder(Object androidContext, Object root) {
    super(false)
    this.androidContext = androidContext
    this.root = root
    initialize()
    autoRegisterNodes()
  }

  private void initialize() {
    setVariable("vertical", LinearLayout.VERTICAL)
    setVariable("horizontal", LinearLayout.HORIZONTAL)
    setVariable("visible", View.VISIBLE)
    setVariable("invisible", View.INVISIBLE)
    setVariable("gone", View.GONE)
    setVariable("left", Gravity.LEFT)
    setVariable("start", Gravity.START)
    setVariable("top", Gravity.TOP)
    setVariable("right", Gravity.RIGHT)
    setVariable("end", Gravity.END)
    setVariable("bottom", Gravity.BOTTOM)
    setVariable("center", Gravity.CENTER)
    setVariable("center_horizontal", Gravity.CENTER_HORIZONTAL)
    setVariable("center_vertical", Gravity.CENTER_VERTICAL)
    // TODO text alignement text direction constants
  }

  void registerViews() {
    registerViewClass(View)
    registerViewClass(AbsListView)
    registerViewClass(AbsSeekBar)
    registerViewClass(AbsSpinner)
    registerViewClass(AutoCompleteTextView)
    registerViewClass(Button)
    registerViewClass(CalendarView)
    registerViewClass(CheckBox)
    registerViewClass(CheckedTextView)
    registerViewClass(CompoundButton)
    registerViewClass(DatePicker)
    registerViewClass(EditText)
    registerViewClass(ExpandableListView)
    registerViewGroupClass(FrameLayout)
    registerViewGroupClass(GridLayout)
    registerViewClass(GridView)
    registerViewGroupClass(HorizontalScrollView)
    registerViewClass(ImageButton)
    registerViewClass(ImageView)
    registerViewGroupClass(LinearLayout)
    registerViewGroupClass(ListView)
    registerViewClass(NumberPicker)
    registerViewClass(ProgressBar)
    registerViewClass(RadioButton)
    registerViewGroupClass(RadioGroup)
    registerViewClass(RatingBar)
    registerViewGroupClass(RelativeLayout)
    registerViewGroupClass(ScrollView)
    registerViewClass(SeekBar)
    registerViewClass(Spinner)
    registerViewClass(Switch)
    registerViewGroupClass(TableLayout)
    registerViewGroupClass(TableRow)
    registerViewClass(TextView)
    registerViewClass(TimePicker)
    registerViewClass(ToggleButton)
    registerViewClass(VideoView)
  }

  private void registerViewClass(Class clazz) {
    String simpleName = clazz.simpleName
    String name = simpleName[0].toLowerCase() + simpleName.substring(1)
    registerFactory(name, newReflectViewFactory(clazz))
  }

  protected newReflectViewFactory(Class clazz) {
    return new ReflectViewFactory(androidContext, clazz)
  }

  private void registerViewGroupClass(Class clazz) {
    String simpleName = clazz.simpleName
    String name = simpleName[0].toLowerCase() + simpleName.substring(1)
    registerFactory(name, newReflectViewGroupFactory( clazz))
  }

  protected newReflectViewGroupFactory(Class clazz) {
    return new ReflectViewGroupFactory(androidContext, clazz)
  }

  int generateId(String name, Object view) {
    return idMapper.generateId(name, view)
  }

  Integer toViewId(Object data) {
    if (data == null) return null
    switch (data) {
      case Integer:
        return data
      default:
        return idMapper[data.toString()]
    }
  }

  @Override
  protected void nodeCompleted(Object parent, Object node) {
    (parent ?: root).addView(node)
    super.nodeCompleted(parent, node)
  }

  @Override
  Object getProperty(String propertyName) {
    try {
      super.getProperty(propertyName)
    } catch (MissingPropertyException e) {
      def view = idMapper[root, propertyName]
      if (view == null) {
        throw e
      }
      return view
    }
  }

  @Override
  protected void setNodeAttributes(Object node, Map attributes) {
    for (Map.Entry entry : (Set<Map.Entry>) attributes.entrySet()) {
      String property = entry.getKey().toString()
      Object value = entry.getValue()
      if (property.endsWith("Color")) {
        value = color(value)
      }
      InvokerHelper.setProperty(node, property, value)
    }
  }

  // TODO add function for toast and dialog
  // useful methods to use when building

  Integer color(def data) {
    if (data == null) {
      return null
    }
    switch (data) {
      case Integer:
        return data
      case { it instanceof Number }:
        long argb = data.toLong()
        return Color.argb(((argb >> 32) & 255) as int, ((argb >> 16) & 255) as int, ((argb >> 8) & 255) as int, (argb & 255) as int)
      case String:
        return Color.parseColor(data)
      default:
        throw new IllegalArgumentException("Cannot convert object of type ${data.class.simpleName} to color integer")
    }
  }
  Drawable drawable(def data) {
    if (data == null) {
      return null
    }
    switch (data) {
      case File:
      case Path:
        return Drawable.createFromPath(data.toString())
      case String:
        // let's assume it's an url
        def b = data.toURL().withInputStream {
          BitmapFactory.decodeStream(it)
        }
        return new BitmapDrawable(androidContext.resources, b)
      default:
        throw new IllegalArgumentException("Cannot convert object of type ${data.class.simpleName} to Drawable")
    }
  }
}
