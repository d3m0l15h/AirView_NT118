package uit.com.airview;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private String Tag = "Check";
    private EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Tag,"Act1 onCreate");
        setContentView(R.layout.screen1);

//        name = findViewById(R.id.name);
//        btn = findViewById(R.id.act1_btn);
//        btn.setOnClickListener(view -> {
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            startActivity(intent);
//        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Tag,"Act1 onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Tag,"Act1 onResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(Tag,"Act1 onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(Tag,"Act1 onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(Tag,"Act1 onDestroy");
    }
}