package com.octavio.ordinary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RemindService extends Service {
	public static RemindService service = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("MyService", "-----MyService onCreate-----");
		service = this;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d("MyService", "-----MyService onStart-----");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("MyService", "-----MyService onStartCommand-----");
		sendBroadcast(new Intent("com.android.set.remind.time"));
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(60000);
						sendBroadcast(new Intent("com.android.system.time"));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}
}
