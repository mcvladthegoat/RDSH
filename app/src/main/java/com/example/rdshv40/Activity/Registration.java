package com.example.rdshv40.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rdshv40.R;
import com.example.rdshv40.ServerApi;
import com.example.rdshv40.ServerHelp;
import com.example.rdshv40.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends AppCompatActivity {

    private  static final String LOG_TAG = "Registration";

    Context context;
    EditText lastName, name, email, password, repeatPass;
    Spinner role;
    Button autho, registration;

    ServerApi service;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = this;
        service = new ServerHelp().getService();

        autho = (Button) findViewById(R.id.authorization);
        registration = (Button) findViewById(R.id.enter);
        lastName = (EditText) findViewById(R.id.lastName);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        repeatPass = (EditText) findViewById(R.id.repeatPassword);
        role = (Spinner) findViewById(R.id.role);

        autho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Autho.class);
                startActivity(intent);
                finish();
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: check empty/correct edittext; check pass!
                Log.d(LOG_TAG, "startRegistration");

                String roleS = "";
                if (role.getSelectedItemPosition() == 0) roleS = "student";
                else if (role.getSelectedItemPosition() == 1) roleS = "parent";
                else if (role.getSelectedItemPosition() == 2) roleS = "teacher";

                service.registration(lastName.getText().toString(), name.getText().toString(),
                        email.getText().toString(), password.getText().toString(), roleS).enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        Log.d(LOG_TAG, "Registration done");

                        if (response.isSuccessful()){
                            Log.d(LOG_TAG, "parse JSON...");
                            user = response.body();

                            if(user.getRegistResult().equals("success")) {
                                Log.d(LOG_TAG, "registration done");
                                Toast.makeText(context, "Вы успешно зарегистрировались", Toast.LENGTH_LONG).show();
                            }
                            //TODO fail result (not added)
                            else {
                                Log.d(LOG_TAG, "user is already exists");
                                Toast.makeText(context, "Пользователь с таким e-mail уже зарегистрирован", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Log.d(LOG_TAG, "smth fail");
                            Toast.makeText(context, "Ошибка, попробуйте ещё раз", Toast.LENGTH_LONG).show();
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
