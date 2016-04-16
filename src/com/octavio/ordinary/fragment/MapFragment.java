package com.octavio.ordinary.fragment;


import com.octavio.ordinary.MapActivity;
import com.octavio.ordinary.R;
import com.octavio.ordinary.utils.MyBaiduLotion;
import com.octavio.ordinary.utils.MyLocation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MapFragment extends Fragment {

	private View parentView;
	private Button btnMyLocation;
	private TextView tvMyLocation;
	
	MyBaiduLotion myLotion;
	MyLocation myLocation;
	String strlocation = "";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		parentView =inflater.inflate(R.layout.fragment_map, container, false);
		
		Button btnMap = (Button) parentView.findViewById(R.id.btn_map);
		btnMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),MapActivity.class);
				startActivity(i);
			}
		});
		
		tvMyLocation = (TextView) parentView.findViewById(R.id.tv_location_map);
		btnMyLocation = (Button) parentView.findViewById(R.id.btn_locate_map);
		btnMyLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				myLotion = new MyBaiduLotion(getActivity());
				Toast.makeText(getActivity(), "正在定位……", 1).show();
				myLocation = new MyLocation();
				myLotion.opetateClient();
				new LocationTHread().start();
			}
		});
		
		return parentView;
	}

	class LocationTHread extends Thread{

		@Override
		public void run() {
			super.run();
			if(myLotion != null)
			while(!myLotion.getIsFinish()){
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(myLotion.myBDcoordinate != null){
				strlocation =  myLocation.getAddress(myLotion.getLatValue() +"", myLotion.getLongValue() + "");
				myHandler.sendEmptyMessage(1);
			}
			
		}
		
	}
	
	Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tvMyLocation.setText(strlocation);
		}
		
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//myLotion.desClient();
	}
	
}
