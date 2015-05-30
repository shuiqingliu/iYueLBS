package com.iyuelbs.ui.settings;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by qingliu on 5/31/15.
 */
public class MyOrientationListener implements SensorEventListener{

    private Context mContext;
    private SensorManager mSensorManager; //传感器管理器
    private Sensor mSensor; //传感器
    private float lastDegree;//上一次角度
    private OnOrientationListener onOrientationListener;

    public MyOrientationListener(Context context){
        this.mContext = context;
    }
    public void start() {
        mSensorManager = (SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }

        if (mSensor != null) {
            mSensorManager.registerListener(this, mSensor
                    , SensorManager.SENSOR_DELAY_UI);
        }

    }

    public void stop(){
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            float degree = event.values[SensorManager.DATA_X];
           /* if(Math.abs(degree - lastDegree)>1.0){
            }*/
            onOrientationListener.onOrientationChanged(degree);
            lastDegree = degree;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setOnOrientationListener(OnOrientationListener onOrientationListener)
    {
        this.onOrientationListener = onOrientationListener ;
    }


    public interface OnOrientationListener
    {
        void onOrientationChanged(float x);
    }
}
