package com.wada811.android.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class ToastUtils {

    /**
     * Toastを表示する
     *
     * @param context
     * @param message
     */
    public static void show(final Context context, final String message){
        if(ThreadUtils.isMainThread()){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }else{
            if(context instanceof Service){
                // Handler で表示する
                //
                // public class HandlerService extends Service {
                //
                //     Handler handler;
                //
                //     @Override
                //     public void onCreate() {
                //         super.onCreate();
                //         handler = new Handler();
                //     }
                //
                //     private void runOnUiThread(Runnable runnable) {
                //         handler.post(runnable);
                //     }
                //
                // }
                throw new RuntimeException("Cannot show Toast in Service.");
            }else if(context instanceof Activity){
                ((Activity)context).runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    /**
     * Toastを表示する
     *
     * @param context
     * @param resId
     */
    public static void show(final Context context, final int resId){
        if(ThreadUtils.isMainThread()){
            Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
        }else{
            if(context instanceof Service){
                // Handler で表示する
                //
                // public class HandlerService extends Service {
                //
                //     Handler handler;
                //
                //     @Override
                //     public void onCreate() {
                //         super.onCreate();
                //         handler = new Handler();
                //     }
                //
                //     private void runOnUiThread(Runnable runnable) {
                //         handler.post(runnable);
                //     }
                //
                // }
                throw new RuntimeException("Cannot show Toast in Service.");
            }else if(context instanceof Activity){
                ((Activity)context).runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    /**
     * Toastを表示する
     *
     * @param context
     * @param bitmap
     */
    public static void show(final Context context, final Bitmap bitmap){
        if(ThreadUtils.isMainThread()){
            Toast toast = new Toast(context);
            ImageView image = new ImageView(context);
            image.setImageBitmap(bitmap);
            image.setLayoutParams(new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
            toast.setView(image);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }else{
            if(context instanceof Service){
                // Handler で表示する
                //
                // public class HandlerService extends Service {
                //
                //     Handler handler;
                //
                //     @Override
                //     public void onCreate() {
                //         super.onCreate();
                //         handler = new Handler();
                //     }
                //
                //     private void runOnUiThread(Runnable runnable) {
                //         handler.post(runnable);
                //     }
                //
                // }
                throw new RuntimeException("Cannot show Toast in Service.");
            }else if(context instanceof Activity){
                ((Activity)context).runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        Toast toast = new Toast(context);
                        ImageView image = new ImageView(context);
                        image.setImageBitmap(bitmap);
                        image.setLayoutParams(new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
                        toast.setView(image);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        }
    }

}
