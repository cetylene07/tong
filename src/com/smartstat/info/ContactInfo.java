package com.smartstat.info;

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

/*
 * 연락처 정보를 읽어서 값을 리턴하는 함수
 */
public class ContactInfo {
	String name;
	String id;
	Context context;
	final String[] CONTACTS_PROJECTION = new String[] {
			ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

	/*
	 * 본 생성자에서는 인자값으로 넘어온 전화번호(number)에 해당되는 contact id 와 catact name을 가져와서
	 * 멤버 변수 name과 id에 저장해 준다.
	 */
	public ContactInfo(Context _context, ContentResolver resolver, String number) {
		context = _context;
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
}
