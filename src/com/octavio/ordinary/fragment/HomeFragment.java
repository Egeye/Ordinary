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
	
	Thread myThread;// ���̣߳���̨����UI
	
	boolean playStatus = true;// ���ƺ�̨�߳��˳�

	// �������������
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// ��bundle�л�ȡ���ȣ���double���ͣ����ŵİٷֱ�
				double progress = msg.getData().getDouble("progress");

				// ���ݲ��Űٷֱȣ�����seekbar��ʵ��λ��
				int max = seekBar.getMax();
				int position = (int) (max * progress);

				// ����seekbar��ʵ��λ��
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

		// ���ְ�ť����ʱ���ѹ���
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
		rotate.setInterpolator(new LinearInterpolator());// ��ͣ��
		

		//����һ�����̣߳�����������Ϣ��֪ͨ����UI
		myThread = new Thread(new MyThread());
		
		//��service;
		Intent serviceIntent = new Intent(getActivity() , MusicService.class);
		//���δ�󶨣�����а�
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
				//������Ҫ�ж������
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
		
		// ������
	    seekBar = (SeekBar)parentView.findViewById(R.id.seekbar);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//�ֶ����ڽ���
				//seekbar���϶�λ��
				int dest = seekBar.getProgress();
				//seekbar�����ֵ
				int max = seekBar.getMax();
				//����service���ڲ��Ž���
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


	// ��ʾ�����Ҳ�˵�
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

	// ��ʾ�������˵�
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

	// ���м䲼�ְ�ť����ʵ ���ڽ���
	private void openMid() {
		parentView.findViewById(R.id.rl_wait_to_click).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getActivity(), HomeActivity.class);
				startActivity(i);
			}
		});
	}


	//ʵ��runnable�ӿڣ����߳�ʵʱ���½�����
	public class MyThread implements Runnable{
		//֪ͨUI���µ���Ϣ
		//������UI�̴߳��ݽ��ȵ�ֵ
		Bundle data = new Bundle();
		
		//����UI���ʱ��
		int milliseconds = 100;
		double progress;
		@Override
		public void run() {
			//������ʶ�Ƿ��ڲ���״̬�����������߳��˳�
			while(playStatus){  
				
                try {  
                	//�󶨳ɹ����ܿ�ʼ����UI
                    if(mBound){
                    	
                    	//������Ϣ��Ҫ�����UI
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
					//ÿ��100ms����һ��UI
        			
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
            
        	//��ȡservice
        	mService = (MusicService) myBinder.getService();  
            
            //�󶨳ɹ�
            mBound = true;  
            
            //�����̣߳�����UI
            myThread.start();
        }  
  
        @Override  
        public void onServiceDisconnected(ComponentName arg0) {  
            mBound = false;  
        }  
    };
	
    
	@Override
	public void onDestroy(){
		//����activityʱ��Ҫ�ǵ������߳�
		playStatus = false;
		super.onDestroy();
	}

}

