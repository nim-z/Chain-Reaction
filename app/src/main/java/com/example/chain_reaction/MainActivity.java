package com.example.chain_reaction;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String COLORS[]={"white","red","green","yellow","blue"};
    GridLayout board;
    int players=2,color=0;
    int i=0,j=0;
    Cell cells[][]=new Cell[9][6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        board=findViewById(R.id.board);
        board.setBackgroundColor(Color.parseColor(COLORS[1]));
        init();


        players=2;

    }
    public void init()
    {
        color=0;
        board.setBackgroundColor(Color.parseColor(COLORS[1]));
        for(int i=0;i<9;i++)
            for(int j=0;j<6;j++)
                cells[i][j]=new Cell(this);

        for(i=0;i<9;i++) {
            for(j=0;j<6;j++) {
                Log.d("cells in board","row : "+i+" :: column : "+j);
                GridLayout.LayoutParams params=new GridLayout.LayoutParams();
                params.width=140;
                params.height=140;
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
        link();
    }

    public int nextColor()
    {
        int tempcolor=(color + 1) % players;
        tempcolor = (tempcolor==0)?players:tempcolor;
        return tempcolor;
    }
    public void link()
    {
        StringBuffer cm=new StringBuffer();
        for(int i=0;i<9;i++)
        {
            for(int j=0;j<6;j++)
            {
                cells[i][j].linkNeighbours(i,j,cells);
                cm.append(cells[i][j].critmass+" ");

                final int x=i,y=j;
                cells[x][y].balls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tempcolor=color + 1;
                        tempcolor = (tempcolor>players)?1:tempcolor;
                        if(cells[x][y].color==0 || cells[x][y].color==tempcolor) {
                            color = tempcolor;
                            cells[x][y].color = color;
                            cells[x][y].atoms++;
                            cells[x][y].overload();
                            Log.d("colors",Integer.toString(color));
                            check();
                            tempcolor=color+1;
                            tempcolor=(tempcolor>players)?1:tempcolor;
                            board.setBackgroundColor(Color.parseColor(COLORS[tempcolor]));
                        }
                    }
                });
            }
            cm.append("\n");
        }
        Log.d("cells","CRITICAL MASS : \n"+cm.toString());
    }

    public void check()
    {
        boolean exist[]=new boolean[6];
        StringBuffer col=new StringBuffer();
        for(int i=0;i<9;i++) {
            for (int j = 0; j < 6; j++) {
                col.append(cells[i][j].color + " ");
                exist[cells[i][j].color] |= true;
            }
            col.append("\n");
        }
        Log.d("check","COLORS : \n"+col.toString());
        int pos=-1;
        for(int i=1;i<=players;i++)
        {
            Log.d("check",i+" : Exist : "+exist[i]);
            if(exist[i]==true && pos==-1)
                pos=i;
            else if(exist[i]==true)
            {
                pos=0;
                break;
            }
        }
        if(pos>0)
        {
            Toast.makeText(MainActivity.this,"Player "+pos+" WON .",Toast.LENGTH_SHORT).show();
        }
    }
}
