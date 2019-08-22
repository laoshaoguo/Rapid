package com.zhiyicx.appupdate.utils;

import android.os.Environment;

import java.io.File;

public class FileHelper {

	public static String getDownloadApkCachePath() {

		String appCachePath;

		if (checkSDCard()) {
			appCachePath = Environment.getExternalStorageDirectory() + "/TSPlusVersionPath/" ;
		} else {
			appCachePath = Environment.getDataDirectory().getPath() + "/TSPlusVersionPath/" ;
		}
		File file = new File(appCachePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return appCachePath;
	}

	public static boolean checkSDCard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

	}



}