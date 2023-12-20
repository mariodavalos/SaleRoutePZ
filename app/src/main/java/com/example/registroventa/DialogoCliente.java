package com.example.registroventa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.registroventa.models.Cliente;

public class DialogoCliente extends DialogFragment {
    private VentaActivity actividad;
    private TextView Titulo;
    private EditText nombre;
    private EditText atencion;
    private EditText calle;
    private EditText numEx;
    private EditText numIn;
    private EditText colonia;
    private EditText CP;
    private EditText ciudad;
    private EditText estado;
    private EditText telefono1;
    private EditText telefono2;
    private EditText telefono3;
    private EditText telefono4;
    private EditText diascredito;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        actividad = (VentaActivity) this.getActivity();
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.dialog_visualizar_cliente, null);
        builder.setView(view);
        if(actividad.nuevoCliente){
            builder.setPositiveButton("Guardar nuevo cliente", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id){
                    if(nombre.getText().toString().isEmpty()){
                        nombre.setError("El nombre es obligatorio");
                        InicioActivity.Toast(actividad,"No se puede agregar un cliente sin nombre");
                        return;
                    }
                    actividad.guardarNuevoClienteOnClick();
                }
            });
        }else {
            builder.setPositiveButton("Visualizacion terminada", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button possitive = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_POSITIVE);
                possitive.setTextColor(Color.parseColor("#e18a33"));

            }
        });
        Titulo = (TextView) view.findViewById(R.id.TituloVisualizacion);

        nombre = (EditText) view.findViewById(R.id.Nombretext);
        atencion = (EditText) view.findViewById(R.id.Atenciontext);
        calle = (EditText) view.findViewById(R.id.Calletext);
        numEx = (EditText) view.findViewById(R.id.Numextext);
        numIn = (EditText) view.findViewById(R.id.Numintext);
        colonia = (EditText) view.findViewById(R.id.coloniatext);
        CP = (EditText) view.findViewById(R.id.cptext);
        ciudad = (EditText) view.findViewById(R.id.ciudadtext);
        estado = (EditText) view.findViewById(R.id.estadotext);
        telefono1 = (EditText) view.findViewById(R.id.telefono1text);
        telefono2 = (EditText) view.findViewById(R.id.telefono2text);
        telefono3 = (EditText) view.findViewById(R.id.telefono3text);
        telefono4 = (EditText) view.findViewById(R.id.telefono4text);
        diascredito = (EditText) view.findViewById(R.id.Diascreditotext);

        nombre.setTextColor(getResources().getColor(R.color.orange));
        atencion.setTextColor(getResources().getColor(R.color.orange));
        calle.setTextColor(getResources().getColor(R.color.orange));
        numEx.setTextColor(getResources().getColor(R.color.orange));
        numIn.setTextColor(getResources().getColor(R.color.orange));
        colonia.setTextColor(getResources().getColor(R.color.orange));
        CP.setTextColor(getResources().getColor(R.color.orange));
        ciudad.setTextColor(getResources().getColor(R.color.orange));
        estado.setTextColor(getResources().getColor(R.color.orange));
        telefono1.setTextColor(getResources().getColor(R.color.orange));
        telefono2.setTextColor(getResources().getColor(R.color.orange));
        telefono3.setTextColor(getResources().getColor(R.color.orange));
        telefono4.setTextColor(getResources().getColor(R.color.orange));
        diascredito.setTextColor(getResources().getColor(R.color.orange));

        nombre.setTextSize(14);
        atencion.setTextSize(14);
        calle.setTextSize(14);
        numEx.setTextSize(14);
        numIn.setTextSize(14);
        colonia.setTextSize(14);
        CP.setTextSize(14);
        ciudad.setTextSize(14);
        estado.setTextSize(14);
        telefono1.setTextSize(14);
        telefono2.setTextSize(14);
        telefono3.setTextSize(14);
        telefono4.setTextSize(14);
        diascredito.setTextSize(14);

        //List<Cliente> clientes = InicioActivity.getListaClientes();
        Cliente seleccionarcliente = actividad.clienteseleccionadoCliente;
        Titulo.setText("Datos del cliente " + seleccionarcliente.getClave());
        Titulo.setTextColor(getResources().getColor(R.color.orange));
        nombre.setText(seleccionarcliente.getNombre());
        atencion.setText(seleccionarcliente.getAtencion());
        calle.setText(seleccionarcliente.getCalle());
        numEx.setText(seleccionarcliente.getNumEx());
        numIn.setText(seleccionarcliente.getNumIn());
        colonia.setText(seleccionarcliente.getColonia());
        CP.setText(seleccionarcliente.getCP());
        ciudad.setText(seleccionarcliente.getCiudad());
        estado.setText(seleccionarcliente.getEstado());
        telefono1.setText(seleccionarcliente.getTelefono1());
        telefono2.setText(seleccionarcliente.getTelefono2());
        telefono3.setText(seleccionarcliente.getTelefono3());
        telefono4.setText(seleccionarcliente.getTelefono4());
        diascredito.setText(String.valueOf(seleccionarcliente.getDiasCredito()));

        if(actividad.nuevoCliente){
            nombre.setEnabled(true);nombre.setFocusable(true);nombre.setFocusableInTouchMode(true);nombre.setClickable(true);
            atencion.setEnabled(true);atencion.setFocusable(true);atencion.setFocusableInTouchMode(true);atencion.setClickable(true);
            calle.setEnabled(true);calle.setFocusable(true);calle.setFocusableInTouchMode(true);calle.setClickable(true);
            numEx.setEnabled(true);numEx.setFocusable(true);numEx.setFocusableInTouchMode(true);numEx.setClickable(true);
            numIn.setEnabled(true);numIn.setFocusable(true);numIn.setFocusableInTouchMode(true);numIn.setClickable(true);
            colonia.setEnabled(true);colonia.setFocusable(true);colonia.setFocusableInTouchMode(true);colonia.setClickable(true);
            CP.setEnabled(true);CP.setFocusable(true);CP.setFocusableInTouchMode(true);CP.setClickable(true);
            ciudad.setEnabled(true);ciudad.setFocusable(true);ciudad.setFocusableInTouchMode(true);ciudad.setClickable(true);
            estado.setEnabled(true);estado.setFocusable(true);estado.setFocusableInTouchMode(true);estado.setClickable(true);
            telefono1.setEnabled(true);telefono1.setFocusable(true);telefono1.setFocusableInTouchMode(true);telefono1.setClickable(true);
            telefono2.setEnabled(true);telefono2.setFocusable(true);telefono2.setFocusableInTouchMode(true);telefono2.setClickable(true);
            telefono3.setEnabled(true);telefono3.setFocusable(true);telefono3.setFocusableInTouchMode(true);telefono3.setClickable(true);
            telefono4.setEnabled(true);telefono4.setFocusable(true);telefono4.setFocusableInTouchMode(true);telefono4.setClickable(true);
            diascredito.setEnabled(true);diascredito.setFocusable(true);diascredito.setFocusableInTouchMode(true);diascredito.setClickable(true);

            nombre.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setNombre(nombre.getText().toString());
                }
            });
            atencion.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setAtencion(atencion.getText().toString());
                }
            });
            calle.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setCalle(calle.getText().toString());
                }
            });
            numEx.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setNumEx(numEx.getText().toString());
                }
            });
            numIn.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setNumIn(numIn.getText().toString());
                }
            });
            colonia.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setColonia(colonia.getText().toString());
                }
            });
            CP.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setCP(CP.getText().toString());
                }
            });
            ciudad.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {}
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setCiudad(ciudad.getText().toString());
                }
            });
            estado.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s){}
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setEstado(estado.getText().toString());
                }
            });
            telefono1.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s){}
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setTelefono1(telefono1.getText().toString());
                }
            });
            telefono2.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s){}
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setTelefono2(telefono2.getText().toString());
                }
            });
            telefono3.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s){}
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setTelefono3(telefono3.getText().toString());
                }
            });
            telefono4.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s){}
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    actividad.clienteseleccionadoCliente.setTelefono4(telefono4.getText().toString());
                }
            });
            diascredito.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s){}
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String valor = diascredito.getText().toString();
                    int dias;
                    try {
                        dias = Integer.parseInt(valor);
                    } catch (NumberFormatException e) {
                        dias = 0;
                    }
                    actividad.clienteseleccionadoCliente.setDiasCredito(dias);
                }
            });
        }

        return dialog;
    }
}
