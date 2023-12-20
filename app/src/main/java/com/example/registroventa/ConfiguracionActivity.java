package com.example.registroventa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.registroventa.models.Configura;
import com.example.registroventa.services.DownloadFile;
import com.example.registroventa.services.SecurePasswordMD5Config;
import com.example.registroventa.xmls.AnalizadorXMLBD;
import com.shawnlin.numberpicker.NumberPicker;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ConfiguracionActivity extends FragmentActivity {

    public static Context ConfiguracionActividad;
    public static String version = "Versionactual(5.10.11).apk";
    private static Configura configuracion;
    String mensajeError = "Version Actual";
    String oldFTP = "";
    Boolean encontroactualizacion, error;
    TextView actualizamensaje;
    ProgressBar loadingactual;
    TextView ftp;// limite;
    EditText TiempoLimpiar;
    Button aceptar;
    Button cancela;
    Button buscara;
    Switch botonesvent;
    Switch clavesprodu;
    Switch existencias;
    Switch modificarprecios;
    Switch seleccionarprecios;
    Switch comentar;
    Switch editarventas;
    int Limite;
    private Intent intent;
    private NumberPicker numberPicker;

    public void cargarConfiguracion() throws Exception {
        AnalizadorXMLBD analizador = new AnalizadorXMLBD(getExternalFilesDir(null));
        Configura config = analizador.procesar();
    }


    void ValidarClaveConfiguracion() {
        final EditText NipText = new EditText(ConfiguracionActivity.this);
        LinearLayout.LayoutParams Style = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        NipText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        NipText.setLayoutParams(Style);
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracionActivity.this);
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
                            InicioActivity.Toast(ConfiguracionActivity.this, "CLAVE CORRECTA");
                        }
                        else {
                            InicioActivity.Toast(ConfiguracionActivity.this, "INCORRECTO");
                            ConfiguracionActivity.this.finish();
                        }
                    }
                });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ConfiguracionActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
        Button negative = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        negative.setTextColor(Color.parseColor("#e18a33"));
    }

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        ConfiguracionActividad = this;
        setContentView(R.layout.activity_configuracion);
        version = "Versionactual(" + this.getTitle().toString().substring(14) + ").apk";
        ftp = (TextView) this.findViewById(R.id.Ftp);
        actualizamensaje = (TextView) this.findViewById(R.id.MensajeActualizacion);
        loadingactual = (ProgressBar) this.findViewById(R.id.LoadingActualizacion);
        botonesvent = (Switch) this.findViewById(R.id.checkBox1);
        clavesprodu = (Switch) this.findViewById(R.id.checkBox2);
        existencias = (Switch) this.findViewById(R.id.checkBox3);
        modificarprecios = (Switch) this.findViewById(R.id.checkBox4);
        seleccionarprecios = (Switch) this.findViewById(R.id.checkBox6);
        comentar = (Switch) this.findViewById(R.id.checkBox5);
        editarventas = (Switch) this.findViewById(R.id.checkBox7);
        TiempoLimpiar = (EditText) this.findViewById(R.id.tiempoLimpiarVentas);
        aceptar = (Button) this.findViewById(R.id.BuscarProducto);
        cancela = (Button) this.findViewById(R.id.Button01);
        buscara = (Button) this.findViewById(R.id.actualizacion);
        //limite = (TextView) this.findViewById(R.id.etiquetalimite);
        actualizamensaje.setVisibility(View.GONE);
        loadingactual.setVisibility(View.GONE);
        configuracion = new Configura();
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
            if (texto != null) configuracion.setFTP(texto);
            texto = lector.readLine();
            if (texto != null) configuracion.setBotones(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) configuracion.setClave(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) configuracion.setExistencia(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) configuracion.setLimite(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) configuracion.setModificar(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) configuracion.setComentar(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) configuracion.setSeleccionar(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) configuracion.setEditarVenta(Integer.parseInt(texto));
            texto = lector.readLine();
            if (texto != null) configuracion.setTiempoLimpiarVenta(Integer.parseInt(texto));


        } catch (Exception ex) {
        } finally {
            try {
                if (flujo != null) flujo.close();
            } catch (IOException e) {
            }
        }

        if (configuracion != null) {
            if (configuracion.getFTP() == null)
                ftp.setText(" ");
            else if (configuracion.getFTP() == "null")
                ftp.setText(" ");
            else if (configuracion.getFTP().length() < 5)
                ftp.setText(" ");
            else
                ftp.setText(configuracion.getFTP());

            Limite = configuracion.getLimite();
            oldFTP = configuracion.getFTP();
        }
        SpannableString content = new SpannableString("Limite de lineas en archivo de venta: " + Limite);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        //limite.setText(content);
        if (configuracion.getBotones() == 8) botonesvent.setChecked(false);
        else if (configuracion.getBotones() == 0) botonesvent.setChecked(true);

        if (configuracion.getClave() == 0) clavesprodu.setChecked(false);
        else if (configuracion.getClave() == 1) clavesprodu.setChecked(true);

        if (configuracion.getExistencia() == 0) existencias.setChecked(false);
        else if (configuracion.getExistencia() == 1) existencias.setChecked(true);

        if (configuracion.getModificar() == 0) modificarprecios.setChecked(false);
        else if (configuracion.getModificar() == 1) modificarprecios.setChecked(true);

        if (configuracion.getSeleccionar() == 0) seleccionarprecios.setChecked(false);
        else if (configuracion.getSeleccionar() == 1) seleccionarprecios.setChecked(true);

        if (configuracion.getEditarVenta() == 0) editarventas.setChecked(false);
        else if (configuracion.getEditarVenta() == 1) editarventas.setChecked(true);



        TiempoLimpiar.setText(" min");
        Selection.setSelection(TiempoLimpiar.getText(), TiempoLimpiar.getText().length());

        TiempoLimpiar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().endsWith(" min")){
                    TiempoLimpiar.setText(" min");
                    Selection.setSelection(TiempoLimpiar.getText(), TiempoLimpiar.getText().length());
                }

            }
        });
        TiempoLimpiar.setText(String.valueOf(configuracion.getTiempoLimpiarVenta())+" min");
        if (configuracion.getComentar() == 0) comentar.setChecked(false);
        else if (configuracion.getComentar() == 1) comentar.setChecked(true);

        numberPicker = findViewById(R.id.number_picker);
        numberPicker.setValue(Limite);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
                setLimite(newVal);
            }
        });

        if(InicioActivity.getListaConfiguracion().get(0).getClaveconfiguracion()!=null)
        if(!InicioActivity.getListaConfiguracion().get(0).getClaveconfiguracion().isEmpty()){
            ValidarClaveConfiguracion();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.configuracion, menu);
        return false;
    }

    private static final Void params = null;

    public void onClickAceptar(View view) throws FileNotFoundException {

        InicioActivity.Toast(ConfiguracionActivity.this,  "Enviando DeviceId...");

        try {
            EnviarClavesPOST envio = new EnviarClavesPOST();
            envio.execute(params);
            InicioActivity.Toast(ConfiguracionActivity.this,  "DeviceId Enviado");
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracionActivity.this);
            builder
                    .setTitle("Envio erroneo")
                    .setMessage("Error: " + e.toString())
                    .setPositiveButton("OK", null)
                    .show();
            AlertDialog dialog = builder.create();

            Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            possitive.setTextColor(Color.parseColor("#e18a33"));
        }

        if (botonesvent.isChecked()) configuracion.setBotones(0);
        else configuracion.setBotones(8);

        if (clavesprodu.isChecked()) configuracion.setClave(1);
        else configuracion.setClave(0);

        if (existencias.isChecked()) configuracion.setExistencia(1);
        else configuracion.setExistencia(0);

        if (modificarprecios.isChecked()) configuracion.setModificar(1);
        else configuracion.setModificar(0);

        if (seleccionarprecios.isChecked()) configuracion.setSeleccionar(1);
        else configuracion.setSeleccionar(0);

        if (editarventas.isChecked()) configuracion.setEditarVenta(1);
        else configuracion.setEditarVenta(0);

        configuracion.setTiempoLimpiarVenta(Integer.parseInt(TiempoLimpiar.getText().toString().replace(" min","")));

        if (comentar.isChecked()) configuracion.setComentar(1);
        else configuracion.setComentar(0);

        configuracion.setFTP(ftp.getText().toString());

        configuracion.setLimite(Limite);

        InicioActivity.ubicacion = ftp.getText().toString();
        OutputStreamWriter escritor = null;
        try {
            File f;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                File root1 = android.os.Environment.getExternalStorageDirectory();
                f = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta/configuracion.txt");
            }else {
                f = new File(getExternalFilesDir(null), "Android/data/com.ventaenruta/configuracion.txt");
            }
            escritor = new OutputStreamWriter(new FileOutputStream(f));
            escritor.write(configuracion.getFTP() + "\n");
            escritor.write(configuracion.getBotones() + "\n");
            escritor.write(configuracion.getClave() + "\n");
            escritor.write(configuracion.getExistencia() + "\n");
            escritor.write(configuracion.getLimite() + "\n");
            escritor.write(configuracion.getModificar() + "\n");
            escritor.write(configuracion.getComentar() + "\n");
            escritor.write(configuracion.getSeleccionar() + "\n");
            escritor.write(configuracion.getEditarVenta() + "\n");
            escritor.write(configuracion.getTiempoLimpiarVenta() + "\n");
        } catch (Exception ex) {
        } finally {
            try {
                if (escritor != null) escritor.close();
            } catch (IOException e) {
            }
        }
        intent = new Intent(this, InicioActivity.class);
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setMessage("La configuracion se ha guardado con exito")
                .setTitle("Configuracion Guardada")
                .setCancelable(false);
        build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (InicioActivity.listaVentas.size() > 0 && ((oldFTP.compareToIgnoreCase(ftp.getText().toString())!=0)&&oldFTP.length()>0)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracionActivity.this);
                    builder
                            .setTitle("FTP CAMBIADO")
                            .setMessage("Se cambio el Negocio FTP\n Las ventas anteriores se eliminaran.")
                            .setIcon(android.R.drawable.ic_delete)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Yes button clicked, do something
                                    InicioActivity.getDB().limpiarVentas();
                                    InicioActivity.getDB().limpiarCuentas();
                                    ListaVentasActivity.limpiarVentas();
                                    InicioActivity.Toast(ConfiguracionActivity.this, "Ventas eliminadas");

                                    InicioActivity.configurando = true;
                                    cancelarOnClick(null);
                                }
                            });
                    //.show();
                    AlertDialog alert = builder.create();
                    alert.show();
                    Button possitive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    possitive.setTextColor(Color.parseColor("#e18a33"));
                } else {
                    InicioActivity.configurando = true;
                    cancelarOnClick(null);
                }
            }
        });
        AlertDialog dialog = build.create();
        dialog.show();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));

    }

    public void onClickCancelar(View view) {
        intent = new Intent(this, InicioActivity.class);
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setMessage("La configuracion no se guardara")
                .setTitle("Configuracion cancelada")
                .setCancelable(false);
        build.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Yes button clicked, do something
                InicioActivity.configurando = true;
                cancelarOnClick(null);
            }
        });
        AlertDialog dialog = build.create();
        dialog.show();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
        Button negative = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        negative.setTextColor(Color.parseColor("#e18a33"));
    }

    public void onClickActualizar(View view) throws Exception {
        encontroactualizacion = false;
        error = false;
        actualizamensaje.setText("Buscando actualizaciones...");
        Actualizar actualizar = new Actualizar();
        Void params = null;
        actualizar.execute(params);
    }

    public void ActualizarVersion() {
        encontroactualizacion = false;
        error = false;
        actualizamensaje.setText("Actualizando aplicacion...");
        Actualizar actualizar = new Actualizar();
        Void params = null;
        actualizar.execute(params);
    }

    public void cancelarOnClick(View view) {
        this.finish();
    }

    public void onClickLimite(View view) {
        DialogFragment dialog = new DialogoLimitar();
        dialog.show(getSupportFragmentManager(), "Limite");
    }



    public void setLimite(int Limite) {
        this.Limite = Limite;

        //SpannableString content = new SpannableString("Limite de lineas en archivo de venta: " + Limite);
        //content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        //limite.setText(content);
    }

    class Actualizar extends AsyncTask<Void, String, Void> {
        String Titulo;

        public Actualizar() {
        }

        protected void onPreExecute() {
            actualizamensaje.setVisibility(View.VISIBLE);
            loadingactual.setVisibility(View.VISIBLE);
            aceptar.setVisibility(View.GONE);
            cancela.setVisibility(View.GONE);
            buscara.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                DownloadFile analizador = new DownloadFile("https://ventaenruta.site/" + version ,getExternalFilesDir(null));
                analizador.procesar();
            } catch (Exception e) {
                mensajeError = e.toString();
                if (mensajeError.equalsIgnoreCase("java.net.ConnectException: failed to connect to /ventaenruta.site (port 80): connect failed: ENETUNREACH (Network is unreachable)")) {
                    mensajeError = "Revise su conexion a internet";
                    Titulo = "Error de conexion";
                    error = true;
                } else if (mensajeError.equalsIgnoreCase("java.io.FileNotFoundException: https://ventaenruta.site/" + version)) {
                    mensajeError = "¿Desea actualizar la aplicacion \"Venta en Ruta\"?";
                    Titulo = "Actualizacion encontrada";
                    encontroactualizacion = true;
                } else {
                    Titulo = "Error de actualizacion";
                    mensajeError = "Revise tener buena conexion a internet. Esta puede ser deficiente.";
                    error = true;
                }
            }
            return null;
        }

        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (encontroactualizacion || error) {
                AlertDialog.Builder build = new AlertDialog.Builder(ConfiguracionActivity.this);
                build.setMessage(mensajeError)
                        .setTitle(Titulo)
                        .setCancelable(false);
                if (error)
                    build.setNegativeButton("OK", null);
                else {
                    build.setPositiveButton("Actualizar ahora", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            version = "RegistroVenta.apk";
                            ActualizarVersion();
                        }
                    });
                    build.setNegativeButton("En otro momento", null);
                }
                AlertDialog dialog = build.create();
                dialog.show();
                Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                possitive.setTextColor(Color.parseColor("#e18a33"));
                Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setTextColor(Color.parseColor("#e18a33"));
            } else if (mensajeError.equalsIgnoreCase("Version Actual")) {
                AlertDialog.Builder build = new AlertDialog.Builder(ConfiguracionActivity.this);
                build.setMessage("Usted cuenta con la ultima version. No es necesario actualizar")
                        .setTitle("No se encontraron actualizaciones")
                        .setCancelable(false);
                build.setPositiveButton("OK", null);
                AlertDialog dialog = build.create();
                dialog.show();
                Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                possitive.setTextColor(Color.parseColor("#e18a33"));
            } else if (version.equalsIgnoreCase("RegistroVenta.apk")) {
                File Apk;
                File directorio;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    File root1 = android.os.Environment.getExternalStorageDirectory();
                    directorio = new File(root1.getAbsolutePath() + "/Android/data/com.ventaenruta");
                }else {
                    directorio = new File(getExternalFilesDir(null), "Android/data/com.ventaenruta");
                }
                if (directorio.exists() == false) {
                    version = "Versionactual(5.10.11).apk";
                    AlertDialog.Builder build = new AlertDialog.Builder(ConfiguracionActivity.this);
                    build.setMessage("No se pudo completar la descarga. Intente de nuevo por favor.")
                            .setTitle("Actualizacion no completada")
                            .setCancelable(false);
                    build.setPositiveButton("OK", null);
                    AlertDialog dialog = build.create();
                    dialog.show();
                    Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    possitive.setTextColor(Color.parseColor("#e18a33"));
                } else {
                    try {
                        Apk = new File(directorio, ConfiguracionActivity.version);
                        Apk.setExecutable(true);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri apkUri = FileProvider.getUriForFile(ConfiguracionActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", Apk);
                            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                            intent.setData(apkUri);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                            return;
                        } else {
                            Uri apkUri = Uri.fromFile(Apk);
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            return;
                        }
                    }catch(Exception e){
                        e.getMessage();
                    }
                }
            }
            actualizamensaje.setVisibility(View.GONE);
            loadingactual.setVisibility(View.GONE);
            aceptar.setVisibility(View.VISIBLE);
            cancela.setVisibility(View.VISIBLE);
            buscara.setVisibility(View.VISIBLE);

        }
    }

    public class EnviarClavesPOST extends AsyncTask<Void, Void, Void> {
        public EnviarClavesPOST() {
        }

        @SuppressLint("MissingPermission")
        protected Void doInBackground(Void... progress) {

            //HttpPost httppost = new HttpPost("http://192.168.0.108/recibirVentas.php");

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(configuracion.getFTP().trim() + "recibirventas.php");

//                InicioActivity.SharedPref.edit().putString("devicekey", "").apply();
                String DeviceKey = InicioActivity.SharedPref.getString("devicekey", "");
                if(DeviceKey.length()<1) {
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                        try {
                            if (ActivityCompat.checkSelfPermission(ConfiguracionActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)return null;
                            DeviceKey = tm.getDeviceId();
                            WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                            WifiInfo info = manager.getConnectionInfo();
                            if(DeviceKey==null) DeviceKey = info.getMacAddress();
                            else if (Integer.parseInt(DeviceKey)<1) DeviceKey = info.getMacAddress();
                            else if (Integer.parseInt(DeviceKey)<1) DeviceKey = null;
                            else if (DeviceKey.equals("020000000000")) DeviceKey = null;
                        } catch (Exception e) {}
                    }
                    if (DeviceKey == null || DeviceKey.length()<1) DeviceKey = (Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID)).toUpperCase();
                    InicioActivity.SharedPref.edit().putString("devicekey", DeviceKey).apply();
                }
                Date fecha = new Date();
                DateFormat.format("EEE, d MMM yyyy HH:mm",fecha).toString();
                String sfecha = DateFormat.format("EEEE d MMM yyyy HH:mm",fecha).toString();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("archivo", String.format("Registro_%s__%s", DeviceKey, sfecha)));
                nameValuePairs.add(new BasicNameValuePair("ventas", DeviceKey));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity ent = response.getEntity();
                BufferedReader lector = new BufferedReader(new InputStreamReader(ent.getContent()));
                lector.close();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                //cargando.setVisibility(View.GONE);
                e.printStackTrace();
            } catch (IOException e) {
                //cargando.setVisibility(View.GONE);
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(Void result) {
            // Toast toast = Toast.makeText(, "Las ventas han sido enviadas al servidor", Toast.LENGTH_LONG);
            //toast.show();
        }
    }

}
