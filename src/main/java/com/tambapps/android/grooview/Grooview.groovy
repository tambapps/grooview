package com.tambapps.android.grooview

import android.content.Context
import android.widget.FrameLayout
import com.tambapps.android.grooview.ViewBuilder
import org.codehaus.groovy.runtime.InvokerHelper

class Grooview extends FrameLayout {

    Grooview(Context context) {
        super(context)
    }

    void show(@DelegatesTo(ViewBuilder.class) Closure<?> closure) {
        ViewBuilder viewBuilder = new ViewBuilder(this.getContext(), this)
        closure.setDelegate(viewBuilder)
        closure.setResolveStrategy(Closure.DELEGATE_FIRST)
        if (getChildCount() > 0) {
            removeViewAt(0)
        }
        InvokerHelper.invokeClosure(closure, [this])
    }


}
