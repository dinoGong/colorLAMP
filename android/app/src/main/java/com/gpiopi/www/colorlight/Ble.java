package com.gpiopi.www.colorlight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-12-14.
 */

public class Ble {
    /**
     * @param context context
     * @param uiHandler
     * @param mac 蓝牙的 mac 地址
     * @param service_uuid 服务uuid
     * @param read_uuid 读uuid
     * @param write_uuid 写uuid
     */

    private Context context;
    private Handler uiHandler;
    private String mac;//蓝牙的mac地址
    private String service_uuid;//服务的uuid
    private String read_uuid;//读取的uuid
    private String write_uuid;//写入的uuid


    private BluetoothAdapter ba=null;
    private BluetoothGatt bluetoothGatt = null;
    private BluetoothDevice device = null;
    private BluetoothGattService service=null;
    private BluetoothGattCharacteristic writeCharacteristic=null;
    private BluetoothGattCharacteristic gattCharacteristic=null;





    public Ble(Context context, Handler uiHandler,String mac, String service_uuid, String read_uuid, String write_uuid) {

        this.context=context;
        this.uiHandler=uiHandler;
        this.mac=mac;
        this.service_uuid=service_uuid;
        this.read_uuid=read_uuid;
        this.write_uuid=write_uuid;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void conn(){

        Message message = Message.obtain();
        message.obj = "正在连接中";
        message.what = 9;
        uiHandler.sendMessage(message);


        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            //finish();
        }

        ba = BluetoothAdapter.getDefaultAdapter();
        if(!ba.isEnabled()){
            ba.enable();
        }

        device = ba.getRemoteDevice(mac);

        bluetoothGatt = device.connectGatt(context, true, gattCallback);
    }




    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override  //当连接上设备或者失去连接时会回调该函数
        public void onConnectionStateChange(BluetoothGatt gatt, int status,int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) { //连接成功

                Message message = Message.obtain();
                message.obj = "已连接";
                message.what = 9;
                uiHandler.sendMessage(message);

                bluetoothGatt.discoverServices(); //连接成功后就去找出该设备中的服务 private BluetoothGatt mBluetoothGatt;
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //连接失败

                Message message = Message.obtain();
                message.obj = "连接失败";
                message.what = 9;
                uiHandler.sendMessage(message);
            }
        }
        @Override  //当设备是否找到服务时，会回调该函数
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.w(TAG, "onServicesDiscovered received: " + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {   //找到服务了
                //在这里可以对服务进行解析，寻找到你需要的服务
            } else {

            }
        }
        @Override  //当读取设备时会回调该函数
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.w(TAG, "onCharacteristicRead:" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //读取到的数据存在characteristic当中，可以通过characteristic.getValue();函数取出。然后再进行解析操作。
                //int charaProp = characteristic.getProperties();if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)表示可发出通知。  判断该Characteristic属性
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override //当向设备Descriptor中写数据时，会回调该函数
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.w(TAG, "onDescriptorWriteonDescriptorWrite = " + status + ", descriptor =" + descriptor.getUuid().toString());
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override //设备发出通知时会调用到该接口
        public void onCharacteristicChanged(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic) {
            if (characteristic.getValue() != null) {
                //System.out.println(characteristic.getStringValue(0));
            }
            Log.w(TAG, "--------onCharacteristicChanged-----");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            Log.w(TAG, "rssi = " + rssi);
        }
        @Override //当向Characteristic写数据时会回调该函数
        public void onCharacteristicWrite(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status) {
            Log.w(TAG, "--------write success----- status:" + status);
        };
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private BluetoothGattService getService(UUID uuid) {
        return bluetoothGatt.getService(uuid);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private BluetoothGattCharacteristic getCharcteristic(String serviceUUID, String characteristicUUID) {

//得到服务对象
        BluetoothGattService service = getService(UUID.fromString(serviceUUID));  //调用上面获取服务的方法

        if (service == null) {
            Log.e(TAG, "Can not find 'BluetoothGattService'");
            return null;
        }

//得到此服务结点下Characteristic对象
        final BluetoothGattCharacteristic gattCharacteristic = service.getCharacteristic(UUID.fromString(characteristicUUID));
        if (gattCharacteristic != null) {
            return gattCharacteristic;
        } else {
            Log.e(TAG, "Can not find 'BluetoothGattCharacteristic'");
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void write(byte[] data) {   //一般都是传byte

        BluetoothGattCharacteristic writeCharacteristic = getCharcteristic(service_uuid, write_uuid);  //这个UUID都是根据协议号的UUID

        Log.e(TAG, "Writing ###################");
        if (writeCharacteristic == null) {
            Log.e(TAG, "Write failed. GattCharacteristic is null.");
            return;
        }else{
            Log.e(TAG, "Writing...");
        }
        writeCharacteristic.setValue(data); //为characteristic赋值
        writeCharacteristicWrite(writeCharacteristic);

    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void writeCharacteristicWrite(BluetoothGattCharacteristic characteristic) {
        if (ba == null || bluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }
        Log.e(TAG, "BluetoothAdapter 写入数据");
        boolean isBoolean = false;
        isBoolean = bluetoothGatt.writeCharacteristic(characteristic);
        Log.e(TAG, "BluetoothAdapter_writeCharacteristic = " +isBoolean);  //如果isBoolean返回的是true则写入成功
    }

    //获取数据
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (ba == null || bluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return;
        }
        bluetoothGatt.readCharacteristic(characteristic);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (ba == null || bluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        return bluetoothGatt.setCharacteristicNotification(characteristic, enabled);
    }
}
