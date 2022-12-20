package com.example.registroventa.models;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cuentas {
    private String id;
    private String Cliente;
    private String Documento;
    private String Fecha;
    private String Vencimiento;
    private Double Saldo;
    private String Tipo;
    private int numero;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String Cliente) {
        this.Cliente = Cliente;
    }

    public String getDocumento() {
        return Documento;
    }

    public void setDocumento(String Documento) {
        this.Documento = Documento;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getVencimiento() {
        return Vencimiento;
    }

    public void setVencimiento(String Vencimiento) {
        this.Vencimiento = Vencimiento;
    }

    public Double getSaldo() {
        return Saldo;
    }

    public void setSaldo(Double Saldo) {
        this.Saldo = Saldo;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public int getnumero() {
        return numero;
    }

    public void setnumero(int numero) {
        this.numero = numero;
    }

    public String toString() {
        String[] docs = Documento.split(" ");
        String numer ="0";
        for (String doc: docs) if (doc.matches("[0-9]*"))numer = doc;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date vence = null;
        try {
            vence = sdf.parse(Vencimiento);

        sdf.applyPattern("dd-MMM");
        return "Vence: " + sdf.format(vence)+" | "+ String.format("%-8s", (Tipo+numer)).replace(' ','_')  +" | "+Saldo+" | ";
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        return Vencimiento;
    }
}
