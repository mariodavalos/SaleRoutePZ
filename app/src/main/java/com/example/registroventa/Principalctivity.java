package com.example.registroventa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.registroventa.models.Configura;
import com.example.registroventa.models.Producto;
import com.example.registroventa.models.Venta;
import com.example.registroventa.models.VentaProducto;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Principalctivity extends android.app.Activity {
    private static final Void params = null;
    public static Configura config = new Configura();
    ProgressBar cargando = null;
    public Button Ventas, Limpiar, Subir;
    private boolean error = false;
    //private String mensajeError = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principalctivity);
        cargando = (ProgressBar) this.findViewById(R.id.avance);
        cargando.setVisibility(View.GONE);
        Buttons();
        configuracion = Configuracion(this.getExternalFilesDir(null));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (InicioActivity.getListaConfiguracion() == null) {
            menu.add(0, 0, 0, "Configuracion");
            menu.add(0, 1, 1, "Impresion");
        } else if (InicioActivity.getListaConfiguracion().get(0).getAsistencia()) {
            menu.add(0, 0, 0, "Configuracion");
            menu.add(0, 1, 1, "Impresion");
        }
        getMenuInflater().inflate(R.menu.inicio, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(this, ConfiguracionActivity.class);
                startActivity(intent);
                return true;
            case 1:
                Intent intent1 = new Intent(this, ImpresionActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void cargarInformacionOnClick(View view) {
        cargando.setVisibility(View.VISIBLE);
        InicioActivity.Toast(Principalctivity.this,"Actualizando datos, esta operación puede tardar. Espere por favor");
        new ActualizarDatos().execute(null, null, null);
    }
    public void registrarVentasOnClick(View view) {
        Intent intent = new Intent(this, ListaVentasActivity.class);
        this.startActivity(intent);
    }

    public void enviarInformacionOnClick(View view) {
        cargando.setVisibility(View.VISIBLE);
        try {
            InicioActivity.Toast(Principalctivity.this,"Enviando existencias...");
            EnviarExistenciasPOST envioExistencias = new EnviarExistenciasPOST() {
                @Override
                public void onGetValue(final String value) {
                    Principalctivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            InicioActivity.Toast(Principalctivity.this,value);
                            cargando.setVisibility(View.GONE);
                            if(value.contains("error")){
                                ReintentarEnvio(value);
                            }else{
                                InicioActivity.Toast(Principalctivity.this,"Envio correcto");
                            }
                        }
                    });
                }
            };
            envioExistencias.execute(params);

            InicioActivity.Toast(Principalctivity.this,"Enviando ventas...");
            EnviarVentasPOST envioVentas = new EnviarVentasPOST() {
                @Override
                public void onGetValue(final String value) {
                    Principalctivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            InicioActivity.Toast(Principalctivity.this,value);
                            cargando.setVisibility(View.GONE);
                            if(value.contains("error")){
                                ReintentarEnvio(value);
                            }else{
                                InicioActivity.Toast(Principalctivity.this,"Envio correcto");
                            }
                        }
                    });
                }
            };
            envioVentas.execute(params);

            InicioActivity.Toast(Principalctivity.this,"Enviando cuentas...");
            EnviarCuentasPOST envioCuentas = new EnviarCuentasPOST() {
                @Override
                public void onGetValue(final String value) {
                    Principalctivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            InicioActivity.Toast(Principalctivity.this,value);
                            cargando.setVisibility(View.GONE);
                            if(value.contains("error")){
                                ReintentarEnvio(value);
                            }else{
                                InicioActivity.Toast(Principalctivity.this,"Envio correcto");
                            }
                        }
                    });
                }
            };
            envioCuentas.execute(params);

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Principalctivity.this);
            builder
                    .setTitle("Envio erroneo")
                    .setMessage("Error: " + e.toString())
                    .setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            possitive.setTextColor(Color.parseColor("#e18a33"));
        }
    }
    public void ReintentarEnvio(String error){
        AlertDialog.Builder builder = new AlertDialog.Builder(Principalctivity.this);
        builder
                .setTitle("Envio erroneo")
                .setMessage(error+"\n\n\n¿Deseas reintentar el envio?\n")
                .setNegativeButton("NO",null)
                .setPositiveButton("REINTENTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        enviarInformacionOnClick(null);
                    }});
        AlertDialog dialog = builder.create();
        dialog.show();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
    }
    public void borrarVentasOnClick(View view) {
        // if (cad.contains("OK")) {
        if(ListaVentasActivity.getListaVentas().size()>0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("BORRAR VENTAS")
                    .setMessage("¿Seguro quiere borrar las ventas?")
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton("BORRAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Date fechactual = new Date();

                            long minutos = (((fechactual.getTime() - ListaVentasActivity.getListaVentas().get(0).getFecha().getTime()) / 1000) / 60);
                            if(minutos >= configuracion.getTiempoLimpiarVenta()) {
                                InicioActivity.getDB().limpiarVentas();
                                InicioActivity.getDB().limpiarCuentas();
                                ListaVentasActivity.limpiarVentas();
                                InicioActivity.Toast(Principalctivity.this, "Ventas eliminadas");
                            }else{
                                InicioActivity.Toast(Principalctivity.this,"Tu primera venta fue hace poco");
                                InicioActivity.Toast(Principalctivity.this,"Hace "+minutos+" minutos");
                                InicioActivity.Toast(Principalctivity.this,"Intenta de nuevo más tarde");
                                InicioActivity.Toast(Principalctivity.this,"Y podras limpiar tus ventas");
                            }
                        }
                    })
                    .setNegativeButton("NO", null);
            //.show();
            AlertDialog alert = builder.create();
            alert.show();
            Button possitive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            possitive.setTextColor(Color.parseColor("#e18a33"));
            Button negative = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            negative.setTextColor(Color.parseColor("#e18a33"));
        }else{
            InicioActivity.Toast(Principalctivity.this,"No hay ninguna venta");
        }

        // }
    }
    Configura configuracion = new Configura();
    public static Configura Configuracion(File externalFilesDir) {
        InputStreamReader flujo = null;
        BufferedReader lector = null;
        try {
            File f;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                File root1 = android.os.Environment.getExternalStorageDirectory();
                f = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta/configuracion.txt");
            }else {
                f = new File(externalFilesDir, "Android/data/com.ventaenruta/configuracion.txt");
            }

            flujo = new InputStreamReader(new FileInputStream(f));

            lector = new BufferedReader(flujo);
            String texto = lector.readLine();
            if (texto != null) config.setFTP(texto);
            texto = lector.readLine();
            if (texto != null) config.setBotones(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) config.setClave(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) config.setExistencia(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) config.setLimite(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) config.setModificar(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) config.setComentar(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) config.setSeleccionar(Integer.parseInt(texto));
            else config.setSeleccionar(config.getModificar());
            texto = lector.readLine();
            if (texto != null) config.setEditarVenta(Integer.parseInt(texto));
            else config.setEditarVenta(8);
            texto = lector.readLine();
            if (texto != null) config.setTiempoLimpiarVenta(Integer.parseInt(texto));
            else config.setTiempoLimpiarVenta(0);

        } catch (Exception ex) {
        } finally {
            try {
                if (flujo != null) flujo.close();
            } catch (IOException e) {
            }
        }

        return config;
    }

    public abstract class EnviarVentasPOST extends AsyncTask<Void, Void, Void> {
        public EnviarVentasPOST() {}
        @SuppressLint("MissingPermission")
        protected Void doInBackground(Void... progress) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(configuracion.getFTP().trim() + "recibirventas.php");
                int Mostrar = configuracion.getBotones();
                int Comentar = configuracion.getComentar();
                String cadVentas = "";
                String cadSQL = "";
                String[] meses = new String[13];meses[1] = "Ene";meses[2] = "Feb";meses[3] = "Mar";meses[4] = "Abr";meses[5] = "May";meses[6] = "Jun";meses[7] = "Jul";meses[8] = "Ago";meses[9] = "Sep";meses[10] = "Oct";meses[11] = "Nov";meses[12] = "Dic";
                for (Venta venta : ListaVentasActivity.getListaVentas()) {
                    Date fecha = venta.getFecha();
                    String fech =
                            String.format("%s %d, %d %d:%d:%d", meses[venta.getFecha().getMonth() + 1], fecha.getDate(), fecha.getYear() + 1900, fecha.getHours(), fecha.getMinutes(), fecha.getSeconds());
                    int corte = 0;
                    int contcorte = 0;
                    int limite = InicioActivity.cargarConfiguracion().getLimite();
                    for (VentaProducto vp : venta.getVentaProductos()) {
                        if (limite > 0) {
                            if (corte == limite) {
                                int minuto = fecha.getMinutes();
                                int hora = fecha.getHours();
                                if (minuto >= 59) {
                                    minuto = 0;
                                    if (hora >= 23) hora = 0;
                                    else hora++;
                                }

                                contcorte++;
                                corte = 0;
                                fech = String.format("%s %d, %d %d:%d:%d", meses[venta.getFecha().getMonth() + 1], fecha.getDate(), fecha.getYear() + 1900, hora, minuto + contcorte, fecha.getSeconds());
                            }
                            corte++;
                        }
                        if (Comentar == 0) {
                            if (Mostrar != 0) {
                                cadSQL = String
                                        .format("insert into Venta (fecha, idvendedor, idcliente, idproducto, cantidad, preciounitario, total, metodopago, numerodeprecio, nota) values ('%s', '%s', '%s', '%s', %s, %s, %s, %s, %s, '')\r\n",
                                                fech,
                                                venta.getVendedor().getClave(),
                                                venta.getCliente().getClave(),
                                                vp.getProducto().getClave(),
                                                String.format("%.2f",vp.getCantidad()).replace(",", "."),
                                                String.format("%.2f",vp.getPrecioUnitario()).replace(",", "."),
                                                String.format("%.2f",vp.getTotal()).replace(",", "."),
                                                venta.isMetodo() ? "CO" : "CR",
                                                vp.getPrecioNum()
                                        );
                            } else {
                                cadSQL = String
                                        .format("insert into Venta (fecha, idvendedor, idcliente, idproducto, cantidad, preciounitario, total, metodopago, entrada, salida, numerodeprecio, nota) values ('%s', '%s', '%s', '%s',%s , %s, %s, '%s', '%s', '%s', %s, '')\r\n",
                                                fech,
                                                venta.getVendedor().getClave(),
                                                venta.getCliente().getClave(),
                                                vp.getProducto().getClave(),
                                                String.format("%.2f",vp.getCantidad()).replace(",", "."),
                                                String.format("%.2f",vp.getPrecioUnitario()).replace(",", "."),
                                                String.format("%.2f",vp.getTotal()).replace(",", "."),
                                                venta.isMetodo() ? "CO" : "CR",
                                                VentaActivity.entrada,
                                                VentaActivity.salida,
                                                vp.getPrecioNum()
                                        );
                            }
                        } else {
                            if (Mostrar != 0) {
                                cadSQL = String
                                        .format("insert into Venta (fecha, idvendedor, idcliente, idproducto, cantidad, preciounitario, total, metodopago, numerodeprecio, nota) values ('%s', '%s', '%s', '%s', %s, %s, %s, %s, %s, %s)\r\n",
                                                fech,
                                                venta.getVendedor().getClave(),
                                                venta.getCliente().getClave(),
                                                vp.getProducto().getClave(),
                                                String.format("%.2f",vp.getCantidad()).replace(",", "."),
                                                String.format("%.2f",vp.getPrecioUnitario()).replace(",", "."),
                                                String.format("%.2f",vp.getTotal()).replace(",", "."),
                                                venta.isMetodo() ? "CO" : "CR",
                                                vp.getPrecioNum(),
                                                "'" + venta.getNota() + "'"
                                        );
                            } else {
                                cadSQL = String
                                        .format("insert into Venta (fecha, idvendedor, idcliente, idproducto, cantidad, preciounitario, total, metodopago, entrada, salida, numerodeprecio, nota) values ('%s', '%s', '%s', '%s',%s , %s, %s, '%s', '%s', '%s', %s, %s)\r\n",
                                                fech,
                                                venta.getVendedor().getClave(),
                                                venta.getCliente().getClave(),
                                                vp.getProducto().getClave(),
                                                String.format("%.2f",vp.getCantidad()).replace(",", "."),
                                                String.format("%.2f",vp.getPrecioUnitario()).replace(",", "."),
                                                String.format("%.2f",vp.getTotal()).replace(",", "."),
                                                venta.isMetodo() ? "CO" : "CR",
                                                VentaActivity.entrada,
                                                VentaActivity.salida,
                                                vp.getPrecioNum(),
                                                "'" + venta.getNota() + "'"
                                        );
                            }
                        }
                        cadVentas += cadSQL;
                    }
                }
                if (cadVentas.length() > 1) {

                    List<BasicNameValuePair> nameValuePairs = new ArrayList<>(3);
                    Date fecha = new Date();
                    String DeviceKey = InicioActivity.SharedPref.getString("devicekey", "");
                    if(DeviceKey.length()<1) {
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                            try {
                                if (ActivityCompat.checkSelfPermission(Principalctivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)return null;
                                DeviceKey = tm.getDeviceId();
                                WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                WifiInfo info = manager.getConnectionInfo();
                                if(DeviceKey==null) DeviceKey = info.getMacAddress();
                                else if (Integer.parseInt(DeviceKey)<1) DeviceKey = info.getMacAddress();
                                else if (Integer.parseInt(DeviceKey)<1) DeviceKey = null;
                                else if (DeviceKey.equals("020000000000")) DeviceKey = null;
                            } catch (Exception e) {}
                        }
                        if (DeviceKey == null || DeviceKey.length()<1) DeviceKey = (Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                        InicioActivity.SharedPref.edit().putString("devicekey", DeviceKey).apply();
                    }

                    String sfecha = String.format("%d_%d_%d_%d_%d", fecha.getDate(), fecha.getMonth() + 1, fecha.getYear() + 1900, fecha.getHours(), fecha.getMinutes());
                    //nameValuePairs.add(new BasicNameValuePair("archivo", String.format("ventas_%s_%s_%s.sql", "357743062597889", sfecha, InicioActivity.getVendedorSeleccionado().getClave())));
                    nameValuePairs.add(new BasicNameValuePair("archivo", String.format("ventas_%s_%s_%s.sql", DeviceKey, sfecha, InicioActivity.getVendedorSeleccionado().getClave())));
                    nameValuePairs.add(new BasicNameValuePair("ventas", cadVentas));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity ent = response.getEntity();
                    BufferedReader lector = new BufferedReader(new InputStreamReader(ent.getContent()));
                    lector.close();
                    if (response.getStatusLine().getStatusCode() == 200) {
                        onGetValue("Se han enviado las ventas");
                    } else {
                        onGetValue("Ocurrio un error al enviar las ventas");
                    }
                }

            } catch (ClientProtocolException e) {
                onGetValue("Ventas \n error Client:" + e.getMessage());
            } catch (IOException e) {
                onGetValue("error: Revisa tu conexion a internet.");
            } catch (Exception e) {
                onGetValue("Ventas \n error Exeption:" + e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(Void... progress) {
        }

        public abstract void onGetValue(String value);

        protected void onPostExecute(Void result) {
        }
    }
    public class ActualizarDatos extends AsyncTask<Void, Void, Void> {
        private boolean error = false;
        private String mensajeError = "";

        protected Void doInBackground(Void... progress) {
            try {
                InicioActivity.cargarDatos();
            } catch (Exception e) {
                error = true;
                mensajeError = e.toString();
                if (mensajeError.equalsIgnoreCase("java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()"))
                    mensajeError = "Revise su conexion a internet";

            }
            return null;
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(Void result) {
            if (error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        Principalctivity.this);
                builder.setMessage(mensajeError)
                        .setTitle("Error BD Cargando Datos")
                        .setCancelable(false);
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                InicioActivity.Toast(Principalctivity.this,"Catalogos de Clientes y Productos actualizados");
//                Toast toast = Toast.makeText(Principalctivity.this, "Catalogos de Clientes y Productos actualizados", Toast.LENGTH_LONG);
//                toast.show();

            }
            cargando.setVisibility(View.GONE);
        }

        protected void onPreExecute(Void result) {


        }
    }
    public abstract class EnviarCuentasPOST extends AsyncTask<Void, Void, Void> {
        public EnviarCuentasPOST() {}
        protected Void doInBackground(Void... progress) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Principalctivity.Configuracion(InicioActivity.InicioActividad.getExternalFilesDir(null)).getFTP().trim() + "recibirventas.php");
                for (String Archivo : InicioActivity.getDB().ObtenerCuentasArchivos()) {

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("archivo", Archivo));
                    String Datos = InicioActivity.getDB().ObtenerContenidoCuenta(Archivo);
                    nameValuePairs.add(new BasicNameValuePair("ventas", Datos));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity ent = response.getEntity();
                    BufferedReader lector = new BufferedReader(new InputStreamReader(ent.getContent()));
                    lector.close();
                    if (response.getStatusLine().getStatusCode() == 200) {
                        onGetValue("Se han enviado las cuentas por pagar");
                    } else {
                        onGetValue("Ocurrio un error al enviar las cuentas por pagar");
                    }
                }
            } catch (ClientProtocolException e) {
                onGetValue("Cuentas \n error Client:" + e.getMessage());
            } catch (IOException e) {
                onGetValue("error: Revisa tu conexion a internet.");
            } catch (Exception e) {
                onGetValue("Cuentas \n error Exception:" + e.getMessage());
            }
            return null;
        }
        protected void onProgressUpdate(Void... progress) {}
        public abstract void onGetValue(String value);
        protected void onPostExecute(Void result) {}
    }
    public abstract class EnviarExistenciasPOST extends AsyncTask<Void, Void, Void> {
        public EnviarExistenciasPOST() {}
        protected Void doInBackground(Void... progress) {
            try {
                InicioActivity.actualInventario();
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Principalctivity.Configuracion(InicioActivity.InicioActividad.getExternalFilesDir(null)).getFTP().trim() + "recibirventas.php");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("archivo", "productos.xml"));
                    String Datos = "<productos>";
                    for(Producto producto : InicioActivity.getListaProductos()){
                        Datos += "<producto>"+
                                "<clave>" + producto.getClave() + "</clave>"+
                                "<descripcion>"+producto.getDescripcion()+"</descripcion>"+
                                "<precio1>"+ producto.getPrecios().get(0)+"</precio1>"+
                                "<precio2>"+ producto.getPrecios().get(1)+"</precio2>"+
                                "<precio3>"+ producto.getPrecios().get(2)+"</precio3>"+
                                "<precio4>"+ producto.getPrecios().get(3)+"</precio4>"+
                                "<precio5>"+ producto.getPrecios().get(4)+"</precio5>"+
                                "<costo>"+producto.getCosto()+"</costo>"+
                                "<existencia>"+producto.getExistencia()+"</existencia>"+
                                "</producto>";
                    }
                    Datos += "</productos>";
                    Datos = Datos.replace("&","&amp;");
                    Datos = Datos.replace("ï¿½","N");
                    Datos = Datos.replace("Ñ","N");
                    Datos = Datos.replace("ñ","n");
                    nameValuePairs.add(new BasicNameValuePair("ventas", Datos));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity ent = response.getEntity();
                    BufferedReader lector = new BufferedReader(new InputStreamReader(ent.getContent()));
                    lector.close();
                    if(response.getStatusLine().getStatusCode()==200){
                        onGetValue("Las existencias se han sincronizado correctamente");
                    }else{
                        onGetValue("Ocurrio un error al actualizar las existencias");
                    }
            } catch (ClientProtocolException e) {
                onGetValue("Existencias \n error Client:"+ e.getMessage());
            } catch (IOException e) {
                onGetValue("error: Revisa tu conexion a internet.");
            } catch (Exception e) {
                onGetValue("Existencias \n error exception:"+ e.getMessage());
            }
            return null;
        }
        protected void onProgressUpdate(Void... progress) {}
        public abstract void onGetValue(String value);
        protected void onPostExecute(Void result) {}
    }

    int clicks = 0;
    public void onClickConfigHide(View view){
        clicks++;
        if(clicks == 5){
            Intent intent = new Intent(this, ImpresionActivity.class);
            startActivity(intent);
            clicks =0;
        }
    }
    public void Buttons(){
        Ventas = (Button) this.findViewById(R.id.ImageButton02);
        Limpiar = (Button) this.findViewById(R.id.Borrar);
        Subir = (Button) this.findViewById(R.id.ImageButton01);
        Ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarVentasOnClick(v);
            }
        });
        Limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarVentasOnClick(v);
            }
        });
        Subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {enviarInformacionOnClick(v);}
        });


    }
}
