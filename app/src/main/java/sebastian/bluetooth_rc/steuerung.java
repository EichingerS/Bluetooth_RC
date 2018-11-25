package sebastian.bluetooth_rc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


import android.view.Menu;
import android.widget.Toast;

public class steuerung extends AppCompatActivity {

    //Hard Coded BLUETOOTH
    private final String DEVICE_ADDRESS = "00:21:13:00:A4:18"; //MAC Address of Bluetooth Module
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private BluetoothSocket socket;

    private ByteArrayOutputStream byteArrayOutputStream;
    private OutputStream outputStream;

    Button forward_btn, reverse_btn, left_btn, right_btn, bluetooth_connect_btn;
    SeekBar driving_seekbar, steering_seekbar;

    boolean BT_connected;
    //String command; //string variable that will store value to be transmitted to the bluetooth module
    //                         0    1    2    3    4    5    6    7    8    9
    byte [] command = {(byte) 255, 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    int middle_position = 126;

    //initiates the linkage from the java-File to the menu-XML
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.steuerung_menu, menu);
        return true;
    }

    //Handler für the selection of the menu and also the Bluetooth-Icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                if(BT_init()) //a query and the start of the BT-Initialisierung
                {
                    Log.d("!!!!!!!!!!!!!!!", "onOptionsItemSelected: ONGOING");
                    if ((BT_connected = BT_connect())) Log.d("!!!!!!!!!!!!!!!", "onOptionsItemSelected: FINISHED");
                }
                return true;
            }
            case R.id.setting_btn:{
                startActivity(new Intent(getApplicationContext(), einstellungen.class));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steuerung);

        //declaration of button variables
        forward_btn = (Button) findViewById(R.id.forward_btn);
        reverse_btn = (Button) findViewById(R.id.reverse_btn);
        reverse_btn = (Button) findViewById(R.id.left_btn);
        reverse_btn = (Button) findViewById(R.id.right_btn);
        bluetooth_connect_btn = (Button) findViewById(R.id.secure_connect_scan);

        driving_seekbar = (SeekBar) findViewById(R.id.drive_seekBar);
        steering_seekbar = (SeekBar) findViewById(R.id.steer_seekBar);

        driving_seekbar.setMax(254);
        driving_seekbar.setProgress(middle_position);

        steering_seekbar.setMax(254);
        steering_seekbar.setProgress(middle_position);

        //OnTouchListener code for the driving_seekbar from 0 to 256
        driving_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (BT_connected) {
                    try {
                        command[3] = (byte) progress;
                        outputStream.write(command);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                driving_seekbar.setProgress(middle_position);
                if (BT_connected) {
                    try {
                        command[1] = (byte) middle_position;
                        outputStream.write(command);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /** START OF FUNCTION-DECLARATIONS.**/
    //Initializes bluetooth module
    public boolean BT_init()
    {
        boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);

            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    public boolean BT_connect()
    {
        boolean connected = true;

        try
        {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            socket.connect();

            Toast.makeText(getApplicationContext(),
                    "Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            connected = false;
        }

        if(connected)
        {
            try
            {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return connected;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

}

