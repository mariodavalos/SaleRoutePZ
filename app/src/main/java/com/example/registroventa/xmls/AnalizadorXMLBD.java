package com.example.registroventa.xmls;

import android.os.Build;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import com.example.registroventa.models.Configura;

import org.xml.sax.Attributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorXMLBD {
    private Configura config;
    private File Carpeta;

    public AnalizadorXMLBD(File Carpeta) {
        this.Carpeta = Carpeta;
    }

    public Configura procesar() throws Exception {
        final List<Configura> configuracion = new ArrayList<Configura>();

        RootElement root = new RootElement("configuracion");
        Element item = root.getChild("config");

        item.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                config = new Configura();
            }
        });

        item.setEndElementListener(new EndElementListener() {
            public void end() {
                configuracion.add(config);
            }
        });

        item.getChild("FTP").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                config.setFTP(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("Botones").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                config.setBotones(Integer.parseInt(contenido));
            }
        });
        item.getChild("Clave").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                config.setClave(Integer.parseInt(contenido));
            }
        });

        item.getChild("Existencia").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                config.setExistencia(Integer.parseInt(contenido));
            }
        });
        item.getChild("Limite").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                config.setLimite(Integer.parseInt(contenido));
            }
        });
        File Xml1;
        File directorio;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            File root1 = android.os.Environment.getExternalStorageDirectory();
            directorio = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta");
        }else {
            directorio = new File(Carpeta, "Android/data/com.ventaenruta");
        }
        if (!directorio.exists()) directorio.mkdirs();
        Xml1 = new File(directorio, "configuracion.xml");
        InputStream leerXml = Files.newInputStream(Xml1.toPath());
        Xml.parse(leerXml, Xml.Encoding.ISO_8859_1, root.getContentHandler());
        leerXml.close();

        return configuracion.get(0);
    }
}
