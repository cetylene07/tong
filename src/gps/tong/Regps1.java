package gps.tong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hb.app.tong.R;

public class Regps1 extends Activity {
	Intent intent;
	TextView gps1;
	TextView gps2;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.send_gps1);
	    intent = getIntent();
	    gps1 = (TextView)findViewById(R.id.gpsre1);
	    gps2 = (TextView)findViewById(R.id.gpsre2);
	    
	    gps1.setText(intent.getStringExtra("gps1").toString());
	    gps2.setText(intent.getStringExtra("gps2").toString());
	    
	}
}
