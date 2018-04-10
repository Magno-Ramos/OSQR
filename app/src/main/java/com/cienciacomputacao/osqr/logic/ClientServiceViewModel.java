package com.cienciacomputacao.osqr.logic;

import android.arch.lifecycle.ViewModel;

import com.cienciacomputacao.osqr.model.Client;
import com.cienciacomputacao.osqr.model.Service;

import java.util.List;

public class ClientServiceViewModel extends ViewModel {

    private Client client;


    public Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public void updateClientData(String name, String cpf, String telephone, List<Service> services) {
        client = getClient();
        client.setName(name);
        client.setCpf(cpf);
        client.setTelephone(telephone);
        client.setServices(services);
    }
}
