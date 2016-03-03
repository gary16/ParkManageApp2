package com.zoway.parkmanage2.view;

import java.text.SimpleDateFormat;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.zoway.parkmanage2.bean.EscapeBean4Wsdl;
import com.zoway.parkmanage2.bean.IgnoreBean4Wsdl;
import com.zoway.parkmanage2.bean.LeaveBean4Wsdl;
import com.zoway.parkmanage2.bean.LoginBean4Wsdl;
import com.zoway.parkmanage2.bean.ParkRecord;
import com.zoway.parkmanage2.bean.ParksDetail;
import com.zoway.parkmanage2.bean.PayBean4Wsdl;
import com.zoway.parkmanage2.http.EscapeWsdl;
import com.zoway.parkmanage2.http.IgnoreWsdl;
import com.zoway.parkmanage2.http.LeaveWsdl;
import com.zoway.parkmanage2.http.PayWsdl;

public class PaybillActivity extends PrintActivity {

	private TextView txtcarnumber;
	private TextView txtpark;
	private TextView txtparktime;
	private TextView txtleavetime;
	private TextView txtmoney;
	private TextView lblcartype;
	private Button btnsure4bill;
	private Button btnsure4escape;
	private Button btnsure4print;
	private Button btnsure4ingore;
	private ProgressDialog pDia;
	private ParkRecord pr;
	private float fare;

	private Handler payHdlr = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				pDia = ProgressDialog.show(PaybillActivity.this, "正在提交", "请稍后");
				break;
			case 2:
				pDia.dismiss();
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				nm.cancel(Integer.parseInt(pr.getParkno()));
				ParksDetail.parkLists.get(pr.getParkno()).setState("空闲");
				Intent i1 = new Intent(
						"com.zoway.parkmanage2.receiver.UpdateUiReceiver");
				i1.putExtra("f", 1);
				PaybillActivity.this.sendBroadcast(i1);
				switch (msg.arg1) {
				case 1:
					PaybillActivity.this.basePrinter.doPrint2(pr.getHphm(),
							pr.getParktime(), pr.getLeavetime(), fare, 1);
					break;
				case 2:
					Toast.makeText(PaybillActivity.this, "修改逃费成功",
							Toast.LENGTH_LONG).show();
					Intent i = new Intent(PaybillActivity.this,
							MainActivity.class);
					PaybillActivity.this.startActivity(i);
					PaybillActivity.this.finish();
					break;
				case 3:
					Toast.makeText(PaybillActivity.this, "修改走费成功",
							Toast.LENGTH_LONG).show();
					Intent j = new Intent(PaybillActivity.this,
							MainActivity.class);
					PaybillActivity.this.startActivity(j);
					PaybillActivity.this.finish();
					break;
				}
				break;
			case 3:
				pDia.dismiss();
				Toast.makeText(PaybillActivity.this, "提交失败", Toast.LENGTH_LONG);
				break;
			}

		}

	};

	private class payThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			PayWsdl wsdl = new PayWsdl();
			PayBean4Wsdl pay = wsdl.whenPay(pr.getRecordno(), "现金", (int) fare);
			if (pay != null) {
				Message msg = new Message();
				msg.what = 2;
				msg.arg1 = 1;
				payHdlr.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = 3;
				payHdlr.sendMessage(msg);
			}
		}
	}

	private class escapeThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			EscapeWsdl wsdl = new EscapeWsdl();
			EscapeBean4Wsdl eb = wsdl.whenCarEscape(pr.getRecordno(),
					LoginBean4Wsdl.getWorker().getWorkerId(), "");
			if (eb != null) {
				Message msg = new Message();
				msg.what = 2;
				msg.arg1 = 2;
				payHdlr.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = 3;
				payHdlr.sendMessage(msg);
			}
		}
	}

	private class ignoreThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			IgnoreWsdl wsdl = new IgnoreWsdl();
			IgnoreBean4Wsdl ib = wsdl.whenCarIngore(pr.getRecordno(),
					LoginBean4Wsdl.getWorker().getWorkerId());
			if (ib != null) {
				Message msg = new Message();
				msg.what = 2;
				msg.arg1 = 3;
				payHdlr.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = 3;
				payHdlr.sendMessage(msg);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paybill);
		Intent intent = this.getIntent();
		Bundle b = intent.getExtras();
		pr = (ParkRecord) b.get("pr");

		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		txtleavetime = (TextView) this.findViewById(R.id.txtleavetime);
		txtmoney = (TextView) this.findViewById(R.id.txtmoney);
		lblcartype = (TextView) this.findViewById(R.id.lblcartype);
		btnsure4bill = (Button) this.findViewById(R.id.btnsure4bill);
		btnsure4escape = (Button) this.findViewById(R.id.btnsure4escape);
		btnsure4print = (Button) this.findViewById(R.id.btnsure4print);
		btnsure4ingore = (Button) this.findViewById(R.id.btnsure4ingore);
		txtcarnumber.setText(pr.getHphm());
		lblcartype.setText(pr.getHpzl());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		txtparktime.setText(sdf.format(pr.getParktime()));
		txtpark.setText(LoginBean4Wsdl.getParkName());
		txtleavetime.setText(sdf.format(pr.getLeavetime()));
		Thread t1 = new Thread(new LeaveThread());
		t1.start();

		btnsure4bill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Message msg = new Message();
					msg.what = 1;
					payHdlr.sendMessage(msg);
					Thread t1 = new Thread(new payThread());
					t1.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btnsure4escape.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Message msg = new Message();
				msg.what = 1;
				payHdlr.sendMessage(msg);
				Thread t1 = new Thread(new escapeThread());
				t1.start();
			}
		});

		btnsure4print.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					PaybillActivity.this.basePrinter.doPrint(pr.getHphm(),
							pr.getParktime(), pr.getRecordno(), 1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btnsure4ingore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = 1;
				payHdlr.sendMessage(msg);
				Thread t1 = new Thread(new ignoreThread());
				t1.start();
			}
		});
		this.setTitle("停车结算");
	}

	private Handler leaveHdlr = new Handler() {
		private ProgressDialog pDia;

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case 1:
				pDia = ProgressDialog.show(PaybillActivity.this, "连接服务器中",
						"请稍候", true, false);
				break;
			case 2:
				pDia.dismiss();
				txtmoney.setText(msg.obj.toString() + "元");
				try {
					fare = Float.parseFloat(msg.obj.toString());
				} catch (Exception e) {
					fare = -1;
				}
				break;
			case 3:
				pDia.dismiss();
				Toast.makeText(PaybillActivity.this, "网络连接不佳，请检查网络连接",
						Toast.LENGTH_LONG).show();
				break;
			}

		}

	};

	private class LeaveThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg1 = new Message();
			msg1.what = 1;
			leaveHdlr.sendMessage(msg1);
			LeaveWsdl wsdl = new LeaveWsdl();
			LeaveBean4Wsdl lb = wsdl.whenCarLeave(pr.getRecordno(),
					LoginBean4Wsdl.getWorker().getWorkerId());

			if (lb != null) {
				msg1 = new Message();
				msg1.what = 2;
				msg1.obj = lb.getFare();
				fare = lb.getFare();
				leaveHdlr.sendMessage(msg1);
			} else {
				msg1 = new Message();
				msg1.what = 3;
				leaveHdlr.sendMessage(msg1);
			}
		}
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent ii = new Intent(this, MainActivity.class);
		this.startActivity(ii);
		this.finish();
	}

	public boolean afterPrint() {
		Intent intent = new Intent(PaybillActivity.this, MainActivity.class);
		PaybillActivity.this.startActivity(intent);
		PaybillActivity.this.finish();
		return true;
	}

}