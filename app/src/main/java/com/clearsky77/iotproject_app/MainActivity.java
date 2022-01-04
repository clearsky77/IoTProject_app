package com.clearsky77.iotproject_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
                final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("센서 로그 정보 수신 중....");
                dialog.show();


                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { // 받은 순간
                        dialog.dismiss(); // 다이얼로그를 없애고
                        try {
                            JSONArray array = new JSONArray(response);
                            items.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                items.add(new Item(obj.getInt("tmp"),
                                        obj.getInt("hum"),
                                        obj.getString("created_at")));
                            }//_for
                            ItemAdapter adapter = new ItemAdapter(MainActivity.this);
                            ListView listView = (ListView) findViewById(R.id.listview);
                            listView.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                // node.js 서버에 요청

                StringRequest dht11 = new DHT11Sensor(sensors[i], listener);
                dht11.setShouldCache(false); // 보통 같은 주소일 경우 캐시로 가져오는데, 그렇게 하지 않게 설정.
                // 현재 우리는 늘 새로운 내용을 가져와야하기 때문

                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(dht11);
            }

            @Override // 선택 안되었을 때
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class Item {
        int temp, humidity;
        String created_at;

        Item(int temp, int humidity, String created_at) {
            this.temp = temp;
            this.humidity = humidity;
            this.created_at = created_at;
        }
    }


    ArrayList<Item> items = new ArrayList<Item>();

    class ItemAdapter extends ArrayAdapter {
        public ItemAdapter(Context context) {
            super(context, R.layout.list_sensor_item, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater =
                        (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_sensor_item, null);
            }
            TextView tempText = view.findViewById(R.id.temp);
            TextView humidityText = view.findViewById(R.id.humidity);
            TextView createdAtText = view.findViewById(R.id.created_at);
            tempText.setText("온도:" + items.get(position).temp);
            humidityText.setText("습도:" + items.get(position).humidity);
            createdAtText.setText("수집정보(날짜/시간)" + items.get(position).created_at);
            return view;
        }
    }
}