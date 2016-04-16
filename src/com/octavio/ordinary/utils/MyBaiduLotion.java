package com.octavio.ordinary.utils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.content.Context;

public class MyBaiduLotion {
	
	Context myContext;
	
	private LocationClient locationClient = null;
	private static final int UPDATE_TIME = 4000;
	private static int LOCATION_COUTNS = 0;
	private boolean isFinish = false;
	public MyBDcoordinate myBDcoordinate = null;
	MyLocation myLocation;
	String strlocation = "";
	
	public MyBaiduLotion(Context context) {
		// TODO Auto-generated constructor stub
		myContext = context;
		myLocation = new MyLocation();
		initLockPst();
		
	}
	
	class MyBDcoordinate{
		double Latitude;
		double Longitude;
	}
	
	 private void initLockPst(){
	    	locationClient = new LocationClient(this.myContext);
	        //���ö�λ����
	        LocationClientOption option = new LocationClientOption();
	        option.setOpenGps(true);		//�Ƿ��GPS
	        option.setCoorType("bd09ll");		//���÷���ֵ���������͡�
	        option.setPriority(LocationClientOption.NetWorkFirst);	//���ö�λ���ȼ�
	        option.setProdName("LocationDemo");	//���ò�Ʒ�����ơ�ǿ�ҽ�����ʹ���Զ���Ĳ�Ʒ�����ƣ����������Ժ�Ϊ���ṩ����Ч׼ȷ�Ķ�λ����
	        option.setScanSpan(UPDATE_TIME);    //���ö�ʱ��λ��ʱ��������λ����
	        locationClient.setLocOption(option);
	        
	        //ע��λ�ü�����
	        locationClient.registerLocationListener(new BDLocationListener() {
				
				@Override
				public void onReceiveLocation(BDLocation location) {
					// TODO Auto-generated method stub
					if(myBDcoordinate != null){
						stopOpetateClient();
						//locationInfoTextView.setText("stop" + LOCATION_COUTNS);
						return;
					}
					if(LOCATION_COUTNS > 5){
						
						stopOpetateClient();
						return;
					}
					if (location == null) {
						LOCATION_COUTNS ++;
						return;
					}
					//location.getLocType();
					//location.getLatitude()
					//location.getLongitude();
					if(location.getLocType() != 161){
						LOCATION_COUTNS ++;
						return;
					}
					myBDcoordinate = new MyBDcoordinate();
					myBDcoordinate.Latitude = location.getLatitude();
					myBDcoordinate.Longitude = location.getLongitude();
					
					
				}
				
				@Override
				public void onReceivePoi(BDLocation location) {
				}
				
			});
	    }
	 
	 private void stopOpetateClient(){
		 locationClient.stop();
		 isFinish = true;
	 }	 
	 
	 private void startOpetateClient(){
		 locationClient.start();
			/*
			 *�����������ֵ���ڵ���1000��ms��ʱ����λSDK�ڲ�ʹ�ö�ʱ��λģʽ��
			 *����requestLocation( )��ÿ���趨��ʱ�䣬��λSDK�ͻ����һ�ζ�λ��
			 *�����λSDK���ݶ�λ���ݷ���λ��û�з����仯���Ͳ��ᷢ����������
			 *������һ�ζ�λ�Ľ�����������λ�øı䣬�ͽ�������������ж�λ���õ��µĶ�λ�����
			 *��ʱ��λʱ������һ��requestLocation���ᶨʱ��������λ�����
			 */
		 isFinish = false;
			locationClient.requestLocation();
	 }
	 
	 public boolean getIsFinish(){//��ȡ��λ�Ƿ���ɻ���ֹ
		 return isFinish;
	 }
	 public void opetateClient(){//��ʼ��ֹͣ��
		 if (locationClient == null) {
				return;
			}
			if (locationClient.isStarted()) {
				stopOpetateClient();
			}else {
				startOpetateClient();
				/*
				 *�����������ֵ���ڵ���1000��ms��ʱ����λSDK�ڲ�ʹ�ö�ʱ��λģʽ��
				 *����requestLocation( )��ÿ���趨��ʱ�䣬��λSDK�ͻ����һ�ζ�λ��
				 *�����λSDK���ݶ�λ���ݷ���λ��û�з����仯���Ͳ��ᷢ����������
				 *������һ�ζ�λ�Ľ�����������λ�øı䣬�ͽ�������������ж�λ���õ��µĶ�λ�����
				 *��ʱ��λʱ������һ��requestLocation���ᶨʱ��������λ�����
				 */
				locationClient.requestLocation();
				
			}
	 }
	
	 /*********************************/
	public double getLatValue(){//γ��
		return myBDcoordinate.Latitude;
	}
	public double getLongValue(){//����
		return myBDcoordinate.Longitude;
	}
	
	 public void desClient(){//�����ڶ�λʱActivity����ʱ����
		 if (locationClient != null && locationClient.isStarted()) {
				locationClient.stop();
				locationClient = null;
			}
	 }
	 
}
