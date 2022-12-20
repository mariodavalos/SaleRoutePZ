package com.example.registroventa.xmls;

import android.content.Context;
import android.os.Build;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import com.example.registroventa.models.Cliente;
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

public class AnalizadorXMLCliente {
    private String pagina;
    private Cliente cliente;
    private Context Carpeta;

    public AnalizadorXMLCliente(String pagina, Context Carpeta) {
        this.pagina = pagina;
        this.Carpeta = Carpeta;
    }

    public List<Cliente> procesar(Boolean Internet) throws Exception {
        final List<Cliente> lugares = new ArrayList<Cliente>();

        RootElement root = new RootElement("clientes");
        Element item = root.getChild("cliente");

        item.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                cliente = new Cliente();
            }
        });

        item.setEndElementListener(new EndElementListener() {
            public void end() {
                lugares.add(cliente);
            }
        });

        item.getChild("clave").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setClave(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("nombre").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setNombre(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("numPrecio").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setNumPrecio(Integer.parseInt(contenido));
            }
        });

        item.getChild("calle").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setCalle(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("numExterior").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setNumEx(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("numInterior").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setNumIn(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("colonia").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setColonia(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("cp").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setCP(contenido);
            }
        });
        item.getChild("ciudad").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setCiudad(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("estado").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setEstado(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("telefono1").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setTelefono1(contenido);
            }
        });
        item.getChild("telefono2").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setTelefono2(contenido);
            }
        });
        item.getChild("telefono3").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setTelefono3(contenido);
            }
        });
        item.getChild("telefono4").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                cliente.setTelefono4(contenido);
            }
        });

        //*///////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////Metodo Original proceso en Streaming  /////////////////////////
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
			Xml.parse(entrada,  Xml.Encoding.ISO_8859_1, root.getContentHandler());                       
			
			//*///////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////Nuevo Metodo Descarga de XML          /////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
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
        if (directorio.exists() == false) directorio.mkdirs();
        Xml1 = new File(directorio, "cliente.xml");

        URL url;
        HttpURLConnection conn;
        final int bufferSize = 1024;
        byte[] buffer;

        try {
            if (Internet) {
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
            InicioActivity.mensajeErrorDB[1] ="Base de Clientes no se encuentra o esta dañada";
        }
        InputStream leerXml = new FileInputStream(Xml1);
        Xml.parse(leerXml, Xml.Encoding.ISO_8859_1, root.getContentHandler());
        leerXml.close();
        ////////////////////////////////////////////////////////////////////////////////////////////
        return lugares;
    }

}
