package com.zoway.parkmanage2.service;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import com.zoway.parkmanage2.R;
import com.zoway.parkmanage2.bean.LoginBean4Wsdl;
import com.zoway.parkmanage2.bean.ParkInfo;
import com.zoway.parkmanage2.bean.ParksDetail;
import com.zoway.parkmanage2.http.GetChangedParks;
import com.zoway.parkmanage2.view.ParkInfoActivity;

public class TerminalService extends Service {

	private boolean flg1 = true;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Thread t1 = new Thread(new UpdateUITask());
		t1.start();

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);

	}

	private void updateAllNotify() {
		for (int i = 0; i < ParksDetail.idxLists.size(); i++) {
			String pno = ParksDetail.idxLists.get(i);
			ParkInfo pi = ParksDetail.parkLists.get(pno);
			if (pi.isHasNotified()) {

			} else {
				if (this.checkPark(pi)) {
					int nid = Integer.parseInt(pno);
					NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					nm.cancel(nid);
					Intent it1 = new Intent(TerminalService.this,
							ParkInfoActivity.class);
					it1.putExtra("parkNo", pi.getParkNo());
					it1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					PendingIntent pt1 = PendingIntent.getActivity(
							TerminalService.this, nid, it1,
							PendingIntent.FLAG_UPDATE_CURRENT);

					Uri u = RingtoneManager
							.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

					Notification noti = new Notification.Builder(
							TerminalService.this)
							.setContentTitle(
									String.format("车位位置:%s", pi.getParkNo()))
							.setContentText(
									String.format("车辆:%s", pi.getState()))
							.setSmallIcon(R.drawable.ic_launcher).setSound(u)
							.setContentIntent(pt1).build();

					noti.defaults |= Notification.DEFAULT_ALL;
					nm.notify(nid, noti);
					pi.setHasNotified(true);
				}

			}

		}
	}

	private boolean checkPark(ParkInfo pi) {
		boolean b = false;
		if (pi.getState().equals("待确认停车") || pi.getState().equals("待确认离开")) {
			b = true;
		} else {

		}
		return b;
	}

	private class UpdateUITask implements Runnable {

		public void run() {
			while (flg1) {
				try {
					Thread.sleep(20000);
					GetChangedParks gcp = new GetChangedParks();
					ArrayList<String> li = gcp
							.whenHaveChangeParks(LoginBean4Wsdl.getTerminalId());
					updateAllNotify();
					Intent intent = new Intent(
							"com.zoway.parkmanage2.receiver.UpdateUiReceiver");
					TerminalService.this.sendBroadcast(intent);

				} catch (Exception er) {

				}
			}
		}
	}
}
