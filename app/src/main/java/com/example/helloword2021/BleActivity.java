package com.example.helloword2021;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class BleActivity extends AppCompatActivity {
    private BytesHexStrTranslate bytesHexStrTranslate;              //HEX String 转换

//    private Button btn_open_ble;
    private Button btn_scan_ble;
    private Button btn_connect_ble;
    private Button btn_disconnect_ble;
    private Button btn_send_ble;
    private EditText edit_send_ble_data;
    private EditText edit_get_ble_data;

    private ProgressDialog myDialog_scan_wait;

    private ListView listView_ble_scan;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt = null;
    private BluetoothDevice btDevice = null;                /*需要连接的设备*/
    private BluetoothGattService mBluetoothGattService;
    private BluetoothGattCharacteristic mWriteCharacteristic;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private String SERVICESUUID = "00000002-0000-1000-8000-00805f9b34fb";
    private String WRITEUUID = "00002a00-0000-1000-8000-00805f9b34fb";
    private String NOTIFYUUID = "00002a00-0000-1000-8000-00805f9b34fb";

//    private String SERVICESUUID = "0000fffe-0000-1000-8000-00805f9b34fb";
//    private String WRITEUUID = "0000fff2-0000-1000-8000-00805f9b34fb";
//    private String NOTIFYUUID = "0000fff2-0000-1000-8000-00805f9b34fb";

    private List btNameList = null;
    private List btAddrList = null;
    private ArrayList<BluetoothDevice> btDevList = new ArrayList<BluetoothDevice>();
    ;

    private Boolean bleAdapterState = Boolean.FALSE;        //蓝牙扫描 flag
    private static boolean deviceConFlag = false;           //设备蓝牙连接flag

    private String DEV_NAME = "AiThinker";
    ArrayList<String> list_ble_scan;

    private int SCAN_TIME = 1000 * 3;                       //扫描时间

    int check_scan_list_id = 0;             //选中的设备的ID
    /*扫描监听相应*/
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            String btName = device.getName();
            String btAddr = device.getAddress();
            Log.i("hello", "Name " + device.getName() + " addr " + (String) device.getAddress() + " RSSI " + rssi);
            //if(!btName.isEmpty()) {
//            if (device.getName() == null) {
////                Log.d("debug", "无设备");
//                Log.d("debug", "无设备 NAME: " + btName + " ADDR: "+btAddr);
//                btDevice = null;
//                return;
//            } else {
//                Log.d("debug", "NAME: " + btName + " ADDR: "+btAddr);


            String data = "NAME: " + btName + " ADDR: " + btAddr;
            if (list_ble_scan.contains(data) == false)            //扫描结果无此项 添加到列表
            {
                list_ble_scan.add(data);                        //添加到表
                btAddrList.add(btAddr);
                btNameList.add(btName);
                btDevList.add(device);
            }
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

        //UUID搜索成功回调
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d("onCharacteristicWrite", "UUID搜索成功回调");
            if (status == BluetoothGatt.GATT_SUCCESS) {             //GATT获取成功
                gatt.requestMtu(35);           //设置MTU值
                List<BluetoothGattService> supportedGattServices = mBluetoothGatt.getServices(); //获取服务列表
                for (int i = 0; i < supportedGattServices.size(); i++) {
                    Log.i("success", "1:BluetoothGattService UUID=:" + supportedGattServices.get(i).getUuid());
                    List<BluetoothGattCharacteristic> listGattCharacteristic = supportedGattServices.get(i).getCharacteristics();      //获取特征值列表
                    for (int j = 0; j < listGattCharacteristic.size(); j++)         //特征值打印
                    {
                        Log.i("success", "2:   BluetoothGattCharacteristic UUID=:" + listGattCharacteristic.get(j).getUuid());
                    }
                }
            } else {
                Log.e("fail", "onservicesdiscovered收到: " + status);
            }

            mBluetoothGattService = mBluetoothGatt.getService(UUID.fromString(SERVICESUUID));
            assert (mBluetoothGattService != null);
            //数据写入char
            mWriteCharacteristic = mBluetoothGattService.getCharacteristic(UUID.fromString(WRITEUUID));
            assert (mWriteCharacteristic != null);
//            gatt.setCharacteristicNotification(notifyCharacteristic,true);
            //数据接收 notify char
            mNotifyCharacteristic = mBluetoothGattService.getCharacteristic(UUID.fromString(NOTIFYUUID));
            //开启 notify监听
            gatt.setCharacteristicNotification(mNotifyCharacteristic, true);
        }

        //数据接收回调
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            String value = characteristic.getValue().toString();
            byte[] value = characteristic.getValue();
            String str_tmp = new String(value);
//            Log.e("BLE ReceiveSuccess:", bytesHexStrTranslate.bytesToHexFun1(value));
            Log.e("------------<", str_tmp);


            edit_get_ble_data.setText(str_tmp);
        }

        //数据写入回调
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e("------------>", "send message successful\r\n");
            } else {
                Log.e("------------<", "send message error\r\n");
            }
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        btNameList = new ArrayList();
        btAddrList = new ArrayList();

//        btn_open_ble = (Button) findViewById(R.id.acBtn_ble_switch);
        btn_scan_ble = (Button) findViewById(R.id.acBtn_ble_scan);
        btn_connect_ble = (Button) findViewById(R.id.acBtn_ble_connect);
        btn_disconnect_ble = (Button) findViewById(R.id.acBtn_ble_disconnect);
        btn_send_ble = (Button) findViewById(R.id.acBtn_ble_send);
        edit_send_ble_data = (EditText) findViewById(R.id.acTxtView_ble_sendData);
        edit_get_ble_data = (EditText) findViewById(R.id.acTxtView_ble_getData);

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
                Log.d("debug", "get device name--------> : " + btNameList.get(i));
                Toast.makeText(BleActivity.this, "MAC : " + btAddrList.get(i), Toast.LENGTH_SHORT).show();
            }
        });

        btn_send_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = edit_send_ble_data.getText().toString();
                Toast.makeText(BleActivity.this, "btn check data: " + data, Toast.LENGTH_SHORT).show();

                Log.d("debug", "data : " + data);
//                    byte[] mybyte = bytesHexStrTranslate.StringtoBytes(data);
//                byte[] mybyte = data.getBytes();
                char[] mychararray = data.toCharArray();
                Log.d("debug", "--------------->String to byte :" + mychararray[0] + " " + mychararray[1]);

                if (deviceConFlag)           //设备已连接
                {
                    mWriteCharacteristic.setValue(data);
                    mWriteCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);          /*支持数据相应 系统自动分包*/
                    mBluetoothGatt.writeCharacteristic(mWriteCharacteristic);
//                    BluetoothGattCharacteristic.setValue
                }
            }
        });

//        btn_open_ble.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (bleAdapterState) {                      //蓝牙扫描已经开启了
//                    bleAdapterState = Boolean.FALSE;
//
//
//                } else {
//                    if (mBluetoothAdapter != null) {
//                        mBluetoothAdapter.enable();
//                    }
//
//                }
//                set_ble_permissions();
//
//            }
//        });


        btn_scan_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_ble_permissions();                  //设置权限

                myDialog_scan_wait = new ProgressDialog(BleActivity.this);
//                myDialog_scan_wait.setTitle("KingLearnJava");
                // 提示信息
//                myDialog_scan_wait.setMessage(getResources().getString(R.string.pd_message));
                myDialog_scan_wait.setMessage("正在扫描...");
                // 标题图标
//                myDialog_scan_wait.setIcon(R.drawable.ic_launcher);
//                // 第一个按钮
//                myDialog_scan_wait.setButton("第一个按钮", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                // 第二个按钮
//                myDialog_scan_wait.setButton2("第二个按钮", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
                // 进度条是否不明确
                myDialog_scan_wait.setIndeterminate(false);
//                // 是否可以按返回键取消
//                myDialog_scan_wait.setCancelable(true);
                // 显示
                myDialog_scan_wait.show();


//                //弹窗测试
//                AlertDialog.Builder builder = new AlertDialog.Builder(BleActivity.this);
//                builder.setTitle("普通对话框");
//                builder.setMessage("这是一个空白的对话框");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        System.out.println("点了确定");
//                    }
//                });
//                //调用show才能显示出来
//                builder.show();

                if (mBluetoothAdapter.isEnabled() == false) {
                    mBluetoothAdapter.enable();
                    Log.d("debug", "mBluetoothAdapter is not Enable");
                }

                Handler handler = new Handler();                      //延时超时等待3s
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("debug", "stop scan ble");
                        Toast.makeText(BleActivity.this, "扫描超时", Toast.LENGTH_SHORT).show();
                        myDialog_scan_wait.dismiss();            //关闭扫描弹窗
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BleActivity.this, android.R.layout.simple_list_item_1, list_ble_scan);

                        listView_ble_scan = (ListView) findViewById(R.id.acLstView_ble_list);
                        listView_ble_scan.setAdapter(adapter);
                    }
                }, SCAN_TIME);


                mBluetoothAdapter.startLeScan(mLeScanCallback);         //4.2
                list_ble_scan = new ArrayList<String>();                //初始化蓝牙列表

            }
        });

        /*Ble 连接按键监听*/
        btn_connect_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new Thread(new Runnable() {
                //@Override

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
            }
        });

        /*Ble 断链监听*/
        btn_disconnect_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    //权限设置
    private void set_ble_permissions() {
        /*设置权限*/
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        for (String str : permissions) {
            if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 111);
                break;
            }
        }
        Toast.makeText(BleActivity.this, "BLE 权限已开", Toast.LENGTH_SHORT).show();
    }
}

class BytesHexStrTranslate {
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String bytesToHexFun1(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for (byte b : bytes) { // 使用除与取余进行转换
            if (b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }
            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }
        return new String(buf);
    }

    public static String bytesToHexFun2(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }
        return new String(buf);
    }

    public static String bytesToHexFun3(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return buf.toString();
    }

    public static byte[] StringtoBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }
}