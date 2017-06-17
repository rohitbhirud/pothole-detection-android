package org.pk.potholedetection.datamodels;

import java.util.ArrayList;

public class ComplaintList {

	private static ComplaintList _instance;
	protected ArrayList<ComplaintReply> _list = new ArrayList<ComplaintReply>();

	protected ComplaintList() {

	}

	public static ComplaintList getInstance() {
		if (_instance == null) {
			_instance = new ComplaintList();
		}
		return _instance;
	}

	public void setList(ArrayList<ComplaintReply> list) {
		if (list != null && list.size() > 0) {
			_list = list;
		}
	}

	public ArrayList<ComplaintReply> getList() {
		return _list;
	}

}
