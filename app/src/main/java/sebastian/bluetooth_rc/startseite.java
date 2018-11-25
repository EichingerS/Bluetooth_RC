package sebastian.bluetooth_rc;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class startseite extends AppCompatActivity {
    private Button bluetooth_switch;
    private final static int REQUEST_ENABLE_BT = 1;
    ArrayAdapter<String> mArrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu steuerung_menu) {
        getMenuInflater().inflate(R.menu.startseite_menu, steuerung_menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startseite);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);


/*
        bluetooth_switch = (Button) findViewById(R.id.bluetooth_switch); //Switch
        bluetooth_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){ //wenn Switch aud An steht
                    enable_bluetooth(); //Bluetooth anschalten
                }else{
                    disable_bluetooth(); //Bluetooth ausschalten
                }
            }
        });
        */

        Button next = (Button) findViewById(R.id.button_suchen);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent myIntent = new Intent(view.getContext(), ListActivity.class);
                //startActivityForResult(myIntent, 0);
            }

        });

    }


    public void ButtonOnClicked(View v)
    {
        Button button = (Button) v;
        startActivity(new Intent(getApplicationContext(), steuerung.class
        ));
    }
    public void openDialog(int zahl) { //öffnet Dialog Fenster
        final Dialog dialog = new Dialog(this); // Context, this, etc.
        //dialog.setContentView(R.layout.dialog);
        //dialog.setTitle(R.string.dialog_title);
        dialog.setTitle(zahl);
        dialog.show();
    }
    public void enable_bluetooth()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
    public void disable_bluetooth()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
    }





    public void Button_suchen_OnClicked(View v)
    {

    }
    public void search_devices()
    {
        // Create a BroadcastReceiver for ACTION_FOUND
         final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    int zahl = mArrayAdapter.getCount();
                }

            }
        };
// Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }
}
