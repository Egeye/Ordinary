package com.octavio.ordinary;

import java.util.Calendar;

import com.octavio.ordinary.service.RemindService;
import com.octavio.ordinary.utils.TimeFormatUtil;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class HomeRemindActivity extends Activity {

	private Calendar calendar = null;
	private TextView currentTime = null;
	private TextView setTime = null;
	private TimeChangeReceiver receiver = null;
	private IntentFilter iFilter = null;
	private Intent mIntent = null;
	private Button set = null;
	private Button cancel = null;

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x001) {
				SharedPreferences shared = getSharedPreferences("ordinary_time", 0);
				setTime.setText("提醒时间：" + " " + TimeFormatUtil.format(shared.getInt("hour", 0)) + ":"
						+ TimeFormatUtil.format(shared.getInt("minute", 0)));
			}
		};
	};

	class TimeChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {  
			if (intent.getAction().equals("com.android.set.remind.time")) {
				Log.d("SetRemindActivity", "-----set remind time-----");
				handler.sendEmptyMessage(0x001);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();

	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home_remind);
		calendar = Calendar.getInstance();

		set = (Button) this.findViewById(R.id.remind_ok);
		cancel = (Button) this.findViewById(R.id.remind_cancel);
		currentTime = (TextView) this.findViewById(R.id.current_time);

		calendar.setTimeInMillis(System.currentTimeMillis());
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int m = calendar.get(Calendar.MINUTE);
		currentTime.setText("打开时间：" + " " + TimeFormatUtil.format(h) + ":" + TimeFormatUtil.format(m));

		setTime = (TextView) this.findViewById(R.id.set_time);
		set.setOnClickListener(new MyListener());
		cancel.setOnClickListener(new MyListener());

		receiver = new TimeChangeReceiver();
		iFilter = new IntentFilter();
		iFilter.addAction("com.android.set.remind.time");
		iFilter.setPriority(Integer.MAX_VALUE);
		// 注册广播接收器
		registerReceiver(receiver, iFilter);

	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences shared = getSharedPreferences("ordinary_time", 0);
		if (shared.getBoolean("is_set", false)) {
			setTime.setText("提醒时间：" + " " + TimeFormatUtil.format(shared.getInt("hour", 0)) + ":"
					+ TimeFormatUtil.format(shared.getInt("minute", 0)));
		}
	}

	class MyListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.remind_ok:
				calendar.setTimeInMillis(System.currentTimeMillis());
				int mHour = calendar.get(Calendar.HOUR_OF_DAY);
				int mMinute = calendar.get(Calendar.MINUTE);
				new TimePickerDialog(HomeRemindActivity.this, new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						calendar.setTimeInMillis(System.currentTimeMillis());
						calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						calendar.set(Calendar.MINUTE, minute);
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
						mIntent = new Intent(HomeRemindActivity.this, RemindService.class);
						PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeRemindActivity.this, 0, mIntent,
								0);
						AlarmManager manager;
						// 获取闹钟管理的实例
						manager = (AlarmManager) getSystemService(ALARM_SERVICE);
						// 设置闹钟
						manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
						// 设置周期闹钟
						manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (10 * 1000),
								(24 * 60 * 60 * 1000), pendingIntent);
						SharedPreferences shared = getSharedPreferences("ordinary_time", 0);
						Editor editor = shared.edit();
						editor.putInt("hour", hourOfDay);
						editor.putInt("minute", minute);
						editor.putBoolean("is_set", true);
						editor.putString("mytime",
								TimeFormatUtil.format(hourOfDay) + "" + TimeFormatUtil.format(minute));
						editor.commit();
						HomeRemindActivity.this.startService(mIntent);
					}
				}, mHour, mMinute, true).show();
				break;
			case R.id.remind_cancel:
				setTime.setText("提醒时间：" + " ");
				SharedPreferences shared = getSharedPreferences("ordinary_time", 0);
				Editor editor = shared.edit();
				editor.putInt("hour", 0);
				editor.putInt("minute", 0);
				editor.putBoolean("is_set", false);
				editor.commit();
				if (mIntent != null) {
					HomeRemindActivity.this.stopService(mIntent);
				}
				break;
			default:
				break;
			}
		}
	}
	

}
