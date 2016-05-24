package ch.fhnw.labyrinthapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends Activity {
    private DrawView dv;

    private Handler workerThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dv = (DrawView) findViewById(R.id.view);

        Intent intent = getIntent();

        CommThread ct = new CommThread("commThread", intent.getStringExtra("ipAddress"), Integer.parseInt(intent.getStringExtra("ipPort")));
        workerThreadHandler = ct.getHandler();

        dv.addObserver(new DrawView.DrawViewCallbackInterface() {
            @Override
            public void handleDraw(int posX, int posY) {
                Message msg = workerThreadHandler.obtainMessage();
                msg.what = 1;

                Bundle b = new Bundle();
                b.putSerializable("posX", posX);
                b.putSerializable("posY", posY);

                msg.setData(b);

                msg.sendToTarget();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Message msg = workerThreadHandler.obtainMessage();
        msg.what = 2;
        msg.sendToTarget();
    }
}
