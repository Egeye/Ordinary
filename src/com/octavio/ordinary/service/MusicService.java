package com.octavio.ordinary.service;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager.OnActivityStopListener;
import android.util.Log;
import android.view.View;

public class MusicService extends Service {

	IBinder musicBinder = new MyBinder();

	// ��ȡ��activity��Handler������֪ͨ���½�����
	Handler mHandler;

	// �������ֵ�ý����
	MediaPlayer mediaPlayer;


	private String TAG = "MyService";

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate() executed");

		init();

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// ���󶨺󣬷���һ��musicBinder
		return musicBinder;
	}

	public class MyBinder extends Binder {

		public Service getService() {
			return MusicService.this;
		}
	}

	// ��ʼ�����ֲ���
	void init() {
		
		// ����Idle
		mediaPlayer = new MediaPlayer();
		try {
			// ��ʼ��
			// ���ظ�����·��
			File file = new File(Environment.getExternalStorageDirectory(), "or.mp3");
			
			if(file.exists()){
				String path = file.getPath();
				mediaPlayer.setDataSource(path);
			} else {
				AssetManager am = getAssets();// ��ø�Ӧ�õ�AssetManager
				AssetFileDescriptor afd = am.openFd("ordinary.mp3");
				mediaPlayer.setDataSource(afd.getFileDescriptor());

			}
			
			
			
			

			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			// prepare ͨ���첽�ķ�ʽװ��ý����Դ
			mediaPlayer.prepareAsync();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ���ص�ǰ�Ĳ��Ž��ȣ���double���ͣ������ŵİٷֱ�
	public double getProgress() {
		int position = mediaPlayer.getCurrentPosition();

		int time = mediaPlayer.getDuration();

		double progress = (double) position / (double) time;

		return progress;
	}

	// ͨ��activity���ڲ��Ž���
	public void setProgress(int max, int dest) {
		int time = mediaPlayer.getDuration();
		mediaPlayer.seekTo(time * dest / max);
	}

	// ���Բ�������
	public void play() {
		if (mediaPlayer != null) {
			mediaPlayer.start();
			
			
		}

	}

	// ��ͣ����
	public void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}
	
	// ֹͣ����
	public void stop(){
		if(mediaPlayer.isPlaying()){
			mediaPlayer.reset();
			init();
			
		}
	}

	// service ����ʱ��ֹͣ�������֣��ͷ���Դ
	@Override
	public void onDestroy() {
		// ��activity������ʱ�������Դ
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		super.onDestroy();
	}

}
