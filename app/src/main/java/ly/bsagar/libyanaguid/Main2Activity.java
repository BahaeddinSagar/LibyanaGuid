package ly.bsagar.libyanaguid;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Locale;

public class Main2Activity extends AppCompatActivity {

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

        sharedPreferences = getSharedPreferences("guide", MODE_PRIVATE);
        boolean arabic = sharedPreferences.getBoolean("Arabic", true);
        if (!arabic) {
            arabicOrEnglish("en");
        } else {
            arabicOrEnglish("ar");
        }
        setContentView(R.layout.activity_main2);

        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {
                    //  Launch app intro
                    Intent i = new Intent(Main2Activity.this, MyIntro.class);
                    startActivity(i);
                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }

            }
        });

        // Start the thread
        t.start();


    }

    @Override
    protected void onStart() {
        super.onStart();
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adView.loadAd(adRequest);


        getPermissionToReadUserContacts();
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
            Intent intent;
            intent = new Intent(this, setting.class);
            startActivity(intent);
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


    public void services(View view) {

        Intent intent = new Intent(this, services.class);
        startActivity(intent);

    }

    public void pay4me(View view) {

        Intent intent = new Intent(this, pay4me.class);
        startActivity(intent);

    }

    public void sendbalance(View view) {
        Intent intent = new Intent(this, send.class);
        startActivity(intent);


    }

    //go to url for damaged cards
    public void damaged(View view) {
//        Intent intent = new Intent(this, MyIntro.class);
//        startActivity(intent);

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://libyana.ly/customerservices/vcard.php"));
        startActivity(browserIntent);
    }

    //dial for not libyana primary, call if libyana is primary
    public void balance(View view) {
        sharedPreferences = getSharedPreferences("guide", MODE_PRIVATE);

        boolean isDualSIM = sharedPreferences.getBoolean("isdualsim", true);
        boolean isPrimary = sharedPreferences.getBoolean("isLibyana", false);

        Intent intent;
        if (!isDualSIM) {

            intent = new Intent(Intent.ACTION_CALL);
        } else if (isPrimary) {
            intent = new Intent(Intent.ACTION_CALL);

        } else {
            intent = new Intent(Intent.ACTION_DIAL);

        }

        intent.setData(Uri.parse(String.format("tel:%s", Uri.encode("*121#"))));
        startActivity(intent);


    }

    private static final int CALL_PHONE_PERMISSIONS_REQUEST = 1;

    public void getPermissionToReadUserContacts() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.CALL_PHONE)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE_PERMISSIONS_REQUEST);
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == CALL_PHONE_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
