package com.example.hc_05access;

import java.util.ArrayList;

public class DataPreperation<K> extends ArrayList<K> {
    private int maxSize;

    DataPreperation(int size){
        this.maxSize = size;
    }

    @Override
    public boolean add(K k){
        boolean r = super.add(k);
        if(size() > this.maxSize){
            removeRange(0, size() - maxSize);
        }
        return r;
    }

    public int getSize(){
        return size();
    }
}