package ch.fhnw.labyrinthapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {
    private Paint paint = new Paint();
    private Path path = new Path();
    private float eventX, eventY, centerX, centerY, canvasWidth, canvasHeight, xTo180, yTo180;
    private boolean moveYellowCircle = false;

    public interface DrawViewCallbackInterface {
        void handleDraw(int posX, int posY);
    }

    private List<DrawViewCallbackInterface> observers = new ArrayList<>();

    public DrawView(Context context, AttributeSet attrs) {
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

        xTo180 = eventX / (canvasWidth / 180);
        yTo180 = eventY / (canvasHeight / 180);

        for (DrawViewCallbackInterface dwci : observers) {
            dwci.handleDraw((int) xTo180, (int) yTo180);
        }

        if (moveYellowCircle) {
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);

            canvas.drawCircle(centerX, centerY, 40, paint);

            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(0);

            canvas.drawCircle(eventX, eventY, 30, paint);

            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);

            canvas.drawPath(path, paint);
        } else {
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);

            canvas.drawCircle(centerX, centerY, 40, paint);

            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(0);

            canvas.drawCircle(centerX, centerY, 30, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        eventX = event.getX();
        eventY = event.getY();
        moveYellowCircle = true;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                path.reset();
                path.moveTo(centerX, centerY);
                if (eventX > canvasWidth) eventX = canvasWidth;
                if (eventX < 0) eventX = 0;
                if (eventY > canvasHeight) eventY = canvasHeight;
                if (eventY < 0) eventY = 0;
                path.lineTo(eventX, eventY);

                break;
            case MotionEvent.ACTION_MOVE:

                path.reset();
                path.moveTo(centerX, centerY);
                if (eventX > canvasWidth) eventX = canvasWidth;
                if (eventX < 0) eventX = 0;
                if (eventY > canvasHeight) eventY = canvasHeight;
                if (eventY < 0) eventY = 0;
                path.lineTo(eventX, eventY);

                break;
            case MotionEvent.ACTION_UP:
                moveYellowCircle = false;
                path.reset();

                break;
            default:

                return false;
        }

        // Schedules a repaint.
        invalidate();
        return true;
    }

    public void addObserver(DrawViewCallbackInterface dwci) {
        observers.add(dwci);
    }
}
