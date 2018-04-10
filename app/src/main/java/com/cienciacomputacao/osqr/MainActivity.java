package com.cienciacomputacao.osqr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.cienciacomputacao.osqr.model.Client;
import com.cienciacomputacao.osqr.util.Encrypt;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.viewGenerateCode).setOnClickListener(this);
        findViewById(R.id.viewReadCode).setOnClickListener(this);
        findViewById(R.id.viewRegisters).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Gson gson = new Gson();
                String coded = result.getContents();

                try {
                    String decoded = Encrypt.decrypt(coded);
                    Client client = gson.fromJson(decoded, Client.class);
                    startActivity(ClientActivity.getInstance(MainActivity.this, client));
                } catch (Exception e) {
                    try {
                        Client client = gson.fromJson(coded, Client.class);
                        startActivity(ClientActivity.getInstance(MainActivity.this, client));
                    } catch (Exception ex) {
                        Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                    }
                }


                return;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewGenerateCode:
                startActivity(new Intent(this, GenerateCodeActivity.class));
                break;
            case R.id.viewReadCode:
                if (qrScan == null) {
                    qrScan = new IntentIntegrator(this);
                    qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    qrScan.setPrompt("Posicione a camera sobre o código");
                }
                qrScan.initiateScan();
                break;
            case R.id.viewRegisters:
                startActivity(new Intent(this, RegistersActivity.class));
                break;
        }
    }
}
