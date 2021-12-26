package com.example.helloword2021;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditTextActivity extends AppCompatActivity {
    private Button edtBtn_1;

    private EditText edtUserName;
    private EditText edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        edtBtn_1 = (Button)findViewById(R.id.edtBtn_1);
        edtBtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Log.d("debug1","edit buttn check");
                Toast.makeText(EditTextActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();


            }
        });

        edtUserName = (EditText)findViewById(R.id.et_username);
        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("editText",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
