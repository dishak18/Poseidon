package com.example.admin.litebulb.Adapters;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.litebulb.Models.Downloads;
import com.example.admin.litebulb.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AdapterDownloads extends RecyclerView.Adapter<AdapterDownloads.MyViewHolder>  {

    private Context mContext;
    private List<Downloads> downloadsList;
    public ImageView thumbnail;
    private ProgressDialog mProgressDialog;
    public int itemId;
    private final int RC_PERM_REQ_EXT_STORAGE = 7;
    private String downloadLink;
    private String itemName;
    private ProgressDialog mProgress;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView item_name, votes, license_name;
        public ImageView thumbnail;
        public Button download_button;
        public CardView cardView;


        public MyViewHolder(View view) {
            super(view);
            //view.setOnClickListener(this);
            item_name = (TextView) view.findViewById(R.id.item_name);
            votes = (TextView) view.findViewById(R.id.votes);
            license_name = (TextView) view.findViewById(R.id.license_name);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            download_button = (Button) view.findViewById(R.id.download_button);
            cardView = (CardView) view.findViewById(R.id.card_view);
            //thumbnail.setOnClickListener(this);
            mProgressDialog = new ProgressDialog(mContext);
            checkPermission();
        }
        public void openItem(final int id,final String link,final String name){

            download_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemId = id;
                    downloadLink = link;
                    itemName = name;
                    Log.e("ADAPTER DOWNLOADS", "this has been clicked + the ID is : "+itemId);
                    //Toast.makeText(mContext, "Item Id : "+itemId, Toast.LENGTH_SHORT).show();
                    mProgress = new ProgressDialog(mContext);
                    mProgress.setTitle("Please Wait...");
                    mProgress.show();
                    final AdapterDownloads.DownloadTask downloadTask = new AdapterDownloads.DownloadTask(mContext);
                    Log.e("AdapterDownloads","Download Link : "+downloadLink);
                    downloadTask.execute(downloadLink);
                    mProgress.dismiss();
                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            downloadTask.cancel(true);
                        }
                    });
                }
            });
        }


       /* public void onClick(View view) {

        }*/
    }


    public AdapterDownloads(Context mContext, List<Downloads> downloadsList) {
        this.mContext = mContext;
        this.downloadsList = downloadsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_download, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Downloads Downloads = downloadsList.get(position);
        holder.item_name.setText(Downloads.getItemName());
        Log.e("ViewHolder","Id is : "+Downloads.getItemId());
        holder.votes.setText(Downloads.getVotes()+" votes");
       // holder.license_name.setText(Downloads.getDownload_link());
        mProgressDialog = new ProgressDialog(mContext);
        /*loading Downloads cover using Glide library*/
        Glide.with(mContext)
                .load(Downloads.getThumbnail())
                .placeholder(R.drawable.studio)
                .error(R.drawable.studio)
                .into(holder.thumbnail);

        holder.openItem(Downloads.getItemId(), Downloads.getDownload_link(), Downloads.getItemName());

        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCardSelected(position, holder.thumbnail);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return downloadsList.size();
    }


    public class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/LiteBulb/"+itemName+".zip");
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
        }
    }




    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Permission to read storage is required.");
            alertBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 7);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERM_REQ_EXT_STORAGE);
        }
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERM_REQ_EXT_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(mContext, "Permission Denied !, Retrying.", Toast.LENGTH_SHORT).show();
                checkPermission();
            }
        }
    }



}





