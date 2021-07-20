package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Toast;

import com.example.buffetrestaurent.Adapter.PagerAdapter;
import com.example.buffetrestaurent.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    TabLayout mainTabLayout; // Tab layout to switch bettween sign up and sign in
    ViewPager2 tabView; // Content fragment of sign up and sign in
    PagerAdapter pagerAdapter; // Adapter of tabView
    private long pressedTime; // Save user back pressed time to exit program

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        Get view by id
         */
        setContentView(R.layout.activity_main);
        mainTabLayout = findViewById(R.id.tabBar);
        tabView = findViewById(R.id.viewPage);

        getSupportActionBar().hide(); // hiden action bar

        /*
        Create and apply adapter
         */
        pagerAdapter = new PagerAdapter(this);
        tabView.setAdapter(pagerAdapter);

        /*
        Set title for tabView
         */
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

    /**
     * Method check if user click back two time in 2 second app will exit
     */
    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    /**
     * Method convert input string to md5 string
     * @param pass
     * @return
     */
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