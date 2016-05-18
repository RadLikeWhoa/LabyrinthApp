package ch.fhnw.labyrinthapp;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends Activity {
    private Handler surfaceThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawingView(this));
    }

    class DrawingView extends SurfaceView implements SurfaceHolder.Callback {
        public DrawingView(Context context) {
            super(context);
        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Log.d("Surface", "created");

            if (surfaceThreadHandler == null) {
                SurfaceThread st = new SurfaceThread("surface", surfaceHolder);
                st.start();

                surfaceThreadHandler = st.getHandler();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (surfaceThreadHandler != null) {
                Message msg = surfaceThreadHandler.obtainMessage();

                msg.what = 1;

                msg.sendToTarget();

                surfaceThreadHandler = null;
            }
        }
    }
}
