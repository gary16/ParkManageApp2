package com.zoway.parkmanage2.receiver;

import com.zoway.parkmanage2.view.MainActivity.ImageAdApter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdateUiReceiver extends BroadcastReceiver {

	private ImageAdApter iad;

	public UpdateUiReceiver(ImageAdApter iad) {
		this.iad = iad;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		iad.notifyDataSetChanged();
	}
}
