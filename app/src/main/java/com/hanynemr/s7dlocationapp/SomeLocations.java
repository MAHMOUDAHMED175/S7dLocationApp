package com.hanynemr.s7dlocationapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class SomeLocations extends AppCompatActivity {
    TextView data;
    TextView price;
    TextView litr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_some_locations);
        data=findViewById(R.id.data);
        price=findViewById(R.id.price);
        litr=findViewById(R.id.litr);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.price), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String value = intent.getStringExtra("data@data");


//        String value = "29.124524558,31.542216,48.45512545,29.132445,29.3254651,29.325465130.4512545";
        List<double[]> locations = parseLocations(value);
        calculateDistances(locations);



    }
    public void calculateDistances(List<double[]> locations) {
        StringBuilder distances = new StringBuilder();
        Location loc1 = new Location("");
        Location loc2 = new Location("");

        for (int i = 0; i < locations.size() - 1; i++) {
            double[] point1 = locations.get(i);
            double[] point2 = locations.get(i + 1);

            loc1.setLatitude(point1[0]);
            loc1.setLongitude(point1[1]);

            loc2.setLatitude(point2[0]);
            loc2.setLongitude(point2[1]);

            float distance = loc1.distanceTo(loc2);
            float distanceInKilometers = distance / 1000;

            distances.append(distanceInKilometers).append("\n");
            String[] distanceStrings = distances.toString().split("\n");
            float totalDistance = 0;

            for (String distStr : distanceStrings) {
                if (!distStr.isEmpty()) {
                    try {
                        totalDistance += Float.parseFloat(distStr);
                    } catch (NumberFormatException e) {
                        e.printStackTrace(); // معالجة الخطأ إذا كانت القيمة غير صالحة
                    }
                }
            }
            data.setText("مجموع المسافات بالكيلومترات: " + String.format("%.2f", totalDistance));
            float litres=(distanceInKilometers / 10);
            litr.setText("عدد اللترات المطلوبه::  "+ litres);
            float priceLitres=(litres * 13);

            price.setText("المبلغ المطلوب::  "+String.format("%.2f", priceLitres));
        }


    }




    public static List<double[]> parseLocations(String value) {
        List<double[]> locations = new ArrayList<>();
        String[] parts = value.split(",");
        for (int i = 0; i < parts.length - 1; i += 2) {
            double latitude = Double.parseDouble(parts[i]);
            double longitude = Double.parseDouble(parts[i + 1]);
            locations.add(new double[]{latitude, longitude});
        }

        return locations;

    }

}