package com.example.brazomov;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    Fragment moveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0F9D58"));
        //actionBar.setBackgroundDrawable( colorDrawable );

        //actionBar.hide(); //hide the title bar
        setContentView(R.layout.activity_main);

        moveFragment = new MainFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer,moveFragment).commit();
    }
}