import android.app.Activity
import android.view.View
import android.view.ViewGroup

def result = build {
    view(width: match_parent)
}

runAsserts {
    assertEquals("Should be a view", View, result.class)
    assertEquals("width should be match parent", ViewGroup.LayoutParams.MATCH_PARENT, result.layoutParams.width)
}
