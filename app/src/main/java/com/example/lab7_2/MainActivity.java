package com.example.lab7_2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button buttonStart, buttonStop, buttonBind,buttonUnBind,buttonGetRandomNumber;
    private TextView textViewthreadCount;
    int count = 0;
    private MyAsyncTask myAsyncTask;

    private MyIntentService myService;
    private boolean isServiceBound;
    private ServiceConnection  serviceConnection;

    private  Intent serviceIntent;

    private boolean mStopLoop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(getString(R.string.service_demo_tag), "MainActivity thread id: " + Thread.currentThread().getId());

        buttonStart = (Button) findViewById(R.id.start);
        buttonStop = (Button) findViewById(R.id.stop);
        buttonBind=(Button)findViewById(R.id.bind);
        buttonUnBind=(Button)findViewById(R.id.unbind);
        buttonGetRandomNumber=(Button)findViewById(R.id.random);


        textViewthreadCount = (TextView) findViewById(R.id.tv);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonBind.setOnClickListener(this);
        buttonUnBind.setOnClickListener(this);
        buttonGetRandomNumber.setOnClickListener(this);
        /*handler=new Handler(getApplicationContext().getMainLooper());*/

        serviceIntent=new Intent(getApplicationContext(),MyIntentService.class);



    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                mStopLoop = true;
                startService(serviceIntent);
                break;
            case R.id.stop:
                stopService(serviceIntent);
                break;
            case R.id.bind:bindService();break;
            case R.id.unbind: unbindService();break;
            case R.id.random: setRandomNumber();break;

        }
    }

    private void bindService(){
        if(serviceConnection==null){
            serviceConnection=new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    MyIntentService.MyIntentServiceBinder myServiceBinder=(MyIntentService.MyIntentServiceBinder)iBinder;
                    myService=myServiceBinder.getService();
                    isServiceBound=true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceBound=false;
                }
            };
        }

        bindService(serviceIntent,serviceConnection,Context.BIND_AUTO_CREATE);

    }

    private void unbindService(){
        if(isServiceBound){
            unbindService(serviceConnection);
            isServiceBound=false;
        }
    }

    private void setRandomNumber(){
        if(isServiceBound){
            textViewthreadCount.setText("Random : "+myService.getRandomNumber());
        }else{
            textViewthreadCount.setText("Service not bound");
        }
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        private int customCounter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customCounter = 0;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            customCounter = integers[0];
            while (mStopLoop) {
                try {
                    Thread.sleep(1000);
                    customCounter++;
                    publishProgress(customCounter);
                } catch (InterruptedException e) {
                    Log.i(TAG, e.getMessage());
                }

            }
            return customCounter;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            textViewthreadCount.setText(""+values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            textViewthreadCount.setText(""+integer);
            count=integer;
        }
    }
}
