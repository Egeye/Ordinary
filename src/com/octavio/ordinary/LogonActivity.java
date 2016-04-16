package com.octavio.ordinary;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class LogonActivity extends Activity {
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			skipToMain();
		}

		// 跳到主页面
		private void skipToMain() {
			Intent intent = new Intent(LogonActivity.this, MainActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold); // 自写的渐隐动画效果
			// overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_logon);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
				finish();
			}
		}).start();
	}

}
