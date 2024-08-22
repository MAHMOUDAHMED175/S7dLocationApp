package com.hanynemr.s7dlocationapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class bestRoute extends AppCompatActivity {

   TextView routeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_best_route);
        routeTextView=findViewById(R.id.routeTextView);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
             String[] routeAddresses = intent.getStringArrayExtra("Route");
        if (routeAddresses != null) {
            StringBuilder routeDisplay = new StringBuilder();
            for (String address : routeAddresses) {
                routeDisplay.append(address).append("\n\n--->>>");
            }
            routeTextView.setText(routeDisplay.toString());
            routeTextView.setMovementMethod(new ScrollingMovementMethod());
        }

    }
}