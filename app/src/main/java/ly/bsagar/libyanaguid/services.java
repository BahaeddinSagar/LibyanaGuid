package ly.bsagar.libyanaguid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Locale;

public class services extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    public void arabicOrEnglish(String lang) {
        // your language
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AdView adView = (AdView) findViewById(R.id.adViewServices);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adView.loadAd(adRequest);

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
            Intent intent = new Intent(this, setting.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        if (id == R.id.action_lang) {
            sharedPreferences = getSharedPreferences("guide", MODE_PRIVATE);
            boolean arabic = sharedPreferences.getBoolean("Arabic", false);
            if (arabic) {
                sharedPreferences.edit().putBoolean("Arabic", false).apply();
                arabicOrEnglish("en");
                recreate();
            } else {
                sharedPreferences.edit().putBoolean("Arabic", true).apply();
                arabicOrEnglish("ar");
                recreate();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    //several functions to handle pressing the buttons
    public void ActInternet(View view) {
        sendsms("1", 1414);
    }

    public void DisInternet(View view) {
        sendsms("0", 1414);
    }

    public void getSetting(View view) {
        sendsms("35840104", 1515);
    }

    public void actwaiting(View view) {
        call("*43#");
    }

    public void deactwaiting(View view) {
        call("#43#");
    }

    public void statuswaiting(View view) {
        call("*#43#");
    }

    public void actmissed(View view) {
        sendsms("2", 133);
    }

    public void deactmissed(View v) {
        sendsms("0", 133);
    }


    //shared method for sending SMS
    public void sendsms(String body, int number) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + number));
        sendIntent.putExtra("sms_body", body);
        startActivity(sendIntent);

    }

    //shared method to handle the call, if Libyana not primary dial , else call
    public void call(String number) {

        SharedPreferences sharedPreferences;

        sharedPreferences = getSharedPreferences("guide", MODE_PRIVATE);
        boolean isDualSIM = sharedPreferences.getBoolean("isdualsim", true);
        boolean isPrimary = sharedPreferences.getBoolean("isLibyana", false);


        Intent intent;
        if (isDualSIM && isPrimary) {
            intent = new Intent(Intent.ACTION_CALL);
            //Toast.makeText(balance.this, "dualsim and primary", Toast.LENGTH_LONG).show();
        } else if (!isDualSIM) {
            intent = new Intent(Intent.ACTION_CALL);
            //Toast.makeText(balance.this, "not dual sim", Toast.LENGTH_LONG).show();
        } else {
            intent = new Intent(Intent.ACTION_DIAL);
            //  Toast.makeText(balance.this, "dualsim not primary", Toast.LENGTH_LONG).show();
        }
        intent.setData(Uri.parse(String.format("tel:%s", Uri.encode(number))));
        startActivity(intent);

    }

}
