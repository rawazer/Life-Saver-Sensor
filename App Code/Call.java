package hakathon.y2021;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Call extends AppCompatActivity {
    private static final String MSG="נפילה!!!";


    public Call(){}
    public void sendSMS(){;
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("0546699476"));
        smsIntent.putExtra("sms_body",MSG);
        startActivity(smsIntent);

    }
}