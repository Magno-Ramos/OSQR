package com.cienciacomputacao.osqr.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.cienciacomputacao.osqr.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class QRCode {

    public static Bitmap getQRCodeImage(Context context, String text) {
        int wh = (int) context.getResources().getDimension(R.dimen.QRCodeSize);
        Bitmap bmp;
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, wh, wh);

            int height = bitMatrix.getHeight();
            int width = bitMatrix.getWidth();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return bmp;
    }

}
