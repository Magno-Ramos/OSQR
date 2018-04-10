package com.cienciacomputacao.osqr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Client implements Serializable{

    private int id;
    private String name;
    private String cpf;
    private String telephone;
    private List<Service> services;

    public Client(){
        setId(0);
        setName("");
        setTelephone("");
        setCpf("");
        services = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
