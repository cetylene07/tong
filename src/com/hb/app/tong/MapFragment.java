/*
 * 시간 그래프 출력
 */

package com.hb.app.tong;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_map, container, false);
	}

	/** Called when the activity is first created. */
	@Override
	public void onStart() {
		super.onStart();

	}

}
