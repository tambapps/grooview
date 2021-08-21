package com.tambapps.android.grooviewexample;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.tambapps.android.grooviewexample.databinding.ActivityMainBinding;
import com.tambapps.android.grooviewexample.groovy.GrooidShell;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;

import org.codehaus.groovy.runtime.IOGroovyMethods;

import java.io.IOException;

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
                }
                Snackbar.make(view, result, Snackbar.LENGTH_LONG).show();
            }
        });

        binding.fabTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result;
                try {
                    result = String.valueOf(shell.evaluate(IOGroovyMethods.getText(getAssets().open("tests/test.groovy"))).result);
                } catch (Exception e) {
                    result = "Error: " + e.getMessage();
                }
                Snackbar.make(view, result, Snackbar.LENGTH_LONG).show();
            }
        });
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

}