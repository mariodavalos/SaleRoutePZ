package com.example.registroventa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registroventa.models.Cliente;
import com.example.registroventa.models.Configura;
import com.example.registroventa.models.Cuentas;
import com.example.registroventa.models.Metodos;
import com.example.registroventa.models.Venta;
import com.example.registroventa.models.VentaProducto;
import com.example.registroventa.services.IntentIntegrator;
import com.example.registroventa.services.IntentResult;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.discovery.BluetoothDiscoverer;
import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.printer.discovery.DiscoveryHandler;

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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

public class VentaActivity extends FragmentActivity {

    //region Declaraciones
    public static String entrada = null;
    public static String salida = null;
    public static boolean nuevoCliente = false;
    public static String clienteseleccionado = "";
    public static Cliente clienteseleccionadoCliente;
    public static VentaProducto errorpro;
    public static Configura config = new Configura();
    public static Boolean addormod = true;

    public Venta venta = null;
    public Boolean cambio = false;
    public String producto = null;
    public int cuentate = -1;
    public int modificacion = 0;
    Button myButton1, myButton2;
    Button Vista,Iniciar,Terminar;
    DialogFragment dialog = new DialogoBuscarCliente();
    DialogFragment dialog2 = new DialogoCliente();
    int IdVenta = 0;
    TextView ventaTextView5;
    public static Context esta;
    //private static ListView my_listview;
    List<String> impresorasencontradas;
    String macAddress = null;
    String macPrivide = null;
    private Spinner clientes;
    private EditText tfecha;
    private ImageButton agregapv;
    private Button CuentasButton;
    private FloatingActionButton add;
    private TextView totalVenta;
    private FrameLayout Editarlayout;

    private ListView listaCuentasVenta = null;
    private List<String> arregloCuentasVentas;
    private ArrayAdapter<String> adapterCuentasVentas;

    private ListView listaProductosVenta = null;
    private List<String> arregloProductosVentas;
    private ArrayAdapter<String> adapterProductosVentas;

    private ListView listaAbonosVenta = null;
    private List<String> arregloAbonosVentas;
    private ArrayAdapter<String> adapterAbonosVentas;

    private LinearLayout OcultarCuentas = null;
    private EditText AbonoText;
    private FrameLayout Agregar;
    EditText nuevo;
    Boolean cxcadd = false;

    private Button Aceptar;
    private Button Reimprimir;
    private Button Cancelar;

    private ImageButton AbonosButton;
//endregion
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            //Thread.sleep(100);
            setContentView(R.layout.activity_venta);
            //Thread.sleep(100);
            ventaActivity = this;
            Date fecha = ListaVentasActivity.getVenta().getFecha();
            String cad = String.format("%02d/%02d/%02d %02d:%02d",
                    fecha.getDate(), fecha.getMonth() + 1,
                    fecha.getYear() + 1900, fecha.getHours(),
                    fecha.getMinutes());
            //Thread.sleep(100);
            clientes = (Spinner) this.findViewById(R.id.listaClientes);
            cargarClientes();
            for (int i = 0; i < InicioActivity.getListaClientes().size(); i++) {
                Cliente cliente = InicioActivity.getListaClientes().get(i);
                if (cliente.equals(ListaVentasActivity.getVenta().getCliente()))
                    clientes.setSelection(i);
            }
            tfecha = (EditText) this.findViewById(R.id.fecha);
            totalVenta = (TextView) this.findViewById(R.id.totalVenta);
            agregapv = (ImageButton) this.findViewById(R.id.Agregaproducto);
            CuentasButton = (Button) this.findViewById(R.id.CuentaPagar);
            Editarlayout = (FrameLayout) this.findViewById(R.id.Editar);
            ventaTextView5 = (TextView) this.findViewById(R.id.textView5);
            add = (FloatingActionButton) this.findViewById(R.id.multiple_actions);

            if (!InicioActivity.getListaConfiguracion().get(0).getVercxc())
                CuentasButton.setVisibility(View.GONE);

            Aceptar = (Button) this.findViewById(R.id.Aceptarventa);
            Reimprimir = (Button) this.findViewById(R.id.Reimprimir);
            Cancelar = (Button) this.findViewById(R.id.CanccelarVenta);

            AbonosButton = (ImageButton) this.findViewById(R.id.ButtonAbonos);
            AbonosButton.setVisibility(View.INVISIBLE);

            OcultarCuentas = (LinearLayout) this.findViewById(R.id.OcultoCuentas);
            AbonoText = (EditText) this.findViewById(R.id.CuentaAbono);

            tfecha.setEnabled(false);
            tfecha.setText(cad);

            listaProductosVenta = (ListView) this.findViewById(R.id.listaProductos);
            arregloProductosVentas = new ArrayList<String>();
            adapterProductosVentas = new ArrayAdapter<String>(this,
                    R.layout.lista_item_layout2, arregloProductosVentas);
            listaProductosVenta.setAdapter(adapterProductosVentas);

            listaCuentasVenta = (ListView) this.findViewById(R.id.listaCuentas);
            arregloCuentasVentas = new ArrayList<String>();
            adapterCuentasVentas = new ArrayAdapter<String>(this,
                    R.layout.lista_item_layout2, arregloCuentasVentas);
            listaCuentasVenta.setAdapter(adapterCuentasVentas);

            listaAbonosVenta = (ListView) this.findViewById(R.id.AbonosLista);
            arregloAbonosVentas = new ArrayList<String>();
            adapterAbonosVentas = new ArrayAdapter<String>(this,
                    R.layout.lista_item_layout2, arregloAbonosVentas);
            listaAbonosVenta.setAdapter(adapterAbonosVentas);

            listaCuentasVenta.setVisibility(View.INVISIBLE);
            OcultarCuentas.setVisibility(View.GONE);

            Agregar = (FrameLayout) findViewById(R.id.Agregar);
            nuevo = new EditText(getApplicationContext());

            venta = ListaVentasActivity.getVenta();
            for (VentaProducto vp : venta.getVentaProductos()) {
                adapterProductosVentas.add(vp.toString());
            }
            double total = 0;
            for (VentaProducto pv : venta.getVentaProductos()) total += pv.getTotal();
            totalVenta.setText(NumberFormat.getCurrencyInstance(Locale.US).format(total));

            int Mostrar = Configuracionleer().getBotones();
            myButton1 = (Button) findViewById(R.id.Iniciaventa);
            myButton1.setVisibility(Mostrar);
            myButton2 = (Button) findViewById(R.id.Terminaventa);
            myButton2.setVisibility(Mostrar);

            myButton1.setClickable(true);
            myButton2.setClickable(false);

            //listaProductosVenta = (ListView)findViewById(R.id.listaVentas);
            //adapter.clear();
            //my_listview.
            APagar = 0.0;
            adapterProductosVentas = new ArrayAdapter<String>(this, R.layout.lista_item_layout2, arregloProductosVentas);
            listaProductosVenta.setAdapter(adapterProductosVentas);
            listaProductosVenta.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if(Configuracionleer().getEditarVenta()==0 && !venta.isNueva())
                        InicioActivity.Toast(VentaActivity.this,"No puedes editar ventas");
                    else if (addormod) {
                        addormod = false;
                        String item = ((TextView) view).getText().toString();
                        for (String productos : arregloProductosVentas)
                            if (item.equals(productos.toString())) {
                                producto = productos;
                                modificacion = position;
                                cambio = true;
                                break;
                            }
                        if (producto != null) {
                            DialogFragment dialog = new DialogoAgregarProducto();
                            dialog.show(getSupportFragmentManager(), "Agregar Producto");

                        }
                    }

                }
            });

            adapterAbonosVentas = new ArrayAdapter<String>(this, R.layout.lista_item_layout2, arregloAbonosVentas);
            listaAbonosVenta.setAdapter(adapterAbonosVentas);

            adapterCuentasVentas = new ArrayAdapter<String>(this, R.layout.lista_item_layout2, arregloCuentasVentas);
            listaCuentasVenta.setAdapter(adapterCuentasVentas);

                listaCuentasVenta.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (InicioActivity.getListaConfiguracion().get(0).getCapturapagos()) {
                            CuentasClick(parent, view, position, id);
                            return;
                        }
                        InicioActivity.Toast(VentaActivity.this, "Tu no puedes registrar pagos. Comuniquese con el administrador.");
                    }
                });
//                listaAbonosVenta.setOnItemClickListener(new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        CuentasClick(parent, view, position, id);
//                    }
//                });

            AbonoText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        try {
                            if (AbonoText.getText().toString().compareTo(nuevo.getText().toString()) != 0) {
                                totalVenta.setText(NumberFormat.getCurrencyInstance(Locale.US).format(((totalaPagar - SumaARestar) + APagar) + Double.parseDouble(AbonoText.getText().toString())));
                            }
                            cxcadd = true;
                        } catch (Exception e) {

                        }
                        nuevo.setText(AbonoText.getText());
                        /*
                        nuevo.setLayoutParams(AbonoText.getLayoutParams());
                        nuevo.setPadding(0, Editarlayout.getPaddingTop(), 0, 0);
                        nuevo.setTextColor(Color.BLACK);
                        nuevo.setFocusable(false);
                        nuevo.setClickable(false);
                        nuevo.setEms(10);
                        nuevo.setId(cuentate);
                        nuevo.bringToFront();
                        Agregar.addView(nuevo);*/
                    } catch (Exception e) {
                        InicioActivity.Toast(VentaActivity.this, "Error:" + e.toString());
                    }
                }
            });

        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage("Error: "+ e.getMessage()+ ". Comuniquese con del administrador.")
                    .setTitle("Error")
                    .setPositiveButton("Ok", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            possitive.setTextColor(Color.parseColor("#e18a33"));
        }
        Buttons();
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
    public void onBackPressed() {
        super.onBackPressed();
        cancelarconfirmaOnClick(null);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            cancelarconfirmaOnClick(null);
        }
        return super.onKeyDown(keyCode, event);
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
    public Boolean CuentasPay = false;
    private Cliente seleccionarcliente;
    private Double totalaPagar = 0.0;
    private Double APagar;
    Double SumaARestar = 0.0;
    private List<Cuentas> cuentas = new ArrayList<Cuentas>();
    public void cuentasPagar(View view) {
        try {
            AbonoText.setVisibility(View.INVISIBLE);
            if (!CuentasPay) {
                ventaTextView5.setText("Total de abonos: ");
                //AbonosButton.setVisibility(View.VISIBLE);
                listaProductosVenta.setVisibility(View.INVISIBLE);
                Aceptar.setVisibility(View.GONE);
                Cancelar.setVisibility(View.GONE);
                Reimprimir.setVisibility(View.GONE);
//            add.setVisibility(View.INVISIBLE);

                //if (seleccionarcliente != null)
                //if (!(seleccionarcliente.toString().compareTo(clientes.getSelectedItem().toString()) == 0))
                Agregar.removeAllViews();
                SumaARestar = 0.0;
                APagar = 0.0;
                Agregar.setVisibility(View.VISIBLE);
                seleccionarcliente = new Cliente();
                adapterCuentasVentas.clear();
                for (Cliente seleccionar : InicioActivity.getListaClientes()) {
                    if (seleccionar.toString().indexOf(clientes.getSelectedItem().toString()) >= 0) {
                        seleccionarcliente = seleccionar;
                    }
                }
                List<Cuentas> cuentasComparar = InicioActivity.getDB().ObtenerCuentas();
                for (Cuentas cu : InicioActivity.getListaCuentas()) {
                    if (cu.getCliente() != null & seleccionarcliente.getClave() != null) {
                        if (cu.getCliente().toString().compareTo(seleccionarcliente.getClave().toString()) == 0) {
                            adapterCuentasVentas.add(cu.toString());
                            int position = adapterCuentasVentas.getPosition(cu.toString());
                            try {
                                //region Crear Input
                                if (findViewById(position) != null)
                                    nuevo = (EditText) findViewById(position);
                                else
                                    nuevo = new EditText(VentaActivity.this);
                                //endregion

                                //TextView vieew = (TextView) listaCuentasVenta.getChildAt(position);
                                try {
                                    for (Cuentas compareCXC : cuentasComparar) {
                                        if (compareCXC.toString().indexOf(cu.toString()) >= 0) {
                                            nuevo.setText(compareCXC.toString().replace(cu.toString(), ""));
                                            if (nuevo.getText().length() < 1) nuevo.setText("No");
                                            APagar += Double.parseDouble(nuevo.getText().toString().trim());
                                        }
                                    }
                                } catch (Exception ex) {
                                    Log.v("Exception", ex.getLocalizedMessage());
                                }
                                //region Colocar Input
                                if (!(nuevo.getText().toString().indexOf("No") >= 0)) {
                                    nuevo.setLayoutParams(AbonoText.getLayoutParams());
//                                    Point size = new Point();
//                                    VentaActivity.this.getWindowManager().getDefaultDisplay().getSize(size);
                                    nuevo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f);
                                    nuevo.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                                    nuevo.setPadding(0, (64) * position, 30, 0);
                                    nuevo.setBackgroundColor(Color.TRANSPARENT);
                                    nuevo.setTextColor(Color.parseColor("#FF9800"));
                                    nuevo.setFocusable(false);
                                    nuevo.setClickable(false);
                                    nuevo.setEms(10);
                                    nuevo.setId(position);
                                    Agregar.addView(nuevo);
                                } else {
                                    adapterCuentasVentas.remove(cu.toString());
                                }
                            } catch (Exception e) {
                                InicioActivity.Toast(VentaActivity.this, "Error:" + e.toString());
                            }
                        }
                    }
                }
                OcultarCuentas.setVisibility(View.VISIBLE);
                listaCuentasVenta.setVisibility(View.VISIBLE);
                CuentasButton.setBackgroundResource(R.drawable.comprar);
                CuentasPay = true;
                listaCuentasVenta.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 75 * adapterCuentasVentas.getCount(), 8));

                if (adapterCuentasVentas.getCount() < 1) {
                    InicioActivity.Toast(VentaActivity.this,
                            "El cliente no tiene cuentas por pagar.\nAgrega un nuevo abono.");
                    totalVenta.setText(NumberFormat.getCurrencyInstance(Locale.US).format(0.0));
                } else {
                    //totalaPagar = InicioActivity.getDB().ObtenerTotalAbonos();
                String total = NumberFormat.getCurrencyInstance(Locale.US).format(totalaPagar);
                totalVenta.setText(total);
                }
            } else if (InicioActivity.getListaConfiguracion().get(0).getCapturapagos() && cxcadd && !mostrarAbonos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setMessage("Se han agregado pagos a sus cuentas, ¿Desea guardarlos?")
                        .setTitle("Guardar pagos")
                        .setIcon(android.R.drawable.ic_menu_save)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    ///////////////////
                                    SaveVentas();
//                                List<Cuentas> nuevas = InicioActivity.getListaCuentas();
//                                nuevas.removeAll(cuentas);
//                                nuevas.addAll(cuentas);
//                                cuentas = nuevas;
                                    //  InicioActivity.setListaCuentas(cuentas);
                                    InicioActivity.getDB().agregarCuentas(cuentas);
                                    cxcadd = false;
                                    ///////////////////
                                    ///////////////////
                                    AlertDialog.Builder builder = new AlertDialog.Builder(VentaActivity.this);
                                    builder
                                            .setTitle("Cuentas guardadas")
                                            .setMessage("Se han guardado los pagos y abonos")
                                            .setIcon(android.R.drawable.stat_sys_upload_done)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    imprimirCXC();
                                                    Agregar.removeAllViews();
                                                    cuentas.clear();
                                                }
                                            });
                                    AlertDialog dialog2 = builder.create();
                                    dialog2.show();
                                    Button possitive = dialog2.getButton(DialogInterface.BUTTON_POSITIVE);
                                    possitive.setTextColor(Color.parseColor("#e18a33"));


                                } catch (Exception e) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(VentaActivity.this);
                                    builder
                                            .setTitle("Envio erroneo")
                                            .setMessage("Error: " + e.toString())
                                            .setPositiveButton("OK", null);
                                    AlertDialog dialog2 = builder.create();
                                    dialog2.show();
                                    Button possitive = dialog2.getButton(DialogInterface.BUTTON_POSITIVE);
                                    possitive.setTextColor(Color.parseColor("#e18a33"));
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SaveVentas();
                                InicioActivity.Toast(VentaActivity.this,
                                        "Continuar con la venta actual");
                                cxcadd = false;
                                cuentas.clear();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                possitive.setTextColor(Color.parseColor("#e18a33"));
                Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setTextColor(Color.parseColor("#e18a33"));
            } else if (!cxcadd) {
                SaveVentas();
                cuentas.clear();
            }
        } catch (Exception e) {
            InicioActivity.Toast(VentaActivity.this, "Error:" + e.toString());
            e.printStackTrace();
        }
    }
    void CuentasClick(AdapterView<?> parent, View view, int position, long id) {
        String item = ((TextView) view).getText().toString();
        APagar = 0.0;
        int posicion = 0;
        if (nuevo != null) nuevo.setVisibility(View.VISIBLE);
        for (String cuenta : arregloCuentasVentas) {
            if (item.equals(cuenta.toString())) cuentate = position;
            if (Agregar.getChildAt(posicion) != null) try {
                APagar += Double.parseDouble((((EditText) Agregar.getChildAt(posicion)).getText()).toString());
            } catch (Exception e) {
            }
            posicion++;
            if (Agregar.getChildAt(cuentate) != null)
                nuevo = (EditText) Agregar.getChildAt(cuentate);
            else
                nuevo = new EditText(getApplicationContext());
            nuevo.setVisibility(View.VISIBLE);
        }

        if (cuentate >= 0) try {
            if (Agregar.getChildAt(cuentate) != null)
                nuevo = (EditText) Agregar.getChildAt(cuentate);
            else
                nuevo = new EditText(getApplicationContext());
            nuevo.setVisibility(View.INVISIBLE);
            AbonoText.setVisibility(View.VISIBLE);

            Editarlayout.setPadding(0, (((TextView) view).getHeight() + 1) * position, 0, 0);
            Editarlayout.setVisibility(View.VISIBLE);
            Editarlayout.bringToFront();
            AbonoText.requestFocus();
            InputMethodManager keyboard = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(AbonoText, 0);
            AbonoText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        nuevo.setVisibility(View.VISIBLE);
                        Editarlayout.setVisibility(View.INVISIBLE);
                        InputMethodManager keyboard = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        keyboard.hideSoftInputFromWindow(AbonoText.getWindowToken(), 0);

                        return true;
                    }
                    return false;
                }
            });
            totalVenta.setText(NumberFormat.getCurrencyInstance(Locale.US).format(((totalaPagar - SumaARestar) + APagar)));

            if (findViewById(cuentate) != null) {
                try {
                    APagar -= Double.parseDouble(nuevo.getText().toString());
                    //  totalVenta.setText(NumberFormat.getCurrencyInstance(Locale.US).format((totalaPagar-APagar)));
                } catch (Exception e) {
                }
                AbonoText.setText(nuevo.getText());
            } else
                AbonoText.setText("");

            AbonoText.isSelected();
            AbonoText.isFocused();
            AbonoText.setSelection(AbonoText.getText().length());
        } catch (Exception e) {

        }
    }
    int abonos=0;
    public void SaveVentas(){
        Aceptar.setVisibility(View.VISIBLE);
        Cancelar.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        listaProductosVenta.setVisibility(View.VISIBLE);
        AbonosButton.setVisibility(View.INVISIBLE);
        CuentasButton.hasFocus();
        InputMethodManager imput = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imput.hideSoftInputFromWindow(CuentasButton.getWindowToken(), 0);

        ventaTextView5.setText("Total de la venta: ");
        Double total = 0.0;
        for (VentaProducto pv : venta.getVentaProductos()) total += pv.getTotal();
            totalVenta.setText(NumberFormat.getCurrencyInstance(Locale.US).format(total));
//        totalVenta.setText(NumberFormat.getCurrencyInstance(Locale.US).format(0.0));
        listaCuentasVenta.setVisibility(View.INVISIBLE);
        listaAbonosVenta.setVisibility(View.INVISIBLE);
        OcultarCuentas.setVisibility(View.INVISIBLE);
        CuentasButton.setBackgroundResource(R.drawable.pagar);
        Agregar.setVisibility(View.INVISIBLE);
        CuentasPay = false;

        if (Agregar.getChildCount() > 0 || APagar > 0) {
            int posicion = 0;
            String DeviceKey = InicioActivity.SharedPref.getString("devicekey", "");
            if(DeviceKey.length()<1) {
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    try {
                        if (ActivityCompat.checkSelfPermission(VentaActivity.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                        DeviceKey = tm.getDeviceId();
                        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo info = manager.getConnectionInfo();
                        if(DeviceKey==null) DeviceKey = info.getMacAddress();
                        else if (Integer.parseInt(DeviceKey)<1) DeviceKey = info.getMacAddress();
                        else if (Integer.parseInt(DeviceKey)<1) DeviceKey = null;
                        else if (DeviceKey.equals("020000000000")) DeviceKey = null;
                        }
                    } catch (Exception e) {}
                }
                if (DeviceKey == null || DeviceKey.length()<1) DeviceKey = (Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                InicioActivity.SharedPref.edit().putString("devicekey", DeviceKey).apply();
            }

            Date fecha = new Date();
            String sfecha = String.format("%d_%d_%d_%d_%d", fecha.getDate(), fecha.getMonth() + 1, fecha.getYear() + 1900, fecha.getHours(), fecha.getMinutes());

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            String nombreArchivo = String.format("cxcs_%s_%s_%s.sql", DeviceKey,venta.getVendedor().getClave(),sfecha);

            for (String cuenta : arregloCuentasVentas) {

                if(findViewById(posicion)!=null) {
                    Cuentas cuentaRow = new Cuentas();
                    Cuentas cuentaR = null;
                    for (Cuentas cu : InicioActivity.getListaCuentas())
                        if (cu.toString().compareTo(cuenta) == 0) {
                            cuentaR = cu;
                            break;
                        }
                    Double cantidadPagada = 0.0;
                    try {
                        cantidadPagada = Double.parseDouble((((EditText) findViewById(posicion)).getText()).toString());
                    } catch (Exception e) {
                    }
                    if(cuentaR == null){
                        cuentaR = new Cuentas();
                        cuentaR.setnumero(0);
                        cuentaR.setId(String.valueOf(abonos));
                        abonos++;
                        cuentaR.setTipo("A");
                    }
                    cuentaRow.setnumero(cuentaR.getnumero());
                    cuentaRow.setId(cuentaR.getId());
                    cuentaRow.setTipo(cuentaR.getTipo());
                    cuentaRow.setDocumento(nombreArchivo);
                    cuentaRow.setFecha(sfecha);
                    cuentaRow.setCliente(seleccionarcliente.getClave());
                    cuentaRow.setSaldo(cantidadPagada);
                    cuentaRow.setVencimiento(cuenta+" "+cantidadPagada);
                    cuentas.add(cuentaRow);
                }
                posicion++;
            }
        }
    }
    private  Boolean mostrarAbonos = false;
    public void mostrarListaAbonos(View view){
        adapterAbonosVentas.clear();
        if(mostrarAbonos) {
            AbonosButton.setBackgroundResource(R.drawable.despliegue);
            mostrarAbonos = false;
            listaAbonosVenta.setVisibility(View.INVISIBLE);
        }
        else{
            AbonosButton.setBackgroundResource(R.drawable.desplieguenver);
            mostrarAbonos = true;
            listaAbonosVenta.setVisibility(View.VISIBLE);
            for (String abono : InicioActivity.getDB().ObtenerListaCuentas())
                adapterAbonosVentas.add(abono);
        }

    }
    private static final Void params = null;
    private void cargarClientes() {
        ((DialogoBuscarCliente) dialog).actualizarArreglosClientes();
        String[] arregloClientes = new String[InicioActivity.getListaClientes().size()];
        for (int i = 0; i < InicioActivity.getListaClientes().size(); i++)
            arregloClientes[i] = InicioActivity.getListaClientes().get(i).toString();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arregloClientes);
        clientes.setAdapter(adapter);
    }

    public void agregarProductoOnClick(View view) {
        if(addormod) {
            addormod = false;
            Configuracionleer();
            //714
            agregapv.setEnabled(false);
            cambio = false;
            DialogFragment dialog = new DialogoAgregarProducto();
            dialog.show(getSupportFragmentManager(), "Agregar Producto");
            agregapv.setEnabled(true);
        }
    }
    String AbonoID = "";
    public void agregarAbonoOnClick(View view) {
        final EditText NotaText = new EditText(this);
        LinearLayout.LayoutParams Style = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        if (!(InicioActivity.getListaConfiguracion().get(0).getCapturapagos())){
            InicioActivity.Toast(VentaActivity.this, "Tu no puedes registrar abonos. Comuniquese con el administrador.");
            return;
        }
        NotaText.setLayoutParams(Style);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setTitle("Concepto de abono")
                .setView(NotaText)
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                        cxcadd = true;
                        AbonoID = NotaText.getText().toString();
                        adapterCuentasVentas.add(NotaText.getText().toString());
                        listaCuentasVenta.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 75 * adapterCuentasVentas.getCount(), 8));
                        nuevo = new EditText(VentaActivity.this);
                        nuevo.setText("0.0");
                        nuevo.setLayoutParams(AbonoText.getLayoutParams());

                        Point size = new Point();
                        VentaActivity.this.getWindowManager().getDefaultDisplay().getSize(size);
                        nuevo.setPadding(0, (64) * (adapterCuentasVentas.getCount()-1), 20, 0);
                        nuevo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f);
                        nuevo.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        //nuevo.setPadding(0, (84) * position, 30, 0);
                        //nuevo.setPadding(0, (AbonoText.getHeight()+1) * position, 30, 0);
                        nuevo.setBackgroundColor(Color.TRANSPARENT);
                        nuevo.setTextColor(Color.parseColor("#FF9800"));
                        //nuevo.setPadding(0, AbonoText.getPaddingTop(), 0, 0);
                        //nuevo.setBackgroundColor(Color.TRANSPARENT);
                        //nuevo.setTextColor(Color.BLACK);
                        nuevo.setFocusable(false);
                        nuevo.setClickable(false);
                        nuevo.setEms(10);
                        nuevo.setId(View.generateViewId());
                        Agregar.addView(nuevo);
                        }catch(Exception e){
                            InicioActivity.Toast(VentaActivity.this,"Error: "+ e.toString());
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
    }
    public void buscarClienteOnClick(View view) {
        dialog.show(getSupportFragmentManager(), "Buscar Cliente");
    }

    public void visualizarNuevoClienteOnClick() {
        String baseClave = (InicioActivity.getListaConfiguracion()).get(0).getSerieclientes();
        baseClave = baseClave==null? "":baseClave;
        int baseSize = InicioActivity.getListaClientes().size() + 1;
        String finalClave;
        int diferenciador = 0;
        boolean exists;
        do {
            finalClave = String.format("%s%s", baseClave, String.format("%04d", baseSize + diferenciador));
            String currentClave = finalClave;
            exists = InicioActivity.getListaClientes().stream().anyMatch(c -> c.getClave().equals(currentClave));
            diferenciador++;
        } while (exists);
        clienteseleccionado = finalClave;
        clienteseleccionadoCliente = new Cliente();
        clienteseleccionadoCliente.setNumPrecio(1);
        clienteseleccionadoCliente.setClave(finalClave);
        nuevoCliente = true;
        dialog2.show(getSupportFragmentManager(), "Nuevo Cliente");
    }
    public void visualizarClienteOnClick(View view) {

        clienteseleccionado = clientes.getSelectedItem().toString();
        int[] arreglofinal = ((DialogoBuscarCliente) dialog).clientesEncontradosID();
        if(arreglofinal[clientes.getSelectedItemPosition()] == 0)
            clienteseleccionadoCliente = InicioActivity.getListaClientes().get(clientes.getSelectedItemPosition());
        else
            clienteseleccionadoCliente = InicioActivity.getListaClientes().get(arreglofinal[clientes.getSelectedItemPosition()]);
        nuevoCliente = false;
        dialog2.show(getSupportFragmentManager(), "Buscar Cliente");
    }

    public void EncontroClientes() {
        String[] arreglofinal = ((DialogoBuscarCliente) dialog).clientesEncontrados();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arreglofinal);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        clientes.setAdapter(adapter);
    }

    @SuppressWarnings("deprecation")
    public void entradaVentaOnClick(View view) {
        try {
            myButton1.setClickable(false);
            myButton2.setClickable(true);
            Date fecha = ListaVentasActivity.getVenta().getFecha();
            entrada = String.format("%02d/%02d/%02d %02d:%02d",
                    fecha.getDate(), fecha.getMonth() + 1,
                    fecha.getYear() + 1900, fecha.getHours(),
                    fecha.getMinutes());
        } catch (Exception e) {
            System.out.println(e);
        }
        InicioActivity.Toast(VentaActivity.this,
                "Entrada: " + entrada);
    }

    @SuppressWarnings("deprecation")
    public void salidaVentaOnClick(View view) {
        try {
            Date fecha = ListaVentasActivity.getVenta().getFecha();
            salida = String.format("%02d/%02d/%02d %02d:%02d",
                    fecha.getDate(), fecha.getMonth() + 1,
                    fecha.getYear() + 1900, fecha.getHours(),
                    fecha.getMinutes());
        } catch (Exception e) {
            System.out.println(e);
        }
        InicioActivity.Toast(VentaActivity.this,
                "Salida: " + salida);
    }

    public void agregarProductoVenta(VentaProducto ventaProducto) {
        venta.getVentaProductos().add(ventaProducto);
        adapterProductosVentas.add(ventaProducto.toString());
        listaProductosVenta.refreshDrawableState();

        double total = 0;
        for (VentaProducto pv : venta.getVentaProductos()) total += pv.getTotal();
        totalVenta.setText(NumberFormat.getCurrencyInstance(Locale.US).format(total));
    }

    public void modificarProductoVenta(VentaProducto ventaProducto) {
        arregloProductosVentas.remove(modificacion);
        if (ventaProducto != null)
            arregloProductosVentas.add(modificacion, ventaProducto.toString());
        venta.getVentaProductos().remove(modificacion);
        if (ventaProducto != null)
            venta.getVentaProductos().add(modificacion, ventaProducto);

        adapterProductosVentas.clear();
        for (VentaProducto vp : venta.getVentaProductos())
            adapterProductosVentas.add(vp.toString());
        listaProductosVenta.refreshDrawableState();

        double total = 0;
        for (VentaProducto pv : venta.getVentaProductos()) total += pv.getTotal();
        cambio = false;
        totalVenta.setText(NumberFormat.getCurrencyInstance(Locale.US).format(total));
    }

    public void errorPrecio(VentaProducto producto, String mensaje) {
        errorpro = producto;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage(mensaje).setCancelable(false)
                .setTitle("Error en los datos")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            DialogFragment dialog1 = new DialogoAgregarProducto();
                            dialog1.show(getSupportFragmentManager(), "Agregar Producto");
                            //
                        }catch(Exception e){
                            InicioActivity.Toast(VentaActivity.this,"error:"+ e.getMessage());
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
    }
    boolean[] checkedItems = null;
    List<EditText> amountInputs = new ArrayList<>(); // Guardar referencias a los EditTexts para que podamos obtener sus valores más tarde

    public void aceptarOnClick(View view) {
        if(adapterProductosVentas.getCount()>0) {
            if(venta.isNueva() || (InicioActivity.getListaMetodos().size()>0)) {
                AlertDialog levelDialog = null;
                CharSequence[] items = {" Contado ", " Credito "};
                if(InicioActivity.getListaMetodos().size()>0) {
                    items = InicioActivity.getListaMetodos().stream().map(Metodos::getNombre).toArray(String[]::new);
                }
                checkedItems = new boolean[items.length]; // para rastrear qué elementos se han seleccionado
                if(!venta.getMetodosMultiples().isEmpty()){
                    Arrays.stream(venta.getMetodosMultiples().split(";")).sequential().forEach(metodo -> {
                        String nombreMetodo = metodo.split(":")[0];
                        int index = InicioActivity.getListaMetodos().indexOf(InicioActivity.getListaMetodos().stream().filter(m -> m.getNombre().contains(nombreMetodo)).findFirst().get());
                        checkedItems[index] = true;
                    });
                }
                // Creating and Building the Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if(InicioActivity.getListaMetodos().size() == 0) {
                builder.setTitle("Selecciona metodo de pago");
                    builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            //finalLevelDialog1.dismiss();
                            switch (item) {
                                case 0:
                                    metodo = true;
                                    confirmacionDialog();
                                    break;
                                case 1:
                                    metodo = false;
                                    confirmacionDialog();
                                    break;
                            }
                            dialog.dismiss();
                        }
                    });
                }else{
                    builder.setTitle("Selecciona metodo(s) de pago");
                    builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            checkedItems[which] = isChecked;
                        }

                    });
                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Aquí puedes presentar otro diálogo para ingresar las cantidades para cada método seleccionado
                            // O una nueva actividad, según tu preferencia
                            List<Metodos> selectedMethods = new ArrayList<>();
                            for (int i = 0; i < checkedItems.length; i++) {
                                if (checkedItems[i]) {
                                    selectedMethods.add(InicioActivity.getListaMetodos().get(i));
                                }
                            }
                            Double total = 0.0;
                            for (VentaProducto pv : venta.getVentaProductos()) total += pv.getTotal();
                            totalAmount = total;
                            // Pasar a la función de introducir montos o lo que decidas hacer
                            enterAmountsForMethods(selectedMethods);
                        }
                    });
                }
                levelDialog = builder.create();
//                levelDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#e18a33"));
                levelDialog.show();
            }
            else{
                confirmacionDialog();
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage("La venta no se guardara. ¿Desea descartar la venta?")
                    .setTitle("Venta sin productos")
                    .setIcon(android.R.drawable.ic_delete)
                    .setCancelable(false)
                    .setPositiveButton("Descartar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            cancelarOnClick(null);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            InicioActivity.Toast(VentaActivity.this,
                                    "Continuar con la venta actual");
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            possitive.setTextColor(Color.parseColor("#e18a33"));
            Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negative.setTextColor(Color.parseColor("#e18a33"));
        }
    }
    double totalAmount = 0.0; // Poner aquí tu total
    private void enterAmountsForMethods(List<Metodos> selectedMethods) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Introduce la cantidad a pagar con cada metodo");
        builder.setCancelable(false);

        // Layout principal
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);

        TextView note = new TextView(this);
        note.setText("La suma de las cantidades debe ser igual al total de la venta.");
        note.setTextColor(Color.rgb(200, 100, 20));
        note.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        note.setGravity(Gravity.CENTER);
//        note.setPadding(10, 10, 10, 10);
        layout.addView(note);

        amountInputs = new ArrayList<>();

// Obtener la altura de la pantalla
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

// Calcular el 50% de la altura de la pantalla
        int maxLayoutHeight = (int) (screenHeight * 0.30);

        ScrollView scrollView = new ScrollView(this);
        LinearLayout layoutScroll = new LinearLayout(this);
        layoutScroll.setOrientation(LinearLayout.VERTICAL);
        layoutScroll.setPadding(10, 10, 10, 10);

// Establecer la altura máxima para el LinearLayout
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, maxLayoutHeight));
        double sum = 0.0;

        for (Metodos method : selectedMethods) {
            // Layout por método de pago
            LinearLayout innerLayout = new LinearLayout(this);
            innerLayout.setOrientation(LinearLayout.HORIZONTAL);
            innerLayout.setGravity(Gravity.CENTER);

            TextView textView = new TextView(this);
            textView.setText(method.getNombre());
            innerLayout.addView(textView);

            EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editText.setHint("0.0");
            if(!venta.getMetodosMultiples().isEmpty()){
                if(venta.getMetodosMultiples().contains(method.getNombre())) {
                    String cantidad = Arrays.stream(venta.getMetodosMultiples().split(";")).sequential().filter(m -> m.contains(method.getNombre())).findFirst().get();
                    if (!cantidad.isEmpty()) editText.setText(cantidad.split(":")[1]);
                    sum += Double.parseDouble(cantidad.split(":")[1]);
                }
            }
            innerLayout.addView(editText);

            amountInputs.add(editText);

            layoutScroll.addView(innerLayout);
        }
        scrollView.addView(layoutScroll);
        layout.addView(scrollView);

        TextView totals = new TextView(this);
        totals.setText("Suma total: $" + String.format("%.2f", sum));
        totals.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        totals.setGravity(Gravity.CENTER);
        totals.setPadding(10, 10, 10, 0);
        layout.addView(totals);

        TextView total = new TextView(this);
        total.setText("Total venta: $" + totalAmount);
        total.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        total.setGravity(Gravity.CENTER);
        total.setPadding(10, 0, 10, 10);
        total.setTextColor(Color.BLACK);
        layout.addView(total);


        builder.setView(layout); // Estableces layout como la vista del AlertDialog

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double sum = 0.0;
                for (EditText input : amountInputs) {
                    String valueStr = input.getText().toString().trim();
                    if (!valueStr.isEmpty()) {
                        valueStr = valueStr.replace(",", ".");
                        sum += Double.parseDouble(valueStr);
                    }
                }

                if (Math.abs(totalAmount - sum) < 0.01) { // Usar un pequeño margen debido a la precisión de los doubles
                    // Mostrar mensaje de éxito
                    metodosMultiples = "";
                    for (int i = 0; i < selectedMethods.size(); i++) {
                        metodosMultiples += selectedMethods.get(i).getNombre()
                                + ":" + amountInputs.get(i).getText().toString().replace(",",".") + ";";
                    }
                    confirmacionDialog();
                } else {
                    // Mostrar error
                    Toast.makeText(getApplicationContext(), "La suma de las cantidades no coincide con el total", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        // Inhabilitar el botón de confirmar hasta que las cantidades sean correctas
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//
//        for (EditText input : amountInputs) {
//            String valueStr = input.getText().toString().trim();
//            if (!valueStr.isEmpty()) {
//                valueStr = valueStr.replace(",", ".");
//                sum += Double.parseDouble(valueStr);
//            }
//        }
//        totals.setText("Suma total: $" + String.format("%.2f", sum));
//
        if (Math.abs(totalAmount - sum) < 0.001) {
            totals.setTextColor(Color.rgb(20, 200, 20));
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }else if(sum < 0.001){
            totals.setText("Suma total: $"+String.format("%.2f", totalAmount - sum));
            amountInputs.get(0).setText(String.format("%.2f", totalAmount - sum));
            amountInputs.get(0).requestFocus();
            amountInputs.get(0).setSelection(amountInputs.get(0).getText().length());
            totals.setTextColor(Color.rgb(20, 200, 20));
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }

        for (EditText editText : amountInputs) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    double sum = 0.0;
                    for (EditText input : amountInputs) {
                        String valueStr = input.getText().toString().trim();
                        if (!valueStr.isEmpty()) {
                            valueStr = valueStr.replace(",", ".");
                            sum += Double.parseDouble(valueStr);
                        }
                    }
                    totals.setText("Suma total: $" + String.format("%.2f", sum));
                    if (Math.abs(totalAmount - sum) < 0.001) {
                        totals.setTextColor(Color.rgb(20, 200, 20));
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    } else {
                        totals.setTextColor(Color.rgb(200, 20, 20));
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    public void confirmacionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage("La venta se va a guardar. ¿Esta seguro que desea salir?\nOprima No para seguir vendiendo")
                .setTitle("La venta se guardara")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(config.getComentar()==1) {
                            aceptarconfirmacionComentarioOnClick(null);
                        }else {
                            aceptarconfirmacionOnClick(null);
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        InicioActivity.Toast(VentaActivity.this,
                                "Continuar con la venta actual");
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
        Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negative.setTextColor(Color.parseColor("#e18a33"));
    }
    Boolean metodo = true;
    String metodosMultiples = "";
    android.app.Activity ventaActivity = null;
    public void aceptarconfirmacionOnClick(View view) {

                        if (venta.isNueva()) {
                            venta.setCliente(getCliente());
                            venta.setFecha(new Date());
                            venta.setId(0);
                            venta.setMetodo(metodo);
                            venta.setMetodosMultiples(metodosMultiples);
                            venta.setNueva(false);
                            venta.setNota("");
                            ListaVentasActivity.agregarVenta(venta);
                            InicioActivity.getDB().agregarVenta(venta);
                        } else {
                            venta.setNota("");
                            venta.setMetodosMultiples(metodosMultiples);
                            InicioActivity.getDB().limpiarVentas();
                            ListaVentasActivity.modificarVenta(venta);
                            for (Venta ventax : ListaVentasActivity.getListaVentas())
                                InicioActivity.getDB().agregarVenta(ventax);
                        }

                        if (InicioActivity.impresiones.getmostrarImpresion() == 1) imprimir();
                        else {
                            InicioActivity.Toast(ventaActivity, "La venta ha sido guardada");
                            cancelarOnClick(null);
                        }


    }
    public void aceptarconfirmacionComentarioOnClick(View view) {

        final EditText NotaText = new EditText(this);
        LinearLayout.LayoutParams Style = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        NotaText.setLayoutParams(Style);

        NotaText.setText(venta.getNota());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage("Ingresa tu comentario sobre la venta:")
                .setTitle("Comentario de venta")
                .setView(NotaText)
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (venta.isNueva()) {
                            venta.setCliente(getCliente());
                            venta.setFecha(new Date());
                            venta.setId(0);
                            venta.setMetodo(metodo);
                            venta.setNueva(false);
                            venta.setNota(NotaText.getText().toString());
                            venta.setMetodosMultiples(metodosMultiples);
                            ListaVentasActivity.agregarVenta(venta);
                            InicioActivity.getDB().agregarVenta(venta);
                        } else {
                            venta.setNota(NotaText.getText().toString());
                            venta.setMetodosMultiples(metodosMultiples);
                            InicioActivity.getDB().limpiarVentas();
                            ListaVentasActivity.modificarVenta(venta);
                            for (Venta ventax : ListaVentasActivity.getListaVentas())
                                InicioActivity.getDB().agregarVenta(ventax);
                        }

                        if (InicioActivity.impresiones.getmostrarImpresion() == 1) imprimir();
                        else {
                            InicioActivity.Toast(ventaActivity, "La venta ha sido guardada");
                            cancelarOnClick(null);
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
        Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negative.setTextColor(Color.parseColor("#e18a33"));

        //Intent intent;
        //intent = new Intent(this, BluetoothPrinter.class);


    }
    private static final String BS_PACKAGE = "com.fidelier.posprinterdriver";

    private void imprimir() {
//        Boolean targetAppInstalled = false;
//        PackageManager pm21 = this.getPackageManager();
//        Intent intent2 = new Intent(Intent.ACTION_MAIN, null);
//        intent2.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> list = pm21.queryIntentActivities(intent2, PackageManager.GET_ACTIVITIES);
//        for (ResolveInfo rInfo : list) {
//            String app = rInfo.activityInfo.packageName;
//            if (app.compareToIgnoreCase(BS_PACKAGE) == 0) {
//                targetAppInstalled = true;
//            }
//        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        if (targetAppInstalled) {
            builder
                    .setMessage("La venta se guardo. ¿Desea imprimir la venta guardada?")//+ macAddress)
                    .setTitle("Venta guardada. Impresion de venta")
                    .setIcon(android.R.drawable.ic_menu_upload)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DialogFragment dialogoimprimir = new DialogoPrint(getVenta());
                            dialogoimprimir.show(getSupportFragmentManager(), "Imprimir ventas");
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            InicioActivity.Toast(VentaActivity.this,
                                    "La venta no se imprimio");
                            cancelarOnClick(null);
                        }
                    });
//        }
//        else {
//            builder.setTitle("Instala y configura POS PRINTER")
//                    .setMessage("Se requiere de esta aplicacion para la impresion. Instalala y configura tu impresora.")
//                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            String packageName;
//                            packageName = BS_PACKAGE;
//                            Uri uri = Uri.parse("market://details?id=" + packageName);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            try {
//                                VentaActivity.this.startActivity(intent);
//                            } catch (ActivityNotFoundException anfe) {
//                                // Hmm, market is not installed
//                                Log.w(TAG, "Google Play is not installed; cannot install " + packageName);
//                            }
//                        }
//                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                }
//            });
//        }
        AlertDialog dialog = builder.create();
        dialog.show();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
        Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negative.setTextColor(Color.parseColor("#e18a33"));
    }
    public Venta getventas() {
        return venta;
    }

    public String impresoraencontrada() {

        try {
            BluetoothDiscoverer.findPrinters(this, new DiscoveryHandler() {


                public void discoveryFinished() {
                    //Discovery is done
                    // macAddress = null;
                }

                public void discoveryError(String message) {
                    //Error during discovery
                    macAddress = null;
                }

                public void foundPrinter(DiscoveredPrinter printer) {
                    // TODO Auto-generated method stub

                    macAddress = printer.address;
                }
            });
        } catch (ConnectionException e) {
            macAddress = null;
        }
        return macAddress;
    }

    public void cancelarOnClick(View view) {

        VentaActivity.this.finish();

    }

    public void cancelarconfirmaOnClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage("La venta no se guardo. ¿Esta seguro que desea salir?\nOprima No para seguir vendiendo")
                .setTitle("Venta no guardada")
                .setIcon(android.R.drawable.ic_delete).setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cancelarOnClick(null);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        InicioActivity.Toast(VentaActivity.this,
                                "Continuar con la venta actual");
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
        Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negative.setTextColor(Color.parseColor("#e18a33"));
    }

    public void imprimirOnClick(View view) {

        InicioActivity.Toast(this,
                "La venta se envio a impresion");
        InicioActivity.Toast(this,
                "Imprimiendo la venta");
        this.finish();
    }

    public Cliente getCliente() {
        String scliente = (String) clientes.getSelectedItem();
        scliente = clientes.getSelectedItem().toString();
        int[] arreglofinal = ((DialogoBuscarCliente) dialog).clientesEncontradosID();
        Cliente cliente;
        if(arreglofinal[clientes.getSelectedItemPosition()] == 0)
            cliente = InicioActivity.getListaClientes().get(clientes.getSelectedItemPosition());
        else
            cliente = InicioActivity.getListaClientes().get(arreglofinal[clientes.getSelectedItemPosition()]);
        return cliente;

//        for (Cliente cliente : InicioActivity.getListaClientes()) {
//            if (cliente.getNombre().equals(scliente)) {
//                return cliente;
//            }
//        }
//        return null;
    }


    public Venta getVenta() {
        return venta;
    }

    public void leerCodigoOnClick(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }
    public void imprimirCXC() {
        String dataToPrint = "$intro$";
        Double totalTicket = 0.0;

        /////////////ENCABEZADO INICIO/////////////////////
        if(InicioActivity.impresiones.getencabezado1().length() >0) dataToPrint = dataToPrint + (InicioActivity.impresiones.getencabezado1().length() > 30 ? InicioActivity.impresiones.getencabezado1().substring(0, 30) : InicioActivity.impresiones.getencabezado1()) + "$intro$";
        if(InicioActivity.impresiones.getencabezado2().length() >0) dataToPrint = dataToPrint + (InicioActivity.impresiones.getencabezado2().length() > 30 ? InicioActivity.impresiones.getencabezado2().substring(0, 30) : InicioActivity.impresiones.getencabezado2()) + "$intro$";
        if(InicioActivity.impresiones.getencabezado3().length() >0) dataToPrint = dataToPrint + (InicioActivity.impresiones.getencabezado3().length() > 30 ? InicioActivity.impresiones.getencabezado3().substring(0, 30) : InicioActivity.impresiones.getencabezado3()) + "$intro$";
        /////////////ENCABEZADO FIN///////////////////////
        dataToPrint = dataToPrint + "-------------------------------" + "$intro$";
        /////////////ENCABEZADO INICIO/////////////////////
        dataToPrint = dataToPrint + "Vendedor: " + InicioActivity.getVendedorSeleccionado().getNombre() + "$intro$";
        dataToPrint = dataToPrint + "Cliente: " + getCliente() + "$intro$";
        Date fecha = new Date();
        dataToPrint = dataToPrint + "Fecha: " + String.format("%02d/%02d/%02d %02d:%02d", fecha.getDate(), fecha.getMonth() + 1, fecha.getYear() + 1900, fecha.getHours(), fecha.getMinutes()) + "$intro$";
        dataToPrint = dataToPrint + "-------------------------------" + "$intro$";
            ////////////VENTAS INICIO///////////////////////////////
            if (cuentas.size() > 0) {
                dataToPrint = dataToPrint + "------------ABONOS-------------" + "$intro$";
                dataToPrint = dataToPrint + "-------------------------------" + "$intro$";
                dataToPrint = dataToPrint + "Vence| TipoNum| Saldo | Abono "+"$intro$";
                dataToPrint = dataToPrint + "-------------------------------" + "$intro$";
                for (Cuentas abono : cuentas) {
                    int cantidad = 0;
                    Double total = 0.0;
                   // if(abono.toString().length()>12)
                        if(abono.getVencimiento().length()>12) {
                            dataToPrint = dataToPrint +
                                    abono.getVencimiento().substring(7,13).replace("-","")+"|" + //fecha
                                    abono.getVencimiento().substring(17,25)+"|"+ //TipoNum
                                    String.format("%7s", (abono.getSaldo()))+
                                    String.format("%7s", (abono.getVencimiento().substring(abono.getVencimiento().lastIndexOf('|')).trim()))+ //TipoNum
//                                    abono.getVencimiento().substring(6,5)+"|"+ //TipoNum
                                    "$intro$";//+"|"+abono.getTipo()+abono.getnumero()+"|"+abono.getSaldo();
                            totalTicket += abono.getSaldo();
                        }
                        //else
                        //dataToPrint = dataToPrint + abono.getVencimiento()+ "$intro$";

                }
                dataToPrint = dataToPrint + "-------------------------------" + "$intro$";
                dataToPrint = dataToPrint + "TOTAL:" + String.format("%24s",NumberFormat.getCurrencyInstance(Locale.US).format(totalTicket)) + "$intro$";
                dataToPrint = dataToPrint + "-------------------------------" + "$intro$";

            } else {
                dataToPrint = dataToPrint + "---------NO HAY ABONOS---------" + "$intro$";
                dataToPrint = dataToPrint + "-------------------------------" + "$intro$";
            }

        AlertDialog.Builder builder = new AlertDialog.Builder(VentaActivity.this);
        final String finalDataToPrint = dataToPrint;
        builder
                .setMessage(finalDataToPrint.replace("$intro$","\n"))
                .setTitle("ABONOS _ CUENTAS POR COBRAR")
                .setIcon(android.R.drawable.ic_menu_agenda)
                .setPositiveButton("IMPRIMIR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
                                        Intent intentPrint = new Intent();

                                        intentPrint.setAction(Intent.ACTION_SEND);
                                        intentPrint.putExtra(Intent.EXTRA_TEXT, finalDataToPrint);
                                        intentPrint.setPackage("com.fidelier.posprinterdriver");
                                        intentPrint.setType("text/plain");

                                        startActivity(intentPrint);
                                        dialog.dismiss();
                                        InicioActivity.Toast(VentaActivity.this,
                                                "Continuar con la venta actual");
                                        ///////////////////
                                        Agregar.removeAllViews();
                                        cuentas.clear();
                                    }
                                });


        AlertDialog dialog = builder.create();
        dialog.show();
        try {
            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            textView.setTypeface(Typeface.MONOSPACE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        }catch (Exception ex){
            Log.v("Exception", ex.getLocalizedMessage());
        }


//        //textView.setMaxLines(50);
//        textView.setScroller(new Scroller(VentaActivity.this));
//        textView.setVerticalScrollBarEnabled(true);
//        textView.setMovementMethod(new ScrollingMovementMethod());

        Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positive.setTextColor(Color.parseColor("#e18a33"));
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String upc = scanResult.getContents();
            for (int i = 0; i < InicioActivity.getListaClientes().size(); i++) {
                Cliente cliente = InicioActivity.getListaClientes().get(i);
                if (cliente.getClave().equals(upc)) {
                    clientes.setSelection(i);
                    break;
                }
            }
        }
    }

    public Configura Configuracionleer() {
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
public void Buttons(){
        Vista = this.findViewById(R.id.button2);
        Iniciar = this.findViewById(R.id.Iniciaventa);
        Terminar = this.findViewById(R.id.Terminaventa);
        add = this.findViewById(R.id.multiple_actions);
        Reimprimir.setVisibility(View.GONE);
        if (InicioActivity.impresiones.getmostrarImpresion() == 1 && !venta.isNueva()) {
            Reimprimir.setVisibility(View.VISIBLE);
        }
        if(Configuracionleer().getEditarVenta()==0 && !venta.isNueva())
        {
            add.setVisibility(View.INVISIBLE);
            Aceptar.setVisibility(View.GONE);
            Cancelar.setVisibility(View.GONE);
        }
        Vista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visualizarClienteOnClick(v);
            }
        });
        Iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entradaVentaOnClick(v);
            }
        });
        Terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salidaVentaOnClick(v);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CuentasPay){
                    agregarAbonoOnClick(v);
                }else {
                    agregarProductoOnClick(v);
                }
            }
        });
        Reimprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogoimprimir = new DialogoPrint(getVenta());
                dialogoimprimir.show(getSupportFragmentManager(), "Imprimir ventas");
            }
        });
        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(Configuracionleer().getEditarVenta()==0 && !venta.isNueva() && InicioActivity.impresiones.getmostrarImpresion() == 1 && (InicioActivity.getListaMetodos().size() == 0)){
//
//                }else{
                    aceptarOnClick(v);
//                }
            }
        });
        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarconfirmaOnClick(v);
            }
        });
}

    public void guardarNuevoClienteOnClick() {
        if (!(clienteseleccionado.isEmpty())) {
            InicioActivity.getListaClientes().add(clienteseleccionadoCliente);
            InicioActivity.Toast(VentaActivity.this,"Actualizando cliente...");
            VentaActivity.EnviarClientesPOST envioClientes = new VentaActivity.EnviarClientesPOST() {
                @Override
                public void onGetValue(final String value) {
                    VentaActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            InicioActivity.Toast(VentaActivity.this,value);
                            if(value.contains("error")){
                                guardarNuevoClienteOnClick();
                            }else{
                                nuevoCliente = false;
                                cargarClientes();
                                clientes.setSelection(clientes.getCount()-1);
                                InicioActivity.Toast(VentaActivity.this,"Envio correcto");
                            }
                        }
                    });
                }
            };
            envioClientes.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            InicioActivity.Toast(this, "Cliente guardado");

        }
    }

    /// Funcion para subir el XML actualizado de clientes
    public abstract class EnviarClientesPOST extends AsyncTask<Void, Void, Void> {
        public EnviarClientesPOST() {}
        protected Void doInBackground(Void... progress) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            try {
                InicioActivity.actualInventario();
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Principalctivity.Configuracion(InicioActivity.InicioActividad.getExternalFilesDir(null)).getFTP().trim() + "recibirventas.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                StringBuilder Datos = new StringBuilder("<clientes>");
                for(Cliente cliente : InicioActivity.getListaClientes()){
                    String productoString = "<cliente>"+
                            "<clave>"+cliente.getClave()+"</clave>"+
                            "<nombre>"+cliente.getNombre()+"</nombre>"+
                            "<numPrecio>"+cliente.getNumPrecio()+"</numPrecio>"+
                            "<calle>"+cliente.getCalle()+"</calle>"+
                            "<numExterior>"+cliente.getNumEx()+"</numExterior>"+
                            "<numInterior>"+cliente.getNumIn()+"</numInterior>"+
                            "<colonia>"+cliente.getColonia()+"</colonia>"+
                            "<cp>"+cliente.getCP()+"</cp>"+
                            "<ciudad>"+cliente.getCiudad()+"</ciudad>"+
                            "<estado>"+cliente.getEstado()+"</estado>"+
                            "<telefono1>"+cliente.getTelefono1()+"</telefono1>"+
                            "<telefono2>"+cliente.getTelefono2()+"</telefono2>"+
                            "<telefono3>"+cliente.getTelefono3()+"</telefono3>"+
                            "<telefono4>"+cliente.getTelefono4()+"</telefono4>"+
                            "<atencion>"+cliente.getAtencion()+"</atencion>"+
                            "<diascredito>"+cliente.getDiasCredito()+"</diascredito>"+
                            "</cliente>";
                    productoString = productoString.
                            replace("&","&amp;").
                            replace("ï¿½","N").
                            replace("Ñ","N").
                            replace("ñ","n").
                            replace("null","");
                    Datos.append(productoString);
                }
                Datos.append("</clientes>");
                nameValuePairs.add(new BasicNameValuePair("archivo", "clientes.xml"));
                nameValuePairs.add(new BasicNameValuePair("ventas", Datos.toString()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity ent = response.getEntity();
                BufferedReader lector = new BufferedReader(new InputStreamReader(ent.getContent()));
                lector.close();
                if(response.getStatusLine().getStatusCode()==200){
                    onGetValue("El cliente se actualizado correctamente");
                    InicioActivity.cargarClientes(true);
                }else{
                    onGetValue("Ocurrio un error al actualizar el cliente");
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


}
