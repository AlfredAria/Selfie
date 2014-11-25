package com.example.selfie;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class SelfieDetailActivity extends Activity {
	private final static String TAG = "SelfieDetailActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "detail view activity created");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selfie_detail_view);
		ImageView imageView = (ImageView) findViewById(R.id.selfie_detail_view);
		
		Intent intent = getIntent();
		Uri uri = (Uri)intent.getExtras().get("fileURI");
		
		Bitmap currentBitmap = null;
		try {
			currentBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
			imageView.setImageBitmap(currentBitmap);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	}
}
