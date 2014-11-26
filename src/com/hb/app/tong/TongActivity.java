/*
 * 처음 시작화면
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

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

//		this.dbOpen();
	}

	// 메뉴에 대한 메소드
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, R.string.preference);

		return true;
	}

	// 메뉴에 있는 아이템을 클릭하면 화면을 넘겨줍니다.
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

	/*
	 * 앱 실행 시 처음에 callDB를 연다. 이 때 DB가 있다면.. DB 업그레이드. DB가 없다면 새로 생성
	 */
	public void dbOpen() {
		boolean uri_found = false;
		ContentResolver cr = this.getContentResolver();
		DbOpenHelper mDbOpenHelper = new DbOpenHelper(getApplicationContext());

		String name,sdate = null;

		Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null,
				CallLog.Calls.DATE + " DESC");

		if (cursor.moveToNext())
			uri_found = true;

		if (uri_found == true) {

			int ididx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			int nameidx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
			int dateidx = cursor.getColumnIndex(CallLog.Calls.DATE);
			int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER);
			int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION);
			int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE);
			boolean found = false;

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");

			int where = 0;

			// CallDB Create and Open
			mDbOpenHelper = new DbOpenHelper(this);
			mDbOpenHelper.open();

			while (cursor.moveToNext()) {

				name = cursor.getString(nameidx);
				if (name == null) {
					name = cursor.getString(numidx);
				}

				// 새로만든 DB에 값을 집어넣음
				mDbOpenHelper.insertColumn(cursor.getString(ididx), name,
						formatter.format(new Date(cursor.getLong(dateidx))), cursor.getString(duridx),
						cursor.getString(typeidx));
			}

			Cursor t = mDbOpenHelper.getAllColumns();
			Log.d("TONG", t.getCount() + "");
			t.moveToFirst();
			 while(t.moveToNext()) {
			 Log.d("TONG",
			 t.getString(t.getColumnIndex(DataBases.CreateDB.callID))+ "/" +
			 t.getString(t.getColumnIndex(DataBases.CreateDB.NAME)) + "/" +
			 formatter.format(new
			 Date(t.getLong(t.getColumnIndex(DataBases.CreateDB.DATE)))));
			 }

		}
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
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			// Fragment fragment = new CallListFragment();
			switch (position) {
			case 0:
				return new CallListFragment();

			case 1:
				return new GraphFragment();
			case 2:
				return new MapFragment();

			}
			return null;

			// Bundle args = new Bundle();
			// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position +
			// 1);
			// fragment.setArguments(args);
			// return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
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

}
