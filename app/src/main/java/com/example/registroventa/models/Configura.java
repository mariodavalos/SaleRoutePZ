package com.example.registroventa.models;

public class Configura {
    private int Botones;
    private String Ftp_IP;
    private int Clave;
    private int Existencia;
    private int Limite;
    private int Modificar;
    private int Seleccionar;
    private int EditarVenta;
    private int TiempoLimpiarVenta;
    private int Comentar;

    public int getBotones() {
        return Botones;
    }

    public void setBotones(int botones) {
        this.Botones = botones;
    }

    public int getClave() {
        return Clave;
    }

    public void setClave(int clave) {
        this.Clave = clave;
    }

    public int getExistencia() {
        return Existencia;
    }

    public void setExistencia(int existencia) {
        this.Existencia = existencia;
    }

    public String getFTP() {
        return Ftp_IP;
    }

    public void setFTP(String FTP) {
        this.Ftp_IP = FTP;
    }

    public int getModificar() {
        return Modificar;
    }

    public void setModificar(int Modificar) {
        this.Modificar = Modificar;
    }

    public int getSeleccionar() {
        return Seleccionar;
    }

    public void setSeleccionar(int Seleccionar) {
        this.Seleccionar = Seleccionar;
    }

    public int getEditarVenta() {
        return EditarVenta;
    }

    public void setEditarVenta(int EditarVenta) {
        this.EditarVenta = EditarVenta;
    }

    public int getTiempoLimpiarVenta() {
        return TiempoLimpiarVenta;
    }
    public void setTiempoLimpiarVenta(int TiempoLimpiarVenta) {this.TiempoLimpiarVenta = TiempoLimpiarVenta;    }
    public int getComentar() {
        return Comentar;
    }
    public void setComentar(int Comentar) {
        this.Comentar = Comentar;
    }

    public int getLimite() {
        return Limite;
    }


    public void setLimite(int Limite) {
        this.Limite = Limite;
    }
}


