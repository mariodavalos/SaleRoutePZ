package com.example.registroventa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Looper;
import androidx.fragment.app.DialogFragment;
import androidx.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.registroventa.models.Impresorayencabezado;
import com.example.registroventa.models.Venta;
import com.example.registroventa.models.VentaProducto;
import com.zebra.android.comm.BluetoothPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebra.android.discovery.DiscoveryHandler;
import com.zebra.android.printer.ZebraPrinter;
import com.zebra.android.printer.ZebraPrinterFactory;
import com.zebra.android.printer.ZebraPrinterLanguageUnknownException;

import java.text.NumberFormat;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class DialogoPrint extends DialogFragment {
    public static String macAddress;
    public static Venta ticket_productos = null;
    private static DiscoveryHandler btDiscoveryHandler = null;
    public Impresorayencabezado MacEncabezado;
    int Renglon = 150;
    private Spinner impresorasencon;
    private VentaActivity actividad;
    private Activity actividadlocal;
    private int claveproducto = 0, existencias = 0;
    private Bitmap ticket;
    String dataToPrint="$intro$";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        actividad = (VentaActivity) this.getActivity();
        actividadlocal = this.getActivity();
        MacEncabezado = InicioActivity.impresiones;
        macAddress = MacEncabezado.getmacAdress();
        String[] impresorae = new String[1];
        impresorae[0] = MacEncabezado.getnombreImpresora();
        ticket_productos = actividad.getventas();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_printer, null);
        impresorasencon = (Spinner) view.findViewById(R.id.ImpresorasEncontradas);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, impresorae);
        impresorasencon.setAdapter(adapter);
        builder.setView(view)
                .setPositiveButton("Imprimir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //doPhotoPrint(ticket);
                        //String dataToPrint="";//"$big$This is a printer test$intro$posprinterdriver.com$intro$$intro$$cut$$intro$";
                        //dataToPrint=dataToPrint+dataToPrint+dataToPrint+dataToPrint+dataToPrint+dataToPrint+dataToPrint+dataToPrint+dataToPrint+dataToPrint;
                        Intent intentPrint = new Intent();

                        intentPrint.setAction(Intent.ACTION_SEND);
                        intentPrint.putExtra(Intent.EXTRA_TEXT, dataToPrint);
                        intentPrint.setPackage("com.fidelier.posprinterdriver");
                        /*String bitmapPath = MediaStore.Images.Media.insertImage(actividad.getContentResolver(), ticket,"title", null);
                        Uri bitmapUri = Uri.parse(bitmapPath);
                        intentPrint.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                        intentPrint.setType("image/jpeg");*/
                        intentPrint.setType("text/plain");

                        startActivity(intentPrint);

                        //printFile(ticket);

                      actividad.cancelarOnClick(null);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        actividad.cancelarOnClick(null);
                    }
                });

        this.setStyle(STYLE_NORMAL, android.R.style.Theme_Light);
        //////////////CREACION DEL TICKET  INICIO////////////////////////////////
        for (VentaProducto producto : ticket_productos.getVentaProductos()) {
            if (producto.getProducto().toString().length() > 38) Renglon += 20;
            Renglon += 50;
        }
        Renglon += 250;
        Paint paint = new Paint();
        Bitmap b = Bitmap.createBitmap(600, Renglon, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        c.drawRect(0, 0, 600, Renglon, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        paint.setTextSize(20);
        paint.setTextScaleX(1.f);
        paint.setColor(Color.BLACK);
        paint.setAlpha(255);
        paint.setAntiAlias(true);
        Renglon = 210;
        int Sangria = 40;
        int product = 1;
        int cantidad = 0;
        Double total = 0.0;
        /////////////ENCABEZADO INICIO/////////////////////
        dataToPrint= dataToPrint+(MacEncabezado.getencabezado1().length()>30?MacEncabezado.getencabezado1().substring(0,30):MacEncabezado.getencabezado1())+"$intro$";
        dataToPrint= dataToPrint+(MacEncabezado.getencabezado2().length()>30?MacEncabezado.getencabezado2().substring(0,30):MacEncabezado.getencabezado2())+"$intro$";
        dataToPrint= dataToPrint+(MacEncabezado.getencabezado3().length()>30?MacEncabezado.getencabezado3().substring(0,30):MacEncabezado.getencabezado3())+"$intro$";

        c.drawText(MacEncabezado.getencabezado1(), Sangria + 35, Renglon - 160, paint);
        c.drawText(MacEncabezado.getencabezado2(), Sangria + 35, Renglon - 130, paint);
        c.drawText(MacEncabezado.getencabezado3(), Sangria + 35, Renglon - 100, paint);
        /////////////ENCABEZADO FIN///////////////////////
        dataToPrint= dataToPrint+"-----------------------------"+"$intro$";
        /////////////ENCABEZADO INICIO/////////////////////
        dataToPrint= dataToPrint+"Cliente: "+ticket_productos.getCliente()+"$intro$";
        dataToPrint= dataToPrint+"Vendedor: "+ticket_productos.getVendedor().getNombre()+"$intro$";
        dataToPrint= dataToPrint+"-----------------------------"+"$intro$";
        c.drawText("Cliente: "+ticket_productos.getCliente(), Sangria + 35, Renglon - 60, paint);
        c.drawText("Vendedor: "+ticket_productos.getVendedor().getNombre(), Sangria + 35, Renglon - 30, paint);
        /////////////ENCABEZADO FIN///////////////////////
        c.drawText("Fecha: ", Sangria + 210, Renglon , paint);
        c.drawText(ticket_productos.getFecha().toLocaleString(), Sangria + 290, Renglon, paint);
        c.drawText("Cantidad", Sangria + 45, Renglon + 40, paint);
        c.drawText("Precio", Sangria + 190, Renglon + 40, paint);
        c.drawText("Total", Sangria + 360, Renglon + 40, paint);

        dataToPrint= dataToPrint+"Fecha: "+ticket_productos.getFecha().toLocaleString()+"$intro$";
        dataToPrint= dataToPrint+"-----------------------------"+"$intro$";
        dataToPrint= dataToPrint+"Cantidad     Precio     Total"+"$intro$";
        dataToPrint= dataToPrint+"-----------------------------"+"$intro$";
        ///////////PRODUCTOS INICIO//////////////////////////
        Renglon+=90;
        for (VentaProducto producto : ticket_productos.getVentaProductos()) {
            if (producto.getProducto().toString().length() > 31) {
                c.drawText(producto.getProducto().toString().substring(0, 30), Sangria + 45, Renglon, paint);
                dataToPrint= dataToPrint+producto.getProducto().toString().substring(0, 30)+"$intro$";
                Renglon += 20;
                c.drawText(producto.getProducto().toString().substring(30, producto.getProducto().toString().length()), Sangria + 45, Renglon, paint);
                if (producto.getProducto().toString().length() > 61) {
                    dataToPrint = dataToPrint + producto.getProducto().toString().substring(30, 60) + "$intro$";
                    dataToPrint = dataToPrint + producto.getProducto().toString().substring(60) + "$intro$";
                }else{
                    dataToPrint = dataToPrint + producto.getProducto().toString().substring(30) + "$intro$";
                }
            } else {
                c.drawText(producto.getProducto().toString(), Sangria + 45, Renglon, paint);
                dataToPrint = dataToPrint + producto.getProducto().toString() + "$intro$";
            }
            String space = "          ";
            dataToPrint= dataToPrint+String.format("%.2f",producto.getCantidad())+space.substring(String.format("%.2f",producto.getCantidad()).length());
            dataToPrint= dataToPrint+NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecioUnitario())+
                    space.substring(NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecioUnitario()).length());
            dataToPrint= dataToPrint+NumberFormat.getCurrencyInstance(Locale.US).format(producto.getTotal())+"$intro$";
            c.drawText(Double.toString(producto.getCantidad()), Sangria + 45, Renglon + 20, paint);
            c.drawText(NumberFormat.getCurrencyInstance(Locale.US).format(producto.getPrecioUnitario()), Sangria + 190, Renglon + 20, paint);
            c.drawText(NumberFormat.getCurrencyInstance(Locale.US).format(producto.getTotal()), Sangria + 360, Renglon + 20, paint);

            Renglon += 50;
            cantidad += producto.getCantidad();
            total += producto.getTotal();
        }
        ////////////PRODUCTOS FIN///////////////////////////
        c.drawLine(Sangria + 40, Renglon + 5, 520, Renglon + 2, paint);
        c.drawLine(Sangria + 40, Renglon + 6, 520, Renglon + 3, paint);
        c.drawLine(Sangria + 40, Renglon + 7, 520, Renglon + 4, paint);
        c.drawLine(Sangria + 40, Renglon + 5, 520, Renglon + 5, paint);
        dataToPrint= dataToPrint+"-----------------------------"+"$intro$";
        dataToPrint= dataToPrint+"Productos:        ";
        dataToPrint= dataToPrint+Integer.toString(cantidad)+"$intro$";
        dataToPrint= dataToPrint+"Total "+(ticket_productos.isMetodo()?"Contado":"Crédito")+": "+NumberFormat.getCurrencyInstance(Locale.US).format(total)+"$intro$";
        dataToPrint= dataToPrint+"-----------------------------";
        c.drawText("Productos", Sangria + 45, Renglon + 25, paint);
        c.drawText("Total", Sangria + 360, Renglon + 25, paint);

        dataToPrint= dataToPrint+"$intro$$intro$$intro$$cut$$intro$";
        c.drawText(Integer.toString(cantidad), Sangria + 45, Renglon + 45, paint);
        c.drawText(NumberFormat.getCurrencyInstance(Locale.US).format(total), Sangria + 360, Renglon + 45, paint);
        ticket = b;
        ImageView tickimagen = (ImageView) view.findViewById(R.id.imageView1);
        Bitmap imagen;

        if (Renglon > 2040)
            imagen = Bitmap.createScaledBitmap(ticket, 600, 2041, true);
        else
            imagen = Bitmap.createScaledBitmap(ticket, 600, Renglon, true);
        ticket = Bitmap.createScaledBitmap(ticket, 1300, Renglon * 2, true);
        tickimagen.setImageBitmap(ticket);

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
            }
        });
        return dialog;

    }
    private void doPhotoPrint(Bitmap bitmap) {
        PrintHelper photoPrinter = new PrintHelper(getActivity());
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("Ticket", bitmap,new PrintHelper.OnPrintFinishCallback() {
            @Override
            public void onFinish() {
                actividad.imprimirOnClick(null);
            }
        });
    }
    private void onClickBusqueda() {
                            InicioActivity.Toast(this.getActivity(), "Buscando...");
        //macAddress = actividad.impresoraencontrada();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        String[] impresorae = new String[1];
        impresorae[0] = macAddress;
        if (impresorae[0] == null) {
            impresorae[0] = "No se encontro impresora";
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, impresorae);
        impresorasencon.setAdapter(adapter);

        if (impresorae[0] == "No se encontro impresora") {
                    InicioActivity.Toast(this.getActivity(), "No se encontraron impresoras");
        } else {
                    InicioActivity.Toast(this.getActivity(), "Impresora encontrada");
        }
    }

    private void printFile(final Bitmap bitmap) {
                            InicioActivity.Toast(actividadlocal, "Enviando impresion...");
                            InicioActivity.Toast(actividadlocal, "Impresion enviada");
                            InicioActivity.Toast(actividadlocal, "Imprimiendo...");
        new Thread(new Runnable() {
            @SuppressWarnings("deprecation")
            public void run() {
                ZebraPrinterConnection connection = null;
                try {

                    Looper.prepare();
                    connection = getZebraPrinterConn();
                    connection.open();
                    ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);

                    printer.getGraphicsUtil().printImage(bitmap, 0, 0, 400, Renglon, false);

                    //connection.close();


                } catch (ZebraPrinterConnectionException e1) {
                    InicioActivity.Toast(actividadlocal, "Ocurrio un error en la impresion...");
                    InicioActivity.Toast(actividadlocal, "Ocurrio un error en la impresion...");
                    // try {
                    //	//connection.close();
                    //} catch (ZebraPrinterConnectionException e) {
                    // TODO Auto-generated catch block
                    //	e.printStackTrace();
                    //}
                } catch (ZebraPrinterLanguageUnknownException e2) {

                    InicioActivity.Toast(actividadlocal, "Ocurrio un error en la impresion...");
                    InicioActivity.Toast(actividadlocal, "Ocurrio un error en la impresion...");
                    //try {
                    //connection.close();
                    //} catch (ZebraPrinterConnectionException e) {
                    // TODO Auto-generated catch block
                    //	e.printStackTrace();
                    //}
                } finally {
                    Looper.myLooper().quit();
                    try {
                        connection.close();
                    } catch (ZebraPrinterConnectionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    @SuppressWarnings("deprecation")
    private ZebraPrinterConnection getZebraPrinterConn() {
        return new BluetoothPrinterConnection(macAddress);
    }
}