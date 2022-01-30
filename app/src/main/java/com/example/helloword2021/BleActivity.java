package com.example.helloword2021;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

public class BleActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Button btn_open_ble;
    Button btn_scan_ble;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);


    btn_open_ble = (Button) findViewById(R.id.acBtn_ble_switch);
    btn_open_ble.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!mBluetoothAdapter.isEnabled()){
            //弹出对话框提示用户是后打开
                Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enabler,   2);
                //不做提示，直接打开，不建议用下面的方法，有的手机会有问题。
                // mBluetoothAdapter.enable();
            }
//            Toast.makeText(BleActivity.this, "Btn BLE is check", Toast.LENGTH_SHORT).show();
        }
    });


        btn_scan_ble = (Button) findViewById(R.id.acBtn_ble_scan);
        btn_scan_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBluetoothAdapter.startDiscovery();
                String TAG = "BLE";
                //获取本机蓝牙名称
                String name = mBluetoothAdapter.getName();
//获取本机蓝牙地址
                String address = mBluetoothAdapter.getAddress();
                Log.d(TAG,"bluetooth name ="+name+" address ="+address);
//获取已配对蓝牙设备
                Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
                Log.d(TAG, "bonded device size ="+devices.size());
                for(BluetoothDevice bonddevice:devices){
                    Log.d(TAG, "bonded device name ="+bonddevice.getName()+" address"+bonddevice.getAddress());
                }
//            Toast.makeText(BleActivity.this, "Btn BLE is check", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
