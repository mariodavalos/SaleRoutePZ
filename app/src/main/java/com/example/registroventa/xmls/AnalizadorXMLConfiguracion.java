package com.example.registroventa.xmls;

/**
 * Created by mario on 26/10/2016.
 */

import android.content.Context;
import android.os.Build;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import org.xml.sax.Attributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.registroventa.models.ConfiguracionFTP;
import com.example.registroventa.InicioActivity;

public class AnalizadorXMLConfiguracion {
    private String pagina;
    private ConfiguracionFTP Configura;
    private Context Carpeta;

    public AnalizadorXMLConfiguracion(String pagina, Context Carpeta) {
        this.pagina = pagina;
        this.Carpeta = Carpeta;
    }

    public List<ConfiguracionFTP> procesar(Boolean Internet) throws Exception {
        final List<ConfiguracionFTP> configuraciones = new ArrayList<ConfiguracionFTP>();

        RootElement item = new RootElement("Configuracion");
        //Element item = root.getChild("configuracion");

        item.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                Configura = new ConfiguracionFTP();
            }
        });

        item.setEndElementListener(new EndElementListener() {
            public void end() {
                configuraciones.add(Configura);
            }
        });

        item.getChild("modificaprecios").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                Configura.setModificarprecios(contenido.compareTo("S") == 0);
            }
        });
        item.getChild("vercxc").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                Configura.setVercxc(contenido.compareTo("S") == 0);
            }
        });
        item.getChild("capturapagos").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                Configura.setCapturapagos(contenido.compareTo("S") == 0);
            }
        });
        item.getChild("asistencia").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                Configura.setAsistencia(contenido.compareTo("S") == 0);
            }
        });
        item.getChild("claveconfiguracion").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                Configura.setClaveconfiguracion(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("serieclientes").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                Configura.setSerieclientes(contenido.replace("Ã\u0091","Ñ"));
            }
        });


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
        Xml1 = new File(directorio, "configuracion.xml");

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
            InicioActivity.mensajeErrorDB[4] ="Base de Configuraciones no se encuentra o esta dañada";
        }

        InputStream leerXml = new FileInputStream(Xml1);
        Xml.parse(leerXml, Xml.Encoding.ISO_8859_1, item.getContentHandler());
        leerXml.close();

        return configuraciones;
    }
}
