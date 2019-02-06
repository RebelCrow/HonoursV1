package com.example.honoursv1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView Acceloutx, Accelouty, AcceloutZ , Gyrooutx, Gyroouty, Gyrooutz, Stepout; //variables for our text view boxes
    private Button butCalibrate;
    private Sensor Accelerometer, Gyroscope, StepDetector;
    private SensorManager sensorManager;
    private int StepCount = 0;

    public boolean testflag = false;
    public File AccelFile = new File (Environment.getExternalStorageDirectory().getAbsolutePath(),"AccelData " +".txt");
    public File GyroFile = new File (Environment.getExternalStorageDirectory().getAbsolutePath(),"GyroData "+ ".txt");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create the sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Accelerometer
        Accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, Accelerometer,sensorManager.SENSOR_DELAY_NORMAL);
        //Gyroscope
        Gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(MainActivity.this, Gyroscope,sensorManager.SENSOR_DELAY_NORMAL);
        //pedometer
        StepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR); //while this will not keep the count of steps it will ping events when a step is taken
        sensorManager.registerListener(MainActivity.this, StepDetector,sensorManager.SENSOR_DELAY_NORMAL);
        Acceloutx = findViewById(R.id.TxtViewAccelOutX);
        Accelouty = findViewById(R.id.TxtViewAccelOutY);    //accelerometer textview variables
        AcceloutZ = findViewById(R.id.TxtViewAceelOutZ);

        Gyrooutx = findViewById(R.id.TxtViewGyroOutX);
        Gyroouty = findViewById(R.id.TxtViewGyroOutY);      //gyroscope textview variables
        Gyrooutz = findViewById(R.id.TxtViewGyroOutZ);

        Stepout = findViewById(R.id.TxtViewPedoOut);    //pedometer textview variable

        butCalibrate = (Button) findViewById(R.id.ButCali);

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000); //request for write file permissions


        butCalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (testflag = false)
                {
                    testflag = true;
                }
                else if(testflag = true)
                {
                    testflag = false;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1000:
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permision Granted", Toast.LENGTH_SHORT).show();       //check if user accepted write file permission
                }else{
                    Toast.makeText(this, "Permision Denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            //Change Txt Views here
            Acceloutx.setText(String.valueOf(sensorEvent.values[0]));
            AcceloutZ.setText(String.valueOf(sensorEvent.values[2]));
            Accelouty.setText(String.valueOf(sensorEvent.values[1]));
            String AccelOutput = (sensorEvent.values[0] + "," + sensorEvent.values[1] + "," + sensorEvent.values[2] + ",");
            if (testflag = true)
            {
                try{
                    FileOutputStream Accelfos = new FileOutputStream(AccelFile, true);
                    Accelfos.write(AccelOutput.getBytes());
                    Accelfos.close();

                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        if (sensorEvent.sensor.getType()== Sensor.TYPE_GYROSCOPE){
            //change Txt Views here
            Gyrooutx.setText(String.valueOf(sensorEvent.values[0]));
            Gyrooutz.setText(String.valueOf(sensorEvent.values[2]));
            Gyroouty.setText(String.valueOf(sensorEvent.values[1]));
            String GyroOutput = (sensorEvent.values[0] + "," + sensorEvent.values[1] + "," + sensorEvent.values[2] + ",");
                try{
                    Writer Accelfos = new BufferedWriter(new FileWriter(GyroFile, true));
                    Accelfos.write(GyroOutput);
                    Accelfos.close();

                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR)
        {
            StepCount++; //adds one to the step count
            Stepout.setText(StepCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //placeholder not used
    }

}
