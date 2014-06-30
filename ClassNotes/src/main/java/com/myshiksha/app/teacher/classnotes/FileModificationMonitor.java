package com.myshiksha.app.teacher.classnotes;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.myshiksha.app.teacher.classnotes.utils.CONSTANTS;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileModificationMonitor extends ListActivity {
	private Button start_btn;
	private Button stop_btn;
	private Button refresh_btn;
	private Button clear_btn;

	private ListView listView;
    private FileModificationService fileService;
    private FileAdapter fileAdapter;
    private List<File> filesToRead;
    private FileModificationServiceReceiver fileChangeReceiver;
    private boolean connected = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            fileService = null;

        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            fileService = ((FileModificationService.LocalBinder) service).getService();
        }
    };

    @Override
    protected void onResume() {
        IntentFilter updateFilter;
        updateFilter = new IntentFilter(FileModificationService.DIR_UPDATE);
        fileChangeReceiver = new FileModificationServiceReceiver();
        registerReceiver(fileChangeReceiver, updateFilter);
        bindService(new Intent(FileModificationMonitor.this, FileModificationService.class),
                serviceConnection, BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(serviceConnection);
    }

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_modification_monitor);
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();

        listView = (ListView) findViewById(android.R.id.list);
        filesToRead = new ArrayList<File>();
        fileAdapter = new FileAdapter(this.getApplicationContext(), R.layout.list_row_file, filesToRead);


        //Start Service
        start_btn = (Button) findViewById(R.id.file_modification_monitor_btn_first);
        start_btn.setWidth(screenWidth / 4);
        start_btn.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                if (!connected) {
                    bindService(new Intent(FileModificationMonitor.this, FileModificationService.class),
                            serviceConnection, BIND_AUTO_CREATE);
                    Toast.makeText(FileModificationMonitor.this, "File Service Connected", Toast.LENGTH_SHORT)
                            .show();
                    connected = false;
                }
                start_btn.setEnabled(true);
            }
        });

        //Stop Service
        stop_btn = (Button) findViewById(R.id.file_modification_monitor_btn_second);
        stop_btn.setWidth(screenWidth / 4);
        stop_btn.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                if (connected) {
                    unbindService(serviceConnection);
                    Toast.makeText(FileModificationMonitor.this, "File Service DisConnected", Toast.LENGTH_SHORT)
                            .show();
                    connected = false;
                }
                start_btn.setEnabled(true);
            }
        });

        //Refresh Contents
        refresh_btn = (Button) findViewById(R.id.file_modification_monitor_btn_third);
        refresh_btn.setWidth(screenWidth / 4);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                refreshLog();
            }
        });

        //Clear Contents
        clear_btn = (Button) findViewById(R.id.file_modification_monitor_btn_forth);
        clear_btn.setWidth(screenWidth / 4);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                clearLog();
            }
        });
	}
	
	
	public void refreshLog() {
        if (fileService != null) {
            filesToRead.clear();
            filesToRead.addAll(fileService.getFilesList());
            fileAdapter.notifyDataSetChanged();
            setListAdapter(fileAdapter);
        }
    }
	
	public void clearLog() {
        filesToRead.clear();
        fileAdapter.notifyDataSetChanged();
        //TODO set the listview as empty
	}

    public class FileModificationServiceReceiver extends BroadcastReceiver
    {
        //this method receives broadcast messages.
        // Be sure to modify AndroidManifest.xml file in order to enable message receiving

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.i(CONSTANTS.FILE_MODIFICATION_RECEIVER_TAG, "Got broadcast message = " + intent);
            refreshLog();
        }
    }
}
