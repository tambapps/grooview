package com.tambapps.android.grooview


import android.graphics.BitmapFactory
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
import com.tambapps.android.grooview.util.PixelsCategory

import javax.swing.text.html.ListView
import java.nio.file.Path

class ViewBuilder extends FactoryBuilderSupport {

  static Object build(Object context, Object parent, Closure closure) {
    PixelsCategory.context = context
    closure.setDelegate(new ViewBuilder(context, parent))
    closure.setResolveStrategy(Closure.DELEGATE_FIRST)
    return closure()
  }

  private final Object context
  private final Object root
  private final IdMapper idMapper = new IdMapper()

  ViewBuilder(Object root, boolean initialize = true) {
    this(root.context, root, initialize)
  }

  ViewBuilder(Object context, Object root, boolean init = true) {
    super(init)
    this.context = context
    this.root = root
    initialize()
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
    registerFactory(name, new ReflectViewFactory(context, clazz))
  }

  private void registerViewGroupClass(Class clazz) {
    String simpleName = clazz.simpleName
    String name = simpleName[0].toLowerCase() + simpleName.substring(1)
    registerFactory(name, new ReflectViewGroupFactory(context, clazz))
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

  // TODO add function for toast and dialog
  // useful methods to use when building
  private Drawable toDrawable(def data) {
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
        return new BitmapDrawable(context.resources, b)
      default:
        throw new IllegalArgumentException("Cannot convert object of type ${data.class.simpleName} to Drawable")
    }
  }
}
