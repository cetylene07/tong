package com.hb.app.tong;

import gps.tong.Contact;
import gps.tong.GpsInfo;
import gps.tong.Regps1;
import gps.tong.gpsDBHelper;
import gps.tong.gpsadpater;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import model.tong.Common;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MapFragment extends Fragment {
	private Button btnShowLocation;
	private Button btnShutdown;

	private ListView listview;
	
	private gpsDBHelper db;

	private Double gps1;
	private Double gps2;
	private String date;

	private GpsInfo gps;
	
	private List<Contact> list;
	private Contact sitem;
	gpsadpater adapter;
	
	Intent intent;
	TimerTask testt;
	Timer timert;
	
	private Handler gHandler;
	private Runnable gRunnable;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.activity_gps, container, false);
	}

	/** Called when the activity is first created. */
	@Override
	public void onStart() {
		super.onStart();

		btnShowLocation = (Button) getView().findViewById(R.id.button1);
		btnShutdown = (Button) getView().findViewById(R.id.button2);

//		Button btnTest = (Button) getView().findViewById(R.id.button3);
//		btnTest.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				//test
//				db.getGpsContact();
//			}
//		});
		
		db = new gpsDBHelper(getActivity(), null, 2);
		

		
        list = new ArrayList<Contact>();
        list = db.getAllContacts();
		
		adapter = new gpsadpater(getActivity(), list);
		listview = (ListView) getView().findViewById(R.id.listView1);
		listview.setAdapter(adapter);
		

        listview.setOnItemClickListener(new OnItemClickListener() {	 
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int setpos = position + 1;
				sitem = db.getContact(setpos);
				
//				intent=new Intent("com.example.gpstest.recgps");
//				intent.putExtra("gps1",	sitem.gpsinfo1);
//				intent.putExtra("gps2", sitem.gpsinfo2);
//				startActivity(intent);
				
				Intent intent=new Intent(getActivity() , Regps1.class);
				intent.putExtra("gps1",	sitem.gpsinfo1);
				intent.putExtra("gps2", sitem.gpsinfo2);
				startActivity(intent);
			}
		});
		// GPS 정보를 보여주기 위한 이벤트 클래스 등록
		btnShowLocation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				testtimer();
			}
		});
		
		btnShutdown.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				stoptimer();
			}
		});
	
	}
	
	public void testtimer(){
		timert = new Timer();
		gHandler = new Handler();
		gRunnable = new Runnable(){
			public void run() {
				gpsdbreturn();
			}
		};
		
		testt = new TimerTask(){
			public void run() {
				gHandler.post(gRunnable);
			}
		};
		timert = new Timer();
		timert.scheduleAtFixedRate(testt, 500, 10000);
	}
	public void stoptimer(){
		gHandler.removeCallbacks(gRunnable);
		timert.cancel();
	}
	
	
	public void gpsdbreturn(){
		gps = new GpsInfo(getActivity());
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat fdate = new SimpleDateFormat(
				Common.dateFormat, Locale.KOREA);
		// GPS 사용유무 가져오기
		if (gps.isGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			gps1 = latitude;
			gps2 = longitude;

			date = fdate.format(today);
			db.addContact(new Contact(date, gps1.toString(), gps2
					.toString()));

			Toast.makeText(getActivity().getApplicationContext(),
					"당신의 위치 - \n위도: " + gps1 + "\n경도: " + gps2 + "\n시간 : " + date,
					Toast.LENGTH_LONG).show();
		} else {
			// GPS 를 사용할수 없으므로
			gps.showSettingsAlert();
		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			db.deleteAll();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

}
