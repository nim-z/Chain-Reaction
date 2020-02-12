package com.example.chain_reaction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.Queue;

public class Cell {
    Context context;
    int x, y;
    String COLORS[] = {"white", "red", "green", "yellow", "blue"};
    int color, critmass, atoms;
    ImageView balls;
    Cell neighbours[] = new Cell[4];

    public Cell(Context context) {
        color = 0;
        critmass = 0;
        atoms = 0;
        this.context = context;
        balls = new ImageView(context);
        drawBalls();
//        balls.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                atoms++;
//                drawBalls(atoms);
//            }
//        });

    }

    public void linkNeighbours(int i, int j, Cell cells[][]) {
        if (i > 0) {
            critmass++;
            neighbours[0] = cells[i - 1][j];
        }
        if (i < 8) {
            critmass++;
            neighbours[1] = cells[i + 1][j];
        }
        if (j > 0) {
            critmass++;
            neighbours[2] = cells[i][j - 1];
        }
        if (j < 5) {
            critmass++;
            neighbours[3] = cells[i][j + 1];
        }
        critmass--;
        x = i;
        y = j;
    }

    public Queue<Cell> overload() {
        Queue<Cell> cellQueue = new LinkedList<>();
        if (this.atoms > this.critmass) {
            this.atoms = 0;
            Log.d("cells","cellQueue : cell : < "+x+" , "+y+" > "+atoms);
            for (int i = 0; i < 4; i++) {
                if (neighbours[i] != null) {
                    neighbours[i].atoms++;
                    neighbours[i].color = color;
                    //drawBalls();
                    cellQueue.add(neighbours[i]);
                    Log.d("cells", "cellQueue : neighbours : < " + neighbours[i].x + " , " + neighbours[i].y + " > " + neighbours[i].atoms);
                }
            }
        }
        Log.d("cells", "cellQueue ");
        return cellQueue;
    }


        public void drawBalls()
        {
            Bitmap bitmap = Bitmap.createBitmap(140, 140, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();

            paint.setColor(Color.parseColor(COLORS[color]));
            if (atoms == 1)
                canvas.drawCircle(70.0f, 70.0f, 20.0f, paint);
            else if (atoms == 2) {
                canvas.drawCircle(85.0f, 55.0f, 20.0f, paint);
                canvas.drawCircle(55.0f, 85.0f, 20.0f, paint);
            } else if (atoms == 3) {
                canvas.drawCircle(70.0f, 55.0f, 20.0f, paint);
                canvas.drawCircle(55.0f, 85.0f, 20.0f, paint);
                canvas.drawCircle(85.0f, 85.0f, 20.0f, paint);
        }

        balls.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
    }
}
