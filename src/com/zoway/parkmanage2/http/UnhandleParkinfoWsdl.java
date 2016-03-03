package com.zoway.parkmanage2.http;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.zoway.parkmanage2.bean.ParkRecord;

public class UnhandleParkinfoWsdl {

	// 命名空间
	private String nameSpace = "http://tempuri.org/";
	// 调用的方法名称
	private String methodName = "GetUnhandledParkRecords";
	// EndPoint
	private String endPoint = BaseUrl.BASETERMINATEURL;
	// SOAP Action
	private String soapAction = "http://tempuri.org/GetUnhandledParkRecords";

	public ArrayList<ParkRecord> whenGetUnhandle(int sectionId, int seatId,
			int pageSize, int pageIndex) {
		ArrayList<ParkRecord> li = null;
		try {
			SoapObject rpc = new SoapObject(nameSpace, methodName);
			rpc.addProperty("sectionId", sectionId);
			rpc.addProperty("seatId", seatId);
			rpc.addProperty("pageSize", pageSize);
			rpc.addProperty("pageIndex", pageIndex);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER12);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(endPoint);
			try {
				transport.call(soapAction, envelope);
				SoapObject object = (SoapObject) envelope.bodyIn;
				String result = object.getProperty(0).toString();

				if (result.indexOf("Exception") > 0) {
					int ii = 0;
				} else {
					li = new ArrayList<ParkRecord>();
					SoapObject o2 = (SoapObject) object.getProperty(0);
					SoapObject o3 = (SoapObject) o2.getProperty(0);
					int c = o3.getPropertyCount();
					for (int i = 0; i < c; i++) {
						SoapObject o4 = (SoapObject) o3.getProperty(i);
						// String res = o3.getProperty(0).toString();
						ParkRecord pr = new ParkRecord();
						pr.setParkno(o4.getPropertySafelyAsString("SeatNo"));
						pr.setRecordno(o4.getPropertySafelyAsString("RecordNo"));
						pr.setHphm(o4.getPropertySafelyAsString("VehicleNo"));
						pr.setHpzl(o4.getPropertySafelyAsString("VehicleType"));
						pr.setParkState(o4
								.getPropertySafelyAsString("ParkState"));
						String reachTime = o4
								.getPropertySafelyAsString("ReachTime");
						if (!reachTime.equals("")) {
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							int ii = reachTime.indexOf(".");
							if (ii > 0) {
								reachTime = reachTime.substring(0, ii).replace(
										"T", " ");
							} else {
								reachTime = reachTime.replace("T", " ");
							}
							pr.setParktime(sdf.parse(reachTime));
						}
						String leaveTime = o4
								.getPropertySafelyAsString("LeaveTime");
						if (!leaveTime.equals("")) {
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							int ii = leaveTime.indexOf(".");
							if (ii > 0) {
								leaveTime = leaveTime.substring(0, ii).replace(
										"T", " ");
							} else {
								leaveTime = leaveTime.replace("T", " ");
							}
							pr.setLeavetime(sdf.parse(leaveTime));
						}
						li.add(pr);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return li;
	}
}
