package com.saurabh.natveapp2;

import android.view.animation.Interpolator;

public class bounceAnim implements Interpolator {

    private double myAmplitude=1;
    private double myFrequency=10;

    bounceAnim(double amplitude, double frequency){
        myAmplitude=amplitude;
        myFrequency=frequency;

    }

    @Override
    public float getInterpolation(float time) {
       return (float)(-1*Math.pow(Math.E,-time/myAmplitude )*Math.cos( myFrequency*time )+1);

    }
}
