package com.octavio.ordinary.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.octavio.ordinary.DiaryActivity;
import com.octavio.ordinary.R;
import com.octavio.ordinary.db.DiaryDbHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * about diary show function
 * 
 * @author Octavio
 *
 */
public class DiaryFragment extends Fragment {

	private View parentView;
	private DiaryDbHelper mDbHelper;
	private Cursor mDiaryCursor;

	private static final int ACTIVITY_EDIT = 1;
	private ListView lv;

	
	//
	private ViewPager viewPager; // android-support-v4�еĻ������
	private List<ImageView> imageViews; // ������ͼƬ����
	private String[] titles; // ͼƬ����
	private int[] imageResId; // ͼƬID
	private List<View> dots; // ͼƬ�������ĵ���Щ��
	private int currentItem = 0; // ��ǰͼƬ��������
	private ScheduledExecutorService scheduledExecutorService;
	
	// �л���ǰ��ʾ��ͼƬ
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// �л���ǰ��ʾ��ͼƬ
		};
	};
	//
	//
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.fragment_diary, container, false);
		
		
		//----------------------------------
		imageResId = new int[] { R.drawable.picture1, R.drawable.picture3, R.drawable.picture5, R.drawable.picture4  };
		imageViews = new ArrayList<ImageView>();

		// ��ʼ��ͼƬ��Դ
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}
		
		dots = new ArrayList<View>();
		dots.add(parentView.findViewById(R.id.v_dot0));
		dots.add(parentView.findViewById(R.id.v_dot1));
		dots.add(parentView.findViewById(R.id.v_dot2));
		dots.add(parentView.findViewById(R.id.v_dot3));

		viewPager = (ViewPager) parentView.findViewById(R.id.vp);
		viewPager.setAdapter(new MyAdapter());// �������ViewPagerҳ���������
		// ����һ������������ViewPager�е�ҳ��ı�ʱ����
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		
		
		//----------------------------------
		
		
		mDbHelper = new DiaryDbHelper(getActivity());
		mDbHelper.open();
		renderListView();

		return parentView;
	}

	private void renderListView() {

		lv = (ListView) parentView.findViewById(R.id.lv_diary);

		mDiaryCursor = mDbHelper.getAllNotes();
		

		String[] from = new String[] { DiaryDbHelper.KEY_TITLE, DiaryDbHelper.KEY_CREATED , DiaryDbHelper.KEY_MOOD, DiaryDbHelper.KEY_PICTURE, DiaryDbHelper.KEY_LOCATION };
		int[] to = new int[] { R.id.tv_title, R.id.tv_time , R.id.tv_gone1, R.id.tv_gone2, R.id.tv_gone3};
		SimpleCursorAdapter notes = new SimpleCursorAdapter(getActivity(), R.layout.fragment_diary_item, mDiaryCursor,
				from, to);
		lv.setAdapter(notes);

		/**
		 * ���ֱ�ӽ����ռ�������� δʹ��
		 */
		// lv.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// Cursor c = mDiaryCursor;
		// c.moveToPosition(position);
		// Intent i = new Intent();
		// i.setClass(getActivity(), DiaryActivity.class);
		// i.putExtra(DiaryDbHelper.KEY_ROWID, id);
		// i.putExtra(DiaryDbHelper.KEY_TITLE,
		// c.getString(c.getColumnIndexOrThrow(DiaryDbHelper.KEY_TITLE)));
		// i.putExtra(DiaryDbHelper.KEY_BODY,
		// c.getString(c.getColumnIndexOrThrow(DiaryDbHelper.KEY_BODY)));
		// i.putExtra(DiaryDbHelper.KEY_CREATED,
		// c.getString(c.getColumnIndexOrThrow(DiaryDbHelper.KEY_CREATED)));
		//
		// startActivity(i);
		//
		// }
		//
		// });

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setItems(new String[] { "�鿴", "ɾ��"}, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Cursor c = mDiaryCursor;
							c.moveToPosition(position);
							Intent i = new Intent();
							i.setClass(getActivity(), DiaryActivity.class);
							i.putExtra(DiaryDbHelper.KEY_ROWID, id);
							i.putExtra(DiaryDbHelper.KEY_TITLE,c.getString(c.getColumnIndexOrThrow(DiaryDbHelper.KEY_TITLE)));
							i.putExtra(DiaryDbHelper.KEY_BODY,c.getString(c.getColumnIndexOrThrow(DiaryDbHelper.KEY_BODY)));
							i.putExtra(DiaryDbHelper.KEY_CREATED,c.getString(c.getColumnIndexOrThrow(DiaryDbHelper.KEY_CREATED)));

							i.putExtra(DiaryDbHelper.KEY_MOOD, c.getInt(c.getColumnIndexOrThrow(DiaryDbHelper.KEY_MOOD)));
							i.putExtra(DiaryDbHelper.KEY_LOCATION,c.getString(c.getColumnIndexOrThrow(DiaryDbHelper.KEY_LOCATION)));
							i.putExtra(DiaryDbHelper.KEY_PICTURE,c.getString(c.getColumnIndexOrThrow(DiaryDbHelper.KEY_PICTURE)));
							
							startActivity(i);
							dialog.dismiss();
						} else if (which == 1) {
							mDbHelper.deleteDiary(id);
							renderListView();
							dialog.dismiss();
						}

					}
				});

				// �����ʾ������ť���鿴��ɾ��
				// builder.setPositiveButton("�鿴", new
				// DialogInterface.OnClickListener(){
				//
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				//
				// Cursor c = mDiaryCursor;
				// c.moveToPosition(position);
				// Intent i = new Intent();
				// i.setClass(getActivity(), DiaryActivity.class);
				// i.putExtra(DiaryDbHelper.KEY_ROWID, id);
				// i.putExtra(DiaryDbHelper.KEY_TITLE,
				// c.getString(c.getColumnIndexOrThrow(DiaryDbHelper.KEY_TITLE)));
				// i.putExtra(DiaryDbHelper.KEY_BODY,
				// c.getString(c.getColumnIndexOrThrow(DiaryDbHelper.KEY_BODY)));
				// i.putExtra(DiaryDbHelper.KEY_CREATED,
				// c.getString(c.getColumnIndexOrThrow(DiaryDbHelper.KEY_CREATED)));
				//
				// startActivity(i);
				// dialog.dismiss();
				// }
				//
				// });
				//
				// builder.setNegativeButton("ɾ��", new
				// android.content.DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// mDbHelper.deleteDiary(id);
				// renderListView();
				// dialog.dismiss();
				//
				// }});
				//
				builder.create().show();

			}
		});

	}
	
	
	
	
	
	
	
	
	
	
	//-------------------------------------------------------
	/**
	 * ���ViewPagerҳ���������
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageResId.length;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}
	
	/**
	 * ��ViewPager��ҳ���״̬�����ı�ʱ����
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			// tv_title.setText(titles[position]);
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}
	

	
	@Override
	public void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// ��Activity��ʾ������ÿ�������л�һ��ͼƬ��ʾ
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3, TimeUnit.SECONDS);
		super.onStart();
	}

	@Override
	public void onStop() {
		// ��Activity���ɼ���ʱ��ֹͣ�л�
		scheduledExecutorService.shutdown();
		super.onStop();
	}
	
	/**
	 * �����л�����
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // ͨ��Handler�л�ͼƬ
			}
		}

	}
	//*********************************** ͼƬ�ַ����� ********************
	
	//----------------------------------
	
	
	
	
}
	
	
	

