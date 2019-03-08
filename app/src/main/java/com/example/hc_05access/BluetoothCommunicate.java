package com.example.hc_05access;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
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
    private boolean run = true;
    private Activity mActivity;
    private TextView disp;

    BluetoothCommunicate(BluetoothSocket socket, Context context){
        mmSocket = socket;
        mActivity = (Activity) context;
        disp = mActivity.findViewById(R.id.txtRecv);

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
                        final String finalStr = str;
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                disp.setText(finalStr);
                            }
                        });
                        Log.d("READ", str);

                        Thread.sleep(500);
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
