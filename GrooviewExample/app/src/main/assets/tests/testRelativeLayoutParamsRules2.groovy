import android.view.View
import android.widget.RelativeLayout
import com.tambapps.android.grooview.Grooview
def v
def result = Grooview.start(new RelativeLayout(context)) {
    v = view(id: 'view1')
    view(width: match_parent, centerInParent: true, alignBottom: v.id)
}

runAsserts {
    assertEquals(View, result.class)
    def rules = result.layoutParams.rules

    // rules are stored as an array
    assertEquals(RelativeLayout.TRUE, rules[RelativeLayout.CENTER_IN_PARENT])
    assertEquals(v.id, rules[RelativeLayout.ALIGN_BOTTOM])
}