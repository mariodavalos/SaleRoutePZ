package com.example.registroventa.services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.annotation.NonNull;

import com.example.registroventa.models.Cliente;
import com.example.registroventa.models.Configura;
import com.example.registroventa.models.Cuentas;
import com.example.registroventa.models.Impresorayencabezado;
import com.example.registroventa.models.Producto;
import com.example.registroventa.models.Vendedor;
import com.example.registroventa.models.Venta;
import com.example.registroventa.models.VentaProducto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseDatosHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public BaseDatosHelper(Context context, String nombre,
                           CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        crearBD(db);
        //agregarnuevaConfiguracion();
    }

    private void crearBD(SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE Vendedor (nombre TEXT, clave TEXT)");
        //db.execSQL("CREATE TABLE Producto (clave TEXT, descripcion TEXT, precio1 REAL, precio2 REAL, precio3 REAL)");
        //db.execSQL("CREATE TABLE Cliente (clave TEXT, nombre TEXT, noprecio INTEGER)");
        try {
            //db.execSQL("DROP TABLE IF EXISTS CuentasXC");
            db.execSQL("CREATE TABLE CuentasXC (Archivo TEXT, Fecha TEXT, Cliente TEXT, Numero INTEGER, Serie TEXT, Id INTEGER, Cantidad TEXT, Return TEXT)");
            db.execSQL("CREATE TABLE Venta (id INTEGER, fecha TEXT, idcliente TEXT, idvendedor TEXT, enviada INTEGER, metodo INTEGER, nota TEXT)");
            db.execSQL("CREATE TABLE VentaProducto (idventa INTEGER, idproducto TEXT, cantidad TEXT, precio TEXT, precionum TEXT)");
            db.execSQL("CREATE TABLE Configuracion (ftp_ip TEXT, botones INTEGER, clave INTEGER, existencia INTEGER)");
            db.execSQL("CREATE TABLE Impresioninfo (renglon1 TEXT, renglon2 TEXT, renglon3 TEXT,macAdress TEXT, nombre TEXT, impresion INTEGER)");
        }catch (Exception e)
        {
            e.toString();
        }
//		agregarnuevaConfiguracion();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior,
                          int versionNueva) {
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS Vendedor");
        //db.execSQL("DROP TABLE IF EXISTS Producto");
        //db.execSQL("DROP TABLE IF EXISTS Cliente");
//        db.execSQL("DROP TABLE IF EXISTS Venta");
//        db.execSQL("DROP TABLE IF EXISTS VentaProducto");
//        db.execSQL("DROP TABLE IF EXISTS Configuracion");
//        db.execSQL("DROP TABLE IF EXISTS Impresioninfo");
//        db.execSQL("DROP TABLE IF EXISTS CuentasXC");
        db.execSQL("ALTER TABLE Venta ADD COLUMN nota TEXT DEFAULT ''");
        // Se crea la nueva versi�n de la tabla
//        crearBD(db);
        //agregarnuevaConfiguracion();
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);

    }

    public void Abrir() {
        db = //this.getWritableDatabase();
                this.getReadableDatabase();
       // db.execSQL("ALTER TABLE foo ADD COLUMN  INTEGER DEFAULT 0");
    }

    public void Cerrar() {
        db.close();
        db = null;
    }

    /*public void limpiarVendedores(List<Vendedor> vendedores) {
        Abrir();
        String cadSQL = String
                .format("DELETE from Vendedor");
        db.execSQL(cadSQL);
        for(Vendedor vendedor : vendedores)
            agregarVendedor(vendedor);
        Cerrar();
    }
    public void limpiarClientes(List<Cliente> clientes) {
        Abrir();
        String cadSQL = String
                .format("DELETE from Cliente");
        db.execSQL(cadSQL);
        for(Cliente cliente : clientes)
            agregarCliente(cliente);
        Cerrar();
    }
    public void limpiarProductos(List<Producto> productos) {
        Abrir();
        String cadSQL = String
                .format("DELETE from Producto");
        db.execSQL(cadSQL);
        for(Producto producto : productos)
            agregarProducto(producto);
        Cerrar();
    }
    public void agregarVendedor(Vendedor vendedor) {
        String cadSQL = String
                .format("insert into Vendedor (nombre, clave) values ('%s', '%s')",
                        vendedor.getNombre(),
                        vendedor.getClave());
        db.execSQL(cadSQL);
    }
    public void agregarProducto(Producto producto) {
        NumberFormat nf_us = NumberFormat.getInstance(Locale.US);
        String sprecio1 = nf_us.format(producto.getPrecios().get(0));
        String sprecio2 = nf_us.format(producto.getPrecios().get(1));
        String sprecio3 = nf_us.format(producto.getPrecios().get(2));
        String cadSQL = String
                .format("insert into Producto (clave, descripcion, precio1, precio2, precio3) values ('%s', '%s', %s, %s, %s)",
                        producto.getClave(),
                        producto.getDescripcion(),
                        sprecio1,
                        sprecio2,
                        sprecio3);
        db.execSQL(cadSQL);
    }
    public void agregarCliente(Cliente cliente) {
        String cadSQL = String
                .format("insert into Cliente (clave, nombre, noprecio) values ('%s', '%s', %d)",
                        cliente.getClave(),
                        cliente.getNombre(),
                        cliente.getNumPrecio());
        db.execSQL(cadSQL);
    }
    */
    public void limpiarVentas() {
        Abrir();
        String cadSQL = String
                .format("DELETE from Venta");
        db.execSQL(cadSQL);
        cadSQL = String
                .format("DELETE from VentaProducto");
        db.execSQL(cadSQL);
        Cerrar();
    }

    public void limpiarCuentas() {
        Abrir();
        String cadSQL = String
                .format("DELETE from CuentasXC");
        db.execSQL(cadSQL);
        Cerrar();
    }

    public void agregarVenta(Venta venta) {
        try {
            venta.setId(obtenerIDVenta());
        Abrir();

                    String fecha = String.format("%d-%d-%d %d:%d", venta.getFecha().getYear() + 1900, venta.getFecha().getMonth() + 1, venta.getFecha().getDate(), venta.getFecha().getHours(), venta.getFecha().getMinutes());
                    String cadSQL = String
                            .format("insert into Venta (id, fecha, idvendedor, idcliente, enviada, metodo, nota) values (%d, '%s', '%s', '%s', %d, %d, '%s')",
                                    venta.getId(),
                                    fecha,
                                    venta.getVendedor().getClave(),
                                    venta.getCliente().getClave(),
                                    venta.isEnviada() ? 1 : 0,
                                    venta.isMetodo() ? 1 : 0,
                                    venta.getNota());
                    db.execSQL(cadSQL);
//            Cursor cur = db
//                    .rawQuery(
//                            "SELECT id, fecha, idcliente, idvendedor, metodo, nota from Venta",
//                            new String[]{});
//            while (cur.moveToNext()) {
//                int id = cur.getInt(0);
//
//            }

                    for (VentaProducto vp : venta.getVentaProductos()) {
try {
    cadSQL = String
            .format("insert into VentaProducto (idventa, idproducto, cantidad, precio, precionum) values ('%d', '%s', '%s', '%s', '%s')",
                    venta.getId(),
                    vp.getProducto().getClave(),
                    vp.getCantidad(),
                    vp.getPrecioUnitario(),
                    vp.getPrecioNum());
    db.execSQL(cadSQL);
}catch (Exception e){
    db.execSQL("ALTER TABLE VentaProducto ADD COLUMN precionum INTEGER DEFAULT 0");
    cadSQL = String
            .format("insert into VentaProducto (idventa, idproducto, cantidad, precio, precionum) values ('%d', '%s', '%s', '%s', '%s')",
                    venta.getId(),
                    vp.getProducto().getClave(),
                    vp.getCantidad(),
                    vp.getPrecioUnitario(),
                    vp.getPrecioNum());
    db.execSQL(cadSQL);
}
                    }
                }catch (Exception e){}
        Cerrar();
    }

    public void agregarCuentas(List<Cuentas> cuentas) {
        Abrir();
        crearBD(db);
        String cadSQL;
        for (Cuentas cuenta : cuentas) {
            cadSQL = String.format("Delete from CuentasXC where Numero = '%s' and Id ='%s' and Serie = '%s' and Cliente = '%s'",
                    cuenta.getnumero(),
                    cuenta.getId(),
                    cuenta.getTipo(),
                    cuenta.getCliente());
            db.execSQL(cadSQL);
            cadSQL = String
                    .format("insert into CuentasXC (Archivo, Fecha, Cliente, Numero, Serie, Id, Cantidad, Return) values ('%s','%s','%s','%d', '%s', '%s', '%s', '%s')",
                            cuenta.getDocumento(),
                            cuenta.getFecha(),
                            cuenta.getCliente(),
                            cuenta.getnumero(),
                            cuenta.getTipo(),
                            cuenta.getId(),
                            cuenta.getSaldo(),
                            cuenta.getVencimiento());
            db.execSQL(cadSQL);
        }
        Cerrar();
    }


    public void agregarConfiguracion(Configura configuracion) {
        Abrir();
        //try{
        String cadSQLd = String
                .format("DELETE from Configuracion");
        db.execSQL(cadSQLd);
        //}catch(Exception e){}
        String cadSQL = String.format("insert into Configuracion (ftp_ip, botones, clave, existencia) values ('%s', '%d', '%d', '%d')",
                configuracion.getFTP(),
                configuracion.getBotones(),
                configuracion.getClave(),
                configuracion.getExistencia()
        );
        db.execSQL(cadSQL);
        Cerrar();
    }

    public void agregarnuevaConfiguracion() {
        boolean abierto = false;
        Configura configuracion = new Configura();
        configuracion.setClave(0);
        configuracion.setBotones(8);
        configuracion.setExistencia(0);
        configuracion.setFTP("");
        try {
            Abrir();
            abierto = true;
        } catch (Exception e) {
            //Toast.makeText(InicioActivity.InicioActividad,e.toString(), Toast.LENGTH_LONG).show();
        }
        if (abierto) {
            try {
                String cadSQL = String.format("insert into Configuracion (ftp_ip, botones, clave, existencia) values ('%s', '%d', '%d', '%d')",
                        configuracion.getFTP(),
                        configuracion.getBotones(),
                        configuracion.getClave(),
                        configuracion.getExistencia());
                db.execSQL(cadSQL);
            } catch (Exception e) {
                Configura config = new Configura();
                //db = this.getReadableDatabase();
                Cursor cur = db.rawQuery("SELECT ftp_ip FROM Configuracion", new String[]{});
                if (cur.moveToNext()) {
                    config.setFTP(cur.getString(0));
                }
                cur.close();
                //	db.close();

                //String cadSQLd = String.format("DELETE from Configuracion");
                //	db.execSQL(cadSQLd);
                //db.execSQL("DROP TABLE Configuracion");
                //db.execSQL("CREATE TABLE Configuracion (ftp_ip TEXT, botones INTEGER, clave INTEGER, existencia INTEGER, limite INTEGER)");

                String cadSQL = String.format("insert into Configuracion (ftp_ip, botones, clave, existencia,limite) values ('%s', '%d', '%d', '%d')",
                        configuracion.getFTP(),
                        configuracion.getBotones(),
                        configuracion.getClave(),
                        configuracion.getExistencia());
                db.execSQL(cadSQL);
            }
            Cerrar();
        }
    }

    public void agregarImpresion(Impresorayencabezado datosimpresion) {
        Abrir();
        try {
            String cadSQLd = String
                    .format("DELETE from Impresioninfo");
            db.execSQL(cadSQLd);
        } catch (Exception e) {
        }
        String renglon1 = datosimpresion.getencabezado1();
        if (renglon1.length() < 1) renglon1 = " ";

        String renglon2 = datosimpresion.getencabezado2();
        if (renglon2.length() < 1) renglon2 = " ";

        String renglon3 = datosimpresion.getencabezado3();
        if (renglon3.length() < 1) renglon3 = " ";

        String madadress = datosimpresion.getmacAdress();
        if (madadress.length() < 1) madadress = " ";

        String nombreimp = datosimpresion.getnombreImpresora();
        if (nombreimp.length() < 1) nombreimp = " ";


        String cadSQL = String.format("insert into Impresioninfo (renglon1, renglon2, renglon3, macAdress, nombre, impresion) values ('%s', '%s', '%s', '%s', '%s', '%d')",
                renglon1, renglon2, renglon3, madadress, nombreimp, datosimpresion.getmostrarImpresion());
        db.execSQL(cadSQL);
        Cerrar();
    }

    public int obtenerIDVenta() {
        SQLiteDatabase db = this.getReadableDatabase();
        int valor = 1;
        Cursor cur = db.rawQuery("SELECT max(id) from venta",
                new String[]{});
        if (cur.moveToNext())
            valor = cur.getInt(0) + 1;
        db.close();
        return valor;
    }

    public Configura obtenerConfiguracion() {
        Configura configuracion = new Configura();
        db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT ftp_ip, botones, clave, existencia FROM Configuracion",
                new String[]{});
        if (cur.moveToNext()) {
            configuracion.setFTP(cur.getString(0));
            configuracion.setBotones(cur.getInt(1));
            configuracion.setClave(cur.getInt(2));
            configuracion.setExistencia(cur.getInt(3));
        }
        if (configuracion.getFTP() == null && configuracion.getBotones() == 0) {
            configuracion.setBotones(8);
            configuracion.setClave(0);
            configuracion.setExistencia(0);
        }
        cur.close();
        db.close();
        return configuracion;
    }

    public Impresorayencabezado obtenerImpresion() {
        Impresorayencabezado datosimpresion = new Impresorayencabezado();
        db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT renglon1, renglon2, renglon3, macAdress, nombre, impresion FROM Impresioninfo",
                new String[]{});
        if (cur.moveToNext()) {
            datosimpresion.setecabezado1(cur.getString(0));
            datosimpresion.setecabezado2(cur.getString(1));
            datosimpresion.setecabezado3(cur.getString(2));
            datosimpresion.setmacAdress(cur.getString(3));
            datosimpresion.setnombreImpresora(cur.getString(4));
            datosimpresion.setmostrarImpresion(cur.getInt(5));
        }
        if (datosimpresion.getencabezado1() == null) datosimpresion.setecabezado1(" ");
        if (datosimpresion.getencabezado2() == null) datosimpresion.setecabezado2(" ");
        if (datosimpresion.getencabezado3() == null) datosimpresion.setecabezado3(" ");
        if (datosimpresion.getmacAdress() == null) datosimpresion.setmacAdress(" ");
        if (datosimpresion.getnombreImpresora() == null) datosimpresion.setnombreImpresora(" ");
        cur.close();
        db.close();
        return datosimpresion;
    }
    /*public List<Vendedor> cargarVendedores() {
		List<Vendedor> lista = new ArrayList();
		db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT clave, nombre from vendedor order by clave",
				new String[] {});
		int c = 0;
		while (cur.moveToNext()) {
			Vendedor vendedor = new Vendedor();
			vendedor.setClave(cur.getString(0));
			vendedor.setNombre(cur.getString(1));		
			lista.add(vendedor);
		}
		cur.close();
		db.close();
		return lista;
	}
	public List<String> cargarVendedoresdb() {
		List<String> lista = new ArrayList();
		db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT clave, nombre from vendedor order by nombre",
				new String[] {});
		int c = 0;
		while (cur.moveToNext()) {
			String vendedor = new String();
			vendedor = cur.getString(0) + " - " + cur.getString(1);		
			lista.add(vendedor);
		}
		cur.close();
		db.close();
		return lista;
	}
	public List<Cliente> cargarClientes() {
		List<Cliente> lista = new ArrayList();
		db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT clave, nombre,noprecio from cliente order by nombre",
				new String[] {});
		int c = 0;
		while (cur.moveToNext()) {
			Cliente cliente = new Cliente();
			cliente.setClave(cur.getString(0));
			cliente.setNombre(cur.getString(1));		
			cliente.setNumPrecio(cur.getInt(2));
			lista.add(cliente);
		}
		cur.close();
		db.close();
		return lista;
	}
	public List<Producto> cargarProductos() {
		List<Producto> lista = new ArrayList();
		db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT clave, descripcion, precio1, precio2, precio3 from producto order by descripcion",
				new String[] {});
		int c = 0;
		while (cur.moveToNext()) {
			Producto producto = new Producto();			
			producto.setClave(cur.getString(0));
			producto.setDescripcion(cur.getString(1));		
			List<Double> precios = new ArrayList();
			producto.setPrecios(precios);
			precios.add(cur.getDouble(2));
			precios.add(cur.getDouble(3));
			precios.add(cur.getDouble(4));
			lista.add(producto);
		}
		cur.close();
		db.close();
		return lista;
	}
	*/

    public List<Venta> cargarVentas(List<Cliente> clientes, List<Vendedor> vendedores, List<Producto> productos) {
        List<Venta> lista = new ArrayList<Venta>();

        db = this.getReadableDatabase();
        boolean agregametodo = true;
        while(agregametodo) {
            try {
                Cursor cur = db.rawQuery("SELECT id, fecha, idcliente, idvendedor, metodo, nota from Venta",new String[]{});
                agregametodo = false;
                while (cur.moveToNext()) {
                    Venta venta = new Venta();
                    venta.setId(cur.getInt(0));
                    String sfecha = cur.getString(1);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    try {
                        Date fecha = sdf.parse(sfecha);
                        venta.setFecha(fecha);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    venta.setNueva(false);
                    venta.setEnviada(false);
                    int metodo = cur.getInt(4);
                    venta.setMetodo(metodo==1?true:false);
                    String nota = cur.getString(5);
                    venta.setNota(nota);
                    String idcliente = cur.getString(2);
                    for (Cliente cliente : clientes)
                        if (cliente.getClave().equals(idcliente)) {
                            venta.setCliente(cliente);
                            break;
                        }

                    String idvendedor = cur.getString(3);
                    for (Vendedor vendedor : vendedores)
                        if (vendedor.getClave().equals(idvendedor)) {
                            venta.setVendedor(vendedor);
                            break;
                        }
                    List<VentaProducto> lprods = new ArrayList<VentaProducto>();
                    Cursor cur2 = db.rawQuery(
                            "SELECT idproducto, cantidad, precio, precionum from VentaProducto where  idventa = "
                                    + venta.getId(), new String[]{});
                    while (cur2.moveToNext()) {
                        VentaProducto vp = new VentaProducto();
                        String idproducto = cur2.getString(0);
                        for (Producto producto : productos)
                            if (producto.getClave().equals(idproducto)) {
                                vp.setProducto(producto);
                                break;
                            }

                        vp.setCantidad(Double.parseDouble(cur2.getString(1)));
                        vp.setPrecioUnitario(Double.parseDouble(cur2.getString(2)));
                        vp.setTotal(vp.getPrecioUnitario() * vp.getCantidad());
                        vp.setPrecioNum(Integer.parseInt(cur2.getString(3)));
                        lprods.add(vp);
                    }
                    cur2.close();
                    venta.setVentaProductos(lprods);
                    lista.add(venta);
                }
                cur.close();
            } catch (Exception e) {
                e.getMessage();
                if (e.toString().indexOf("no such column: metodo (code 1): , while compiling: SELECT id, fecha, idcliente, idvendedor, metodo, nota from venta") > 0) {
                    db.execSQL("ALTER TABLE venta ADD COLUMN metodo INTEGER DEFAULT 0");
                }
                if(e.toString().indexOf("no such column: precionum (code 1):") > 0) {
                    db.execSQL("ALTER TABLE VentaProducto ADD COLUMN precionum INTEGER DEFAULT 0");
                }
                if (e.toString().indexOf("no such column: nota (code 1):") > 0) {
                    db.execSQL("ALTER TABLE venta ADD COLUMN nota TEXT DEFAULT ''");
                }
            }
        }
        db.close();
        return lista;
    }
    public List<String> ObtenerCuentasArchivos() {
        Abrir();
        List<String> Archivos;
        Archivos = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT Archivo FROM CuentasXC",
                new String[]{});
        while (cur.moveToNext())
            Archivos.add(cur.getString(0));
        db.close();
       return Archivos;
    }
    public List<String> ObtenerListaCuentas()
    {
        //Abrir();
        List<String> Cuentas = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT Return FROM CuentasXC",
                new String[]{});
        while (cur.moveToNext())
            Cuentas.add(cur.getString(0));
        db.close();
        return Cuentas;
    }
    public List<Cuentas> ObtenerCuentas()
    {
        //Abrir();
        List<Cuentas> CuentasList = new ArrayList<Cuentas>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT Archivo, Fecha, Cliente, Numero, Serie, Id, Cantidad, Return FROM CuentasXC",
                new String[]{});
        while (cur.moveToNext()){
            Cuentas cuenta = new Cuentas();
                    cuenta.setDocumento(cur.getString(0));
                    cuenta.setFecha(cur.getString(1));
                    cuenta.setCliente(cur.getString(2));
                    cuenta.setnumero(cur.getInt(3));
                    cuenta.setTipo(cur.getString(4));
                    cuenta.setId(cur.getString(5));
                    cuenta.setSaldo(cur.getDouble(6));
                    cuenta.setVencimiento(cur.getString(7));
            CuentasList.add(cuenta);
        }

        db.close();
        return CuentasList;
    }
    public String ObtenerContenidoCuenta(String Nombre)
    {
        //Abrir();
        String Archivo = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT Cliente, Numero, Serie, Id, Cantidad FROM CuentasXC WHERE Archivo = '"+Nombre+"';",
                new String[]{});
        while (cur.moveToNext())
            Archivo += "insert into cxcs (Cliente, Numero, Serie, Id, Cantidad) values ('"+
                    cur.getString(0)+"', '"+cur.getInt(1)+"', '"+cur.getString(2)+"', '"+cur.getInt(3)+"', '"+cur.getString(4)+"')\r\n";
        db.close();
        return Archivo;
    }
    public Double ObtenerTotalAbonos()
    {
        Abrir();
        Double Total = 0.0;
        crearBD(db);
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("SELECT Cantidad FROM CuentasXC",
                    new String[]{});
            while (cur.moveToNext())
                Total += cur.getDouble(0);
        }catch(Exception e)
        {e.toString();}
        db.close();
        return Total;
    }
}