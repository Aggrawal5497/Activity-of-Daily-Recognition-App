package com.example.hc_05access;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    ArrayList<String> devices;
    ArrayList<String> address;
    ArrayList<BluetoothDevice> btDevices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView pList = (RecyclerView) findViewById(R.id.progList);

        devices = new ArrayList<String>();
        address = new ArrayList<String>();
        btDevices = new ArrayList<BluetoothDevice>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG).show();
        }

        if(!mBluetoothAdapter.isEnabled()){
            Intent enbalebtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enbalebtIntent, 1);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice bt : pairedDevices){
                devices.add(bt.getName());
                address.add(bt.getAddress());
                btDevices.add(bt);
            }
        }

        pList.setLayoutManager(new LinearLayoutManager(this));
        pList.setAdapter(new ProgAdapter(this, devices, address, btDevices));
    }
}
