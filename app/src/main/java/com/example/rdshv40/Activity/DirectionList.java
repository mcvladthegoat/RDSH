package com.example.rdshv40.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
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
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.rdshv40.DirectionModel;
import com.example.rdshv40.EventModel;
import com.example.rdshv40.R;
import com.example.rdshv40.ServerApi;
import com.example.rdshv40.ServerHelp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = "DirectionsList";

    Context context;
    ServerApi service;
    Call<List<DirectionModel>> direction;

    List<DirectionModel> allDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDirectionList);
        setSupportActionBar(toolbar);
        context = this;
        service = new ServerHelp().getService();
        allDirection = new ArrayList<>();

        direction = service.getDirections();
        direction.enqueue(new Callback<List<DirectionModel>>() {
            @Override
            public void onResponse(Call<List<DirectionModel>> call, Response<List<DirectionModel>> response) {
                Log.d(LOG_TAG, "loading done...");
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "allOK");
                    Log.d(LOG_TAG, response.toString());
                    Log.d(LOG_TAG, response.body().toString());
                    allDirection.addAll(response.body());

                    ArrayList<HashMap<String, String>> data = new ArrayList<>();
                    for (DirectionModel d : allDirection) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("name", d.getDirectionName());
                        data.add(hm);
                    }

                    String[] from = new String[] {"name"};
                    int [] to = new int[] {R.id.directionN};
                    SimpleAdapter adapterDirect = new SimpleAdapter(context, data, R.layout.item_direction, from, to);
                    ListView listDirection = (ListView) findViewById(R.id.listDirections);
                    listDirection.setAdapter(adapterDirect);
                    Log.d(LOG_TAG, "adapter is set");
                }
                else {
                    Log.d(LOG_TAG, "smth fail");
                    Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<DirectionModel>> call, Throwable t) {
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
        getMenuInflater().inflate(R.menu.direction_list, menu);
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
            //
        }
        else if (id == R.id.person) {
            Toast.makeText(context, "Временно недоступно",Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
