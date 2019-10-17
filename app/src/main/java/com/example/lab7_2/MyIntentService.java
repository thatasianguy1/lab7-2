package com.example.lab7_2;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final int N = alphabet.length();
    private Random r = new Random();
    private char c;

    public MyIntentService(){
        super(MyIntentService.class.getSimpleName());
    }

    class MyIntentServiceBinder extends Binder{
        public MyIntentService getService(){
            return MyIntentService.this;
        }
    }

    private IBinder mBinder = new MyIntentServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(getString(R.string.service_demo_tag),"In OnBind");
        return mBinder;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(getString(R.string.service_demo_tag),"Service Started");
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i(getString(R.string.service_demo_tag),"In OnReBind");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mIsRandomGeneratorOn =true;
        startRandomNumberGenerator();
    }

    private void startRandomNumberGenerator(){
        while (mIsRandomGeneratorOn){
            try{
                Thread.sleep(1000);
                if(mIsRandomGeneratorOn){
                    c = alphabet.charAt(r.nextInt(N));
                    Log.i(getString(R.string.service_demo_tag),"Thread id: "+Thread.currentThread().getId()+", Random : "+ c);
                }
            }catch (InterruptedException e){
                Log.i(getString(R.string.service_demo_tag),"Thread Interrupted");
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsRandomGeneratorOn=false;
        Log.i(getString(R.string.service_demo_tag),"Service Destroyed");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(getString(R.string.service_demo_tag),"In onUnbind");
        return super.onUnbind(intent);
    }

    public char getRandomNumber(){
        return c;
    }
}

