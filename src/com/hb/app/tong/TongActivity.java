/*
 * 처占쏙옙 占쏙옙占쏙옙화占쏙옙
 */

package com.hb.app.tong;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import model.tong.DataBases;
import model.tong.DbOpenHelper;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

public class TongActivity extends FragmentActivity implements
		ActionBar.TabListener {
	TabHost mTab;
	TabHost tabHost;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));

		}

		/*
		 * Call 데이터 베이스를 오픈 DB 없으면 새로 생성하고 DB 있으면 그거 불러 오도록 하는 dbOpen 함수
		 */
		long start = System.currentTimeMillis();
		this.dbOpen();
		long end = System.currentTimeMillis();
		Log.i("time", "==========================================");
		Log.i("time", "dbOpen() time : " + ( end - start )/1000.0);
		Log.i("time", "==========================================");
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, R.string.preference);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case 1:
			intent = new Intent(TongActivity.this, SetPreferenceActivity.class);
			startActivity(intent);
			return true;
		}
		return false;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new CallListFragment();
			case 1:
				return new GraphFragment();
			case 2:
				return new MapFragment();

			}
			return null;

		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);

			}
			return null;
		}
	}

	public void dbOpen() {
		String number = null;
		boolean uri_found = false;
		ContentResolver cr = this.getContentResolver();
		DbOpenHelper mDbOpenHelper = new DbOpenHelper(getApplicationContext());

		// CallDB Create and Open
		mDbOpenHelper = new DbOpenHelper(this);
		mDbOpenHelper.open();

		String name, sdate = null;

		Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null,
				CallLog.Calls.DATE + " DESC");

		// 통화기록이 있따면 수집하도록 한다. 통화기록이 없다면 수집하지 않음
		if (cursor.getCount() > 0)
			uri_found = true;

		if (uri_found == true) {

			int ididx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			int nameidx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
			int dateidx = cursor.getColumnIndex(CallLog.Calls.DATE);
			int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER);
			int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION);
			int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE);

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");


			cursor.moveToFirst();
			do {

				name = cursor.getString(nameidx);
				number = cursor.getString(numidx);
				if (name == null) {
					name = cursor.getString(numidx);

				}

				if (mDbOpenHelper.isDuplicateID(cursor.getString(ididx))) {
//					Log.d("TEST", "Data is duplicated");
				} else {
//					Log.d("TEST", "Data is entered");
					mDbOpenHelper
							.insertColumn(cursor.getString(ididx), name,
									number, formatter.format(new Date(cursor
											.getLong(dateidx))), cursor
											.getString(duridx), cursor
											.getString(typeidx));
				}

			} while (cursor.moveToNext());

			Cursor t = mDbOpenHelper.getAllColumns();

//			t.moveToFirst();
//			do {
//				Log.d("check",
//						t.getString(t.getColumnIndex(DataBases.CreateDB.callID))
//								+ "/"
//								+ t.getString(t
//										.getColumnIndex(DataBases.CreateDB.NAME))
//								+ "/"
//								+ formatter.format(new Date(
//										t.getLong(t
//												.getColumnIndex(DataBases.CreateDB.DATE)))));
//			} while (t.moveToNext());

		}
	}

	public class PhoneStateCheckListener extends PhoneStateListener {
		TongActivity tongActivity;

		PhoneStateCheckListener(TongActivity _tongActivity) {
			tongActivity = _tongActivity;
		}

		/*
		 * 전화 수신, 발신 상태일 때의 이벤트를 캐치하여 처리하는 리스너 (non-Javadoc)
		 * 
		 * @see android.telephony.PhoneStateListener#onCallStateChanged(int,
		 * java.lang.String)
		 */
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (state == TelephonyManager.CALL_STATE_IDLE) {
				Toast.makeText(tongActivity,
						"STATE_IDLE : Incoming number " + incomingNumber,
						Toast.LENGTH_SHORT).show();
			} else if (state == TelephonyManager.CALL_STATE_RINGING) {
				Toast.makeText(tongActivity,
						"STATE_RINGING : Incoming number " + incomingNumber,
						Toast.LENGTH_SHORT).show();
				// 수신 부분 입니다.
			} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
				Toast.makeText(tongActivity,
						"STATE_OFFHOOK : Incoming number " + incomingNumber,
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}