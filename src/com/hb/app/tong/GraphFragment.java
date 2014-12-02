/*
 * �ð� �׷��� ���
 */

package com.hb.app.tong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.smartstat.info.DateInfo;
import com.smartstat.info.Info;

public class GraphFragment extends Fragment {
	double total_duration = 0;
	double total_call_count = 0;
	double incall_count = 0;
	double incall_duration = 0;
	double outcall_count = 0;
	double outcall_duration = 0;
	double miss_count = 0;
	double average_duration = 0;
	double average_call_count = 0;
	double t_value;
	int t1_value;
	String t_time;
	GraphicalView gv;
	GraphicalView hour_gv;
	Date when;
	DateInfo dateinfo = new DateInfo();
	ArrayAdapter<CharSequence> chart_spin;
	LinearLayout chartView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_graph, container, false);
	}

	/** Called when the activity is first created. */
	@Override
	public void onStart() {
		super.onStart();

		ContentResolver cr = getActivity().getContentResolver();
		Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null,
				CallLog.Calls.DATE + " DESC");

		ArrayList<Info> list = new ArrayList<Info>(); 

		int nameidx = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME); 
																		
		int dateidx = cursor.getColumnIndex(CallLog.Calls.DATE); 
																	
		int numidx = cursor.getColumnIndex(CallLog.Calls.NUMBER); 
		int duridx = cursor.getColumnIndex(CallLog.Calls.DURATION); 
		int typeidx = cursor.getColumnIndex(CallLog.Calls.TYPE); 

		boolean found = false; 

		StringBuilder result = new StringBuilder();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		result.append("�� ��� ���� : " + cursor.getCount() + "��\n");

		int where = 0;

		while (cursor.moveToNext()) { 


			Info temp = new Info();
			Iterator<Info> it = list.iterator(); 

			String name = cursor.getString(nameidx); 

			if (name == null) {
				name = cursor.getString(numidx); 
			}


			temp.name = name;

			found = false;
			where = 0;
			while (it.hasNext()) {
				Info data = it.next();
				if (data.name.equals(name)) { 
					found = true;

				
					temp = data;
					break;
				}
				where++;
			}

			long date = cursor.getLong(dateidx);
			when = new Date(date);
			Date today = new Date();

			int type = cursor.getInt(typeidx);

			switch (type) {
			case CallLog.Calls.INCOMING_TYPE:
				total_call_count++;
				incall_count++;
				incall_duration += cursor.getInt(duridx);
				total_duration += cursor.getInt(duridx);
				temp.in_count++;
				temp.in_dur += cursor.getInt(duridx);

				temp.in_year = cursor.getLong(dateidx);
				SimpleDateFormat t_format = new SimpleDateFormat("yyyy"); 
				t_time = t_format.format(new Date(temp.in_year));
				temp.in_year = Long.valueOf(t_time);

				dateinfo.hour_in_dur[when.getHours()] += cursor.getInt(duridx);

				break;
			case CallLog.Calls.OUTGOING_TYPE:
	
				total_call_count++;
				outcall_count++;
				outcall_duration += cursor.getInt(duridx);
				temp.out_count++;
				total_duration += cursor.getInt(duridx);
				temp.out_dur += cursor.getInt(duridx);

				dateinfo.hour_out_dur[when.getHours()] += cursor.getInt(duridx);
				break;
			case CallLog.Calls.MISSED_TYPE:
	
				total_call_count++;
				miss_count++;
				temp.miss_count++;
				break;

			}

			if (true) {
				switch (when.getDay()) {
				case 0: 
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.sun_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.sun_out_dur += cursor.getInt(duridx);
					}
					break;
				case 1: 
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.mon_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.mon_out_dur += cursor.getInt(duridx);
					}
					break;
				case 2:
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.tus_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.tus_out_dur += cursor.getInt(duridx);
					}
					break;
				case 3: // ������
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.wed_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.wed_out_dur += cursor.getInt(duridx);
					}
					break;
				case 4: // �����
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.thr_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.thr_out_dur += cursor.getInt(duridx);
					}
					break;
				case 5: // �ݿ���
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.fri_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.fri_out_dur += cursor.getInt(duridx);
					}
					break;
				case 6: // �����
					if (cursor.getInt(typeidx) == CallLog.Calls.INCOMING_TYPE) {

						dateinfo.sat_in_dur += cursor.getInt(duridx);
					} else {
						dateinfo.sat_out_dur += cursor.getInt(duridx);
					}
					break;
				}
			}

			if (found == false) {
				list.add(temp); // ���� �߰�
			}

		}

		cursor.close();


		List<int[]> values = new ArrayList<int[]>();


		values.add(new int[] { dateinfo.sun_in_dur, dateinfo.mon_in_dur,
				dateinfo.tus_in_dur, dateinfo.wed_in_dur, dateinfo.thr_in_dur,
				dateinfo.fri_in_dur, dateinfo.sat_in_dur });

		values.add(new int[] { dateinfo.sun_out_dur, dateinfo.mon_out_dur,
				dateinfo.tus_out_dur, dateinfo.wed_out_dur,
				dateinfo.thr_out_dur, dateinfo.fri_out_dur,
				dateinfo.sat_out_dur });

		// �ð��� ������ �߰�
		List<int[]> hour_values = new ArrayList<int[]>();
		hour_values.add(dateinfo.hour_in_dur);
		hour_values.add(dateinfo.hour_out_dur);

		// �׷��� ����� ���� �׷��� �Ӽ� ���� ��ü
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		XYMultipleSeriesRenderer hour_renderer = new XYMultipleSeriesRenderer();

		// ��� ǥ�� ����� ũ��
		// renderer.setChartTitle("���Ϻ� ��ȭ����");
		renderer.setChartTitleTextSize(40);

		// �з� ���� �̸�
		String[] titles = new String[] { getString(R.string.receivingTime),
				getString(R.string.outGoingTime) };
		String[] test_titles = new String[] {
				getString(R.string.receivingTime),
				getString(R.string.outGoingTime) };

		// �׸��� ǥ���ϴ� �� ���� ����
		int[] colors = new int[] { Color.argb(100, 55, 128, 71),
				Color.argb(100, 40, 55, 142) };

		// int[] test_colors = new int[] { Color.argb(100, 94, 154, 210) };

		// �з�� ���� ũ�� �� �� ���� ����
		// renderer.setLabelsTextSize(35);
		// hour_renderer.setLabelsTextSize(75);
		// renderer.setChartTitleTextSize(30);
		renderer.setLegendTextSize(30);
		hour_renderer.setLegendTextSize(30);
		// renderer.setAxisTitleTextSize(30);
		// renderer.setChartValuesTextSize(60);

		int length = colors.length;
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
			hour_renderer.addSeriesRenderer(r);
		}

		// X,Y �� �׸��̸��� ���� ũ��
		// renderer.setXTitle("����");
		// renderer.setYTitle("����(��)");
		renderer.setAxisTitleTextSize(25);
		hour_renderer.setAxisTitleTextSize(18);

		// ��ġ�� ���� ũ�� , X�� �ּ� �ִ밪 , Y�� �ּ� �ִ밪 ����
		renderer.setLabelsTextSize(14);
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(7.5);
		renderer.setYAxisMin(0.5);

		hour_renderer.setLabelsTextSize(14);
		hour_renderer.setXAxisMin(0.5);
		hour_renderer.setXAxisMax(24.5);
		hour_renderer.setYAxisMin(0.5);

		// ��Ƽ�˷��̽� ����
		renderer.setAntialiasing(true);

		renderer.setXLabels(0);

		renderer.addXTextLabel(1, getString(R.string.sun));
		renderer.addXTextLabel(2, getString(R.string.mon));
		renderer.addXTextLabel(3, getString(R.string.tus));
		renderer.addXTextLabel(4, getString(R.string.wed));
		renderer.addXTextLabel(5, getString(R.string.thr));
		renderer.addXTextLabel(6, getString(R.string.fri));
		renderer.addXTextLabel(7, getString(R.string.sat));

		hour_renderer.setXLabels(0);
		for (int i = 0; i < 24; i++)
			hour_renderer.addXTextLabel(i, i + "");

		// X��� Y���� ���� ����
		renderer.setAxesColor(Color.BLACK);

		// �������, X,Y�� ����, ��ġ���� ���� ����
		renderer.setLabelsColor(Color.BLACK);

		// X�� ǥ�� ����
		// renderer.setXLabels(7);
		//
		// Y�� ǥ�� ����
		// renderer.setYLabels(5);

		// X,Y�� ���Ĺ���
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		hour_renderer.setXLabelsAlign(Align.LEFT);
		hour_renderer.setYLabelsAlign(Align.LEFT);

		// X,Y�� ��ũ�� ���� ON/OFF
		renderer.setPanEnabled(false, false);
		hour_renderer.setPanEnabled(false, false);

		// ZOOM��� ON/OFF
		renderer.setZoomEnabled(false, false);
		hour_renderer.setZoomEnabled(false, false);

		// ZOOM ����
		renderer.setZoomRate(1.0f);
		hour_renderer.setZoomRate(1.0f);

		// ���밣 ����
		renderer.setBarSpacing(0.5f);
		hour_renderer.setBarSpacing(0.5f);

		// ��� ����
		 renderer.setMarginsColor(Color.parseColor("#ffffff"));
		 hour_renderer.setMarginsColor(Color.parseColor("#ffffff"));

		// ���� ���� ����
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		for (int i = 0; i < titles.length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			int[] v = values.get(i);
			int seriesLength = v.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}

		XYMultipleSeriesDataset test_dataset = new XYMultipleSeriesDataset();
		for (int i = 0; i < test_titles.length; i++) {
			CategorySeries test_series = new CategorySeries(test_titles[i]);
			int[] test_v = hour_values.get(i);
			int test_seriesLength = test_v.length;
			for (int k = 0; k < test_seriesLength; k++) {
				test_series.add(test_v[k]);
			}
			test_dataset.addSeries(test_series.toXYSeries());
		}

		gv = ChartFactory.getBarChartView(getActivity(), dataset, renderer,
				Type.DEFAULT);

		hour_gv = ChartFactory.getBarChartView(getActivity(), test_dataset,
				hour_renderer, Type.DEFAULT);
		//

		// ���ǳ�
		Spinner spin = (Spinner) getView().findViewById(R.id.chart_spinner);
		spin.setPrompt("Choice Option");
		chart_spin = ArrayAdapter.createFromResource(getActivity(),
				R.array.chart_choice, android.R.layout.simple_spinner_item);
		chart_spin
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(chart_spin);
		spin.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				chartView = (LinearLayout) getView().findViewById(
						R.id.chartView);
				// hourChart = (LinearLayout) getView().findViewById
				// (R.id.hour_chart);

				// dayChart.addView(gv);
				// dayChart.addView(hour_gv);

				switch (position) {
				case 0:
					// // �׷����� Layout�� �߰�
					chartView.removeAllViews();
					// hourChart.setVisibility(View.INVISIBLE);
					// dayChart.setVisibility(View.VISIBLE);
					chartView.addView(gv);
					// chartView.setBackgroundColor(Color.parseColor("#f6f7ef"));

					break;

				case 1:
					// // �׷����� Layout�� �߰�
					chartView.removeAllViews();
					// dayChart.setVisibility(View.INVISIBLE);
					// hourChart.setVisibility(View.VISIBLE);
					chartView.addView(hour_gv);
					// hourChart.addView(hour_gv);
					// chartView.setBackgroundColor(Color.parseColor("#f6f7ef"));
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

	}

}
