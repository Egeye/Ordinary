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

	// 获取到activity的Handler，用来通知更新进度条
	Handler mHandler;

	// 播放音乐的媒体类
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
		// 当绑定后，返回一个musicBinder
		return musicBinder;
	}

	public class MyBinder extends Binder {

		public Service getService() {
			return MusicService.this;
		}
	}

	// 初始化音乐播放
	void init() {
		
		// 进入Idle
		mediaPlayer = new MediaPlayer();
		try {
			// 初始化
			// 本地歌曲的路径
			File file = new File(Environment.getExternalStorageDirectory(), "or.mp3");
			
			if(file.exists()){
				String path = file.getPath();
				mediaPlayer.setDataSource(path);
			} else {
				AssetManager am = getAssets();// 获得该应用的AssetManager
				AssetFileDescriptor afd = am.openFd("ordinary.mp3");
				mediaPlayer.setDataSource(afd.getFileDescriptor());

			}
			
			
			
			

			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			// prepare 通过异步的方式装载媒体资源
			mediaPlayer.prepareAsync();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 返回当前的播放进度，是double类型，即播放的百分比
	public double getProgress() {
		int position = mediaPlayer.getCurrentPosition();

		int time = mediaPlayer.getDuration();

		double progress = (double) position / (double) time;

		return progress;
	}

	// 通过activity调节播放进度
	public void setProgress(int max, int dest) {
		int time = mediaPlayer.getDuration();
		mediaPlayer.seekTo(time * dest / max);
	}

	// 测试播放音乐
	public void play() {
		if (mediaPlayer != null) {
			mediaPlayer.start();
			
			
		}

	}

	// 暂停音乐
	public void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}
	
	// 停止音乐
	public void stop(){
		if(mediaPlayer.isPlaying()){
			mediaPlayer.reset();
			init();
			
		}
	}

	// service 销毁时，停止播放音乐，释放资源
	@Override
	public void onDestroy() {
		// 在activity结束的时候回收资源
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		super.onDestroy();
	}

}
