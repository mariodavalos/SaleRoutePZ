package com.example.registroventa.models;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Venta {
    private int id;
    private Date fecha;
    private Cliente cliente;
    private Vendedor vendedor;
    private boolean nueva;
    private boolean enviada;
    private List<VentaProducto> ventaProductos;
    private boolean metodo;
    private String nota;

    public Venta() {
        nueva = true;
        enviada = false;
        fecha = new Date();
        metodo = true;
        nota = "";
        ventaProductos = new ArrayList<VentaProducto>();
    }

    public boolean isEnviada() {
        return enviada;
    }

    public void setEnviada(boolean enviada) {
        this.enviada = enviada;
    }

    public boolean isMetodo() {
        return metodo;
    }

    public void setMetodo(boolean metodo) {
        this.metodo = metodo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public boolean isNueva() {
        return nueva;
    }

    public void setNueva(boolean nueva) {
        this.nueva = nueva;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public List<VentaProducto> getVentaProductos() {
        return ventaProductos;
    }

    public void setVentaProductos(List<VentaProducto> ventaProductos) {
        this.ventaProductos = ventaProductos;
    }


    @SuppressWarnings("deprecation")
    public String toString() {
        double total = 0;
        if (ventaProductos != null)
            for (VentaProducto vp : ventaProductos)
                total += vp.getTotal();
        return String.format("%02d/%02d/%02d %02d:%02d - %s - %s - %s", fecha.getDate(), fecha.getMonth() + 1, fecha.getYear() + 1900, fecha.getHours(), fecha.getMinutes(), cliente.getNombre(), NumberFormat.getCurrencyInstance(Locale.US).format(total), metodo? "Contado":"Credito");
    }
}
