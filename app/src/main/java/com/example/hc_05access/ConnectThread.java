package com.example.hc_05access;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mmSocket = null;
    private Context mContext;
    private BluetoothAdapter b;
    //private final BluetoothSocket mmDevice;

    public ConnectThread(BluetoothDevice device, Context context, BluetoothAdapter bt){
        BluetoothSocket tmp = null;
        try {
            tmp = device.createInsecureRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmSocket = tmp;
        mContext = context;
        b = bt;
    }

    public void run(){
        b.cancelDiscovery();
        try {
            mmSocket.connect();
            Toast.makeText(mContext, "Connected", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            try {
                mmSocket.close();
                Toast.makeText(mContext, "Not Connected", Toast.LENGTH_LONG).show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void cancel(){
        try {
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
