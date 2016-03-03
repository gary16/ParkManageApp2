package com.zoway.parkmanage2.utils;

import android.util.Log;

public class LogUtils {

	public static void i(Object o, String msg) {
		Class c = (Class) o;
		Log.i(c.getSimpleName(), msg);
	}
}
