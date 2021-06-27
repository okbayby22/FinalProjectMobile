package com.example.buffetrestaurent.Controler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.R;
import com.example.buffetrestaurent.Utils.Apis;
import com.example.buffetrestaurent.Utils.CustomerService;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity {

    String userEmail;
    CustomerService customerService;
    Customer customerInfor;
    TextView txtName,txtEmail,txtPhone,txtAddress;
    TextView txtNameError,txtPhoneError;
    //Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strProfile);

        txtName=findViewById(R.id.userInfor_txtName);
        txtEmail=findViewById(R.id.userInfor_txtEmail);
        txtPhone=findViewById(R.id.userInfor_txtPhone);
        txtAddress=findViewById(R.id.userInfor_txtAddress);

        txtNameError=findViewById(R.id.userInfor_txtName_error);
        txtPhoneError=findViewById(R.id.userInfor_txtPhone_error);

        txtEmail.setEnabled(false);
        customerInfor =new Customer();
        userEmail= getIntent().getStringExtra("USER_EMAIL");
        loadData();
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

    public void loadData(){
        customerService = Apis.getCustomerService();
        Call<Customer> call=customerService.getUserInfor(userEmail);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()) {
                    customerInfor = response.body();
                    txtName.setText(customerInfor.getCustomerEmail());
                    txtAddress.setText(customerInfor.getCustomerAddress());
                    txtPhone.setText(customerInfor.getCustomerPhone());
                    txtEmail.setText(customerInfor.getCustomerEmail());
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("Error:",t.getMessage());
            }
        });
    }

    public void update_Click(View view) {
        boolean nameStatus=true;
        boolean phoneStatus=true;
        Pattern pattern = Pattern.compile("(0[1-9])+([0-9]{8})");
        Matcher matcher = pattern.matcher(txtPhone.getText().toString());
        if(txtName.getText().toString().isEmpty()){
            txtNameError.setText("Name can not empty !!!");
            nameStatus=false;
        }
        else{
            nameStatus=true;
            txtNameError.setText("");
        }
        if(txtPhone.getText().toString().isEmpty()){
            txtPhoneError.setText("Phone number can not empty !!!");
            phoneStatus=false;
        }
        else if(!matcher.matches()){
            txtPhoneError.setText("Wrong phone format !!! Ex: 0123456789");
            phoneStatus=false;
        }
        else{
            txtPhoneError.setText("");
            phoneStatus=true;
        }

        if(nameStatus&&phoneStatus){
            customerInfor.setCustomerName(txtName.getText().toString());
            customerInfor.setCustomerPhone(txtPhone.getText().toString());
            customerInfor.setCustomerAddress(txtAddress.getText().toString());
            updateToDB();
        }
    }

    public void updateToDB(){
        customerService = Apis.getCustomerService();
        Call<Customer> call=customerService.updateCusInfor(customerInfor);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(UserProfile.this,"Update successful !",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("Error:",t.getMessage());
                Toast.makeText(UserProfile.this,"Error !!!!",Toast.LENGTH_LONG).show();
            }
        });
    }
}