package com.zoway.parkmanage2.http;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class GetCallWorkerRecordWsdl {

	// 命名空间
	private String nameSpace = "http://tempuri.org/";
	// 调用的方法名称
	private String methodName = "GetCallWorkerRecord";
	// EndPoint
	private String endPoint = BaseUrl.BASETERMINATEURL;
	// SOAP Action
	private String soapAction = "http://tempuri.org/GetCallWorkerRecord";

	public ArrayList<String[]> getCallRecord(int sectionId) {
		ArrayList<String[]> al = null;
		try {
			SoapObject rpc = new SoapObject(nameSpace, methodName);
			rpc.addProperty("sectionId", sectionId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER12);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE transport = new HttpTransportSE(endPoint);
			try {
				transport.call(soapAction, envelope);
			} catch (Exception e) {
				e.printStackTrace();
			}

			SoapObject object = (SoapObject) envelope.bodyIn;
			String result = object.getProperty(0).toString();

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return al;
	}

	public ArrayList<String[]> parseStr(String s) {
		ArrayList<String[]> al = new ArrayList<String[]>();

		return al;
	}

}
