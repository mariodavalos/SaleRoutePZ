package com.example.registroventa.xmls;

import android.content.Context;
import android.os.Build;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import com.example.registroventa.models.PreciosAdicionales;

import org.xml.sax.Attributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorXMLPreciosAdicionales {
    private String pagina;
    private PreciosAdicionales ListPrecio;
    private Context Carpeta;

    public AnalizadorXMLPreciosAdicionales(String pagina, Context Carpeta) {
        this.pagina = pagina;
        this.Carpeta = Carpeta;
    }

    public List<PreciosAdicionales> procesar(Boolean Internet) throws Exception {
        final List<PreciosAdicionales> ListaPrecio = new ArrayList<PreciosAdicionales>();

        RootElement root = new RootElement("preciosadicionales");
        Element item = root.getChild("producto");

        item.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                ListPrecio = new PreciosAdicionales();
            }
        });

        item.setEndElementListener(new EndElementListener() {
            public void end() {
                ListaPrecio.add(ListPrecio);
            }
        });

        item.getChild("Producto").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                ListPrecio.setProducto(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("NumeroDePrecio").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                ListPrecio.setNumPrecio(Integer.parseInt(contenido));
            }
        });
        item.getChild("Precio").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                ListPrecio.setPrecio(Double.parseDouble(contenido));
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
        Xml1 = new File(directorio, "preciosadicionales.xml");

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
            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(InicioActivity.InicioActividad);
            builder.setMessage("Base de Precios Negociados no se encuentra o esta dañada")
                    .setTitle("Error de conexion")
                    .setCancelable(false);
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();

            Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            possitive.setTextColor(Color.parseColor("#e18a33"));*/
            //e.getMessage();
            //InicioActivity.mensajeErrorDB[5] ="Base de Precios adicionales no se encuentra o esta dañada";
        }
        InputStream leerXml = new FileInputStream(Xml1);
        Xml.parse(leerXml, Xml.Encoding.ISO_8859_1, root.getContentHandler());
        leerXml.close();
        ////////////////////////////////////////////////////////////////////////////////////////////
        return ListaPrecio;
    }

}
