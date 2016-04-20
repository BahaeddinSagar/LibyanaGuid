package ly.bsagar.libyanaguid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.util.Locale;

public class send extends AppCompatActivity {

    TextView money;
    SharedPreferences sharedPreferences;
    static final int PICK_CONTACT_REQUEST = 1;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_send);
        AdView adView = (AdView) findViewById(R.id.adViewSend);
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

    public void plus(View view) {

        TextView dirham = (TextView) findViewById(R.id.money);
        String derhamstring = String.valueOf(dirham.getText());
        int derham = Integer.parseInt(derhamstring);

        if (derham < 9000) {
            derham += 1000;
        } else {
            Toast.makeText(getApplicationContext(), "can't send more than 9900 derham",
                    Toast.LENGTH_SHORT).show();
        }
        derhamstring = String.valueOf(derham);
        dirham.setText(derhamstring);


    }

    public void minus(View view) {

        TextView dirham = (TextView) findViewById(R.id.money);
        String derhamstring = String.valueOf(dirham.getText());
        int derham = Integer.parseInt(derhamstring);

        if (derham > 1000) {
            derham -= 1000;
        } else {
            Toast.makeText(getApplicationContext(), "can't send less than 1000 derham",
                    Toast.LENGTH_SHORT).show();
        }
        derhamstring = String.valueOf(derham);
        dirham.setText(derhamstring);


    }


    // when clicking the contact button, a new intent to view contacts is made
    public void choose(View view) {
        TextView phoneNumber = (TextView) findViewById(R.id.number);
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);

    }

    // other method to handle intent results
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView phoneNumber = (TextView) findViewById(R.id.number);
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);

                // place the number in the number field
                phoneNumber.setText(number);

            }
        }
    }

    // after collecting the number, this method will make sure it's a valid number
    //and call the create Balance send to connect the number and the derham
    public void send(View view) {
        TextView phoneNumber = (TextView) findViewById(R.id.number);
        String number = String.valueOf(phoneNumber.getText());
        TextView dirham = (TextView) findViewById(R.id.money);
        String derham = String.valueOf(dirham.getText());


        if (number.startsWith("+21892")) {
            String number218 = number.substring(4);
            createNumber(number218, derham);
        } else if (number.startsWith("092")) {
            String number218 = number.substring(1);
            createNumber(number218, derham);
        } else if (number.startsWith("+218 92")) {
            String number218 = number.substring(5);
            createNumber(number218, derham);
        } else if (number.startsWith("00218 92")) {
            String number218 = number.substring(5);
            createNumber(number218, derham);
        } else if (number.startsWith("0021892")) {
            String number218 = number.substring(5);
            createNumber(number218, derham);
        } else if (number.startsWith("00218 94")) {
            String number218 = number.substring(5);
            createNumber(number218, derham);
        } else if (number.startsWith("0021894")) {
            String number218 = number.substring(5);
            createNumber(number218, derham);
        } else if (number.startsWith("+21894")) {
            String number218 = number.substring(4);
            createNumber(number218, derham);
        } else if (number.startsWith("094")) {
            String number218 = number.substring(1);
            createNumber(number218, derham);
        } else if (number.startsWith("+218 94")) {
            String number218 = number.substring(5);
            createNumber(number218, derham);
        } else {
            Toast.makeText(getApplicationContext(), "only +21892xxxxxxx, 092xxxxxxx,+21894xxxxxxx or 094xxxxxxx are accepted",
                    Toast.LENGTH_LONG).show();

        }

    }

    //simple method to create the send balance number
    public void createNumber(String phnumber, String derham) {
        sendBalancee("*122*" + "218" + phnumber + "*" + derham + "#");

    }

    // method to dial the number to send balance
    public void sendBalancee(String number) {

        sharedPreferences = getSharedPreferences("guide", MODE_PRIVATE);

        boolean isDualSIM = sharedPreferences.getBoolean("isdualsim", true);

        boolean isPrimary = sharedPreferences.getBoolean("isLibyana", false);


        Intent intent;
        if (isDualSIM && isPrimary) {
            intent = new Intent(Intent.ACTION_CALL);

        } else if (!isDualSIM) {
            intent = new Intent(Intent.ACTION_CALL);

        } else {
            intent = new Intent(Intent.ACTION_DIAL);

        }
        intent.setData(Uri.parse(String.format("tel:%s", Uri.encode(number))));
        startActivity(intent);

    }


}
