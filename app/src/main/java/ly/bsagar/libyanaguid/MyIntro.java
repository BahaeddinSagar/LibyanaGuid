package ly.bsagar.libyanaguid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;

import java.util.Locale;

public class MyIntro extends AppIntro {

    Context context = this;
    SharedPreferences sharedPreferences;

    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        //addSlide(AppIntroFragment.newInstance("first" , "wow", image, background_colour));
        //addSlide(AppIntroFragment.newInstance("second" , "wow", image, background_colour));
        //addSlide(AppIntroFragment.newInstance("third", "wow", image, background_colour));
        //addSlide(AppIntroFragment.newInstance("forth", "wow", image, background_colour));

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(SampleSlide.newInstance(R.layout.activity_my_intro));
        addSlide(SampleSlide.newInstance(R.layout.my_intro1));
        addSlide(SampleSlide.newInstance(R.layout.my_intro2));
        addSlide(SampleSlide.newInstance(R.layout.my_intro3));

        // OPTIONAL METHODS
        // Override bar/separator color.
        TextView doneText = (TextView) findViewById(com.github.paolorotolo.appintro.R.id.done);
        doneText.setText(R.string.done);

        setBarColor(Color.parseColor("#9C27B0"));
        setSeparatorColor(Color.parseColor("#CE93D8"));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(false);
        //setVibrateIntensity(30);


    }

    //    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.

    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.

        sharedPreferences = getSharedPreferences("guide", MODE_PRIVATE);

        // create an alert dialog for dual sim check

        AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.choosephonetype);
        alertDialogBuilder.setTitle(R.string.DualOrSingle);
        alertDialogBuilder.setIcon(R.drawable.ic_call_black_24dp);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog.Builder alertDialogBuilder2 = new android.support.v7.app.AlertDialog.Builder(this);
        //alertDialogBuilder2.setMessage("for dual sim phones, it recommended to let the app dial the number for you, and then you can choose which sim to call from, however if you are sure that libyana is the one that the phone uses by default choose call");
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
                Toast.makeText(MyIntro.this, "This phone contains two sim cards", Toast.LENGTH_SHORT).show();
            }
        });
        //if no, then just exit
        alertDialogBuilder.setNegativeButton(R.string.singlesim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences.edit().putBoolean("isdualsim", false).apply();
                Toast.makeText(MyIntro.this, "This phone contains single sim card", Toast.LENGTH_SHORT).show();
                loadMainActivity();
            }
        });
        alertDialogBuilder2.setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                sharedPreferences.edit().putBoolean("isLibyana", true).apply();
                Toast.makeText(MyIntro.this, "Libyana is the primary Sim", Toast.LENGTH_SHORT).show();
                loadMainActivity();
            }
        });
        alertDialogBuilder2.setNegativeButton(R.string.dial, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences.edit().putBoolean("isLibyana", false).apply();
                Toast.makeText(MyIntro.this, "Libyana is not the primary Sim", Toast.LENGTH_SHORT).show();
                loadMainActivity();
            }
        });

        //show the previous dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.


    }

    public void loadMainActivity() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);

    }

    public void changelang (View view){
        //TODO: read local lang, then change the language
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
    public  void arabicOrEnglish(String lang){
        // your language
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


    }

}