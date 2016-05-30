package ch.fhnw.labyrinthapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscProperties;

public class MainActivity extends Activity {
    private OscP5 oscP5;

    private String ipAddr;
    private int ipPort;

    private DrawView dv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dv = (DrawView) findViewById(R.id.view);

        Intent intent = getIntent();

        ipAddr = intent.getStringExtra("ipAddress");
        ipPort = Integer.parseInt(intent.getStringExtra("ipPort"));

        final Context that = this;

        dv.addObserver(new DrawView.DrawViewCallbackInterface() {
            @Override
            public void handleDraw(int posX, int posY) {
                Intent intent = new Intent("input");

                intent.putExtra("posX", posX);
                intent.putExtra("posY", posY);

                LocalBroadcastManager.getInstance(that).sendBroadcast(intent);
            }
        });
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
