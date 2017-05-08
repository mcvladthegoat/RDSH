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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rdshv40.EventModel;
import com.example.rdshv40.R;
import com.example.rdshv40.ServerApi;
import com.example.rdshv40.ServerHelp;
import com.example.rdshv40.TaskModel;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventParticipant extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = "EventPart";
    private static final String APP_DATA = "Settings";

    Context context;
    SharedPreferences settings;
    ServerApi service;
    Call<List<TaskModel>> activeTask;
    Call<EventModel> getEvent;

    EventModel event = new EventModel();
    List<TaskModel> taskList;

    TextView eventName, eventDate;
    ListView listViewTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(APP_DATA, MODE_PRIVATE);
        service = new ServerHelp().getService();
        context = this;
        Intent intent = getIntent();
        Long eventId = intent.getLongExtra("currentEventId", 0);

        setContentView(R.layout.activity_event_participant);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEventPart);
        //toolbar.setTitle(event.getEventName());
        setSupportActionBar(toolbar);

        eventName = (TextView) findViewById(R.id.eventNameP);
        eventDate = (TextView) findViewById(R.id.eventDateP);
        taskList = new ArrayList<>();

        getEvent = service.getEventInfo(settings.getLong("id", 0), eventId);
        getEvent.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                Log.d(LOG_TAG, "info is get...");
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "parse json...");
                    event = response.body();

                    toolbar.setTitle(event.getEventName());
                    eventName.setText(event.getEventName());
                    eventDate.setText(event.getEventDate());
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


        activeTask = service.getTaskInEvent(event.getEventId());
        activeTask.enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {
                Log.d(LOG_TAG, "loading done...");
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "allOK");
                    Log.d(LOG_TAG, response.toString());
                    Log.d(LOG_TAG, response.body().toString());
                    taskList.addAll(response.body());

                    ArrayList<String> tasksName = new ArrayList<String>();
                    for (TaskModel t : taskList) {
                        tasksName.add(t.getTaskName());
                    }

                    ArrayAdapter<String> adapterTask = new ArrayAdapter<String>(context, R.layout.item_string_in_event, tasksName);
                    listViewTask = (ListView) findViewById(R.id.listTask);
                    listViewTask.setAdapter(adapterTask);
                    Log.d(LOG_TAG, "adapter is set");


                }
                else {
                    Log.d(LOG_TAG, response.errorBody().toString());
                    Toast.makeText(getApplicationContext(), "Ошибка, попробуйте ещё раз", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<TaskModel>> call, Throwable t) {
                Log.d(LOG_TAG, "fail(");
                t.printStackTrace();
                Toast.makeText(context, "Отсутствуетс соединение с сервером",Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.event_participant, menu);
        //add task
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
            //TODO add info-menu
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
