package neka.sms.task2_final;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SMSContent extends Activity {
    Button btn_close;
    TextView number;
    TextView content;
    String sms = "";
    String phone = "";
    String email = "";
    String address = "";
    Bundle data;
    final Context context = this;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smscontent);
        btn_close = (Button)findViewById(R.id.btn_close);
        number = (TextView)findViewById(R.id.textview_number);
        content = (TextView)findViewById(R.id.sms_text);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        data = getIntent().getExtras();
        displaySMS();
        //Display dialog contact
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Contact Detail");
                builder.setMessage(getContactDisplayNameByNumber(phone));
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smscontent, menu);
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
        return super.onOptionsItemSelected(item);
    }

    public String getContactDisplayNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String info = "There are no info about this phone number !";
        ContentResolver contentResolver = getContentResolver();
        Cursor phoneName = contentResolver.query(uri, null, null, null, null);
        if (phoneName.getCount() > 0) {
            while (phoneName.moveToNext()) {
                String id = phoneName.getString(phoneName
                        .getColumnIndex(ContactsContract.Contacts._ID));
                info = phoneName.getString(phoneName
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                // Get Email of id contact
                Cursor emailCur = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                + " = ?", new String[]{id}, null);
                while (emailCur.moveToNext()) {
                    email = emailCur
                            .getString(emailCur
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                // Get Address of id contact
                Cursor addrCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                        new String[]{ ContactsContract.CommonDataKinds.StructuredPostal.DATA},
                        ContactsContract.Data.CONTACT_ID + "=? AND " + ContactsContract.CommonDataKinds.StructuredPostal.MIMETYPE + "=?",
                        new String[]{String.valueOf(id), ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE},
                        null);
                while (addrCursor.moveToNext()){
                    address = addrCursor.getString(addrCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA));
                }
                info += "\nEmail: " + email + "\nAddress: " + address;
                emailCur.close();
                addrCursor.close();
            }
        }
        return info;
    }

    private void displaySMS(){
        sms = data.getString("smsMessage");
        phone = data.getString("phoneNumber");
        number.setText(phone);
        content.setText(sms);
    }
}
