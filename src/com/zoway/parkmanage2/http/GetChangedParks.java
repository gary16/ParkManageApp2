package com.zoway.parkmanage2.http;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.zoway.parkmanage2.bean.ParkInfo;
import com.zoway.parkmanage2.bean.ParkRecord;
import com.zoway.parkmanage2.bean.ParksDetail;

public class GetChangedParks {

	// �����ռ�
	private String nameSpace = "http://tempuri.org/";
	// ���õķ�������
	private String methodName = "GetChangedSeats";
	// EndPoint
	private String endPoint = BaseUrl.BASETERMINATEURL;
	// SOAP Action
	private String soapAction = "http://tempuri.org/GetChangedSeats";

	public ArrayList<String> whenHaveChangeParks(int terminalId) {
		ArrayList<String> li = new ArrayList<String>();
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

						String reachTime = o3.getPropertySafelyAsString("Time");
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

						ParkInfo pp = ParksDetail.parkLists.get(pr.getParkNo());
						pp.setState(pr.getState());
						pp.setHasNotified(false);
						pp.setReachTime(pr.getReachTime());
						pp.setLeaveTime(pr.getLeaveTime());
						li.add(pp.getParkNo());
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
