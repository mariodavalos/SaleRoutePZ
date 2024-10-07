package com.example.registroventa.xmls;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class VerificarArchivo {

   public static CompletableFuture<Boolean> verificarArchivoAsync(String urlArchivoInternet) {
      return CompletableFuture.supplyAsync(() -> {
         try {
            URL url = new URL(urlArchivoInternet);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("HEAD");
            int responseCode = urlConnection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
         } catch (IOException e) {
            e.printStackTrace();
            return false;
         }
      });
   }

   public static CompletableFuture<Void> verificarYEliminarArchivoAsync(String urlArchivoInternet, File archivoLocal) {
      return verificarArchivoAsync(urlArchivoInternet).thenAccept(archivoExisteEnInternet -> {
         if (!archivoExisteEnInternet) {
            if (archivoLocal.exists()) {
               archivoLocal.delete();
               System.out.println("El archivo local ha sido eliminado porque no se encontró en internet.");
            }
         } else {
            System.out.println("El archivo existe en internet, no se eliminará el archivo local.");
         }
      });
   }
}
