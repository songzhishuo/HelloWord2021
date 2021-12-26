package com.example.helloword2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button mBtnTextView;
    private Button mBtnHello;

    private Button mBtnEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnTextView = (Button) findViewById(R.id.Btn_1);
        mBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent mBtnIntent = new Intent(MainActivity.this,TextViewActivity.class);
                startActivity(mBtnIntent);
            }
        });

        mBtnHello = (Button) findViewById(R.id.Btn_hello);
        mBtnHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转Button页面
                Intent mBtnIntent = new Intent(MainActivity.this,ButtnActivity.class);
                startActivity(mBtnIntent);
            }
        });

        mBtnEditText = (Button)findViewById(R.id.Btn_2);
        mBtnEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转Button页面
                Intent mBtnIntent = new Intent(MainActivity.this, EditTextActivity.class);
                startActivity(mBtnIntent);
            }
        });
    }
}
