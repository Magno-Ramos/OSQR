package com.cienciacomputacao.osqr.model;

import java.io.Serializable;

public class Service implements Serializable{

    private int id;
    private int ClientId;
    private String service;
    private String additional;
    private String value;

    public Service(){
        setId(0);
        setClientId(0);
        setService("");
        setAdditional("");
        setValue("");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
