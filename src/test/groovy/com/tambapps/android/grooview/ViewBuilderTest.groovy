package com.tambapps.android.grooview

import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.tambapps.android.grooview.util.ClassEnhancer
import com.tambapps.android.grooview.util.FakeViewBuilder
import com.tambapps.android.grooview.util.FakeView
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

import java.util.concurrent.atomic.AtomicInteger

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertTrue

class ViewBuilderTest {

  private static final AtomicInteger ID_GENERATOR = new AtomicInteger()

  private FakeView root = new FakeView().with {
    type = "ViewGroup"
    it
  }
  @BeforeAll
  static void mock() {
    View.metaClass.static.generateViewId = { ID_GENERATOR.getAndIncrement() }
    TypedValue.metaClass.static.convert = { new Random().nextInt() }
    Looper.metaClass.static.getMainLooper = {
      return [isCurrentThread: { true }]
    }
    ClassEnhancer.metaClass.static.enhanceClasses = { ->

    }
  }

  @Test
  void test() {
    def result = build {
      view()
    }
    assertEquals('View', result.type)
  }

  @Test
  void testSetAttribute() {
    def result = build {
      view(visibility: visible)
    }
    assertEquals("View", result.type)
    assertEquals(View.VISIBLE, result.visibility)
  }

  @Test
  void testSetBeanAttribute() {
    def result = build {
      view(alpha: 1.234)
    }
    assertEquals("View", result.type)
    assertEquals(1.234, result.alpha)
  }

  @Test
  void testSetInterfaceAttribute() {
    def result = build {
      view(onLongClickListener: { true })
    }
    assertEquals("View", result.type)
    assertNotNull(result.onLongClickListener)
    assertTrue(result.onLongClickListener(null))
  }

  @Test
  void testLinearLayout() {
    def result = build {
      linearLayout(visibility: gone) {
        view(backgroundColor: 0xff00ff)
        textView()
        linearLayout() {
          for (i in 0..<5) view()
        }
      }
    }
    assertEquals("LinearLayout", result.type)
    assertEquals(View.GONE, result.visibility)
    assertEquals(3, result.children.size())
    def nestedLinearLayout = result.children[2]
    assertEquals(5, nestedLinearLayout.children.size())
  }

  @Test
  void testViewWithId() {
    def v
    def foundView
    def result = build {
      view(visibility: visible, id: 'view2')
      v = view(visibility: visible, onClickListener: {
        foundView = view2
      })
    }
    assertNotNull(v.onClickListener)
    v.onClickListener(null)
    assertNotNull(foundView)
    assertNotNull(foundView.id)
  }

  @Test
  void testDefaultProperties() {
    def builder = new FakeViewBuilder(root)
    def defaultViewProperties = builder.defaultViewProperties
    def defaultTextViewProperties = [onLongClickListener: { true }]
    defaultViewProperties[TextView] = defaultTextViewProperties

    def result = Grooview.start(builder) {
      textView()
    }
    assertEquals('TextView', result.type)
    assertEquals(defaultTextViewProperties.onLongClickListener(null), result.onLongClickListener(null))
  }

  @Test
  void testListView() {
    def data = ["Hello", "World", "!"]
    def result = build {
      listView(items: data) {
        textView(text: it)
      }
    }
    assertEquals("ListView", result.type)
    def adapter = result.adapter
    for (i in 0..<data.size()) {
      assertEquals(data[i], adapter.getView(i, null, new FakeView()).text)
    }
  }

  @Test
  void testListViewSet() {
    def data = new LinkedHashSet(["Hello", "World", "!"])
    def result = build {
      listView(items: data) {
        textView(text: it)
      }
    }
    assertEquals("ListView", result.type)
    def adapter = result.adapter
    def iterator = data.iterator()
    for (i in 0..<data.size()) {
      assertEquals(iterator.next(), adapter.getView(i, null, new FakeView()).text)
    }
  }

  @Test
  void testListViewMap() {
    def data = new LinkedHashMap()
    data['one'] = "Hello"
    data['two'] = "World"
    data['three'] = "!"
    def result = build {
      listView(items: data) {
        textView(text: it)
      }
    }
    assertEquals("ListView", result.type)
    def adapter = result.adapter
    def iterator = data.iterator()
    for (i in 0..<data.size()) {
      assertEquals(iterator.next(), adapter.getView(i, null, new FakeView()).text)
    }
  }

  @Test
  void testListViewTwoParameter() {
    def data = ["Hello", "World", "!"]
    def result = build {
      listView(items: data) { item, i ->
        textView(text: "$item $i")
      }
    }
    assertEquals("ListView", result.type)
    def adapter = result.adapter
    for (i in 0..<data.size()) {
      assertEquals("${data[i]} $i", adapter.getView(i, null, new FakeView()).text)
    }
  }

  @Test
  void testListViewThreeParameter() {
    def data = ["Hello", "World", "!"]
    def result = build {
      listView(items: data) { item, i, items ->
        textView(text: "$item $i $items")
      }
    }
    assertEquals("ListView", result.type)
    def adapter = result.adapter
    for (i in 0..<data.size()) {
      assertEquals("${data[i]} $i $data", adapter.getView(i, null, new FakeView()).text)
    }
  }

  @Test
  void testListViewWithParent() {
    def data = ["Hello", "World", "!"]
    def result = build {
      linearLayout() {
        listView(items: data) {
          textView(text: it)
        }
      }
    }
    result = result.children[0]
    assertEquals("ListView", result.type)
    def adapter = result.adapter
    for (i in 0..<data.size()) {
      assertEquals(data[i], adapter.getView(i, null, new FakeView()).text)
    }
  }

  @Test
  void testLayoutParamsProperty() {
    def result = build {
      view(width: match_parent)
    }
    assertEquals("View", result.type)
    assertEquals([width: ViewGroup.LayoutParams.MATCH_PARENT], result.layoutParams)
  }

  @Test
  void testRelativeLayoutParamsRules() {
    def result = build {
      view(id: 'view1')
      view(width: match_parent, rules: [RelativeLayout.CENTER_VERTICAL, [(RelativeLayout.ALIGN_BOTTOM): view1.id]])
    }
    assertEquals("View", result.type)
    def rules = result.layoutParams.rules

    assertEquals([RelativeLayout.CENTER_VERTICAL, [(RelativeLayout.ALIGN_BOTTOM): 0]], rules)

  }
  private def build(Closure closure) {
    return Grooview.start(new FakeViewBuilder(root), closure)
  }
}
