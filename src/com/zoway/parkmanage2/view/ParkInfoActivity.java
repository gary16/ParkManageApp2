package com.zoway.parkmanage2.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zoway.parkmanage2.R;
import com.zoway.parkmanage2.bean.LoginBean4Wsdl;
import com.zoway.parkmanage2.bean.ParkInfo;
import com.zoway.parkmanage2.bean.ParkRecord;
import com.zoway.parkmanage2.bean.ParksDetail;
import com.zoway.parkmanage2.http.UnhandleParkinfoWsdl;
import com.zoway.parkmanage2.utils.TimeUtil;

public class ParkInfoActivity extends Activity {

	private ParkInfo pi;
	private ParkRecord pr = null;
	private Button btn4printbill;
	private Button btn4history;
	private Button btn4forinputdata;
	private Button btn4paybill;
	private TextView txtpark;
	private TextView txtcarnumber;
	private TextView txtstate;
	private TextView txtparktime;
	private TextView txtleavetime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_park_info);
		this.setTitle("停车信息");
		Intent intent = this.getIntent();
		String pno = intent.getStringExtra("parkNo");
		pi = ParksDetail.parkLists.get(pno);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtstate = (TextView) this.findViewById(R.id.txtstate);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		txtleavetime = (TextView) this.findViewById(R.id.txtleavetime);
		btn4printbill = (Button) this.findViewById(R.id.btn4printbill);
		btn4history = (Button) this.findViewById(R.id.btn4history);
		btn4forinputdata = (Button) this.findViewById(R.id.btn4forinputdata);
		btn4paybill = (Button) this.findViewById(R.id.btn4paybill);

		Thread thr = new Thread(new parkInfoThread());
		thr.start();

		btn4forinputdata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pr != null) {
					Intent intent = new Intent(ParkInfoActivity.this,
							TakeOcrPhotoActivity.class);
					intent.putExtra("sno", pr.getParkno());
					intent.putExtra("rcno", pr.getRecordno());
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMdd HH:mm:ss");
					intent.putExtra("rt", sdf.format(pr.getParktime()));
					intent.putExtra("type", 4);
					ParkInfoActivity.this.startActivity(intent);
				} else {
					Toast.makeText(ParkInfoActivity.this, "无未处理订单",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		btn4printbill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		btn4history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putSerializable("pi", pi);
				Intent intent = new Intent(ParkInfoActivity.this,
						PayListsActivity.class);
				intent.putExtras(b);
				ParkInfoActivity.this.startActivity(intent);
			}
		});
		btn4paybill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (pr != null) {
					Intent intent = new Intent(ParkInfoActivity.this,
							PaybillActivity.class);
					if (pr.getLeavetime() == null) {
						pr.setLeavetime(TimeUtil.getTime());
					}
					Bundle b = new Bundle();
					b.putSerializable("pr", pr);
					intent.putExtras(b);
					ParkInfoActivity.this.startActivity(intent);
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.park_info, menu);
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

	private Handler parkinfoHdlr = new Handler() {
		private ProgressDialog pDia;

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case 1:
				pDia = ProgressDialog.show(ParkInfoActivity.this, "连接服务器中",
						"请稍候", true, false);
				break;
			case 2:
				pDia.dismiss();
				txtpark.setText(pr.getParkno());
				txtcarnumber.setText(pr.getHphm());
				txtstate.setText(pr.getParkState());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy年MM月dd日 HH时mm分");
				txtparktime.setText(sdf.format(pr.getParktime()));
				txtleavetime.setText(pr.getLeavetime() == null ? "" : sdf
						.format(pr.getLeavetime()));

				break;
			case 3:
				pDia.dismiss();
				break;
			case 4:
				pDia.dismiss();
				Toast.makeText(ParkInfoActivity.this, "网络连接不佳，请检查网络连接",
						Toast.LENGTH_LONG).show();
				break;
			}

		}

	};

	private class parkInfoThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg1 = new Message();
			msg1.what = 1;
			parkinfoHdlr.sendMessage(msg1);
			UnhandleParkinfoWsdl wsdl = new UnhandleParkinfoWsdl();
			ArrayList<ParkRecord> li = wsdl.whenGetUnhandle(
					LoginBean4Wsdl.getSectionId(), pi.getParkId(), 1, 0);

			if (li != null) {
				if (li.size() > 0) {
					pr = li.get(0);
					msg1 = new Message();
					msg1.what = 2;
					parkinfoHdlr.sendMessage(msg1);
				} else {
					msg1 = new Message();
					msg1.what = 3;
					parkinfoHdlr.sendMessage(msg1);
				}
			} else {
				msg1 = new Message();
				msg1.what = 4;
				parkinfoHdlr.sendMessage(msg1);
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

}
