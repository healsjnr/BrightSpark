package com.healsjnr.brightspark.lib.ui;

import java.util.List;

import com.healsjnr.brightspark.lib.SimpleAddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

public class UITools {
	
	public static AlertDialog createAddressDialog(Activity parentActivity, List<SimpleAddress> addresses, DialogInterface.OnClickListener listener)
	{
		ArrayAdapter<SimpleAddress> adapter = new ArrayAdapter<SimpleAddress>(
				parentActivity, android.R.layout.simple_list_item_1, addresses);

		AlertDialog dialog = new AlertDialog.Builder(parentActivity)
				.setTitle("Which address did you mean?")
				.setSingleChoiceItems(adapter, 0, listener)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).create();

		return dialog;
		
	}

}
