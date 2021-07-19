package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.buffetrestaurent.Adapter.PagerAdapter;
import com.example.buffetrestaurent.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    TabLayout mainTabLayout;
    ViewPager2 tabView;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTabLayout = findViewById(R.id.tabBar);
        tabView = findViewById(R.id.viewPage);

        getSupportActionBar().hide();

        FirebaseFirestore db = FirebaseFirestore.getInstance();



        pagerAdapter = new PagerAdapter(this
        );
        tabView.setAdapter(pagerAdapter);

        new TabLayoutMediator(mainTabLayout, tabView, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText(R.string.strSignIn);
                } else {
                    tab.setText(R.string.strSignUp);
                }
            }
        }).attach();
    }


    @Override
    public void onBackPressed() {

    }

    private String md5(String pass) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(pass.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch ( NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}