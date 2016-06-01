package ch.fhnw.labyrinthapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;

public class SensorView extends GameView implements SensorEventListener {
    private SensorManager manager;
    private Sensor accelerometer;

    public SensorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();

        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;

        if (eventX == -1 || eventY == -1) {
            eventX = centerX;
            eventY = centerY;
        }

        xTo180 = (int) (180 * (eventX / 20));
        yTo180 = (int) (180 * (eventY / 20));

        eventX = canvasWidth * (eventX / 20);
        eventY = canvasHeight * (eventY / 20);

        for (PositionUpdateInterface pui : observers) {
            pui.handlePositionUpdate(xTo180, yTo180);
        }

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        canvas.drawCircle(centerX, centerY, 40, paint);

        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);

        canvas.drawCircle(eventX, eventY, 30, paint);

        path.reset();
        path.moveTo(centerX, centerY);
        path.lineTo(eventX, eventY);

        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);

        canvas.drawPath(path, paint);
    }

    public void addSensor(SensorManager manager, Sensor accelerometer) {
        this.manager = manager;
        this.accelerometer = accelerometer;

        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void removeSensor() {
        manager.unregisterListener(this, accelerometer);

        manager = null;
        accelerometer = null;
    }

    long lastSaved = System.currentTimeMillis();

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if ((System.currentTimeMillis() - lastSaved) > 100) {
            lastSaved = System.currentTimeMillis();

            eventX = sensorEvent.values[0] * -1 + 10;
            eventY = sensorEvent.values[1] + 10;

            invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}
