package gps.tong;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hb.app.tong.R;


public class gpsadpater extends BaseAdapter {
	private Context context;
	private List<Contact> arrData;
	private LayoutInflater inflater;

	public gpsadpater(Context c, List<Contact> arr) {
		this.context = c;
		this.arrData = arr;
		inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return arrData.size();
	}

	public Object getItem(int position) {
		return arrData.get(position).gettime();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_layout, parent, false);
		}

		TextView date = (TextView) convertView.findViewById(R.id.ldate);
		date.setText(arrData.get(position).gettime());

		TextView gps1 = (TextView) convertView.findViewById(R.id.lgps1);
		gps1.setText(arrData.get(position).gpsinfo1);
		TextView gps2 = (TextView) convertView.findViewById(R.id.lgps2);
		gps2.setText(arrData.get(position).gpsinfo2);

		return convertView;
	}

}
