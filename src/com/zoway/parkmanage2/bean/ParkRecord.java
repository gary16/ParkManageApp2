package com.zoway.parkmanage2.bean;

import java.io.Serializable;
import java.util.Date;

public class ParkRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int tid;
	private int recoidid;
	private String recordno;
	private int parkid;
	private String parkno;
	private String hphm;
	private String hpzl;
	private String hphmcolor;
	private Date parktime;
	private Date leavetime;
	private float fees;
	private int status;
	private String filepath;
	private String parkState;
	int isprint;

	public String getParkState() {
		return parkState;
	}

	public void setParkState(String parkState) {
		this.parkState = parkState;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getHpzl() {
		return hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public int getRecoidid() {
		return recoidid;
	}

	public void setRecoidid(int recoidid) {
		this.recoidid = recoidid;
	}

	public String getRecordno() {
		return recordno;
	}

	public void setRecordno(String recordno) {
		this.recordno = recordno;
	}

	public int getParkid() {
		return parkid;
	}

	public void setParkid(int parkid) {
		this.parkid = parkid;
	}

	public String getParkno() {
		return parkno;
	}

	public void setParkno(String parkno) {
		this.parkno = parkno;
	}

	public String getHphm() {
		return hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public String getHphmcolor() {
		return hphmcolor;
	}

	public void setHphmcolor(String hphmcolor) {
		this.hphmcolor = hphmcolor;
	}

	public Date getParktime() {
		return parktime;
	}

	public void setParktime(Date parktime) {
		this.parktime = parktime;
	}

	public Date getLeavetime() {
		return leavetime;
	}

	public void setLeavetime(Date leavetime) {
		this.leavetime = leavetime;
	}

	public float getFees() {
		return fees;
	}

	public void setFees(float fees) {
		this.fees = fees;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIsprint() {
		return isprint;
	}

	public void setIsprint(int isprint) {
		this.isprint = isprint;
	}

}
