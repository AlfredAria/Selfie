package com.example.selfie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.net.Uri;

public class MainActivity extends ListActivity {
	public static final String TAG="MainActivity:";
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	private static final int ALARM_DELAY = 1000 * 40;	
	private static final int ALARM_INTERVAL_1_MINUTE_IN_MILLIS = 1000 * 120;
	private static Uri fileUri;
	private static int fileCount = 0;
	private static SelfieListAdapter madapter;
	private static File imagesFolder;
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");	
	private static PendingIntent mPendingIntent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize the list adapter and the external file path
		imagesFolder = new File(Environment.getExternalStorageDirectory(), "Selfies");
		imagesFolder.mkdirs();
		madapter = new SelfieListAdapter(this, getFilesFromExternalMemory());
		this.setListAdapter(madapter);

		// Set periodic alarm
		Intent intent = new Intent(getApplicationContext(), AlarmNotificationReceiver.class);
		mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + ALARM_DELAY,
				ALARM_INTERVAL_1_MINUTE_IN_MILLIS,
				mPendingIntent);
		
		// Set ListView onItemClickListener
		ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				Uri uri = ((SelfieItem)madapter.getItem(position)).getImageUri();
				Intent intent = new Intent (MainActivity.this, SelfieDetailActivity.class);
				intent.putExtra("fileURI", uri);
				startActivity(intent);
			}
			
		});
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult");
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//			Uri pictureUri = data.getData(); // Android does not return picture pointer
											 // as MediaStore.EXTRA_OUTPUT is specified
			madapter.add(new SelfieItem(fileUri));				
		}
	}
	
	protected ArrayList<Uri> getFilesFromExternalMemory() {
		if (imagesFolder == null) return null;
		// Check the folder for existing files
		ArrayList<Uri> uris = new ArrayList<Uri>();
		File[] listOfImages = imagesFolder.listFiles();
		for (File f : listOfImages) {
			Log.i(TAG, "Image file: " + f.getAbsolutePath());
			uris.add(Uri.fromFile(f));
		} return uris;		
	}

	protected void takePicture() {
		Log.i(TAG,"MainActivity.takePicture");
		// Delete previously taken image from disk
//		if (fileUri != null) {
//			getContentResolver().delete(fileUri, null,null);
//			fileUri = null;
//		}		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		
		File image = new File (imagesFolder, dateFormat.format(Calendar.getInstance().getTime()) + ".jpg");
		fileUri = Uri.fromFile(image);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}
	
	protected void cancelAlarm() {
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		if (am != null) am.cancel(mPendingIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.i(TAG,"MainActivity.onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG,"MainActivity.onOptionsItemSelected");
		switch(item.getItemId()) {
			case R.id.camera_button:
				takePicture();
				return true;
			case R.id.delete_selfies:
				madapter.removeAllViews();
				return true;
			case R.id.cancel_alarm:
				return true;
		}
	    return  false;
	}
}
