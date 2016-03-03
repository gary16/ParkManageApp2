package com.zoway.parkmanage2.view;

import java.io.File;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoway.parkmanage2.R;
import com.zoway.parkmanage2.bean.LoginBean4Wsdl;
import com.zoway.parkmanage2.bean.ParkInfo;
import com.zoway.parkmanage2.bean.ParksDetail;
import com.zoway.parkmanage2.receiver.UpdateUiReceiver;
import com.zoway.parkmanage2.service.TerminalService;
import com.zoway.parkmanage2.utils.PathUtils;

public class MainActivity extends Activity {

	GridView gv1 = null;
	DisplayMetrics dm = new DisplayMetrics();
	ImageAdApter iad = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int h = dm.heightPixels;
		int w = dm.widthPixels;
		gv1 = ((GridView) findViewById(R.id.gridview));
		iad = new ImageAdApter(this, h, w);
		gv1.setAdapter(iad);
		gv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						ParkInfoActivity.class);
				String pno = ParksDetail.idxLists.get(position);
				intent.putExtra("parkNo", pno);
				MainActivity.this.startActivity(intent);
			}
		});
		this.setTitle(LoginBean4Wsdl.getParkName());

		IntentFilter intfil = new IntentFilter(
				"com.zoway.parkmanage2.receiver.UpdateUiReceiver");
		this.registerReceiver(new UpdateUiReceiver(iad), intfil);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.act_exit) {

			// TODO Auto-generated method stub
			NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancelAll();
			Intent ii = new Intent(MainActivity.this, TerminalService.class);
			MainActivity.this.stopService(ii);

			String tmppath = PathUtils.getTmpImagePath();
			File dri = new File(tmppath);
			if (dri.exists() && dri.isDirectory()) {
				File[] filelst = dri.listFiles();
				for (int cnt = 0; cnt < filelst.length; cnt++) {
					File f = filelst[cnt];
					f.delete();
				}
			}

			String wintonepath = PathUtils.getWintoneImagePath();
			File wdri = new File(wintonepath);
			if (wdri.exists() && wdri.isDirectory()) {
				File[] filelst = wdri.listFiles();
				for (int cnt = 0; cnt < filelst.length; cnt++) {
					File f = filelst[cnt];
					f.delete();
				}
			}

			Intent MyIntent = new Intent(MainActivity.this, HeadActivity.class);
			MyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			MyIntent.putExtra("status", 1);
			MainActivity.this.startActivity(MyIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class ImageAdApter extends BaseAdapter {
		private Context myContext;
		private int he;
		private int wi;

		// 使用android.R.drawable里面的图片作为图库源。类型为整数数组

		// 构造器只有一个参数，即要存储的Context
		public ImageAdApter(Context c, int he, int wi) {
			this.myContext = c;
			this.he = he;
			this.wi = wi;
		}

		// 返回所有已定义的图片总数量
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ParksDetail.parkLists.size();
		}

		// 取得目前容器图像的数组ID
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		// 取得目前要显示的图像view，传入数组ID值使之读取与成像
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				RelativeLayout rl1 = new RelativeLayout(myContext);
				AbsListView.LayoutParams lp1 = null;
				if (position % 2 == 0) {
					lp1 = new AbsListView.LayoutParams((wi / 2) - 5, (he / 5));
				} else {
					lp1 = new AbsListView.LayoutParams((wi / 2) - 3, (he / 5));
				}
				rl1.setLayoutParams(lp1);
				RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);

				TextView tv1 = new TextView(myContext);
				tv1.setId(0x11112222);
				tv1.setGravity(Gravity.CENTER_HORIZONTAL);
				tv1.setLayoutParams(lp2);
				rl1.addView(tv1);
				holder.tv1 = tv1;

				// TextView tv2 = new TextView(myContext);
				// tv2.setText("粤X12345");
				// tv2.setTextSize(24);
				// tv2.setId(0x11112223);
				// // tv2.setBackgroundResource(R.drawable.tvsty1);
				// tv2.setGravity(Gravity.CENTER_HORIZONTAL);
				// RelativeLayout.LayoutParams lp3 = new
				// RelativeLayout.LayoutParams(
				// RelativeLayout.LayoutParams.MATCH_PARENT,
				// RelativeLayout.LayoutParams.WRAP_CONTENT);
				// lp3.addRule(RelativeLayout.BELOW, 0x11112222);
				// tv2.setLayoutParams(lp3);
				// rl1.addView(tv2);

				TextView tv3 = new TextView(myContext);
				tv3.setSingleLine(false);
				tv3.setId(0x11112224);
				tv3.setGravity(Gravity.CENTER_HORIZONTAL);
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

			String parkno = ParksDetail.idxLists.get(position);
			ParkInfo pr = ParksDetail.parkLists.get(parkno);
			// 空闲
			if (pr.getState().endsWith("空闲")) {
				convertView.setBackgroundColor(0xff99cc33);
			}
			// 待确认停
			else if (pr.getState().endsWith("待确认停车")) {
				convertView.setBackgroundColor(0xffffff00);

			}
			// 在停
			else if (pr.getState().endsWith("在停")) {
				convertView.setBackgroundColor(0xff6699cc);
			}
			// 待确认离开
			else if (pr.getState().endsWith("待确认离开")) {
				convertView.setBackgroundColor(0xffff9999);
			}
			holder.tv1.setText(pr.getParkNo());
			holder.tv1.setTextSize(28);
			holder.tv1.setTextColor(0xffffffff);
			holder.tv3.setText("0天12小时59分");
			holder.tv3.setTextSize(18);

			return convertView;
		}

		// 根据中央的位移量，利用getScale返回views的大小
		public float getScale(boolean focused, int offset) {
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}

	}

	private static class ViewHolder {
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}

}
