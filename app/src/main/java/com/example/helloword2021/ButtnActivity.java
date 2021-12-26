package com.example.helloword2021;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ButtnActivity extends AppCompatActivity {

    private Button myBtn_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttn);

        myBtn_3 = (Button)findViewById(R.id.acBtn_3);
        myBtn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ButtnActivity.this, "Btn3 is check", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
