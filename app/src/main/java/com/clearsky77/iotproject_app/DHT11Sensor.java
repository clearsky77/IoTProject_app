package com.clearsky77.iotproject_app;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DHT11Sensor extends StringRequest { // 스트링 데이터를 요청한다
    final static private String URL="http://172.30.1.50:3000/devices/device";
    private Map<String, String> parameters; // 요청 보낼때 요청 데이터를 담을 변수

    public DHT11Sensor(String sensor, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null); // node.js에 요청하는 부분.
        // 포스트 방식, 이 주소로, 받은 callback,

        Log.d("태그","DHT11Sensor 진입");

//        parameters = new HashMap<>();
        parameters = new HashMap<String, String>();
        parameters.put("sensor",sensor); // 여러게 put 가능
    }

    @Override
    protected Map<String, String> getParams(){
        return parameters;
    }

}
