package com.example.registroventa.models;

public class PreciosAdicionales {

    private String Producto;
    private int NumPrecio;
    private Double Precio;

    public String getProducto() {
        return Producto;
    }
    public void setProducto(String Producto) {
        this.Producto = Producto;
    }
    public Double getPrecio() {
        return Precio;
    }

    public void setPrecio(Double Precio) {
        this.Precio = Precio;
    }

    public int getNumPrecio() {
        return NumPrecio;
    }

    public void setNumPrecio(int NumPrecio) {
        this.NumPrecio = NumPrecio;
    }


}
