package uit.com.airview;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance("https://airview-406715-default-rtdb.asia-southeast1.firebasedatabase.app").setPersistenceEnabled(true);
    }
}
