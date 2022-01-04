package com.clearsky77.iotproject_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] sensors = {"dht11", "mq2"};

        // 스피너 어댑터
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, sensors);
        Spinner spinner = (Spinner) findViewById(R.id.spinView);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override // 선택 되었을 때
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final ProgressDialog dialog=new ProgressDialog(MainActivity.this);
                dialog.setMessage("센서 로그 정보 수신 중....");
                dialog.show();
            }

            @Override // 선택 안되었을 때
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}