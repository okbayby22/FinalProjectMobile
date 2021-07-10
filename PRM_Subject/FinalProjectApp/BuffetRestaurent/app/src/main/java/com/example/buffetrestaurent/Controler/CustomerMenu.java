package com.example.buffetrestaurent.Controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.buffetrestaurent.R;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

public class CustomerMenu extends AppCompatActivity {

    String userEmail;
    CollapsibleCalendar collapsibleCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strMenu);

        userEmail= getIntent().getStringExtra("USER_EMAIL");

        collapsibleCalendar=findViewById(R.id.customer_menu_calendarView);
        collapsibleCalendar.changeToToday();
        collapsibleCalendar.setExpandIconVisible(false);
        collapsibleCalendar.setExpanded(false);

        Day day = collapsibleCalendar.getSelectedDay();
        String selectedDay= day.getYear() + "/" + (day.getMonth()) + "/" + day.getDay();

        Fragment selectedFragment = customerMenuContent.newInstance(selectedDay);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.customer_menu_content, selectedFragment);
        transaction.commit();

        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDayChanged() {

            }

            @Override
            public void onClickListener() {

            }

            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                Log.i(getClass().getName(), "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth()+1) + "/" + day.getDay());
                String date= day.getYear() + "/" + (day.getMonth()+1) + "/" + day.getDay();
                Fragment selectedFragment = customerMenuContent.newInstance(date);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.customer_menu_content, selectedFragment);
                transaction.commit();
            }

            @Override
            public void onItemClick(View view) {

            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , HomePage.class );
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }
}