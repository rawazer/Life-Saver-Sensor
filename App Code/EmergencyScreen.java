package hakathon.y2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

public class EmergencyScreen extends AppCompatActivity {
    Handler handler = new Handler();
    BluetoothManager manager = new BluetoothManager(this);
    ImageView btn1=findViewById(R.id.CancelBtn);
//    Times times=new Times();
    Call call=new Call();
//    EditText editText = findViewById(R.id.cancelTime);
//    Editable before=editText.getText();
//    private final long TIME_FOR_CANCEL=Long.parseLong(before.toString());
//    private long milisLeft=TIME_FOR_CANCEL;
    private CountDownTimer mCountDownTimer;
    TextView countdown=findViewById(R.id.Timer);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_screen);
//        startTimer();
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO WRITE
//                mCountDownTimer.cancel();
//                milisLeft=TIME_FOR_CANCEL;
//                Intent intent=new Intent(EmergencyScreen.this, MainActivity.class);
//                intent.putExtra("cancellation",2);
//                startActivityForResult(intent,1);
//
//            }
//        });
        Runnable r = new Runnable() {
            public void run() {
                try {
                    int state = manager.read();
                    if(state == 0){
                        Intent intent = new Intent(EmergencyScreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 400);
            }
        };
        handler.postDelayed(r, 400);
    }
//    private void startTimer(){
//        mCountDownTimer=new CountDownTimer(milisLeft,1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                milisLeft=millisUntilFinished;
//                updateCountdownText();
//
//            }
//
//            @Override
//            public void onFinish() {
//                call.sendSMS();
//
//
//            }
//        };
//    }
//    private void updateCountdownText(){
//        int minutes=(int) (milisLeft/1000)/60;
//        int seconds=(int) (milisLeft/1000)%60;
//        String timeLeftFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
//        countdown.setText(timeLeftFormatted);
//
//    }

}