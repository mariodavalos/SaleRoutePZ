package com.example.registroventa.xmls;

import android.content.Context;
import android.os.Build;
import android.sax.Element;
import android.sax.RootElement;
import android.util.Xml;

import com.example.registroventa.InicioActivity;
import com.example.registroventa.models.Metodos;

import org.xml.sax.Attributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorMetodos {

   private String pagina;

   private Metodos metodo;

   private Context Carpeta;

   public AnalizadorMetodos(String pagina, Context Carpeta) {
      this.pagina = pagina;
      this.Carpeta = Carpeta;
   }

    public List<Metodos> procesar(Boolean Internet) throws Exception {
       final List<Metodos> metodos = new ArrayList<Metodos>();

       RootElement root = new RootElement("formasdepago");
       Element item = root.getChild("forma");

       item.setStartElementListener((Attributes attrs) -> {
          metodo = new Metodos();
       });

       item.setEndElementListener(() -> metodos.add(metodo));

       item.getChild("FormaDePago").setEndTextElementListener((String contenido) -> {
          contenido = new String(contenido.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
          metodo.setNombre(contenido);
       });

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
       Xml1 = new File(directorio, "formasdepago.xml");

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
             if((Bytes = entra.read(buffer))<=0){
                InicioActivity.mensajeErrorDB[5] ="Base de Metodos no se encuentra o esta dañada";
                return metodos;
             }else{
                file.write(buffer, 0, Bytes);
             }
             while ((Bytes = entra.read(buffer)) > 0) {
                file.write(buffer, 0, Bytes);
             }
             entra.close();
             file.close();
             conn.disconnect();
          }
       } catch (Exception e) {
          InicioActivity.mensajeErrorDB[2] ="Base de Metodos no se encuentra o esta dañada";
       }
       InputStream leerXml = Files.newInputStream(Xml1.toPath());
       Xml.parse(leerXml, Xml.Encoding.ISO_8859_1, root.getContentHandler());
       leerXml.close();
       ////////////////////////////////////////////////////////////////////////////////////////////
       ////////////////////////////////////////////////////////////////////////////////////////////
       ////////////////////////////////////////////////////////////////////////////////////////////
       //*/

       return metodos;


    }


}

