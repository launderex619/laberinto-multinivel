package com.example.sulemaia.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sulemaia.R;

public class Tutorial extends AppCompatActivity {

    ViewPager viewPager;
    int images[] = {R.drawable.tut1,R.drawable.tut2,R.drawable.tut3,R.drawable.tut4,R.drawable.tut5,R.drawable.tut6,R.drawable.tut7,
                    R.drawable.tut8,R.drawable.tut9,R.drawable.tut10,R.drawable.tut11,R.drawable.tut12,R.drawable.tut13,R.drawable.tut14,
                    R.drawable.tut15,R.drawable.tut16};

    MyCustomPagerAdapter myCustomPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        viewPager = findViewById(R.id.pager);

        myCustomPagerAdapter = new MyCustomPagerAdapter(Tutorial.this, images);
        viewPager.setAdapter(myCustomPagerAdapter);

        Button btnMain = (Button) findViewById(R.id.toMain);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
