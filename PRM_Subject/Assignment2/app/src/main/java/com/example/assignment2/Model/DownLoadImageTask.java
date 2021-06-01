package com.example.assignment2.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
    ImageView imageView;

    /**
     * Create constructor method DownLoadImageTask
     *
     * @param imageView containing the bitmap image when handling
     */
    public DownLoadImageTask(ImageView imageView){
        this.imageView = imageView;
    }

    /**
     * Create method doInBackground for decoding the image with URL provided into bitmap
     *
     * @param urls storing the url of the image
     * @return the bitmap image
     */
    protected Bitmap doInBackground(String...urls) {
        String urlOfImage = urls[0];
        Bitmap logo = null;
        InputStream is = null;
        try {
            is = new URL(urlOfImage).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                is = new URL("https://i.ibb.co/rmRpF3X/Avatar.jpg").openStream();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        logo = BitmapFactory.decodeStream(is);
        return logo;
    }

    /**
     * Create method onPostExecute for storing the bitmap image into the ImageView after executing
     *
     * @param result storing the bitmap image that is got from the URL provided
     */
    protected void onPostExecute(Bitmap result){
        imageView.setImageBitmap(result);
    }
}

