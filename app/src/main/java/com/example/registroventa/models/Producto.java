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

    // Método auxiliar para obtener precios de manera segura
    public String getPrecio(int index) {
        if (precios.size() > index) {
            return precios.get(index).toString();
        } else {
            return "0"; // Devuelve "0" si no hay un precio en el índice especificado
        }
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
