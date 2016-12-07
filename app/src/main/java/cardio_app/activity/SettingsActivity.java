package cardio_app.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;

import java.util.Locale;

import cardio_app.R;

public class SettingsActivity extends AppCompatActivity {

    public static boolean isPolishLanguage(AppCompatActivity activity) {
        Configuration cfg = activity.getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale("pl");
        // TODO locale cfg.locale is deprecated
        return locale.getLanguage().equals(cfg.locale.getLanguage());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RadioButton polishRadioButton = (RadioButton)findViewById(R.id.radio_polish);
        RadioButton englishRadioButton = (RadioButton)findViewById(R.id.radio_english);

        boolean isPolishSet = isPolishLanguage(this);

        if(isPolishSet) {
            englishRadioButton.setChecked(true);
        } else {
            polishRadioButton.setChecked(true);
        }

        polishRadioButton.setOnClickListener(view -> {
            changeLanguage("en");
        });

        englishRadioButton.setOnClickListener(view -> {
            changeLanguage("pl");
        });
    }

    private void changeLanguage(String qualifier) {
        Resources resources = getBaseContext().getResources();
        Locale locale = new Locale(qualifier);
        Locale.setDefault(locale);
        Configuration cfg = resources.getConfiguration();
        cfg.locale = locale;
        resources.updateConfiguration(cfg, resources.getDisplayMetrics());
        Intent refresh = new Intent(this, SettingsActivity.class);
        startActivity(refresh);
    }

    @Override
    public void onBackPressed(){
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
    }

}
