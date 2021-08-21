import android.widget.FrameLayout
import android.widget.ListView

def data = new LinkedHashSet(["Hello", "World", "!"])
def result = build {
    listView(items: data) {
        textView(text: it.toString())
    }
}

runAsserts {
    assertEquals(ListView, result.class)
    def adapter = result.adapter
    def iterator = data.iterator()
    for (i in 0..<data.size()) {
        assertEquals(iterator.next().toString(), adapter.getView(i, null, new FrameLayout(context)).text)
    }
}