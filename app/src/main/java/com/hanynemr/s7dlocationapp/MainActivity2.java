package com.hanynemr.s7dlocationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mumayank.com.airlocationlibrary.AirLocation;
public class MainActivity2 extends AppCompatActivity implements AirLocation.Callback {

    List<EditText> addressFields = new ArrayList<>();

    EditText addText;
    TextView resText;
    AirLocation airLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        addText = findViewById(R.id.addText);
        resText = findViewById(R.id.resText);
        airLocation = new AirLocation(this, this, true, 0, "");
        airLocation.start();

        // Add the first EditText to the list
        addressFields.add(addText);
    }

    public void addLocationField(View view) {
        Geocoder geocoder = new Geocoder(this);
        resText.setText("");
        Set<String> addressSet = new HashSet<>();

        for (EditText addressField : addressFields) {
            String address = addressField.getText().toString().trim();

            if (address.isEmpty()) {
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(5)
                        .playOn(addressField);
            } else if (!addressSet.add(address)) {
                Toast.makeText(this, "Duplicate address found: " + address, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        try {
            for (EditText addressField : addressFields) {
                String address = addressField.getText().toString();
                List<Address> addressList = geocoder.getFromLocationName(address, 1);
                if (addressList.isEmpty()) {
                    Toast.makeText(this, "Place not found: " + address, Toast.LENGTH_SHORT).show();
                    return;
                }
                double latitude = addressList.get(0).getLatitude();
                double longitude = addressList.get(0).getLongitude();

                resText.append(latitude +","+longitude + ",");

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // Create a new EditText for additional location input
        EditText newAddressField = new EditText(this);
        newAddressField.setHint("Enter address");
        newAddressField.setInputType(InputType.TYPE_CLASS_TEXT);
        // Get the LinearLayout and add the new EditText to it
        LinearLayout layout = findViewById(R.id.linearLayout);
        int lastEditTextIndex = layout.indexOfChild(addressFields.get(addressFields.size() - 1));
        layout.addView(newAddressField, lastEditTextIndex + 1);
        // Add the new EditText to the list of address fields
        addressFields.add(newAddressField);
    }
    public void removeLastField(View view) {
        // تأكد أن هناك أكثر من حقل واحد، لكي لا يتم إزالة الحقل الأصلي
        if (addressFields.size() > 1) {
            // الحصول على الـ LinearLayout
            LinearLayout layout = findViewById(R.id.linearLayout);

            // تحديد آخر حقل EditText
            EditText lastField = addressFields.get(addressFields.size() - 1);

            // إزالة الـ EditText من الـ LinearLayout
            layout.removeView(lastField);

            // إزالة الـ EditText من القائمة
            addressFields.remove(addressFields.size() - 1);
        } else {
            Toast.makeText(this, "لا يمكن إزالة الحقل الأخير!", Toast.LENGTH_SHORT).show();
        }
    }

    public void show(View view) {
        if (!resText.getText().equals("")) {
         Intent intent = new Intent(MainActivity2.this, SomeLocations.class);
            intent.putExtra("data@data", resText.getText().toString());
            startActivity(intent);
        }

    }

    @Override
    public void onFailure(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {
        Toast.makeText(this, "Error in getting location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(@NonNull ArrayList<Location> locations) {
        double latitude = locations.get(0).getLatitude();
        double longitude = locations.get(0).getLongitude();
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            addText.setText("\n" + addressList.get(0).getAddressLine(0));
        } catch (IOException e) {
            Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show();
        }
    }
}
