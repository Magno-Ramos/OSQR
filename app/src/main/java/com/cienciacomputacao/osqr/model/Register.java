package com.cienciacomputacao.osqr.model;


import java.io.Serializable;

public class Register implements Serializable{

    public int id;
    private String client;
    private String dateTime;
    private String jsonEncryptedClientService;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getJsonEncryptedClientService() {
        return jsonEncryptedClientService;
    }

    public void setJsonEncryptedClientService(String jsonEncryptedClientService) {
        this.jsonEncryptedClientService = jsonEncryptedClientService;
    }
}
