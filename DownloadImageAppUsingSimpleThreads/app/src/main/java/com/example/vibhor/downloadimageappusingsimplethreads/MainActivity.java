package com.example.vibhor.downloadimageappusingsimplethreads;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText mEditText;
    private ListView mListView;
    private String[] listOfImages;
    private ProgressBar mProgressBar;
    private LinearLayout mLoadingSection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mEditText = (EditText) findViewById(R.id.edit_text);
        mListView = (ListView) findViewById(R.id.list_view);
        mProgressBar = (ProgressBar) findViewById(R.id.download_bar);
        mListView.setOnItemClickListener(this);
        listOfImages = getResources().getStringArray(R.array.image_urls);
        mLoadingSection= (LinearLayout) findViewById(R.id.downloading_image_layout);


    }


    public void onDownload(View view) {

        //to give custom file names the code below
        String url =mEditText.getText().toString() ;



        Thread myThread=new Thread(new DownloadImagesThread(url));
         myThread.start();

    }


    public boolean downloadImageUsingThreads(String url)  {
        boolean success = false;
        HttpURLConnection httpURLConnection = null;
        URL downloadUrl = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream=null;

        File file =null;
        try {
            downloadUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) downloadUrl.openConnection();
            inputStream = httpURLConnection.getInputStream();

            file =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath()+"/"+Uri.parse(url).getLastPathSegment());
            L.m(file.getAbsolutePath());


            fileOutputStream = new FileOutputStream(file);

           byte[] buffer =new byte[1024];

            int read =-1;
            while((read = inputStream.read(buffer))!=-1 )
            {
               fileOutputStream.write(buffer,0,read);
            }
                success = true;
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        } finally {


            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingSection.setVisibility(View.GONE);
                }
            });
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if(inputStream !=null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            }
            if(fileOutputStream != null)
            {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {

                }
            }
        }
        return success;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mEditText.setText(listOfImages[position]);

    }
    private  class DownloadImagesThread implements Runnable
    {
        private String url ;


        public DownloadImagesThread(String url)
        {
            this.url=url;
        }

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingSection.setVisibility(View.VISIBLE);
                }
            });

            downloadImageUsingThreads(url);
        }
    }
}
