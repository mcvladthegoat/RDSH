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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rdshv40.DirectionModel;
import com.example.rdshv40.EventModel;
import com.example.rdshv40.R;
import com.example.rdshv40.ServerApi;
import com.example.rdshv40.ServerHelp;
import com.example.rdshv40.UserModel;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventNoParticipant extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String LOG_TAG = "EventNoParticipant";
    private static final String APP_DATA = "Settings";

    Context context;
    ServerApi service;
    SharedPreferences settings;
    Call<EventModel> getEvent;
    Call<ResponseBody> joinTo;

    EventModel event;
    Button apply, watch;

    TextView eventNameNP, eventDateNP, contentNP, eventOrganizerNP;
    ListView lDirectionEventNP, lParticipantEventNP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Intent intent = getIntent();
        Long eventId = intent.getLongExtra("currentEventId", 0);
        service = new ServerHelp().getService();
        settings = getSharedPreferences(APP_DATA, MODE_PRIVATE);

        setContentView(R.layout.activity_event_no_participant);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEventNoPart);
        //toolbar.setTitle(event.getEventName());
        setSupportActionBar(toolbar);

        lDirectionEventNP = (ListView) findViewById(R.id.listDirectionEventNP);
        lParticipantEventNP = (ListView) findViewById(R.id.listParticipantEventNP);

        eventNameNP = (TextView) findViewById(R.id.eventNameNP);
        eventDateNP = (TextView) findViewById(R.id.eventDateNP);
        contentNP = (TextView) findViewById(R.id.contentNP);
        eventOrganizerNP = (TextView) findViewById(R.id.eventOrganizerNP);
        {
            //TODO ставить кнопки в зависимости от роли пользователя
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(170, 90);
            layoutParams.gravity = Gravity.CENTER;
            LinearLayout linerEventNP = (LinearLayout) findViewById(R.id.linerEventNP);

            apply = new Button(this);
            watch = new Button(this);
            apply.setText("Принять участие");
            watch.setText("Наблюдать");
            apply.setTextColor(getResources().getColor(R.color.rdshWhite));
            watch.setTextColor(getResources().getColor(R.color.rdshWhite));
            apply.setBackgroundColor(getResources().getColor(R.color.rdshRed));
            watch.setBackgroundColor(getResources().getColor(R.color.rdshRed));

            //and !activeLeader
            if (!settings.getString("role", "student").equals("parent")
                    && !settings.getString("role", "student").equals("teacher")) {
                //layoutParams.setMargins(100, 0, 0, 0);
                linerEventNP.addView(apply, layoutParams);
                layoutParams.setMargins(35, 0, 0, 0);
            }
            linerEventNP.addView(watch, layoutParams);

            apply.setOnClickListener(this);
            watch.setOnClickListener(this);
        }

        getEvent = service.getEventInfo(settings.getLong("id", 0), eventId);
        getEvent.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                Log.d(LOG_TAG, "info is get");

                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "parse JSON...");
                    event = response.body();

                    //set data
                    toolbar.setTitle(event.getEventName());
                    eventNameNP.setText(event.getEventName());
                    eventDateNP.setText(event.getEventDate());
                    contentNP.setText(event.getEventDescription());
                    eventOrganizerNP.setText(event.getEventOrganizer().getPersonLastName() + " " + event.getEventOrganizer().getPersonName());

                    ArrayList<String> directionList = new ArrayList<>();
                    for (DirectionModel d : event.getEventDirection()) {
                        directionList.add(d.getDirectionName());
                    }
                    ArrayAdapter<String> adapterDirect = new ArrayAdapter<String>(context, R.layout.item_string_in_event, directionList);
                    lDirectionEventNP.setAdapter(adapterDirect);

                    ArrayList<String> participantList = new ArrayList<>();
                    for (UserModel u : event.getEventParticipant()) {
                        participantList.add(u.getPersonLastName() + " " + u.getPersonName());
                    }
                    ArrayAdapter<String> adapterPart = new ArrayAdapter<String>(context, R.layout.item_string_in_event, participantList);
                    lParticipantEventNP.setAdapter(adapterPart);

                    //TODO set onClickListener
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



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewEventNoPart);
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
        getMenuInflater().inflate(R.menu.event_no_participant, menu);
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

    @Override
    public void onClick(View v) {
        //Log.d(LOG_TAG, event.getEventId().toString() + " " + settings.getLong("id", 0));
        String role;
        if (v.getId() == apply.getId()) {
            role = "participant";
        } else {
            role = "watcher";
        }
        joinTo = service.addToEvent(event.getEventId(), settings.getLong("id", 0), role);

        joinTo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String result = response.body().string();
                    Log.d(LOG_TAG, result);
                    if (result.equals("success")) {
                        Toast.makeText(getApplicationContext(), "Вызов принят!", Toast.LENGTH_LONG).show();
                        //to my events and finish()
                    } else {
                        Toast.makeText(getApplicationContext(), "Ошибка, попробуйте ещё раз", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Ошибка, попробуйте ещё раз", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(LOG_TAG, "fail(");
                t.printStackTrace();
                Toast.makeText(context, "Отсутствует соединение с сервером", Toast.LENGTH_LONG).show();
            }
        });
    }
}
