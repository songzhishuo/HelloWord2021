package com.example.helloword2021;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class BleActivity extends AppCompatActivity {


    private Button btn_open_ble;
    private Button btn_scan_ble;
    private Button btn_connect_ble;
    private Button btn_disconnect_ble;
    private ListView listView_ble_scan;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt = null;
    private BluetoothDevice btDevice = null;                /*需要连接的设备*/

    private List btNameList;
    private List btAddrList;
    private ArrayList<BluetoothDevice> btDevList = new ArrayList<BluetoothDevice>();;

    private Boolean bleAdapterState = Boolean.FALSE;        //蓝牙扫描 flag
    private static boolean deviceConFlag = false;           //设备蓝牙连接flag

    private String DEV_NAME = "AiThinker";
    ArrayList<String> list_ble_scan;

    private int SCAN_TIME = 1000 * 5;                       //扫描时间

    int check_scan_list_id = 0;             //选中的设备的ID
    /*扫描监听相应*/
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            String btName = device.getName();
            String btAddr = device.getAddress();
            Log.i("hello", "Name "+ device.getName() +
                    " addr " + (String) device.getAddress() +
                    " RSSI " + rssi);
            //if(!btName.isEmpty()) {
//            if (device.getName() == null) {
////                Log.d("debug", "无设备");
//                Log.d("debug", "无设备 NAME: " + btName + " ADDR: "+btAddr);
//                btDevice = null;
//                return;
//            } else {
//                Log.d("debug", "NAME: " + btName + " ADDR: "+btAddr);


                String data = " ADDR: "+btAddr;
                if(list_ble_scan.contains(data) == false)
                    list_ble_scan.add(data);             //扫描结果无此项 添加到列表
                    btAddrList.add( device.getAddress());
                    btDevList.add(device);
//            }

//            if (device.getName().equals(DEV_NAME.toString())) {
//                btDevice = device;
//                Log.d("debug", "找到目标");
//                mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                Toast.makeText(BleActivity.this, "搜索到目标ble", Toast.LENGTH_SHORT).show();
//                //return;         //找到匹配的设备
//            }


//            else
//            {
//                btDevice = null;
//                Log.d("debug", "未找到");
//            }
        }
    };

    // BLE回调操作
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("debug", "连接成功");
                deviceConFlag = true;
                mBluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                deviceConFlag = false;
                mBluetoothGatt.close();
                Log.d("debug", "连接失败-->" + status);
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        btNameList = new ArrayList();
        btAddrList = new ArrayList();

        btn_open_ble = (Button) findViewById(R.id.acBtn_ble_switch);
        btn_scan_ble = (Button) findViewById(R.id.acBtn_ble_scan);
        btn_connect_ble = (Button) findViewById(R.id.acBtn_ble_connect);
        btn_disconnect_ble = (Button) findViewById(R.id.acBtn_ble_disconnect);

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        listView_ble_scan = (ListView) findViewById(R.id.acLstView_ble_list);


        /*listview触发*/
        listView_ble_scan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("debug", "check : " + i);

                String addr = list_ble_scan.get(i);
                check_scan_list_id = i;
                btDevice = btDevList.get(i);
//                Log.d("debug", "--------------> : " + addr);
                Toast.makeText(BleActivity.this, "MAC : " + btAddrList.get(i), Toast.LENGTH_SHORT).show();
            }
        });

        btn_open_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //if(mBluetoothAdapter.isEnabled()) {
                if (bleAdapterState) {                      //蓝牙扫描已经开启了
                    bleAdapterState = Boolean.FALSE;

//                    if(mBluetoothAdapter != null) {
//                        mBluetoothAdapter.stopLeScan(mLeScanCallback);      //停止扫描
//                        mBluetoothAdapter.disable();
//                        mBluetoothAdapter = null;
//                    }
                } else {
                    if (mBluetoothAdapter != null) {
                        mBluetoothAdapter.enable();
                    }

                }

                /*设置权限*/
                String[] permissions = {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                };

                for (String str : permissions) {
                    if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(permissions, 111);
                        break;
                    }
                }
                Toast.makeText(BleActivity.this, "BLE 权限已开", Toast.LENGTH_SHORT).show();
            }
        });


        btn_scan_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleAdapterState = Boolean.TRUE;
                if (mBluetoothAdapter.isEnabled()) {
                    Handler handler = new Handler();                      //延时超时等待3s
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("debug", "stop scan ble");
                            Toast.makeText(BleActivity.this, "扫描超时", Toast.LENGTH_SHORT).show();
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(BleActivity.this,
                                    android.R.layout.simple_list_item_1,list_ble_scan);


                            listView_ble_scan = (ListView) findViewById(R.id.acLstView_ble_list);
                            listView_ble_scan.setAdapter(adapter);


                        }
                    }, SCAN_TIME);


                    mBluetoothAdapter.startLeScan(mLeScanCallback);         //4.2
                    list_ble_scan = new ArrayList<String>();                //初始化蓝牙列表
                } else {
                    Log.d("debug", "mBluetoothAdapter is not Enable");
                }
            }
        });

        /*Ble 连接按键监听*/
        btn_connect_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new Thread(new Runnable() {
                //@Override
                // public void run() {

                if (btDevice != null) {
                    Log.d("debug", "connect dev --->" + btDevice.getName());
                    // mBluetoothAdapter.stopLeScan(mLeScanCallback);      //停止扫描
                    mBluetoothGatt = btDevice.connectGatt(BleActivity.this, false, mGattCallback);

                    Log.d("debug", "连接到目标ble");
                    Toast.makeText(BleActivity.this, "连接到目标ble", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("debug", "没有连接到目标ble");
                    Toast.makeText(BleActivity.this, "没有连接到目标ble", Toast.LENGTH_SHORT).show();
                }
                //   }
                // }).start();
            }
        });

        /*Ble 断链监听*/
        btn_disconnect_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(deviceConFlag == true)   //设备已经连接
//                {
//                    mBluetoothGatt.disconnect();
//                }

                if (mBluetoothGatt != null) {
                    mBluetoothGatt.disconnect();
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                    deviceConFlag = false;
                    Log.d("debug", "断开连接到目标ble");
                    Toast.makeText(BleActivity.this, "断开连接到目标ble", Toast.LENGTH_SHORT).show();

                }

            }
        });



    }
}
