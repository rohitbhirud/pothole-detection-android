package org.pk.potholedetection.ui.controllers;

import java.util.ArrayList;

import org.pk.potholedetection.datamodels.ComplaintList;
import org.pk.potholedetection.datamodels.ComplaintReply;
import org.pk.potholedetection.eventnotification.EventNotifier;
import org.pk.potholedetection.eventnotification.EventTypes;
import org.pk.potholedetection.eventnotification.IEventListener;
import org.pk.potholedetection.eventnotification.ListenerPriority;
import org.pk.potholedetection.eventnotification.NotifierFactory;
import org.pk.potholedetection.ui.FrgComplaintList;
import org.pk.potholedetection.utilities.AppSettings;
import org.pk.potholedetection.utilities.CustomProgressDialog;
import org.pk.potholedetection.webservices.GetComplaintList;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.pk.potholedetection.R;

public class ComplaintListController extends BaseAdapter implements IEventListener, OnClickListener, OnKeyListener {
	private ArrayList<ComplaintReply> _list;
	private final FrgComplaintList _fragment;
	private final LayoutInflater _inflater;

	public ComplaintListController(FrgComplaintList fragment) {
		_fragment = fragment;
		_inflater = (LayoutInflater) _fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_initComplaintList();
		notifyDataSetChanged();
		GetComplaintList list = new GetComplaintList(AppSettings.getInstance().getUserId());
		list.send();
	}

	private void _initComplaintList() {
		_list = new ArrayList<ComplaintReply>(ComplaintList.getInstance().getList());
		if (_list.size() == 0) {
			CustomProgressDialog.show(_fragment.getActivity(), R.string.loading_list);
		}
	}

	@Override
	public int getCount() {
		return _list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return _list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = _inflater.inflate(R.layout.row, parent, false);
		}

		convertView = _updateView(convertView, position);
		return convertView;
	}

	private View _updateView(View view, int position) {
		ComplaintReply reply = _list.get(position);

		String type = reply.getComplaintType();
		String title = reply.getTitle();
		String desc = reply.getDescription();
		String comment = reply.getComment();
		String status = reply.getStatus();

		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setText("Title : " + title);

		TextView tvType = (TextView) view.findViewById(R.id.tv_type);
		tvType.setText("Type : " + type);

		TextView tvDesc = (TextView) view.findViewById(R.id.tv_desc);
		tvDesc.setText("Description : " + desc);

		TextView tvComm = (TextView) view.findViewById(R.id.tv_comment);
		if (!TextUtils.isEmpty(comment)) {
			tvComm.setVisibility(View.VISIBLE);
			tvComm.setText("Reply : " + comment);
		} else {
			tvComm.setVisibility(View.GONE);
		}

		TextView tvStatus = (TextView) view.findViewById(R.id.tv_status);
		if (!TextUtils.isEmpty(status)) {
			tvStatus.setVisibility(View.VISIBLE);
			tvStatus.setText("Status : " + status);
		} else {
			tvStatus.setVisibility(View.GONE);
		}

		return view;
	}

	private void _updateList() {
		_list = ComplaintList.getInstance().getList();
		_fragment.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}

	@Override
	public int eventNotify(int eventType, Object eventObject) {
		switch (eventType) {
		case EventTypes.COMPLAINT_LIST_SUCCESS:
			CustomProgressDialog.dismiss(_fragment.getActivity());
			_updateList();
			break;

		case EventTypes.COMPLAINT_LIST_FAILED:
			CustomProgressDialog.dismiss(_fragment.getActivity());
			break;

		}
		return 0;
	}

	@Override
	public void registerListener() {
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.COMPLAINT_NOTIFIER);
		notifier.registerListener(ComplaintListController.this, ListenerPriority.HIGH);

	}

	@Override
	public void unregisterListener() {
		EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.COMPLAINT_NOTIFIER);
		notifier.unregisterListener(ComplaintListController.this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_complaint:
			_fragment.gotoAddComplaintScreen();
			break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				_fragment.gotoHomeScreen();
				return true;
			}
		}
		return false;
	}

}
