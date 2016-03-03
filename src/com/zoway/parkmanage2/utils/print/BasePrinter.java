package com.zoway.parkmanage2.utils.print;

import java.util.Date;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;

import com.zoway.parkmanage2.view.PrintActivity;

public abstract class BasePrinter {

	public PrintActivity activity;
	public ProgressDialog printDia;
	public Handler printHandler;

	protected BasePrinter(PrintActivity activity) {
		this.activity = activity;
	}

	public void doBeforeCreate() {
	};

	public void doBeforeStart() {
	};

	public void doBeforeRestart() {
	};

	public void doBeforeResume() {
	};

	public void doBeforePause() {
	};

	public void doBeforeStop() {
	};

	public void doBeforeDestory() {
	};

	public void doBeforeBackPressed() {
	};

	public void doAfterCreate() {
	};

	public void doAfterStart() {
	};

	public void doAfterRestart() {
	};

	public void doAfterResume() {
	};

	public void doAfterPause() {
	};

	public void doAfterStop() {
	};

	public void doAfterDestory() {
	};

	public void doAfterBackPressed() {
	};

	public void doBeforeActivityResult(int requestCode, int resultCode,
			Intent data) {
	};

	public void doAfterActivityResult(int requestCode, int resultCode,
			Intent data) {
	};

	public void doPrint(String hphm, Date parktime, String recno, int fromWhere) {
	}

	public void doPrint2(String hphm, Date parktime, Date leavetime,
			float fare, int fromWhere) {
	}

}
