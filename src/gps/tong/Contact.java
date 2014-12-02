package gps.tong;

public class Contact {

	int id;
	String time;
	public String gpsinfo1;
	public String gpsinfo2;

	public Contact() {

	}

	public Contact(int id, String time, String gpsinfo1, String gpsinfo2) {
		this.id = id;
		this.time = time;
		this.gpsinfo1 = gpsinfo1;
		this.gpsinfo2 = gpsinfo2;
	}

	public Contact(String time, String gpsinfo1, String gpsinfo2) {
		this.time = time;
		this.gpsinfo1 = gpsinfo1;
		this.gpsinfo2 = gpsinfo2;
	}

	public int getID() {
		return this.id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String gettime() {
		return this.time;
	}

	public void settime(String time) {
		this.time = time;
	}

	public String getgpsinfo1() {
		return this.gpsinfo1;
	}

	public void setgpsinfo1(String gpsinfo1) {
		this.gpsinfo1 = gpsinfo1;
	}
	public String getgpsinfo2() {
		return this.gpsinfo2;
	}

	public void setgpsinfo2(String gpsinfo2) {
		this.gpsinfo2 = gpsinfo2;
	}
}
