package com.example.topquiz.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.topquiz.R;
import com.example.topquiz.model.User;

public class MainActivity extends AppCompatActivity {
    SharedPreferences preferences ;
    SharedPreferences.Editor editor ;//

//    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//    SharedPreferences.Editor editor = pref.edit();


    //    SharedPreferences.Editor editor = preferences.edit();
    private TextView mGreetingsText ;
    private EditText mNameInput;
    private Button mPlayButton;
    private User mUser;
    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);
            preferences.edit().putInt("score",score).commit();
            preferences.edit().apply();
//            editor.putInt("score",score).apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String first_name = preferences.getString("first_name",null);
        if (preferences.getString("first_name",null)!=null){
            mGreetingsText.setText("Welcome back "+preferences.getString("first_name",null)+" !\n" +
                    "your last score was "+preferences.getInt("score",0)+", will you do better this time? :)");
            mNameInput.setText(preferences.getString("first_name",null));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGreetingsText = (TextView) findViewById(R.id.activity_main_greeting_txt);
        mNameInput = (EditText) findViewById(R.id.activity_main_name_input);
        mPlayButton = (Button) findViewById(R.id.activity_main_play_btn);
        mUser = new User();

        preferences = getPreferences(MODE_PRIVATE);
        editor= getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
        String first_name = preferences.getString("first_name", null);
        if (preferences.getString("first_name",null)!=null){
            mGreetingsText.setText("Welcome back "+preferences.getString("first_name",null)+" !\n" +
                    "your last score was "+preferences.getInt("score",0)+", will you do better this time? :)");
            mNameInput.setText(preferences.getString("first_name",null));
        }



        mPlayButton.setEnabled(false);
        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPlayButton.setEnabled(s.length()!=0);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFirstName(mNameInput.getText().toString());
                String first_name = mUser.getFirstName();
//                editor.putString("first_name",first_name).commit();
                preferences.edit().putString("first_name",first_name).commit();
//                editor.apply();
                preferences.edit().apply();
                String test = preferences.getString("first_name", null);
                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivityForResult(gameActivityIntent,GAME_ACTIVITY_REQUEST_CODE);


            }
        });

    }
}