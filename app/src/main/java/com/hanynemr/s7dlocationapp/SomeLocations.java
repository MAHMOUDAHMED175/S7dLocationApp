package com.hanynemr.s7dlocationapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        locations = parseLocations(value);
        calculateDistanc(locations);





         path = findPath(locations);

    }
    List<double[]> locations;
    List<Integer> path;
    public void goToBestRoute(View view){

        List<Integer> path = findPath(locations);

        System.out.println("Path visiting all points:");
        for (int i : path) {
            System.out.println("Point " + i + ": " + locations.get(i)[0] + ", " + locations.get(i)[1]);
        }


        List<String> addresses = convertPointsToAddresses(locations, path);

                Intent intent = new Intent(SomeLocations.this, bestRoute.class);
                intent.putExtra("Route",  addresses.toArray(new String[0]));
                startActivity(intent);

    }
    private List<String> convertPointsToAddresses(List<double[]> locations, List<Integer> path) {
        Geocoder geocoder = new Geocoder(this);
        List<String> addresses = new ArrayList<>();

        for (int index : path) {
            double[] location = locations.get(index);
            try {
                List<Address> addressList = geocoder.getFromLocation(location[0], location[1], 1);
                if (!addressList.isEmpty()) {
                    Address address = addressList.get(0);
                    StringBuilder addressString = new StringBuilder();
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        addressString.append(address.getAddressLine(i)).append("\n");
                    }
                    addresses.add(addressString.toString().trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return addresses;
    }
    // Method to calculate the distance between two points

    // Class to hold distance information


//calculateDistanceBetweenTwoPoint
//calculateDistancesBetweenAllPoints


    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // convert to km
    }

    // Class to hold distance information
    static class DistanceInfo {
        int fromIndex;
        int toIndex;
        double distance;

        DistanceInfo(int fromIndex, int toIndex, double distance) {
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
            this.distance = distance;
        }
    }

    // Method to calculate all distances between points
    public static List<DistanceInfo> calculateDistances(List<double[]> locations) {
        List<DistanceInfo> distanceInfos = new ArrayList<>();

        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                double[] point1 = locations.get(i);
                double[] point2 = locations.get(j);
                double distance = calculateDistance(
                        point1[0], point1[1], point2[0], point2[1]);
                distanceInfos.add(new DistanceInfo(i, j, distance));
            }
        }

        return distanceInfos;
    }

    // Method to find the shortest path visiting all points
    public static List<Integer> findPath(List<double[]> locations) {
        List<DistanceInfo> distances = calculateDistances(locations);

        List<Integer> path = new ArrayList<>();
        boolean[] visited = new boolean[locations.size()];
        int currentIndex = 0; // Starting point
        path.add(currentIndex);
        visited[currentIndex] = true;

        // Visit all points
        while (path.size() < locations.size()) {
            int nextIndex = -1;
            double minDistance = Double.MAX_VALUE;

            for (DistanceInfo info : distances) {
                if (info.fromIndex == currentIndex && !visited[info.toIndex] && info.distance < minDistance) {
                    minDistance = info.distance;
                    nextIndex = info.toIndex;
                }
            }

            if (nextIndex != -1) {
                path.add(nextIndex);
                visited[nextIndex] = true;
                currentIndex = nextIndex;
            } else {
                // If there are still unvisited points, find the closest one
                for (int i = 0; i < visited.length; i++) {
                    if (!visited[i]) {
                        path.add(i);
                        visited[i] = true;
                        currentIndex = i;
                        break;
                    }
                }
            }
        }

        return path;
    }




































    // Method to calculate all distances between points and sort them

    public void calculateDistanc(List<double[]> locations) {
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