package com.akshanshgusain.serviceonly;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

public class MyService extends Service {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;

    private final int MIN=0;
    private final int MAX=100;
    public static final int GET_RANDOM_NUMBER_FLAG = 0;

    private class RandomNumberRequestHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
                switch(msg.what)// What will be holding the flag (Sent from another app)
                {
                    //Check whether the flag is equivalent to the "GET_RANDOM_NUMBER_FLAG"
                    case GET_RANDOM_NUMBER_FLAG:
                        //1.Obtain the message address :
                        Message messageSendRandomNumber = Message.obtain(null, GET_RANDOM_NUMBER_FLAG);
                        //2. Assign the random number to argument 1 of the Message reference variable :
                        messageSendRandomNumber.arg1 = getmRandomNumber();
                        //3. msg.reply to return to the Messenger

                        try {
                            msg.replyTo.send(messageSendRandomNumber);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                }
                super.handleMessage(msg);
        }
    }
    //4.Then we will create a Messenger
     private Messenger randomNumberMessenger = new Messenger(new RandomNumberRequestHandler());


//    //You need to define a method "getService()" that returns a service reference
//    public class MyServiceBinder extends Binder{
//        public MyService getService()
//        {
//            return MyService.this;
//        }
//    }
//    private IBinder mBinder = new MyServiceBinder() ;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return randomNumberMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
        Log.d("lolo", "onDestroy: The Service is Destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("lolo", "onStartCommand: "+ Thread.currentThread().getId());
//        stopSelf();
        if(mIsRandomGeneratorOn==false)
        {
            mIsRandomGeneratorOn = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startRandomNumberGenerator();
                }
            }).start();
        }

        return START_STICKY;
    }




    private void startRandomNumberGenerator()
    {
          while(mIsRandomGeneratorOn)
          {
              try {
                  Thread.sleep(1000);
                  if(mIsRandomGeneratorOn)
                  {
                      mRandomNumber = new Random().nextInt(MAX)+MIN;
                      Log.d("lolo", "startRandomNumberGenerator: "+ Thread.currentThread().getId()+" Randome Number: "+ mRandomNumber);
                  }

              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
    }
    private void stopRandomNumberGenerator()
    {
          mIsRandomGeneratorOn=false;
    }


    public int getmRandomNumber()
    {
        return mRandomNumber;
    }
}
