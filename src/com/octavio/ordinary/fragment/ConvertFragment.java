package com.octavio.ordinary.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.octavio.ordinary.R;
import com.octavio.ordinary.model.LunarCalendar;
import com.octavio.ordinary.widget.BorderTextView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

public class ConvertFragment  extends Fragment {

	private View parentView;

	// -------------
	private LunarCalendar lc = null;
	private BorderTextView convertDate = null;
	private BorderTextView convertBT = null;
	private TextView lunarDate = null;

	private int year_c;
	private int month_c;
	private int day_c;

	public ConvertFragment() {
		lc = new LunarCalendar();
	}
	// ---

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		parentView = inflater.inflate(R.layout.fragment_calendar_convert, container, false);

		// --
		convertDate = (BorderTextView) parentView.findViewById(R.id.convertDate);
		convertBT = (BorderTextView) parentView.findViewById(R.id.convert);
		lunarDate = (TextView) parentView.findViewById(R.id.convertResult);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		String currentDate = sdf.format(date); // 当期日期
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
		convertDate.setText(year_c + "年" + month_c + "月" + day_c);

		convertDate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,null).show();

				new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,new OnDateSetListener() {

					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

						if (year < 1901 || year > 2049) {
							// 不在查询范围内
							new AlertDialog.Builder(getActivity()).setTitle("你选择的日期不在操作的范围内")
									.setMessage("跳转日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
						} else {
							year_c = year;
							month_c = monthOfYear + 1;
							day_c = dayOfMonth;
							convertDate.setText(year_c + "年" + month_c + "月" + day_c + "日");
						}
					}
				}, year_c, month_c - 1, day_c).show();
			}
		});

		//
		convertBT.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				String lunarDay = getLunarDay(year_c, month_c, day_c);
				String lunarYear = String.valueOf(lc.getYear());
				String lunarMonth = lc.getLunarMonth();

				lunarDate.setText(lunarYear + "年" + lunarMonth + lunarDay);
			}
		});

		return parentView;
	}

	
	/**
	 * 根据日期的年月日返回阴历日期
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public String getLunarDay(int year, int month, int day) {
		String lunarDay = lc.getLunarDate(year, month, day, true);
		// {由于在取得阳历对应的阴历日期时，如果阳历日期对应的阴历日期为"初一"，就被设置成了月份(如:四月，五月。。。等)},所以在此就要判断得到的阴历日期是否为月份，如果是月份就设置为"初一"
		if (lunarDay.substring(1, 2).equals("月")) {
			lunarDay = "初一";
		}
		return lunarDay;
	}
	
	
}
