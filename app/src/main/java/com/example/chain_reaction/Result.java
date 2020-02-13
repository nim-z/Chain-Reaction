package com.example.chain_reaction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Result extends AppCompatActivity {
    String COLORS[] = {"White", "Red", "Green", "Yellow", "Blue"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_result);

        Intent received = getIntent();
        int color = received.getIntExtra("Player", 0);
        TextView winner = findViewById(R.id.winner);
        Log.d("resultACTIVITY","color : "+color);
        winner.setText(COLORS[color] + " Won. ");
        winner.setTextColor(Color.parseColor(COLORS[color]));

            Button reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent();
                response.putExtra("reset", true);
                setResult(Activity.RESULT_OK, response);
                finish();
            }
        });
    }
}
