package com.example.helloword2021;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class CheckBoxActivity extends AppCompatActivity {

    private CheckBox ckBox_code;
    private CheckBox ckBox_cook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_box);

        ckBox_code = findViewById(R.id.ckBox_title_code);
        ckBox_cook = findViewById(R.id.ckBox_title_cook);

        ckBox_code.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(CheckBoxActivity.this, isChecked?"code check":"code not check", Toast.LENGTH_SHORT).show();
            }
        });

        ckBox_cook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(CheckBoxActivity.this, isChecked?"cook check":"cook not check", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
