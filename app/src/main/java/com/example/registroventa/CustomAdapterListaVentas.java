package com.example.registroventa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.registroventa.models.Venta;

import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import static com.example.registroventa.ListaVentasActivity.getListaVentas;

public class CustomAdapterListaVentas extends ArrayAdapter<String> {
      private Context context;
      private List<String> valores;
      private FragmentManager supportFragmentManager;

   public CustomAdapterListaVentas(Context context, int resource, List<String> objetos, FragmentManager supportFragmentManager ) {
         super(context, resource, objetos);
         this.context = context;
         this.valores = objetos;
            this.supportFragmentManager = supportFragmentManager;
   }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
         LayoutInflater inflater = LayoutInflater.from(context);
         View customView = inflater.inflate(R.layout.lista_item_layout, parent, false);

         TextView textView = (TextView) customView.findViewById(R.id.Productitulo);
         ImageButton button = (ImageButton) customView.findViewById(R.id.buttonItem);

         if (InicioActivity.impresiones.getmostrarImpresion() == 1) button.setVisibility(View.VISIBLE);
         else button.setVisibility(View.INVISIBLE);
         textView.setText(valores.get(position));
         textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String item = valores.get(position);
               Venta venta = null;
               for (Venta ventax : getListaVentas()) {
                  if (item.equals(ventax.toString())) {
                     ListaVentasActivity.
                             Ventaseleccionada = position;
                     ListaVentasActivity.venta = ventax;
                     venta = ventax;
                     break;
                  }
               }
               if (venta != null) {
                  Intent intent = new Intent(context, VentaActivity.class);
                  context.startActivity(intent);
               }
               // Acción del botón
            }
         });
         button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String item = valores.get(position);
               Venta venta = null;
               for (Venta ventax : getListaVentas()) {
                  if (item.equals(ventax.toString())) {
                     venta = ventax;
                     break;
                  }
               }
               if (venta != null) {
                  DialogFragment dialogoimprimir = new DialogoPrint(venta);
                  dialogoimprimir.show(supportFragmentManager, "Imprimir venta");
               }
               // Acción del botón
            }
         });

         return customView;
      }
   }