package ch.fhnw.labyrinthapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TouchViewVertical extends View {
    private Paint paint = new Paint();
    private Path path = new Path();
    private float eventY = -1, centerX, centerY, canvasHeight;
    private int yTo180;

    public interface DrawViewCallbackInterface {
        void handleDraw(int posY);
    }

    private List<DrawViewCallbackInterface> observers = new ArrayList<>();

    public TouchViewVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasHeight = canvas.getHeight();

        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;

        if (eventY == -1) {
            eventY = centerY;
        }

        yTo180 = (int) (eventY / (canvasHeight / 180));

        for (DrawViewCallbackInterface dwci : observers) {
            dwci.handleDraw(yTo180);
        }

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        path.reset();
        path.moveTo(centerX, 0);
        path.lineTo(centerX, canvasHeight);

        canvas.drawPath(path, paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        canvas.drawCircle(centerX, centerY, 40, paint);

        paint.setColor(Color.argb(255, 69, 66, 69));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);

        canvas.drawCircle(centerX, centerY, 38, paint);

        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);

        canvas.drawCircle(centerX, eventY, 30, paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (eventY == event.getY()) {
            return true;
        }

        eventY = event.getY();

        if (eventY > canvasHeight) {
            eventY = canvasHeight;
        }

        if (eventY < 0) {
            eventY = 0;
        }

        // Schedules a repaint.

        invalidate();
        return true;
    }

    public void addObserver(DrawViewCallbackInterface dwci) {
        observers.add(dwci);
    }

    public int getValue() {
        return yTo180;
    }
}
