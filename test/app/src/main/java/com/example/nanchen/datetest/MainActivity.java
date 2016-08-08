package com.example.nanchen.datetest;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nanchen.datetest.adapter.NumericWheelAdapter;
import com.example.nanchen.datetest.widget.WheelView;

import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends Activity{
	private LayoutInflater inflater = null;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private WheelView hour;
	private WheelView mins;
	
	PopupWindow menuWindow;
	
	Button tv_time,tv_date,popBtn;
	private Button btn_edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		tv_time=(Button) findViewById(R.id.tv_time);//时间选择器
		tv_date=(Button) findViewById(R.id.tv_date);//日期选择器

		popBtn = (Button) findViewById(R.id.tv_date_time);

		btn_edit = (Button) findViewById(R.id.tv_edit);
		btn_edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showPopwindow(getEditText());
			}
		});

		tv_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				showPopwindow(getTimePick());//弹出时间选择器
				showTimeDialog();
			}
		});
		tv_date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				showPopwindow(getDataPick());//弹出日期选择器
				showDateDialog();
			}
		});

		popBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
//				showMyNewDate(getDateAndTime());
				showDateAndTime();
			}
		});
	}



	/**
	 * 初始化popupWindow
	 * @param view
	 */
	private void showPopwindow(View view) {
		menuWindow = new PopupWindow(view,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		menuWindow.setFocusable(true);
		menuWindow.setBackgroundDrawable(new BitmapDrawable());
		menuWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		menuWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				menuWindow=null;
			}
		});
	}

	private View getEditText() {
		View view = inflater.inflate(R.layout.edit_layout,null);
		final EditText editText = (EditText) view.findViewById(R.id.editText);
		InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		manager.toggleSoftInput(0,InputMethodManager.HIDE_IMPLICIT_ONLY);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(MainActivity.this,editText.getText().toString(),Toast.LENGTH_SHORT).show();
				menuWindow.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				menuWindow.dismiss();
			}
		});
		return view;
	}

	/**
	 * 
	 * @return
	 */
	private View getTimePick() {
		View view = inflater.inflate(R.layout.time_picker_layout, null);
		hour = (WheelView) view.findViewById(R.id.hour);
		initHour();
		mins = (WheelView) view.findViewById(R.id.mins);
		initMins();
		// 设置当前时间
		hour.setCurrentItem(8);
		mins.setCurrentItem(30);


		hour.setVisibleItems(7);
		mins.setVisibleItems(7);
		
		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = hour.getCurrentItem() + ":"+ mins.getCurrentItem();
				Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
				menuWindow.dismiss();
			}
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuWindow.dismiss();
			}
		});
		
		return view;
	}

	/**
	 * 
	 * @return
	 */
	private View getDataPick() {
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		final View view = inflater.inflate(R.layout.datepicker_layout, null);
		
		year = (WheelView) view.findViewById(R.id.year);
		initYear();
		month = (WheelView) view.findViewById(R.id.month);
		initMonth();
		day = (WheelView) view.findViewById(R.id.day);
		initDay(curYear,curMonth);

		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		year.setVisibleItems(7);
		month.setVisibleItems(7);
		day.setVisibleItems(7);
		
		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = (year.getCurrentItem()+1950) + "-"+ (month.getCurrentItem()+1)+"-"+(day.getCurrentItem());
				Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
				menuWindow.dismiss();
			}
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuWindow.dismiss();
			}
		});
		return view;
	}



	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}
	/**
	 * 初始化年
	 */
	private void initYear() {
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,1950, 2050);
		numericWheelAdapter.setLabel(" 年");
		//		numericWheelAdapter.setTextSize(15);  设置字体大小
		year.setViewAdapter(numericWheelAdapter);
		year.setCyclic(true);
	}

	/**
	 * 初始化月
	 */
	private void initMonth() {
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,1, 12, "%02d");
		numericWheelAdapter.setLabel(" 月");
		//		numericWheelAdapter.setTextSize(15);  设置字体大小
		month.setViewAdapter(numericWheelAdapter);
		month.setCyclic(true);
	}

	/**
	 * 初始化天
	 */
	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter=new NumericWheelAdapter(this,1, getDay(arg1, arg2), "%02d");
		numericWheelAdapter.setLabel(" 日");
		//		numericWheelAdapter.setTextSize(15);  设置字体大小
		day.setViewAdapter(numericWheelAdapter);
		day.setCyclic(true);
	}

	/**
	 * 初始化时
	 */
	private void initHour() {
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,0, 23, "%02d");
		numericWheelAdapter.setLabel(" 时");
		//		numericWheelAdapter.setTextSize(15);  设置字体大小
		hour.setViewAdapter(numericWheelAdapter);
		hour.setCyclic(true);
	}

	/**
	 * 初始化分
	 */
	private void initMins() {
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,0, 59, "%02d");
		numericWheelAdapter.setLabel(" 分");
//		numericWheelAdapter.setTextSize(15);  设置字体大小
		mins.setViewAdapter(numericWheelAdapter);
		mins.setCyclic(true);
	}


	/**
	 * 显示全部日期
	 */
	private void showDateAndTime(){
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		int curHour = c.get(Calendar.HOUR_OF_DAY);
		int curMin = c.get(Calendar.MINUTE);


		final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
				.create();
		dialog.show();
		Window window = dialog.getWindow();
		// 设置布局
		window.setContentView(R.layout.date_time_picker_layout);
		// 设置宽高
		window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置弹出的动画效果
		window.setWindowAnimations(R.style.AnimBottom);

		year = (WheelView) window.findViewById(R.id.new_year);
		initYear();
		month = (WheelView) window.findViewById(R.id.new_month);
		initMonth();
		day = (WheelView) window.findViewById(R.id.new_day);
		initDay(curYear,curMonth);
		hour = (WheelView) window.findViewById(R.id.new_hour);
		initHour();
		mins = (WheelView) window.findViewById(R.id.new_mins);
		initMins();

        // 设置当前时间
		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		hour.setCurrentItem(curHour);
		mins.setCurrentItem(curMin);

		month.setVisibleItems(7);
		day.setVisibleItems(7);
		hour.setVisibleItems(7);
		mins.setVisibleItems(7);

		// 设置监听
		TextView ok = (TextView) window.findViewById(R.id.set);
		TextView cancel = (TextView) window.findViewById(R.id.cancel);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String time = String.format(Locale.CHINA,"%04d年%02d月%02d日 %02d时%02d分",year.getCurrentItem()+1950,
						month.getCurrentItem()+1,day.getCurrentItem()+1,hour.getCurrentItem(),mins.getCurrentItem());
				Toast.makeText(MainActivity.this, time, Toast.LENGTH_LONG).show();
				dialog.cancel();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		LinearLayout cancelLayout = (LinearLayout) window.findViewById(R.id.view_none);
		cancelLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				dialog.cancel();
				return false;
			}
		});
	}


	/**
	 * 显示时间
	 */
	private void showTimeDialog(){
		final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
				.create();
		dialog.show();
		Window window = dialog.getWindow();
		// 设置布局
		window.setContentView(R.layout.time_picker_layout);
		// 设置宽高
		window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置弹出的动画效果
		window.setWindowAnimations(R.style.AnimBottom);

		Calendar c = Calendar.getInstance();
		int curHour = c.get(Calendar.HOUR_OF_DAY);
		int curMin = c.get(Calendar.MINUTE);


		hour = (WheelView) window.findViewById(R.id.hour);
		initHour();
		mins = (WheelView) window.findViewById(R.id.mins);
		initMins();
		// 设置当前时间
		hour.setCurrentItem(curHour);
		mins.setCurrentItem(curMin);


		hour.setVisibleItems(7);
		mins.setVisibleItems(7);

		// 设置监听
		Button ok = (Button) window.findViewById(R.id.set);
		Button cancel = (Button) window.findViewById(R.id.cancel);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = String.format(Locale.CHINA,"%2d:%2d",hour.getCurrentItem(), mins.getCurrentItem());
				Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
				dialog.cancel();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		LinearLayout cancelLayout = (LinearLayout) window.findViewById(R.id.view_none);
		cancelLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				dialog.cancel();
				return false;
			}
		});
	}


	/**
	 * 显示日期
	 */
	private void showDateDialog() {
		final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
				.create();
		dialog.show();
		Window window = dialog.getWindow();
		// 设置布局
		window.setContentView(R.layout.datepicker_layout);
		// 设置宽高
		window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置弹出的动画效果
		window.setWindowAnimations(R.style.AnimBottom);


		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		year = (WheelView) window.findViewById(R.id.year);
		initYear();
		month = (WheelView) window.findViewById(R.id.month);
		initMonth();
		day = (WheelView) window.findViewById(R.id.day);
		initDay(curYear,curMonth);


		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		year.setVisibleItems(7);
		month.setVisibleItems(7);
		day.setVisibleItems(7);

		// 设置监听
		Button ok = (Button) window.findViewById(R.id.set);
		Button cancel = (Button) window.findViewById(R.id.cancel);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = String.format(Locale.CHINA,"%4d年%2d月%2d日",year.getCurrentItem()+1950,month.getCurrentItem()+1,day.getCurrentItem()+1);
				Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
				dialog.cancel();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		LinearLayout cancelLayout = (LinearLayout) window.findViewById(R.id.view_none);
		cancelLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				dialog.cancel();
				return false;
			}
		});

	}

}
