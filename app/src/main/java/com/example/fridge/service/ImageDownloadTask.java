package com.example.fridge.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.fridge.R;
import com.example.fridge.JsonResponseEventHandler;
import com.example.fridge.RecipeData;

import java.util.concurrent.ExecutionException;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

    RecipeData data;
    JsonResponseEventHandler eventHandler;
    Context context;

    public ImageDownloadTask(Context context, RecipeData data, JsonResponseEventHandler eventHandler) {
        this.data = data;
        this.context = context;
        this.eventHandler = eventHandler;
    }

    // Execute when Thread Manager Invoke this Thread
    // The URL is given as a String in the Parameters
    protected Bitmap doInBackground(String... urls) {

        // The Result Bitmap
        Bitmap mIcon11 = null;

        // Start the download of Image with Glide
        try {
            mIcon11 = Glide.with(context)
                    .load(urls[0])
                    .asBitmap()
                    .into(-1, -1) // Scaling. -1 means original Size
                    .get(); // Start a Get Request

        } catch (ExecutionException e) {

            // If Image could not get Loaded for whatever reason, the Default Image will be loaded as Bitmap
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.no_image);

        } catch (Exception e){
            e.printStackTrace();
        }
        return mIcon11;
    }

    // After The Thread Method (doInBackground) is finished
    protected void onPostExecute(Bitmap result) {

        // Adds the Image to the data in RecipeDataHolder
        data.setImage(result);

        // Notifys the Activity, that the Image is downloaded and added to the data
        eventHandler.isFetched();
    }
}
