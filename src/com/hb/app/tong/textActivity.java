package com.hb.app.tong;
///*
// * 搭鉢 歳汐 鉢檎
// */
//package com.ts.app.tong;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//
//import android.app.Activity;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.CallLog;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.AdapterView.OnItemSelectedListener;
//
//class sms_info {
//	int rank;						//授是
//	String name; 					// 戚硯
//	int in_count; 					// 呪重搭鉢 刊旋 噺呪
//	double in_count_percent;		// 呪重搭鉢 刊旋 判呪 搾晴
//	int out_count; 					// 降重搭鉢 刊旋 判呪
//	double out_count_percent;		// 降重搭鉢 刊旋 判呪 搾晴
//	long date;						// 劾促
//
//	int get_incount() {
//		return in_count;
//	}
//}
//
//public class textActivity extends Activity implements OnItemSelectedListener {
//	int total_in_count = 0;										// 恥 呪重 五獣走 鯵呪
//	int total_out_count = 0;									// 恥 降重 五獣走 鯵呪
//	TextView text1, text2;
//	TextView value1, value2;
//	ArrayList<sms_info> list = new ArrayList<sms_info>(); 		// 搭域舛左研 疑旋軒什闘稽 梓端 持失
//	ArrayList<sms_info> temp_list = new ArrayList<sms_info>(); 	// 搭域舛左研 疑旋軒什闘稽 績獣梓端 持失
//																
//	ArrayAdapter<CharSequence> adspin;
//	double average;
//	String temp_average;
//	Cursor cursor, cursor1;
//	
//	ContentResolver cr;
//	boolean uri_found = false;
//
//	int jj;
//
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.textactivity);
//
//		cr = getContentResolver();
//		cursor = cr.query(Uri.parse("content://sms"), null, null, null, null);
//		cursor1 = cr.query(Uri.parse("content://com.sec.mms.provider/message"), null, null, null, null);
//		if(cursor.moveToNext())	//content:sms 税 Uri研 亜走檎 失因旋生稽 Uri研 降胃廃 依生稽 竺舛
//			uri_found = true;
//		
//		if(cursor1 != null)
//			if(cursor1.moveToNext())	//content:sms 税 Uri研 亜走檎 失因旋生稽 Uri研 降胃廃 依生稽 竺舛
//				uri_found = true;		
//
//
//		if (uri_found == true) {			
//			int nameidx = cursor.getColumnIndex("address"); // 庚切企雌切
//			int dateidx = cursor.getColumnIndex("date"); // 庚切 獣繊.
//
//			int bodyidx = cursor.getColumnIndex("body");
//			int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER); // 穿鉢腰硲
//			int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION); // 搭鉢獣娃
//			int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE); // 搭鉢曽嫌(呪重,降重,採仙掻)
//
//			boolean found = false; // 旭精 戚硯聖 達生檎 刊旋判呪研 装亜獣轍陥. 達走公馬檎 軒什闘拭 蓄亜
//
//			StringBuilder result = new StringBuilder();
//
//			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
//			result.append("恥 奄系 鯵呪 : " + cursor.getCount() + "鯵\n");
//			int count = 0;
//			int where = 0; // list拭 薄仙 戚硯葵戚 嬢巨拭 赤澗走 溌昔
//
//			while (cursor.moveToNext()) { // cursor亜 石走 公拝 凶 猿走 鋼差
//				// 搭鉢 企雌切
//
//				sms_info temp = new sms_info();
//				Iterator<sms_info> it = list.iterator(); // iterator稽 痕発
//
//				String name = cursor.getString(nameidx); // 戚硯聖 庚切伸稽 痕発
//
//				if (name == null) {
//					name = cursor.getString(numidx); // 戚硯戚 煽舌鞠走 省生檎 腰硲稽 煽舌
//				}
//
//				temp.name = name;
//
//				found = false;
//				where = 0;
//				while (it.hasNext()) {
//					sms_info data = it.next();
//					if (data.name.equals(name)) { // list拭 赤澗 据社 掻 戚硯戚 旭精 依聖 達生檎
//						found = true;
//
//						/*
//						 * data税 爽社研 temp拭 煽舌獣轍陥 魚虞辞 temp拭辞 琶球研 尻至馬檎 戚惟 data拭辞
//						 * 疑獣拭 尻至鞠澗 暗櫛 原濁亜走! せせ 持唖馬走亀 公廃 暗心澗汽 酔腎せ 瓜 せ
//						 */
//
//						temp = data;
//						break;
//					}
//					where++;
//				}
//
//				// 搭鉢 曽嫌
//				int type = cursor.getInt(typeidx);
//				String stype;
//				switch (type) {
//
//				// 斡薫獣S税 井酔 暗箭精 4, 庚切 勺呪重精 14, 13 生稽 MMS 勺呪重精 16, 15生稽 舛税鞠嬢 赤陥.
//
//				case CallLog.Calls.INCOMING_TYPE:
//					stype = "庚切閤製";
//					temp.in_count++;
//					total_in_count++;
//					break;
//				case CallLog.Calls.OUTGOING_TYPE:
//					stype = "庚切左蛙";
//					temp.out_count++;
//					total_out_count++;
//					break;
//				default:
//					stype = "奄展" + type;
//					break;
//
//				}
//
//				// 搭鉢 劾促
//				long date = cursor.getLong(dateidx);
//				temp.date = date;
//				String sdate = formatter.format(new Date(date));
//
//				if (found == false) {
//					list.add(temp); // 歯稽 蓄亜
//				}
//
//			}
//
//			// 遁湿闘 鯵至馬澗 鋼差庚
//			for (int i = 0; i < list.size(); i++) {
//				if (total_in_count > 0)
//					list.get(i).in_count_percent = (double) list.get(i).in_count
//							/ (double) total_in_count * 100;
//				else
//					list.get(i).in_count_percent = 0;
//
//				if (total_out_count > 0)
//					list.get(i).out_count_percent = (double) list.get(i).out_count
//							/ (double) total_out_count * 100;
//				else
//					list.get(i).out_count_percent = 0;
//			}
//
//			cursor.close();
//
//			Spinner spin = (Spinner) findViewById(R.id.text_spinner1);
//			spin.setPrompt("識澱馬室政");
////			adspin = ArrayAdapter.createFromResource(this, R.array.sms_choice,
//					android.R.layout.simple_spinner_item);
//			adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			spin.setAdapter(adspin);
//			spin.setOnItemSelectedListener(this);
//
//		} else {
//
//			TextView tv1 = (TextView) findViewById(R.id.text_subject1);
//			tv1.setText("汽戚斗 石奄 叔鳶");
//		}
//	}
//
//	public void onItemSelected(AdapterView<?> parent, View view, int position,
//			long id) {
//
//		// temp_list 肢薦 奄狛
//		for (int i = temp_list.size() - 1; i >= 0; i--) {
//			temp_list.remove(i);
//		}
//
//		TextView tv1 = (TextView) findViewById(R.id.text_subject1);
//		tv1.setText(adspin.getItem(position));
//		switch (position) {
//		case 0:
//			text1 = (TextView) findViewById(R.id.text1);
//			text1.setText("恥 呪重 五獣走 鯵呪 : ");
//			value1 = (TextView) findViewById(R.id.value1);
//			value1.setText(String.valueOf(total_in_count) + "鯵");
//
//			text2 = (TextView) findViewById(R.id.text2);
//			text2.setText("汝液 呪重 五獣走 鯵呪 : ");
//			value2 = (TextView) findViewById(R.id.value2);
//
//			average = (double) total_in_count / (double) list.size();
//			temp_average = String.format("%.2f", average);
//			value2.setText(temp_average + "鯵");
//
//			TextView call_text = (TextView) findViewById(R.id.text_title1);
//			call_text
//					.setText("授是        戚硯                    呪重判呪             搾晴");
//			// 識澱舛慶聖 戚遂馬食 呪重判呪研 鎧顕託授生稽 舛慶敗(笛惟 胡煽神惟)
//			for (int i = 0; i < list.size(); i++) {
//				int max = i;
//				for (int j = i + 1; j < list.size(); j++) {
//					if (list.get(j).in_count > list.get(max).in_count) {
//						max = j;
//					}
//				}
//				sms_info trans = new sms_info();
//				trans = list.get(i);
//				list.set(i, list.get(max));
//				list.set(max, trans);
//			}
//
//			// 授是研 煽舌馬澗 鋼差庚
//			jj = 1;
//			for (int i = 0; i < list.size() - 1; i++) {
//
//				list.get(i).rank = jj;
//				if (list.get(i + 1).in_count != list.get(i).in_count) {
//					jj++;
//				}
//			}
//
//			// 固 原走厳 汽戚斗税 授是澗 魚稽 坪漁背醤 吉陥.
//			if (list.get(list.size() - 1).in_count != list.get(list.size() - 1).in_count) {
//
//				list.get(list.size() - 1).rank = jj + 1;
//			} else {
//				list.get(list.size() - 1).rank = jj;
//			}
//
//			// 0戚雌幻 temp_list拭 煽舌廃陥.
//			for (int i = 0; i < list.size(); i++) {
//				if (list.get(i).in_count > 0) {
//					temp_list.add(list.get(i));
//
//				}
//			}
//
//			// 朕什賭 坂研 戚遂馬食 軒什闘坂拭 窒径
//			sms_Adapter MyAdapter = new sms_Adapter(this, R.layout.sms_view,
//					temp_list);
//
//			ListView MyList;
//			MyList = (ListView) findViewById(R.id.sms_list);
//			MyList.setAdapter(MyAdapter);
//			break;
//		case 1:
//
//			text1 = (TextView) findViewById(R.id.text1);
//			text1.setText("恥 降重 五獣走 鯵呪 : ");
//			value1 = (TextView) findViewById(R.id.value1);
//			value1.setText(String.valueOf(total_out_count) + "鯵");
//
//			text2 = (TextView) findViewById(R.id.text2);
//			text2.setText("汝液 降重 五獣走 鯵呪 : ");
//			value2 = (TextView) findViewById(R.id.value2);
//
//			average = (double) total_out_count / (double) list.size();
//			temp_average = String.format("%.2f", average);
//			value2.setText(temp_average + "鯵");
//
//			call_text = (TextView) findViewById(R.id.text_title1);
//			call_text
//					.setText("授是        戚硯                    降重判呪             搾晴");
//			// 識澱舛慶聖 戚遂馬食 呪重判呪研 鎧顕託授生稽 舛慶敗(笛惟 胡煽神惟)
//			for (int i = 0; i < list.size(); i++) {
//				int max = i;
//				for (int j = i + 1; j < list.size(); j++) {
//					if (list.get(j).out_count > list.get(max).out_count) {
//						max = j;
//					}
//				}
//				sms_info trans = new sms_info();
//				trans = list.get(i);
//				list.set(i, list.get(max));
//				list.set(max, trans);
//			}
//
//			// 授是研 煽舌馬澗 鋼差庚
//			jj = 1;
//			for (int i = 0; i < list.size() - 1; i++) {
//
//				list.get(i).rank = jj;
//				if (list.get(i + 1).out_count != list.get(i).out_count) {
//					jj++;
//				}
//			}
//
//			// 固 原走厳 汽戚斗税 授是澗 魚稽 坪漁背醤 吉陥.
//			if (list.get(list.size() - 1).out_count != list
//					.get(list.size() - 1).out_count) {
//
//				list.get(list.size() - 1).rank = jj + 1;
//			} else {
//				list.get(list.size() - 1).rank = jj;
//			}
//
//			// 0戚雌幻 temp_list拭 煽舌廃陥.
//			for (int i = 0; i < list.size(); i++) {
//				if (list.get(i).out_count > 0) {
//					temp_list.add(list.get(i));
//
//				}
//			}
//
//			// 朕什賭 坂研 戚遂馬食 軒什闘坂拭 窒径
//			sms_Out_Adapter out_Adapter = new sms_Out_Adapter(this,
//					R.layout.sms_view, temp_list);
//
//			MyList = (ListView) findViewById(R.id.sms_list);
//			MyList.setAdapter(out_Adapter);
//			break;
//		}
//	}
//
//	public void onNothingSelected(AdapterView<?> parent) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
//
//// 呪重 五獣走 嬢基斗 適掘什
//class sms_Adapter extends BaseAdapter {
//	Context maincon;
//	LayoutInflater Inflater;
//	ArrayList<sms_info> arSrc;
//	int layout;
//	String s_value;
//	String debug;
//	ImageView image;
//	TextView rank;
//	
//	public sms_Adapter(Context context, int alayout, ArrayList<sms_info> alist) {
//		maincon = context;
//		Inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		arSrc = alist;
//		layout = alayout;
//	}
//
//	public int getCount() {
//		return arSrc.size();
//	}
//
//	public String getItem(int position) {
//		return arSrc.get(position).name;
//	}
//
//	public long getItemId(int position) {
//		return position;
//	}
//
//	// 唖 牌鯉税 坂 持失
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		if (convertView == null) {
//			convertView = Inflater.inflate(layout, parent, false);
//		}
//
//		// 1,2,3去精 戚耕走稽 粂滴研 左食爽亀系 廃陥.
//		if (arSrc.get(position).rank == 1) {
//			//1去精 榎五含!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.first);			
//			
//			
//			//焼掘税 2庚舌聖 床走 省生檎 textView拭 葵戚 蟹人辞 照鞠...訊 戚係惟 背醤 鞠澗走 戚背亜 照亜..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//		} else if (arSrc.get(position).rank == 2) {
//			//2去精 精五含!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.second);
//			//焼掘税 2庚舌聖 床走 省生檎 textView拭 葵戚 蟹人辞 照鞠...訊 戚係惟 背醤 鞠澗走 戚背亜 照亜..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//		} else if (arSrc.get(position).rank == 3) {
//			//3去精 疑古含!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.third);
//			//焼掘税 2庚舌聖 床走 省生檎 textView拭 葵戚 蟹人辞 照鞠...訊 戚係惟 背醤 鞠澗走 戚背亜 照亜..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//			
//		} else {
//			//4去採斗澗 益撹 努什闘稽 窒径! せせせ
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.black);	
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			debug = String.valueOf(arSrc.get(position).rank);
//			rank.setText("[" + debug + "]");
//		}
//
//		TextView name = (TextView) convertView.findViewById(R.id.sms_name);
//		name.setText(arSrc.get(position).name);
//
//		TextView count = (TextView) convertView.findViewById(R.id.sms_incount);
//		debug = String.valueOf(arSrc.get(position).in_count);
//		count.setText(debug);
//
//		TextView percent = (TextView) convertView
//				.findViewById(R.id.sms_incount_percent);
//		// 遁湿闘 社呪繊 2切軒猿走 妊獣馬澗 号狛
//		s_value = String.format("%.2f", arSrc.get(position).in_count_percent);
//		debug = String.valueOf(s_value + "%");
//		percent.setText(debug);
//
//		return convertView;
//
//	}
//}
//
//// 降重 五獣走 嬢基斗 適掘什
//class sms_Out_Adapter extends sms_Adapter {
//
//	public sms_Out_Adapter(Context context, int alayout,
//			ArrayList<sms_info> alist) {
//
//		super(context, alayout, alist);
//	}
//
//	// 唖 牌鯉税 坂 持失
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		if (convertView == null) {
//			convertView = Inflater.inflate(layout, parent, false);
//		}
//		
//		// 1,2,3去精 戚耕走稽 粂滴研 左食爽亀系 廃陥.
//		if (arSrc.get(position).rank == 1) {
//			//1去精 榎五含!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.first);			
//			
//			
//			//焼掘税 2庚舌聖 床走 省生檎 textView拭 葵戚 蟹人辞 照鞠...訊 戚係惟 背醤 鞠澗走 戚背亜 照亜..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//		} else if (arSrc.get(position).rank == 2) {
//			//2去精 精五含!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.second);
//			//焼掘税 2庚舌聖 床走 省生檎 textView拭 葵戚 蟹人辞 照鞠...訊 戚係惟 背醤 鞠澗走 戚背亜 照亜..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//		} else if (arSrc.get(position).rank == 3) {
//			//3去精 疑古含!
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.third);
//			//焼掘税 2庚舌聖 床走 省生檎 textView拭 葵戚 蟹人辞 照鞠...訊 戚係惟 背醤 鞠澗走 戚背亜 照亜..
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			rank.setText("");
//			
//		} else {
//			//4去採斗澗 益撹 努什闘稽 窒径! せせせ
//			image = (ImageView) convertView.findViewById(R.id.sms_image);
//			image.setImageResource(R.drawable.black);	
//			rank = (TextView) convertView.findViewById(R.id.sms_rank);
//			debug = String.valueOf(arSrc.get(position).rank);
//			rank.setText("[" + debug + "]");
//		}
//
//
//		TextView name = (TextView) convertView.findViewById(R.id.sms_name);
//		name.setText(arSrc.get(position).name);
//
//		TextView count = (TextView) convertView.findViewById(R.id.sms_incount);
//		debug = String.valueOf(arSrc.get(position).out_count);
//		count.setText(debug);
//
//		TextView percent = (TextView) convertView
//				.findViewById(R.id.sms_incount_percent);
//		// 遁湿闘 社呪繊 2切軒猿走 妊獣馬澗 号狛
//		s_value = String.format("%.2f", arSrc.get(position).out_count_percent);
//		debug = String.valueOf(s_value + "%");
//		percent.setText(debug);
//
//		return convertView;
//
//	}
//}
