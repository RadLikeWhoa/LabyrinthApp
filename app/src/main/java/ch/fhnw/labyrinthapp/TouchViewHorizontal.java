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

public class TouchViewHorizontal extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();
    private float eventX = -1, centerX, centerY, canvasWidth;
    private int xTo180;

    public interface DrawViewCallbackInterface {
        void handleDraw(int posX);
    }

    private List<DrawViewCallbackInterface> observers = new ArrayList<>();

    public TouchViewHorizontal(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasWidth = canvas.getWidth();

        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;

        if (eventX == -1) {
            eventX = centerX;
            xTo180 = (int) (eventX / (canvasWidth / 180));
        } else {
            xTo180 = (int) (eventX / (canvasWidth / 180));

            for (DrawViewCallbackInterface dwci : observers) {
                dwci.handleDraw(xTo180);
            }
        }

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        path.reset();
        path.moveTo(0, centerY);
        path.lineTo(canvasWidth, centerY);

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

        canvas.drawCircle(eventX, centerY, 30, paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (eventX == event.getX()) {
            return true;
        }

        eventX = event.getX();

        if (eventX > canvasWidth) {
            eventX = canvasWidth;
        }

        if (eventX < 0) {
            eventX = 0;
        }

        // Schedules a repaint.

        invalidate();
        return true;
    }

    public void addObserver(DrawViewCallbackInterface dwci) {
        observers.add(dwci);
    }

    public int getValue() {
        return xTo180;
    }
}
