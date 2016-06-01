package ch.fhnw.labyrinthapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscProperties;

public class MainActivity extends Activity {
    private OscP5 oscP5;

    private String ipAddr;
    private int ipPort;

    private SensorView sensorView;
    private TouchViewHorizontal touchViewH;
    private TouchViewVertical touchViewV;

    private int lastX, lastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorView = (SensorView) findViewById(R.id.view);
        touchViewH = (TouchViewHorizontal) findViewById(R.id.horizontalView);
        touchViewV = (TouchViewVertical) findViewById(R.id.verticalView);

        Intent intent = getIntent();

        ipAddr = intent.getStringExtra("ipAddress");
        ipPort = Integer.parseInt(intent.getStringExtra("ipPort"));
    }

    private PositionUpdateInterface updateInterface = new PositionUpdateInterface() {
        @Override
        public void handlePositionUpdate(int posX, int posY) {
            Intent intent = new Intent("input");

            if (posX > -1) {
                lastX = posX;
            }

            if (posY > -1) {
                lastY = posY;
            }

            intent.putExtra("posX", lastX);
            intent.putExtra("posY", lastY);

            LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("input"));

        new OscAsyncTask().execute();

        if (sensorView != null) {
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            sensorView.addSensor(sensorManager, accelerometer);
            sensorView.addObserver(updateInterface);
        } else if (touchViewH != null && touchViewV != null) {
            touchViewV.addObserver(updateInterface);
            touchViewH.addObserver(updateInterface);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

        if (sensorView != null) {
            sensorView.removeSensor();
            sensorView.removeObserver(updateInterface);
        } else if (touchViewH != null && touchViewV != null) {
            touchViewV.removeObserver(updateInterface);
            touchViewH.removeObserver(updateInterface);
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int x = intent.getIntExtra("posX", 0);
            int y = intent.getIntExtra("posY", 0);

            OscMessage msg = new OscMessage("Labyrinth");

            msg.add(0);
            msg.add(x);
            msg.add(1);
            msg.add(y);

            if (oscP5 != null) {
                oscP5.send(msg);
            }
        }
    };

    private class OscAsyncTask extends AsyncTask<OscMessage, Integer, Void> {
        protected Void doInBackground(OscMessage... msg) {
            OscProperties properties = new OscProperties();

            properties.setNetworkProtocol(OscProperties.TCP);
            properties.setRemoteAddress(ipAddr, ipPort);

            oscP5 = new OscP5(this, properties);

            return null;
        }
    }
}
