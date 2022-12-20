package com.example.registroventa.models;

import java.util.List;


public class Producto {
    private String clave;
    private String descripcion;
    private List<Double> precios;
    private Double costo;
    private Double existencias;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Double> getPrecios() {
        return precios;
    }

    public void setPrecios(List<Double> precios) {
        this.precios = precios;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Double getExistencia() {
        return existencias;
    }

    public void setExistencias(Double existencia) {
        this.existencias = existencia;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
