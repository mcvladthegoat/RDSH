package com.example.rdshv40.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rdshv40.DirectionModel;
import com.example.rdshv40.EventModel;
import com.example.rdshv40.R;
import com.example.rdshv40.ServerApi;
import com.example.rdshv40.ServerHelp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEvent extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "AddEvent";
    private static final String APP_DATA = "Settings";
    //TODO override finish and onStop

    Context context;
    ServerApi service;
    SharedPreferences settings;

    EditText eventNameEdit, eventDateEdit, eventContentEdit;
    CheckBox[] directionsCheckBox;
    List<DirectionModel> directionList;
    ArrayList <Long> selectDirection = new ArrayList<>();
    Button addEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAddEvent);
        setSupportActionBar(toolbar);

        context = this;
        service = new ServerHelp().getService();
        settings = getSharedPreferences(APP_DATA, MODE_PRIVATE);
        directionList = new ArrayList<>();

        addEvent = (Button) findViewById(R.id.addEvent);
        //TODO select date from calendar
        //TODO moderation menu (add, edit, delete, closed, etc)
        eventNameEdit = (EditText) findViewById(R.id.eventNameAdd);
        eventDateEdit = (EditText) findViewById(R.id.eventDateAdd);
        eventContentEdit = (EditText) findViewById(R.id.eventContentAdd);

        Call <List<DirectionModel>> getAllDirections = service.getDirections();
        getAllDirections.enqueue(new Callback<List<DirectionModel>>() {
            @Override
            public void onResponse(Call<List<DirectionModel>> call, Response<List<DirectionModel>> response) {
                Log.d(LOG_TAG, "directions is get");

                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "parse JSON...");
                    Log.d(LOG_TAG, response.toString());
                    Log.d(LOG_TAG, response.body().toString());
                    directionList.addAll(response.body());

                    //TODO set on linearlayout
                    directionsCheckBox = new CheckBox[directionList.size()];
                    LinearLayout linearCheckBox= (LinearLayout) findViewById(R.id.linearCheckbox);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //n^2, use arraylist (?)
                    for (int i = 0; i < directionList.size(); i++) {
                        directionsCheckBox[i] = new CheckBox(context);
                        directionsCheckBox[i].setText(directionList.get(i).getDirectionName());
                        //TODO color checkBox
                        directionsCheckBox[i].setTextSize(17);
                        linearCheckBox.addView(directionsCheckBox[i], params);
                    }
                }
                else {
                    Log.d(LOG_TAG, response.errorBody().toString());
                    Toast.makeText(getApplicationContext(), "Ошибка, попробуйте ещё раз", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<DirectionModel>> call, Throwable t) {
                Log.d(LOG_TAG, "fail(");
                t.printStackTrace();
                Toast.makeText(context, "Отсутствует соединение с сервером", Toast.LENGTH_LONG).show();
            }
        });


        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < directionsCheckBox.length; i++) {
                    if (directionsCheckBox[i].isChecked()) {
                        selectDirection.add(directionList.get(i).getDirectionId());
                    }
                }
                //TODO if selectDirection null
                //TODO check correct info, get userId from sharedPreferences
                Call <EventModel> add = service.addEvent(settings.getLong("id", 0), eventNameEdit.getText().toString(), eventDateEdit.getText().toString(),
                        eventContentEdit.getText().toString(), selectDirection);
                add.enqueue(new Callback<EventModel>() {
                    @Override
                    public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                        //логи
                        if (response.isSuccessful()) {
                            Log.d(LOG_TAG, "parse JSON...");
                            //what???
                            EventModel currentEvent = response.body();

                            /*
                            Intent intent = new Intent(context, EventNoParticipant.class);
                            intent.putExtra("currentEvent", (Serializable) currentEvent);
                            startActivity(intent);
                            finish();
                            */
                        }
                        else {
                            Log.d(LOG_TAG, response.errorBody().toString());
                            Toast.makeText(getApplicationContext(), "Ошибка, попробуйте ещё раз", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EventModel> call, Throwable t) {
                        Log.d(LOG_TAG, "fail(");
                        t.printStackTrace();
                        Toast.makeText(context, "Отсутствуетс соединение с сервером",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.news) {
            intent = new Intent(context, News.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.notifications) {
            Toast.makeText(context, "Временно недоступно",Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.myEvent) {
            intent = new Intent(context, MyEvents.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.direction) {
            intent = new Intent(context, DirectionList.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.person) {
            Toast.makeText(context, "Временно недоступно",Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
