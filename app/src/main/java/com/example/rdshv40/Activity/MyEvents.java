package com.example.rdshv40.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.rdshv40.EventModel;
import com.example.rdshv40.R;
import com.example.rdshv40.ServerApi;
import com.example.rdshv40.ServerHelp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyEvents extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "MyEvents";
    private static final String APP_DATA = "Settings";

    SharedPreferences settings;
    Context context;
    ServerApi service;
    Call<List<EventModel>> activeEvent;
    List<EventModel> eventList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMyEvents);
        setSupportActionBar(toolbar);

        settings = getSharedPreferences(APP_DATA, MODE_PRIVATE);
        context = this;


        service = new ServerHelp().getService();
        activeEvent = service.getActiveUserEvent(settings.getLong("id", 0));

        activeEvent.enqueue(new Callback<List<EventModel>>() {
            @Override
            public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
                Log.d(LOG_TAG, "loading done...");
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "allOK");
                    Log.d(LOG_TAG, response.toString());
                    Log.d(LOG_TAG, response.body().toString());
                    eventList.addAll(response.body());

                    Collections.sort(eventList, new Comparator<EventModel>() {
                        @Override
                        public int compare(EventModel o1, EventModel o2) {
                            return (o1.getEventDate().compareTo(o2.getEventDate()));
                        }
                    });

                    //TODO переделать отображение даты в ДД.ММ.ГГГГ
                    ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
                    for (EventModel e : eventList) {
                        data.add(e.getHashMap());
                    }

                    String[] from = new String[]{"eventName", "eventDate"};
                    int[] to = new int[]{R.id.eventName, R.id.eventDate};

                    SimpleAdapter adapter = new SimpleAdapter(context, data, R.layout.item_event, from, to);
                    ListView lv = (ListView) findViewById(R.id.listMyEvents);
                    lv.setAdapter(adapter);
                    Log.d(LOG_TAG, "adapter is set");

                } else {
                    Log.d(LOG_TAG, "smth fail");
                    Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<EventModel>> call, Throwable t) {
                Log.d(LOG_TAG, "fail(");
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Отсутствует соединение с сервером", Toast.LENGTH_LONG).show();
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewMyEvents);
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
        getMenuInflater().inflate(R.menu.my_events, menu);
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
            //
        }
        else if (id == R.id.myEvent) {
            //nothing
        }
        else if (id == R.id.direction) {
            intent = new Intent(context, DirectionList.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.person) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
