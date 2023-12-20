package com.example.registroventa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.registroventa.models.Cuentas;
import com.example.registroventa.models.Producto;
import com.example.registroventa.models.Venta;
import com.example.registroventa.models.VentaProducto;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class ListaVentasActivity extends AppCompatActivity {
    public static int Ventaseleccionada = 0;
    private static List<Venta> listaVentas = new ArrayList<Venta>();
    static Venta venta;
    private static List<String> arregloVentas = new ArrayList<String>();
    private static CustomAdapterListaVentas adapter;
    private static TextView cantidad;
    private static TextView total;
    private static ListView my_listview;
    public Button ImprimeCorte;
    public Button ImprimeInventario;
    public Button ImprimeAbonos;

    public static void agregarVenta(Venta venta) {
        adapter.add(venta.toString());
        listaVentas.add(venta);
        my_listview.refreshDrawableState();
        //cantidad = (TextView)findViewById(R.id.textView1);
        //total = (TextView)findViewById(R.id.textView2);
        Double Precio = 0.0;
        int Numero = 0;
        for (Venta vents : listaVentas) {
            for (VentaProducto vent : vents.getVentaProductos()) {
                Precio += vent.getTotal();
                Numero += vent.getCantidad();
            }
        }
        cantidad.setText("Productos: " + Numero);
        total.setText("Total: " + NumberFormat.getCurrencyInstance(Locale.US).format(Precio));
        InicioActivity.actualInventario();
    }

    public static void modificarVenta(Venta venta) {
        listaVentas.remove(Ventaseleccionada);
        listaVentas.add(Ventaseleccionada, venta);
        arregloVentas.remove(Ventaseleccionada);
        arregloVentas.add(Ventaseleccionada, venta.toString());
        adapter.clear();
        for (Venta ventax : listaVentas)
            adapter.add(ventax.toString());
        my_listview.refreshDrawableState();
        Double Precio = 0.0;
        int Numero = 0;
        for (Venta vents : listaVentas) {
            for (VentaProducto vent : vents.getVentaProductos()) {
                Precio += vent.getTotal();
                Numero += vent.getCantidad();
            }
        }
        cantidad.setText("Productos: " + Numero);
        total.setText("Total: " + NumberFormat.getCurrencyInstance(Locale.US).format(Precio));
        InicioActivity.actualInventario();
    }

    public static void limpiarVentas() {
        listaVentas.clear();
        //my_listview.refreshDrawableState();
    }

    public static List<Venta> getListaVentas() {
        return listaVentas;
    }

    public static void setListaVentas(List<Venta> listaVentas) {
        ListaVentasActivity.listaVentas = listaVentas;
        /*Double Precio = 0.0;
		int Numero = 0;
		for(Venta vents : listaVentas)
		{
			for(VentaProducto vent: vents.getVentaProductos())
			{
				Precio += vent.getTotal();
				Numero += vent.getCantidad();
			}
		}
		cantidad.setText("Cantidad de productos: " + Numero);
		total.setText("Total: "+NumberFormat.getCurrencyInstance(Locale.US).format(Precio));*/
    }

    public static Venta getVenta() {
        return venta;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ventas);
        //this.listaVentas.clear();
        //my_listview.refreshDrawableState(;
        cantidad = (TextView) findViewById(R.id.VersionC);
        total = (TextView) findViewById(R.id.numInterior);
        ImprimeCorte = findViewById(R.id.imprimirCorte2);
        ImprimeInventario = findViewById(R.id.imprimirCorte);
        ImprimeAbonos = findViewById(R.id.imprimirAbonos);
        ImprimeCorte.setVisibility(View.GONE);
        ImprimeInventario.setVisibility(View.GONE);
        ImprimeAbonos.setVisibility(View.GONE);
        Boolean cuentaAbonos = false;
        for (Cuentas cuenta : InicioActivity.getListaCuentas()) {
            if (cuenta.getTipo().compareToIgnoreCase("A") == 0) cuentaAbonos = true;
        }

        if ((InicioActivity.impresiones.getimprimirCorte() == 1 || InicioActivity.impresiones.getimprimirCorte() == 3)) {
            ImprimeCorte.setVisibility(View.VISIBLE);
        }
        if (InicioActivity.impresiones.getimprimirCorte() > 1) {
            ImprimeInventario.setVisibility(View.VISIBLE);
        }
        if (cuentaAbonos) {
            ImprimeAbonos.setVisibility(View.VISIBLE);
        }
        Double Precio = 0.0;
        int Numero = 0;
        for (Venta vents : listaVentas) {
            if (vents.getCliente() == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setTitle("BORRAR VENTAS")
                        .setMessage("Se cambio el Negocio FTP o elimino clientes de su base de datos.\n ¿Seguro quiere borrar las ventas?")
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton("BORRAR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes button clicked, do something
                                InicioActivity.getDB().limpiarVentas();
                                InicioActivity.getDB().limpiarCuentas();
                                ListaVentasActivity.limpiarVentas();
                                InicioActivity.Toast(ListaVentasActivity.this, "Ventas eliminadas");
                                finish();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes button clicked, do something
                                finish();
                                finish();
                                finish();
                            }
                        });
                //.show();
                AlertDialog alert = builder.create();
                alert.show();
                Button possitive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                possitive.setTextColor(Color.parseColor("#e18a33"));
                Button negative = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setTextColor(Color.parseColor("#e18a33"));
            }
        }
        for (Venta vents : listaVentas) {

            for (VentaProducto vent : vents.getVentaProductos()) {
                Precio += vent.getTotal();
                Numero += vent.getCantidad();
            }
        }
        cantidad.setText("Productos: " + Numero);
        total.setText("Total: " + NumberFormat.getCurrencyInstance(Locale.US).format(Precio));
        my_listview = (ListView) findViewById(R.id.listaVentas);
        my_listview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //adapter.clear();
        //my_listview.
//        adapter = new ArrayAdapter<String>(this, R.layout.lista_item_layout, arregloVentas);
        adapter = new CustomAdapterListaVentas(ListaVentasActivity.this, R.layout.lista_item_layout, arregloVentas,getSupportFragmentManager());

        my_listview.setAdapter(adapter);
        my_listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView) view).getText().toString();
                for (Venta ventax : listaVentas)
                    if (item.equals(ventax.toString())) {
                        Ventaseleccionada = position;
                        venta = ventax;
                        break;
                    }
                if (venta != null) {
                    Intent intent = new Intent(ListaVentasActivity.this, VentaActivity.class);
                    ListaVentasActivity.this.startActivity(intent);
                }
            }


        });

        //cargarVentasBD();
        crearVentas();
//        Button NuevaVenta = (Button) findViewById(R.id.agregarVenta);
//        NuevaVenta.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                agregarVentaOnClick(view);
//                return false;
//            }
////            @Override
////            public void onClick(View v) {
////                agregarVentaOnClick(v);
////            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(InicioActivity.getListaConfiguracion() == null)
        {
            menu.add(0, 0, 0, "Configuracion");
            menu.add(0, 1, 1, "Impresion");
        }
        else if(InicioActivity.getListaConfiguracion().get(0).getAsistencia())
        {
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

    public void agregarVentaOnClick(View view) {
        venta = new Venta();
        venta.setVendedor(InicioActivity.getVendedorSeleccionado());
        try {
            Intent intent = new Intent(this, VentaActivity.class);
            this.startActivity(intent);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void crearVentas() {
        try {
            adapter.clear();
            for (int i = 0; i < listaVentas.size(); i++)
                adapter.add(listaVentas.get(i).toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void imprimirCortes(View view) {
        StringBuilder dataToPrint = new StringBuilder("$intro$");
        int cantidadTicket = 0;
        Double totalTicket = 0.0;

        /////////////ENCABEZADO INICIO/////////////////////
        dataToPrint.append(InicioActivity.impresiones.getencabezado1().length() > 30 ? InicioActivity.impresiones.getencabezado1().substring(0, 30) : InicioActivity.impresiones.getencabezado1()).append("$intro$");
        dataToPrint.append(InicioActivity.impresiones.getencabezado2().length() > 30 ? InicioActivity.impresiones.getencabezado2().substring(0, 30) : InicioActivity.impresiones.getencabezado2()).append("$intro$");
        dataToPrint.append(InicioActivity.impresiones.getencabezado3().length() > 30 ? InicioActivity.impresiones.getencabezado3().substring(0, 30) : InicioActivity.impresiones.getencabezado3()).append("$intro$");
        /////////////ENCABEZADO FIN///////////////////////
        dataToPrint.append("-----------------------------").append("$intro$");
        /////////////ENCABEZADO INICIO/////////////////////
        dataToPrint.append("Vendedor: ").append(InicioActivity.getVendedorSeleccionado().getNombre()).append("$intro$");
        Date fecha = new Date();
        dataToPrint.append("Fecha: ").append(String.format("%02d/%02d/%02d %02d:%02d", fecha.getDate(), fecha.getMonth() + 1, fecha.getYear() + 1900, fecha.getHours(), fecha.getMinutes())).append("$intro$");
                dataToPrint.append("-----------------------------").append("$intro$");
        /////////////ENCABEZADO FIN///////////////////////
        if ((InicioActivity.impresiones.getimprimirCorte() == 1 || InicioActivity.impresiones.getimprimirCorte() == 3)) {
            ////////////VENTAS INICIO///////////////////////////////
            if (listaVentas.size() > 0) {
                dataToPrint.append("----------VENTAS-------------").append("$intro$");
                dataToPrint.append("-----------------------------").append("$intro$");
                for (Venta ticket_productos : listaVentas) {
                    int cantidad = 0;
                    Double total = 0.0;
                    dataToPrint.append("Fecha: ").append(ticket_productos.getFecha().toLocaleString()).append("$intro$");
                    dataToPrint.append("-----------------------------").append("$intro$");
                    dataToPrint.append("Cantidad     Precio     Total").append("$intro$");
                    dataToPrint.append("-----------------------------").append("$intro$");
                    ///////////PRODUCTOS INICIO//////////////////////////
                    for (VentaProducto producto : ticket_productos.getVentaProductos()) {
                        if (producto.getProducto().toString().length() > 31) {
                            dataToPrint.append(producto.getProducto().toString().substring(0, 30)).append("$intro$");
                            if (producto.getProducto().toString().length() > 61) {
                                dataToPrint.append(producto.getProducto().toString().substring(30, 60)).append("$intro$");
                                dataToPrint.append(producto.getProducto().toString().substring(60)).append("$intro$");
                            } else {
                                dataToPrint.append(producto.getProducto().toString().substring(30)).append("$intro$");
                            }
                        } else {
                            dataToPrint.append(producto.getProducto().toString()).append("$intro$");
                        }
                        String space = "                             ";
                        dataToPrint.append(String.format("%.2f", producto.getCantidad())).append(space.substring(String.format("%.2f", producto.getCantidad()).length()));
                        dataToPrint.append(NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecioUnitario())).append(space.substring(NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecioUnitario()).length()));
                        dataToPrint.append(NumberFormat.getCurrencyInstance(Locale.US).format(producto.getTotal())).append("$intro$");

                        cantidad += producto.getCantidad();
                        total += producto.getTotal();
                    }
                    cantidadTicket += cantidad;
                    totalTicket += total;
                    ////////////PRODUCTOS FIN///////////////////////////
                    dataToPrint.append("-----------------------------").append("$intro$");
                    dataToPrint.append("Productos:             ");
                    dataToPrint.append(Integer.toString(cantidad)).append("$intro$");
                    dataToPrint.append(ticket_productos.getMetodosMultiples().isEmpty()?(ticket_productos.isMetodo() ? "Contado" : "Crédito"):
                                   "Pagado con:$intro$"+ticket_productos.getMetodoPagos()).
                            append("Subtotal: ").append(NumberFormat.getCurrencyInstance(Locale.US).format(total)).append("$intro$");
                    ////////////////VENTAS FIN///////////////////////////
                }
                dataToPrint.append("-----------------------------").append("$intro$");
                dataToPrint.append("-----------------------------").append("$intro$");
                dataToPrint.append("Productos:        ");
                dataToPrint.append(Integer.toString(cantidadTicket)).append("$intro$");
                dataToPrint.append("TOTAL:            ").append(NumberFormat.getCurrencyInstance(Locale.US).format(totalTicket)).append("$intro$");
                dataToPrint.append("-----------------------------").append("$intro$");

            } else {
                dataToPrint.append("---------NO HAY VENTAS-------").append("$intro$");
                dataToPrint.append("-----------------------------").append("$intro$");
            }
        }
        dataToPrint.append("-----------------------------").append("$intro$");
        dataToPrint.append("-----------------------------").append("$intro$");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String finalDataToPrint = dataToPrint.toString();
        builder
                .setMessage("Selecciona una opcion para el corte.")
                .setTitle("CORTE")
                .setIcon(android.R.drawable.ic_menu_agenda)
                .setPositiveButton("VISTA PREVIA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListaVentasActivity.this);
                        builder
                                .setMessage(finalDataToPrint.replace("$intro$","\n"))
                                .setTitle("Visualizacion CORTE")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog2 = builder.create();
                        dialog2.show();
                        TextView textView = (TextView) dialog2.findViewById(android.R.id.message);
                        //textView.setMaxLines(50);
                        textView.setScroller(new Scroller(ListaVentasActivity.this));
                        textView.setVerticalScrollBarEnabled(true);
                        textView.setMovementMethod(new ScrollingMovementMethod());
                        Button possitive = dialog2.getButton(DialogInterface.BUTTON_POSITIVE);
                        possitive.setTextColor(Color.parseColor("#e18a33"));

                    }
                })
                .setNegativeButton("IMPRIMIR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //////////IMPRIMIR//////////////
                        Intent intentPrint = new Intent();

                        intentPrint.setAction(Intent.ACTION_SEND);
                        intentPrint.putExtra(Intent.EXTRA_TEXT, finalDataToPrint);
                        intentPrint.setPackage("com.fidelier.posprinterdriver");
                        intentPrint.setType("text/plain");

                        startActivity(intentPrint);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
        Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negative.setTextColor(Color.parseColor("#e18a33"));
    }
    public void imprimirInventarios(View view) {
        String dataToPrint = "$intro$";
        Double cantidadTicket = 0.0;
        Double totalTicket = 0.0;
        /////////////ENCABEZADO INICIO/////////////////////
        dataToPrint = dataToPrint + (InicioActivity.impresiones.getencabezado1().length() > 30 ? InicioActivity.impresiones.getencabezado1().substring(0, 30) : InicioActivity.impresiones.getencabezado1()) + "$intro$";
        dataToPrint = dataToPrint + (InicioActivity.impresiones.getencabezado2().length() > 30 ? InicioActivity.impresiones.getencabezado2().substring(0, 30) : InicioActivity.impresiones.getencabezado2()) + "$intro$";
        dataToPrint = dataToPrint + (InicioActivity.impresiones.getencabezado3().length() > 30 ? InicioActivity.impresiones.getencabezado3().substring(0, 30) : InicioActivity.impresiones.getencabezado3()) + "$intro$";
        /////////////ENCABEZADO FIN///////////////////////
        dataToPrint = dataToPrint +"-----------------------------"+"$intro$";
        /////////////ENCABEZADO INICIO/////////////////////
        dataToPrint = dataToPrint + "Vendedor: " + InicioActivity.getVendedorSeleccionado().getNombre() + "$intro$";
        Date fecha = new Date();
        dataToPrint = dataToPrint + "Fecha: " + String.format("%02d/%02d/%02d %02d:%02d", fecha.getDate(), fecha.getMonth() + 1, fecha.getYear() + 1900, fecha.getHours(), fecha.getMinutes()) + "$intro$";
        dataToPrint = dataToPrint + "-----------------------------"+ "$intro$";
        /////////////ENCABEZADO FIN///////////////////////

        //////////// INVENTARIO INICIO//////////////////////////
        if (InicioActivity.impresiones.getimprimirCorte() > 1) {
            dataToPrint = dataToPrint + "-----------------------------" + "$intro$";
            dataToPrint = dataToPrint + "--------INVENTARIO-----------" + "$intro$";
            dataToPrint = dataToPrint + "-----------------------------" + "$intro$"+ "$intro$";
            dataToPrint = dataToPrint + "Producto             Cantidad" + "$intro$";
                                      //+ "Precio               Cantidad"+"$intro$";
            dataToPrint = dataToPrint + "-----------------------------" + "$intro$";
            for (Producto producto : InicioActivity.getListaProductos()) {
                if (producto.getExistencia() > 0) {
                    //dataToPrint = dataToPrint + producto.toString() + "$intro$";
                    if (producto.getDescripcion().toString().length() > 31) {
                        dataToPrint = dataToPrint + producto.getDescripcion().toString().substring(0, 30) + "$intro$";
                        if (producto.getDescripcion().toString().length() > 61) {
                            dataToPrint = dataToPrint + producto.getDescripcion().toString().substring(30, 60) + "$intro$";
                            dataToPrint = dataToPrint + producto.getDescripcion().toString().substring(60) + "$intro$";
                        } else {
                            dataToPrint = dataToPrint + producto.getDescripcion().toString().substring(30) + "$intro$";
                        }
                    } else {
                        dataToPrint = dataToPrint + producto.getDescripcion().toString() + "$intro$";
                    }
                    String space = "                    ";
                    dataToPrint = dataToPrint + space.substring(Double.toString(producto.getExistencia()).length()) + Double.toString(producto.getExistencia()) + "$intro$";
                    totalTicket += producto.getExistencia();

                }
            }
        }
        dataToPrint = dataToPrint + "-----------------------------" + "$intro$";
        dataToPrint = dataToPrint + "TOTAL PRODUCTOS: " + Double.toString(totalTicket) + "$intro$";
        dataToPrint = dataToPrint + "-----------------------------" + "$intro$";
        //////////// INVENTARIO FIN ///////////////////////////
        final String finalDataToPrint = dataToPrint;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage("Selecciona una opcion para el inventario.")
                .setTitle("INVENTARIO")
                .setIcon(android.R.drawable.ic_menu_agenda)
                .setPositiveButton("VISTA PREVIA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListaVentasActivity.this);
                        builder
                                .setMessage(finalDataToPrint.replace("$intro$","\n"))
                                .setTitle("Visualizacion INVENTARIO")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog2 = builder.create();
                        dialog2.show();
                        TextView textView = (TextView) dialog2.findViewById(android.R.id.message);
                        //textView.setMaxLines(250);
                        textView.setScroller(new Scroller(ListaVentasActivity.this));
                        textView.setVerticalScrollBarEnabled(true);
                        textView.setMovementMethod(new ScrollingMovementMethod());
                        Button possitive = dialog2.getButton(DialogInterface.BUTTON_POSITIVE);
                        possitive.setTextColor(Color.parseColor("#e18a33"));
                    }
                })
                .setNegativeButton("IMPRIMIR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //////////IMPRIMIR//////////////
                        Intent intentPrint = new Intent();

                        intentPrint.setAction(Intent.ACTION_SEND);
                        intentPrint.putExtra(Intent.EXTRA_TEXT, finalDataToPrint);
                        intentPrint.setPackage("com.fidelier.posprinterdriver");
                        intentPrint.setType("text/plain");

                        startActivity(intentPrint);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
        Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negative.setTextColor(Color.parseColor("#e18a33"));
    }
    public void imprimirAbonos(View view) {
        String dataToPrint = "$intro$";
        Double cantidadTicket = 0.0;
        Double totalTicket = 0.0;
        /////////////ENCABEZADO INICIO/////////////////////
        dataToPrint = dataToPrint + (InicioActivity.impresiones.getencabezado1().length() > 30 ? InicioActivity.impresiones.getencabezado1().substring(0, 30) : InicioActivity.impresiones.getencabezado1()) + "$intro$";
        dataToPrint = dataToPrint + (InicioActivity.impresiones.getencabezado2().length() > 30 ? InicioActivity.impresiones.getencabezado2().substring(0, 30) : InicioActivity.impresiones.getencabezado2()) + "$intro$";
        dataToPrint = dataToPrint + (InicioActivity.impresiones.getencabezado3().length() > 30 ? InicioActivity.impresiones.getencabezado3().substring(0, 30) : InicioActivity.impresiones.getencabezado3()) + "$intro$";
        /////////////ENCABEZADO FIN///////////////////////
        dataToPrint = dataToPrint +"-----------------------------"+"$intro$";
        /////////////ENCABEZADO INICIO/////////////////////
        dataToPrint = dataToPrint + "Vendedor: " + InicioActivity.getVendedorSeleccionado().getNombre() + "$intro$";
        Date fecha = new Date();
        dataToPrint = dataToPrint + "Fecha: " + String.format("%02d/%02d/%02d %02d:%02d", fecha.getDate(), fecha.getMonth() + 1, fecha.getYear() + 1900, fecha.getHours(), fecha.getMinutes()) + "$intro$";
        dataToPrint = dataToPrint + "-----------------------------"+ "$intro$";
        /////////////ENCABEZADO FIN///////////////////////

        //////////// INVENTARIO INICIO//////////////////////////
        if (InicioActivity.impresiones.getimprimirCorte() > 1) {
            dataToPrint = dataToPrint + "-----------------------------" + "$intro$";
            dataToPrint = dataToPrint + "-----CORTE ABONOS---------" + "$intro$";
            dataToPrint = dataToPrint + "-----------------------------" + "$intro$"+ "$intro$";
            dataToPrint = dataToPrint + "Concepto                Abono" + "$intro$";
            //+ "Precio               Cantidad"+"$intro$";
            dataToPrint = dataToPrint + "-----------------------------" + "$intro$";
            String Cliente = "";
            for (Cuentas abono: InicioActivity.getListaCuentas()) {

                if(abono.getTipo().compareToIgnoreCase("A")==0){
                    //dataToPrint = dataToPrint + producto.toString() + "$intro$";
                    String AbonoCliente = "";
                    for(com.example.registroventa.models.Cliente nombre : InicioActivity.getListaClientes()){
                        if(nombre.getClave().compareToIgnoreCase(abono.getCliente())==0) AbonoCliente=nombre.getNombre();
                    }
                     if(Cliente.compareToIgnoreCase(AbonoCliente)!=0) {
                        Cliente = AbonoCliente;

                        if (AbonoCliente.length() > 31) {
                            dataToPrint = dataToPrint + AbonoCliente.substring(0, 30) + "$intro$$intro$";
                        } else {
                            dataToPrint = dataToPrint + AbonoCliente + "$intro$$intro$";
                        }
                    }
                     String Concepto = abono.getVencimiento().substring(0,abono.getVencimiento().length()-(String.valueOf(abono.getSaldo()).length()));
                    if (Concepto.length() > 31) {
                        dataToPrint = dataToPrint + Concepto.substring(0, 30) + "$intro$";
                    } else {
                        dataToPrint = dataToPrint + Concepto + "$intro$";
                    }
                    String space = "                       ";
                    String SaldoAbono = NumberFormat.getCurrencyInstance(Locale.US).format(abono.getSaldo());
                    dataToPrint = dataToPrint + space.substring(SaldoAbono.length()) + SaldoAbono + "$intro$";
                    //totalTicket += producto.getExistencia();

                }
            }
        }
//        dataToPrint = dataToPrint + "-----------------------------" + "$intro$";
//        dataToPrint = dataToPrint + "TOTAL PRODUCTOS: " + Double.toString(totalTicket) + "$intro$";
//        dataToPrint = dataToPrint + "-----------------------------" + "$intro$";
//        //////////// INVENTARIO FIN ///////////////////////////
        final String finalDataToPrint = dataToPrint;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage("Selecciona una opcion para los abonos.")
                .setTitle("CORTE ABONOS")
                .setIcon(android.R.drawable.ic_menu_agenda)
                .setPositiveButton("VISTA PREVIA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListaVentasActivity.this);
                        builder
                                .setMessage(finalDataToPrint.replace("$intro$","\n"))
                                .setTitle("Visualizacion ABONOS")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog2 = builder.create();
                        dialog2.show();
                        TextView textView = (TextView) dialog2.findViewById(android.R.id.message);
                        //textView.setMaxLines(250);
                        textView.setScroller(new Scroller(ListaVentasActivity.this));
                        textView.setVerticalScrollBarEnabled(true);
                        textView.setMovementMethod(new ScrollingMovementMethod());
                        Button possitive = dialog2.getButton(DialogInterface.BUTTON_POSITIVE);
                        possitive.setTextColor(Color.parseColor("#e18a33"));
                    }
                })
                .setNegativeButton("IMPRIMIR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //////////IMPRIMIR//////////////
                        Intent intentPrint = new Intent();

                        intentPrint.setAction(Intent.ACTION_SEND);
                        intentPrint.putExtra(Intent.EXTRA_TEXT, finalDataToPrint);
                        intentPrint.setPackage("com.fidelier.posprinterdriver");
                        intentPrint.setType("text/plain");

                        startActivity(intentPrint);
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
