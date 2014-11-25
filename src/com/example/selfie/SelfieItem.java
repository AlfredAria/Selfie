package com.example.selfie;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class SelfieItem {
	private Uri    fileUri;
	public SelfieItem (Uri fileUri) {
		this.fileUri = fileUri;
	}
	public Uri getImageUri() {
		return fileUri;
	}
	public String getDateString() {
		List<String> path = fileUri.getPathSegments();
		return path.get(path.size()-1);
	}
	@Override
	public String toString() {
		return fileUri.toString();
	}
}
