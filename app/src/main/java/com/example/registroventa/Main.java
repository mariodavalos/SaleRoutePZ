package com.example.registroventa;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

class Main  extends Application {

   @Override
   public void onCreate() {
      super.onCreate();
      Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
   }

   private static class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
      private final Context context;

      public MyExceptionHandler(Context context) {
         this.context = context.getApplicationContext(); // Use the application context to avoid leaks.
      }

      @Override
      public void uncaughtException(Thread thread, Throwable e) {
         // Aquí puedes manejar la excepción de manera adecuada.
         // Por ejemplo, puedes reiniciar la app, o simplemente registrar el error en un archivo de log.
         Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

         // Si eliges reiniciar la app, asegúrate de hacerlo con cuidado y considerar mostrar alguna UI o notificación para informar al usuario.

         // Ejemplo para reiniciar la app:
         Intent intent = new Intent(context, InicioActivity.class); // Asegúrate de que MainActivity es tu actividad de entrada.
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         context.startActivity(intent);

         System.exit(10);
      }
   }
}


