package com.tambapps.android.grooviewexample;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.tambapps.android.grooview.Grooview;
import com.tambapps.android.grooview.PixelCategory;
import com.tambapps.android.grooview.ViewBuilder;
import com.tambapps.android.grooview.view.ViewDecorator;
import com.tambapps.android.grooviewexample.databinding.ActivityMainBinding;
import com.tambapps.android.grooviewexample.groovy.GrooidShell;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;

import org.codehaus.groovy.runtime.IOGroovyMethods;
import org.codehaus.groovy.runtime.ResourceGroovyMethods;

import java.io.File;

import groovy.lang.Closure;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.tambapps.android.grooviewexample.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        editText = findViewById(R.id.editText);

        final GrooidShell shell = new GrooidShell(getDir("dynclasses", 0), getClassLoader());
        shell.setVariable("context", this);
        shell.setVariable("root", new FrameLayout(this));

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result;
                try {
                    result = String.valueOf(shell.evaluate(editText.getText().toString()).result);
                } catch (Exception e) {
                    result = "Error: " + e.getMessage();
                    e.printStackTrace();
                }
                Snackbar.make(view, result, Snackbar.LENGTH_LONG).show();
            }
        });

        Grooview.setShow(new ShowClosure());
        binding.fabTest.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TestActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        File classesDir = getDir("dynclasses", 0);
        ResourceGroovyMethods.deleteDir(classesDir);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        ViewGroup grooviewContainer = findViewById(R.id.grooviewContainer);

        if (grooviewContainer.getVisibility() == View.VISIBLE) {
            // if there is more than one grooview, we dismiss only the last
            View viewToAnimate = grooviewContainer.getChildCount() > 1 ? grooviewContainer.getChildAt(grooviewContainer.getChildCount() - 1) : grooviewContainer;
            viewToAnimate.animate()
                    .scaleX(0f)
                    .scaleY(0f)
                    .alpha(0f)
                    .setDuration(200)
                    .setInterpolator(new AccelerateInterpolator())
                    .withEndAction(() -> {
                grooviewContainer.removeViewAt(grooviewContainer.getChildCount() - 1);
                if (grooviewContainer.getChildCount() == 0) {
                    grooviewContainer.setVisibility(View.GONE);
                }
            }).start();
        } else {
            super.onBackPressed();
        }
    }

    private class ShowClosure extends Closure<Object> {

        ShowClosure() {
            super(null);
        }

        public void doCall(Closure<?> closure) {
            // to refresh, just in case
            PixelCategory.context = MainActivity.this;
            ViewGroup container = findViewById(R.id.grooviewContainer);
            ViewBuilder builder = new ViewBuilder(container);
            View root = ((ViewDecorator) com.tambapps.android.grooview.Grooview.start(builder, closure)).get_View();
            // to prevent clicking to underlying (groo)view when there are multiple
            root.setClickable(true);
            runOnUiThread(() -> {
                container.setVisibility(View.VISIBLE);
                container.setScaleX(0f);
                container.setScaleY(0f);
                container.setAlpha(0f);
                container.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .setDuration(200)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            });
        }
    }
}