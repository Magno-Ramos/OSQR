package com.cienciacomputacao.osqr.dialog;

import android.graphics.Bitmap;

public interface QRDialogClickListener {

    void onClickSave(Bitmap bitmap);

    void onClickPrint(Bitmap bitmap);
}
