package com.example.registroventa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.registroventa.models.Impresorayencabezado;
import com.example.registroventa.services.SecurePasswordMD5Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ImpresionActivity extends Activity {
    private static final Void params = null;
    public static Context Inicio;
    public String macAdres;
    public String nombrem;
    public String macDefin;
    Impresorayencabezado datos;
    EditText Renglones1;
    EditText Renglones2;
    EditText Renglones3;
    EditText macAdress;
    TextView ActualizaLabel;
    ProgressBar ActualizaPro;
    Button BuscarmacAdress;
    Button Guardarlosdatos;
    Switch MostrarImpresion;
    Switch ImpresionCorte;
    Switch ImpresionDuplicado;
    Button impresionprueba;
    CheckBox ventaCheck, inventarioCheck;

    private Intent intent;
    private VentaActivity actividad;

    void ValidarClaveConfiguracion() {
        final EditText NipText = new EditText(ImpresionActivity.this);
        LinearLayout.LayoutParams Style = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        NipText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        NipText.setLayoutParams(Style);
        AlertDialog.Builder builder = new AlertDialog.Builder(ImpresionActivity.this);
        builder
                .setMessage("Ingresa la clave de administrador para ingresar a la configuración").
                setTitle("Accesos de administrador").setCancelable(false)
                .setView(NipText)
                .setPositiveButton("Ingresar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String ClaveEncriptada = SecurePasswordMD5Config.getSecurePassword(
                                NipText.getText().toString());
                        //PruebasConfig = 31fc5966c65724b995b2d9f84b754925
                        if(ClaveEncriptada.contentEquals(InicioActivity.getListaConfiguracion().get(0).getClaveconfiguracion())) {
                            InicioActivity.Toast(ImpresionActivity.this, "CLAVE CORRECTA");
                        }
                        else {
                            InicioActivity.Toast(ImpresionActivity.this, "INCORRECTO");
                            ImpresionActivity.this.finish();
                        }
                    }
                });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ImpresionActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
        Button negative = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        negative.setTextColor(Color.parseColor("#e18a33"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impresion);
        Inicio = this;
        Renglones1 = (EditText) this.findViewById(R.id.Renglon1);
        Renglones2 = (EditText) this.findViewById(R.id.Renglon2);
        Renglones3 = (EditText) this.findViewById(R.id.Reglon3);
        macAdress = (EditText) this.findViewById(R.id.macAdress);
        ActualizaLabel = (TextView) this.findViewById(R.id.ActualizacionLabel);
        ActualizaPro = (ProgressBar) this.findViewById(R.id.ActualizacionProgress);
        BuscarmacAdress = (Button) this.findViewById(R.id.BusquedamacAdress);
        Guardarlosdatos = (Button) this.findViewById(R.id.GuardarInfo);
        MostrarImpresion = (Switch) this.findViewById(R.id.MostrarImpresionBut);
        impresionprueba = (Button) this.findViewById(R.id.button4);
        ImpresionCorte =  (Switch) this.findViewById(R.id.impresionCorte);
        ImpresionDuplicado =  (Switch) this.findViewById(R.id.impresionDuplicado);
        ventaCheck = this.findViewById(R.id.ventacheck);
        inventarioCheck = this.findViewById(R.id.inventariocheck);

        datos = leerImpresion();


        ImpresionCorte.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ventaCheck.setEnabled(true);
                    inventarioCheck.setEnabled(true);
                    if(!ventaCheck.isChecked()&&!inventarioCheck.isChecked())
                    {
                        ventaCheck.setChecked(true);
                        inventarioCheck.setChecked(true);
                    }
                }else{
                    ventaCheck.setEnabled(false);
                    inventarioCheck.setEnabled(false);
                }
            }
        });
        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!ventaCheck.isChecked()&&!inventarioCheck.isChecked()) ImpresionCorte.setChecked(false);
            }
        };
        ventaCheck.setOnCheckedChangeListener(checkedChangeListener);
        inventarioCheck.setOnCheckedChangeListener(checkedChangeListener);

        Renglones1.setText(datos.getencabezado1());
        Renglones2.setText(datos.getencabezado2());
        Renglones3.setText(datos.getencabezado3());
        macAdress.setText(datos.getnombreImpresora());
        if (datos.getmostrarImpresion() == 0) MostrarImpresion.setChecked(false);
        else if (datos.getmostrarImpresion() == 1) MostrarImpresion.setChecked(true);
        if (datos.getimprimirCorte() == 0) ImpresionCorte.setChecked(false);
        else if (datos.getimprimirCorte() > 0) ImpresionCorte.setChecked(true);
        if(datos.getimprimirCorte() == 1 || datos.getimprimirCorte() == 3) ventaCheck.setChecked(true);
        if(datos.getimprimirCorte() > 1) inventarioCheck.setChecked(true);
        if (datos.getimprimirDuplicado() == 0) ImpresionDuplicado.setChecked(false);
        else if (datos.getimprimirDuplicado() == 1) ImpresionDuplicado.setChecked(true);
//        macAdres = impresoraencontrada();

        if(InicioActivity.getListaConfiguracion().get(0).getClaveconfiguracion()!=null)
            if(!InicioActivity.getListaConfiguracion().get(0).getClaveconfiguracion().isEmpty()){
                ValidarClaveConfiguracion();
            }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        boolean targetAppInstalled = false;
//        PackageManager pm21 = this.getPackageManager();
//        Intent intent2 = new Intent(Intent.ACTION_MAIN, null);
//        intent2.addCategory(Intent.CATEGORY_LAUNCHER);
//        @SuppressLint("WrongConstant") List<ResolveInfo> list = pm21.queryIntentActivities(intent2, PackageManager.PERMISSION_GRANTED);
//        for (ResolveInfo rInfo : list) {
//            String app = rInfo.activityInfo.packageName;
//            if (app.compareToIgnoreCase(BS_PACKAGE) == 0) {
//                targetAppInstalled = true;
//            }
//        }
//        if(targetAppInstalled){
            impresionprueba.setVisibility(View.VISIBLE);
//        }else{
//            impresionprueba.setVisibility(View.INVISIBLE);
//        }

    }

    public void onClickAceptar(View view) {
        datos.setecabezado1(Renglones1.getText().toString());
        datos.setecabezado2(Renglones2.getText().toString());
        datos.setecabezado3(Renglones3.getText().toString());
        datos.setnombreImpresora(macAdress.getText().toString());
        if (macDefin == null) macDefin = "";
        if (macDefin.length() < 2) macDefin = datos.getmacAdress();
        datos.setmacAdress(macDefin);

        if (MostrarImpresion.isChecked()) datos.setmostrarImpresion(1);
        else datos.setmostrarImpresion(0);

        int Corte = 0;
        if (!ImpresionCorte.isChecked()) datos.setimprimirCorte(Corte);
        else{
            if(ventaCheck.isChecked()) Corte = 1;
            if(inventarioCheck.isChecked())Corte = 2;
            if(ventaCheck.isChecked() && inventarioCheck.isChecked()) Corte = 3;
            datos.setimprimirCorte(Corte);
        }

        if (ImpresionDuplicado.isChecked()) datos.setimprimirDuplicado(1);
        else datos.setimprimirDuplicado(0);

        intent = new Intent(this, InicioActivity.class);
        final AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setMessage("La configuracion se ha guardado con exito")
                .setTitle("Configuracion Guardada")
                .setCancelable(false);
        build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Yes button clicked, do something
                //startActivity(intent);
                cancelarOnClick(null);
            }
        });

        AlertDialog.Builder build1 = new AlertDialog.Builder(this);
        build1.setMessage("Seguro desea guardar esta configuracion")
                .setTitle("Guardar configuracion")
                .setCancelable(false);
        build1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Yes button clicked, do something
                OutputStreamWriter escritor = null;
                try {
                    File f;
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                        File root1 = android.os.Environment.getExternalStorageDirectory();
                        f = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta/impresoras.txt");
                    }else {
                        f = new File(getExternalFilesDir(null), "Android/data/com.ventaenruta/impresoras.txt");
                    }
                    escritor = new OutputStreamWriter(new FileOutputStream(f));
                    escritor.write(datos.getencabezado1() + "\n");
                    escritor.write(datos.getencabezado2() + "\n");
                    escritor.write(datos.getencabezado3() + "\n");
                    escritor.write(datos.getmacAdress() + "\n");
                    escritor.write(datos.getmostrarImpresion() + "\n");
                    escritor.write(datos.getnombreImpresora() + "\n");
                    escritor.write(datos.getimprimirCorte() + "\n");
                    escritor.write(datos.getimprimirDuplicado() + "\n");
                    InicioActivity.impresiones = datos;
                } catch (Exception ex) {
                } finally {
                    try {
                        if (escritor != null) escritor.close();
                    } catch (IOException e) {
                    }
                }
                AlertDialog dialog3 = build.create();
                dialog3.show();

                Button possitive = dialog3.getButton(DialogInterface.BUTTON_POSITIVE);
                possitive.setTextColor(Color.parseColor("#e18a33"));
            }
        });
        build1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Yes button clicked, do something
                //startActivity(intent);
                cancelarOnClick(null);
            }
        });

        AlertDialog dialog1 = build1.create();
        dialog1.show();
        Button possitive = dialog1.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
        Button negative = dialog1.getButton(DialogInterface.BUTTON_NEGATIVE);
        negative.setTextColor(Color.parseColor("#e18a33"));

    }

//    public void onClicBuscar(View view) throws InterruptedException {
//        macAdres = impresoraencontrada();
//        ActualizaLabel.setVisibility(View.VISIBLE);
//        ActualizaPro.setVisibility(View.VISIBLE);
//
//        macAdress.setText(nombrem);
//        CargarBusqueda cargar = new CargarBusqueda();
//        cargar.execute(params);
//
//    }

    private static final String BS_PACKAGE = "com.fidelier.posprinterdriver";

    public void onSettingPrinter(View view){
        Boolean targetAppInstalled = false;
        PackageManager pm21 = this.getPackageManager();
        Intent intent2 = new Intent(Intent.ACTION_MAIN, null);
        intent2.addCategory(Intent.CATEGORY_LAUNCHER);
        @SuppressLint("WrongConstant")
        List<ResolveInfo> list = pm21.queryIntentActivities(intent2, PackageManager.PERMISSION_GRANTED);
        for (ResolveInfo rInfo : list) {
            String app = rInfo.activityInfo.packageName;
            if (app.compareToIgnoreCase(BS_PACKAGE) == 0) {
                targetAppInstalled = true;
            }
        }
        if (!targetAppInstalled) {
            showDownloadDialog();
        }else {
            Intent intentPrint = new Intent();

            intentPrint.setAction(Intent.ACTION_SEND);
            intentPrint.putExtra(Intent.EXTRA_TEXT,
                    "$intro$" +
                            "Impresion de prueba"+
                            "$intro$" +
                            "La impresora esta configurada correctamente"+
                            "$intro$$cut$$intro$");
            intentPrint.setPackage("com.fidelier.posprinterdriver");
            intentPrint.setType("text/plain");
            startActivity(intentPrint);
        }
        //https://play.google.com/store/apps/details?id=com.fidelier.posprinterdriver
    }
    public void onPrintTest(View view){
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(ImpresionActivity.this);
        downloadDialog.setTitle("Imprimir prueba");
        downloadDialog.setMessage("La impresion se enviara a tu impresora configurada.");
        downloadDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentPrint = new Intent();

                intentPrint.setAction(Intent.ACTION_SEND);
                intentPrint.putExtra(Intent.EXTRA_TEXT,
                        "$intro$" +
                                "Impresion de prueba"+
                                "$intro$" +
                                "La impresora esta configurada correctamente"+
                                "$intro$$cut$$intro$");
                intentPrint.setPackage("com.fidelier.posprinterdriver");
                intentPrint.setType("text/plain");
                startActivity(intentPrint);
            }
        });
        AlertDialog dialog = downloadDialog.create();
        dialog.show();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));

    }
    private void showDownloadDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(ImpresionActivity.this);
        downloadDialog.setTitle("Instala y configura POS PRINTER");
        downloadDialog.setMessage("Se requiere de esta aplicacion para la impresion. Instalala y configura tu impresora.");
        downloadDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String packageName = BS_PACKAGE;
                Uri uri = Uri.parse("market://details?id=" + packageName);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    ImpresionActivity.this.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                    // Hmm, market is not installed
                    Log.w(TAG, "Google Play is not installed; cannot install " + packageName);
                }
            }
        });
        downloadDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        try {
            AlertDialog dialog = downloadDialog.create();
            dialog.show();
            Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            possitive.setTextColor(Color.parseColor("#e18a33"));
            Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negative.setTextColor(Color.parseColor("#e18a33"));

        }catch(Exception e){
            e.getMessage();
        }
    }
//    public String impresoraencontrada() {
//
//        try {
//            BluetoothDiscoverer.findPrinters(this, new DiscoveryHandler() {
//                public void discoveryFinished() {
//                    //Discovery is done
//                    macAdress.setText(nombrem);
//                    //macAdres = null;
//                }
//
//                public void discoveryError(String message) {
//                    //Error during discovery
//                    macAdres = null;
//                    nombrem = null;
//                }
//
//                public void foundPrinter(DiscoveredPrinter printer) {
//                    // TODO Auto-generated method stub
//                    DiscoveredPrinterBluetooth nombre = (DiscoveredPrinterBluetooth) printer;
//                    nombrem = nombre.friendlyName;
//                    macAdres = printer.address;
//                }
//            });
//        } catch (ConnectionException e) {
//            macAdres = null;
//            nombrem = null;
//        }
//        return macAdres;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.configuracion, menu);
        return false;
    }

    public void cancelarOnClick(View view) {
        this.finish();
    }

    public Impresorayencabezado leerImpresion() {
        InputStreamReader flujo = null;
        BufferedReader lector = null;
        try {
            File root1 = android.os.Environment.getExternalStorageDirectory();
            File f = new File(root1.getAbsolutePath(), "/Android/data/com.ventaenruta/impresoras.txt");
            flujo = new InputStreamReader(new FileInputStream(f));
            lector = new BufferedReader(flujo);
            String texto = lector.readLine();
            if (texto != null) InicioActivity.impresiones.setecabezado1(texto);
            texto = lector.readLine();
            if (texto != null) InicioActivity.impresiones.setecabezado2(texto);
            texto = lector.readLine();
            if (texto != null) InicioActivity.impresiones.setecabezado3(texto);
            texto = lector.readLine();
            if (texto != null) InicioActivity.impresiones.setmacAdress(texto);
            texto = lector.readLine();
            if (texto != null)InicioActivity.impresiones.setmostrarImpresion(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) InicioActivity.impresiones.setnombreImpresora(texto);
            texto = lector.readLine();
            if (texto != null)InicioActivity.impresiones.setimprimirCorte(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null)InicioActivity.impresiones.setimprimirDuplicado(Integer.parseInt(texto));
        } catch (Exception ex) {
        } finally {
            try {
                if (flujo != null) flujo.close();
            } catch (IOException e) {
            }
        }

        return InicioActivity.impresiones;
    }
//
//
//    class CargarBusqueda extends AsyncTask<Void, String, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//            ActualizaLabel.setVisibility(View.VISIBLE);
//            ActualizaPro.setVisibility(View.VISIBLE);
//            publishProgress("Buscando impresoras...");
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            macAdres = impresoraencontrada();
//
//            return null;
//        }
//
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            ActualizaPro.setVisibility(View.GONE);
//            if (macAdres == null) {
//                //macAdres = " ";
//                //publishProgress("No se encontro impresora");
//            } else {
//                macAdress.setText(nombrem);
//                //publishProgress("Impresora encontrada");
//                macDefin = macAdres;
//                macAdres = null;
//                nombrem = null;
//            }
//            //macAdress.setText(macAdres);
//            //Renglones3.setText(macAdres);
//        }
//
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
//            String progreso = values[0];
//            ActualizaLabel.setText(progreso);
//        }
//    }
}
