package com.hb.app.tong;

import gps.tong.Regps1;
import gps.tong.gpsDBHelper;

import java.util.ArrayList;
import java.util.Calendar;

import model.tong.DataBases;
import model.tong.DbOpenHelper;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartstat.info.Info;

public class CallListFragment extends Fragment {

	ArrayList<Info> temp_list = new ArrayList<Info>();
	ArrayAdapter<CharSequence> adspin;	// 스피너 관련 변수
	LinearLayout linear;
	static int itemPosition = 0;

	// adapterView is a sort of CustomView
	public MyListAdapter adapterView = null;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_call_list, container, false);
	}

	public void onStart() {
		super.onStart();

		linear = (LinearLayout) View.inflate(getActivity(), R.layout.item_view,
				null);

		this.buttonControl(); // 월 이동하는 버튼 함수

		ContentResolver cr = getActivity().getContentResolver();
		Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null,null);

		if (cursor.moveToNext()) {

			// 스피너 이벤트
			Spinner spin = (Spinner) getView().findViewById(R.id.call_spinner1);
			spin.setPrompt("Choice Option");
			adspin = ArrayAdapter.createFromResource(getActivity(),
					R.array.call_choice, android.R.layout.simple_spinner_item);
			adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin.setAdapter(adspin);
			spin.setOnItemSelectedListener(new OnItemSelectedListener() {

				// 스피너 선택 리스너
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {

					OnItemClickListener listener = new OnItemClickListener() {
						/*
						 * 아이템을 누르면 가장 최근에 연락한 위치가 나오도록 함
						 */

						@Override
						public void onItemClick(AdapterView<?> parent, View arg1,
								int position, long arg3) {

							
							String st = (String) parent.getAdapter().getItem(position);
							Cursor c = DbOpenHelper.mDB.rawQuery("SELECT * FROM calldb WHERE name='"+st + "' ORDER BY date DESC", null);
							if(c.moveToFirst())	{
								st = c.getString(c.getColumnIndex("date"));
								gpsDBHelper mDB = new gpsDBHelper(getActivity(), null, 2);
								SQLiteDatabase db =  mDB.getReadableDatabase();
								c = db.rawQuery("SELECT * FROM gpsinfo WHERE date='" + st + "' ORDER BY date DESC", null);
								if(c.moveToFirst())	{
								String l1 = c.getString(c.getColumnIndex("gpsinfo1"));	//위도 얻기
								String l2 = c.getString(c.getColumnIndex("gpsinfo2"));	//경도 얻기
//								Toast.makeText(getActivity(), "위도 : " + l1 + "  경도 : " + l2, Toast.LENGTH_SHORT).show();
								
								Intent intent=new Intent(getActivity() , Regps1.class);
								intent.putExtra("gps1",	l1);
								intent.putExtra("gps2", l2);
								startActivity(intent);
								}
								else	{
									Toast.makeText(getActivity(), "위치정보를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
								}
							} else	{
								Toast.makeText(getActivity(), "callDB Empty", Toast.LENGTH_SHORT).show();
							}

							itemPosition = position;
						}

					}; 
					
					ListView MyList;
					DbOpenHelper mDbOpenHelper = new DbOpenHelper(getActivity().getApplicationContext());
					mDbOpenHelper = new DbOpenHelper(getActivity());
					Cursor allColumnsCursor = mDbOpenHelper.getAllSums();
					
					int ididx = allColumnsCursor.getColumnIndex(DataBases.CreateDB.callID);
					int nameidx = allColumnsCursor.getColumnIndex(DataBases.CreateDB.NAME);
					int numberidx = allColumnsCursor.getColumnIndex(DataBases.CreateDB.NUMBER);
					int duridx = allColumnsCursor.getColumnIndex("SUM(DURATION)");
					
					switch (position) {					
					case 0:

						temp_list.clear();
						allColumnsCursor = mDbOpenHelper.getAllSums();
						if(allColumnsCursor.moveToFirst())	{
							Log.d("count",  "getAllCursor is " + allColumnsCursor.getCount());
							do	{
								Info item = new Info();
								item.name = allColumnsCursor.getString(nameidx);
								item.number = allColumnsCursor.getString(numberidx);
								item.sum_dur = Integer.parseInt(allColumnsCursor.getString(duridx));
								temp_list.add(item);
//								Log.d("test","ID:"+ "--" + " NAME : " + allColumnsCursor.getString(nameidx) + " DURATION : " + allColumnsCursor.getString(duridx) );
							}while(allColumnsCursor.moveToNext());
						}
						
						adapterView = new MyListAdapter(getActivity(),
								R.layout.incall_view, temp_list, "sumdur");

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						MyList.setAdapter(adapterView);
						MyList.setOnItemClickListener(listener);
//						allColumnsCursor.close();
						break;
						
					case 1:

						temp_list.clear();						
						allColumnsCursor = mDbOpenHelper.getAllIncalls();
						
						if(allColumnsCursor.moveToFirst())	{
							Log.d("count",  "getAllIncomes Count is " + allColumnsCursor.getCount());
							do	{
								Info item = new Info();
								item.name = allColumnsCursor.getString(nameidx);
								item.number = allColumnsCursor.getString(numberidx);
								item.in_dur = Integer.parseInt(allColumnsCursor.getString(duridx));
								temp_list.add(item);
//								Log.d("test","ID:"+ "--" + " NAME : " + allColumnsCursor.getString(nameidx) + " DURATION : " + allColumnsCursor.getString(duridx) );
							}while(allColumnsCursor.moveToNext());
						}
						
						adapterView = new MyListAdapter(getActivity(),
								R.layout.incall_view, temp_list, "indur");

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						
						MyList.setAdapter(adapterView);
						MyList.setOnItemClickListener(listener);
						break;
					case 2:

						temp_list.clear();						
						allColumnsCursor = mDbOpenHelper.getAllOutcalls();
						
						if(allColumnsCursor.moveToFirst())	{
							Log.d("count",  "getAllOutcomes Count is " + allColumnsCursor.getCount());
							do	{
								Info item = new Info();
								item.name = allColumnsCursor.getString(nameidx);
								item.number = allColumnsCursor.getString(numberidx);
								item.out_dur = Integer.parseInt(allColumnsCursor.getString(duridx));
								temp_list.add(item);
//								Log.d("test","ID:"+ "--" + " NAME : " + allColumnsCursor.getString(nameidx) + " DURATION : " + allColumnsCursor.getString(duridx) );
							}while(allColumnsCursor.moveToNext());
						}
						
						adapterView = new MyListAdapter(getActivity(),
								R.layout.incall_view, temp_list, "outdur");

						MyList = (ListView) getView().findViewById(
								R.id.call_list);
						
						MyList.setAdapter(adapterView);
						MyList.setOnItemClickListener(listener);
						break;			
					}	// End of Switch()
					allColumnsCursor.close();
				}

				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});

		} 

	}

	void buttonControl() {
		// 표시할 년,월을 보여주도록 하는 함수

		final Calendar cal = Calendar.getInstance();

		Button btnBeforeMonth = (Button) getView().findViewById(
				R.id.btnBeforeMonth);
		final Button btnAfterMonth = (Button) getView().findViewById(
				R.id.btnAfterMonth);
		final Button btnListMonth = (Button) getView().findViewById(
				R.id.btnListMonth);

		btnBeforeMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cal.add(Calendar.MONTH, -1);
				btnListMonth.setText(cal.get(Calendar.YEAR) + "년 "
						+ (cal.get(Calendar.MONTH) + 1) + "월");

				if (cal.get(Calendar.MONTH) != Calendar.getInstance().get(
						Calendar.MONTH)) {
					// 미래의 월은 의미가 없으므로 안보이도록 한다.
					btnAfterMonth.setVisibility(View.VISIBLE);
				}
			}
		});

		btnAfterMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cal.add(Calendar.MONTH, 1);
				btnListMonth.setText(cal.get(Calendar.YEAR) + "년 "
						+ (cal.get(Calendar.MONTH) + 1) + "월");

				if (cal.get(Calendar.MONTH) == Calendar.getInstance().get(
						Calendar.MONTH)) {
					// 미래의 월은 의미가 없으므로 안보이도록 한다.
					btnAfterMonth.setVisibility(View.INVISIBLE);
				}

			}
		});

		btnListMonth.setText(cal.get(Calendar.YEAR) + "년 "
				+ (cal.get(Calendar.MONTH) + 1) + "월");

	}

}
