package gps.tong;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hb.app.tong.R;

public class Regps1 extends FragmentActivity implements OnMapClickListener {

	GoogleMap mGoogleMap;
	Intent intent;
	Double rGps1;
	Double rGps2;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	    setContentView(R.layout.send_gps1);
	    intent = getIntent();
	    
	    String sgps1, sgps2;
	    
	    sgps1 = intent.getStringExtra("gps1").toString();
	    sgps2 = intent.getStringExtra("gps2").toString();
	    
	    rGps1 = Double.parseDouble(sgps1);
	    rGps2 = Double.parseDouble(sgps2);
		init();
	}


	public void onMapClick(LatLng point) {

		Point screenPt = mGoogleMap.getProjection().toScreenLocation(point);

		LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(screenPt);
		
		Log.d("t","(" + String.valueOf(point.latitude) + "), (" + String.valueOf(point.longitude) + ")");
		Log.d("f","X(" + String.valueOf(screenPt.x) + "), Y(" + String.valueOf(screenPt.y) + ")");
	}
	

	private void init() {

		String title = "현재위치";
		double lat = rGps1;
		double lng = rGps2;


		LatLng position = new LatLng(lat, lng);
		GooglePlayServicesUtil.isGooglePlayServicesAvailable(Regps1.this);

		mGoogleMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		

		mGoogleMap.setOnMapClickListener(this);
		

		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));


		mGoogleMap.addMarker(
				new MarkerOptions().position(position).title(title))
				.showInfoWindow();


		mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			public boolean onMarkerClick(Marker marker) {

				return false;
			}
		});
	}
	
}