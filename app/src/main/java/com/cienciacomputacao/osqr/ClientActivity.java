package com.cienciacomputacao.osqr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.cienciacomputacao.osqr.adapter.ServiceAdapter;
import com.cienciacomputacao.osqr.model.Client;

public class ClientActivity extends AppCompatActivity {

    private TextView tvName, tvCpf, tvTel;
    private RecyclerView recyclerView;

    public static Intent getInstance(Context context, Client client) {
        Intent intent = new Intent(context, ClientActivity.class);
        intent.putExtra("client", client);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        setTitle(R.string.client);

        tvName = findViewById(R.id.tvName);
        tvCpf = findViewById(R.id.tvCpf);
        tvTel = findViewById(R.id.tvTel);
        recyclerView = findViewById(R.id.recyclerView);

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                Client client = (Client) getIntent().getSerializableExtra("client");
                setData(client);
            }
        }
    }

    private void setData(Client client) {
        tvName.setText(client.getName());
        tvCpf.setText(client.getCpf());
        tvTel.setText(client.getTelephone());

        tvCpf.setVisibility( client.getCpf().isEmpty() ? View.GONE : View.VISIBLE);
        tvTel.setVisibility( client.getTelephone().isEmpty() ? View.GONE : View.VISIBLE);

        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ServiceAdapter(this, client.getServices(), null, true));
    }
}
