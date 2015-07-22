package neka.sms.task2_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {
    private Button btnOK;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOK = (Button)findViewById(R.id.btn_OK);
        final Intent intent = new Intent(this, SMSContent.class);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Toast.makeText(getApplicationContext(),
                "SMS Receiver is started !",
                Toast.LENGTH_LONG).show();
    }
}
