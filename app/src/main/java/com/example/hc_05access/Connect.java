package com.example.hc_05access;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public class Connect extends AppCompatActivity {
    BluetoothDevice bt;
    BluetoothAdapter btAdap;
    TextView txtdevice, txtaddr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        btAdap = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = btAdap.getBondedDevices();

        txtdevice = (TextView) findViewById(R.id.txtName);
        txtaddr = (TextView) findViewById(R.id.txtAddress);
        Intent intent = getIntent();

        String device_name = intent.getStringExtra("Name");
        String device_address = intent.getStringExtra("Address");
        int pos = intent.getExtras().getInt("Position");

        for(BluetoothDevice b : pairedDevices){
            if(b.getName().equals(device_name))
                bt = b;
        }

        Toast.makeText(this, "position" + pos, Toast.LENGTH_LONG).show();
        txtdevice.setText(device_name);
        txtaddr.setText(device_address);

    }

    public void onConnect(View view){
        ConnectThread t = new ConnectThread(bt, this, btAdap);
        t.start();
    }
}
