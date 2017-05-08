package com.example.rdshv40.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rdshv40.R;
import com.example.rdshv40.ServerApi;
import com.example.rdshv40.ServerHelp;
import com.example.rdshv40.UserModel;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class Autho extends AppCompatActivity {

    private static final String LOG_TAG = "Autho";
    private static final String APP_DATA = "Settings";

    Context context;
    EditText email, password;
    Button registration, login;
    SharedPreferences settings;

    ServerApi service;
    Call <UserModel> autho;
    Call <ResponseBody> tokeno;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        service = new ServerHelp().getService();

        //TODO global: back pressed in all activity, change orientation, onStop, finish (stop service + close sharedPreferences), etc...

        //TODO: auto authorization

        settings = getSharedPreferences(APP_DATA, MODE_PRIVATE);
        settings.edit().clear().commit();

        setContentView(R.layout.activity_autho);
        registration = (Button) findViewById(R.id.registration);
        login = (Button) findViewById(R.id.enter);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);



        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Registration.class);
                startActivity(intent);
                finish();
            }
        });


        //TODO arraylist everywhere
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: check empty/correct edittext
                Log.d(LOG_TAG, "start Authorization");

                autho = service.authorization(email.getText().toString(), password.getText().toString());
                autho.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        Log.d(LOG_TAG, "Authorization done");

                        if (response.isSuccessful()) {
                            Log.d(LOG_TAG, "parse JSON...");
                            user = (UserModel) response.body();
                            if (user.getLoginResult().equals("success")) {
                                Log.d(LOG_TAG, "allOk, save!");
                                //TODO: save data into settings
                                //TODO: get Name! set in menu
                                {
                                    settings = getSharedPreferences(APP_DATA, MODE_PRIVATE);
                                    SharedPreferences.Editor edit = settings.edit();
                                    edit.putLong("id", user.getPersonId());
                                    edit.putString("role", user.getPersonRole());
                                    //for debug
                                    //edit.putString("tokenUpdated", "sdfdsfdsfsdfdsf");
                                    edit.commit();

                                    if(settings.contains("tokenUpdated")){
                                        String token = settings.getString("tokenUpdated", "0");
                                        tokeno = service.updateToken(user.getPersonId(), token);
                                        tokeno.enqueue(new Callback<ResponseBody>() {
                                            //TODO: implement result's retrieve
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {}

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {}
                                        });
                                        edit.remove("tokenUpdated");
                                    }
                                }

                                Log.d(LOG_TAG, "save done");
                                Intent intent = new Intent(context, News.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d(LOG_TAG, "smth fail");
                                Toast.makeText(getApplicationContext(), "Неверный логин или пароль", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.d(LOG_TAG, response.errorBody().toString());
                            Toast.makeText(getApplicationContext(), "Ошибка, попробуйте ещё раз", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Log.d(LOG_TAG, "fail(");
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Отсутствует соединение с сервером", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
