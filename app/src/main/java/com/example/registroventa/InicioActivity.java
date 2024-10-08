package com.example.registroventa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;
import android.os.Looper;

import com.example.registroventa.models.Cliente;
import com.example.registroventa.models.Configura;
import com.example.registroventa.models.ConfiguracionFTP;
import com.example.registroventa.models.Cuentas;
import com.example.registroventa.models.Impresorayencabezado;
import com.example.registroventa.models.ListaPrecios;
import com.example.registroventa.models.Metodos;
import com.example.registroventa.models.PreciosAdicionales;
import com.example.registroventa.models.Producto;
import com.example.registroventa.models.Vendedor;
import com.example.registroventa.models.Venta;
import com.example.registroventa.models.VentaProducto;
import com.example.registroventa.services.BaseDatosHelper;
import com.example.registroventa.xmls.AnalizadorMetodos;
import com.example.registroventa.xmls.AnalizadorXMLCliente;
import com.example.registroventa.xmls.AnalizadorXMLConfiguracion;
import com.example.registroventa.xmls.AnalizadorXMLCuentas;
import com.example.registroventa.xmls.AnalizadorXMLListaPrecios;
import com.example.registroventa.xmls.AnalizadorXMLPreciosAdicionales;
import com.example.registroventa.xmls.AnalizadorXMLProducto;
import com.example.registroventa.xmls.AnalizadorXMLVendedor;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_SETTINGS;

public class InicioActivity extends android.app.Activity {
    private static final Void params = null;
    View view;
    public static String ubicacion = "";
    public static Context InicioActividad;
    public static SharedPreferences SharedPref;
    public static Boolean configurando = false;
    public static Configura config = new Configura();
    public static Impresorayencabezado impresiones = new Impresorayencabezado();
    private static List<Cliente> listaClientes;
    private static List<Producto> listaProductos;
    private static boolean isProductosPostCarga = false;
    private static List<Vendedor> listaVendedores;
    private static List<Cuentas> listaCuentas;
    private static List<ListaPrecios> listaPrecios;
    private static List<PreciosAdicionales> listaPadionales;
    private static List<ConfiguracionFTP> listaConfiguracion;
    private static List<Metodos> listaMetodos;
    private static List<String> listaVendedoresString;
    private static Vendedor vendedorSeleccionado;
    public static BaseDatosHelper db = null;
    private Spinner Vendedores;
    private Button ingresar;
    private Button cargarinfo;
    private TextView leyendaIngresar;
    private ProgressBar CargaInfo;
    private Intent intent;
    private TextView ProcesoMensaje;

    public static void cargarClientesAsincrono(Boolean internet) {
        new CargarClientesTask().execute(internet);
    }

    public static class CargarClientesTask extends AsyncTask<Boolean, Void, List<Cliente>> {
        @Override
        protected List<Cliente> doInBackground(Boolean... params) {
            Boolean internet = params[0];
            List<Cliente> listaClientes2 = null;
            try {
                AnalizadorXMLCliente analizador = new AnalizadorXMLCliente(ubicacion + "clientes.xml", InicioActividad);
                listaClientes2 = analizador.procesar(internet);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listaClientes2;
        }

        @Override
        protected void onPostExecute(List<Cliente> result) {
            if (result != null && !result.isEmpty()) {
                listaClientes = result;
            }
            // Actualizar UI aquí si es necesario
        }
    }
//    public static void cargarClientes(Boolean Internet) throws Exception {
//        AnalizadorXMLCliente analizador = new AnalizadorXMLCliente(ubicacion
//                + "clientes.xml",InicioActividad);
//        List<Cliente> listaClientes2 = analizador.procesar(Internet);
//        if ((listaClientes2 != null) && (listaClientes2.size() > 0))
//            listaClientes = listaClientes2;
//    }
    public static void cargarProductosAsincrono(Boolean internet) {
        new CargarProductosTask().execute(internet);
    }
private static class CargarProductosTask extends AsyncTask<Boolean, Void, List<Producto>> {
    @Override
    protected List<Producto> doInBackground(Boolean... params) {
        Boolean internet = params[0];
        List<Producto> listaProductos2 = null;
        try {
            AnalizadorXMLProducto analizador = new AnalizadorXMLProducto(ubicacion + "productos.xml", InicioActividad);
            listaProductos2 = analizador.procesar(internet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaProductos = listaProductos2;
        isProductosPostCarga = true;
        return listaProductos2;
    }

    @Override
    protected void onPostExecute(List<Producto> result) {
        if (result != null && !result.isEmpty()) {
            listaProductos = result;
            isProductosPostCarga = true;
        }
        // Actualizar UI aquí si es necesario
    }
}

    //    public static void cargarProductos(Boolean Internet) throws Exception {
//        AnalizadorXMLProducto analizador = new AnalizadorXMLProducto(ubicacion
//                + "productos.xml",InicioActividad);
//        List<Producto> listaProductos2 = analizador.procesar(Internet);
//        if ((listaProductos2 != null) && (listaProductos2.size() > 0))
//             listaProductos = listaProductos2;
//
//    }
    public static void cargarVendedores(Boolean Internet) throws Exception {
        AnalizadorXMLVendedor analizador = new AnalizadorXMLVendedor(ubicacion
                + "vendedores.xml",InicioActividad);
        Vendedor vendedorvacio = new Vendedor();
        vendedorvacio.setClave("0");
        vendedorvacio.setNombre("N");
        List<Vendedor> listaVendedores2 = analizador.procesar(Internet);
        if ((listaVendedores2 != null))
            if ((listaVendedores2.size() > 0))
                listaVendedores = listaVendedores2;
            else
                listaVendedores.add(vendedorvacio);
    }
    public static void cargarCuentas(Boolean Internet) throws Exception {
        AnalizadorXMLCuentas analizador = new AnalizadorXMLCuentas(ubicacion
                + "cxcs.xml",InicioActividad);
        List<Cuentas> listaCuentas2 = analizador.procesar(Internet);
        listaCuentas = new ArrayList<Cuentas>();

        if ((listaCuentas2 != null) && (listaCuentas2.size() > 0))
            listaCuentas = listaCuentas2;
    }
    public static void cargarListaPrecios(Boolean Internet) throws Exception {
        AnalizadorXMLListaPrecios analizador = new AnalizadorXMLListaPrecios(ubicacion
                + "listadeprecios.xml",InicioActividad);
            List<ListaPrecios> listaPrecios2 = analizador.procesar(Internet);
            if ((listaPrecios2 != null) && (listaPrecios2.size() > 0))
                listaPrecios = listaPrecios2;
    }
    public static void cargarPreciosAdicionales(Boolean Internet) throws Exception {
        AnalizadorXMLPreciosAdicionales analizador = new AnalizadorXMLPreciosAdicionales(ubicacion
                + "preciosadicionales.xml",InicioActividad);
        List<PreciosAdicionales> listaPrecios2 = analizador.procesar(Internet);
        if ((listaPrecios2 != null) && (listaPrecios2.size() > 0))
            listaPadionales = listaPrecios2;
    }
    public static void cargarConfiguracion(Boolean Internet) throws Exception {
        AnalizadorXMLConfiguracion analizador = new AnalizadorXMLConfiguracion(ubicacion
                + "configuracion.xml",InicioActividad);
        List<ConfiguracionFTP> listaConfiguracion2 = analizador.procesar(Internet);
        if ((listaConfiguracion2 != null) && (listaConfiguracion2.size() > 0))
            listaConfiguracion = listaConfiguracion2;
    }
    public static void cargarMetodos(Boolean Internet) throws Exception {
        AnalizadorMetodos analizador = new AnalizadorMetodos(ubicacion
                + "formasdepago.xml",InicioActividad);
        List<Metodos> listaMetodos2 = analizador.procesar(Internet);
        if ((listaMetodos2 != null) && (listaMetodos2.size() > 0))
            listaMetodos = listaMetodos2;
    }
    public static Configura cargarConfiguracion() {
        return config;
    }
//
//    public static void cargarDatos(boolean isInternet) throws Exception {
//        //ubicacion = InicioActivity.cargarConfiguracion().getFTP();
//        try {cargarClientes(isInternet);}catch (Exception e) {}
//        try {cargarProductos(isInternet);}catch (Exception e) {}
//        try {cargarVendedores(isInternet);}catch (Exception e) {}
//        try {cargarCuentas(isInternet);}catch (Exception e) {}
//        try {cargarListaPrecios(isInternet);}catch (Exception e) {}
//        try {cargarConfiguracion(isInternet);}catch (Exception e) {}
//        try {cargarPreciosAdicionales(isInternet);}catch (Exception e) {}
//        try {cargarMetodos(isInternet);}catch (Exception e) {}
//    }
//
//    public static Configura cargarConfiguracionvieja() {
//        Configura nuevo = new Configura();
//        try {
//            nuevo = db.obtenerConfiguracion();
//        } catch (Exception e) {
//            db.agregarnuevaConfiguracion();
//        }
//        return nuevo;
//    }
//    public static Impresorayencabezado cargarImpresion() {
//        Impresorayencabezado nuevo = new Impresorayencabezado();
//        try {
//            nuevo = db.obtenerImpresion();
//        } catch (Exception e) {
//        }
//        return nuevo;
//    }
//    public static void guardarBase(Configura configuracion) {
//        db.agregarConfiguracion(configuracion);
//    }
//    public static void guardarDatos(Impresorayencabezado ImpresionDatos) {
//        db.agregarImpresion(ImpresionDatos);
//    }
    public static BaseDatosHelper getDB() {
        return db;
    }
    public static List<Cliente> getListaClientes() {
        if(listaClientes==null)listaClientes = new ArrayList<Cliente>();
        return listaClientes;
    }
    public static List<Producto> getListaProductos() {
        return listaProductos;
    }
    public static List<Vendedor> getListaVendedores() {
        return listaVendedores;
    }
    public static List<Cuentas> getListaCuentas() {
        if(listaCuentas==null)listaCuentas = new ArrayList<Cuentas>();
        return listaCuentas;
    }
    public static List<ListaPrecios> getListaPreciosNegociados() {
        if(listaPrecios==null)listaPrecios = new ArrayList<ListaPrecios>();
        return listaPrecios;
    }
    public static List<PreciosAdicionales> getListaPreciosAdicionales() {
        if(listaPadionales==null)listaPadionales = new ArrayList<PreciosAdicionales>();
        return listaPadionales;
    }
    public static List<ConfiguracionFTP> getListaConfiguracion() {
        List<ConfiguracionFTP> listConfig = listaConfiguracion;
        if(listaConfiguracion == null)
        {
            listConfig = new ArrayList<>();
            ConfiguracionFTP config = new ConfiguracionFTP();
            config.setAsistencia(true);
            config.setCapturapagos(true);
            config.setModificarprecios(true);
            config.setVercxc(true);
            config.setModificarprecios(true);
            config.setClaveconfiguracion("");
            config.setSerieclientes("");
            listConfig.add(config);
        }
        return listConfig;
    }
    public static List<Metodos> getListaMetodos() {
        if(listaMetodos==null)listaMetodos = new ArrayList<Metodos>();
        return listaMetodos;
    }

    public static Vendedor getVendedorSeleccionado() {
        return vendedorSeleccionado;
    }

    public static void setVendedorSeleccionado(Vendedor vendedorSeleccionado) {
        InicioActivity.vendedorSeleccionado = vendedorSeleccionado;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        view = getLayoutInflater().inflate(R.layout.activity_inicio, null);
        //TextView tv = (TextView) view.findViewById(R.id.sample);
        db = new BaseDatosHelper(InicioActividad,
                "BDRegistroVentas", null, 11);

        InicioActividad = this;
        SharedPref = this.getPreferences(Context.MODE_PRIVATE);
        CargaInfo = (ProgressBar) this.findViewById(R.id.Cargandoinfo);
        Vendedores = (Spinner) this.findViewById(R.id.Vendedores);
        ProcesoMensaje = (TextView) this.findViewById(R.id.ProgressMensaje);
        CargaInfo.setVisibility(View.GONE);
        ProcesoMensaje.setVisibility(View.GONE);
        ingresar = (Button) findViewById(R.id.acepta);
        cargarinfo = (Button) findViewById(R.id.cargar);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Vendedores.getSelectedItem() != null) {
                    String svendedor = Vendedores.getSelectedItem().toString();

                    for (Vendedor vendedor : listaVendedores)
                        if (svendedor.equals(vendedor.getClave() + " - "
                                + vendedor.getNombre())) {
                            vendedorSeleccionado = vendedor;
                            break;
                        }
                    if (vendedorSeleccionado != null) {

                        final Intent principalActivity = new Intent(InicioActividad, Principalctivity.class);
                        if (InicioActivity.getVendedorSeleccionado().getNip() == null)
                            startActivity(principalActivity);
                        else if (InicioActivity.getVendedorSeleccionado().getNip().length() < 0)
                            startActivity(principalActivity);
                        else {
                            final EditText NipText = new EditText(InicioActivity.this);
                            LinearLayout.LayoutParams Style = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            NipText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                            NipText.setLayoutParams(Style);
                            AlertDialog.Builder builder = new AlertDialog.Builder(InicioActivity.this);
                            builder
                                    .setMessage("Tu usuario cuenta con un NIP, ingresalo para continuar el envio")
                                    .setTitle("Usuario con un NIP")
                                    .setView(NipText)
                                    .setPositiveButton("Validar NIP", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (NipText.getText().toString().contentEquals(InicioActivity.getVendedorSeleccionado().getNip()))
                                                startActivity(principalActivity);
                                            else
                                                InicioActivity.Toast(InicioActivity.this, "NIP INCORRECTO");
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                            possitive.setTextColor(Color.parseColor("#e18a33"));
                        }

                    }
                } else {
                    InicioActivity.Toast(InicioActivity.this, "No hay vendedores");
                }
            }
        });
        Vendedores = (Spinner) this.findViewById(R.id.Vendedores);
        //ingresar = (ImageButton) this.findViewById(R.id.acepta);
        //cargarinfo = (ImageButton) this.findViewById(R.id.cargar);
        leyendaIngresar = (TextView) this.findViewById(R.id.leyendaIngresar);
        CargaInfo = (ProgressBar) this.findViewById(R.id.Cargandoinfo);
        cargarinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProcesoMensaje.setVisibility(View.VISIBLE);
                CargaInfo.setVisibility(View.VISIBLE);
                Vendedores.setVisibility(View.GONE);
                //ingresar.setVisibility(View.GONE);
                //cargarinfo.setVisibility(View.GONE);
                leyendaIngresar.setText("Cargando Información, espere por favor...");

                listaVendedoresString = new ArrayList<String>();
                cargarProductosAsincrono(true);
                cargarClientesAsincrono(true);
                CargarDatos cargar = new CargarDatos(InicioActividad, db, true);
                cargar.execute(params);

                try {
                    leerConfiguracion();
                    ubicacion = InicioActivity.cargarConfiguracion().getFTP();
                    InputStreamReader flujo = null;
                    BufferedReader lector = null;
                    try {
                        flujo = new InputStreamReader(openFileInput("configuracion.xml"));
                        lector = new BufferedReader(flujo);
                        String texto = lector.readLine();
                        if (texto != null) ubicacion = texto;

                    } catch (Exception ex) {
                    } finally {
                        try {
                            if (flujo != null) flujo.close();
                        } catch (IOException e) {
                        }
                    }

                } catch (Exception e) {
                }
                db = new BaseDatosHelper(InicioActividad,
                        "BDRegistroVentas", null, 11);
                cargarProductosAsincrono(true);
                cargarClientesAsincrono(true);
                cargar = new CargarDatos(InicioActividad, db, true);
                cargar.execute(params);
                //db.limpiarClientes(listaClientes);
                //db.limpiarProductos(listaProductos);
                //db.limpiarVendedores(listaVendedores);
            }
        });
        if (!mayRequestStoragePermission()) {
            return;
        }
        db = new BaseDatosHelper(this, "BDRegistroVentas", null, 11);
        try {
            cargaPrincipal();
            if(ubicacion.length()>5) {
                cargarVendedores(false);
                cargarConfiguracion(false);

                listaVendedoresString = new ArrayList<String>();
                for (Vendedor vendedor : listaVendedores) {
                    String Vendedor = (vendedor.getClave() + " - " + vendedor.getNombre());
                    listaVendedoresString.add(Vendedor);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(InicioActividad, android.R.layout.simple_dropdown_item_1line, listaVendedoresString);
                Vendedores.setAdapter(adapter);
                Vendedores.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Cargar datos en segundo plano después de que la UI inicial se haya mostrado
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if(ubicacion.length()>5) {
                        cargarProductosAsincrono(false);
                        cargarClientesAsincrono(false);
                        CargarDatos cargar = new CargarDatos(InicioActividad, db, false);
                        cargar.execute(params);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);
    }
   public void cargaPrincipal() {
//        try {cargarClientes(false);} catch (Exception e) {}
//        try {cargarProductos(false);} catch (Exception e) {}
//        try {cargarVendedores(false);} catch (Exception e) {}
//        try {cargarCuentas(false);} catch (Exception e) {}
//        try {cargarListaPrecios(false);} catch (Exception e) {}
//        try {cargarPreciosAdicionales(false);} catch (Exception e) {}
//        try {cargarConfiguracion(false);} catch (Exception e) {}
//        try {cargarMetodos(false);} catch (Exception e) {}
       if (!configurando) {
           try {
               Impresorayencabezado impresion = db.obtenerImpresion();
               Configura configuracionguardada = db.obtenerConfiguracion();
               String FTP = configuracionguardada.getFTP();
               if(FTP ==null)FTP = "";
               if (FTP.length() > 5) {
                   OutputStreamWriter escritor = null;
                   try {
                       File f;
                       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                           File root1 = android.os.Environment.getExternalStorageDirectory();
                           f = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta/configuracion.txt");
                       } else {
                           f = new File(getExternalFilesDir(null), "Android/data/com.ventaenruta/configuracion.txt");
                       }
                       escritor = new OutputStreamWriter(new FileOutputStream(f));
                       escritor.write(configuracionguardada.getFTP() + "\n");
                       escritor.write(configuracionguardada.getBotones() + "\n");
                       escritor.write(configuracionguardada.getClave() + "\n");
                       escritor.write(configuracionguardada.getExistencia() + "\n");
                       escritor.write(configuracionguardada.getLimite() + "\n");
                       escritor.write(configuracionguardada.getModificar() + "\n");
                   } catch (Exception ex) {
                   } finally {
                       try {
                           if (escritor != null) escritor.close();
                       } catch (IOException e) {
                       }
                   }
               } else {
                   OutputStreamWriter escritor = null;
                   if (leerConfiguracion().getFTP() == null) {
                       try {
                           File f;
                           if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                               File root1 = android.os.Environment.getExternalStorageDirectory();
                               f = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta/configuracion.txt");
                           } else {
                               f = new File(getExternalFilesDir(null), "Android/data/com.ventaenruta/configuracion.txt");
                           }
                           escritor = new OutputStreamWriter(new FileOutputStream(f));
                           escritor.write(" \n");
                           escritor.write("8\n");
                           escritor.write("0\n");
                           escritor.write("0\n");
                           escritor.write("0\n");
                           escritor.write("0\n");
                       } catch (FileNotFoundException e1) {
                           e1.toString();
                       } catch (IOException e1) {
                           e1.toString();
                       } finally {
                           try {
                               if (escritor != null) escritor.close();
                           } catch (IOException ea) {
                           }
                       }
                   }
               }
               if (impresion.getencabezado1().length() > 2) {
                   OutputStreamWriter escritor = null;
                   try {
                       File f;
                       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                           File root1 = android.os.Environment.getExternalStorageDirectory();
                           f = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta/impresoras.txt");
                       } else {
                           f = new File(getExternalFilesDir(null), "Android/data/com.ventaenruta/impresoras.txt");
                       }
                       escritor = new OutputStreamWriter(new FileOutputStream(f));
                       escritor.write(impresion.getencabezado1() + "\n");
                       escritor.write(impresion.getencabezado2() + "\n");
                       escritor.write(impresion.getencabezado3() + "\n");
                       escritor.write(impresion.getmacAdress() + "\n");
                       escritor.write(impresion.getmostrarImpresion() + "\n");
                       escritor.write(impresion.getnombreImpresora() + "\n");
                   } catch (Exception ex) {
                   } finally {
                       try {
                           if (escritor != null) escritor.close();
                       } catch (IOException e) {
                       }
                   }
               } else {
                   OutputStreamWriter escritor = null;
                   if (leerImpresion().getencabezado1() == null) {
                       try {
                           File f;
                           if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                               File root1 = android.os.Environment.getExternalStorageDirectory();
                               f = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta/impresoras.txt");
                           } else {
                               f = new File(getExternalFilesDir(null), "Android/data/com.ventaenruta/impresoras.txt");
                           }
                           escritor = new OutputStreamWriter(new FileOutputStream(f));
                           escritor.write(" \n \n \n \n 0\n \n");
                       } catch (FileNotFoundException e1) {
                           e1.toString();
                       } catch (IOException e1) {
                           e1.toString();
                       } finally {
                           try {
                               if (escritor != null) escritor.close();
                           } catch (IOException ea) {
                           }
                       }
                   }
               }
           } catch (Exception e) {
               OutputStreamWriter escritor = null;
               if (leerConfiguracion().getFTP() == null) {
                   try {
                       File f;
                       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                           File root1 = android.os.Environment.getExternalStorageDirectory();
                           f = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta/configuracion.txt");
                       } else {
                           f = new File(getExternalFilesDir(null), "Android/data/com.ventaenruta/configuracion.txt");
                       }
                       escritor = new OutputStreamWriter(new FileOutputStream(f));
                       escritor.write(" \n");
                       escritor.write("8\n");
                       escritor.write("0\n");
                       escritor.write("0\n");
                       escritor.write("0\n");
                       escritor.write("0\n");
                   } catch (FileNotFoundException e1) {
                       e1.toString();
                   } catch (IOException e1) {
                       e1.toString();
                   } finally {
                       try {
                           if (escritor != null) escritor.close();
                       } catch (IOException ea) {
                       }
                   }
               }
           }
           if (listaVendedores != null) {
               listaVendedoresString = new ArrayList<String>();
               for (Vendedor vendedor : listaVendedores) {
                   String Vendedor = (vendedor.getClave() + " - " + vendedor.getNombre());
                   listaVendedoresString.add(Vendedor);
               }
           } else {
               listaVendedoresString = new ArrayList<String>();
               //listaVendedoresString.add("NINGUNO");
           }
       }

       ArrayAdapter<String> adapter = new ArrayAdapter<String>(InicioActividad, android.R.layout.simple_dropdown_item_1line, 0, listaVendedoresString);
       Vendedores.setAdapter(adapter);

       try {
           leerConfiguracion();
           leerImpresion();
           ubicacion = InicioActivity.cargarConfiguracion().getFTP();
           Configura vacia = new Configura();
           db.agregarConfiguracion(vacia);
       } catch (Exception e) {

       }
       if (ubicacion == null) {
           noFTP();
       } else if (ubicacion == "null" || ubicacion.length() < 5) {
           noFTP();
       }
    }
    public void cargarOnClick(View view) {
        Vendedores = (Spinner) this.findViewById(R.id.Vendedores);
        //ingresar = (ImageButton) this.findViewById(R.id.acepta);
        //cargarinfo = (ImageButton) this.findViewById(R.id.cargar);
        leyendaIngresar = (TextView) this.findViewById(R.id.leyendaIngresar);
        CargaInfo = (ProgressBar) this.findViewById(R.id.Cargandoinfo);
        ProcesoMensaje.setVisibility(View.VISIBLE);
        CargaInfo.setVisibility(View.VISIBLE);
        Vendedores.setVisibility(View.GONE);
        //ingresar.setVisibility(View.GONE);
        //cargarinfo.setVisibility(View.GONE);
        leyendaIngresar.setText("Cargando Información, espere por favor...");

        listaVendedoresString = new ArrayList<String>();
        db = new BaseDatosHelper((android.content.Context) this,
                "BDRegistroVentas", null, 10);
        cargarProductosAsincrono(true);
        cargarClientesAsincrono(true);
        CargarDatos cargar = new CargarDatos(this, db, true);
        cargar.execute(params);

        try {
            leerConfiguracion();
            ubicacion = InicioActivity.cargarConfiguracion().getFTP();
            InputStreamReader flujo = null;
            BufferedReader lector = null;
            try {
                flujo = new InputStreamReader(openFileInput("configuracion.xml"));
                lector = new BufferedReader(flujo);
                String texto = lector.readLine();
                if (texto != null) ubicacion = texto;

            } catch (Exception ex) {
            } finally {
                try {
                    if (flujo != null) flujo.close();
                } catch (IOException e) {
                }
            }

        } catch (Exception e) {
        }
        db = new BaseDatosHelper((android.content.Context) this,
                "BDRegistroVentas", null, 10);
        cargarProductosAsincrono(true);
        cargarClientesAsincrono(true);
        cargar = new CargarDatos(this, db, true);
        cargar.execute(params);
        //db.limpiarClientes(listaClientes);
        //db.limpiarProductos(listaProductos);
        //db.limpiarVendedores(listaVendedores);
    }
    boolean OpenNoFTP = false;
    private void noFTP() {
        if(!OpenNoFTP) {
            OpenNoFTP = true;
            intent = new Intent(this, ConfiguracionActivity.class);

            AlertDialog.Builder build = new AlertDialog.Builder(this);
            build.setMessage("FTP no configurado o vacio")
                    .setTitle("Configurar FTP")
                    .setCancelable(false);
            build.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(intent);
                }
            });
            AlertDialog dialog = build.create();
            dialog.show();

            Button negative = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            negative.setTextColor(Color.parseColor("#e18a33"));
        }
    }
    static List<Venta> listaVentas = new ArrayList<Venta>();
    int esperaMaxima = 150;
    private void cargarDatosBD() {
        try {
            while ((listaClientes == null || listaProductos == null || listaVendedores == null) && esperaMaxima > 0) {
                Thread.sleep(100);
                esperaMaxima--;
            }if (esperaMaxima <= 0) {
                Toast.makeText(this, "Error al cargar la base de datos", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Revisa las bases de datos desde PuntoZero", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Se requiere de CLIENTES, PRODUCTOS Y VENDEDORES", Toast.LENGTH_LONG).show();
                if(listaClientes== null) {
                    Toast.makeText(this, "Fallo en CLIENTES", Toast.LENGTH_LONG).show();
                }
                if(listaProductos== null) {
                    Toast.makeText(this, "Fallo en PRODUCTOS", Toast.LENGTH_LONG).show();
                }
                if(listaVendedores== null) {
                    Toast.makeText(this, "Fallo en VENDEDORES", Toast.LENGTH_LONG).show();
                }
                return;
            }
            listaVentas = InicioActivity.getDB().cargarVentas(
                    InicioActivity.getListaClientes(),
                    InicioActivity.getListaVendedores(),
                    InicioActivity.getListaProductos());
            ListaVentasActivity.setListaVentas(listaVentas);
            actualInventario();
        } catch (Exception e) {
            if (InicioActivity.getListaClientes() == null ||
                    InicioActivity.getListaVendedores() == null ||
                    InicioActivity.getListaProductos() == null) {
                AlertDialog.Builder build = new AlertDialog.Builder(this);
                build.setTitle("Descargar base de datos de nuevo")
                        .setMessage("Base de datos no encontrada o dañada")
                        .setCancelable(false);
                build.setPositiveButton("Ok, enterado", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        cargarOnClick(null);
                    }
                });
//                build.setNegativeButton("Cancelar", null);
                AlertDialog dialog = build.create();
                dialog.show();

                Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                possitive.setTextColor(Color.parseColor("#e18a33"));
                Button negative = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                negative.setTextColor(Color.parseColor("#e18a33"));
            }
        }
    }
    //region CargaDB Vieja
    /*class CargarBD extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                publishProgress("Guardando Datos...");
                cargarBD();
                publishProgress(" ");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

         protected void onProgressUpdate(String... values) {
             super.onProgressUpdate(values);
                String progreso = values[0];
                ProcesoMensaje.setText(progreso);
            }
    }
    public static void cargarBD() throws Exception{

        //db.limpiarVendedores(listaVendedores);
        //db.limpiarClientes(listaClientes);
        //db.limpiarProductos(listaProductos);

    }*/
    //endregion
    Menu menuConfiguracion;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menuConfiguracion = menu;
        if(listaConfiguracion == null)
        {
            menu.add(0, 0, 0, "Configuracion");
            menu.add(0, 1, 1, "Impresion");
        }
        else if(listaConfiguracion.get(0).getAsistencia())
        {
            menu.add(0, 0, 0, "Configuracion");
            menu.add(0, 1, 1, "Impresion");
        }
        else if(!listaConfiguracion.get(0).getAsistencia()){
            menu.clear();
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

    public Configura leerConfiguracion() {
        InputStreamReader flujo = null;
        BufferedReader lector = null;
        try {
            File f;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                File root1 = android.os.Environment.getExternalStorageDirectory();
                f = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta/configuracion.txt");
            }else {
                f = new File(getExternalFilesDir(null), "Android/data/com.ventaenruta/configuracion.txt");
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

    public Impresorayencabezado leerImpresion() {
        InputStreamReader flujo = null;
        BufferedReader lector = null;
        try {
            File f;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                File root1 = android.os.Environment.getExternalStorageDirectory();
                f = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta/impresoras.txt");
            }else {
                f = new File(getExternalFilesDir(null), "Android/data/com.ventaenruta/impresoras.txt");
            }
            flujo = new InputStreamReader(new FileInputStream(f));
            lector = new BufferedReader(flujo);
            String texto = lector.readLine();
            if (texto != null) impresiones.setecabezado1(texto);
            texto = lector.readLine();
            if (texto != null) impresiones.setecabezado2(texto);
            texto = lector.readLine();
            if (texto != null) impresiones.setecabezado3(texto);
            texto = lector.readLine();
            if (texto != null) impresiones.setmacAdress(texto);
            texto = lector.readLine();
            if (texto != null) impresiones.setmostrarImpresion(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) impresiones.setnombreImpresora(texto);
            texto = lector.readLine();
            if (texto != null)InicioActivity.impresiones.setimprimirCorte(Integer.parseInt(texto));

        } catch (Exception ex) {
        } finally {
            try {
                if (flujo != null) flujo.close();
            } catch (IOException e) {
            }
        }

        return impresiones;
    }

    public void aceptarOnClick(View view) {

        if (Vendedores.getSelectedItem() != null) {
            String svendedor = Vendedores.getSelectedItem().toString();

            for (Vendedor vendedor : listaVendedores)
                if (svendedor.equals(vendedor.getClave() + " - "
                        + vendedor.getNombre())) {
                    vendedorSeleccionado = vendedor;
                    break;
                }
            if (vendedorSeleccionado != null) {
                Intent intent = new Intent(this, Principalctivity.class);
                startActivity(intent);
            }
        } else {
            InicioActivity.Toast(InicioActivity.this, "No hay vendedores");
        }

    }
    public static String[] mensajeErrorDB = new String[]{"","","","","",""};
    class CargarDatos extends AsyncTask<Void, String, Void> {
        BaseDatosHelper db;
        private android.content.Context context;
        private boolean error = false;
        private String mensajeError = "";
        private boolean isInternet = false;

        public CargarDatos(android.content.Context context, BaseDatosHelper db, boolean isInternet) {
            this.context = context;
            this.db = db;
            this.isInternet = isInternet;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String base="Vendedores.xml";
            try {
                try {
                    publishProgress("Procesando Vendedores...");
                    cargarVendedores(isInternet);
                    base = "Clientes.xml";
                }catch (Exception e) {mensajeErrorDB[0] = e.toString();}
                try {
                    publishProgress("Procesando Clientes...");

                    base="Productos.xml";
                }catch (Exception e) {mensajeErrorDB[0] = e.toString();}
                try {
                    publishProgress("Procesando Productos...");

                    base="Cuentas.xml";
                }catch (Exception e) {mensajeErrorDB[0] = e.toString();}
                try {
                    publishProgress("Procesando Cuentas por pagar...");
                    cargarCuentas(isInternet);base="Configuracion.xml";
                }catch (Exception e) {mensajeErrorDB[0] = e.toString();}
                try {
                    publishProgress("Procesando Configuracion...");
                    cargarConfiguracion(isInternet);base="ListadePrecios.xml";
                }catch (Exception e) {mensajeErrorDB[0] = e.toString();}
                try {
                    publishProgress("Procesando Precios Negociados...");
                    cargarListaPrecios(isInternet);base="PreciosAdicionales.xml";
                }catch (Exception e) {mensajeErrorDB[0] = e.toString();}
                try {
                    publishProgress("Procesando Precios Adicionales...");
                    cargarPreciosAdicionales(isInternet);base="MetodosPago.xml";
                }catch (Exception e) {mensajeErrorDB[0] = e.toString();}
                try {
                    publishProgress("Procesando Metodos de Pago...");
                    cargarMetodos(isInternet);
                    publishProgress(" ");
                }catch (Exception e) {mensajeErrorDB[0] = e.toString();}
            } catch (Exception e) {
                publishProgress(" ");
                error = true;
                mensajeError = e.toString();
                if (mensajeError.equalsIgnoreCase("java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()"))
                    mensajeError = "Revise su conexion a internet";
                if (mensajeError.equalsIgnoreCase("java.io.FileNotFoundException: /mnt/sdcard/Android/data/com.ventaenruta/vendedor.xml: open failed: ENOENT (No such file or directory)"))
                    mensajeError = "No se crearon los archivos. Revise su conexion a internet";
                if (listaVendedores == null)
                    mensajeError = "No se encontraron vendedores en la base de datos. Favor de verificar";
                if(mensajeError.indexOf("not well-formed")>0 || mensajeError.indexOf("unclosed token")>0 || mensajeError.indexOf("FileNotFoundException")>0)
                    mensajeError = "El archivo "+base+" de la base de datos esta dañado, comuniquese con el Administrador";
                if (mensajeError.indexOf("PreciosAdicionales")>0)
                    error = false;
            }
            return null;
        }

        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String progreso = values[0];
            ProcesoMensaje.setText(progreso);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            CargaInfo.setVisibility(View.GONE);
            ProcesoMensaje.setVisibility(View.GONE);
            if (ubicacion == null) {
                noFTP();
            } else if (ubicacion == "null" || ubicacion.length() < 5) {
                noFTP();
            }
            configurando = false;

            //Abonos Add
            List<Cuentas> abonos = InicioActivity.getDB().ObtenerCuentas();
            if (listaCuentas == null)
                listaCuentas = new ArrayList<Cuentas>();
            listaCuentas.addAll(abonos);

            menuConfiguracion.clear();
            if(listaConfiguracion == null)
            {
                menuConfiguracion.add(0, 0, 0, "Configuracion");
                menuConfiguracion.add(0, 1, 1, "Impresion");
            }
            else if(listaConfiguracion.get(0).getAsistencia())
            {
                menuConfiguracion.add(0, 0, 0, "Configuracion");
                menuConfiguracion.add(0, 1, 1, "Impresion");
            }
            getMenuInflater().inflate(R.menu.inicio, menuConfiguracion);
            if (error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        InicioActivity.this);
                builder.setMessage(mensajeError)
                        .setTitle("Error BD Cargando Datos")
                        .setCancelable(false);
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                if (ubicacion != null || ubicacion.length() < 5 || mensajeError == "No se crearon los archivos. Revise su conexion a internet") {
                    dialog.show();
                    Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    possitive.setTextColor(Color.parseColor("#e18a33"));
                    Button negative = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    negative.setTextColor(Color.parseColor("#e18a33"));
                }
            } else if (listaVendedores != null) {
//                Toast(InicioActivity.this,"Base de datos actualizada");
                for(String errorDB:mensajeErrorDB) {
                    if (errorDB.length() > 0) {
//                        Toast(InicioActivity.this, "Error: ");
                        Toast(InicioActivity.this, errorDB);
                       // Toast(InicioActivity.this, "Revise sus bases de datos en PuntoZero");
                    }
                }
                mensajeErrorDB = new String[]{"","","","","","",""};
                listaVendedoresString = new ArrayList<String>();
                for (Vendedor vendedor : listaVendedores) {
                    String Vendedor = (vendedor.getClave() + " - " + vendedor.getNombre());
                    listaVendedoresString.add(Vendedor);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, listaVendedoresString);
                Vendedores.setAdapter(adapter);

            }

//            if ((mensajeError != "No se crearon los archivos. Revise su conexion a internet")
//                    && (mensajeError != "Revise su conexion a internet")
//                    && (mensajeError != "No se encontraron vendedores en la base de datos. Favor de verificar")
//                    ) {
//                listaVendedoresString = new ArrayList<String>();
//                for (Vendedor vendedor : listaVendedores) {
//                    String Vendedor = (vendedor.getClave() + " - " + vendedor.getNombre());
//                    listaVendedoresString.add(Vendedor);
//                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, listaVendedoresString);
//                Vendedores.setAdapter(adapter);
//            }
            CargaInfo.setVisibility(View.GONE);
            Vendedores.setVisibility(View.VISIBLE);
            //ingresar.setVisibility(View.VISIBLE);
            leyendaIngresar.setText("Vendedor");
            CargaInfo.setVisibility(View.GONE);
            ProcesoMensaje.setVisibility(View.GONE);
            //cargarinfo.setVisibility(View.VISIBLE);
            // Crear un Handler
            Handler handler = new Handler(Looper.getMainLooper());

// Ejecutar la tarea después del retraso especificado
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Código que deseas ejecutar después del retraso
                    mayRequestStoragePermission();
                    cargarDatosBD();
                }
            }, 100);
        }

        public BaseDatosHelper getDb() {
            return db;
        }

    }
    public static void Toast(android.app.Activity context, String Message){
        try {
            Toast toast = Toast.makeText(context,Message, Toast.LENGTH_SHORT);
            View toastView = toast.getView(); // This'll return the default View of the Toast.
            toastView.setMinimumWidth(7777);
            /* And now you can get the TextView of the default View of the Toast. */
            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
            toastMessage.setTextSize(15);
            toastMessage.setTextColor(Color.WHITE);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
            toastMessage.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            //toastMessage.setWidth(getScreenWidth(context));
            toastView.setBackgroundColor(Color.parseColor("#b15a03"));
            //toastMessage.setCompoundDrawablePadding(16);
            toast.show();
        }catch(Exception e){
            e.getMessage();
        }
    }
    public static int getScreenWidth(android.app.Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x+300;
    }

    private static final int MY_PERMISSIONS_REQUEST_STORAGE_LEGACY = 101;
        private final int MY_PERMISSIONS = 100;
        private boolean mayRequestStoragePermission() {
            boolean storagePermission = false;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                storagePermission = (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            }else{
                storagePermission = Environment.isExternalStorageManager();
            }
            if(storagePermission && (checkSelfPermission(READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
                return true;
            }
                AlertDialog.Builder confirmacion = new AlertDialog.Builder(InicioActivity.this);
                confirmacion.setIcon(R.mipmap.ic_launcherpz);
                confirmacion.setTitle("ACEPTAR PERMISOS");
                confirmacion.setMessage("Necesitamos algunos permisos, por favor concede los permisos para utilizar optimamente nuestra app.");
                confirmacion.setPositiveButton("PERMITIR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))
                                || (shouldShowRequestPermissionRationale(READ_PHONE_STATE))) {
                            showExplanation();
                        } else {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, ACCESS_WIFI_STATE, READ_PHONE_STATE, BLUETOOTH_ADMIN,BLUETOOTH_ADMIN}, MY_PERMISSIONS);
                            }else{
                                requestPermissions(new String[]{ACCESS_WIFI_STATE, READ_PHONE_STATE, BLUETOOTH_ADMIN,BLUETOOTH_ADMIN}, MY_PERMISSIONS);
                            }

                        }
                    }
                });
                AlertDialog alert = confirmacion.create();
                alert.show();
                Button possitive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                possitive.setTextColor(Color.parseColor("#e18a33"));
            return false;
        }
        @Override
       public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode == MY_PERMISSIONS){
                int permission = Arrays.stream(grantResults).sum();
                if(permission == PackageManager.PERMISSION_GRANTED){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, MY_PERMISSIONS_REQUEST_STORAGE_LEGACY);
                    }else {
                        Toast(InicioActivity.this, "Permisos aceptados");
                        this.finish();
                        Intent intent2 = new Intent(InicioActivity.this, InicioActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                    }
                } else {
                    showExplanation();
                }
            }else{
                showExplanation();
            }
        }
    private void showStoragePermissionRationale() {
        new AlertDialog.Builder(this)
                .setTitle("Permiso necesario")
                .setMessage("Necesitas permitir el acceso al almacenamiento para utilizar optimamente nuestra app.")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, MY_PERMISSIONS_REQUEST_STORAGE_LEGACY);
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSIONS_REQUEST_STORAGE_LEGACY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Toast(InicioActivity.this, "Permisos aceptados");
                    this.finish();
                    Intent intent2 = new Intent(InicioActivity.this, InicioActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent2);
                    // Permiso concedido para Android 11 y superiores
                } else {
                    // Permiso denegado
                    showStoragePermissionRationale();
                }
            }
        }
    }
        private void showExplanation() {
            AlertDialog.Builder builder = new AlertDialog.Builder(InicioActivity.this);
            builder.setTitle("Permisos denegados").setIcon(R.mipmap.ic_launcherpz);
            builder.setMessage("Denegaste los permisos anteriormente, activalos manualmente en Permisos de CONFIGURACIÓN.");
            builder.setPositiveButton("CONFIGURACIÓN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog alert=builder.create();
            alert.show();
            Button possitive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            possitive.setTextColor(Color.parseColor("#e18a33"));
            Button negative = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            negative.setTextColor(Color.parseColor("#e18a33"));
        }
    int clicks = 0;
    public void onClickConfigHide(View view){
        clicks++;
        if(clicks == 5){
            Intent intent = new Intent(this, ConfiguracionActivity.class);
            startActivity(intent);
            clicks =0;
        }
    }
    public static void actualInventario(){
        try {cargarProductosAsincrono(false);} catch (Exception e) {}
        for(Producto producto: listaProductos){
            for( Venta venta :listaVentas){
                for (VentaProducto vProducto: venta.getVentaProductos()){
                    if(vProducto.getProducto().getClave().compareToIgnoreCase(producto.getClave())==0){
                        producto.setExistencias(producto.getExistencia() - vProducto.getCantidad());
                    }
                }
            }
        }
    }
}
