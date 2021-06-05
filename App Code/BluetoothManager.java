package hakathon.y2021;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothManager {
    private final Context context;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket socket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothManager(Context context) {
        this.context=context;
        findMyDevice();
        OutputStream tmpOut = null;
        try {
            tmpOut = socket.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();

        }
        mmOutStream = tmpOut;
    }
            public void findMyDevice(){
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        System.out.println(devices.size());
        for (BluetoothDevice device : devices) {
            System.out.println("FOUND");
            //TODO CONSTANT
            if (device.getName().equals("ESP32test")){
                try {
                    socket=device.createInsecureRfcommSocketToServiceRecord(uuid);
                    socket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
//    private class ConnectedThread extends Thread {
//
//

    public int read() throws IOException {
//        socket.connect();
//        InputStream tmpIn = null;
//        try {
//            tmpIn = socket.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mmInStream = tmpIn;
        mmInStream = socket.getInputStream();
        byte[] buffer = new byte[1];
        int begin = 0;
        int bytes = 0;
        bytes = mmInStream.read(buffer, bytes, buffer.length - bytes);
        System.out.println(bytes);
        return bytes;
    }


//    public void read() {
//        byte[] buffer = new byte[1];
//        int begin = 0;
//        int bytes = 0;
//        while (true) {
//            try {
//                if (mmInStream.available() > 0) {
//                    bytes = mmInStream.read(buffer, bytes, buffer.length - bytes);
//                        if (buffer[0] == "1".getBytes()[0]) {
//                            Intent intent=new Intent(context,EmergencyScreen.class);
//                            context.startActivity(intent);
//                            begin = i + 1;
//                            if (i == bytes - 1) {
//                                bytes = 0;
//                                begin = 0;
//                            }
//                        }
//                        else{
//                            Intent intent=new Intent(context,MainActivity.class);
//                            context.startActivity(intent);
//                        }
//
//                }
//            } catch (IOException e) {
//                break;
//            }
//        }

//        ConnectedThread mConnectedThread = new ConnectedThread(mmSocket);
//        mConnectedThread.start();

    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
        }
    }

    public void cancel() {
//        try {
//            mmSocket.close();
//        } catch (IOException e) {
//        }
    }

}
