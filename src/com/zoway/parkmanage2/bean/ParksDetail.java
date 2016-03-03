package com.zoway.parkmanage2.bean;

import java.util.ArrayList;
import java.util.Hashtable;

public class ParksDetail {
	public static ArrayList<String> idxLists;
	public static Hashtable<String, ParkInfo> parkLists;
	static {
		idxLists = new ArrayList<String>(32);
		parkLists = new Hashtable<String, ParkInfo>(32);
	}

}
