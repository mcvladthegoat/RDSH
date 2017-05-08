package com.example.rdshv40.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.rdshv40.EventModel;
import com.example.rdshv40.R;
import com.example.rdshv40.ServerApi;
import com.example.rdshv40.ServerHelp;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class News extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "News";
    private static final String APP_DATA = "Settings";

    SharedPreferences settings;
    Context context;
    ServerApi service;
    Call<List<EventModel>> activeEvent;
    Call<ResponseBody> roleInEvent;
    List<EventModel> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNews);
        setSupportActionBar(toolbar);

        context = this;
        service = new ServerHelp().getService();
        eventList = new ArrayList<>();

        //TODO in all - set empty
        settings = getSharedPreferences(APP_DATA, MODE_PRIVATE);
        // TODO sort, filtr
        //вынести из onResponse в отдельный код?
        activeEvent = service.getActiveEvent();
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

                     String[] from = new String[] {"eventName", "eventDate"};
                     int[] to = new int[] {R.id.eventName, R.id.eventDate};

                     SimpleAdapter adapter = new SimpleAdapter(context, data, R.layout.item_event, from, to);
                     ListView lv = (ListView) findViewById(R.id.listNews);
                     lv.setAdapter(adapter);



                     Log.d(LOG_TAG, "adapter is set");

                     lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                         @Override
                         public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                             Log.d(LOG_TAG, "click" + String.valueOf(position));

                             roleInEvent = service.checkUserRole(settings.getLong("id", 0), eventList.get(position).getEventId());
                             roleInEvent.enqueue(new Callback<ResponseBody>() {
                                 @Override
                                 public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                     try {
                                         String role = response.body().string();
                                         Log.d(LOG_TAG, role);
                                         if (role.equals("none")) {
                                             Intent intent = new Intent(context, EventNoParticipant.class);
                                             intent.putExtra("currentEventId", eventList.get(position).getEventId());
                                             startActivity(intent);
                                             //no finish;
                                         }
                                         else {
                                             Intent intent = new Intent(context, EventParticipant.class);
                                             intent.putExtra("currentEventId", eventList.get(position).getEventId());
                                             startActivity(intent);
                                         }
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                         Toast.makeText(getApplicationContext(), "Ошибка, попробуйте ещё раз", Toast.LENGTH_LONG).show();
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<ResponseBody> call, Throwable t) {
                                     t.printStackTrace();
                                     Toast.makeText(context, "Отсутствуетс соединение с сервером",Toast.LENGTH_LONG).show();
                                 }
                             });
                             //get all info about event
                             //TODO get id user from shared preferences
                             //Log.d(LOG_TAG, eventList.get(position).getEventId().toString());
                         }
                     });

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewNews);
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
        getMenuInflater().inflate(R.menu.news, menu);

        if (settings.getString("role", "student").equals("student")
                || settings.getString("role", "student").equals("teacher")
                || settings.getString("role", "student").equals("parent")) {

            MenuItem add = menu.findItem(R.id.action_addEvent);
            add.setVisible(false);
        }
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
            //filtr
            return true;
        }
        else if (id == R.id.action_addEvent) {
            Intent intent = new Intent(context, AddEvent.class);
            startActivity(intent);
            finish();
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
            //nothing
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
        }
        else if (id == R.id.person) {
            Toast.makeText(context, "Временно недоступно",Toast.LENGTH_LONG).show();
        }
        /*
        if (id == R.id.nav_camera) {
            // Handle the camera action

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.myEvent) {
            // TODO отдельное меню, временно!
            Intent intent = new Intent(context, AddEvent.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStop() {
        /*
        //надо ли??
        if (!activeEvent.isCanceled()) activeEvent.cancel();
        if (selectedEvent != null && !selectedEvent.isCanceled()) selectedEvent.cancel();*/
        super.onStop();

    }

    @Override
    public void finish() {
        /*
        if (!activeEvent.isCanceled()) activeEvent.cancel();
        //??
        if (selectedEvent != null && !selectedEvent.isCanceled()) selectedEvent.cancel();*/
        super.finish();

    }
}
