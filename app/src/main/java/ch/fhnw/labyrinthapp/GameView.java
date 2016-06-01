package ch.fhnw.labyrinthapp;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class GameView extends View {
    protected Paint paint = new Paint();
    protected Path path = new Path();
    protected float eventX = -1, eventY = -1, centerX, centerY, canvasHeight, canvasWidth;
    protected int xTo180, yTo180;

    public GameView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    protected List<PositionUpdateInterface> observers = new ArrayList<>();

    public void addObserver(PositionUpdateInterface pui) {
        observers.add(pui);
    }

    public void removeObserver(PositionUpdateInterface pui) { observers.remove(pui); }
}
