import android.view.View
import android.widget.LinearLayout

def result = build {
    linearLayout(visibility: gone) {
        view(backgroundColor: 0xff00ff)
        textView()
        linearLayout() {
            for (i in 0..<5) view()
        }
    }
}


runAsserts {
    assertEquals(LinearLayout, result.class)
    assertEquals(View.GONE, result.visibility)
    assertEquals(3, result.children.size())
    def nestedLinearLayout = result.children[2]
    assertEquals(5, nestedLinearLayout.children.size())
}