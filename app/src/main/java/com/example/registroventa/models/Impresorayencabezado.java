package com.example.registroventa.models;

public class Impresorayencabezado {

    private String encabezadorenglon1;
    private String encabezadorenglon2;
    private String encabezadorenglon3;
    private String MacAdressImpresora;
    private String Nombreimpresora;
    private int Mostrarimpresion;
    private int Imprimircorte;

    public String getencabezado1() {
        return encabezadorenglon1;
    }

    public void setecabezado1(String encabezado) {
        encabezadorenglon1 = encabezado;
    }

    public String getencabezado2() {
        return encabezadorenglon2;
    }

    public void setecabezado2(String encabezado) {
        encabezadorenglon2 = encabezado;
    }

    public String getencabezado3() {
        return encabezadorenglon3;
    }

    public void setecabezado3(String encabezado) {
        encabezadorenglon3 = encabezado;
    }

    public String getmacAdress() {
        return MacAdressImpresora;
    }

    public void setmacAdress(String macadress) {
        MacAdressImpresora = macadress;
    }

    public String getnombreImpresora() {
        return Nombreimpresora;
    }

    public void setnombreImpresora(String nombreimpresora) {
        Nombreimpresora = nombreimpresora;
    }

    public int getmostrarImpresion() {
        return Mostrarimpresion;
    }
    public void setmostrarImpresion(int mostrarImpresion) {
        Mostrarimpresion = mostrarImpresion;
    }

    public int getimprimirCorte() {
        return Imprimircorte;
    }

    public void setimprimirCorte(int imprimirCorte) {
        Imprimircorte = imprimirCorte;
    }
}
