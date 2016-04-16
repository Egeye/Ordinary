package com.octavio.ordinary.fragment;


import com.octavio.ordinary.HomeActivity;
import com.octavio.ordinary.HomeRemindActivity;
import com.octavio.ordinary.MainActivity;
import com.octavio.ordinary.R;
import com.octavio.ordinary.ResideMenu.ResideMenu;
import com.octavio.ordinary.service.MusicService;
import com.octavio.ordinary.service.MusicService.MyBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * About Home
 * 
 * @author Octavio
 *
 */
public class HomeFragment extends Fragment{

	private View parentView;
	private ResideMenu resideMenu;
	
	private Button playButton;
	private Button pauseButton;  


	private ImageView iv;
	private Animation rotate;

	
	// service play music
	Boolean mBound = false;

	MusicService mService;

	SeekBar seekBar;
	
	Thread myThread;// 多线程，后台更新UI
	
	boolean playStatus = true;// 控制后台线程退出

	// 处理进度条更新
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 从bundle中获取进度，是double类型，播放的百分比
				double progress = msg.getData().getDouble("progress");

				// 根据播放百分比，计算seekbar的实际位置
				int max = seekBar.getMax();
				int position = (int) (max * progress);

				// 设置seekbar的实际位置
				seekBar.setProgress(position);
				break;
			default:
				break;
			}

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		parentView = inflater.inflate(R.layout.fragment_home, container, false);

		openLeft();
		openRight();
		openMid();

		// 文字按钮，定时提醒功能
		Button btnInfo = (Button) parentView.findViewById(R.id.btn_or);
		btnInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), HomeRemindActivity.class);
				startActivity(i);
			}
		});

		
		iv = (ImageView) parentView.findViewById(R.id.iv_music);
		rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
		rotate.setInterpolator(new LinearInterpolator());// 不停顿
		

		//定义一个新线程，用来发送消息，通知更新UI
		myThread = new Thread(new MyThread());
		
		//绑定service;
		Intent serviceIntent = new Intent(getActivity() , MusicService.class);
		//如果未绑定，则进行绑定
		if(!mBound){
			getActivity().bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
		}
		
		playButton = (Button)parentView.findViewById(R.id.btn_play);
		playButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(mBound){
					mService.play();
					playButton.setVisibility(View.GONE);
					pauseButton.setVisibility(View.VISIBLE);
					iv.startAnimation(rotate);
					rotate.setAnimationListener(new AnimationListener(){

						@Override
						public void onAnimationStart(Animation animation) {
							
						}

						@Override
						public void onAnimationEnd(Animation animation) {
							iv.startAnimation(rotate);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							
						}
						
					});
				}			
			}
			
		});
		
		pauseButton = (Button)parentView.findViewById(R.id.btn_pause);	
		pauseButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//首先需要判定绑定情况
				if(mBound){
					mService.pause();
					pauseButton.setVisibility(View.GONE);
					playButton.setVisibility(View.VISIBLE);
					
					iv.clearAnimation();
					rotate.setAnimationListener(new AnimationListener(){

						@Override
						public void onAnimationStart(Animation animation) {
							
						}

						@Override
						public void onAnimationEnd(Animation animation) {
							iv.clearAnimation();
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							
						}
						
					});
				}
			}
		});
		
		Button stopButton = (Button) parentView.findViewById(R.id.btn_stop);
		stopButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mBound){
					mService.stop();
					pauseButton.setVisibility(View.GONE);
					playButton.setVisibility(View.VISIBLE);
					
					iv.clearAnimation();
					rotate.setAnimationListener(new AnimationListener(){

						@Override
						public void onAnimationStart(Animation animation) {
							
						}

						@Override
						public void onAnimationEnd(Animation animation) {
							iv.clearAnimation();
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							
						}
						
					});
				}
			}
		});
		
		// 进度条
	    seekBar = (SeekBar)parentView.findViewById(R.id.seekbar);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//手动调节进度
				//seekbar的拖动位置
				int dest = seekBar.getProgress();
				//seekbar的最大值
				int max = seekBar.getMax();
				//调用service调节播放进度
				mService.setProgress(max, dest);
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}
			
		});	

		return parentView;

	}


	// 显示滑动右侧菜单
	private void openRight() {
		MainActivity parentActivity = (MainActivity) getActivity();
		resideMenu = parentActivity.getResideMenu();
		parentView.findViewById(R.id.btn_open_right).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
			}
		});
	}

	// 显示滑动左侧菜单
	private void openLeft() {
		MainActivity parentActivity = (MainActivity) getActivity();
		resideMenu = parentActivity.getResideMenu();
		parentView.findViewById(R.id.btn_open_left).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});

	}

	// 打开中间布局按钮，现实 关于界面
	private void openMid() {
		parentView.findViewById(R.id.rl_wait_to_click).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getActivity(), HomeActivity.class);
				startActivity(i);
			}
		});
	}


	//实现runnable接口，多线程实时更新进度条
	public class MyThread implements Runnable{
		//通知UI更新的消息
		//用来向UI线程传递进度的值
		Bundle data = new Bundle();
		
		//更新UI间隔时间
		int milliseconds = 100;
		double progress;
		@Override
		public void run() {
			//用来标识是否还在播放状态，用来控制线程退出
			while(playStatus){  
				
                try {  
                	//绑定成功才能开始更新UI
                    if(mBound){
                    	
                    	//发送消息，要求更新UI
                    	Message msg = new Message();
                    	data.clear();
             	
                    	progress = mService.getProgress();
            			msg.what = 0;
            			
            			data.putDouble("progress", progress);
            			msg.setData(data);
            			mHandler.sendMessage(msg);
                    }
                    Thread.sleep(milliseconds);  
        			//Thread.currentThread().sleep(milliseconds);  
					//每隔100ms更新一次UI
        			
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
			}
		}
	}
	
	 /** Defines callbacks for service binding, passed to bindService() */  
    private ServiceConnection mConnection = new ServiceConnection() {  
  
        @Override  
        public void onServiceConnected(ComponentName className,  
                IBinder binder) {  
            // We've bound to LocalService, cast the IBinder and get LocalService instance  
        	MyBinder myBinder = (MyBinder) binder;
            
        	//获取service
        	mService = (MusicService) myBinder.getService();  
            
            //绑定成功
            mBound = true;  
            
            //开启线程，更新UI
            myThread.start();
        }  
  
        @Override  
        public void onServiceDisconnected(ComponentName arg0) {  
            mBound = false;  
        }  
    };
	
    
	@Override
	public void onDestroy(){
		//销毁activity时，要记得销毁线程
		playStatus = false;
		super.onDestroy();
	}

}

