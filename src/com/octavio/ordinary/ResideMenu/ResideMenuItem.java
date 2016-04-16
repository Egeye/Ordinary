package com.octavio.ordinary.ResideMenu;

import com.octavio.ordinary.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResideMenuItem extends LinearLayout {

	/** menu item icon */
	private ImageView iv_icon;
	/** menu item title */
	private TextView tv_title;

	public ResideMenuItem(Context context) {
		super(context);
		initViews(context);
	}

	// public ResideMenuItem(Context context, int icon, int title) {
	// super(context);
	// initViews(context);
	// iv_icon.setImageResource(icon);
	// tv_title.setText(title);
	// tv_title.setTextColor(Color.GREEN);
	// }

	public ResideMenuItem(Context context, int icon, String title) {
		super(context);
		initViews(context);
		iv_icon.setImageResource(icon);
		tv_title.setText(title);
		// tv_title.setTextColor(0xff1E90FF);
		// tv_title.setTextColor(Color.BLACK);

		tv_title.setTextColor(0xff191970);
	}

	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.residemenu_item, this);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}

	/**
	 * set the icon color;
	 *
	 * @param icon
	 */
	public void setIcon(int icon) {
		iv_icon.setImageResource(icon);
	}

	/**
	 * set the title with resource ;
	 * 
	 * @param title
	 */
	public void setTitle(int title) {
		tv_title.setText(title);
	}

	/**
	 * set the title with string;
	 *
	 * @param title
	 */
	public void setTitle(String title) {
		tv_title.setText(title);
	}
}
