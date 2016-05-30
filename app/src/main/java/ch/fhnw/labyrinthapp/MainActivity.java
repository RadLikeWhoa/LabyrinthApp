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

    private SensorManager sensorManager;
    private Sensor accelerometer;

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

        final Context that = this;

        if (sensorView != null) {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            sensorView.addSensor(sensorManager, accelerometer);

            sensorView.addObserver(new SensorView.DrawViewCallbackInterface() {
                @Override
                public void handleDraw(int posX, int posY) {
                    Intent intent = new Intent("input");

                    intent.putExtra("posX", posX);
                    intent.putExtra("posY", posY);

                    LocalBroadcastManager.getInstance(that).sendBroadcast(intent);
                }
            });
        } else if (touchViewH != null && touchViewV != null) {
            touchViewV.addObserver(new TouchViewVertical.DrawViewCallbackInterface() {
                @Override
                public void handleDraw(int posY) {
                    Intent intent = new Intent("input");

                    intent.putExtra("posX", touchViewH.getValue());
                    intent.putExtra("posY", posY);

                    LocalBroadcastManager.getInstance(that).sendBroadcast(intent);
                }
            });

            touchViewH.addObserver(new TouchViewHorizontal.DrawViewCallbackInterface() {
                @Override
                public void handleDraw(int posX) {
                    Intent intent = new Intent("input");

                    intent.putExtra("posX", posX);
                    intent.putExtra("posY", touchViewV.getValue());

                    LocalBroadcastManager.getInstance(that).sendBroadcast(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("input"));
        new OscAsyncTask().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
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
