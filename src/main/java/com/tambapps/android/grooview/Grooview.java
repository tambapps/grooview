package com.tambapps.android.grooview;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.tambapps.android.grooview.builder.ViewBuilder;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import org.codehaus.groovy.runtime.InvokerHelper;

public class Grooview extends FrameLayout {

    public Grooview(Context context) {
        super(context);
    }

    public void show(@DelegatesTo(ViewBuilder.class) Closure<?> closure) {
        ViewBuilder viewBuilder = new ViewBuilder(this.getContext(), this);
        closure.setDelegate(viewBuilder);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        if (getChildCount() > 0) {
            removeViewAt(0);
        }
        InvokerHelper.invokeClosure(closure, new Object[] { this });
    }


}
