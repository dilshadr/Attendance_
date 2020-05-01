package com.example.attendance_app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leave extends AppCompatActivity {
    private Toolbar toolbar;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    //-----------------------------------------------FRAGMENT VARABLE DECLARTION -----------------------------------------
    private Apply apply;
    private Home home;
    private Status status;
    String Username, Password, androidDevice_Id;

    private static String URL_LOG = "https://vishalt-cloudanalogy.cs1.force.com/attdapp/services/apexrest/Attendance";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
//============================================================FATCH DATA THROUGH SHAREDPREF save data in LOGIN ===============================================


        SharedPreferences sharedPreferences = getSharedPreferences("saveUser", Context.MODE_PRIVATE);

        Username = sharedPreferences.getString("Username","defvalue");
        Password = sharedPreferences.getString("Password","defvalue");
        androidDevice_Id = sharedPreferences.getString("androidDevice_Id","defvalue");

        Log.i("TAG", "Leavepref_U: "+Username);
        Log.i("TAG", "Leavepref_P: "+Password);
        Log.i("TAG", "Leavepref_D: "+androidDevice_Id);


        toolbar = findViewById(R.id.tollBar_leave);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.calogo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager_leave);
        tabLayout = findViewById(R.id.tablout_leave);
        updateAndroidSecurityProvider(this);

//-----------------------------------------------------------OBJECT CREATE FRAGMENT-----------------------------------------
        apply = new Apply();
        home = new Home();
        status =new Status();

        tabLayout.setupWithViewPager(viewPager);
        ViewPaggerAddapter viewPaggerAddapter = new ViewPaggerAddapter(getSupportFragmentManager(), 0);




//        -------------------------------------------CALL CALSS AND ADD FRAGNMET AS WELL TAB TITLE-------------------------------------------

        viewPaggerAddapter.addFragment(home, "Home");
        viewPaggerAddapter.addFragment(apply, "Apply");
        viewPaggerAddapter.addFragment(status, "Status");
        viewPager.setAdapter(viewPaggerAddapter);

//-------------------------------------------ADD TAB ICON DYNAMICALLY--------------------------------------------


        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_create_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_info_black_24dp);

        updateAndroidSecurityProvider(Leave.this);

        Leave();

    }

//    ---------------------NEW CLASS FOR ADD CUSTOM FRAGMENT AND ADD CUSTOM TAB TITLE-----------------------------------------------

    private class ViewPaggerAddapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPaggerAddapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

    private void Leave() {
        Log.i("TAG", "Logi: in method");
        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest sr = new StringRequest(Request.Method.POST, URL_LOG, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("TAG", "onResponselogin Leave: " + response);
                    try {
                        JSONObject result = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {

            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {

        }
    }



}
