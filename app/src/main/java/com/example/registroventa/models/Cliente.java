package com.example.registroventa.models;

public class Cliente {
    private String clave;
    private String nombre;
    private String atencion;
    private int numPrecio;
    private String Calle;
    private String NumEx;
    private String NumIn;
    private String Colonia;
    private String CP;
    private String Ciudad;
    private String Estado;
    private String Telefono1;
    private String Telefono2;
    private String Telefono3;
    private String Telefono4;
    private int diascredito;

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getAtencion() {
        return atencion;
    }
    public void setAtencion(String atencion) {
        this.atencion = atencion;
    }
    public int getNumPrecio() {
        return numPrecio;
    }

    public void setNumPrecio(int numPrecio) {
        if(numPrecio<1) numPrecio=1;
        this.numPrecio = numPrecio;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getNumEx() {
        return NumEx;
    }

    public void setNumEx(String NumeroExterior) {
        this.NumEx = NumeroExterior;
    }

    public String getNumIn() {
        return NumIn;
    }

    public void setNumIn(String NumeroInterior) {
        this.NumIn = NumeroInterior;
    }

    public String getColonia() {
        return Colonia;
    }

    public void setColonia(String Colonia) {
        this.Colonia = Colonia;
    }

    public String getCP() {
        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String Ciudad) {
        this.Ciudad = Ciudad;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getTelefono1() {
        return Telefono1;
    }

    public void setTelefono1(String Telefono1) {
        this.Telefono1 = Telefono1;
    }

    public String getTelefono2() {
        return Telefono2;
    }

    public void setTelefono2(String Telefono2) {
        this.Telefono2 = Telefono2;
    }

    public String getTelefono3() {
        return Telefono3;
    }

    public void setTelefono3(String Telefono3) {
        this.Telefono3 = Telefono3;
    }

    public String getTelefono4() {
        return Telefono4;
    }

    public void setTelefono4(String Telefono4) {
        this.Telefono4 = Telefono4;
    }

    public int getDiasCredito() {
        return diascredito;
    }

    public void setDiasCredito(int diascredito) {
        this.diascredito = diascredito;
    }

    public String toString() {
        return nombre;
    }
}
