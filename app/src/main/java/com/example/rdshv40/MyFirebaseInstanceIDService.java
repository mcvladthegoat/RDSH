package com.example.rdshv40;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;



public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private static final String APP_DATA = "Settings";
    SharedPreferences settings;




    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //Update user's token
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        Log.d("InstanceIDService", "sendRegistToServer");

        settings = getSharedPreferences(APP_DATA, MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("tokenUpdated", token);
        edit.commit();
    }
}
