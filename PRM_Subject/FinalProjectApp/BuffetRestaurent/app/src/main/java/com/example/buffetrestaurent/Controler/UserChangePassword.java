package com.example.buffetrestaurent.Controler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.R;
import com.example.buffetrestaurent.Utils.Apis;
import com.example.buffetrestaurent.Utils.CustomerService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserChangePassword extends AppCompatActivity {

    String userEmail;
    CustomerService customerService;
    TextView txtPassword,txtConfirmPass;
    TextView txtPassError,txtCPassError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password);
        userEmail= getIntent().getStringExtra("USER_EMAIL");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strChangePass);
        txtPassword = findViewById(R.id.changePassword_txtPassword);
        txtConfirmPass = findViewById(R.id.changePassword_txtConfirmPassword);
        txtPassError = findViewById(R.id.changePassword_txtPasswordError);
        txtCPassError = findViewById(R.id.changePassword_txtConfirmPasswordError);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , HomePage.class );
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                return true;
        }
        return true;
    }

    public void update_Click(View view) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");
        Matcher matcher = pattern.matcher(txtPassword.getText().toString());
        boolean check = true;
        if(txtPassword.getText().toString().isEmpty()){
            txtPassError.setText("Password can not empty !!!");
            check = false;
        }else if(!matcher.matches()) {
            txtPassError.setText("Password are not match strong");
            check = false;
        }
        else{
            txtPassError.setText("");
        }
        if(txtConfirmPass.getText().toString().isEmpty()){
            txtCPassError.setText("Confirm Password can not empty !!!");
        }
        else if(!txtPassword.getText().toString().equals(txtConfirmPass.getText().toString())){
            txtCPassError.setText("Confirm Password are not match !!!");
        }else if(check){
            checkPassword();
            txtCPassError.setText("");
        }
        else {
            txtCPassError.setText("");

        }

    }
    public void checkPassword(){
    customerService = Apis.getCustomerService();
    Call<Boolean> call=customerService.checkPassword(txtPassword.getText().toString(),userEmail);
    call.enqueue(new Callback<Boolean>() {
        @Override
        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
            if(response.isSuccessful()) {
               boolean check = response.body();
               if(check){
                   updateToDB();
               }else{
                   txtPassError.setText("Password is alredady exist");
               }
            }

        }

        @Override
        public void onFailure(Call<Boolean> call, Throwable t) {
            Log.e("Error:",t.getMessage());
            Toast.makeText(UserChangePassword.this,"Error !!!!",Toast.LENGTH_LONG).show();
        }
    });
    }
    public void updateToDB(){
        customerService = Apis.getCustomerService();
        Call<Boolean> call=customerService.updateCusPassword(txtPassword.getText().toString(),userEmail);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(UserChangePassword.this,"Update successful !",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("Error:",t.getMessage());
                Toast.makeText(UserChangePassword.this,"Error !!!!",Toast.LENGTH_LONG).show();
            }
        });
    }
}