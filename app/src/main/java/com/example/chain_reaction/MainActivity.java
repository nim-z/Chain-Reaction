package com.example.chain_reaction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    String COLORS[]={"white","red","green","yellow","blue"};
    GridLayout board;
    int players=2,color=0;
    int i=0,j=0,turns=1;
    Cell cells[][]=new Cell[9][6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        board=findViewById(R.id.board);
        init();players=2;

    }
    public void init()
    {
        color=0;turns=0;
        board.setBackgroundColor(Color.parseColor(COLORS[1]));
        for(int i=0;i<9;i++)
            for(int j=0;j<6;j++)
                cells[i][j]=new Cell(this);

        for(i=0;i<9;i++) {
            for(j=0;j<6;j++) {
                Log.d("cells in board","row : "+i+" :: column : "+j);
                GridLayout.LayoutParams params=new GridLayout.LayoutParams();
                params.width=160;
                params.height=160;
                params.bottomMargin=2;
                params.topMargin=2;
                params.leftMargin=2;
                params.rightMargin=2;
                params.rowSpec=GridLayout.spec(i);
                params.columnSpec=GridLayout.spec(j);
                cells[i][j].balls.setLayoutParams(params);
                board.addView(cells[i][j].balls);
                //cells[i][j].linkNeighbours(i,j,cells);
            }
        }
        firstlink();
        link();
    }

    public int nextColor() {
        int tempcolor=(color + 1) % players;
        tempcolor = (tempcolor==0)?players:tempcolor;
        return tempcolor;
    }
    public void firstlink(){
        for(int i=0;i<9;i++) {
            for(int j=0;j<6;j++) {
                cells[i][j].linkNeighbours(i,j,cells);
            }
        }
    }
    public void restartGame(){
        //cellQueue.clear();
        for(int i=0;i<9;i++) {
            for(int j=0;j<6;j++) {
                cells[i][j].resetCell();
            }
        }
        turns=0;
        color=0;
        board.setBackgroundColor(Color.parseColor(COLORS[1]));
    }
    public void drawAllBalls(){
        for(int i=0;i<9;i++)
        {
            for(int j=0;j<6;j++)
            {
                cells[i][j].drawBalls();
            }
        }
    }
    public void link() {
        final Vibrator vibe= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        for(int i=0;i<9;i++) {
            for(int j=0;j<6;j++) {
                final int x = i, y = j;
                cells[x][y].balls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vibe.vibrate(50);
                        if (cells[x][y].atoms == 0 || (cells[x][y].color == nextColor() && cells[x][y].atoms > 0)) {
                            color = nextColor();
                            cells[x][y].color = color;
                            cells[x][y].atoms++;
                            Queue<Cell> cellQueue=new LinkedList<>();
                            cellQueue.add(cells[x][y]);
                            cellQueue.addAll(cells[x][y].overload());
                            boolean run = cellQueue.size() > 1;
                            Log.d("colors", Integer.toString(color));
                            if(run)
                            {
                                Log.d("checking","check taking place in click");
                                check(cellQueue);
                            }
                            board.setBackgroundColor(Color.parseColor(COLORS[nextColor()]));
                            drawCells(cellQueue);
                        }
                        turns++;
                        printgrid();
                    }
                });
            }
        }
    }
    public void check(Queue<Cell> cellQueue)
    {
        int[] score=new int[6];
        //score[1]=0;
       // score[2]=0;
        for(int i=0;i<9;i++) {
            for(int j=0;j<6;j++) {
                if(cells[i][j].atoms>0)
                score[cells[i][j].color]+=cells[i][j].atoms;
            }
        }
        boolean wincondition = (score[1] == 0 && score[2] > 0) || (score[2] == 0 && score[1] > 0);
        Log.d("score","CHECK : "+score[1]+" : "+score[2]+" :: "+ wincondition);
        if ( (wincondition && turns >=2)) {
           // Toast.makeText(MainActivity.this, "Player" + color + "Won", Toast.LENGTH_SHORT).show();
            Log.d("cells","winner : "+color);
            Intent i=new Intent(MainActivity.this,Result.class);
            i.putExtra("Player",color);
            turns=0;
            //cellQueue.clear();
           // drawCells(cellQueue);
            startActivityForResult(i,1);
            restartGame();
        }
    }

    public void printgrid()
    {
        StringBuilder grid=new StringBuilder();
        for(int i=0;i<9;++i) {
            for (int j = 0; j < 6; ++j)
                grid.append(cells[i][j].atoms+"\t");
            grid.append("\n");
        }
        Log.d("cellgrid","Grid :\n"+grid.toString());
    }

    public void drawCells(Queue<Cell> cellQueue)
    {
        StringBuilder q=new StringBuilder();
        while(!cellQueue.isEmpty())
        {
            Cell cell=cellQueue.remove();
            q.append(" < "+cell.x+" , "+cell.y+" > \t");
            Queue<Cell> temp=cell.overload();
            if(temp.size()>0)
                cellQueue.addAll(temp);
            cell.drawBalls();
            check(cellQueue);
        }
        Log.d("cells","Queue , \n"+q.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Intentb/w","inside onactivityresult");
        if((requestCode==1)){
            if(resultCode== Activity.RESULT_OK)
            {
                boolean flag=data.getBooleanExtra("reset",false);
                Log.d("Intentb/w","flag : "+flag);
                if(flag){
                   drawAllBalls();
                }
            }
        }
    }
}
