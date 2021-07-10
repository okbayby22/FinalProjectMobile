package com.example.buffetrestaurent.Controler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.buffetrestaurent.HomePageStaff;
import com.example.buffetrestaurent.MenuMangeForStaff;
import com.example.buffetrestaurent.R;

public class AddMenuActivity extends AppCompatActivity {

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strMenuStaffAddFood);

        userEmail= getIntent().getStringExtra("USER_EMAIL");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , MenuMangeForStaff.class );
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , MenuMangeForStaff.class );
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);
        this.finish();
    }
}