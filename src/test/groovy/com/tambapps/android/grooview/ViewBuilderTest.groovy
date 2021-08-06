package com.tambapps.android.grooview

import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.tambapps.android.grooview.util.ClassEnhancer
import com.tambapps.android.grooview.util.FakeViewBuilder
import com.tambapps.android.grooview.util.MockedObject
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

import java.util.concurrent.atomic.AtomicInteger

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertTrue

class ViewBuilderTest {

  private static final AtomicInteger ID_GENERATOR = new AtomicInteger()

  private MockedObject root = new MockedObject().with {
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
    assertEquals(result.type, 'View')
    assertEquals(result.properties, [:])
  }

  @Test
  void testSetAttribute() {
    def result = build {
      view(visibility: visible)
    }
    assertEquals(result.type, "View")
    assertEquals(result.visibility, View.VISIBLE)
  }

  @Test
  void testSetBeanAttribute() {
    def result = build {
      view(alpha: 1.234)
    }
    assertEquals(result.type, "View")
    assertEquals(result.alpha, 1.234)
  }

  @Test
  void testSetInterfaceAttribute() {
    def result = build {
      view(onLongClickListener: { true })
    }
    assertEquals(result.type, "View")
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
    assertEquals(result.type, "LinearLayout")
    assertEquals(result.visibility, View.GONE)
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
    assertEquals(result.type, "ListView")
    def adapter = result.adapter
    for (i in 0..<data.size()) {
      assertEquals(data[i], adapter.getView(i, null, new MockedObject()).text)
    }
  }
  private def build(Closure closure) {
    return Grooview.start(new FakeViewBuilder(root), closure)
  }
}
