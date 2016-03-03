package com.zoway.parkmanage2.http;

import java.text.SimpleDateFormat;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.zoway.parkmanage2.bean.ParkInfo;
import com.zoway.parkmanage2.bean.ParksDetail;

public class GetAllParksWsdl {

	// 命名空间
	private String nameSpace = "http://tempuri.org/";
	// 调用的方法名称
	private String methodName = "GetAllSeats";
	// EndPoint
	private String endPoint = BaseUrl.BASETERMINATEURL;
	// SOAP Action
	private String soapAction = "http://tempuri.org/GetAllSeats";

	public int whenInitSetALLParks(int terminalId) {
		try {
			SoapObject rpc = new SoapObject(nameSpace, methodName);
			rpc.addProperty("terminalId", terminalId);

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
					SoapObject o2 = (SoapObject) object.getProperty(0);
					int c = o2.getPropertyCount();
					for (int i = 0; i < c; i++) {
						SoapObject o3 = (SoapObject) o2.getProperty(i);
						// String res = o3.getProperty(0).toString();
						ParkInfo pr = new ParkInfo();
						pr.setRecordNo(o3.getPropertySafelyAsString("RecordNo"));
						pr.setParkId(Integer.parseInt(o3
								.getPropertySafelyAsString("SeatId")));
						pr.setParkNo(o3.getPropertySafelyAsString("SeatNo"));
						String strstate = o3.getPropertySafelyAsString("State");
						pr.setState(strstate);
						String strenale = o3
								.getPropertySafelyAsString("Enabled");
						if (strenale.equals("true")) {
							pr.setEnable(true);
						} else {
							pr.setEnable(false);
						}

						String reachTime = o3
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
							pr.setReachTime(sdf.parse(reachTime));
						}
						String leaveTime = o3
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
							pr.setLeaveTime(sdf.parse(leaveTime));
						}
						pr.setHasChanged(true);
						pr.setHasNotified(false);
						ParksDetail.idxLists.add(pr.getParkNo());
						ParksDetail.parkLists.put(pr.getParkNo(), pr);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ParksDetail.parkLists.size();
	}
}
