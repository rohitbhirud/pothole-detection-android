package org.pk.potholedetection.ui;

import org.pk.potholedetection.ui.controllers.ComplaintListController;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.pk.potholedetection.R;

public class FrgComplaintList extends Fragment {

	private ListView _lvComplaint;
	private ComplaintListController _controller;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.complaint_list, container, false);
		_lvComplaint = (ListView) view.findViewById(R.id.lv_complaint);
		_controller = new ComplaintListController(this);
		_lvComplaint.setAdapter(_controller);

		Button btnAddComplaint = (Button) view.findViewById(R.id.btn_add_complaint);
		btnAddComplaint.setOnClickListener(_controller);

		view.setOnKeyListener(_controller);
		view.setFocusableInTouchMode(true);
		view.requestFocus();

		return view;
	}

	@Override
	public void onResume() {
		_controller.registerListener();
		((ScrMain) getActivity()).setTitle(R.string.complaint_list);
		super.onResume();
	}

	public void gotoHomeScreen() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		FrgHome frgHome = new FrgHome();
		transaction.replace(R.id.container, frgHome);
		transaction.commit();
	}

	public void gotoAddComplaintScreen() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		FrgMakeComplaint fr = new FrgMakeComplaint();
		transaction.replace(R.id.container, fr);
		transaction.commit();
	}

	@Override
	public void onStop() {
		_controller.unregisterListener();
		super.onStop();
	}
}
