import android.widget.FrameLayout
import android.widget.ListView

def data = ["Hello", "World", "!"]
def result = build {
    listView(items: data) { item, i, items ->
        textView(text: "$item $i $items")
    }
}

runAsserts {
    assertEquals(ListView, result.class)
    def adapter = result.adapter
    for (i in 0..<data.size()) {
        assertEquals("${data[i]} $i $data".toString(), adapter.getView(i, null, new FrameLayout(context)).text)
    }
}