package com.example.hc_05access;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Connect extends AppCompatActivity {
    BluetoothDevice bt;
    BluetoothAdapter btAdap;
    TextView txtdevice, txtaddr;
    BluetoothSocket mSocket;
    BluetoothCommunicate comm;
    private boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        btAdap = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = btAdap.getBondedDevices();

        txtdevice = findViewById(R.id.txtName);
        txtaddr = findViewById(R.id.txtAddress);
        Intent intent = getIntent();

        String device_name = intent.getStringExtra("Name");
        String device_address = intent.getStringExtra("Address");
        int pos = Objects.requireNonNull(intent.getExtras()).getInt("Position");

        for(BluetoothDevice b : pairedDevices){
            if(b.getName().equals(device_name))
                bt = b;
        }

        txtdevice.setText(device_name);
        txtaddr.setText(device_address);

    }

    public void onConnect(View view){
        ConnectThreadAsync task = new ConnectThreadAsync(bt, btAdap, this);
        task.execute();
        /*
        ConnectThread t = new ConnectThread(bt, this, btAdap);
        t.start();
        */
    }

    public void Communicate(View view){
        if(connected){
            comm = new BluetoothCommunicate(mSocket, this);
            comm.write("a".getBytes());
            comm.start();
        }
        else{
            Toast.makeText(this, "Bluetooth not connected", Toast.LENGTH_LONG).show();
        }
    }

    public void Disconnect(View view){
        comm.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        comm.cancel();
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ConnectThreadAsync extends AsyncTask<Void, Void, Boolean> {
        private final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        private BluetoothSocket mmSocket;
        private Context mContext;
        private BluetoothAdapter b;
        private boolean flag = false;

        ConnectThreadAsync(BluetoothDevice device, BluetoothAdapter bt, Context context){
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

        @Override
        protected Boolean doInBackground(Void... voids) {
            b.cancelDiscovery();
            try {
                mmSocket.connect();
                flag = true;
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return flag;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                connected = true;
                mSocket = mmSocket;
                Toast.makeText(mContext, "Connected", Toast.LENGTH_SHORT).show();
            }
            else {
                connected = false;
                Toast.makeText(mContext, "Not Connected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
