package com.example.vibhor.downloadimageappusingsimplethreads;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by vibhor on 16-May-16.
 */
public class L {

    public static void m(String message) {
        Log.d("vibhor", message);
    }

    public static void s(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
