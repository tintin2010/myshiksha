package com.myshiksha.app.teacher.classnotes;

import android.app.Service;
import android.content.Intent;
import android.database.DataSetObservable;
import android.os.Binder;
import android.os.FileObserver;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.myshiksha.app.teacher.classnotes.utils.CONSTANTS;
import com.myshiksha.app.teacher.classnotes.utils.EnvironmentUtilsStatic;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileModificationService extends Service {
    public static final String DIR_UPDATE = "com.myshiksha.app.teacher.classnotes.FileModificationService.action.DIR_UPDATE";
    private FileObserver fileOb;
    private File listenToDir = null;
    private Handler handler;


    //this method sends broadcast messages
    private void announceDirChanges()
    {
        Intent intent = new Intent(DIR_UPDATE);
        Log.i(CONSTANTS.FILE_MODIFICATION_SERVICE_TAG, "Announce Dir Changes.....");
        sendBroadcast(intent);
    }

	@Override
	public void onCreate() {
        File downloadsDir = EnvironmentUtilsStatic.get_downloads_dir();
        if ( downloadsDir == null || !downloadsDir.exists()) {
            Log.w(CONSTANTS.FILE_MODIFICATION_SERVICE_TAG, "Downloads Dir Not Found");
            Toast.makeText(FileModificationService.this, "DOWNLOADS DIR is not available!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(CONSTANTS.FILE_MODIFICATION_SERVICE_TAG, "Found Downloads Dir = " + downloadsDir.getAbsolutePath());

        File teacherClassNotes = new File(downloadsDir.getPath() + File.separator + CONSTANTS.CLASS_NOTES_SYNC_DIR);
        if ( (teacherClassNotes.exists() && teacherClassNotes.isDirectory()) ||
                (!teacherClassNotes.exists() && teacherClassNotes.mkdir()) )  {
            listenToDir = teacherClassNotes;
            Log.i(CONSTANTS.FILE_MODIFICATION_SERVICE_TAG, "Found ClassNotes Dir" + teacherClassNotes.getAbsolutePath());
        }  else {
            Log.w(CONSTANTS.FILE_MODIFICATION_SERVICE_TAG, CONSTANTS.CLASS_NOTES_SYNC_DIR + " Not a dir");
            Toast.makeText(FileModificationService.this, CONSTANTS.CLASS_NOTES_SYNC_DIR + " Not a dir", Toast.LENGTH_SHORT).show();
            return;
        }

        handler = new Handler();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(CONSTANTS.FILE_MODIFICATION_SERVICE_TAG, "Starting fileObserver.....");
        int result = super.onStartCommand(intent, flags, startId);
        fileOb = createFileObserver(listenToDir);
        if (fileOb != null) {
            Log.i(CONSTANTS.FILE_MODIFICATION_SERVICE_TAG, "StartWatching fileObserver.....");

            fileOb.startWatching();
        }
        return result;
    }

    @Override
	public void onDestroy() {
        Log.i(CONSTANTS.FILE_MODIFICATION_SERVICE_TAG, "Stopping fileObserver.....");
        if (fileOb != null) {
            Log.i(CONSTANTS.FILE_MODIFICATION_SERVICE_TAG, "StopWatching fileObserver.....");
            fileOb.stopWatching();
        }
 	}

	@Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }


    public List<File> getFilesList() {
        return listenToDir != null ? Arrays.asList(listenToDir.listFiles()) : null;
    }

    public class LocalBinder extends Binder {
        FileModificationService getService() {
            return FileModificationService.this;
        }
    }

    /** Sets up a FileObserver to watch the current directory. */
    private FileObserver createFileObserver(File path) {
        return new FileObserver(path.getAbsolutePath(), FileObserver.CREATE | FileObserver.DELETE
                | FileObserver.MOVED_FROM | FileObserver.MOVED_TO) {

            @Override
            public void onEvent(int event, String path) {
                    Log.i(CONSTANTS.FILE_MODIFICATION_SERVICE_TAG, "FileObserver received event " + event);
                    runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        announceDirChanges();
                    }
                });
            }
        };
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
}
