package com.octavio.ordinary;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener {

	private Button btnBack;
	private Button btnUpdate;
	private Button btnAbout;
	private Button btnExit;
	private Button btnFeedback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		setContentView(R.layout.activity_home_about);

		btnBack = (Button) findViewById(R.id.btn_back);

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

			}
		});

		btnUpdate = (Button) findViewById(R.id.btn_update);
		btnAbout = (Button) findViewById(R.id.btn_about);
		btnExit = (Button) findViewById(R.id.btn_exit);
		btnFeedback = (Button) findViewById(R.id.btn_feedback);
		
		btnUpdate.setOnClickListener(this);
		btnAbout.setOnClickListener(this);
		btnExit.setOnClickListener(this);
		btnFeedback.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_about:
			Toast.makeText(this, "¾¡ÇëÆÚ´ý(-.-)", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_update:
			Toast.makeText(this, "¾¡ÇëÆÚ´ý(-.-)", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_exit:
			Toast.makeText(this, "¾¡ÇëÆÚ´ý(-.-)", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_feedback:
			Toast.makeText(this, "¾¡ÇëÆÚ´ý(-.-)", Toast.LENGTH_SHORT).show();
			break;
		}

	}

}
