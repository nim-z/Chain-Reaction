package com.example.chain_reaction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
GridLayout board;
int players=2,color=0;
int i=0,j=0;
Cell cells[][]=new Cell[9][6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        board=findViewById(R.id.board);
        init();
        link();

        players=2;

    }
    public void init()
    {
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
                        int tempcolor=(color + 1) % players;
                        tempcolor = (tempcolor==0)?players:tempcolor;
                        if(cells[x][y].color==0 || cells[x][y].color==tempcolor) {
                            color = tempcolor;
                            cells[x][y].color = color;
                            cells[x][y].atoms++;
                            boolean run=cells[x][y].overload();
                            if(run)
                            {
                                boolean win=check();
                                if(win) {
                                    Toast.makeText(MainActivity.this, "Player "+color+" Wins", Toast.LENGTH_SHORT).show();
                                    init();
                                }
                            }
                            Log.d("colors",Integer.toString(color));
                        }

                    }
                });
            }
            cm.append("\n");
        }
        Log.d("cells","CRITICAL MASS : \n"+cm.toString());
    }
    public boolean check()
    {
        int score1=0,score2=0;
        for(int i=0;i<9;i++)
        {
            for(int j=0;j<6;j++)
            {
                if(cells[i][j].color==1 && cells[i][j].atoms>0)
                {
                    score1=score1+cells[i][j].atoms;
                }
                if(cells[i][j].color==2 && cells[i][j].atoms>0)
                {
                    score2=score2+cells[i][j].atoms;
                }
            }
        }
        if((score1==0 && score2>0)|| (score2==0 && score1>0))
            return true;
        else
            return false;
    }
}
