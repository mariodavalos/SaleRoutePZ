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
import android.widget.NumberPicker;


public class DialogoLimitar extends DialogFragment {

    private NumberPicker numero;
    private ConfiguracionActivity actividad;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        actividad = (ConfiguracionActivity) this.getActivity();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        View view = inflater.inflate(R.layout.cantidad, null);


        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Limitar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        actividad.setLimite(numero.getValue());
                    }
                });

        AlertDialog dialog = builder.create();
        Button possitive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        possitive.setTextColor(Color.parseColor("#e18a33"));
        //this.setStyle(STYLE_NORMAL, android.R.style.Theme_Light);
        numero = (NumberPicker) view.findViewById(R.id.cantidadlimite);
        numero.setMaxValue(100);
        numero.setMinValue(0);
        numero.setValue(actividad.Limite);
        return builder.create();
    }

}	