package com.example.attendance_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Status extends Fragment {
   private List<Leave_List> leave_lists ;
    private RecyclerView recyclerView;
    private LeaveList_Adapter leaveList_adapter;
    LeaveList_Adapter adapter;

    private static String URL_LOG = "https://vishalt-cloudanalogy.cs1.force.com/attdapp/services/apexrest/Attendance";


    View view;
    private String Username, Password, androidDevice_Id;

    public Status() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_status, container, false);


        fatchLeaveStatus();
        Log.i("TAG", "Status dilshad: ");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        leave_lists = new ArrayList<>();

        //=======================================================================sharedPref==================================

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("saveUser", Context.MODE_PRIVATE);

        Username = sharedPreferences.getString("Username","defvalue");
        Password = sharedPreferences.getString("Password","defvalue");
        androidDevice_Id = sharedPreferences.getString("androidDevice_Id","defvalue");

        Log.i("TAG", "Apply u: "+Username);
        Log.i("TAG", "Apply P: "+Password);
        Log.i("TAG", "Appl d: " +androidDevice_Id);

        return view;
    }

    public void fatchLeaveStatus (){
        Log.i("TAG", "Logi: in method");
        try {

            RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest sr = new StringRequest(Request.Method.POST, URL_LOG, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("TAG", "onResponse status: " + response);
                    try {
                        JSONObject result = new JSONObject(response);
                        JSONArray jsonArray = result.getJSONArray("leaveListWrapper");

                        Log.i("TAG", "leavelist: "+jsonArray);
                        for (int i =0; i< jsonArray.length(); i++){
                            JSONObject leaeList = jsonArray.getJSONObject(i);
                            Leave_List leave_list = new Leave_List();
                            leave_list.setAppliedDate(leaeList.getString("LeaveType__c").toString());
                            leave_list.setTlApproval(leaeList.getString("Approved_by_TL__c").toString());
                            leave_list.setHrApproval(leaeList.getString("Approved_by_HR__c").toString());
                            leave_lists.add(leave_list);

//                            String date = leaeList.getString("LeaveType__c");
//                            String Startdate = leaeList.getString("StartDate__c");
//                            String EndDate = leaeList.getString("EndDate__c");
//                           // String Startdate = leaeList.getString("StartDate__c");
//
//                          // tv.append(date+", "+Startdate+", "+EndDate+"\n\n");
//                            Log.i("TAG", "forloop: "+date);
                            Log.i("TAG", "TlApporval: "+leaeList.getString("Approved_by_TL__c").toString());


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new LeaveList_Adapter(leave_lists);
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
                    params.put("username",Username);
                    params.put("password", Password);
                    params.put("deviceId", "vishal"); //hr -suneel Teanlead -vishal
                    params.put("method", "getAppliedLeaves");

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
