package com.octavio.ordinary.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.octavio.ordinary.R;
import com.octavio.ordinary.model.LunarCalendar;
import com.octavio.ordinary.widget.BorderText;
import com.octavio.ordinary.widget.BorderTextView;
import com.octavio.ordinary.widget.CalendarView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * about calendar
 * 
 * @author Octavio
 *
 */
public class CalendarFragment extends Fragment implements OnGestureListener {

	private View parentView;
	 
	private static final String Tag="CalendarActivity";
	private LunarCalendar lcCalendar = null;
	private LunarCalendar calendar;
	private ViewFlipper flipper = null;
	private GestureDetector gestureDetector = null;
	private CalendarView calV = null;
	private GridView gridView = null;
	private BorderText topText = null;
	private Drawable draw = null;
	private static int jumpMonth = 0;      //ÿ�λ��������ӻ��ȥһ����,Ĭ��Ϊ0������ʾ��ǰ�£�
	private static int jumpYear = 0;       //������Խһ�꣬�����ӻ��߼�ȥһ��,Ĭ��Ϊ0(����ǰ��)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
//	private ScheduleDAO dao = null;
//	private ScheduleVO scheduleVO;
	private String[] scheduleIDs;
	private  ArrayList<String> scheduleDate;
	private Dialog builder;
//	private ScheduleVO scheduleVO_del;
	private String scheduleitems[];
	//С����item�Ŀؼ�
	private BorderTextView schdule_tip;
	private Button add;
	private Button quit;
	private TextView day_tv;
	private TextView launarDay;
	private ListView listView;
	private TextView weekday;
	private TextView lunarTime;
	private ListView list;
	private String dateInfo;//���gridview��������Ϣ
	private LayoutInflater inflater;
	
	public CalendarFragment() {
		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //��������
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
//    	dao = new ScheduleDAO(getActivity());
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.fragment_calendar, container, false);
		
		
		gestureDetector = new GestureDetector(this);
		flipper = (ViewFlipper) parentView.findViewById(R.id.flipper);
		flipper.removeAllViews();
		calV = new CalendarView(getActivity(), getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		
		addGridView();
		gridView.setAdapter(calV);
		flipper.addView(gridView,0);
		topText = (BorderText) parentView.findViewById(R.id.schedule_toptext);
		addTextToTopTextView(topText);
		
		
		
		
		
		
		return parentView;
	}



	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		int gvFlag = 0;         //ÿ�����gridview��viewflipper��ʱ���ı��
		if (e1.getY() - e2.getY() > 50) {
            //���󻬶�
			addGridView();   //���һ��gridView
			jumpMonth++;     //��һ����
			
			calV = new CalendarView(getActivity(), getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        //flipper.addView(gridView);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView, gvFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_up_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_up_out));
			this.flipper.showNext();
			flipper.removeViewAt(0);
			return true;
		} else if (e1.getY() - e2.getY() < -50) {
            //���һ���
			//���»���
			addGridView();   //���һ��gridView
			jumpMonth--;     //��һ����
			
			calV = new CalendarView(getActivity(), getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        gvFlag++;
	        addTextToTopTextView(topText);
	        flipper.addView(gridView,gvFlag);
	        
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_down_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_down_out));
			this.flipper.showPrevious();
			flipper.removeViewAt(0);
			return true;
		}
		return false;
	}

	
	//
	//���gridview,��ʾ���������
	@SuppressLint("ResourceAsColor")
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		//ȡ����Ļ�Ŀ�Ⱥ͸߶�
		WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth(); 
        int Height = display.getHeight();
        
        Log.d(Tag, "��Ļ�ֱ���=="+"height*weight"+Height+Width);
        
		gridView = new GridView(getActivity());
		gridView.setNumColumns(7);
		gridView.setColumnWidth(46);
		if(Width == 480 && Height == 800){
			gridView.setColumnWidth(69);
		}else if(Width==800&&Height==1280){
			gridView.setColumnWidth(69);
		}
		
		
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // ȥ��gridView�߿�
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
        gridView.setBackgroundResource(R.drawable.gridview_bk2);
        // ��������������ɫ
		gridView.setOnTouchListener(new OnTouchListener() {
            //��gridview�еĴ����¼��ش���gestureDetector
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return CalendarFragment.this.gestureDetector
						.onTouchEvent(event);
			}
		});

		
		/*
		gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView�е�ÿһ��item�ĵ���¼�
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				  //����κ�һ��item���õ����item������(�ų�����������յ�����(�������Ӧ))
				  int startPosition = calV.getStartPositon();
				  int endPosition = calV.getEndPosition();
				  if(startPosition <= position  && position <= endPosition){
					  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //��һ�������
					  //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //��һ�������
	                  String scheduleYear = calV.getShowYear();
	                  String scheduleMonth = calV.getShowMonth();
	                  String week = "";
	                 
	                  Log.i("�ճ���ʷ���", scheduleDay);
	                  
	                  //ͨ�����ڲ�ѯ��һ���Ƿ񱻱�ǣ����������ճ̾Ͳ�ѯ������������ճ���Ϣ
	                  scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear)
	                		  , Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
	                  
	                  //�õ���һ�������ڼ�
	                  switch(position%7){
	                  case 0:
	                	  week = "������";
	                	  break;
	                  case 1:
	                	  week = "����һ";
	                	  break;
	                  case 2:
	                	  week = "���ڶ�";
	                	  break;
	                  case 3:
	                	  week = "������";
	                	  break;
	                  case 4:
	                	  week = "������";
	                	  break;
	                  case 5:
	                	  week = "������";
	                	  break;
	                  case 6:
	                	  week = "������";
	                	  break;
	                  }
					 
	                  scheduleDate = new ArrayList<String>();
	                  scheduleDate.add(scheduleYear);
	                  scheduleDate.add(scheduleMonth);
	                  scheduleDate.add(scheduleDay);
	                  scheduleDate.add(week);
	                  
	                  
                	   LayoutInflater inflater=getActivity().getLayoutInflater();
	              		View linearlayout= inflater.inflate(R.layout.activity_calendar_schedule_detail, null);
	              		 add=(Button)linearlayout.findViewById(R.id.btn_add);
	              		 quit=(Button) linearlayout.findViewById(R.id.btn_back);
	              	 day_tv=(TextView) linearlayout.findViewById(R.id.todayDate);
	              		launarDay=(TextView)linearlayout.findViewById(R.id.tv_launar);
	                  schdule_tip=(com.octavio.diary.calendar.utils.BorderTextView)linearlayout.findViewById(R.id.schdule_tip);
	              	 listView=(ListView)linearlayout.findViewById(R.id.schedulelist1);
	              		//����
	              		 weekday=(TextView)linearlayout.findViewById(R.id.dayofweek);
	              		//ũ������
	              		 lunarTime=(TextView)linearlayout.findViewById(R.id.lunarTime);
	              		list=(ListView)linearlayout.findViewById(R.id.schedulelist1);
	              	
	              	 dateInfo=scheduleYear+"��"+scheduleMonth+"��"+scheduleDay+"��";
	              	//���ũ����Ϣ	
	              	String scheduleLunarDay = getLunarDay(Integer.parseInt(scheduleYear),
	        				Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
	              	
	              	Log.i("LunarDay", scheduleLunarDay);
	              	//����ѡ�е����ڵ�����,���ں�ũ����Ϣ
	              		day_tv.setText(dateInfo);
	              		weekday.setText(week);
	              		addLunarDayInfo(lunarTime);
	              		launarDay.setText( scheduleLunarDay);
	              		
	              		Log.i("scheduleDate", "scheduleDate��������Ϣ��"+scheduleDate);
	              		//����ճ̰�ť
	              		//TableLayout dialog_tab=(TableLayout) linearlayout.findViewById(R.id.dialog_tab);
	              		add.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(builder!=null&&builder.isShowing()){
									builder.dismiss();
									Intent intent = new Intent();
					                  intent.putStringArrayListExtra("scheduleDate", scheduleDate);
//					                  intent.setClass(getActivity(), ScheduleViewAddActivity.class);
					                  startActivity(intent);
								}
							}
						});
	              		//���ذ�ť
	              		quit.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(builder!=null&&builder.isShowing()){
									builder.dismiss();
								}
							}
						});
	                  
	                  //�������ǣ��������Ӧ���ճ���Ϣ�б�
//                  if(scheduleIDs != null && scheduleIDs.length > 0){
//                	  
//                	  
//		              		//list.setAdapter(new MyAdapter());
//		              		View inflate=getLayoutInflater().inflate(R.layout.schedule_detail_item, null);
//                        //ͨ��arraylist�����ݵ�listview��ȥ
//		              		ArrayList<HashMap<String,String>> Data = new ArrayList<HashMap<String, String>>();  
//							ScheduleDAO dao=new ScheduleDAO(CalendarActivity.this);
//							 String time="";
//		                	  String content="";
//	                	  for(int i=0;i<scheduleIDs.length;i++){
//	                	  scheduleVO=dao.getScheduleByID(CalendarActivity.this,Integer.parseInt(scheduleIDs[i]));
//	                	 time="";
//	                	 content="";
//	                	  
//	                	  time=dateInfo+" "+scheduleVO.getTime();
//	                	  content=scheduleVO.getScheduleContent();
//	                		
//	                	 
//	                	 
//	                		  HashMap<String, String> map=new HashMap<String, String>();
//	                		  map.put("date", time);
//	                		  map.put("content", content);
//          	              	  Data.add(map);
//          	              	  
//	                	  }
//	                	 String  from[]={"date","content"};
//	                	  int to[]={R.id.itemTime,R.id.itemContent};
//	                	  
//	                	  SimpleAdapter adapter=new SimpleAdapter(CalendarActivity.this, Data, R.layout.schedule_detail_item, from, to);
//	                	  
//	                	  list.setAdapter(adapter);
//	                	  
//	                	  //���list��item��Ӧ�¼�
////	                	  list.setOnClickListener(CalendarActivity.this);
////	                	  list.setOnLongClickListener(CalendarActivity.this);
//	        
//	                	  
//	                  }else{ //���û�б��λֱ�������Ϊ�����ް��š�
	                 
	                	  
	                	  schdule_tip.setText("���ް���");
	                	  listView.setVisibility(View.INVISIBLE);
	                	  
//		                  Intent intent = new Intent();
//		                  intent.putExtra("top_Time", dateInfo);
//		                  Log.i("calendar", "calendar ifo-->"+dateInfo);
//		                  intent.putStringArrayListExtra("scheduleDate", scheduleDate);
//		                  intent.setClass(CalendarActivity.this,ScheduleDetailsNoDataActivity.class);
//		                  startActivity(intent);
	                  }
	                  
           	   //��dialog����ʽ��ʾ��windows��
            	  builder =	new Dialog(CalendarActivity.this,R.style.FullScreenDialog);
            	  builder.setContentView(linearlayout);
            	  WindowManager windowManager = getWindowManager();
            	  Display display = windowManager.getDefaultDisplay();
            	  WindowManager.LayoutParams lp = builder.getWindow().getAttributes();
            	  lp.width = (int)(display.getWidth()); //���ÿ��
            	  lp.height=display.getHeight();
            	  builder.getWindow().setAttributes(lp); 
            	  builder.setCanceledOnTouchOutside(true);
            	  builder.show();
	                  
                  list.setOnItemClickListener(new OnItemClickListener() {

					@Override
						public void onItemClick(AdapterView<?> adapterview,
								View view, int position, long id) {

							
							Log.i("�ճ�item���", "��"+position+"��item");
							Intent intent=new Intent();
							
							if(view!=null){
							
								HashMap<String, String> map=(HashMap<String, String>) adapterview.getItemAtPosition(position);
								
								ScheduleVO scheduleVO=  (ScheduleVO) view.getTag();//								
								Log.i("scheduleVo", "scheduleVO��ֵ="+scheduleVO);
								
								if(scheduleDate!=null){
									//intent.putStringArrayListExtra("scheduleDate", scheduleDate);
									intent.setClass(CalendarActivity.this,ScheduleInfoDetailActivity.class);
									intent.putStringArrayListExtra("scheduleDate", scheduleDate);
						        intent.putExtra("scheduleVO", scheduleVO);
							        
							        Log.i("scheduleVo", "��intent��ŵ�ֵ"+scheduleVO);
										  startActivity(intent);
									
								}
							}
							
					}
				}); 
                
	                  
	                  //����ɾ��
    
	                  
				  }
			}
		});
		gridView.setLayoutParams(params);
		*/
	}
	
	
	
	
	
	
	/**
	 * �������ڵ������շ�����������
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public String getLunarDay(int year, int month, int day) {
		lcCalendar=new LunarCalendar();
		String lunar = lcCalendar.getLunarDate(year, month, day, true);
		// {������ȡ��������Ӧ����������ʱ������������ڶ�Ӧ����������Ϊ"��һ"���ͱ����ó����·�(��:���£����¡�������)},�����ڴ˾�Ҫ�жϵõ������������Ƿ�Ϊ�·ݣ�������·ݾ�����Ϊ"��һ"
		if (lunar.substring(1, 2).equals("��")) {
			lunar = "��һ";
		}
//		
//		Log.i("lunar", lunar);
//		String lunarDay=lunar.substring(2);
		
		return lunar;
	}
	
	
	
	
	
	//���ũ����Ϣ
	public void addLunarDayInfo(TextView text){
		StringBuffer textDate = new StringBuffer();
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("��").append(calV.getLeapMonth()).append("��")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("��").append("(").append(
				calV.getCyclical()).append("��)");
		text.setText(textDate);
	}
	
	
	
	
	
	
	//��ӻ���ͷ������� �����µ���Ϣ
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		draw = getResources().getDrawable(R.drawable.schedule_title_bg);
		view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowYear()).append("��").append(
				calV.getShowMonth()).append("��").append("\t");
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("��").append(calV.getLeapMonth()).append("��")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("��").append("(").append(
				calV.getCyclical()).append("��)");
		view.setText(textDate);
		view.setTextColor(Color.WHITE);
		view.setTextSize(15.0f);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	
	public boolean onTouchEvent(MotionEvent event) {

		return this.gestureDetector.onTouchEvent(event);
	}
	 public static class ViewHolder {
			TextView itemTime;
			TextView itemContent;
		}
	 
	
	 
	 
	 
	 
}

