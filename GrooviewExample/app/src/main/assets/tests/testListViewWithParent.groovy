import android.widget.FrameLayout
import android.widget.ListView

def data = ["Hello", "World", "!"]
def result = build {
    linearLayout() {
        listView(items: data) {
            textView(text: it)
        }
    }
}

runAsserts {
    result = result.children[0]
    assertEquals(ListView, result.class)
    def adapter = result.adapter
    for (i in 0..<data.size()) {
        assertEquals(data[i], adapter.getView(i, null, new FrameLayout(context)).text)
    }
}