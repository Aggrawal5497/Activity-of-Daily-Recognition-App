package com.example.hc_05access;

import android.bluetooth.BluetoothSocket;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Handler;

public class BluetoothCommunicate extends Thread {
    private Handler handler;
    private BluetoothSocket mmSocket;
    private InputStream mmInStream = null;
    private OutputStream mmOutStream = null;
    private byte[] mmBuffer;
    private View mView;
    private TextView mText;
    private boolean run = true;

    BluetoothCommunicate(BluetoothSocket socket, View mainView){
        mmSocket = socket;
        mView = mainView;
        mText = mView.findViewById(R.id.txtShow);

        try {
            mmInStream = mmSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mmOutStream = mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        mmBuffer = new byte[9];
        int numBytes;
        String str;

        while(run) {
            while (true) {
                if(!run)
                    return;
                try {
                    numBytes = mmInStream.read(mmBuffer);
                    if (numBytes == 9) {
                        str = new String(mmBuffer, StandardCharsets.UTF_8);
                        Log.d("READ", str);

                        Thread.sleep(500);
                        //mText.setText(numBytes + "");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void write(byte[] bytes){
        try {
            mmOutStream.write(bytes);
            //mText.setText("Written");
            Log.d("WRITE", "Written a");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void cancel(){
        run = false;
        try {
            mmInStream.close();
            mmOutStream.close();
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
