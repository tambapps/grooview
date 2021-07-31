package com.tambapps.android.grooview;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.tambapps.android.grooview.builder.ViewBuilder;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.util.List;

public class Grooview extends FrameLayout {

    public Grooview(Context context) {
        super(context);
    }

    public void show(@DelegatesTo(ViewBuilder.class) Closure<?> closure) {
        ViewBuilder viewBuilder = new ViewBuilder(this.getContext());
        closure.setDelegate(viewBuilder);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        if (getChildCount() > 0) {
            removeViewAt(0);
        }
        InvokerHelper.invokeClosure(closure, new Object[] { this });
        List<View> views = viewBuilder.getViews();
        if (views.size() != 1) {
            throw new IllegalStateException();
        }
        addView(views.get(0));
    }


}
