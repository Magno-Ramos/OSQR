package com.cienciacomputacao.osqr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.cienciacomputacao.osqr.adapter.RegisterAdapter;
import com.cienciacomputacao.osqr.database.RegistersDB;
import com.cienciacomputacao.osqr.model.Register;

import java.util.List;

public class RegistersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registers);
        setTitle(R.string.records);

        List<Register> registers = new RegistersDB(this).findAll();
        if (!registers.isEmpty()) {
            findViewById(R.id.tvMessage).setVisibility(View.GONE);

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down));
            recyclerView.setAdapter(new RegisterAdapter(this, registers, registerClickListener()));
        }
    }

    public RegisterAdapter.RegisterClickListener registerClickListener(){
        return new RegisterAdapter.RegisterClickListener() {
            @Override
            public void onClick(Register register) {
                startActivity(ItemRegisterActivity.getIntent(RegistersActivity.this, register));
            }
        };
    }
}
