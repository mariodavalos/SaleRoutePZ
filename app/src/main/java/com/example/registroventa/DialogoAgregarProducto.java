package com.example.registroventa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.registroventa.models.Cliente;
import com.example.registroventa.models.ListaPrecios;
import com.example.registroventa.models.PreciosAdicionales;
import com.example.registroventa.models.Producto;
import com.example.registroventa.models.VentaProducto;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.DialogFragment;


public class DialogoAgregarProducto extends DialogFragment {
    public static VentaProducto errorproducto;
    Button button;
    Button precib;
    Button search;
    VentaProducto ventaProducto = new VentaProducto();
    int numero_de_productos = InicioActivity.getListaProductos().size();
    final Double arregloDePSugerido[] = new Double[numero_de_productos];
    final Double arregloDePrecios[] = new Double[numero_de_productos];
    final Double arregloExistenci[] = new Double[numero_de_productos];
    String arregloProductos[] = new String[numero_de_productos];
    String arreglosBuscados[] = new String[numero_de_productos];
    int posicionproducto = 0;
    int n = 0;
    private Spinner productos;
    private Spinner preciosto;
    android.widget.ListPopupWindow popupWindow;
    ListView  listPopUp;
    private EditText cantidad;
    private EditText psugerido;
    private EditText precio;
    private EditText existen;
    Double existenciaCambian;
    private EditText productoBuscado;
    private TextView ClaveLabel;
    private TextView SugeridoLabel;
    private TextView ExistenciaLabel;
    private TextView ExistenciaLabel2;
    private VentaActivity actividad;
    private String Boton;
    private int despliegue = 8, precionum = 0;
    private int claveproducto = 0, existencias = 0, modificar = 0;

    @SuppressLint("WrongConstant")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        actividad = (VentaActivity) this.getActivity();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        try {

            claveproducto = VentaActivity.config.getClave();
            existencias = VentaActivity.config.getExistencia();
            modificar = VentaActivity.config.getModificar();
        } catch (Exception e1) {
        }


        errorproducto = actividad.errorpro;
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_nuevo_producto, null);
        if (actividad.cambio)
            Boton = "Modificar";
        else
            Boton = "Agregar";
        builder.setView(view)

                // Add action buttons
                .setPositiveButton(Boton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try{
                                actividad.addormod = true;
                        String mensajerro = "";
                        n = 0;
                        Producto producto = InicioActivity.getListaProductos().get(getIndexProducto(productos.getSelectedItem().toString()));
                        if(producto == null) return;
                        Double precis = 0.0;
                        ventaProducto.setProducto(producto);
                        Cliente cliente = actividad.getCliente();
                        if (cantidad.getText().length() > 0)
                            ventaProducto.setCantidad(Double.parseDouble((cantidad.getText().toString())));

                        if (!precio.getText().toString().isEmpty()) {
                            precis = Double.parseDouble(precio.getText().toString().replaceAll(",",""));
                        }
                        Double costos = producto.getCosto();
                        Double pclien = producto.getPrecios().get(cliente.getNumPrecio() - 1);
                        if(precionum==0)
                           precionum = cliente.getNumPrecio();
                        ventaProducto.setPrecioNum(precionum);

                        if (precio.getText().length() > 0) {
                            if (producto.getCosto() == null) {
                                InicioActivity.Toast(actividad, "No se detecto costo");
                                costos = 0.0;
                            }
                            if (precis >= costos) {
                                ventaProducto.setPrecioUnitario(precis);

                                if (cantidad.getText().length() > 0)
                                    ventaProducto.setTotal(precis * Double.parseDouble(cantidad.getText().toString()));
                            } else {
                                //precis = costos;
                                /*ventaProducto.setPrecioUnitario(precis);

                                if (cantidad.getText().length() > 0)
                                    ventaProducto.setTotal(precis * Double.parseDouble(cantidad.getText().toString()));
                                InicioActivity.Toast(actividad,"El precio no puede ser menor al costo.");*/
                                mensajerro += "El precio ingresado es menor al costo. ";

                            }
                        } else {
                            ventaProducto.setPrecioUnitario(pclien);
                            mensajerro += "El precio a quedado en blanco ingresa un precio. ";
                        }
                        if (productos.getSelectedItem().toString().equals("NINGUNO")) {
                            producto = InicioActivity.getListaProductos().get(0);
                            producto.setDescripcion("NINGUNO");
                            producto.setExistencias(0.0);
                            ventaProducto.setProducto(producto);
                            mensajerro += "El producto seleccionado es incorrecto verificalo.";
                        }
                        if (cantidad.getText().length() < 1) {
                            mensajerro += "Ingresa una cantidad del producto. ";
                        }
                        else if (Double.parseDouble(cantidad.getText().toString()) == 0) {
                            mensajerro += "La cantidad no puede ser 0. ";
                        }
                        if (mensajerro.isEmpty()) {
                            ventaProducto.setVenta(actividad.getventas());
                            if (actividad.cambio)
                                actividad.modificarProductoVenta(ventaProducto);
                            else
                                actividad.agregarProductoVenta(ventaProducto);
                        } else {
                            actividad.errorPrecio(ventaProducto, mensajerro);
                        }
                        InputMethodManager imm = (InputMethodManager) actividad.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                        //InicioActivity.actualInventario();
                            dismiss();
//                            view.dismiss();
                    }catch(Exception e){
                            InicioActivity.Toast(actividad, "Error: " + e.getMessage());
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actividad.addormod = true;
                        InputMethodManager imm = (InputMethodManager) actividad.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                        //InicioActivity.actualInventario();
                    }
                });
        if (actividad.cambio)
            builder.setView(view).setNeutralButton("Eliminar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    actividad.addormod = true;
                    actividad.modificarProductoVenta(null);
                    InputMethodManager imm = (InputMethodManager) actividad.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                    //InicioActivity.actualInventario();
                }
            });


        button = view.findViewById(R.id.botonproductos);
        search = view.findViewById(R.id.buttonsearch);
        precib = view.findViewById(R.id.desplegarPrecios);
        SugeridoLabel = (TextView) view.findViewById(R.id.textView6);
        psugerido = view.findViewById(R.id.psugerido);
        precio = view.findViewById(R.id.Precioproducto);
        listPopUp = view.findViewById(R.id.ListPopUp);

        //Permisos desde FTP XML Configuracion
        if(!InicioActivity.getListaConfiguracion().get(0).getModificarprecios())
        {
            precib.setVisibility(View.INVISIBLE);
            psugerido.setVisibility(View.INVISIBLE);
            SugeridoLabel.setVisibility(View.INVISIBLE);
            precio.setEnabled(false);
        }

        //Permisos desde Configuracion Local
        if(modificar==0){
            precio.setEnabled(false);
        }
        if(VentaActivity.config.getSeleccionar()==0){
            precib.setVisibility(View.INVISIBLE);
            psugerido.setVisibility(View.INVISIBLE);
            SugeridoLabel.setVisibility(View.INVISIBLE);
        }


        productos = (Spinner) view.findViewById(R.id.producto);
        try {
            cargarProductos();
        } catch (Exception e) {
        }
        preciosto = (Spinner) view.findViewById(R.id.todoslosprecios);
        cantidad = (EditText) view.findViewById(R.id.cantidad);
        cantidad.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                try {
                    Double cantidadCambian = (!cantidad.getText().toString().isEmpty())? Double.parseDouble(cantidad.getText().toString()): 0.0;
                    existen.setText(""+(existenciaCambian - cantidadCambian));
                }catch (Exception e){
                    e.getMessage();
                }
                return false;
            }
        });
        productoBuscado = (EditText) view.findViewById(R.id.productoBuscar);
        productoBuscado.addTextChangedListener(new TextWatcher() {
                                                   @Override
                                                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                   }

                                                   @Override
                                                   public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                       onClickBusqueda();
                                                       productoBuscado.requestFocus();
                                                   }

                                                   @Override
                                                   public void afterTextChanged(Editable s) {

                                                   }
                                               });

                existen = (EditText) view.findViewById(R.id.EditText01);
        ClaveLabel = (TextView) view.findViewById(R.id.clavelabel);
        ExistenciaLabel = (TextView) view.findViewById(R.id.TextView03);
        ExistenciaLabel2 = (TextView) view.findViewById(R.id.existencialabel2);

        if (existencias == 0) {
            existen.setVisibility(View.GONE);
            ExistenciaLabel.setVisibility(View.GONE);
            ExistenciaLabel2.setVisibility(View.GONE);
        }
        if (claveproducto == 0)
            ClaveLabel.setVisibility(View.GONE);

        if (actividad.cambio) {
            VentaProducto amodificar = actividad.venta.getVentaProductos().get(actividad.modificacion);
            button.setVisibility(View.GONE);
            search.setVisibility(View.GONE);
            productoBuscado.setVisibility(View.GONE);
            String[] producto = new String[1];
            producto[0] = amodificar.getProducto().toString();
            if(claveproducto == 1)
                producto[0] = amodificar.getProducto().getClave() + " - " + amodificar.getProducto().toString();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, producto);
            productos.setAdapter(adapter);
            productos.setClickable(false);
            cantidad.setText(String.valueOf(amodificar.getCantidad()));
            precio.setText(String.valueOf(amodificar.getPrecioUnitario()));
        } else if (errorproducto != null) {
            try{
            if (!errorproducto.getProducto().getDescripcion().isEmpty()) {
                precio.setText(Double.toString(errorproducto.getPrecioUnitario()));
                precioUni = String.valueOf(actividad.errorpro.getPrecioUnitario());
                if (errorproducto.getCantidad() > 0)
                    cantidad.setText(Double.toString(errorproducto.getCantidad()));
                precionum = errorproducto.getPrecioNum();

                int posicionproducto = 0;
                if (arregloProductos != null){
                    posicionproducto = getIndexProducto(errorproducto.getProducto().getDescripcion().toString());
                }
                if(posicionproducto<productos.getCount()) {
                    productos.setSelection(posicionproducto, true);
                }
            }
                errorproducto = null;
                actividad.errorpro = null;
            }catch(Exception e){
                InicioActivity.Toast(actividad,"error:"+ e.getMessage());
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBusqueda();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productoBuscado.getVisibility() == View.GONE) {
                    productoBuscado.setVisibility(View.VISIBLE);
                    listPopUp.setVisibility(View.VISIBLE);
                    button.setVisibility(View.INVISIBLE);
                    productos.setEnabled(false);
                    productoBuscado.requestFocus();
                    //InputMethodManager imm = (InputMethodManager) actividad.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    //imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                }else{
                    productoBuscado.setVisibility(View.GONE);
                    listPopUp.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    productos.setEnabled(true);
                }

            }
        });
        //productoBuscado.requestFocus();

        preciosto.setVisibility(despliegue);
        precib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (despliegue == 0) {
                    despliegue = 8;
                    precib.setTextColor(getResources().getColor(R.color.darkgrey));
                    precio.setVisibility(View.VISIBLE);
                    precib.setEnabled(true);
                } else if (despliegue == 8) {
                    despliegue = 0;
                    precib.setTextColor(getResources().getColor(R.color.orange));
                    precio.setVisibility(View.INVISIBLE);
                    //preciosto.performClick();
                    precib.setEnabled(false);
                }
                preciosto.setVisibility(despliegue);
                cargarPrecios();

            }
        });
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.create();
        setCancelable(false);// <-  press back button not cancel dialog, this one works fine
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button possitive = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_POSITIVE);
                possitive.setTextColor(Color.parseColor("#e18a33"));
                Button negative = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setTextColor(Color.parseColor("#e18a33"));
                Button neutral = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_NEUTRAL);
                neutral.setTextColor(Color.parseColor("#e18a33"));
            }
        });

        AlertDialog dialog2 = builder2.create();
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button possitive = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_POSITIVE);
                possitive.setTextColor(Color.parseColor("#e18a33"));
            }
        });

        InputMethodManager imm = (InputMethodManager) actividad.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        try {

            builder2
                    .setMessage("error")
                    .setTitle("Error en los datos")
                    .setPositiveButton("OK", null);
            return dialog;
        } catch (Exception e) {
            builder2
                    .setMessage(e.toString());
            return dialog2;
        }

    }
    String precioUni = "";
    String[] arreglofinal;
    @SuppressLint("SuspiciousIndentation")
    public void onClickBusqueda() {
        String buscado = productoBuscado.getText().toString();
        if (buscado.length() > 0) {
            int e = 0;
            String[] buscador= buscado.split(" ");
            Cliente cliente = actividad.getCliente();
            for (Producto prod : InicioActivity.getListaProductos()) {
                int palabraencontrada = 0;
                for(String busca:buscador)
                {
                    if(prod.getDescripcion().contains(busca.toUpperCase()) || prod.getClave().contains(busca.toUpperCase())) {
                        palabraencontrada++;
                    }
                }
                if (palabraencontrada == buscador.length) {
                    if (claveproducto == 1)
                        arreglosBuscados[e] = prod.getClave() + " - " + prod.getDescripcion();
                    else if (claveproducto == 0)
                        arreglosBuscados[e] = prod.getDescripcion();

                    if (prod.getExistencia() == null)
                        arregloExistenci[e] = (double) -1000000;
                    else
                        arregloExistenci[e] = prod.getExistencia();


                    arregloDePrecios[e] = prod.getPrecios().get(cliente.getNumPrecio() - 1);
                    for (ListaPrecios preciosN: InicioActivity.getListaPreciosNegociados()) {
                        if (preciosN.getCliente().compareToIgnoreCase(actividad.getCliente().getClave()) == 0)
                            if (preciosN.getProducto().compareToIgnoreCase(prod.getClave()) == 0)
                                arregloDePrecios[e] = preciosN.getPrecio();
                    }
                    if(prod.getPrecios().size()>5) {
                        arregloDePSugerido[e] = prod.getPrecios().get(5);
                    }else{
                        arregloDePSugerido[e] = arregloDePrecios[e];
                    }
                    e++;

                }

            }
            int f = numero_de_productos;
            if (e > 0)
                f = e;
             arreglofinal = new String[f];
            for (int i = 0; i < e; i++) {
                arreglofinal[i] = arreglosBuscados[i];
            }
            if (e == 0) {
                arreglofinal = arregloProductos;

                InicioActivity.Toast(actividad, "No coincidencias");
            } else {
                buscado.length();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, arreglofinal);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

            productos.setAdapter(adapter);
            listPopUp.setAdapter(adapter);


            OnItemSelectedListener selected = new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!actividad.cambio) {
                        precib.setTextColor(getResources().getColor(R.color.darkgrey));
                        precib.setEnabled(true);
                        precio.setVisibility(View.VISIBLE);
                        preciosto.setVisibility(View.GONE);

                        precio.setText(arregloDePrecios[position].toString());
                        psugerido.setText(arregloDePSugerido[position].toString());

                    }
                    if (existencias == 1 && arregloExistenci[position] != null) {
                        if (arregloExistenci[position] == -1000000) {
                            existen.setText("SIN");
//                            InicioActivity.Toast(actividad, "Existencia no registrada");
                        } else {
                            existen.setText(Double.toString(arregloExistenci[position]));
                            existenciaCambian = arregloExistenci[position];
                        }
                    } else {
                        existen.setText("SIN");
//                        InicioActivity.Toast(actividad, "Existencia no registrada");
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            };
            AdapterView.OnItemClickListener clicked = new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    productos.setSelection(position);
                    productoBuscado.setVisibility(View.GONE);
                    listPopUp.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    productos.setEnabled(true);
                }
            };
            productos.setOnItemSelectedListener(selected);
            listPopUp.setOnItemClickListener(clicked);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, arregloProductos);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            productos.setAdapter(adapter);
            listPopUp.setAdapter(adapter);
            InicioActivity.Toast(actividad, "Por favor ingresa una Clave o Descripcion");
        }
    }

    private void cargarProductos() {
        Cliente cliente;// = actividad.getCliente();
        for (int i = 0; i < InicioActivity.getListaProductos().size(); i++) {
            if (claveproducto == 1)
                arregloProductos[i] = InicioActivity.getListaProductos().get(i).getClave() + " - " + InicioActivity.getListaProductos().get(i).toString();
            else if (claveproducto == 0)
                arregloProductos[i] = InicioActivity.getListaProductos().get(i).toString();

            cliente = actividad.getCliente();
            int numPrecio = 1;

            if((cliente.getNumPrecio())>0)
                numPrecio = cliente.getNumPrecio();

            arregloDePrecios[i] = InicioActivity.getListaProductos().get(i).getPrecios().get((numPrecio)-1);

            if (InicioActivity.getListaProductos().get(i).getExistencia() == null)
                arregloExistenci[i] = (double) -1000000;
            else
                arregloExistenci[i] = InicioActivity.getListaProductos().get(i).getExistencia();

            for (ListaPrecios preciosN: InicioActivity.getListaPreciosNegociados()) {
                if(preciosN.getCliente().compareToIgnoreCase(cliente.getClave())==0)
                    if(preciosN.getProducto().compareToIgnoreCase(InicioActivity.getListaProductos().get(i).getClave())==0)
                    {
                        arregloDePrecios[i] = preciosN.getPrecio();
                    }
            }
            if(InicioActivity.getListaProductos().get(i).getPrecios().size()>5) {
                arregloDePSugerido[i] = InicioActivity.getListaProductos().get(i).getPrecios().get(5);
            }else{
                arregloDePSugerido[i] = arregloDePrecios[i];
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, arregloProductos);
        productos.setAdapter(adapter);
        listPopUp.setAdapter(adapter);
        OnItemSelectedListener selected = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!actividad.cambio) {
                    precib.setTextColor(getResources().getColor(R.color.darkgrey));
                    precib.setEnabled(true);
                    preciosto.setVisibility(View.GONE);
                    precio.setText(arregloDePrecios[position].toString());
                    psugerido.setText(arregloDePSugerido[position].toString());

                    if(precioUni.length()>1)
                        precio.setText(precioUni);
                }

                if (existencias == 1 && arregloExistenci[position] != null) {
                    if (arregloExistenci[position] == -1000000) {
                        existen.setText("SIN");
                        InicioActivity.Toast(actividad, "Existencia no registrada");
                    } else {
                        existen.setText(Double.toString(arregloExistenci[position]));
                        existenciaCambian = arregloExistenci[position];
                    }
                } else {
                    existen.setText("SIN");
                    InicioActivity.Toast(actividad, "Existencia no registrada");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        };
        AdapterView.OnItemClickListener clicked = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productos.setSelection(position);
                productoBuscado.setVisibility(View.GONE);
                listPopUp.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                productos.setEnabled(true);
            }
        };
        productos.setOnItemSelectedListener(selected);
        listPopUp.setOnItemClickListener(clicked);
        //listPopUp.setOnItemSelectedListener(selected);
    }
    Double PrecioNegociado = 0.0;
    public void cargarPrecios() {
        try {

            n = 0;
            Producto producto = InicioActivity.getListaProductos().get(getIndexProducto(productos.getSelectedItem().toString()));
            if(producto == null) return;
            final List<String> ListaPreciosProducto = new ArrayList<String>();
            if (!precio.getText().toString().isEmpty())
                ListaPreciosProducto.add(n, precio.getText().toString());
            else
                ListaPreciosProducto.add(n, Double.toString(producto.getPrecios().get(actividad.getCliente().getNumPrecio() - 1)));
            if(!(InicioActivity.getListaPreciosNegociados().isEmpty())) {
                PrecioNegociado = 0.0;
                for (ListaPrecios preciosN : InicioActivity.getListaPreciosNegociados()) {
                    if (preciosN.getCliente().compareToIgnoreCase(actividad.getCliente().getClave()) == 0)
                        if (preciosN.getProducto().compareToIgnoreCase(producto.getClave()) == 0) {
                            n++;
                            ListaPreciosProducto.add(n, "N-" + NumberFormat.getCurrencyInstance(Locale.US).format(preciosN.getPrecio()));
                            PrecioNegociado = preciosN.getPrecio();
                        }
                }
            }
            n++;
            ListaPreciosProducto.add(n,"1-" + NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecios().get(0)));
            n++;
            ListaPreciosProducto.add(n,"2-" + NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecios().get(1)));
            n++;
            ListaPreciosProducto.add(n,"3-" + NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecios().get(2)));
            n++;
            ListaPreciosProducto.add(n,"4-" + NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecios().get(3)));
            n++;
            ListaPreciosProducto.add(n,"5-" + NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecios().get(4)));
            if(producto.getPrecios().size()>5) {
                n++;
                ListaPreciosProducto.add(n, "S-" + NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecios().get(5)));
            }
            //******************************************************************//
            ////////////////PRECIOS ADICIONALES //////////////////////////////////
            //******************************************************************//
            for (PreciosAdicionales precioAdicional: InicioActivity.getListaPreciosAdicionales()) {
                if(producto.getClave().compareToIgnoreCase(precioAdicional.getProducto())==0){
                    n++;
                    ListaPreciosProducto.add(n, precioAdicional.getNumPrecio() + "-" + NumberFormat.getCurrencyInstance(Locale.US).format(precioAdicional.getPrecio()));
                }
            }
            //******************************************************************//
            //******************************************************************//

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                    android.R.layout.simple_dropdown_item_1line, ListaPreciosProducto);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            preciosto.setAdapter(adapter);

            preciosto.setOnItemSelectedListener(new OnItemSelectedListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    preciosto.setVisibility(despliegue);
                    precionum=position;
                    if (despliegue == 8) {
                        String precioSelecto = preciosto.getSelectedItem().toString();
                        precioSelecto = precioSelecto.substring(precioSelecto.indexOf("$")+1);
                        precio.setText(precioSelecto);
                        precib.setTextColor(getResources().getColor(R.color.darkgrey));
                        precio.setVisibility(View.VISIBLE);
                        precib.setEnabled(true);
                    }
                    despliegue = 8;

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            preciosto.performClick();
        }catch(Exception e){
            e.getMessage();
        }
    }
    int getIndexProducto(String busqueda){
        posicionproducto = -1;

        String seleccionado = busqueda;
        // Eliminamos las tabulaciones y espacios adicionales
        seleccionado = seleccionado.replaceAll("\t", "").replaceAll(" ", "");

        for (Producto productoSearch : InicioActivity.getListaProductos()) {
            String cadenaBusqueda;
            if (claveproducto == 1) {
                cadenaBusqueda = productoSearch.getClave() + "-" + productoSearch.getDescripcion();
            } else {
                cadenaBusqueda = productoSearch.getDescripcion();
            }
            // Limpiamos la cadena de búsqueda similar a la cadena seleccionada
            cadenaBusqueda = cadenaBusqueda.replaceAll(" ", "");
            if (seleccionado.equals(cadenaBusqueda)) {
                posicionproducto = InicioActivity.getListaProductos().indexOf(productoSearch);
                break;  // Rompemos el ciclo si encontramos una coincidencia
            }
        }
        if(posicionproducto < 0){
            InicioActivity.Toast(actividad, "Hay un problema con el producto seleccionado, intentalo de nuevo");
            return -1;
        }
        return posicionproducto;
    }
}
