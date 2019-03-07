package com.example.hc_05access;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProgAdapter extends RecyclerView.Adapter<ProgAdapter.ProgViewHolder> {

    private ArrayList<String> dataset;
    private ArrayList<String> addresses;
    private ArrayList<BluetoothDevice> devices;
    private Context mContext;
    public ProgAdapter(Context context, ArrayList<String> data, ArrayList<String> add, ArrayList<BluetoothDevice> btDevices){
        this.dataset = data;
        this.devices = btDevices;
        this.addresses = add;
        this.mContext = context;
    }
    @NonNull
    @Override
    public ProgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_layout, viewGroup, false);
        return new ProgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgViewHolder progViewHolder, final int i) {
        String title = dataset.get(i);
        String addr = addresses.get(i);
        progViewHolder.txtShow.setText(title);
        progViewHolder.txtAddress.setText(addr);
        progViewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Connect.class);
                intent.putExtra("Name", dataset.get(i));
                intent.putExtra("Address", addresses.get(i));
                intent.putExtra("Position", i);
                intent.putExtra("Device", devices.get(i));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ProgViewHolder extends RecyclerView.ViewHolder {
        TextView txtShow, txtAddress;
        LinearLayout mLayout;

        public ProgViewHolder(@NonNull View itemView) {
            super(itemView);
            txtShow = itemView.findViewById(R.id.txtShow);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            mLayout = itemView.findViewById(R.id.list_item_layout);
        }
    }
}
