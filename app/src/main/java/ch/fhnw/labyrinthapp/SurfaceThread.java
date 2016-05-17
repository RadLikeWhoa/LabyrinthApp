package ch.fhnw.labyrinthapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by Sacha on 17.5.16.
 */
public class SurfaceThread extends HandlerThread {
    private boolean running = false;

    private Handler mHandler;

    private SurfaceHolder surfaceHolder;

    public SurfaceThread(String name, SurfaceHolder surfaceHolder) {
        super(name);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    running = false;
                }
            }
        };

        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void run() {
        while (running) {
            Canvas c = null;

            try {
                c = surfaceHolder.lockCanvas();

                synchronized (surfaceHolder) {
                    int centerX = surfaceHolder.getSurfaceFrame().centerX();
                    int centerY = surfaceHolder.getSurfaceFrame().centerY();

                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

                    paint.setColor(Color.WHITE);
                    paint.setStyle(Paint.Style.STROKE);

                    c.drawCircle(centerX, centerY, 50, paint);
                }
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public Handler getHandler() {
        return mHandler;
    }
}
