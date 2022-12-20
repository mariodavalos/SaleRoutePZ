package com.example.registroventa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
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
    private String clienteseleccionado;
    private Cliente clienteseleccionadoCliente;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        actividad = (VentaActivity) this.getActivity();
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.dialog_visualizar_cliente, null);
        builder.setView(view)
                .setPositiveButton("Visualizacion terminada", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
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

        nombre.setTextColor(getResources().getColor(R.color.orange));
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

        nombre.setTextSize(14);
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

        //List<Cliente> clientes = InicioActivity.getListaClientes();
        Cliente seleccionarcliente = actividad.clienteseleccionadoCliente;
        Titulo.setText("Datos del cliente " + seleccionarcliente.getClave());
        Titulo.setTextColor(getResources().getColor(R.color.orange));
        nombre.setText(seleccionarcliente.getNombre());
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


        return dialog;
    }
}
