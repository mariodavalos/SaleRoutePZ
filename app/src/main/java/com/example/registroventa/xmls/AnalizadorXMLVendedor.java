package com.example.registroventa.xmls;

import android.content.Context;
import android.os.Build;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import com.example.registroventa.InicioActivity;
import com.example.registroventa.models.Vendedor;

import org.xml.sax.Attributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorXMLVendedor {
    private String pagina;
    private Vendedor vendedor;
    private Context Carpeta;

    public AnalizadorXMLVendedor(String pagina, Context Carpeta) {
        this.pagina = pagina;
        this.Carpeta = Carpeta;
    }

    public List<Vendedor> procesar(Boolean Internet) throws Exception {
        final List<Vendedor> vendedores = new ArrayList<Vendedor>();

        RootElement root = new RootElement("vendedores");
        Element item = root.getChild("vendedor");

        item.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                vendedor = new Vendedor();
            }
        });

        item.setEndElementListener(new EndElementListener() {
            public void end() {
                vendedores.add(vendedor);
            }
        });

        item.getChild("clave").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                vendedor.setClave(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("nombre").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                vendedor.setNombre(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("nip").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                vendedor.setNip(contenido.replace("Ã\u0091","Ñ"));
            }
        });
        item.getChild("NIP").setEndTextElementListener(new EndTextElementListener() {
            public void end(String contenido) {
                vendedor.setNip(contenido.replace("Ã\u0091","Ñ"));
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
        if (directorio.exists() == false) directorio.mkdirs();
        Xml1 = new File(directorio, "vendedor.xml");

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
            InicioActivity.mensajeErrorDB[0] ="Base de Vendedores no se encuentra o esta dañada";
        }

        InputStream leerXml = new FileInputStream(Xml1);
        Xml.parse(leerXml, Xml.Encoding.ISO_8859_1, root.getContentHandler());
        leerXml.close();
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        //*/

        return vendedores;
    }
	/*
	private String Limpiar(String cadLimpiar, int tam, int tam2) {
		//System.out.println("Limpiando ["+tam+"] ["+tam2+"] ->\n"+cadLimpiar);
		String carsPermitidos="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890.,-_?�!�\\=()&%$#\"'������������\n<> \t:/\r;[]{}+*";
		StringBuffer sb = new StringBuffer();
		int tamCad = cadLimpiar.length();
		for(int i=0;i<tamCad; i++) {
			char c = cadLimpiar.charAt(i);
			if (carsPermitidos.indexOf(c) > -1)
				sb.append(c);
		}
		String cadNueva = sb.toString(); 
		//System.out.println("LIMPIO ->"+cadNueva);
		return cadNueva;
	}	*/

}
