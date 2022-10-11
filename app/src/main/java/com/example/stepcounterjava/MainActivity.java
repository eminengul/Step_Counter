package com.example.stepcounterjava;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    TextView tv_steps;
    SensorManager sensorManager;
    boolean running=false;
    private double MagnitudePrevious=0;
    private Integer stepcount=0;
    private ProgressBar progressBar;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

        }
        setContentView(R.layout.activity_main);
        tv_steps=(TextView) findViewById(R.id.tv_steps);

        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        running=true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(countSensor!=null){
            sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this,"Sensör Bulunamadı",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running=false;
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        progressBar = findViewById(R.id.progress_bar);
        if(running){
            float x_axis=event.values[0];
            float y_axis=event.values[0];
            float z_axis=event.values[0];
            double magnitude=Math.sqrt(x_axis*x_axis+y_axis*y_axis+z_axis*z_axis);
            double MagnitudeDelta=magnitude - MagnitudePrevious;
            MagnitudePrevious=magnitude;
            if(MagnitudeDelta >6){
                stepcount++;
            }
            int count;
            count=stepcount/2;
            tv_steps.setText(String.valueOf(count));
            progressBar.setProgress(count/100);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}