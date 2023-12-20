package com.example.registroventa;

import android.annotation.SuppressLint;
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
import android.widget.Spinner;

public class DialogoBuscarCliente extends DialogFragment {
    int numerode_clientes = InicioActivity.getListaClientes().size();
    //String arregloCliente[] = new String[numerode_clientes];
    String arregloBuscado[] = new String[numerode_clientes];
    int arregloBuscadoID[] = new int[numerode_clientes];

    private Spinner clientes;
    private VentaActivity actividad;
    private EditText clienteBuscado;

    public void actualizarArreglosClientes(){
        numerode_clientes = InicioActivity.getListaClientes().size();
        //String arregloCliente[] = new String[numerode_clientes];
        arregloBuscado = new String[numerode_clientes];
        arregloBuscadoID = new int[numerode_clientes];
    }

    @SuppressLint("MissingInflatedId")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        actividad = (VentaActivity) this.getActivity();
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.dialog_busqueda_cliente, null);
        builder.setView(view)
                .setPositiveButton("Buscar por nombre", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        onClickBusqueda(false);
                    }
                })
                .setNegativeButton("Buscar por clave", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onClickBusqueda(true);
                    }
                }).setNeutralButton("Agregar nuevo cliente", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                actividad.visualizarNuevoClienteOnClick();
            }
        })
        .setTitle("Buscar cliente");
        setClientes((Spinner) view.findViewById(R.id.listaClientes));
        clienteBuscado = (EditText) view.findViewById(R.id.ClienteBusqueda);
        AlertDialog dialog = builder.create();
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
        return dialog;
    }

    public void onClickBusqueda(Boolean clave) {
        //arregloCliente = new String[numerode_clientes];
        arregloBuscado = new String[numerode_clientes];
        arregloBuscadoID = new int[numerode_clientes];
        String[] buscado = clienteBuscado.getText().toString().toUpperCase().split(" ");
        //for (int i = 0; i < InicioActivity.getListaClientes().size(); i++) {
        //	arregloCliente[i] = InicioActivity.getListaClientes().get(i).toString();
        //}
        int e = 0;
        for (int i = 0; i < numerode_clientes; i++) {
            int palabraencontrada = 0;
            String clienteNombre = InicioActivity.getListaClientes().get(i).getNombre().toUpperCase();
            String clienteClave = InicioActivity.getListaClientes().get(i).getClave().toUpperCase();
            for (String buscador : buscado) {
                if (!clave && (clienteNombre.indexOf(buscador) >= 0))
                    palabraencontrada++;
                if (clave && (clienteClave.indexOf(buscador) >= 0))
                    palabraencontrada++;
            }
            if (palabraencontrada == buscado.length) {
                arregloBuscado[e] = InicioActivity.getListaClientes().get(i).toString();
                arregloBuscadoID[e] = i;
                e++;
            }
        }
        int f = 1;
        if (e > 0)
            f = e;
        String[] arreglofinal = new String[f];
        int[] arreglofinalID = new int[f];
        for (int i = 0; i < e; i++) {
            arreglofinal[i] = arregloBuscado[i];
            arreglofinalID[i] =  arregloBuscadoID[i];
        }
        if (e == 0) {
            InicioActivity.Toast(actividad, "No se encontro coincidencia");
            arreglofinal[0] = "NINGUNO";
        } else if (clienteBuscado.getText().toString().length() < 1)
            InicioActivity.Toast(actividad, "Ingrese busqueda");
        arregloBuscado = arreglofinal;
        arregloBuscadoID = arreglofinalID;
        actividad.EncontroClientes();
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
		android.R.layout.simple_dropdown_item_1line, arreglofinal);
		adapter.setDropDownViewResource(android . R . layout.simple_dropdown_item_1line);
		clientes.setAdapter(adapter);*/
        //productoBuscado.setText(arregloProductos[2]);
    }

    public String[] clientesEncontrados() {
        return arregloBuscado;
    }
    public int[] clientesEncontradosID() {
        return arregloBuscadoID;
    }
    public Spinner getClientes() {
        return clientes;
    }

    public void setClientes(Spinner clientes) {
        this.clientes = clientes;
    }
}
