package com.cienciacomputacao.osqr.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;

import com.cienciacomputacao.osqr.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

public class BitmapUtil {

    public static boolean saveBitmapOnGallery(Context context, android.graphics.Bitmap bitmap) {
      return saveImage(context, bitmap);
    }

    private static boolean saveImage(Context context, android.graphics.Bitmap image) {
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String name = "Image-" + n + ".png";

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/OSQR");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, name);
            String savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(context, savedImagePath);
            showNotificationImageSaved(context, image, imageFile);
            return true;
        } else {
            return false;
        }
    }

    private static void showNotificationImageSaved(Context context, android.graphics.Bitmap image, File imageFile) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                        FileProvider.getUriForFile(context, context.getPackageName() + ".provider", imageFile) : Uri.fromFile(imageFile),
                "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PendingIntent contentIntent =
                PendingIntent.getActivity(context,
                        200,
                        intent,
                        PendingIntent.FLAG_ONE_SHOT
                );

        String notificationChannelId = "com.cc.osqr.channelId";
        Notification notification = new NotificationCompat.Builder(context, notificationChannelId)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setTicker("Seu código foi gerado!")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText("Código gerado com sucesso!")
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))
                .build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, notification);
    }

    //
    private static void galleryAddPic(Context context, String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
