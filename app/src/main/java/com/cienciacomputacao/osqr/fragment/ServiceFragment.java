package com.cienciacomputacao.osqr.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.cienciacomputacao.osqr.GenerateCodeActivity;
import com.cienciacomputacao.osqr.R;
import com.cienciacomputacao.osqr.adapter.ServiceAdapter;
import com.cienciacomputacao.osqr.custom.TextView;
import com.cienciacomputacao.osqr.dialog.ServiceDialog;
import com.cienciacomputacao.osqr.dialog.ServiceDialogClickListener;
import com.cienciacomputacao.osqr.logic.ClientServiceViewModel;
import com.cienciacomputacao.osqr.model.Client;
import com.cienciacomputacao.osqr.model.Service;

import java.util.List;

public class ServiceFragment extends Fragment implements View.OnClickListener, ServiceDialogClickListener, ServiceAdapter.OnDeleteItemClickListener {

    private RecyclerView recyclerView;
    private List<Service> services;
    private ServiceAdapter adapter;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_frag_service, container, false);
        view.findViewById(R.id.fab).setOnClickListener(this);
        view.findViewById(R.id.fabNext).setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView = view.findViewById(R.id.tvMessage);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                ServiceDialog serviceDialog = new ServiceDialog(getContext());
                serviceDialog.setDialogClickListener(this);
                serviceDialog.show();
                break;
            case R.id.fabNext:
                ((GenerateCodeActivity) getActivity()).nextFragment();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ClientServiceViewModel clientServiceViewModel = ViewModelProviders.of(getActivity()).get(ClientServiceViewModel.class);
        Client client = clientServiceViewModel.getClient();
        services = client.getServices();
        textView.setVisibility(services.isEmpty() ? View.VISIBLE : View.GONE);

        adapter = new ServiceAdapter(getContext(), services, this, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down));
        recyclerView.setAdapter(adapter);
    }

    private void updateRecyclerView() {
        textView.setVisibility(services.isEmpty() ? View.VISIBLE : View.GONE);
        adapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void sendData() {
        ClientServiceViewModel clientServiceViewModel = ViewModelProviders.of(getActivity()).get(ClientServiceViewModel.class);
        Client client = clientServiceViewModel.getClient();
        client.setServices(services);
    }

    @Override
    public void onPause() {
        super.onPause();
        sendData();
    }

    @Override
    public void onDeselected() {
        super.onDeselected();
        sendData();
    }

    @Override
    public void onClickCreateService(Service service) {
        services.add(service);
        updateRecyclerView();
    }

    @Override
    public void onDeleteItem(int position) {
        services.remove(position);
        updateRecyclerView();
    }
}
