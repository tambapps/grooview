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
import android.widget.Toast
import android.widget.ToggleButton
import android.widget.VideoView
import com.tambapps.android.grooview.factory.ReflectViewFactory
import com.tambapps.android.grooview.factory.ReflectViewGroupFactory
import com.tambapps.android.grooview.util.IdMapper
import com.tambapps.android.grooview.util.Utils
import com.tambapps.android.grooview.view.ObservableCollectionViewDecorator
import com.tambapps.android.grooview.view.ViewDecorator
import org.codehaus.groovy.runtime.InvokerHelper

import java.nio.file.Path

// TODO listeners are executed in main thread
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
    this.root = root instanceof ViewDecorator ? root : new ViewDecorator(root)
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
    Factory parentFactory = getProxyBuilder().getParentFactory()
    if (parentFactory != null) {
      // the parent factory will be responsible of adding the child view to parent
      // allowing to handle custom behaviours (e.g AdapterViews)
      parentFactory.onNodeCompleted(getProxyBuilder().getChildBuilder(), parent, node)
    } else {
      root.addView(node)
    }
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

  // useful methods to use when building

  def dialog(Map args) {
    def dialogBuilder = Class.forName('androidx.appcompat.app.AlertDialog$Builder').newInstance(androidContext)
    if (args.title) {
      dialogBuilder.setTitle(args.title)
    }
    if (args.message) {
      dialogBuilder.setMessage(args.message)
    }
    if (args.negativeButton) {
      dialogBuilder.setNegativeButton(args.negativeButton, null)
    }
    if (args.neutralButton) {
      dialogBuilder.setNeutralButton(args.neutralButton, null)
    }
    if (args.positiveButton) {
      dialogBuilder.setPositiveButton(args.positiveButton, null)
    }
    return dialogBuilder
  }

  void toast(String text, int length = Toast.LENGTH_SHORT) {
    Toast.makeText(androidContext, text, length).show()
  }

  Integer color(def data) {
    Utils.color(data)
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
