package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Staff;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaffChangePasswordActivity extends AppCompatActivity {

    String staffEmail;
    Staff staff;
    TextView txtPassword,txtConfirmPass;
    TextView txtPassError,txtCPassError;
    double staffRole;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strChangePass);
        setContentView(R.layout.activity_staff_change_password);
        staffEmail = getIntent().getStringExtra("USER_EMAIL"); //Get email of user from another activity
        staffRole = getIntent().getDoubleExtra("ROLE",0);
        /*
        Mapping view with layout
         */
        txtPassword = findViewById(R.id.staffChangePassword_txtPassword);
        txtConfirmPass = findViewById(R.id.staffChangePassword_txtConfirmPassword);
        txtPassError = findViewById(R.id.staffChangePassword_txtPasswordError);
        txtCPassError = findViewById(R.id.staffChangePassword_txtConfirmPasswordError);
        getStaff(); //Get current staff information
    }

    @Override
    /*
    Back button on supported bar
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , HomePageStaff.class );
                intent.putExtra("USER_EMAIL", staffEmail);
                intent.putExtra("ROLE", staffRole);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , HomePageStaff.class );
        intent.putExtra("USER_EMAIL", staffEmail);
        intent.putExtra("ROLE", staffRole);
        startActivity(intent);
        this.finish();
    }

    public void update_Click_Staff(View view) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");
        Matcher matcher = pattern.matcher(txtPassword.getText().toString());
        boolean check = true;
        if(txtPassword.getText().toString().isEmpty()){
            txtPassError.setText("Password can not empty !!!");
            check = false;
        }else if(!matcher.matches()) {
            txtPassError.setText("Password must have 8 to 20 characters,at least 1 digit,lowercase character,uppercase character,special character");
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

    }

    /**
     * Get staff information
     */
    public void getStaff(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
        Get staff information with email from database
         */
        db.collection("staffs")
                .whereEqualTo("staffEmail",staffEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            staff = document.toObject(Staff.class);
                        }
                    }
                })
        ;
    }


    /**
     * Check new password is duplicate with old password
     */
    public void checkPassword(){
        String password = txtPassword.getText().toString();
        if(staff.getStaffPassword().equals(md5(password))){
            txtPassError.setText("Password has been existed");
        }else{
            updateToDB();
            Intent intent;
            intent= new Intent(this , HomePageStaff.class );
            intent.putExtra("USER_EMAIL", staffEmail);
            intent.putExtra("ROLE",staffRole);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Update staff new password to database
     */
    public void updateToDB(){
        Map<String, Object> data = new HashMap<>();
        data.put("staffPassword", md5(txtPassword.getText().toString()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("staffs").document(staff.getStaffId())
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(StaffChangePasswordActivity.this,"Update successful !",Toast.LENGTH_LONG).show();
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        // auth.updateCurrentUser(user);
                        user.updatePassword(md5(txtPassword.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    txtPassword.setText("");
                                    txtConfirmPass.setText("");
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull @NotNull Exception e) {
                        Log.e("Error:",e.getMessage());
                        Toast.makeText(StaffChangePasswordActivity.this,"Error !!!!",Toast.LENGTH_LONG).show();
                    }
                });;
    }


    /**
     * MD5 hash
     * @param pass Password of user
     * @return Hash string of user's password
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
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}