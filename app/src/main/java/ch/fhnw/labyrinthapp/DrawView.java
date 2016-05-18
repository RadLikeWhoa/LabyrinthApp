package ch.fhnw.labyrinthapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        final float centerX = canvas.getWidth() / 2;
        final float centerY = canvas.getHeight() / 2;

        canvas.drawCircle(centerX, centerY, 40, paint);

        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);

        canvas.drawCircle(centerX, centerY, 30, paint);
    }
}
