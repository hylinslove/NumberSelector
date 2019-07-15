package com.chinastis.numberselector;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.horizontalselectedviewlibrary.HorizontalselectedView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.text_main);
        NumberSelector numberSelector = (NumberSelector) findViewById(R.id.hd_main);

        numberSelector.setOnNumberChangeListener(new NumberSelector.OnNumberChangeListener() {
            @Override
            public void onNumberChanged(float number) {

                textView.setText(number+" 公斤");
            }
        });


    }
}
