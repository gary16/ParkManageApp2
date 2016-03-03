package com.zoway.parkmanage2.bean;

import java.io.Serializable;
import java.util.Date;

public class ParkInfo implements Serializable {

	private String recordNo;
	private int parkId;
	private String parkNo;
	private String hphm;
	private String hpzl;
	private int tagId;
	private String State;
	private boolean hasNotified = false;
	private boolean enable;
	private boolean hasChanged;
	private Date reachTime;
	private Date leaveTime;

	private static final long serialVersionUID = 1L;

	public boolean isHasNotified() {
		return hasNotified;
	}

	public void setHasNotified(boolean hasNotified) {
		this.hasNotified = hasNotified;
	}

	public String getRecordNo() {
		return recordNo;
	}

	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}

	public String getHphm() {
		return hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public String getHpzl() {
		return hpzl;
	}

	public void setHpzl(String hpzl) {
		this.hpzl = hpzl;
	}

	public boolean isHasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	public int getParkId() {
		return parkId;
	}

	public void setParkId(int parkId) {
		this.parkId = parkId;
	}

	public String getParkNo() {
		return parkNo;
	}

	public void setParkNo(String parkNo) {
		this.parkNo = parkNo;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Date getReachTime() {
		return reachTime;
	}

	public void setReachTime(Date reachTime) {
		this.reachTime = reachTime;
	}

	public Date getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(Date leaveTime) {
		this.leaveTime = leaveTime;
	}

}
