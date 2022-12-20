package com.example.registroventa.models;

import com.example.registroventa.models.Producto;
import com.example.registroventa.models.Venta;

import java.text.NumberFormat;
import java.util.Locale;

public class VentaProducto {
    private Venta venta;
    private Producto producto;
    private double cantidad;
    private double precioUnitario;
    private int precioNum;

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getPrecioNum() {
        return precioNum;
    }

    public void setPrecioNum(int precioNum) {
        this.precioNum = precioNum;
    }

    public double getTotal() {
        return cantidad * precioUnitario;
    }

    public void setTotal(double total) {
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s",
                getCantidad(),
                getProducto().getDescripcion(),
                NumberFormat.getCurrencyInstance(Locale.US).format(getPrecioUnitario()),
                NumberFormat.getCurrencyInstance(Locale.US).format(getTotal()));
    }
}
