package com.cienciacomputacao.osqr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.cienciacomputacao.osqr.adapter.ServiceSmallAdapter;
import com.cienciacomputacao.osqr.dialog.QRDialog;
import com.cienciacomputacao.osqr.model.Client;
import com.cienciacomputacao.osqr.model.Register;
import com.cienciacomputacao.osqr.util.BitmapUtil;
import com.cienciacomputacao.osqr.util.Encrypt;
import com.cienciacomputacao.osqr.util.QRCode;
import com.google.gson.Gson;

public class ItemRegisterActivity extends AppCompatActivity {

    private Register register;

    public static Intent getIntent(Context context, Register register) {
        Intent intent = new Intent(context, ItemRegisterActivity.class);
        intent.putExtra("register", register);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_register);
        setTitle("Ordem de Serviço");
        findViewById(R.id.btGetCode).setOnClickListener(clickGetCode());

        CardView cardView = findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE);

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                TextView tvDate = findViewById(R.id.tvDate);
                TextView tvClient = findViewById(R.id.tvClient);
                TextView tvCpf = findViewById(R.id.tvCpf);
                TextView tvNumber = findViewById(R.id.tvNumber);

                register = (Register) getIntent().getExtras().getSerializable("register");
                Client client = getClient(register);
                if (client != null) {

                    // text views
                    tvDate.setText(register.getDateTime());
                    tvClient.setText(register.getClient());
                    tvCpf.setText(client.getCpf());
                    tvNumber.setText(client.getTelephone());

                    tvCpf.setVisibility(client.getCpf().isEmpty() ? View.GONE : View.VISIBLE);
                    tvNumber.setVisibility(client.getTelephone().isEmpty() ? View.GONE : View.VISIBLE);

                    // Recycler View
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down));
                    recyclerView.setAdapter(new ServiceSmallAdapter(this, client.getServices()));

                    // card visible
                    cardView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "Falha", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Client getClient(Register register) {
        try {
            String json = Encrypt.decrypt(register.getJsonEncryptedClientService());
            Gson gson = new Gson();
            return gson.fromJson(json, Client.class);
        } catch (Exception e) {
            return null;
        }
    }

    private View.OnClickListener clickGetCode() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (register != null) {

                    final QRDialog qrDialog = new QRDialog(ItemRegisterActivity.this, QRDialog.PROGRESS_TYPE);
                    qrDialog.setTitleText("Carregando");
                    qrDialog.setCancelable(false);
                    qrDialog.setCanceledOnTouchOutside(false);
                    qrDialog.show();

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = QRCode.getQRCodeImage(ItemRegisterActivity.this, register.getJsonEncryptedClientService());
                            final boolean success =  BitmapUtil.saveBitmapOnGallery(getApplicationContext(), bitmap);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), success ? "Salvo na galeria " : "Falha ao salvar imagen", Toast.LENGTH_LONG).show();
                                    qrDialog.dismissWithAnimation();
                                }
                            });
                        }
                    });

                } else {
                    Toast.makeText(ItemRegisterActivity.this, "Impossivel gerar código", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}

