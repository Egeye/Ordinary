package com.octavio.ordinary.fragment;

import java.util.Calendar;
import java.util.TimeZone;

import com.octavio.ordinary.R;
import com.octavio.ordinary.db.BilldbHelper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * About Record
 * 
 * @author Octavio
 *
 */
public class RecordFragment extends Fragment implements OnItemClickListener, OnClickListener {

	private View parentView;

	BilldbHelper billdb;
	View sv;
	EditText edit;
	AbsoluteLayout alayout;
	int a = 10, b = 10;
	GridView grd;

	TextView total, tvTime;
	Button btnDate;

	DatePicker dp;
	Button okbtn;
	ListView lv;

	private int mYear;
	private int mMonth;
	private int mDay;

	String today, time;
	String[] from;
	int[] to;

	SimpleCursorAdapter mAdapter;
	Cursor cur;
	int _id;

	protected GridView listHands = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		parentView = inflater.inflate(R.layout.fragment_record, container, false);

		showCost();
		
		btnDate =(Button) parentView.findViewById(R.id.btn_date);
		btnDate.setOnClickListener(this);

		return parentView;

	}

	private void showCost() {
		billdb = new BilldbHelper(getActivity());

		lv = (ListView) parentView.findViewById(R.id.listview);

		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);

		today = mYear + "-" + mMonth;

		time = mMonth + "月";

		tvTime = (TextView) parentView.findViewById(R.id.tv_titleforshow);
		tvTime.setText(time);

		cur = billdb.getBills(today);
		// from = new String[] { "rowid", "name", "fee", "sdate", "desc" };
		// to = new int[] { R.id.item1, R.id.item2, R.id.item3,
		// R.id.item4,R.id.item5 };
		from = new String[] { "name", "fee", "sdate", "desc" };
		to = new int[] { R.id.item2, R.id.item3, R.id.item4, R.id.item5 };
		mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.fragment_record_item, cur, from, to);

		lv.setAdapter(mAdapter);

		total = (TextView) parentView.findViewById(R.id.totalitem);
		total.setText(billdb.getBillsTotal(today));

		// lv.setOnItemLongClickListener(this);
		lv.setOnItemClickListener(this);

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;
			// today = mYear + "-" + mMonth;
			today = mYear + "-" + mMonth;
			
			time = mMonth + "月";

			tvTime = (TextView) parentView.findViewById(R.id.tv_titleforshow);
			tvTime.setText(time);


			// setTitle("ColaBox-账单明细(" + today + ")");
			cur = billdb.getBills(today);
			total = (TextView) parentView.findViewById(R.id.totalitem);
			total.setText(billdb.getBillsTotal(today));
			mAdapter.changeCursor(cur);
			// lv.setAdapter(mAdapter);
			((SimpleCursorAdapter) mAdapter).notifyDataSetChanged();
		}
	};

	
	
	private void showDialog(String title, String text) {
		final DatePickerDialog dia = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,mDateSetListener, mYear, mMonth - 1, mDay);

		dia.show();
	}
	
	@Override
	public void onClick(View v) {
		
		if(v.equals(btnDate)){
			showDialog("请选择年月:", "");
		}

	}

	// @Override
	// public boolean onItemLongClick(AdapterView<?> parent, View view, int
	// position, long id) {
	//
	// _id=(int)id;
	// new
	// AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("确定删除该明细?").setIcon(R.drawable.quit).setPositiveButton("确定",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// //Log.v("",""+_id);
	// billdb.delBills(_id);
	// mAdapter.changeCursor(cur);
	// ((SimpleCursorAdapter) mAdapter).notifyDataSetChanged();
	// // finish();
	// }
	// }).setNegativeButton("取消",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int whichButton) {
	// }
	// }).show();
	//
	// return true;
	// }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(new String[] { "删除" }, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {

					billdb.delBills((int) id);
					showCost();

					dialog.dismiss();

				}

			}

		});
		builder.create().show();

	}



}

