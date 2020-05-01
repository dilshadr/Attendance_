package com.example.attendance_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText username, password;
    private String androidDevice_Id;
    private static String URL_LOG = "https://vishalt-cloudanalogy.cs1.force.com/attdapp/services/apexrest/Attendance";
    private ProgressBar loading;
    private String Username;
    private String Password;
    SharedPreferences sharedPreferences;
    ConstraintLayout LoginForm;
   static String mypref = "mypref";


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        updateAndroidSecurityProvider(this);

        username = (EditText) findViewById(R.id.inp_usrname);
        password = (EditText) findViewById(R.id.inpt_pwd);
        Button login = (Button) findViewById(R.id.btn_log);
        loading = findViewById(R.id.loading);
        LoginForm = findViewById(R.id.LoginFrom);

//---------------------------------------------------------LOGING BUTTON HANDLER------------------------------------------------
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty()) {
                    username.setError("Field can not be Empty");
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("pws not be null");
                } else {
                    username.setError(null);
                    password.setError(null);
                    updateAndroidSecurityProvider(Login.this);
                    Logi();
                }
            }
        });


        androidDevice_Id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("TAG", "DEvice id: "+androidDevice_Id);


//        ===========================================CHECK LOGIN STATUS====================================================================
        sharedPreferences = getSharedPreferences("Login_Status",MODE_PRIVATE);
        String check = sharedPreferences.getString("firstrun","true");

        if(check.equals("true")){
            LoginForm.setVisibility(View.VISIBLE);
            SharedPreferences.Editor logeditor = sharedPreferences.edit();
            logeditor.putString("firstrun","false");
            logeditor.apply();
        }else {
            Intent intent = new Intent(Login.this, Dashboard.class);
            startActivity(intent);

        }

//=========================================================SHAREDPREFANCE======================================================
         sharedPreferences = getSharedPreferences("saveUser", Context.MODE_PRIVATE);

    }

    private void Logi() {
        loading.setVisibility(View.VISIBLE);
        Log.i("TAG", "Logi: in method");
         Username = this.username.getText().toString().trim();
         Password = this.password.getText().toString().trim();
        Log.i("TAG", "username: " + username + "Password: " + password);

        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest sr = new StringRequest(Request.Method.POST, URL_LOG, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("TAG", "onResponselogin: " + response);
                    try {
                        JSONObject result = new JSONObject(response);
                        checkValidateuser(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loading.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("TAG", "onErrorResponse kk: " + error.getMessage());
                    loading.setVisibility(View.GONE);

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", Username); //username
                    params.put("password", Password);   //password
                    params.put("deviceId", "vishal"); //hr -suneel Teanlead -vishal
                    params.put("method", "login");

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

    } private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {

            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {

        }
    }




    private Boolean validateusername() {
        String val = username.getText().toString();
        String noWhitespace = "(?=\\s+$)";
        if (val.isEmpty()) {
            username.setError("username can not be Empty");
            return false;
        } else if (!val.matches(noWhitespace)) {
            username.setError("White space are not allowd");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private Boolean validatepws() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            username.setError("password  can not be Empty");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private void checkValidateuser(JSONObject result) {
        try {
            String userRole = String.valueOf(result.get("userRole"));
            String Errormsg = String.valueOf(result.get("errorMessage"));
            String hasError = String.valueOf(result.get("hasError"));
            String name = String.valueOf(result.get("name"));
            String designation = String.valueOf(result.get("designation"));
            String Email = String.valueOf(result.get("email"));
           // String username = Usernam;


            if(Errormsg.equals("null") && hasError.equals("false")){

                Intent intent = new Intent(Login.this, Dashboard.class);

                intent.putExtra("name",name);
                intent.putExtra("designation",designation);
                intent.putExtra("userRole",userRole);
                intent.putExtra("Email",Email);
                intent.putExtra("Username",Username);

//  ==============================================Sharespef save data========================================================================================

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Username",Username);
                editor.putString("name",name);
                editor.putString("Password",Password);
                editor.putString("androidDevice_Id",androidDevice_Id);
                editor.putString("designation",designation);
                editor.putString("name",name);
                editor.putString("Email",Email);
                editor.putString("userRole",userRole);

                editor.commit();
//===============================================================================================END SHAREDPREF===================================================
                startActivity(intent);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setCancelable(true);
                builder.setTitle("ALERT");
                builder.setMessage(Errormsg);
                builder.setIcon(R.drawable.error);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        loading.setVisibility(View.GONE);
                        username.setText(null);
                        password.setText(null);
                    }
                });
                builder.show();
            }
        } catch (JSONException e) {
            Log.i("TAG", "checkValidateuser e: "+e);
            e.printStackTrace();
        }
    }
}
