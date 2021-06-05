package hakathon.y2021;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }
    public String addTimeDate(){
        Date currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        return formattedDate;
    }
}