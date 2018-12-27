package com.innovations.hosa.rickhansen.hosa_app;

import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.innovations.hosa.rickhansen.hosa_app.R;

public class UserProfile extends Activity {

    private static final String TAG = "UserProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "In pause");
    }

    public void onStop(){
        super.onStop();
        Log.d(TAG, "In Stop");
    }

}
