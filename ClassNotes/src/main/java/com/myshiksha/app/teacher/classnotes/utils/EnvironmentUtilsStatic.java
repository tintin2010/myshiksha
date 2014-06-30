package com.myshiksha.app.teacher.classnotes.utils;

import android.os.Environment;

import java.io.File;

public class EnvironmentUtilsStatic {
	public static boolean is_external_storage_available() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

    public static File get_downloads_dir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }
}
