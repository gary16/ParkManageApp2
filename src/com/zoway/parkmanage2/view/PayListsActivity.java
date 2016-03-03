package com.zoway.parkmanage2.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zoway.parkmanage2.R;
import com.zoway.parkmanage2.bean.LoginBean4Wsdl;
import com.zoway.parkmanage2.bean.ParkInfo;
import com.zoway.parkmanage2.bean.ParkRecord;
import com.zoway.parkmanage2.http.UnhandleParkinfoWsdl;
import com.zoway.parkmanage2.utils.TimeUtil;

public class PayListsActivity extends Activity {

	private final ArrayList<ParkRecord> groups = new ArrayList<ParkRecord>();
	private ListView lview;
	private MyListAdapter madapter;
	private LayoutInflater mInflater;
	private ParkInfo pi;
	DisplayMetrics dm = new DisplayMetrics();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_lists);
		pi = (ParkInfo) this.getIntent().getExtras().get("pi");
		mInflater = this.getLayoutInflater();
		groups.clear();

		Thread t1 = new Thread(new parklistThread());
		t1.start();

		lview = (ListView) this.findViewById(R.id.reclist);
		int h = dm.heightPixels;
		int w = dm.widthPixels;
		madapter = new MyListAdapter(this, h, w);
		lview.setAdapter(madapter);

		lview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ParkRecord pr = groups.get(position);
				if (pr != null) {
					Intent intent = new Intent();
					Bundle b = new Bundle();
					b.putSerializable("pr", pr);
					intent.putExtras(b);
					intent.setClass(PayListsActivity.this,
							PaybillActivity.class);
					PayListsActivity.this.startActivity(intent);
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class MyListAdapter extends BaseAdapter {
		private Context myContext;
		private int he;
		private int wi;

		public MyListAdapter(Context myContext, int he, int wi) {
			this.myContext = myContext;
			this.he = he;
			this.wi = wi;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return groups.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				RelativeLayout rl1 = new RelativeLayout(myContext);
				AbsListView.LayoutParams lp1 = new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT, he / 6);
				rl1.setLayoutParams(lp1);
				RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);

				TextView tv1 = new TextView(myContext);
				tv1.setId(0x11112222);
				tv1.setGravity(Gravity.LEFT);
				tv1.setTextSize(20);
				tv1.setLayoutParams(lp2);
				rl1.addView(tv1);
				holder.tv1 = tv1;

				TextView tv3 = new TextView(myContext);
				tv3.setSingleLine(false);
				tv3.setId(0x11112224);
				tv3.setTextSize(19);
				tv3.setTextColor(0xffff4238);
				tv3.setGravity(Gravity.LEFT);
				RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				lp4.addRule(RelativeLayout.BELOW, 0x11112222);
				tv3.setLayoutParams(lp4);
				rl1.addView(tv3);
				holder.tv3 = tv3;

				convertView = rl1;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ParkRecord pr = groups.get(position);
			if (pr != null) {
				holder.tv1.setText(pr.getHphm());

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy年MM月dd日  HH時mm分");
				holder.tv3.setText("停车时间:" + sdf.format(pr.getParktime()));
			} else {
				holder.tv1.setText("无记录");

			}

			return convertView;
		}
	}

	private static class ViewHolder {
		public TextView tv1;
		public TextView tv3;
	}

	private Handler parklistsHdlr = new Handler() {
		private ProgressDialog pDia;

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case 1:
				pDia = ProgressDialog.show(PayListsActivity.this, "连接服务器中",
						"请稍候", true, false);
				break;
			case 2:
				pDia.dismiss();
				madapter.notifyDataSetChanged();
				break;
			case 3:
				pDia.dismiss();

				break;
			case 4:
				pDia.dismiss();
				Toast.makeText(PayListsActivity.this, "网络连接不佳，请检查网络连接",
						Toast.LENGTH_LONG).show();
				break;
			}

		}

	};

	private class parklistThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg1 = new Message();
			msg1.what = 1;
			parklistsHdlr.sendMessage(msg1);
			UnhandleParkinfoWsdl wsdl = new UnhandleParkinfoWsdl();
			ArrayList<ParkRecord> li = wsdl.whenGetUnhandle(
					LoginBean4Wsdl.getSectionId(), pi.getParkId(), 8, 0);
			if (li != null) {
				if (li.size() > 0) {
					groups.addAll(li);
					msg1 = new Message();
					msg1.what = 2;
					parklistsHdlr.sendMessage(msg1);
				} else {
					msg1 = new Message();
					msg1.what = 3;
					parklistsHdlr.sendMessage(msg1);
				}
			} else {
				msg1 = new Message();
				msg1.what = 4;
				parklistsHdlr.sendMessage(msg1);
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent ii = new Intent(this, MainActivity.class);
		this.startActivity(ii);
		this.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		setContentView(R.layout.view_null);
		super.onDestroy();
	}

}
