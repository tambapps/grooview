package com.tambapps.android.grooviewexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tambapps.android.grooviewexample.groovy.GrooidShell;

import junit.framework.AssertionFailedError;

import org.codehaus.groovy.runtime.IOGroovyMethods;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TestActivity extends AppCompatActivity {

    private static final String TESTS_ROOT_PATH = "tests/";

    private RecyclerView recyclerView;
    private ExecutorService executor;
    private List<Test> tests;
    private MyAdapter adapter;
    private GrooidShell shell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        recyclerView = findViewById(R.id.recyclerView);
        executor = Executors.newSingleThreadExecutor();
        String[] testScriptPaths;
        try {
            testScriptPaths = getAssets().list(TESTS_ROOT_PATH);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error while retrieving tests", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        tests = Arrays.stream(testScriptPaths)
                .map(Test::new)
                .collect(Collectors.toList());

        adapter = new MyAdapter(tests);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        shell = new GrooidShell(getDir("dynclasses", 0), getClassLoader());

        shell.setVariable("context", this);
        shell.setVariable("root", new FrameLayout(this));
    }

    public void startTests(View v) {
        executor.submit(() -> runTests(tests));
    }

    private void runTests(List<Test> tests) {

        String initScriptText;
        try {
            initScriptText = IOGroovyMethods.getText(getAssets().open("test_functions.groovy"));
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error while initializing shell. Couldn't star tests", Toast.LENGTH_SHORT).show();
            return;
        }
        shell.evaluate(initScriptText);

        for (int i = 0; i < tests.size(); i++) {
            final int position = i;
            Test test = tests.get(i);
            String scriptText;
            try {
                scriptText = test.getScriptTest();
            } catch (IOException e) {
                runOnUiThread(() -> {
                    test.state = TestState.ERROR;
                    test.message = "Couldn't get script: " + e.getMessage();
                    adapter.notifyItemChanged(position);
                });
                continue;
            }
            runOnUiThread(() -> {
                test.state = TestState.RUNNING;
                test.message = "";
                adapter.notifyItemChanged(position);
            });
            try {
                shell.evaluate(scriptText);
                runOnUiThread(() -> {
                    test.state = TestState.PASSED;
                    test.message = "";
                    adapter.notifyItemChanged(position);
                });
            } catch (AssertionFailedError e) {
                runOnUiThread(() -> {
                    test.state = TestState.FAILED;
                    test.message = "Test failed: " + e.getMessage();
                    adapter.notifyItemChanged(position);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    test.state = TestState.ERROR;
                    test.message = "An unexpected error occured: " + e.getMessage();
                    adapter.notifyItemChanged(position);
                });
            }
        }
    }

    class Test {
        String name;
        String path;
        String message;
        TestState state = TestState.PENDING;

        public Test(String fileName) {
            this.name = fileName.replace(".groovy", "");
            this.path = TestActivity.TESTS_ROOT_PATH + fileName;
        }

        String getScriptTest() throws IOException {
            return IOGroovyMethods.getText(getAssets().open(path));
        }
    }

    enum TestState {
        PENDING(R.color.pending), RUNNING(R.color.running), FAILED(R.color.failed), ERROR(R.color.failed), PASSED(R.color.passed);
        private final int color;

        TestState(int color) {
            this.color = color;
        }
    }

    static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private final List<Test> tests;

        public MyAdapter(List<Test> tests) {
            this.tests = tests;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_layout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TestActivity.MyViewHolder holder, int position) {
            Test test = tests.get(position);
            holder.nameText.setText(test.name);
            holder.stateText.setText(test.state.name());
            holder.stateText.setTextColor(holder.itemView.getContext().getColor(test.state.color));
        }

        @Override
        public int getItemCount() {
            return tests.size();
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameText;
        private final TextView stateText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            stateText = itemView.findViewById(R.id.stateText);
        }
    }
}