package com.cienciacomputacao.osqr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;

import com.cienciacomputacao.osqr.R;
import com.cienciacomputacao.osqr.model.Service;
import com.cienciacomputacao.osqr.util.OptAnimationLoader;

public class ServiceDialog extends Dialog implements View.OnClickListener {

    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private boolean mCloseFromCancel;

    private EditText edtService;
    private EditText edtAdditional;
    private EditText edtValue;
    private Button btCancel;
    private Button btCreate;

    private ServiceDialogClickListener dialogClickListener;

    public ServiceDialog(@NonNull Context context) {
        this(context, 0);
    }

    public ServiceDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.dialog_style);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out_fade);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            ServiceDialog.super.cancel();
                        } else {
                            ServiceDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_dialog_service);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        edtService = findViewById(R.id.edtDialogService);
        edtAdditional = findViewById(R.id.edtDialogAdditional);
        edtValue = findViewById(R.id.edtDialogValue);
        btCancel = findViewById(R.id.btDialogCancel);
        btCreate = findViewById(R.id.btDialogCreate);

    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
        btCancel.setOnClickListener(this);
        btCreate.setOnClickListener(this);
    }

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        mDialogView.startAnimation(mModalOutAnim);
    }

    public void setDialogClickListener(ServiceDialogClickListener dialogClickListener) {
        this.dialogClickListener = dialogClickListener;
    }

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btDialogCreate:
                String serviceName = edtService.getText().toString();
                if (!serviceName.isEmpty()) {

                    Service service = new Service();
                    service.setService(serviceName);

                    String additional = edtAdditional.getText().toString();
                    String value = edtValue.getText().toString();

                    service.setAdditional(additional.isEmpty() ? "Sem Adicionais" : additional);
                    service.setValue(value.isEmpty() ? "--" : value);

                    if (dialogClickListener != null) {
                        dialogClickListener.onClickCreateService(service);
                    }

                } else {
                    edtService.setError("O serviço não pode ser vazio");
                    return;
                }
                break;
        }

        dismissWithAnimation();
    }
}
