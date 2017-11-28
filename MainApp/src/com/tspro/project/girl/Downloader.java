package com.tspro.project.girl;

import android.app.Application;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tspro.project.girl.model.Entry;

import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by truonglx on 08/11/2017.
 */

public class Downloader {
    private static Downloader instance;
    private DownloadManager downloadManager;
    private Application application;

    public synchronized static Downloader getInstance() {
        return instance;
    }

    public synchronized static void init(Application application) {
        instance = new Downloader(application);
    }

    private Downloader(Application application) {
        this.application = application;
        downloadManager = (DownloadManager) application.getApplicationContext().getSystemService(DOWNLOAD_SERVICE);

    }

    public long downloadData(final Entry entry) {

        long downloadReference;

        // Create request for android download manager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(entry.getFull_picture()));

        //Setting title of request
        request.setTitle(application.getString(R.string.app_name));

        //Setting description of request
        request.setDescription(entry.getFrom().getNameUser());

        //Set the local destination for the downloaded file to a path
        //within the application's external files directory
        request.setVisibleInDownloadsUi(true);
        Log.d("TruongLX", String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        request.setDestinationInExternalFilesDir(application.getApplicationContext(), null
                , entry.getFrom().getNameUser() + ".jpg");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, entry.getFrom().getNameUser() + ".jpg");

        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);

        list.add(downloadReference);
        return downloadReference;
    }

    public BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {

            // get the refid from the download manager
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            list.remove(referenceId);

            if (list.isEmpty()) {

                Log.e("INSIDE", "" + referenceId);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(application.getApplicationContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(application.getApplicationContext().getString(R.string.app_name))
                                .setContentText("Download completed");

                PendingIntent contentIntent = PendingIntent.getActivity(application.getApplicationContext(), 0,
                        new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS), PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentIntent(contentIntent);
                mBuilder.setAutoCancel(true);

                NotificationManager notificationManager = (NotificationManager) application.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());


            }

        }
    };

    ArrayList<Long> list = new ArrayList<>();
}
