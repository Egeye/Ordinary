package com.octavio.ordinary.fragment;

import java.util.Calendar;
import java.util.TimeZone;

import com.octavio.ordinary.R;
import com.octavio.ordinary.db.BilldbHelper;
import com.octavio.ordinary.utils.RegisterActivityAuto;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class RegisterFragment extends Fragment implements OnClickListener {

	private View parentView;

	///
	EditText edittext_acctitem, EditTextDESC, Fee;
	TextView mDate;
	TextView mTime;
	static final int RG_REQUEST = 0;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	Spinner s1;
	Button BtnDate, BtnTime;
	Button BtnCancel, BtnSave;

	BilldbHelper billdb;

	int acctitemid = -1;
	////

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		parentView = inflater.inflate(R.layout.fragment_register, container, false);

		edittext_acctitem = (EditText) parentView.findViewById(R.id.edittext_acctitem); // 账目类别
		edittext_acctitem.setOnClickListener(this);

		EditTextDESC = (EditText) parentView.findViewById(R.id.EditTextDESC); // memo
		Fee = (EditText) parentView.findViewById(R.id.Fee); // money

		BtnDate = (Button) parentView.findViewById(R.id.BtnDate); // button date
		BtnDate.setOnClickListener(this);

		BtnTime = (Button) parentView.findViewById(R.id.BtnTime); // button time
		BtnTime.setOnClickListener(this);

		BtnCancel = (Button) parentView.findViewById(R.id.BtnCancel); // button
																		// clear
		BtnCancel.setOnClickListener(this);

		BtnSave = (Button) parentView.findViewById(R.id.BtnSave); // button save
		BtnSave.setOnClickListener(this);

		mDate = (TextView) parentView.findViewById(R.id.vdate); // place to show
																// date
		mTime = (TextView) parentView.findViewById(R.id.vtime); // place to show
																// time
		initTime();
		setDatetime();

		initApp();

		billdb = new BilldbHelper(getActivity());

		s1 = (Spinner) parentView.findViewById(R.id.Spinner01); // 账目分类
		String[] from = new String[] { "caption" };
		int[] to = new int[] { android.R.id.text1 };
		Cursor cur = billdb.getUserid();
		SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, cur,
				from, to);
		// 账目分类数据注入

		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s1.setAdapter(mAdapter);

		return parentView;
	}

	@Override
	public void onClick(View v) {

		if (v.equals(edittext_acctitem)) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), RegisterActivityAuto.class);
			startActivityForResult(intent, RG_REQUEST);
		} else if (v.equals(BtnTime)) {
			showDialog2("请选择年月:", "");
		} else if (v.equals(BtnDate)) {
			showDialog("请选择时间:", "");
		} else if (v.equals(BtnCancel)) {
			cancel();
		} else if (v.equals(BtnSave)) {
			save();
		}
	}
	
	private void showDialog(String title, String text) {
		final DatePickerDialog dia = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,mDateSetListener, mYear, mMonth - 1, mDay);
		dia.show();
	}
	
	private void showDialog2(String title, String text) {
		final TimePickerDialog dia2 =new TimePickerDialog(getActivity(),AlertDialog.THEME_HOLO_LIGHT,mTimeSetListener, mHour, mMinute, false);
		dia2.show();
	}
	

	private void initTime() {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}

	private void setDatetime() {
		mDate.setText(mYear + "-" + mMonth + "-" + mDay);
		mTime.setText(pad(mHour) + ":" + pad(mMinute));
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private void cancel() {
		Log.v("cola", "u put cancel btn");
		edittext_acctitem.setText("");
		Fee.setText("");
		acctitemid = -1;
		initTime();
		setDatetime();
		EditTextDESC.setText("");
	}

	private void save() {
		Log.v("cola", "u put save btn");
		if (acctitemid == -1) {
			new AlertDialog.Builder(getActivity()).setMessage("请首先选择账目.").show();
			return;
		}
		int fee = 0;
		String s = Fee.getText().toString();
		int pos = s.indexOf(".");
		// Log.v("cola","i="+(s.length()-pos));
		if (pos > 0) {
			if (s.length() - pos < 3) {
				s = s + "0";
			}
			fee = Integer.parseInt(s.substring(0, pos) + s.substring(pos + 1, pos + 3));
		} else {
			fee = Integer.parseInt(s) * 100;

		}
		Log.v("cola", "u put save btn");
		if (billdb.Bills_save(acctitemid, fee, (int) s1.getSelectedItemId(), ((TextView) mDate).getText().toString(),
				((TextView) mTime).getText().toString(), EditTextDESC.getText().toString())) {
			Toast.makeText(getActivity(), "保存成功.", Toast.LENGTH_SHORT).show();
			cancel();
		} else {
			Toast.makeText(getActivity(), "保存失败,请检查数据.", Toast.LENGTH_SHORT).show();
		}
	}

	public void initApp() {
		BilldbHelper billdb = new BilldbHelper(getActivity());
		billdb.FirstStart();
		billdb.close();

	}

	// ------------------------
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RG_REQUEST) {
			if (resultCode == getActivity().RESULT_CANCELED) {
				// setTitle("Canceled...");
			} else if (resultCode == getActivity().RESULT_OK) {
				// setTitle((String)data.getCharSequenceExtra("DataKey"));
				edittext_acctitem.setText((String) data.getCharSequenceExtra("name"));
				acctitemid = Integer.parseInt((String) data.getCharSequenceExtra("id"));
				Log.v("cola", "get acctitemid=" + acctitemid);

			}
		}
	}

	
	
	
//	
//	@Override
//	public Dialog onCreateDialog(int id) {
//		switch (id) {
//		case 1:
//			return new TimePickerDialog(getActivity(), mTimeSetListener, mHour, mMinute, false);
//		case 2:
//			return new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth - 1, mDay);
//		}
//		return null;
//	}
	

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			setDatetime();
		}
	};

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;

			setDatetime();
		}
	};

}

