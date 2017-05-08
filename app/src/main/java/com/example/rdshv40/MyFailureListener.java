package com.example.rdshv40;

import com.google.android.gms.tasks.OnFailureListener;
import android.support.annotation.NonNull;
/**
 * Created by mcvlad on 08.05.17.
 */

class MyFailureListener implements OnFailureListener {
    @Override
    public void onFailure(@NonNull Exception exception) {
//        int errorCode = ((StorageException) exception).getErrorCode();
        String errorMessage = exception.getMessage();
        // test the errorCode and errorMessage, and handle accordingly
    }
}