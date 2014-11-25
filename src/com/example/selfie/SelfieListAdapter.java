package com.example.selfie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfieListAdapter extends BaseAdapter {

	public final static String TAG = "SelfieListAdapter:";
	private ArrayList<SelfieItem> list = new ArrayList<SelfieItem>();
	private static LayoutInflater inflater = null;
	private Context mContext;

	public SelfieListAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public SelfieListAdapter(Context context, ArrayList<Uri> storedFiles) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		if (storedFiles != null) {
			for (Uri uri : storedFiles) {
				list.add(new SelfieItem(uri));
			} notifyDataSetChanged();
		}
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		Log.i(TAG, "getView");
		View newView = convertView;
		ViewHolder holder;

		SelfieItem curr = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.selfie_view, null);
			holder.selfiePicture = (ImageView) newView.findViewById(R.id.selfie_picture_view);
			holder.dateTaken = (TextView) newView.findViewById(R.id.selfie_date);
			newView.setTag(holder);
			
		} else {
			holder = (ViewHolder) newView.getTag();
		}

		Bitmap currentBitmap = null;
		try {
			currentBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), curr.getImageUri());
			holder.selfiePicture.setImageBitmap(currentBitmap);
			holder.dateTaken.setText(curr.getDateString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return newView;
	}
	
	static class ViewHolder {
	
		ImageView selfiePicture;
		TextView  dateTaken;
		
	}

	public void add(SelfieItem listItem) {
		Log.i(TAG,"add selfie item" + listItem.getImageUri().toString());
		list.add(listItem);
		notifyDataSetChanged();
	}
	
	public ArrayList<SelfieItem> getList(){
		return list;
	}
	
	public void removeAllViews(){
		Log.i(TAG,"remove all items");
		for (SelfieItem item : list) {
			if (item.getImageUri() != null) {
				Log.i(TAG,"Removing from " + item.getImageUri().toString());
				if(false == new File(item.getImageUri().getPath()).delete()){
					Log.i(TAG, "No file named " + item.getImageUri().getPath() +" was deleted.");
				}
			}		
		}
		list.clear();
		this.notifyDataSetChanged();
	}
}
