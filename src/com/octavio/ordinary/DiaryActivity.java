package com.octavio.ordinary;

import java.io.File;

import com.octavio.ordinary.db.DiaryDbHelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DiaryActivity extends Activity {

	private TextView tvTitle;
	private TextView tvTime;
	private TextView tvContent;
	private DiaryDbHelper mDbHelper;
	private Long mRowId;
	private Button btnBack;
	private Button btnDelete;
	private ImageView ll;

	private ImageView ivMood;
	private TextView tvLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		mDbHelper = new DiaryDbHelper(this);
		mDbHelper.open();
		setContentView(R.layout.activity_diary_show);

		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvContent = (TextView) findViewById(R.id.tv_content);
		btnBack = (Button) findViewById(R.id.btn_back);
		ll = (ImageView) findViewById(R.id.ads_ll);

		ivMood = (ImageView) findViewById(R.id.iv_mood_show);
		tvLocation = (TextView) findViewById(R.id.tv_location_show);

		ll.setImageResource((R.drawable.show));

		mRowId = null;
		// 每一个intent都会带一个Bundle型的extras数据。
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String title = extras.getString(DiaryDbHelper.KEY_TITLE);
			String body = extras.getString(DiaryDbHelper.KEY_BODY);
			String time = extras.getString(DiaryDbHelper.KEY_CREATED);

			int mood = extras.getInt(DiaryDbHelper.KEY_MOOD);
			String location = extras.getString(DiaryDbHelper.KEY_LOCATION);
			String picture = extras.getString(DiaryDbHelper.KEY_PICTURE);

			mRowId = extras.getLong(DiaryDbHelper.KEY_ROWID);

			if (title != null) {
				tvTitle.setText(title);
			}
			if (body != null) {
				tvContent.setText(body);
			}
			if (time != null) {
				tvTime.setText(time);
			}
			if (mood != 0) {
				switch (mood) {
				case 1:
					ivMood.setImageResource(R.drawable.mood_idles);
					break;
				case 2:
					ivMood.setImageResource(R.drawable.mood_angry);
					break;
				case 3:
					ivMood.setImageResource(R.drawable.mood_laught);
					break;
				case 4:
					ivMood.setImageResource(R.drawable.mood_sad);
					break;
				case 5:
					ivMood.setImageResource(R.drawable.mood_sleppy);
					break;
				case 6:
					ivMood.setImageResource(R.drawable.mood_smoke);
					break;
				case 7:
					ivMood.setImageResource(R.drawable.mood_tears);
					break;
				case 8:
					ivMood.setImageResource(R.drawable.mood_upset);
					break;
				case 9:
					ivMood.setImageResource(R.drawable.mood_wuyu);
					break;
				default:
					break;

				}
			}
			if (location != null) {
				tvLocation.setText(location);
			}
			if (picture != null) {
				File file = new File(picture);

				if (file.exists()) {
					Bitmap bm = BitmapFactory.decodeFile(picture);
					// 将图片显示到ImageView中
					ll.setImageBitmap(bm);
				}

			}
		}

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

			}
		});

		// btnDelete.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Bundle extras = getIntent().getExtras();
		// mRowId = extras.getLong(DiaryDbHelper.KEY_ROWID);
		//
		// AlertDialog.Builder builder = new
		// AlertDialog.Builder(DiaryActivity.this);
		// builder.setMessage("确定要删除这页日记");
		// builder.setTitle("删除");
		// builder.setIcon(getResources().getDrawable(R.drawable.dialogdelete));
		// builder.setPositiveButton("确定", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//
		// mDbHelper.deleteDiary(mRowId);
		// }
		// });
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

}
