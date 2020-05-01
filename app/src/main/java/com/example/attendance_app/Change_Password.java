package com.example.attendance_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Change_Password extends AppCompatActivity {
    private Toolbar toolbar;
    private String Username, Password, androidDevice_Id, valoldpws, vanewpws, valcnfrmpws;
//    private String Password;
//    private String androidDevice_Id;
    EditText oldpws, newpws, cnfmpws;
    private static String URL_LOG = "https://vishalt-cloudanalogy.cs1.force.com/attdapp/services/apexrest/Attendance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);

        toolbar = findViewById(R.id.tollBar_leave);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");

        //  Button btnCanel = (Button)findViewById(R.id.btn_cancel);
        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        oldpws = (EditText) findViewById(R.id.old_pws);
        newpws = (EditText) findViewById(R.id.new_pws);
        cnfmpws = (EditText) findViewById(R.id.cnfirm_pws);

        Log.i("TAG", "pws o: "+valoldpws);
        Log.i("TAG", "pws n: "+vanewpws);
        Log.i("TAG", "pws c: "+valcnfrmpws);

//        btnCanel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Change_Password.this,Leave.class);
//                startActivity(intent);
//            }
//        });
//====================================================SharedPreferences============================================================

        SharedPreferences sharedPreferences = getSharedPreferences("saveUser", Context.MODE_PRIVATE);
        Username = sharedPreferences.getString("Username", "defvalue");
        Password = sharedPreferences.getString("Password", "defvalue");
        androidDevice_Id = sharedPreferences.getString("androidDevice_Id", "defvalue");


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldpws.getText().toString().isEmpty()) {
                    oldpws.setError("Field can not be Empty");
                } else if (newpws.getText().toString().isEmpty()) {
                    newpws.setError("pws not be null");
                } else if (cnfmpws.getText().toString().isEmpty()) {
                    newpws.setError("filed not be Empty");
               }
//                else if (!valoldpws.equals(Password)) {
//                    oldpws.setError("please Enter correct old pws");
//                } else if (!valcnfrmpws.equals(vanewpws)) {
//                    cnfmpws.setError("Password not match");
//                }
               else {
                oldpws.setError(null);
                newpws.setError(null);
                cnfmpws.setError(null);
                changePwsApi();
            }
            }
        });
    }



    private void changePwsApi() {

        valoldpws = oldpws.getText().toString();
        vanewpws = newpws.getText().toString();
        valcnfrmpws = cnfmpws.getText().toString();


        Log.i("TAG", "change: in method");
//        final String username = this.username.getText().toString().trim();
//        final String password = this.password.getText().toString().trim();
//        Log.i("TAG", "username: " + username + "Password: " + password);

        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest sr = new StringRequest(Request.Method.POST, URL_LOG, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("TAG", "onResponse Change_pws: " + response);
                    try {
                        final JSONObject result = new JSONObject(response);
                        Log.i("TAG", "change pws: "+result.getString("changePasswordResponseWrapper"));

                        String msg = result.getString("changePasswordResponseWrapper");
                        final String haserror = result.getString("hasError");
                        AlertDialog.Builder builder = new AlertDialog.Builder(Change_Password.this);
                        builder.setCancelable(true);
                        builder.setTitle("ALERT");
                        builder.setMessage(msg);
                        builder.setIcon(R.drawable.error);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                oldpws.setText(null);
                                newpws.setText(null);
                                cnfmpws.setText(null);
//                                if (haserror.equals("false")){
//                                    Intent intent = new Intent(Change_Password.this, Login.class);
//                                    startActivity(intent);
//                                }
                            }
                        });
                        builder.show();
                        

                    } catch (JSONException e) {
                        e.printStackTrace();
//                        loading.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("TAG", "onErrorResponse change_pws kk: " + error.getMessage());
//                    loading.setVisibility(View.GONE);

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", Username); //username
                    params.put("password", Password);   //password
                    params.put("deviceId", "vishal"); //hr -suneel Teanlead -vishal
                    params.put("oldPassword",valoldpws);
                    params.put("newPassword", vanewpws);
                    params.put("confirmPassword", valcnfrmpws);
                    params.put("method", "changePassword");


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
            Log.i("TAG", "change_Pws: " + e);
            e.printStackTrace();
        }

    }
}
