package com.example.attendance_app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Apply extends Fragment implements AdapterView.OnItemSelectedListener {
    View view;
    EditText date, todate, fromdate, letter;
    String textletter, Date, To, selected;
    private static String URL_LOG = "https://vishalt-cloudanalogy.cs1.force.com/attdapp/services/apexrest/Attendance";

    DatePickerDialog.OnDateSetListener setListener;
    private String Username;
    private String Password;
    private String androidDevice_Id;

    public Apply() {

        // Required empty public constructor
    }


    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_apply, container, false);

        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        date = view.findViewById(R.id.inpt_date);
        todate = view.findViewById(R.id.dateto);
        fromdate = view.findViewById(R.id.datefrom);
        Button btnApplyLeave = view.findViewById(R.id.btn_apply);
        letter = view.findViewById(R.id.text_letter);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String d = year + "-" + month + "-" + day;
                        date.setText(d);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String d = year + "-" + month + "-" + day;
                        fromdate.setText(d);
                        Date = fromdate.toString();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String d = year + "-" + month + "-" + day;
                        todate.setText(d);
                        To = todate.getText().toString();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        btnApplyLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button clicked", Toast.LENGTH_SHORT).show();
                Log.i("TAG", "onClick aplly: " + letter.getText().toString());
                textletter = letter.getText().toString();
                Date = date.getText().toString();
                if (selected.equals("Multiple Days")) {
                    To = todate.getText().toString();
                    Date = fromdate.getText().toString();
                    Log.i("TAG", "onItemSelected: if " + To);
                } else {
                    To = Date;
                    Log.i("TAG", "onItemSelected: " + Date + "> " + To);
                }
                callApi();
            }
        });
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected = parent.getSelectedItem().toString();
        if (selected.equals("Multiple Days")) {
            date.setVisibility(View.GONE);
            todate.setVisibility(View.VISIBLE);
            fromdate.setVisibility(View.VISIBLE);

        } else {
            date.setVisibility(View.VISIBLE);
            todate.setVisibility(View.GONE);
            fromdate.setVisibility(View.GONE);

            //To = Date;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void callApi() {
        Log.i("TAG", "Logi: in method");
        try {

            RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest sr = new StringRequest(Request.Method.POST, URL_LOG, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("TAG", "onResponse Apply: " + response);
                    try {
                        JSONObject result = new JSONObject(response);
                        Toast.makeText(getActivity(), result.get("errorMessage").toString(), Toast.LENGTH_SHORT).show();

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

                    Log.i("TAG", "getParams body: " + textletter);
                    Log.i("TAG", "getParams type: " + selected);
                    Log.i("TAG", "getParams Date: " + Date);
                    Log.i("TAG", "getParams to: " + To);

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username",Username);
                    params.put("password", Password);
                    params.put("deviceId", "vishal"); //hr -suneel Teanlead -vishal
                    params.put("subject", "Application For Leave");
                    params.put("applicationBody", textletter);
                    params.put("leavetype", selected);
                    params.put("toDate", To);
                    params.put("fromDate", Date);
                    params.put("method", "leave");

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
