package uit.com.airview;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public class BaseActivity extends AppCompatActivity {
    protected ImageButton languageButton;
    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("Settings", MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(updateBaseContextLocale(newBase, lang));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFlagIcon();
    }

    private void updateFlagIcon() {
        if (languageButton != null) {
            // Update the icon based on the current language
            int flagIcon = getCurrentLanguage().equals("en") ? R.mipmap.vietnam : R.mipmap.english;
            languageButton.setImageResource(flagIcon);
        }
    }

    protected void setupLanguageButton(int buttonId) {
        languageButton = findViewById(buttonId);
        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentLang = getCurrentLanguage();
                String newLang = currentLang.equals("en") ? "vi" : "en";
                changeLanguage(newLang);
                // Update icon immediately
                updateFlagIcon();
                // Refresh the current activity to apply the new language
                recreate();
            }
        });
    }

    protected void changeLanguage(String lang) {
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    private Context updateBaseContextLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        return updateResourcesLocale(context, locale);
    }

    @SuppressWarnings("deprecation")
    private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    private Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    protected String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }
}
