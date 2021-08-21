import android.view.View
import android.widget.RelativeLayout

def result = build {
    view(id: 'view1')
    view(width: match_parent, rules: [RelativeLayout.CENTER_VERTICAL, [(RelativeLayout.ALIGN_BOTTOM): view1.id]])
}

runAsserts {
    assertEquals(View, result.class)
    def rules = result.layoutParams.rules

    assertEquals([RelativeLayout.CENTER_VERTICAL, [(RelativeLayout.ALIGN_BOTTOM): 0]], rules)
}