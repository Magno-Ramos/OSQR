package com.cienciacomputacao.osqr.fragment;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cienciacomputacao.osqr.R;
import com.cienciacomputacao.osqr.database.RegistersDB;
import com.cienciacomputacao.osqr.dialog.QRDialog;
import com.cienciacomputacao.osqr.dialog.QRDialogClickListener;
import com.cienciacomputacao.osqr.logic.ClientServiceViewModel;
import com.cienciacomputacao.osqr.model.Client;
import com.cienciacomputacao.osqr.model.Register;
import com.cienciacomputacao.osqr.model.Service;
import com.cienciacomputacao.osqr.util.BitmapUtil;
import com.cienciacomputacao.osqr.util.Encrypt;
import com.cienciacomputacao.osqr.util.QRCode;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

public class FinalFragment extends Fragment implements QRDialogClickListener {

    private static final int CODE_PERMISSION_STORAGE = 51;

    private TextView tvTitleClient;
    private TextView tvTitleService;
    private TextView tvName;
    private TextView tvCpf;
    private TextView tvTelephone;
    private TextView tvServices;
    private Button btFinal;
    private Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_frag_final, container, false);

        tvTitleClient = view.findViewById(R.id.tvTitleClient);
        tvTitleService = view.findViewById(R.id.tvTitleService);
        tvName = view.findViewById(R.id.tvClientName);
        tvCpf = view.findViewById(R.id.tvClientCpf);
        tvTelephone = view.findViewById(R.id.tvClientTelephone);
        tvServices = view.findViewById(R.id.tvServiceCount);
        btFinal = view.findViewById(R.id.btFinal);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setData();
    }

    @Override
    public void onSelected() {
        super.onSelected();
        setData();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void setData() {
        ClientServiceViewModel clientServiceViewModel = ViewModelProviders.of(getActivity()).get(ClientServiceViewModel.class);
        Client client = clientServiceViewModel.getClient();

        String name = client.getName();
        String cpf = client.getCpf();
        String tel = client.getTelephone();
        List<Service> services = client.getServices();

        tvCpf.setVisibility(cpf.isEmpty() ? View.GONE : View.VISIBLE);
        tvCpf.setText(cpf.isEmpty() ? getString(R.string.cpf) : cpf);

        tvTelephone.setVisibility(tel.isEmpty() ? View.GONE : View.VISIBLE);
        tvTelephone.setText(tel.isEmpty() ? getString(R.string.telephone) : tel);

        boolean clientOK = false;
        boolean serviceOk = false;

        if (!name.isEmpty() && name.length() > 2) {
            tvTitleClient.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle_green, 0);
            tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvName.setText(name);
            clientOK = true;
        } else {
            tvName.setText(R.string.name);
            tvName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close, 0, 0, 0);
            tvTitleClient.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle_gray, 0);
        }

        if (!services.isEmpty()) {
            tvTitleService.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle_green, 0);
            tvServices.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvServices.setText(MessageFormat.format("{0} adicionado", services.size()));
            serviceOk = true;
        } else {
            tvServices.setText(R.string.message_min_one_service);
            tvServices.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close, 0, 0, 0);
            tvTitleService.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle_gray, 0);
        }

        btFinal.setOnClickListener((clientOK && serviceOk) ? clickGenerateCode() : clickInvalidFields());
    }

    private View.OnClickListener clickGenerateCode() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final QRDialog qrDialog = new QRDialog(getActivity(), QRDialog.PROGRESS_TYPE);
                qrDialog.setCancelable(false);
                qrDialog.setCanceledOnTouchOutside(false);
                qrDialog.setTitleText("Carregando");
                qrDialog.show();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        ClientServiceViewModel clientServiceViewModel = ViewModelProviders.of(getActivity()).get(ClientServiceViewModel.class);
                        final Client client = clientServiceViewModel.getClient();

                        Gson gson = new Gson();
                        String stringClient = gson.toJson(client);

                        String messageAfterDecrypt;
                        try {
                            messageAfterDecrypt = Encrypt.encrypt(stringClient);
                        } catch (Exception e) {
                            messageAfterDecrypt = stringClient;
                        }
                        bitmap = QRCode.getQRCodeImage(getContext(), messageAfterDecrypt);

                        Date date = new Date(System.currentTimeMillis());
                        String dateTime = DateFormat.getDateTimeInstance().format(date);

                        final Register register = new Register();
                        register.setClient(client.getName());
                        register.setJsonEncryptedClientService(messageAfterDecrypt);
                        register.setDateTime(dateTime);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RegistersDB registersDB = new RegistersDB(getContext());
                                registersDB.save(register);
                                showDialogQR();
                            }
                        });
                    }
                });
            }
        };
    }

    private void showDialogQR() {
        if (bitmap != null) {
            QRDialog qrDialog = new QRDialog(getActivity(), QRDialog.SUCCESS_TYPE);
            qrDialog.setCancelable(false);
            qrDialog.setCanceledOnTouchOutside(false);
            qrDialog.setQRImage(bitmap);
            qrDialog.setTitleText("Código gerado com sucesso!");
            qrDialog.setQrDialogClickListener(FinalFragment.this);
            qrDialog.setConfirmClickListener(new QRDialog.OnSweetClickListener() {
                @Override
                public void onClick(QRDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                    getActivity().finish();
                }
            });
            qrDialog.show();
        } else {
            QRDialog qrDialog = new QRDialog(getActivity(), QRDialog.ERROR_TYPE);
            qrDialog.setTitleText("Falha ao gerar código");
            qrDialog.show();
        }
    }

    private View.OnClickListener clickInvalidFields() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Preencha as informações obrigatórias", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onClickSave(Bitmap bitmap) {
        if (checkPermissionStorage()) {
            boolean success = BitmapUtil.saveBitmapOnGallery(getContext(), bitmap);
            Toast.makeText(getContext(), success ? "Salvo na galeria " : "Falha ao salvar imagen", Toast.LENGTH_LONG).show();
        } else {
            requestForPermissionStorage();
        }
    }

    @Override
    public void onClickPrint(Bitmap bitmap) {
        PrintHelper printHelper = new PrintHelper(getContext());
        printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        printHelper.printBitmap("Ordem de Serviço", bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODE_PERMISSION_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean success = BitmapUtil.saveBitmapOnGallery(getContext(), bitmap);
                    Toast.makeText(getContext(), success ? "Salvo na galeria " : "Falha ao salvar imagen", Toast.LENGTH_LONG).show();
                }
                return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkPermissionStorage() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForPermissionStorage() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_PERMISSION_STORAGE);
    }
}
