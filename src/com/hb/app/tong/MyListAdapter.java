package com.hb.app.tong;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstat.info.Info;

public class MyListAdapter extends BaseAdapter implements OnItemClickListener {
	Context context;
	LayoutInflater Inflater;
	ArrayList<Info> arSrc;
	int layout;
	String s_value;
	TextView rank;
	String debug;
	ImageView image;
	Cursor cursor;
	String attribute;

	public MyListAdapter(Context _context, int alayout, ArrayList<Info> alist,
			String _attribute, Cursor _cursor) {
		context = _context;
		Inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arSrc = alist;
		layout = alayout;
		attribute = _attribute;
		cursor = _cursor;
	}

	public MyListAdapter(Context _context, int alayout, ArrayList<Info> alist,
			String _attribute) {
		context = _context;
		Inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arSrc = alist;
		layout = alayout;
		attribute = _attribute;
	}

	public int getCount() {
		return arSrc.size();
	}

	public String getItem(int position) {
		return arSrc.get(position).name;
	}

	public long getItemId(int position) {
		return position;
	}

	public String secToHourMinuteSecond(int time) {

		int hour = time / 3600;
		int minute = (time - hour * 3600) / 60;
		int second = ((time - hour * 3600) - minute * 60);

		String hourText = "", minuteText = "", secondText = "";
		if (hour > 0) {
			hourText = hour + "시간 ";
		}
		if (minute > 0) {
			minuteText = minute + "분";
		}
		if (second > 0) {
			secondText = second + "초";
		}
		return hourText + minuteText + secondText;
	}

	public String secToHourMinuteSecond(double doubleTime) {
		int time = (int) doubleTime;
		return secToHourMinuteSecond(time);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		} 

			ImageView image = (ImageView) convertView
					.findViewById(R.id.call_image);

			ContactInfo photoInfo = new ContactInfo(convertView.getContext()
					.getContentResolver(), arSrc.get(position).number);
			if (photoInfo.hasPhoto()) {
				image.setImageBitmap(photoInfo.getPhoto());
			}

			TextView name = (TextView) convertView.findViewById(R.id.call_name);
			name.setText(arSrc.get(position).name);
			TextView count = (TextView) convertView
					.findViewById(R.id.call_value);
			debug = "";

			if (attribute == "sumdur") {
				debug = secToHourMinuteSecond(arSrc.get(position).sum_dur);
			} else if (attribute == "incount") {
				debug = String.valueOf(arSrc.get(position).in_count) + " "
						+ context.getString(R.string.times);
			} else if (attribute == "indur") {
				debug = secToHourMinuteSecond(arSrc.get(position).in_dur);
			} else if (attribute == "average_indur") {
				debug = secToHourMinuteSecond(arSrc.get(position).average_in_dur);
			} else if (attribute == "outdur") {
				debug = secToHourMinuteSecond(arSrc.get(position).out_dur);
			} else if (attribute == "average_outdur") {
				debug = secToHourMinuteSecond(arSrc.get(position).average_out_dur);
			} else if (attribute == "misscount") {
				debug = String.valueOf((int) arSrc.get(position).miss_count)
						+ " " + context.getString(R.string.times);
			} else if (attribute == "outcount") {
				debug = String.valueOf((int) arSrc.get(position).out_count)
						+ " " + context.getString(R.string.times);
			}
			count.setText(debug);
		
		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
	}

	public void toggle(int position) {
		// mExpanded[position] = !mExpanded[position];
		notifyDataSetChanged();
	}

	public class ContactInfo {
		String name;
		String id;
		final String[] CONTACTS_PROJECTION = new String[] {
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

		/*
		 * 본 생성자에서는 인자값으로 넘어온 전화번호(number)에 해당되는 contact id 와 catact name을 가져와서
		 * 멤버 변수 name과 id에 저장해 준다.
		 */
		public ContactInfo(ContentResolver resolver, String number) {
			Cursor cursor = resolver.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					CONTACTS_PROJECTION,
					ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
					new String[] { number }, null);
			if (cursor.getCount() < 1) {
				name = null;
				id = null;
			} else {
				cursor.moveToFirst();
				id = cursor.getString(0);
				name = cursor.getString(1);
			}
			cursor.close();
		} // End of ContactInfo 생성자

		public boolean hasPhoto() {
			/*
			 * 연락처 사진을 찾으면 true 연락처 사진이 없으면 false
			 */
			if (this.getPhoto() == null)
				return false;
			else
				return true;
		}

		/*
		 * 위 생성자에서 구해진 id 값을 기준으로 해서 id와 name에 일치하는 Contact의 사진을 가져와서Bitmap 형식으로
		 * 반환해 준다.
		 */
		public Bitmap getPhoto() {
			if (id == null)
				return null;
			Uri uri = ContentUris
					.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
							Long.parseLong((id)));
			InputStream data = ContactsContract.Contacts
					.openContactPhotoInputStream(context.getContentResolver(),
							uri);
			if (data != null)
				return BitmapFactory.decodeStream(data);
			else {
				/*
				 * 해당하는 연락처 이미지를 찾을 수 없다면 기본 이미지를 보여준다.
				 */
				return null;
				// return BitmapFactory.decodeResource(context.getResources(),
				// R.drawable.ic_contact_picture_holo_light);
			}
		} // End of getPhoto()
	} // End of ContactInfo Class
}