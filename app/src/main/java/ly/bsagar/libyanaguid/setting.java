package ly.bsagar.libyanaguid;


import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;

import java.util.Locale;


public class setting extends AppCompatActivity {

    //intialize sharedprefrences
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

        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }if (id == R.id.action_lang) {
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

    //check the status of the phone from shared prefrences, and change the status of checkboxes accordingly
    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = getSharedPreferences("guide", MODE_PRIVATE);

        TextView dual = (TextView) findViewById(R.id.dualsim);
        TextView primary = (TextView) findViewById(R.id.primary);


        if (!sharedPreferences.getBoolean("isdualsim", false)) {
            dual.setText(R.string.thisissinglesim);
            primary.setVisibility(View.INVISIBLE);
        } else {
            dual.setText(R.string.thisisdualsim);
            primary.setVisibility(View.VISIBLE);
        }
        if ((!sharedPreferences.getBoolean("isLibyana", false))) {
            primary.setText(R.string.libyanaNotPrimary);
        } else {
            primary.setText(R.string.libyanaprimary);
        }

    }



    //go to Libyana website on click
    public void Libyana(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://libyana.ly/"));
        startActivity(browserIntent);
    }

    //share the link of the app
    public void share(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=ly.bsagar.libyanaguid");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share with your friends");
        startActivity(Intent.createChooser(intent, "Share"));


    }

    //go to the link for the app to rate
    public void rate(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=ly.bsagar.libyanaguid"));
        startActivity(browserIntent);
    }

    public void change(View view) {
        sharedPreferences = getSharedPreferences("guide", MODE_PRIVATE);

        // create an alert dialog for dual sim check

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.choosephonetype);
        alertDialogBuilder.setTitle(R.string.DualOrSingle);
        alertDialogBuilder.setIcon(R.drawable.ic_call_black_24dp);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this);
       // alertDialogBuilder2.setMessage("for dual sim phones, it recommended to let the app dial the number for you, and then you can choose which sim to call from, however if you are sure that libyana is the one that the phone uses by default choose call");
        alertDialogBuilder2.setMessage(R.string.CallOrDual);

        //alertDialogBuilder2.setMessage(R.string.islibyanaprimary);
        alertDialogBuilder2.setTitle(R.string.dialorcall);
        alertDialogBuilder2.setIcon(R.drawable.ic_call_black_24dp);
        alertDialogBuilder2.setCancelable(false);



        //if yes, then check if Libyana is the first sim
        alertDialogBuilder.setPositiveButton(R.string.dualsim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                sharedPreferences.edit().putBoolean("isdualsim", true).apply();
                AlertDialog alertDialog2 = alertDialogBuilder2.create();
                alertDialog2.show();
                Toast.makeText(setting.this, R.string.thisisdualsim, Toast.LENGTH_SHORT).show();
            }
        });
        //if no, then just exit
        alertDialogBuilder.setNegativeButton(R.string.singlesim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences.edit().putBoolean("isdualsim", false).apply();
                onStart();
                Toast.makeText(setting.this, R.string.thisissinglesim, Toast.LENGTH_SHORT).show();

            }
        });
        alertDialogBuilder2.setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                sharedPreferences.edit().putBoolean("isLibyana", true).apply();
                onStart();
           //     Toast.makeText(setting.this, R.string.libyanaprimary, Toast.LENGTH_LONG).show();

            }
        });
        alertDialogBuilder2.setNegativeButton(R.string.dial, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences.edit().putBoolean("isLibyana", false).apply();
                onStart();
           //     Toast.makeText(setting.this, R.string.libyanaNotPrimary, Toast.LENGTH_LONG).show();

            }
        });

        //show the previous dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

}





