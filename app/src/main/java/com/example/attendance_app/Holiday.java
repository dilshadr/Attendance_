package com.example.attendance_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Holiday extends AppCompatActivity {
    private static String URL_LOG = "https://vishalt-cloudanalogy.cs1.force.com/attdapp/services/apexrest/Attendance";
    String Username, Password, androidDevice_Id;

    Bitmap bitmap;
    TextView textView;
    String ecodedvalue;


    private List<Holiday_List> holiday_lists ;
    private RecyclerView recyclerView;
    private HolidayList_Adapter holidayList_adapter;
    HolidayList_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        holiday_lists = new ArrayList<>();

       // textView = (TextView) findViewById(R.id.textView);


        //============================================================FATCH DATA THROUGH SHAREDPREF save data in LOGIN ===============================================


        SharedPreferences sharedPreferences = getSharedPreferences("saveUser", Context.MODE_PRIVATE);

        Toast.makeText(this, sharedPreferences.getString("Username",""), Toast.LENGTH_SHORT).show();
        Username = sharedPreferences.getString("Username","defvalue");
        Password = sharedPreferences.getString("Password","defvalue");
        androidDevice_Id = sharedPreferences.getString("androidDevice_Id","defvalue");

        fatchApi_Holiday();
    }



    private void fatchApi_Holiday() {
        Log.i("TAG", "Logi: in method");
        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest sr = new StringRequest(Request.Method.POST, URL_LOG, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("TAG", "onResponselogin Leave: " + response);
                    try {
                        JSONObject result = new JSONObject(response);
                        JSONArray jsonArray =  result.getJSONArray("holidayAndAttachmentList");

//                        EditText editText = (EditText)findViewById(R.id.jsontext);
//                       editText.setText(jsonArray.getJSONObject(0).getString("holidayName").toString());
                        Toast.makeText(Holiday.this, "size "+jsonArray.length(), Toast.LENGTH_SHORT).show();
                        for (int i =0; i< jsonArray.length(); i++){
                            JSONObject HolidayList = jsonArray.getJSONObject(i);
                            ecodedvalue = HolidayList.getString("relatedAttachment");
//                            Log.i("TAG", "Dilskhad: "+HolidayList.getString("CA_Holidays Name"));
//                            Toast.makeText(Holiday.this, "l>>>"+HolidayList.getString("CA_Holidays Name"), Toast.LENGTH_SHORT).show();
                            byte [] decodedValue = Base64.decode(ecodedvalue, Base64.DEFAULT);
                            Bitmap decode = BitmapFactory.decodeByteArray(decodedValue, 0, decodedValue.length);
                            Holiday_List holiday_list = new Holiday_List();
                            holiday_list.setCoverImage(decode);
                            holiday_list.setHolidayTitle(HolidayList.getString("holidayName").toString());
                            holiday_list.setHolidateDate(HolidayList.getString("holidayDate").toString());
                            holiday_lists.add(holiday_list);


                        }

//                        byte [] decodedValue = Base64.decode(ecodedvalue, Base64.DEFAULT);
//                        Bitmap decode = BitmapFactory.decodeByteArray(decodedValue, 0, decodedValue.length);
//                        ImageView imageView = (ImageView)findViewById(R.id.imageView3);
//                        im
//                        textView.append(decodedValue.toString());

                        Log.i("TAG", "Jsonarray holi: "+jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new HolidayList_Adapter(holiday_lists);
                    recyclerView.setAdapter(adapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("TAG", "onErrorResponse kk: " + error.getMessage());

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", Username);
                    params.put("password", Password);
                    params.put("deviceId", "vishal"); //hr -suneel Teanlead -vishal
                    params.put("method", "holiday");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(sr);
        } catch (Exception e) {
            Log.i("TAG", "Logi: " + e);
            e.printStackTrace();
        }

    }
}
