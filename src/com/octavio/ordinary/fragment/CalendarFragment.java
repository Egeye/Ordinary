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
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
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
	//小布局item的控件
	private BorderTextView schdule_tip;
	private Button add;
	private Button quit;
	private TextView day_tv;
	private TextView launarDay;
	private ListView listView;
	private TextView weekday;
	private TextView lunarTime;
	private ListView list;
	private String dateInfo;//点击gridview的日期信息
	private LayoutInflater inflater;
	
	public CalendarFragment() {
		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //当期日期
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
		int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
		if (e1.getY() - e2.getY() > 50) {
            //像左滑动
			addGridView();   //添加一个gridView
			jumpMonth++;     //下一个月
			
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
            //向右滑动
			//向下滑动
			addGridView();   //添加一个gridView
			jumpMonth--;     //上一个月
			
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
	//添加gridview,显示具体的日期
	@SuppressLint("ResourceAsColor")
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		//取得屏幕的宽度和高度
		WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth(); 
        int Height = display.getHeight();
        
        Log.d(Tag, "屏幕分辨率=="+"height*weight"+Height+Width);
        
		gridView = new GridView(getActivity());
		gridView.setNumColumns(7);
		gridView.setColumnWidth(46);
		if(Width == 480 && Height == 800){
			gridView.setColumnWidth(69);
		}else if(Width==800&&Height==1280){
			gridView.setColumnWidth(69);
		}
		
		
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除gridView边框
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
        gridView.setBackgroundResource(R.drawable.gridview_bk2);
        // 设置日历背景颜色
		gridView.setOnTouchListener(new OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return CalendarFragment.this.gestureDetector
						.onTouchEvent(event);
			}
		});

		
		/*
		gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView中的每一个item的点击事件
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				  //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				  int startPosition = calV.getStartPositon();
				  int endPosition = calV.getEndPosition();
				  if(startPosition <= position  && position <= endPosition){
					  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
					  //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
	                  String scheduleYear = calV.getShowYear();
	                  String scheduleMonth = calV.getShowMonth();
	                  String week = "";
	                 
	                  Log.i("日程历史浏览", scheduleDay);
	                  
	                  //通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信息
	                  scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear)
	                		  , Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
	                  
	                  //得到这一天是星期几
	                  switch(position%7){
	                  case 0:
	                	  week = "星期日";
	                	  break;
	                  case 1:
	                	  week = "星期一";
	                	  break;
	                  case 2:
	                	  week = "星期二";
	                	  break;
	                  case 3:
	                	  week = "星期三";
	                	  break;
	                  case 4:
	                	  week = "星期四";
	                	  break;
	                  case 5:
	                	  week = "星期五";
	                	  break;
	                  case 6:
	                	  week = "星期六";
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
	              		//星期
	              		 weekday=(TextView)linearlayout.findViewById(R.id.dayofweek);
	              		//农历日期
	              		 lunarTime=(TextView)linearlayout.findViewById(R.id.lunarTime);
	              		list=(ListView)linearlayout.findViewById(R.id.schedulelist1);
	              	
	              	 dateInfo=scheduleYear+"年"+scheduleMonth+"月"+scheduleDay+"日";
	              	//添加农历信息	
	              	String scheduleLunarDay = getLunarDay(Integer.parseInt(scheduleYear),
	        				Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
	              	
	              	Log.i("LunarDay", scheduleLunarDay);
	              	//设置选中的日期的阳历,星期和农历信息
	              		day_tv.setText(dateInfo);
	              		weekday.setText(week);
	              		addLunarDayInfo(lunarTime);
	              		launarDay.setText( scheduleLunarDay);
	              		
	              		Log.i("scheduleDate", "scheduleDate的所有信息："+scheduleDate);
	              		//添加日程按钮
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
	              		//返回按钮
	              		quit.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(builder!=null&&builder.isShowing()){
									builder.dismiss();
								}
							}
						});
	                  
	                  //如果被标记，则加载相应的日程信息列表
//                  if(scheduleIDs != null && scheduleIDs.length > 0){
//                	  
//                	  
//		              		//list.setAdapter(new MyAdapter());
//		              		View inflate=getLayoutInflater().inflate(R.layout.schedule_detail_item, null);
//                        //通过arraylist绑定数据导listview中去
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
//	                	  //点击list的item相应事件
////	                	  list.setOnClickListener(CalendarActivity.this);
////	                	  list.setOnLongClickListener(CalendarActivity.this);
//	        
//	                	  
//	                  }else{ //如果没有标记位直接则跟换为“暂无安排”
	                 
	                	  
	                	  schdule_tip.setText("暂无安排");
	                	  listView.setVisibility(View.INVISIBLE);
	                	  
//		                  Intent intent = new Intent();
//		                  intent.putExtra("top_Time", dateInfo);
//		                  Log.i("calendar", "calendar ifo-->"+dateInfo);
//		                  intent.putStringArrayListExtra("scheduleDate", scheduleDate);
//		                  intent.setClass(CalendarActivity.this,ScheduleDetailsNoDataActivity.class);
//		                  startActivity(intent);
	                  }
	                  
           	   //以dialog的形式显示到windows上
            	  builder =	new Dialog(CalendarActivity.this,R.style.FullScreenDialog);
            	  builder.setContentView(linearlayout);
            	  WindowManager windowManager = getWindowManager();
            	  Display display = windowManager.getDefaultDisplay();
            	  WindowManager.LayoutParams lp = builder.getWindow().getAttributes();
            	  lp.width = (int)(display.getWidth()); //设置宽度
            	  lp.height=display.getHeight();
            	  builder.getWindow().setAttributes(lp); 
            	  builder.setCanceledOnTouchOutside(true);
            	  builder.show();
	                  
                  list.setOnItemClickListener(new OnItemClickListener() {

					@Override
						public void onItemClick(AdapterView<?> adapterview,
								View view, int position, long id) {

							
							Log.i("日程item点击", "第"+position+"个item");
							Intent intent=new Intent();
							
							if(view!=null){
							
								HashMap<String, String> map=(HashMap<String, String>) adapterview.getItemAtPosition(position);
								
								ScheduleVO scheduleVO=  (ScheduleVO) view.getTag();//								
								Log.i("scheduleVo", "scheduleVO的值="+scheduleVO);
								
								if(scheduleDate!=null){
									//intent.putStringArrayListExtra("scheduleDate", scheduleDate);
									intent.setClass(CalendarActivity.this,ScheduleInfoDetailActivity.class);
									intent.putStringArrayListExtra("scheduleDate", scheduleDate);
						        intent.putExtra("scheduleVO", scheduleVO);
							        
							        Log.i("scheduleVo", "往intent存放的值"+scheduleVO);
										  startActivity(intent);
									
								}
							}
							
					}
				}); 
                
	                  
	                  //长按删除
    
	                  
				  }
			}
		});
		gridView.setLayoutParams(params);
		*/
	}
	
	
	
	
	
	
	/**
	 * 根据日期的年月日返回阴历日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public String getLunarDay(int year, int month, int day) {
		lcCalendar=new LunarCalendar();
		String lunar = lcCalendar.getLunarDate(year, month, day, true);
		// {由于在取得阳历对应的阴历日期时，如果阳历日期对应的阴历日期为"初一"，就被设置成了月份(如:四月，五月。。。等)},所以在此就要判断得到的阴历日期是否为月份，如果是月份就设置为"初一"
		if (lunar.substring(1, 2).equals("月")) {
			lunar = "初一";
		}
//		
//		Log.i("lunar", lunar);
//		String lunarDay=lunar.substring(2);
		
		return lunar;
	}
	
	
	
	
	
	//添加农历信息
	public void addLunarDayInfo(TextView text){
		StringBuffer textDate = new StringBuffer();
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("闰").append(calV.getLeapMonth()).append("月")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("年").append("(").append(
				calV.getCyclical()).append("年)");
		text.setText(textDate);
	}
	
	
	
	
	
	
	//添加画板头部的年份 闰哪月等信息
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		draw = getResources().getDrawable(R.drawable.schedule_title_bg);
		view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowYear()).append("年").append(
				calV.getShowMonth()).append("月").append("\t");
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("闰").append(calV.getLeapMonth()).append("月")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("年").append("(").append(
				calV.getCyclical()).append("年)");
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

