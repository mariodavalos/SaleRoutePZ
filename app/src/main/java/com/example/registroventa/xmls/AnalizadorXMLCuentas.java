package com.example.registroventa.xmls;

import android.content.Context;
import android.os.Build;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import com.example.registroventa.models.Cuentas;
import com.example.registroventa.InicioActivity;

import org.xml.sax.Attributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorXMLCuentas {
    private String pagina;
    private Cuentas cuenta;
    private Context Carpeta;

    public AnalizadorXMLCuentas(String pagina, Context Carpeta) {
        this.pagina = pagina;
        this.Carpeta = Carpeta;
    }

    public List<Cuentas> procesar(Boolean Internet) throws Exception {
        final List<Cuentas> Cuentas = new ArrayList<Cuentas>();

        RootElement root = new RootElement("cxcs");
        Element item = root.getChild("cxc");

        item.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                cuenta = new Cuentas();
            }
        });

        item.setEndElementListener(new EndElementListener() {
            public void end() {
                Cuentas.add(cuenta);
            }
        });

        item.getChild("id").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cuenta.setId(contenido);
            }
        });
        item.getChild("cliente").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cuenta.setCliente(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("documento").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cuenta.setDocumento(contenido);
            }
        });

        item.getChild("fecha").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                    cuenta.setFecha(contenido);
            }
        });
        item.getChild("vencimiento").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                    cuenta.setVencimiento(contenido);
            }
        });
        item.getChild("saldo").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cuenta.setSaldo(Double.parseDouble(contenido));
            }
        });
        item.getChild("numero").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cuenta.setnumero(Integer.parseInt(contenido));
            }
        });
        item.getChild("tipo").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cuenta.setTipo(contenido);
            }
        });
        //region Metodo Original proceso en Streaming
        ////////////////////////////////////////////////////////////////////////////////////////////
            /*URL url = new URL(pagina);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(500000);
			conn.setReadTimeout(500000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			InputStream entrada = conn.getInputStream();
			int tam = conn.getContentLength();

			int tam2 = 0;
			Xml.parse(entrada,  Xml.Encoding.ISO_8859_1, root.getContentHandler());*/
        //endregion
        //////////////////////////////Nuevo Metodo Descarga de XML          /////////////////////////
        InputStream entra;
        File Xml1;
        File directorio;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            File root1 = android.os.Environment.getExternalStorageDirectory();
            directorio = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta");
        }else {
            directorio = new File(Carpeta.getExternalFilesDir(null), "Android/data/com.ventaenruta");
        }
        FileOutputStream file = null;
        if (!directorio.exists()) directorio.mkdirs();
        Xml1 = new File(directorio, "cxcs.xml");

        URL url;
        HttpURLConnection conn;
        final int bufferSize = 1024;
        byte[] buffer;

        try {
            if (Internet) {
                if(Xml1.exists())Xml1.delete();
                pagina = pagina.trim();
                url = new URL(pagina);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                conn.connect();

                int Bytes;
                entra = conn.getInputStream();
                file = new FileOutputStream(Xml1);

                buffer = new byte[bufferSize];
                while ((Bytes = entra.read(buffer)) > 0) {
                    file.write(buffer, 0, Bytes);
                }
                entra.close();
                file.close();
                conn.disconnect();
            }
        } catch (Exception e) {
            InicioActivity.mensajeErrorDB[3] ="Base de Cuentas por cobrar no se encuentra o esta dañada";
        }
        InputStream leerXml = new FileInputStream(Xml1);
        Xml.parse(leerXml, Xml.Encoding.ISO_8859_1, root.getContentHandler());
        leerXml.close();
        ////////////////////////////////////////////////////////////////////////////////////////////
        return Cuentas;
    }

}
