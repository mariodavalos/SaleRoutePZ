package com.example.registroventa.xmls;

import android.content.Context;
import android.os.Build;
import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import com.example.registroventa.InicioActivity;
import com.example.registroventa.models.Producto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorXMLProducto {
    private String pagina;
    private Producto producto;
    private Context Carpeta;

    public AnalizadorXMLProducto(String pagina, Context Carpeta) {
        this.pagina = pagina;
        this.Carpeta = Carpeta;
    }

    public List<Producto> procesar(Boolean Internet) throws Exception {
        final List<Producto> productos = new ArrayList<Producto>();

        RootElement root = new RootElement("productos");
        Element item = root.getChild("producto");

        item.setStartElementListener(attrs -> {
            producto = new Producto();
            producto.setPrecios(new ArrayList<Double>());
        });

        item.setEndElementListener(() -> productos.add(producto));

        item.getChild("clave").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                contenido = new String(contenido.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                producto.setClave(contenido);
            }
        });
        item.getChild("descripcion").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                contenido = new String(contenido.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                producto.setDescripcion(contenido);
            }
        });
        item.getChild("precio1").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                producto.getPrecios().add(Double.parseDouble(contenido));
            }
        });
        item.getChild("precio2").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                producto.getPrecios().add(Double.parseDouble(contenido));
            }
        });
        item.getChild("precio3").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                producto.getPrecios().add(Double.parseDouble(contenido));
            }
        });
        item.getChild("precio4").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                producto.getPrecios().add(Double.parseDouble(contenido));
            }
        });
        item.getChild("precio5").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                producto.getPrecios().add(Double.parseDouble(contenido));
            }
        });
        item.getChild("precioS").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                producto.getPrecios().add(Double.parseDouble(contenido));
            }
        });
        item.getChild("costo").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                producto.setCosto(Double.parseDouble(contenido));
            }
        });
        item.getChild("existencia").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                producto.setExistencias(Double.parseDouble(contenido));
            }
        });
        //java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(entrada));

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
        FileOutputStream file;
        if (!directorio.exists()) directorio.mkdirs();
        Xml1 = new File(directorio, "productos.xml");

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

                entra = conn.getInputStream();
                file = new FileOutputStream(Xml1);

                int Bytes;
                buffer = new byte[bufferSize];
                while ((Bytes = entra.read(buffer)) > 0) {
                    file.write(buffer, 0, Bytes);
                }
                entra.close();
                file.close();
                conn.disconnect();
            }
        } catch (Exception e) {
            InicioActivity.mensajeErrorDB[2] ="Base de Productos no se encuentra o esta dañada";
        }
        InputStream leerXml = new FileInputStream(Xml1);
        Xml.parse(leerXml, Xml.Encoding.ISO_8859_1, root.getContentHandler());
        leerXml.close();
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        //*/

        return productos;
    }


}
