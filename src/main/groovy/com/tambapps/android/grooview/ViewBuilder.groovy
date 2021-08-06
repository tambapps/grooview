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
import com.tambapps.android.grooview.factory.ReflectAdapterViewFactory
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
    registerViewClass(AutoCompleteTextView)
    registerViewClass(Button)
    registerViewClass(CalendarView)
    registerViewClass(CheckBox)
    registerViewClass(CheckedTextView)
    registerViewClass(CompoundButton)
    registerViewClass(DatePicker)
    registerViewClass(EditText)
    registerAdapterViewClass(ExpandableListView)
    registerViewGroupClass(FrameLayout)
    registerViewGroupClass(GridLayout)
    registerAdapterViewClass(GridView)
    registerViewGroupClass(HorizontalScrollView)
    registerViewClass(ImageButton)
    registerViewClass(ImageView)
    registerViewGroupClass(LinearLayout)
    registerAdapterViewClass(ListView)
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

  private void registerAdapterViewClass(Class clazz) {
    String simpleName = clazz.simpleName
    String name = simpleName[0].toLowerCase() + simpleName.substring(1)
    registerFactory(name, newReflectAdapterViewFactory( clazz))
  }

  protected newReflectAdapterViewFactory(Class clazz) {
    return new ReflectAdapterViewFactory(androidContext, clazz)
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

  // method copied from super class in order to add custom behaviour for list widgets
  @Override
  protected Object dispatchNodeCall(Object name, Object args) {
    Object node;
    Closure closure = null;
    List list = InvokerHelper.asList(args);

    final boolean needToPopContext;
    if (getProxyBuilder().getContexts().isEmpty()) {
      // should be called on first build method only
      getProxyBuilder().newContext();
      needToPopContext = true;
    } else {
      needToPopContext = false;
    }

    try {
      Map namedArgs = Collections.EMPTY_MAP;

      // the arguments come in like [named_args?, args..., closure?]
      // so peel off a hashmap from the front, and a closure from the
      // end and presume that is what they meant, since there is
      // no way to distinguish node(a:b,c,d) {..} from
      // node([a:b],[c,d], {..}), i.e. the user can deliberately confuse
      // the builder and there is nothing we can really do to prevent
      // that

      if ((!list.isEmpty())
          && (list.get(0) instanceof LinkedHashMap)) {
        namedArgs = (Map) list.get(0);
        list = list.subList(1, list.size());
      }
      if ((!list.isEmpty())
          && (list.get(list.size() - 1) instanceof Closure)) {
        closure = (Closure) list.get(list.size() - 1);
        list = list.subList(0, list.size() - 1);
      }
      Object arg;
      if (list.isEmpty()) {
        arg = null;
      } else if (list.size() == 1) {
        arg = list.get(0);
      } else {
        arg = list;
      }
      node = getProxyBuilder().createNode(name, namedArgs, arg);

      Object current = getProxyBuilder().getCurrent();
      if (current != null) {
        getProxyBuilder().setParent(current, node);
      }

      if (closure != null) {
        Factory parentFactory = getProxyBuilder().getCurrentFactory();
        if (parentFactory.isLeaf()) {
          throw new RuntimeException("'" + name + "' doesn't support nesting.");
        }
        boolean processContent = true;
        if (parentFactory.isHandlesNodeChildren()) {
          processContent = parentFactory.onNodeChildren(this, node, closure);
        }
        if (processContent) {
          // push new node on stack
          String parentName = getProxyBuilder().getCurrentName();
          Map parentContext = getProxyBuilder().getContext();
          getProxyBuilder().newContext();
          try {
            getProxyBuilder().getContext().put(OWNER, closure.getOwner());
            getProxyBuilder().getContext().put(CURRENT_NODE, node);
            getProxyBuilder().getContext().put(PARENT_FACTORY, parentFactory);
            getProxyBuilder().getContext().put(PARENT_NODE, current);
            getProxyBuilder().getContext().put(PARENT_CONTEXT, parentContext);
            getProxyBuilder().getContext().put(PARENT_NAME, parentName);
            getProxyBuilder().getContext().put(PARENT_BUILDER, parentContext.get(CURRENT_BUILDER));
            getProxyBuilder().getContext().put(CURRENT_BUILDER, parentContext.get(CHILD_BUILDER));
            // lets register the builder as the delegate
            getProxyBuilder().setClosureDelegate(closure, node);
            // see ReflectViewGroupFactory.callChildClosure
            parentFactory.callChildClosure(closure, node)
          } finally {
            getProxyBuilder().popContext();
          }
        }
      }

      getProxyBuilder().nodeCompleted(current, node);
      node = getProxyBuilder().postNodeCompletion(current, node);
    } finally {
      if (needToPopContext) {
        // pop the first context
        getProxyBuilder().popContext();
      }
    }
    return node;
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
