package com.example.registroventa.models;

/**
 * Created by mario on 26/10/2016.
 */
public class ConfiguracionFTP {
        private Boolean modificarprecios;
        private Boolean vercxc;
        private Boolean capturapagos;
        private Boolean asistencia;
        private String claveconfiguracion;

        public Boolean getModificarprecios() {
            return modificarprecios;
        }

        public void setModificarprecios(Boolean modificarprecios) {
            this.modificarprecios = modificarprecios;
        }

        public Boolean getVercxc() {
            return vercxc;
        }

        public void setVercxc(Boolean vercxc) {
            this.vercxc = vercxc;
        }

        public Boolean getCapturapagos() {
            return capturapagos;
        }

        public void setCapturapagos(Boolean capturapagos) {
            this.capturapagos = capturapagos;
        }

        public Boolean getAsistencia() {
            return asistencia;
        }

        public void setAsistencia(Boolean asistencia) {
            this.asistencia = asistencia;
        }

        public String getClaveconfiguracion() {
        return claveconfiguracion;
    }

        public void setClaveconfiguracion(String claveconfiguracion) {this.claveconfiguracion = claveconfiguracion;}

    }

