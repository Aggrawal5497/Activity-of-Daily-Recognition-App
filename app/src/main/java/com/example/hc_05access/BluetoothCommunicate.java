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
import java.util.Arrays;
import java.util.logging.Handler;

public class BluetoothCommunicate extends Thread {
    private Handler handler;
    private BluetoothSocket mmSocket;
    private InputStream mmInStream = null;
    private OutputStream mmOutStream = null;
    private byte[] mmBuffer;
    private boolean run = true;
    private Activity mActivity;
    private TextView disp1, disp2, disp3, disp4;
    private DataPreperation<Integer[]> dp;

    BluetoothCommunicate(BluetoothSocket socket, Context context){

        dp = new DataPreperation<Integer[]>(2);
        mmSocket = socket;
        mActivity = (Activity) context;
        disp1 = (TextView) mActivity.findViewById(R.id.txt1);
        disp2 = (TextView) mActivity.findViewById(R.id.txt2);
        disp3 = (TextView) mActivity.findViewById(R.id.txt3);
        disp4 = (TextView) mActivity.findViewById(R.id.txtMain);

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
                        final String[] val = finalStr.split(" ");
                        final Integer[] arr = new Integer[val.length];
                        int i = 0;
                        for(String s : val){
                            arr[i] = Integer.parseInt(s.trim());
                            i++;
                        }
                        dp.add(arr);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                disp1.setText(arr[0] + "");
                                disp2.setText(arr[1] + "");
                                disp3.setText(arr[2] + "");
                                if(dp.size() == 2){
                                    disp4.setText(Arrays.deepToString(get2dArray(dp)));
                                }
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

    private Integer[][] get2dArray(DataPreperation<Integer[]> val){
        Integer[][] arr = new Integer[val.size()][3];
        int i = 0;
        for(Integer[] a : val) {
            int j = 0;
            for(Integer z : a) {
                arr[i][j] = z;
                j++;
            }
            i++;
        }
        return arr;

    }
}
