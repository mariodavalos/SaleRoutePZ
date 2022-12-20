//package com.example.registroventa.services;
//
//import android.annotation.SuppressLint;
//import android.app.Dialog;
//import android.app.ListActivity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;
//import android.content.Intent;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbManager;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.ContextMenu;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.MenuInflater;
//import android.view.View;
//import android.widget.AdapterView.AdapterContextMenuInfo;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//
//import com.example.registroventa.R;
////import com.zebra.android.devdemo.storedformat.VariablesScreen;
////import com.zebra.android.devdemo.util.UIHelper;
//import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
//import com.zebra.sdk.printer.discovery.DiscoveredPrinterUsb;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
///*
//import com.zebra.android.devdemo.util.UIHelper;
//import com.zebra.sdk.comm.ConnectionException;
//import com.zebra.sdk.printer.discovery.BluetoothDiscoverer;
//import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
//import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;
//import com.zebra.sdk.printer.discovery.DiscoveryHandler;*/
//
//@SuppressLint("NewApi")
//@SuppressWarnings("deprecation")
//public class BluetoothPrinter extends ListActivity {
//    public static final String APP_PREFERENCES_KEY = "PrintStationPreferences";
//    public static final int DIALOG_DISCOVERY = 0;
//    public static final int DIALOG_ABOUT = 1;
//    public static final int REQUEST_ENABLE_BT = 0;
//    String[] attributeKeys = new String[]{"drive_letter", "format_name", "extension", "source_image", "format_details"};
//    String[] storedPrinterAttributeKeys = new String[]{"printer_name", "printer_address"};
//    private BaseAdapter statusListAdapter;
//    private BaseAdapter spinnerAdapter;
//    private DiscoveredPrinter formatPrinter;
//    private List<Map<String, String>> formatsList = new ArrayList<Map<String, String>>();
//    //private UIHelper uiHelper = new UIHelper(this);
//    //int [] attributeIds = new int [] {R.id.formatDrive, R.id.formatFilename, R.id.formatExtension, R.id.formatSourceImage, R.id.formatDetails};
//    private volatile boolean usbDiscoveryComplete = false;
//    private volatile boolean bluetoothDiscoveryComplete = false;
//        /*
//		@Override
//		public boolean onContextItemSelected(MenuItem item) {
//			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//
//			final SavedFormatProvider formatStorageHandler = new SavedFormatProvider(this);
//			Map<String, String> selectedFormat = formatsList.get(info.position);
//			final String formatDrive = selectedFormat.get(attributeKeys[0]);
//			final String formatName = selectedFormat.get(attributeKeys[1]);
//			final String formatExtension = selectedFormat.get(attributeKeys[2]);
//			final String sourcePrinterName = SelectedPrinterManager.getSelectedPrinter().address;
//
//		    switch (item.getItemId()) {
//		    case R.id.menu_save:
//		        new AsyncTask<Void, Void, Void>() {
//		        	String formatText = null;
//					@Override
//					protected Void doInBackground(Void... params) {
//				        uiHelper.showLoadingDialog("Retrieving format...");
//				        Connection connection = SelectedPrinterManager.getPrinterConnection();
//
//				        if (connection != null) {
//				            try {
//				                connection.open();
//				                ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
//
//				                byte[] formatContents = printer.retrieveFormatFromPrinter(formatDrive + formatName + formatExtension);
//				                formatText = new String(formatContents, "UTF-8");
//
//				            } catch (ConnectionException e) {
//				                uiHelper.showErrorDialogOnGuiThread(e.getMessage());
//				            } catch (ZebraPrinterLanguageUnknownException e) {
//				                uiHelper.showErrorDialogOnGuiThread(e.getMessage());
//				            } catch (UnsupportedEncodingException e) {
//				                uiHelper.showErrorDialogOnGuiThread(e.getMessage());
//				            } finally {
//				            	try {
//									connection.close();
//								} catch (ConnectionException e) {
//									uiHelper.showErrorDialogOnGuiThread(e.getMessage());
//								}
//				            }
//				        }
//				        uiHelper.dismissLoadingDialog();
//						return null;
//					}
//					protected void onPostExecute(Void result) {
//						if (formatText != null) {
//							long id = formatStorageHandler.insert(formatDrive, formatName, formatExtension, sourcePrinterName, formatText);
//							if (id >= 0) {
//								Map<String, String> listEntry = new HashMap<String, String>();
//								listEntry.put(attributeKeys[0], "");
//								listEntry.put(attributeKeys[1], formatName);
//								listEntry.put(attributeKeys[2], formatExtension);
//								listEntry.put(attributeKeys[3], Integer.toString(R.drawable.btn_star_big_on));
//								listEntry.put(attributeKeys[4], "Retrieved from: " + sourcePrinterName + " on " + formatStorageHandler.getTimestampOfFormat(id));
//								listEntry.put(FormatRefresher.FORMAT_SOURCE_KEY, FormatRefresher.FORMAT_SOURCE_DATABASE);
//								listEntry.put(FormatRefresher.FORMAT_LOCATION_KEY, Long.toString(id));
//
//								formatsList.add(formatStorageHandler.getNumberOfStoredFormats() - 1 , listEntry);
//								statusListAdapter.notifyDataSetChanged();
//							}
//						}
//					};
//					}.execute((Void) null);
//		        return true;
//		    case R.id.menu_delete:
//		    	final String formatId = selectedFormat.get(FormatRefresher.FORMAT_LOCATION_KEY);
//		    	if (formatStorageHandler.delete(formatId) == true) {
//		    		formatsList.remove(info.position);
//		    		statusListAdapter.notifyDataSetChanged();
//		    	}
//
//		    	return true;
//		    default:
//		        return super.onContextItemSelected(item);
//		    }
//		}*/
//    private volatile boolean networkDiscoveryComplete = false;
//    private List<Map<String, String>> storedPrintersList = new ArrayList<Map<String, String>>();
//    private volatile boolean isFinished = false;
//
//    @SuppressLint("NewApi")
//    private boolean isPrinterToRemove(UsbDevice device, DiscoveredPrinter printer) {
//        if (printer instanceof DiscoveredPrinterUsb) {
//            DiscoveredPrinterUsb usbPrinter = (DiscoveredPrinterUsb) printer;
//            return device.getDeviceName().equals(usbPrinter.device.getDeviceName());
//        }
//        return false;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.dialog_printer);
//        //statusListAdapter = new SimpleAdapter(this, formatsList, R.layout.format_list_item, attributeKeys, attributeIds);
//
//
//        ListView listView = (ListView) findViewById(android.R.id.list);
//        listView.setDividerHeight(3);
//        //findViewById(R.id.emptyFormat).setVisibility(View.INVISIBLE);
//        //listView.setEmptyView(findViewById(R.id.emptyFormat));
//        //ImageButton refreshButton = (ImageButton) findViewById(R.id.refreshFormatsButton);
//        //refreshButton.setOnClickListener(new OnClickListener() {
//        //	public void onClick(View v) {
//        //		FormatRefresher.execute(ChooseFormatScreen.this, uiHelper, attributeKeys, formatsList, statusListAdapter);
//        //	}});
//
//        registerForContextMenu(listView);
//
//        setListAdapter(statusListAdapter);
//
//        //SelectedPrinterManager.populatePrinterHistoryFromPreferences(this);
//
//        //usbHelper.onCreate(getIntent());
//
//        //DiscoveredPrinter selectedPrinter = SelectedPrinterManager.getSelectedPrinter();
//        //if (selectedPrinter != null && selectedPrinter instanceof DiscoveredPrinterUsb == false) {
//        //	FormatRefresher.execute(this, uiHelper, attributeKeys, formatsList, statusListAdapter);
//        //}
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo info) {
//        super.onCreateContextMenu(menu, v, info);
//        MenuInflater inflater = getMenuInflater();
//        //inflater.inflate(R.menu.format_menu, menu);
//
//        Map<String, String> selectedFormat = formatsList.get(((AdapterContextMenuInfo) info).position);
//        //String formatSource = selectedFormat.get(FormatRefresher.FORMAT_SOURCE_KEY);
//
//        //menu.findItem(R.id.menu_save).setVisible(formatSource.equals(FormatRefresher.FORMAT_SOURCE_PRINTER));
//        //menu.findItem(R.id.menu_delete).setVisible(formatSource.equals(FormatRefresher.FORMAT_SOURCE_DATABASE));
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        //usbHelper.onNewIntent(intent);
//        //processNfcScan(intent);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //usbHelper.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //usbHelper.onResume();
//
//        removeDisconnectedUsbPrintersFromList();
//
//
//        //DiscoveredPrinter selectedPrinter = SelectedPrinterManager.getSelectedPrinter();
//
//        //if (selectedPrinter == null) {
//        //	showDialog(DIALOG_DISCOVERY);
//        //} else if (formatPrinter != null && formatPrinter.address.equals(selectedPrinter.address) == false) {
//        //	FormatRefresher.execute(this, uiHelper, attributeKeys, formatsList, statusListAdapter);
//        //}
//    }
//
//    private void removeDisconnectedUsbPrintersFromList() {
//        UsbManager usbService = (UsbManager) this.getSystemService(Context.USB_SERVICE);
//        HashMap<String, UsbDevice> deviceList = usbService.getDeviceList();
//
//
//        //DiscoveredPrinter[] history = SelectedPrinterManager.getPrinterHistory();
//        //	for (int i = 0; i < history.length; i++) {
//        //	DiscoveredPrinter historyPrinter = history[i];
//        //	if (historyPrinter instanceof DiscoveredPrinterUsb && deviceList.containsKey(historyPrinter.address) == false) {
//        //	SelectedPrinterManager.removeHistoryItemAtIndex(i);
//        //		i--;
//        //		history = SelectedPrinterManager.getPrinterHistory();
//        //		}
//        //	}
//
//        if (spinnerAdapter != null) {
//            spinnerAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//
//        //formatPrinter = SelectedPrinterManager.getSelectedPrinter();
//
//        Intent intent;
//        intent = new Intent(String.valueOf(this));
//        Map<String, String> listItem = (Map<String, String>) l.getItemAtPosition(position);
//        String formatName = listItem.get(attributeKeys[0]) + listItem.get(attributeKeys[1]) + listItem.get(attributeKeys[2]);
//        //intent.putExtra(FormatRefresher.FORMAT_NAME, formatName);
//        //intent.putExtra(FormatRefresher.FORMAT_SOURCE_KEY, listItem.get(FormatRefresher.FORMAT_SOURCE_KEY));
//        //	intent.putExtra(FormatRefresher.FORMAT_LOCATION_KEY, listItem.get(FormatRefresher.FORMAT_LOCATION_KEY));
//
//        startActivity(intent);
//        //overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
//    }
//
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        Dialog dialog = null;
//        switch (id) {
//            case DIALOG_DISCOVERY:
//                dialog = new Dialog(this);
//
//                dialog.setContentView(R.layout.dialog_printer);
//                dialog.setTitle("Select a Printer");
//                dialog.setCancelable(true);
//
//                dialog.setOnCancelListener(new OnCancelListener() {
//
//                    public void onCancel(DialogInterface dialog) {
//                        //		if (SelectedPrinterManager.getSelectedPrinter() == null) {
//                        //			finish();
//                        //		}
//                    }
//                });
//
//                ListView discoveryList = (ListView) dialog.findViewById(android.R.id.list);
//                //discoveryList.setEmptyView(dialog.findViewById(R.id.emptyDiscoveryLayout));
//
//                //discoveredPrinterListAdapter = new DiscoveredPrinter(this);
//
//
//				/*discoveryList.setOnItemClickListener(new OnItemClickListener() {
//					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
//						DiscoveredPrinter printer = (DiscoveredPrinter) discoveredPrinterListAdapter.getPrinter(position);
//						dialog.dismiss();
//
//						if (printer instanceof DiscoveredPrinterUsb) {
//							UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//							UsbDevice device = ((DiscoveredPrinterUsb)printer).device;
//							if (usbManager.hasPermission(device) == false) {
//								usbHelper.requestUsbPermission(usbManager, device);
//								return;
//							}
//						}
//						SelectedPrinterManager.setSelectedPrinter(printer);
//						spinnerAdapter.notifyDataSetChanged();
//						SelectedPrinterManager.storePrinterHistoryInPreferences(ChooseFormatScreen.this);
//						FormatRefresher.execute(ChooseFormatScreen.this, uiHelper, attributeKeys, formatsList, statusListAdapter);
//					}});*/
//                break;
//            case DIALOG_ABOUT:
//				/*dialog = new Dialog(this);
//				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//				dialog.setContentView(R.layout.about_dialog);
//				dialog.setCancelable(true);*/
//                break;
//            default:
//                dialog = super.onCreateDialog(id);
//        }
//        return dialog;
//    }
//
//    @Override
//    protected void onPrepareDialog(int id, final Dialog dialog) {
//        switch (id) {
//            case DIALOG_DISCOVERY:
//				/*dialog.findViewById(R.id.refreshPrintersButton).setOnClickListener(new OnClickListener() {
//					public void onClick(View v) {
//						doDiscovery(dialog);
//					}});
//
//					doDiscovery(dialog);*/
//        }
//
//        super.onPrepareDialog(id, dialog);
//    }
//    //int [] storedPrinterAttributeIds = new int [] {R.id.storedPrinterName, R.id.storedPrinterAddress};
//
//    private void doDiscovery(final Dialog dialog) {
//
//			/*((TextView)dialog.findViewById(R.id.emptyDiscovery)).setText("Searching...");
//			dialog.findViewById(R.id.searchingSpinner).setVisibility(View.VISIBLE);
//			dialog.findViewById(R.id.refreshPrintersButton).setVisibility(View.INVISIBLE);*/
//
//        usbDiscoveryComplete = true;
//        bluetoothDiscoveryComplete = false;
//        networkDiscoveryComplete = true;
//
//			/*
//			try {
//				BluetoothDiscoverer.findPrinters(this, new DiscoveryHandler(){
//
//					public void foundPrinter(final DiscoveredPrinter printer) {
//						this.runOnUiThread(new Runnable() {
//							public void run() {
//								discoveredPrinterListAdapter.addPrinter(printer);
//							}});
//					}
//					public void discoveryFinished() { bluetoothDiscoveryComplete = true; }
//					public void discoveryError(String message) { bluetoothDiscoveryComplete = true; }
//
//				});
//			} catch (ConnectionException e) { bluetoothDiscoveryComplete = true; }
//				*/
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                while (usbDiscoveryComplete == false || bluetoothDiscoveryComplete == false || networkDiscoveryComplete == false) {
//                    try {
//                        Thread.sleep(250);
//                    } catch (InterruptedException e) {
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
//                //dialog.findViewById(R.id.searchingSpinner).setVisibility(View.INVISIBLE);
//                //dialog.findViewById(R.id.refreshPrintersButton).setVisibility(View.VISIBLE);
//                //((TextView)dialog.findViewById(R.id.emptyDiscovery)).setText("No Printers Found");
//                super.onPostExecute(result);
//            }
//        }.execute((Void) null);
//    }
//
//    @Override
//    public void finish() {
//        isFinished = true;
//        super.finish();
//    }
//
//    public boolean isFinished() {
//        return isFinished;
//    }
//}
///*
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//
//        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//        setContentView(R.layout.activity_busqueda);
//
//        try
//        {
//        btDiscoveryHandler = new DiscoveryHandler()
//        {
//            public void discoveryError(String message)
//            {
//                new UIHelper(BluetoothPrinter.this).showErrorDialogOnGuiThread(message);
//            }
//
//            public void discoveryFinished()
//            {
//                runOnUiThread(new Runnable()
//                {
//                    public void run()
//                    {
//                        Toast.makeText(BluetoothPrinter.this, " Discovered " + discoveredPrinters.size() + " devices", Toast.LENGTH_SHORT).show();
//                        setProgressBarIndeterminateVisibility(false);
//                    }
//                });
//            }
//
//            public void foundPrinter(final DiscoveredPrinter printer)
//            {
//                runOnUiThread(new Runnable()
//                {
//                    public void run()
//                    {
//                        DiscoveredPrinterBluetooth p = (DiscoveredPrinterBluetooth) printer;
//                        discoveredPrinters.add(p.address + " (" + p.friendlyName + ")");
//                        mArrayAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//
//        };
//        }
//        catch(Exception e)
//        {
//        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder
//			.setMessage(e.toString())
//			.setTitle("Error")
//			.setPositiveButton("Si", null)
//			.show();
//        }
//        setProgressBarIndeterminateVisibility(true);
//        discoveredPrinters = new ArrayList<String>();
//        setupListAdapter();
//
//        new Thread(new Runnable()
//        {
//            public void run()
//            {
//                Looper.prepare();
//                try
//                {
//                    BluetoothDiscoverer.findPrinters(BluetoothPrinter.this, btDiscoveryHandler);
//                }
//                catch (ZebraPrinterConnectionException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//                finally
//                {
//                    Looper.myLooper().quit();
//                }
//            }
//        }).start();
//    }
//
//    private void setupListAdapter()
//    {
//        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, discoveredPrinters);
//        setListAdapter(mArrayAdapter);
//    }
//
//
//}//*/
