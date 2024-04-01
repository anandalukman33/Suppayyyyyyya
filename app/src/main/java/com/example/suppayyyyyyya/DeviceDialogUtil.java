package com.example.suppayyyyyyya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.suppayyyyyyya.UIMessage;
import com.whty.bluetooth.manage.util.BluetoothStruct;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class DeviceDialogUtil {

	private Handler handler;
	private Dialog mDialog;
	// private ArrayAdapter<BluetoothStruct> adapter = null;
	// private ArrayAdapter<String> adapter = null;
	private DeviceAdapter adapter = null;
	private BluetoothDevice device;
	private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
	private int No_Device_Selected = 95;
	private int Device_Ensured = 99;
	private ArrayList<BluetoothStruct> items;
	private ListView listView;

	public DeviceDialogUtil(Handler handler) {
		super();
		this.handler = handler;
		device = null;
	}

	private void createDialog(final Context context) {
		device = null;
		LayoutInflater factory = LayoutInflater.from(context);
		final View textEntryView = factory.inflate(R.layout.dialog_listview,
				null);
		listView = (ListView) textEntryView.findViewById(R.id.dl_listview);

		// adapter = new ArrayAdapter<BluetoothStruct>(context,
		// android.R.layout.select_dialog_singlechoice,
		// BlueToothDeviceReceiver.items);
		// adapter = new ArrayAdapter<String>(context,
		// android.R.layout.select_dialog_singlechoice,
		// BlueToothDeviceReceiver.itemsNames);

		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		adapter = new DeviceAdapter(context);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				device = BlueToothDeviceReceiver.items.get(arg2).getDevice();
			}
		});

		mDialog = new AlertDialog.Builder(context)
				.setTitle(UIMessage.device_list_title)
				.setPositiveButton(UIMessage.device_list_positive,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								btAdapter.cancelDiscovery();
								// BlueToothUtil.items.clear();
								BlueToothDeviceReceiver.items.clear();
								BlueToothDeviceReceiver.itemsNames.clear();
								// adapter.notifyDataSetChanged();
								adapter.clearDeviceList();
								btAdapter.startDiscovery();
								dismissDialog(mDialog, false);
							}
						})
				.setNegativeButton(UIMessage.device_list_negative,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if (device != null) {
									handler.obtainMessage(Device_Ensured,
											device).sendToTarget();
									dismissDialog(mDialog, true);
									// BlueToothUtil.items.clear();
									BlueToothDeviceReceiver.items.clear();
									BlueToothDeviceReceiver.itemsNames.clear();
									adapter.clearDeviceList();
									mDialog = null;
									btAdapter.cancelDiscovery();

								} else {
									dismissDialog(mDialog, false);
									handler.obtainMessage(No_Device_Selected)
											.sendToTarget();
								}
							}
						}).setView(textEntryView).create();
		mDialog.setCancelable(false);
		mDialog.show();

		mDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					btAdapter.cancelDiscovery();
					dismissDialog(mDialog, true);
					// BlueToothUtil.items.clear();
					BlueToothDeviceReceiver.items.clear();
					BlueToothDeviceReceiver.itemsNames.clear();
					adapter.clearDeviceList();
					mDialog = null;
					return true;
				}
				return false;
			}

		});
	}

	/**
	 * 
	 * 
	 * @param dialog
	 * @param flag
	 */
	private void dismissDialog(Dialog dialog, boolean flag) {
		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, flag);
			dialog.dismiss();
		} catch (Exception e) {

		}
	}

	// public void listDevice(final Context context, final Map<String,
	// BluetoothDevice> map, final Handler handler) {
	// if (mDialog != null) {
	// notifyDataChange(map);
	// } else {
	// createDialog(context);
	// notifyDataChange(map);
	// }
	// }

	public void listDevice(final Context context) {
		if (mDialog == null) {
			createDialog(context);
		}

		if (listView != null) {

			adapter.setDeviceList(BlueToothDeviceReceiver.items);

		}
	}

	// public void notifyDataChange(final Map<String, BluetoothDevice> map) {
	// for (String key : map.keySet().toArray(new String[] {})) {
	// String name = map.get(key).getName();
	// String mac = map.get(key).getAddress();
	// BluetoothDevice device = map.get(key);
	// BluetoothStruct bluetoothStruct = new BluetoothStruct(name, mac, device);
	// if (name != null && mac != null && device != null) {
	// int index = findBluetoothDevice(mac, items);
	// if (index < 0) {
	// items.add(bluetoothStruct);
	// } else {
	// items.get(index).setDevice(device);
	// }
	// }
	// }
	// adapter.notifyDataSetChanged();
	// }

	// private int findBluetoothDevice(String mac, ArrayList<BluetoothStruct>
	// item) {
	// for (int i = 0; i < item.size(); i++) {
	// if (item.get(i).getMac().equals(mac))
	// return i;
	// }
	// return -1;
	// }

	// private class BluetoothStruct
	// {
	// private String name = "";
	// private String mac = "";
	// private BluetoothDevice device = null;
	//
	// public BluetoothStruct(String name, String mac, BluetoothDevice device)
	// {
	// super();
	// this.name = name;
	// this.mac = mac;
	// this.device = device;
	// }
	//
	// @Override
	// public String toString()
	// {
	// return name;
	// }
	//
	//
	// public String getName() {
	// return name;
	// }
	//
	// public BluetoothDevice getDevice()
	// {
	// return device;
	// }
	//
	// public void setDevice(BluetoothDevice device)
	// {
	// this.device = device;
	// }
	//
	// public String getMac()
	// {
	// return mac;
	// }
	//
	// }
}
