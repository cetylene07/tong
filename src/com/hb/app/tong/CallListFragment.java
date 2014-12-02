package com.hb.app.tong;

import java.util.ArrayList;
import java.util.Calendar;

import model.tong.DataBases;
import model.tong.DbOpenHelper;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
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

import com.smartstat.info.Info;

public class CallListFragment extends Fragment {

	ArrayList<Info> list = new ArrayList<Info>();
	ArrayList<Info> temp_list = new ArrayList<Info>();
	double total_dur = 0;
	double total_incall_count = 0;
	double total_outcall_count = 0;
	double total_average_in_dur = 0;
	double total_average_out_dur = 0;
	double total_average_sum_dur = 0;
	double total_indur = 0;
	double total_outdur = 0;
	double total_miss = 0;
	double temp_value;
	int jj;
	boolean uri_found = false;

	int tmp1;
	String name;
	String number;
	long date;
	String sdate;

	ArrayAdapter<CharSequence> adspin;

	LinearLayout linear;

	final static int itemView = 0;
	static int itemPosition = 0;

	private DbOpenHelper mDbOpenHelper;

	// adapterView is a sort of CustomView
	public MyListAdapter adapterView = null;

	final String[] CONTACTS_PROJECTION = new String[] {
			ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME

	};
	
	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		for (int i = 0; i < list.size(); i++)
			list.remove(i);
		return inflater.inflate(R.layout.fragment_call_list, container, false);
	}

	public void onStart() {
		super.onStart();

		// fix duplicate data when resume activity
		list.clear();

		linear = (LinearLayout) View.inflate(getActivity(), R.layout.item_view,
				null);

		this.buttonControl(); // 월 이동하는 버튼 함수

		ContentResolver cr = getActivity().getContentResolver();

		String name = null;

		Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null,
				CallLog.Calls.DATE + " DESC");


		if (cursor.moveToNext()) {


			// 스피너 이벤트
			Spinner spin = (Spinner) getView().findViewById(R.id.call_spinner1);
			spin.setPrompt("Choice Option");
			adspin = ArrayAdapter.createFromResource(getActivity(),
					R.array.call_choice, android.R.layout.simple_spinner_item);
			adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spin.setAdapter(adspin);
			spin.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {

					String s_value;

					OnItemClickListener listener = new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {

							itemPosition = position;
						}

					}; 
					
					
					ListView MyList;
					DbOpenHelper mDbOpenHelper = new DbOpenHelper(getActivity().getApplicationContext());
					mDbOpenHelper = new DbOpenHelper(getActivity());
					
					Cursor allColumnsCursor = mDbOpenHelper.getAllSums();
					
					int ididx = allColumnsCursor.getColumnIndex(DataBases.CreateDB.callID);
					int nameidx = allColumnsCursor.getColumnIndex(DataBases.CreateDB.NAME);
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
