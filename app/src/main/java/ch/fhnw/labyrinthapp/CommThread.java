package ch.fhnw.labyrinthapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscProperties;

public class CommThread extends HandlerThread {
    private Handler mHandler;

    private OscP5 oscP5;

    public CommThread(String name, String ip, int port) {
        super(name);

        OscProperties oscProperties = new OscProperties();
        oscProperties.setNetworkProtocol(OscProperties.TCP);
        oscProperties.setRemoteAddress(ip, port);

        oscP5 = new OscP5(this, oscProperties);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    OscMessage oscMsg = new OscMessage("Lab");

                    Bundle b = msg.getData();

                    oscMsg.add(0);
                    oscMsg.add((int) b.getSerializable("posX"));
                    oscMsg.add(1);
                    oscMsg.add((int) b.getSerializable("posY"));

                    oscP5.send(oscMsg);
                } else if (msg.what == 2) {
                    Log.d("comm", "stopping thread");
                    quit();
                }
            }
        };
    }

    public Handler getHandler() {
        return mHandler;
    }
}
