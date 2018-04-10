package com.cienciacomputacao.osqr.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.cienciacomputacao.osqr.GenerateCodeActivity;
import com.cienciacomputacao.osqr.R;
import com.cienciacomputacao.osqr.logic.ClientServiceViewModel;
import com.cienciacomputacao.osqr.model.Client;

public class ClientFragment extends Fragment implements View.OnClickListener {

    private EditText edtName;
    private EditText edtCpf;
    private EditText edtTelephone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_frag_client, container, false);
        view.findViewById(R.id.fabNext).setOnClickListener(this);

        edtName = view.findViewById(R.id.edtNome);
        edtCpf = view.findViewById(R.id.edtCpf);
        edtTelephone = view.findViewById(R.id.edtTelephone);

        return view;
    }

    private void sendData() {
        ClientServiceViewModel clientServiceViewModel = ViewModelProviders.of(getActivity()).get(ClientServiceViewModel.class);
        Client client = clientServiceViewModel.getClient();
        client.setCpf(edtCpf.getText().toString());
        client.setName(edtName.getText().toString());
        client.setTelephone(edtTelephone.getText().toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ClientServiceViewModel clientServiceViewModel = ViewModelProviders.of(getActivity()).get(ClientServiceViewModel.class);
        Client client = clientServiceViewModel.getClient();
        edtName.setText(client.getName());
        edtCpf.setText(client.getCpf());
        edtTelephone.setText(client.getTelephone());
    }

    @Override
    public void onPause() {
        super.onPause();
        sendData();
    }


    @Override
    public void onDeselected() {
        sendData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabNext:
                ((GenerateCodeActivity) getActivity()).nextFragment();
                break;
        }
    }
}
