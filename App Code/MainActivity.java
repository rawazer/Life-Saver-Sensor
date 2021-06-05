package hakathon.y2021;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;

import hakathon.y2021.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    private ActivityMainBinding binding;
    final int REQUEST_ENABLE = 0;
    final int REQUEST_DISCOVER = 1;
    Call call = new Call();

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE:
                if (resultCode == RESULT_OK) {
                    showToast("alreay on");
                } else {
                    showToast("couldnt turn");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("test");
        BluetoothManager manager = new BluetoothManager(this);
        super.onCreate(savedInstanceState);

//        CountDownTimer countDownTimer = new CountDownTimer(600000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                // For every second, do something.
//                manager.read();
//            }
//
//            public void onFinish() {
//            }
//        }.start();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button button1 = findViewById(R.id.personalData);
        Button button2= findViewById(R.id.settings);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,);
//                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Personal.class);
                startActivity(intent);
            }
        });
        Runnable r = new Runnable() {
            public void run() {
                try {
                    int state = manager.read();
                    System.out.println(state);
                    if(state == 1){
                       Intent intent = new Intent(MainActivity.this, Personal.class);
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 400);
            }
        };
        handler.postDelayed(r, 400);
//        while (true) {
//        }
    }


    public void writeCancel(OutputStream outputStream) throws IOException {
        String ok = "2";
        outputStream.write(ok.getBytes());
    }


}