package com.example.buffetrestaurent.Controller.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.buffetrestaurent.Controller.Fragment.staffMenuContent;
import com.example.buffetrestaurent.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

public class MenuMangeForStaff extends AppCompatActivity {

    String userEmail; // Save user email
    CollapsibleCalendar collapsibleCalendar; // object of calendar library
    FloatingActionButton btnAdd; //floating button for add food to menu
    String date; // Save user pick date
    double staffRole; // Save staff role
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_mange_for_staff);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strMenuStaff);

        /*
        Get system data from parent activity
         */
        userEmail= getIntent().getStringExtra("USER_EMAIL");
        staffRole = getIntent().getDoubleExtra("ROLE",0);

        /*
        Get view by id
         */
        btnAdd = findViewById(R.id.staff_menu_btnAdd);
        collapsibleCalendar=findViewById(R.id.staff_menu_calendarView);
        collapsibleCalendar.changeToToday();
        collapsibleCalendar.setExpandIconVisible(false);
        collapsibleCalendar.setExpanded(false);

        /*
        Set default fragment when first run activity set current date to today
         */
        Day day = collapsibleCalendar.getSelectedDay();
        String selectedDay= day.getYear() + "/" + (day.getMonth()) + "/" + day.getDay();
        Fragment selectedFragment = staffMenuContent.newInstance(selectedDay);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.staff_menu_content, selectedFragment);
        transaction.commit();
        date= day.getYear() + "/" + (day.getMonth()) + "/" + day.getDay();
        /*
        Set click event for button add. Intent to AddMenu activity
         */
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(v.getContext(), AddMenuActivity.class);
                intent.putExtra("SELECTED_DATE", date);
                intent.putExtra("USER_EMAIL", userEmail);
                intent.putExtra("ROLE", staffRole);
                startActivity(intent);
                finish();
            }
        });

        /*
        Set event for calendar
         */
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDayChanged() {

            }

            @Override
            public void onClickListener() {

            }

            /**
             * Change fragment base on user pick date
             */
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                Log.i(getClass().getName(), "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth()+1) + "/" + day.getDay());
                date= day.getYear() + "/" + (day.getMonth()+1) + "/" + day.getDay();
                Fragment selectedFragment = staffMenuContent.newInstance(date);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.staff_menu_content, selectedFragment);
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
    /*
    Back button on supported bar
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , HomePageStaff.class );
                intent.putExtra("USER_EMAIL", userEmail);
                intent.putExtra("ROLE", staffRole);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }

    /*
   Back button on phone
    */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , HomePageStaff.class );
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("ROLE", staffRole);
        startActivity(intent);
        this.finish();
    }
}