package com.example.helloword2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button mBtnTextView;
    private Button mBtnHello;

    private Button mBtnEditText;            //EditText
    private Button mBtnRadioBtn;
    private Button mBtnCheckBoxBtn;         //CheckBox
    private Button mBtnImgView;             //ImageView
    private Button mBtnBleBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnTextView = findViewById(R.id.Btn_1);
        mBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent mBtnIntent = new Intent(MainActivity.this,TextViewActivity.class);
                startActivity(mBtnIntent);
            }
        });

        mBtnHello = findViewById(R.id.Btn_hello);
        mBtnHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转Button页面
                Intent mBtnIntent = new Intent(MainActivity.this,ButtnActivity.class);
                startActivity(mBtnIntent);
            }
        });

        mBtnEditText = findViewById(R.id.Btn_2);

        mBtnRadioBtn = findViewById(R.id.Btn_rdBtn);

        mBtnCheckBoxBtn = findViewById(R.id.Btn_cbBtn);

        mBtnImgView = findViewById(R.id.Btn_imgBtn);

        mBtnBleBtn = findViewById(R.id.Btn_ble);

        this.setListeners();


    }

    private  void setListeners(){
        OnClick onClick = new OnClick();

        mBtnEditText.setOnClickListener(onClick);
        mBtnRadioBtn.setOnClickListener(onClick);
        mBtnCheckBoxBtn.setOnClickListener(onClick);
        mBtnImgView.setOnClickListener(onClick);
        mBtnBleBtn.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{

        Intent intent = null;

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case (R.id.Btn_2):
                    intent = new Intent(MainActivity.this, EditTextActivity.class);
                    break;

                case (R.id.Btn_rdBtn):
                    //cls = RadioButtonActivity.class;
                    intent = new Intent(MainActivity.this, RadioButtonActivity.class);

                    break;
                case (R.id.Btn_cbBtn):
                    intent = new Intent(MainActivity.this, CheckBoxActivity.class);

                    break;
                case (R.id.Btn_imgBtn):
                    intent = new Intent(MainActivity.this, ImageViewActivity.class);

                case (R.id.Btn_ble):
                    intent = new Intent(MainActivity.this, BleActivity.class);

                default:
                    break;
            }

            //跳转
            startActivity(intent);
        }
    }
}
