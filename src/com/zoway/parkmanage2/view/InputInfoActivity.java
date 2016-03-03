package com.zoway.parkmanage2.view;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.zoway.parkmanage2.R;
import com.zoway.parkmanage2.bean.LoginBean4Wsdl;
import com.zoway.parkmanage2.bean.ParkBean4Wsdl;
import com.zoway.parkmanage2.bean.ParksDetail;
import com.zoway.parkmanage2.http.ParkWsdl;
import com.zoway.parkmanage2.image.BitmapHandle;
import com.zoway.parkmanage2.service.TerminalService;
import com.zoway.parkmanage2.utils.PathUtils;
import com.zoway.parkmanage2.utils.TimeUtil;

public class InputInfoActivity extends PrintActivity implements OnClickListener {

	public final int REQIMG1 = 0X01;
	public final int REQIMG2 = 0X02;
	public final int REQIMG3 = 0X03;
	private String rcid = null;
	private String rcno = null;
	private String sno = null;
	private String rt = null;
	private Date parkTime = null;
	private String hphm = null;
	private String hpzl = null;
	private String fn = null;
	private int type = 0;
	private String img_path = PathUtils.getSdPath();

	// size 48*48
	private ImageButton img1;
	private Button infosure;
	private Button btninfocancel;
	private Bitmap bitmapSelected1 = null;
	private Bitmap bitmapOcr = null;
	private ProgressDialog pDia;
	private TextView txtparktime;
	private TextView txtcarnumber;
	private TextView txtcartype;
	private TextView txtpark;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键

		}
		return super.onKeyDown(keyCode, event);
	}

	private Handler infoHdlr = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				pDia = ProgressDialog.show(InputInfoActivity.this, "正在提交",
						"请稍后");
				break;
			case 2:
				pDia.dismiss();
				InputInfoActivity.this.basePrinter.doPrint(hphm, parkTime,
						rcno, 1);
				break;
			case 3:
				pDia.dismiss();
				Toast.makeText(InputInfoActivity.this, "提交失败",
						Toast.LENGTH_LONG);
				break;
			}

		}

	};

	private class parkThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ParkWsdl wsdl = new ParkWsdl();
			ParkBean4Wsdl park = wsdl.whenCarIn(rcno, LoginBean4Wsdl
					.getTerminalId(), LoginBean4Wsdl.getWorker().getWorkerId(),
					"小型汽车", hphm, parkTime);
			if (park != null && park.getReachTime() != null) {
				Message msg = new Message();
				msg.what = 2;
				infoHdlr.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = 3;
				infoHdlr.sendMessage(msg);
			}
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.parkimg1:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(img_path, "p1ori.jpg")));
			startActivityForResult(intent, REQIMG1);
			break;
		case R.id.btninfosure:
			try {
				InputStream is = getContentResolver().openInputStream(
						Uri.fromFile(new File(fn)));
				bitmapOcr = BitmapHandle.getReduceBitmap(is, false, 5, 0);
				BitmapHandle.writeJpgFromBitmap(img_path + File.separator
						+ "p2.jpg", bitmapOcr, 90);
				File f = new File(fn);
				if (f.exists()) {
					f.delete();
				}
				Message msg = new Message();
				msg.what = 1;
				infoHdlr.sendMessage(msg);
				Thread thr = new Thread(new parkThread());
				thr.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.btninfocancel:
			this.onBackPressed();
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_info);
		Intent intent = this.getIntent();
		rcid = intent.getStringExtra("rcid");

		sno = intent.getStringExtra("sno");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		type = intent.getIntExtra("type", 0);
		if (type == 4) {
			rt = intent.getStringExtra("rt").replace(" ", "").replace("\t", "")
					.replace("-", "").replace(":", "");
			try {
				parkTime = sdf.parse(rt);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rcno = intent.getStringExtra("rcno");
		} else {
			parkTime = TimeUtil.getTime();
			String uuid = java.util.UUID.randomUUID().toString();
			rcno = uuid;
		}
		hphm = intent.getStringExtra("hphm");
		hpzl = intent.getStringExtra("hpzl");
		fn = intent.getStringExtra("fn");

		img_path = img_path + File.separator + sdf.format(parkTime) + hphm
				+ File.separator;
		File fDir = new File(img_path);
		if (!fDir.exists()) {
			fDir.mkdirs();
		}
		img1 = (ImageButton) this.findViewById(R.id.parkimg1);
		infosure = (Button) this.findViewById(R.id.btninfosure);
		btninfocancel = (Button) this.findViewById(R.id.btninfocancel);
		txtpark = (TextView) this.findViewById(R.id.txtpark);
		txtparktime = (TextView) this.findViewById(R.id.txtparktime);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		txtparktime.setText(sdf1.format(parkTime));
		txtcarnumber = (TextView) this.findViewById(R.id.txtcarnumber);
		txtcartype = (TextView) this.findViewById(R.id.txtcartype);
		txtpark.setText(sno);
		txtcarnumber.setText(hphm);
		txtcartype.setText(hpzl);
		infosure.setOnClickListener(this);
		btninfocancel.setOnClickListener(this);
		this.setTitle("资料录入");
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED)
			return;
		else {
			switch (requestCode) {
			case REQIMG1:
				try {
					InputStream is = getContentResolver().openInputStream(
							Uri.fromFile(new File(img_path, "p1ori.jpg")));
					bitmapSelected1 = BitmapHandle.getReduceBitmap(is, false,
							5, 90);
					this.img1.setScaleType(ScaleType.FIT_XY);
					this.img1.setImageBitmap(bitmapSelected1);
					BitmapHandle.writeJpgFromBitmap(img_path + File.separator
							+ "p1.jpg", bitmapSelected1, 90);
					File f = new File(img_path + File.separator + "p1ori.jpg");
					if (f.exists()) {
						f.delete();
					}

				} catch (Exception er) {
					er.printStackTrace();
				} finally {
				}
				break;
			default:
				break;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
		// unbindDeviceService();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// bindDeviceService();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (bitmapSelected1 != null && !bitmapSelected1.isRecycled()) {
			bitmapSelected1.recycle();
			bitmapSelected1 = null;

		}
		if (bitmapOcr != null && !bitmapOcr.isRecycled()) {
			bitmapOcr.recycle();
			bitmapOcr = null;
		}
		setContentView(R.layout.view_null);
		System.gc();
	}

	public boolean afterPrint() {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(Integer.parseInt(sno));
		ParksDetail.parkLists.get(sno).setState("在停");
		Intent i1 = new Intent(
				"com.zoway.parkmanage2.receiver.UpdateUiReceiver");
		i1.putExtra("f", 1);
		this.sendBroadcast(i1);
		Intent intent = new Intent(InputInfoActivity.this, MainActivity.class);
		InputInfoActivity.this.startActivity(intent);
		InputInfoActivity.this.finish();
		return true;
	}

}
