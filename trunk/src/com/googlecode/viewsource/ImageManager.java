package com.googlecode.viewsource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 21.08.12
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
public class ImageManager {


    private ImageManager () {}

    public static void fetchImage(final String iUrl, final ImageView iView) {
        if ( iUrl == null){
            return;
        }
        new AsyncTask<String, Void, Bitmap>() {
            protected Bitmap doInBackground(String... Urls) {
                iView.setImageResource(R.drawable.ic_launcher);
                return downloadImage(Urls[0]);
            }
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                if (iView != null) {
                    iView.setImageBitmap(result);
                }
            }
        }.execute(new String[] { iUrl });
    }






    public static Bitmap downloadImage(String iUrl) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream buf_stream = null;
        try {
            conn = (HttpURLConnection) new URL(iUrl).openConnection();
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            buf_stream = new BufferedInputStream(conn.getInputStream(), 7858);
            bitmap = BitmapFactory.decodeStream(buf_stream);
            buf_stream.close();
            conn.disconnect();
            buf_stream = null;
            conn = null;
        } catch (Exception ex) {
            return null;
        } finally {
            if ( buf_stream != null )
                try { buf_stream.close(); } catch (IOException ex) {}
            if ( conn != null )
                conn.disconnect();
        }
        return bitmap;
    }

}
