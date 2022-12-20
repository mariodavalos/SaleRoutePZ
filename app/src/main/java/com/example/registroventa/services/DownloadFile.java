package com.example.registroventa.services;

import android.os.Build;

import com.example.registroventa.ConfiguracionActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFile {
    private String pagina;
    private File carpeta;

    public DownloadFile(String pagina,File Carpeta) {
        this.pagina = pagina;
        this.carpeta = Carpeta;
    }

    public void procesar() throws Exception {

        InputStream entra;
        File Apk;
        File directorio;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            File root1 = android.os.Environment.getExternalStorageDirectory();
             directorio = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta");
        }else {
            directorio = new File(carpeta, "Android/data/com.ventaenruta");
        }
        FileOutputStream file = null;
        if (directorio.exists() == false) directorio.mkdirs();
        Apk = new File(directorio, ConfiguracionActivity.version);

        URL url;
        HttpURLConnection conn;
        final int bufferSize = 1024;
        byte[] buffer;
        url = new URL(pagina);
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setDoInput(true);
        conn.setRequestMethod("GET");
        conn.connect();

        int Bytes;
        entra = conn.getInputStream();
        file = new FileOutputStream(Apk);

        buffer = new byte[bufferSize];
        while ((Bytes = entra.read(buffer)) > 0) {
            file.write(buffer, 0, Bytes);
        }
        entra.close();

        file.close();
        conn.disconnect();
    }
}