package com.example.suppayyyyyyya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.whty.bluetooth.manage.util.BluetoothStruct;

import java.util.ArrayList;

public class DeviceAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<BluetoothStruct> devices;
	private Context mContext;

	public DeviceAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public void setDeviceList(ArrayList<BluetoothStruct> list) {
		if (list != null) {
			devices = (ArrayList<BluetoothStruct>) list.clone();
			notifyDataSetChanged();
		}
	}

	public void clearDeviceList() {
		if (devices != null) {
			devices.clear();
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return devices == null ? 0 : devices.size();

	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.dialog_listitem, null);
			holder = new ViewHolder();
			holder.checktv_title = (CheckedTextView) convertView
					.findViewById(R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (devices != null) {
			holder.checktv_title.setText(devices.get(position).getName());
		}

		return convertView;

	}

	class ViewHolder {
		CheckedTextView checktv_title;
	}

}
