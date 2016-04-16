package com.octavio.ordinary.utils;


import com.octavio.ordinary.db.BilldbHelper;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

/**
 * bill
 * @author Octavio
 *
 */
public class RegisterActivityAuto  extends ExpandableListActivity {
	private int mGroupIdColumnIndex;

	private String mPhoneNumberProjection[] = new String[] { People.Phones._ID,
			People.Phones.NUMBER };

	private ExpandableListAdapter mAdapter;

	BilldbHelper billdb;

	Dialog_edit newdialog;
	
	


	private ExpandableListContextMenuInfo info;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("ColaBox-ѡ����Ŀ");
		billdb = new BilldbHelper(this);

		// Query for people
		Cursor groupCursor = billdb.getParentNode();
		// Cache the ID column index
		mGroupIdColumnIndex = groupCursor.getColumnIndexOrThrow("_ID");
    	// Set up our adapter
		mAdapter = new MyExpandableListAdapter(groupCursor, this,
				android.R.layout.simple_expandable_list_item_1,
				android.R.layout.simple_expandable_list_item_1,
				new String[] { "NAME" }, // Name for group layouts
				new int[] { android.R.id.text1 }, new String[] { "NAME" }, //
				new int[] { android.R.id.text1 });
		setListAdapter(mAdapter);
		registerForContextMenu(getExpandableListView());

		//test
		SharedPreferences sharedata = getSharedPreferences("data", 0);
		String data = sharedata.getString("item", null);
	    Log.v("cola","data="+data);
		
	}
	
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
    {
    	Bundle bundle = new Bundle();
    	bundle.putString("name", ((TextView)v).getText().toString());//��bundle д������
    	bundle.putString("id", id+"");
    	
    	Intent mIntent = new Intent();
    	mIntent.putExtras(bundle);
    	setResult(RESULT_OK, mIntent);
    	billdb.close();
    	finish(); 

    	return true;	
    }
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateOptionsMenu(menu);
		if (ExpandableListView
				.getPackedPositionType(((ExpandableListContextMenuInfo) menuInfo).packedPosition) == 1) {
			Log.v("cola", "run menu");
			menu.setHeaderTitle("�˵�");
			menu.add(0, 1, 0, "�� ��");
			menu.add(0, 2, 0, "ɾ ��");
			menu.add(0, 3, 0, "�� ��");
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		info = (ExpandableListContextMenuInfo) item.getMenuInfo();

		if (item.getItemId() == 1) {
			// Log.v("cola","id"+info.id);
			newdialog = new Dialog_edit(this, "������������Ŀ������", "",
					mDialogClick_new);
			newdialog.show();
		} else if (item.getItemId() == 2) {
			new AlertDialog.Builder(this).setTitle("��ʾ").setMessage("ȷ��Ҫɾ��'"+((TextView)info.targetView).getText().toString()+"'�����Ŀ��?")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									billdb.Acctitem_delitem((int)info.id);
									updatedisplay();
								}
							}).setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// ȡ����ť�¼�
								}
							}).show();

		} else if (item.getItemId() == 3) {
			newdialog = new Dialog_edit(this, "���޸���Ŀ����",
					((TextView) info.targetView).getText().toString(),
					mDialogClick_edit);
			newdialog.show();
		}

		return false;
	}

	private Dialog_edit.OnDateSetListener mDialogClick_new = new Dialog_edit.OnDateSetListener() {
		public void onDateSet(String text) {
			Log.v("cola", "new acctitem");
			billdb.Acctitem_newitem(text,ExpandableListView.getPackedPositionGroup(info.packedPosition));
			updatedisplay();
		}

	};
	
	private Dialog_edit.OnDateSetListener mDialogClick_edit = new Dialog_edit.OnDateSetListener() {
		public void onDateSet(String text) {			
			billdb.Acctitem_edititem(text,(int)info.id);
			updatedisplay();
		}

	};

	private void updatedisplay(){
		Log.v("cola", "update display");
		((MyExpandableListAdapter)mAdapter).notifyDataSetChanged();

	}
	
	public class MyExpandableListAdapter extends SimpleCursorTreeAdapter {

		public MyExpandableListAdapter(Cursor cursor, Context context,
				int groupLayout, int childLayout, String[] groupFrom,
				int[] groupTo, String[] childrenFrom, int[] childrenTo) {
			super(context, cursor, groupLayout, groupFrom, groupTo,
					childLayout, childrenFrom, childrenTo);
		}

		@Override
		protected Cursor getChildrenCursor(Cursor groupCursor) {

			String pid = groupCursor.getLong(mGroupIdColumnIndex) + "";
			// Log.v("cola","pid="+pid);
	 
			return billdb.getChildenNode(pid);

		}

		@Override
		public long getGroupId(int groupPosition) {
			// Log.v("cola", "getGroupId " + groupPosition);
			Cursor groupCursor = (Cursor) getGroup(groupPosition);
			return groupCursor.getLong(mGroupIdColumnIndex);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// Log.v("cola", "getChildId " + groupPosition + "," +
			// childPosition);
			Cursor childCursor = (Cursor) getChild(groupPosition, childPosition);
			return childCursor.getLong(0);
		}

	}
}
