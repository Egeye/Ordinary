package com.octavio.ordinary.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.octavio.ordinary.R;
import com.octavio.ordinary.db.DiaryDbHelper;
import com.octavio.ordinary.utils.MyBaiduLotion;
import com.octavio.ordinary.utils.MyLocation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * write function
 * 
 * @author Octavio
 *
 */
public class WriteFragment extends Fragment {

	// P
	private String pictureName;
	private String path; // 图片保存路径
	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
//	public static final int PHOTO_REQUEST_GALLERY = 3;
//	public static final int PHOTO_REQUEST_CUT = 4;
	
	Button btnPhoto, btnPicture;
	private Uri imageUri;
	private ImageView picture;
	private TextView tvPath;
	
	

	// 定位
	MyBaiduLotion myLotion;
	MyLocation myLocation;
	String strlocation = ""; //定位文本
	private TextView tvMyLocation;

	// 定义选项菜单
	private String[] allOptionsMenuTexts = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private int[] allOptionsMenuOrders = { 5, 2, 6, 1, 4, 3, 7, 8, 9 };
	private int[] allOptionsMenuIds = { Menu.FIRST + 1, Menu.FIRST + 2, Menu.FIRST + 3, Menu.FIRST + 4, Menu.FIRST + 5,
			Menu.FIRST + 6, Menu.FIRST + 7, Menu.FIRST + 8, Menu.FIRST + 9 };
	private int[] allOptionsMenuIcons = { R.drawable.mood_idles, R.drawable.mood_angry, R.drawable.mood_laught,
			R.drawable.mood_sad, R.drawable.mood_sleppy, R.drawable.mood_smoke, R.drawable.mood_tears,
			R.drawable.mood_upset, R.drawable.mood_wuyu };

	private ImageView ivMood;
	private TextView tvMood, tvLocation; //心情和定位

	private EditText mTitleText;
	private EditText mBodyText;
	private Long mRowId;
	private DiaryDbHelper mDbHelper;

	private View parentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		parentView = inflater.inflate(R.layout.fragment_write, container, false);

		mDbHelper = new DiaryDbHelper(getActivity());
		mDbHelper.open();
		// setContentView(R.layout.activity_write);

		tvMood = (TextView) parentView.findViewById(R.id.tv_mood);
		ivMood = (ImageView) parentView.findViewById(R.id.iv_mood);
		tvLocation = (TextView) parentView.findViewById(R.id.tv_locate);

		mTitleText = (EditText) parentView.findViewById(R.id.et_title);
		mBodyText = (EditText) parentView.findViewById(R.id.et_content);
		Button confirmButton = (Button) parentView.findViewById(R.id.btn_save);

		mRowId = null;

		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// 标题
				String title = mTitleText.getText().toString();

				// 内容
				String body = mBodyText.getText().toString();

				// 心情 号码
				String mood = tvMood.getText().toString();
				int m = Integer.parseInt(mood);

				// 位置
				String location = tvLocation.getText().toString();

				// 图片
				String picture = tvPath.getText().toString();

				if ("".equals(title) || "".equals(body)) {
					Toast.makeText(getActivity(), "日记不完整", 5).show();
					return;
				} else if (mRowId != null) {
					mDbHelper.updateDiary(mRowId, title, body);
				} else {
					mDbHelper.createDiary(title, body, m, picture, location);
					Intent mIntent = new Intent();
					// setResult(RESULT_OK, mIntent);
					setResult(-1, mIntent);
					Toast.makeText(getActivity(), "已保存", 0).show();

				}
			}
		});

		
		// 重写
		Button btnCancel = (Button) parentView.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTitleText.setText("");
				mBodyText.setText("");
				tvMood.setText(""); 
				tvLocation.setText("");
				tvPath.setText(null);
				
				ivMood.setImageResource(R.drawable.white);
				picture.setImageResource(R.drawable.white);

			}
		});

		// 心情图片
		Button btnMood = (Button) parentView.findViewById(R.id.btn_mood);
		btnMood.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog9();
			}
		});

		// 定位文字
		tvMyLocation = (TextView) parentView.findViewById(R.id.tv_locate);
		Button btnLocate = (Button) parentView.findViewById(R.id.btn_location);
		btnLocate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "正在定位……", 1).show();
				myLotion = new MyBaiduLotion(getActivity());
				myLocation = new MyLocation();
				myLotion.opetateClient();
				new LocationTHread().start();
			}
		});
		// 清除定位
		Button btnClear = (Button) parentView.findViewById(R.id.btn_locate);
		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tvMyLocation.setText("");
			}
		});

		// 图片 iv_p
		picture = (ImageView) parentView.findViewById(R.id.iv_p);
		btnPhoto = (Button) parentView.findViewById(R.id.btn_photo);
		btnPicture = (Button) parentView.findViewById(R.id.btn_picture);
		tvPath = (TextView) parentView.findViewById(R.id.tv_pic_path);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);
		
		pictureName = year+"_"+month+"_"+day+"_"+hour+"_"+min + "_" + sec + ".jpg";
		
		btnPhoto.setOnClickListener(new OnClickListener() {

			
			@Override
			public void onClick(View v) {
				// 创建File对象，用于存储拍照后的图片,
				// 调用Environment的getExternalStorageDirectory()方法获取到的就是手机SD卡的根目录
				File outputImage = new File(Environment.getExternalStorageDirectory(), pictureName);
				
				path = outputImage.getPath();
				try {
					
					outputImage.createNewFile();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				imageUri = Uri.fromFile(outputImage);
				// 调用Uri的fromFile()方法将File对象转换成Uri对象，这个Uri对象标识着output_image.jpg这张图片的唯一地址

				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, TAKE_PHOTO); // 启动相机程序
				// 使用startActivityForResult()来启动活动的，因此拍完照后会有结果返回到onActivityResult()方法中。
				// 如果发现拍照成功，则会再次构建出一个Intent对象，并把它的action指定为com.android.camera.action.CROP
			}
		});
		
//		btnPicture.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// 创建File对象，用于存储选择的照片
//				File outputImage = new File(Environment.getExternalStorageDirectory(), pictureName);
//				try {
//					outputImage.createNewFile();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				imageUri = Uri.fromFile(outputImage);
//
//				// 激活系统图库，选择一张图片
//		        Intent intent = new Intent(Intent.ACTION_PICK);
//		        intent.setType("image/*");
//		        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
//		        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
//			}
//		});
		
		
		

		return parentView;

	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == getActivity().RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				// 这个Intent是用于对拍出的照片进行裁剪的，因为摄像头拍出的照片都比较大，而我们可能只希望截取其中的一小部分

				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
			}
			break;
		case CROP_PHOTO:
			if (resultCode == getActivity().RESULT_OK) {
				try {
					// 调用BitmapFactory的decodeStream()方法将output_image.jpg这张照片解析成Bitmap对象，
					// 然后把它设置到ImageView中显示出来。
					Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
					picture.setImageBitmap(bitmap); // 将裁剪后的照片显示出来
					tvPath.setText(path);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
			break;
//		case PHOTO_REQUEST_GALLERY:
//			Uri uri = data.getData();
//			
//			Intent intent = new Intent("com.android.camera.action.CROP");
//	        intent.setDataAndType(uri, "image/*");
//	        intent.putExtra("crop", "true");
////	        // 裁剪框的比例，1：1
////	        intent.putExtra("aspectX", 1);
////	        intent.putExtra("aspectY", 1);
////	        // 裁剪后输出图片的尺寸大小
////	        intent.putExtra("outputX", 250);
////	        intent.putExtra("outputY", 250);
//	 
//	        intent.putExtra("outputFormat", "JPEG");// 图片格式
//	        intent.putExtra("noFaceDetection", true);// 取消人脸识别
//	        intent.putExtra("return-data", true);
//	        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
//	        startActivityForResult(intent, PHOTO_REQUEST_CUT);
//			break;
//		case PHOTO_REQUEST_CUT:
//			Bitmap bitmap = data.getParcelableExtra("data");
//			picture.setImageBitmap(bitmap);
//			tvPath.setText(path);
		default:
			break;
		}
		
	}
	

	int mResultCode = 0;
	Intent mResultData = null;

	public final void setResult(int resultCode, Intent data) {
		synchronized (this) {
			mResultCode = resultCode;
			mResultData = data;
		}
	}

	// custome menu 心情
	public void showDialog9() {
		final Context context = getActivity();

		// 获取自定义布局
		LayoutInflater layoutInflater = getActivity().getLayoutInflater();
		View menuView = layoutInflater.inflate(R.layout.fragment_write_mood_gridview, null);

		// 获取GridView组件并配置适配器
		GridView gridView = (GridView) menuView.findViewById(R.id.gridview);
		SimpleAdapter menuSimpleAdapter = createSimpleAdapter(allOptionsMenuTexts, allOptionsMenuIcons);
		gridView.setAdapter(menuSimpleAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Toast.makeText(context,
				// "菜单["+allOptionsMenuTexts[position]+"]点击了.",
				// Toast.LENGTH_SHORT).show();

				ivMood.setImageResource(allOptionsMenuIcons[position]);
				tvMood.setText(allOptionsMenuTexts[position]);

			}
		});

		// 创建对话框并显示
		new AlertDialog.Builder(context).setView(menuView).show();
	}

	public SimpleAdapter createSimpleAdapter(String[] menuNames, int[] menuImages) {
		List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
		String[] fromsAdapter = { "item_text", "item_image" };
		int[] tosAdapter = { R.id.item_text, R.id.item_image };
		for (int i = 0; i < menuNames.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(fromsAdapter[0], menuNames[i]);
			map.put(fromsAdapter[1], menuImages[i]);
			data.add(map);
		}

		SimpleAdapter SimpleAdapter = new SimpleAdapter(getActivity(), data, R.layout.fragment_write_mood_items,
				fromsAdapter, tosAdapter);
		return SimpleAdapter;
	}

	// 定位
	class LocationTHread extends Thread {

		@Override
		public void run() {
			super.run();
			if (myLotion != null)
				while (!myLotion.getIsFinish()) {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			if (myLotion.myBDcoordinate != null) {
				strlocation = myLocation.getAddress(myLotion.getLatValue() + "", myLotion.getLongValue() + "");
				myHandler.sendEmptyMessage(1);
			}

		}

	}

	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tvMyLocation.setText(strlocation);
		}

	};
	

}
