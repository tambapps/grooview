import android.widget.FrameLayout
import android.widget.ListView


def data = ["Hello", "World", "!"]
def result = build {
    listView(items: data) {
        textView(text: it.toString())
    }
}

runAsserts {
    assertEquals(ListView, result.class)
    def adapter = result.adapter
    for (i in 0..<data.size()) {
        assertEquals(data[i], adapter.getView(i, null, new FrameLayout(context)).text.toString())
    }
}