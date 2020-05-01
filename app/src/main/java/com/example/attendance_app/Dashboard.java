package com.example.attendance_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView pnchUsername, pncDesignation, pnchemail, navusername, nav_email;
    String userRole, name, designation, email;
    private CircleImageView profileimage;
    Uri imageURi;
    private static final int PICK_IMAGE = 1;
    SharedPreferences sharedPreferences;
    View headerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toast.makeText(this, getIntent().getStringExtra("Username"), Toast.LENGTH_SHORT).show();


//  ======================================================SHAREDPREFANCE FATCH AND SAVE DATA===================================================================================




//        ----------------------------------Hooks---------------------------------------

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        pnchUsername = (TextView) findViewById(R.id.pnch_username);
        pncDesignation =(TextView) findViewById(R.id.designation);
        pnchemail = (TextView)findViewById(R.id.text_email);
//        navusername = (TextView)findViewById(R.id.nav_name);
//        nav_email = (TextView)findViewById(R.id.nav_Email);
//        Toast.makeText(this, "check"+navusername.getText().toString(), Toast.LENGTH_SHORT).show();

        setTextinpnch();

        Log.i("TAG", "D name: "+getIntent().getStringExtra("name"));
        Log.i("TAG", "D desination: "+getIntent().getStringExtra("designation"));
        Log.i("TAG", "D role: "+getIntent().getStringExtra("userRole"));
        Log.i("TAG", "D role: "+getIntent().getStringExtra("Email"));

        //nave_username.setText(getIntent().getStringExtra("json"));
//        ------------------------------Toolbar--------------------------------------------
        setSupportActionBar(toolbar);
//        -----------------------------------Navigayion ---------------------------------------
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        -------------------------------------------------SET NAV HEADER VALUE------------------------------------------------------------------------------------------
        headerview =findViewById(R.id.di);
        headerview = navigationView.getHeaderView(0);
        navusername = (TextView)headerview.findViewById(R.id.nav_name);
        nav_email = (TextView)headerview.findViewById(R.id.nav_Email);
        navusername.setText(pnchUsername.getText().toString());
        nav_email.setText(pnchemail.getText().toString());
//      -------------------------------------------------------------------------------------END-----------------------------------------------------------------------------

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

        profileimage = (CircleImageView) findViewById(R.id.Profile_image);
        final Button punchin = (Button) findViewById(R.id.punch_in);
        final Button punchout = (Button) findViewById(R.id.punch_out);


        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/+");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Selllect Picture"), PICK_IMAGE);

            }
        });

        punchin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Punch in succefully", Toast.LENGTH_SHORT).show();
                punchin.setVisibility(View.GONE);
                punchout.setVisibility(View.VISIBLE);

            }
        });

        punchout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Punch out succefully", Toast.LENGTH_SHORT).show();
                punchin.setVisibility(View.VISIBLE);
                punchout.setVisibility(View.GONE);

            }
        });

//        -------------------------------------------CHECK ROLE OF USERS--------------------------------------------------------------------
        Menu menu = navigationView.getMenu();
        if (userRole.equals("HR")){

            menu.findItem(R.id.resetid).setVisible(true);
            menu.findItem(R.id.empleave).setVisible(true);
        }
        else if (userRole.equals("Team Lead")){
            menu.findItem(R.id.myTeam).setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.na_Leave:
                Intent intent = new Intent(Dashboard.this, Leave.class);
                startActivity(intent);
                break;
            case R.id.nav_Holiday:
                Intent intent1 = new Intent(Dashboard.this, Holiday.class);
                startActivity(intent1);
                break;
            case R.id.nav_Birthday:
                Toast.makeText(this, "Birthday", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:

                sharedPreferences = getSharedPreferences("Login_Status",MODE_PRIVATE);
                SharedPreferences.Editor logeditor = sharedPreferences.edit();
                logeditor.putString("firstrun","true");
                logeditor.apply();

                Intent intent2 = new Intent(Dashboard.this, Login.class);
                startActivity(intent2);
                break;
            case R.id.nav_changepws:
                Intent changepws = new Intent(Dashboard.this, Change_Password.class);
                startActivity(changepws);
                break;
            case R.id.cmp_Rule:
                Intent intent_cmp_Rule = new Intent(Dashboard.this, Company_Rule.class);
                startActivity(intent_cmp_Rule);
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && requestCode == RESULT_OK) {
            imageURi = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURi);
                profileimage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

//    ---------------------------------------FATCH INTENT DATA AND SET PUNCH IN HOME PAGE------------------------------------------------
    private void setTextinpnch (){
//        pncDesignation.setText(getIntent().getStringExtra("designation"));
//        pnchemail.setText(getIntent().getStringExtra("Email"));
//        pnchUsername.setText(getIntent().getStringExtra("name"));
//        userRole = getIntent().getStringExtra("userRole");
//


        SharedPreferences sharedPreferences = getSharedPreferences("saveUser", Context.MODE_PRIVATE);

        pncDesignation.setText(sharedPreferences.getString("designation","defvalue"));
        pnchemail .setText( sharedPreferences.getString("Email","defvalue"));
        pnchUsername .setText( sharedPreferences.getString("name","defvalue"));
        userRole = sharedPreferences.getString("userRole","defvalue");


    }

}
