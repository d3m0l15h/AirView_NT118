package uit.com.airview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    private Button btn;
    private String Tag = "Check";
    private TextView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Tag,"Act2 onCreate");
        setContentView(R.layout.activity2);

        view = findViewById(R.id.view2);
        Intent intent = getIntent();
        view.setText(intent.getStringExtra("name").toString());

        btn = findViewById(R.id.act2_btn);
        btn.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(MainActivity2.this, MainActivity.class);
            startActivity(i);
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Tag,"Act2 onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Tag,"Act2 onResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(Tag,"Act2 onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(Tag,"Act2 onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(Tag,"Act2 onDestroy");
    }
}