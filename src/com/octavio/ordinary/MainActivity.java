package com.octavio.ordinary;


import com.octavio.ordinary.ResideMenu.ResideMenu;
import com.octavio.ordinary.ResideMenu.ResideMenuItem;
import com.octavio.ordinary.fragment.CalendarFragment;
import com.octavio.ordinary.fragment.ConvertFragment;
import com.octavio.ordinary.fragment.DiaryFragment;
import com.octavio.ordinary.fragment.HomeFragment;
import com.octavio.ordinary.fragment.MapFragment;
import com.octavio.ordinary.fragment.RecordFragment;
import com.octavio.ordinary.fragment.RegisterFragment;
import com.octavio.ordinary.fragment.WriteFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

/**
 * The main Activity
 * 
 * @author Octavio
 *
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

	private ResideMenu resideMenu;

	private ResideMenuItem itemDiary; // 看日记
	private ResideMenuItem itemWrite; // 写日记

	private ResideMenuItem itemCalendar; // 看日历
	private ResideMenuItem itemConvert; // 查农历

	private ResideMenuItem itemRecord; // 看纪录
	private ResideMenuItem itemRegister; // 做记录

	private ResideMenuItem itemHome; // 听音乐/定时
	private ResideMenuItem itemMap; // 看地图

	private long mExitTime;

	/**
	 * by octavio 防止误触返回键，连按两次返回键退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				// Object mHelperUtils;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setUpMenu();
		if (savedInstanceState == null) {
			changeFragment(new HomeFragment());
			// 这是设置哪个碎片作为首次显示界面
		}
		// overridePendingTransition(android.R.anim.fade_in,
		// android.R.anim.fade_out);

	}

	private void setUpMenu() {

		// attach to current activity;
		resideMenu = new ResideMenu(this);
		resideMenu.setUse3D(true);
		// resideMenu.setBackground(R.drawable.menu_background_or_3);
		resideMenu.setBackground(R.drawable.logon2);
		resideMenu.attachToActivity(this);
		resideMenu.setMenuListener(menuListener);
		// valid scale factor is between 0.0f and 1.0f. leftmenu'width is
		// 150dip.
		resideMenu.setScaleValue(0.6f);

		// create menu items;
		itemHome = new ResideMenuItem(this, R.drawable.icon_home, "『日常』");
		itemDiary = new ResideMenuItem(this, R.drawable.icon_diary, "『日记』");
		itemCalendar = new ResideMenuItem(this, R.drawable.icon_calendar, "『日历』");
		itemRecord = new ResideMenuItem(this, R.drawable.icon_record, "『纪录』");

		itemMap =  new ResideMenuItem(this, R.drawable.icon_map, "『看地图』");
		itemWrite = new ResideMenuItem(this, R.drawable.icon_write, "『写日记』");
		itemConvert = new ResideMenuItem(this, R.drawable.icon_convert, "『查农历』");
		itemRegister = new ResideMenuItem(this, R.drawable.icon_register, "『记账目』");
		

		itemHome.setOnClickListener(this);
		itemDiary.setOnClickListener(this);
		itemCalendar.setOnClickListener(this);
		itemRecord.setOnClickListener(this);
		
		itemWrite.setOnClickListener(this);
		itemRegister.setOnClickListener(this);
		itemMap.setOnClickListener(this);
		itemConvert.setOnClickListener(this);
		//

		resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemDiary, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemRecord, ResideMenu.DIRECTION_LEFT);

		resideMenu.addMenuItem(itemMap, ResideMenu.DIRECTION_RIGHT);
		resideMenu.addMenuItem(itemWrite, ResideMenu.DIRECTION_RIGHT);
		resideMenu.addMenuItem(itemRegister, ResideMenu.DIRECTION_RIGHT);
		resideMenu.addMenuItem(itemConvert, ResideMenu.DIRECTION_RIGHT);

		// You can disable a direction by setting ->
		// resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

		findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
			}
		});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View view) {

		if (view == itemDiary) {
			changeFragment(new DiaryFragment());
			// } else if (view == itemMood) {
			// changeFragment(new MoodFragment());
		} else if (view == itemCalendar) {
			changeFragment(new CalendarFragment());
		} else if (view == itemRecord) {
			changeFragment(new RecordFragment());
		} else if (view == itemWrite) {
			changeFragment(new WriteFragment());
		} else if (view == itemHome) {
			changeFragment(new HomeFragment());
		} else if (view == itemRegister) {
			changeFragment(new RegisterFragment());
		} else if (view == itemConvert) {
			changeFragment(new ConvertFragment());
		} else if(view == itemMap) {
			changeFragment(new MapFragment());
		}

		resideMenu.closeMenu();
	}

	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
		@Override
		public void openMenu() {
			// Toast.makeText(mContext, "Menu is opened!",
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void closeMenu() {
			// Toast.makeText(mContext, "Menu is closed!",
			// Toast.LENGTH_SHORT).show();
		}
	};

	private void changeFragment(Fragment targetFragment) {
		resideMenu.clearIgnoredViewList();
		getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
	}

	// What good method is to access resideMenu��
	public ResideMenu getResideMenu() {
		return resideMenu;
	}

}

