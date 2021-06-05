package hakathon.y2021;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.PasswordAuthentication;
import java.util.Calendar;
import java.util.Set;

public class Personal extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        EditText DisplayDate = (EditText) findViewById(R.id.birth_date);

        DisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeybaord(v);
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog =  new DatePickerDialog(Personal.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener
                        ,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
            private static final String TAG ="" ;

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                Log.d(TAG, "onDateSet: "+ year +"/ " + month +"/" +dayOfMonth );
                String date = dayOfMonth+"/"+month+"/"+year;
                DisplayDate.setText(date);

            }


        };
        TextView firstName = (TextView) findViewById(R.id.first_name);
        TextView lastName = (TextView) findViewById(R.id.last_name);
        TextView birthDate = (TextView) findViewById(R.id.birth_date);
        TextView address = (TextView) findViewById(R.id.address);
        TextView contact_name = (TextView) findViewById(R.id.contact_name);
        TextView contact_number = (TextView) findViewById(R.id.contact_number);
        Button save = findViewById(R.id.save);
        Button edit=findViewById(R.id.edit);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstName.getText().equals("") || lastName.getText().equals("") ||
                        birthDate.getText().equals("") || address.getText().equals("") ||
                        contact_name.getText().equals("") || contact_number.getText().equals("")){
                    Toast.makeText(Personal.this,"Some field were not filled...",Toast.LENGTH_LONG).show();
                }
                else{
                    SharedPreferences sp = getSharedPreferences("key",0);
                    SharedPreferences.Editor spe=sp.edit();
                    spe.putString("first name",firstName.getText().toString());
                    spe.putString("last name",lastName.getText().toString());
                    spe.putString("birth date",birthDate.getText().toString());
                    spe.putString("address",address.getText().toString());
                    spe.putString("contact name",contact_name.getText().toString());
                    spe.putString("contact number",contact_number.getText().toString());
                    spe.apply();
                    firstName.setText(sp.getString("first Name",""));
                    lastName.setText((sp.getString("last name","")));
                    birthDate.setText(sp.getString("birth date",""));
                    address.setText(sp.getString("address",""));
                    contact_name.setText(sp.getString("contact name",""));
                    contact_number.setText(sp.getString("contact number",""));
                    firstName.setEnabled(false);
                    lastName.setEnabled(false);
                    birthDate.setEnabled(false);
                    address.setEnabled(false);
                    contact_name.setEnabled(false);
                    contact_number.setEnabled(false);

                }

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName.setEnabled(true);
                lastName.setEnabled(true);
                birthDate.setEnabled(true);
                address.setEnabled(true);
                contact_name.setEnabled(true);
                contact_number.setEnabled(true);
            }
        });




    }
}