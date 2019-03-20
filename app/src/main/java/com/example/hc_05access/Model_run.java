package com.example.hc_05access;

import android.app.Activity;
import android.content.res.AssetManager;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.Collections;


public class Model_run {
    private TensorFlowInferenceInterface tfHelper;
    private float[] output = new float[4];

    public Model_run(AssetManager am, String modelPath){
        tfHelper = new TensorFlowInferenceInterface(am, modelPath);
    }

    public float[] predict(float[] input){
        int lens[] = {100};
        tfHelper.feed("input_node", input, 1, 100, 3);
        tfHelper.feed("length", lens, 1);
        tfHelper.run(new String[]{"Output_node"});
        tfHelper.fetch("Output_node", output);

        return output;
    }
}
