package com.example.buffetrestaurent.Controller.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AddStaffActivity extends AppCompatActivity {

    EditText name,phone,email,address;
    TextView nameError,phoneError,emailError,addressError,passwordError,confirmPasswordError,password,confirmpassword;
    RadioButton gendermale,genderfemale;
    Button addStaff;
    RadioGroup groupGender;
    int gender;
    String getemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strAddStaff);
        getemail = getIntent().getStringExtra("USER_EMAIL");
        name = findViewById(R.id.txt_Staff_Name);
        nameError = findViewById(R.id.txt_Staff_Name_Error);
        email = findViewById(R.id.txt_Staff_Email);
        emailError = findViewById(R.id.txt_Staff_Email_Error);
        phone = findViewById(R.id.txt_Staff_Phone);
        phoneError = findViewById(R.id.txt_Staff_Phone_Error);
        address = findViewById(R.id.txt_Staff_Address);
        addressError = findViewById(R.id.txt_Staff_Address_Error);
        password = findViewById(R.id.txt_Staff_Password);
        passwordError = findViewById(R.id.txt_Staff_Password_Error);
        confirmpassword = findViewById(R.id.txt_Staff_RePassword);
        confirmPasswordError = findViewById(R.id.txt_Staff_RePassword_Error);
        gendermale = findViewById(R.id.txt_Staff_Male);
        genderfemale = findViewById(R.id.txt_Staff_Female);
        addStaff = findViewById(R.id.ad_staff_btn);
        groupGender = findViewById(R.id.group_Gender);
        gendermale.setChecked(true);
        groupGender.setOnCheckedChangeListener((group, checkedId) -> {
            if(gendermale.isChecked()){
                gender=0;
            }else{
                gender=1;
            }
        });
        addStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aname,aphone,aaddress,aemail,aimage,apassword,arepassword;
                int role=1,status=1;
                aname = name.getText().toString();
                aphone = phone.getText().toString();
                aaddress = address.getText().toString();
                aemail = email.getText().toString();
                apassword = password.getText().toString();
                arepassword = confirmpassword.getText().toString();
                System.out.println("This is password"+apassword);
                boolean cname,cphone,cemail,cpass,crepass,caddress;
                if(aname.isEmpty()){
                    nameError.setText("Name must more than 1 character");
                    cname=false;
                }else{
                    nameError.setText("");
                    cname=true;
                }

                Pattern patterna = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

                if(!patterna.matcher(aemail).matches()){
                    emailError.setText("Wong email format. Exp:abc@gamil.com");
                    cemail=false;
                }else{
                    emailError.setText("");
                    cemail=true;
                }

                Pattern p = Pattern.compile("(0[1-9])+([0-9]{8})");
                if(!p.matcher(aphone).matches()){
                    phoneError.setText("Wrong phone format. Exp:0123456789");
                    cphone=false;
                }else{
                    phoneError.setText("");
                    cphone=true;
                }

                if(aaddress.isEmpty()){
                    addressError.setText("Address can not empty");
                    caddress=false;
                }else{
                    addressError.setText("");
                    caddress=true;
                }
                Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");
                if(!pattern.matcher(apassword).matches()){
                    passwordError.setText("Password must have 8 to 20 characters,at least 1 digit,lowercase character,uppercase character,special character");
                    cpass=false;
                }else{
                    passwordError.setText("");
                    cpass=true;
                }

                if(!apassword.equals(arepassword)){
                    confirmPasswordError.setText("Password not match");
                    crepass=false;
                }else{
                    confirmPasswordError.setText("");
                    crepass=true;
                }
                if(cname == true && caddress == true && cemail == true && cpass == true && crepass == true && cphone == true){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("customers")
                            .whereEqualTo("customerEmail",aemail)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot doc= task.getResult();
                                    if (!doc.isEmpty()) {
                                        emailError.setText("Email has already exist");
                                    }
                                    else{
                                        db.collection("staffs")
                                                .whereEqualTo("staffEmail",aemail)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        QuerySnapshot doc= task.getResult();
                                                        if (!doc.isEmpty()) {
                                                            emailError.setText("Email has already exist");
                                                        }
                                                        else {
                                                            emailError.setText("");
                                                            Map<String ,Object> data =  new HashMap<>();
                                                            data.put("staffAddress",aaddress);
                                                            data.put("staffEmail",aemail);
                                                            data.put("staffGender",gender);
                                                            data.put("staffId","");
                                                            data.put("staffImage","https://firebasestorage.googleapis.com/v0/b/buffetrestaurant-e631f.appspot.com/o/staff.jpg?alt=media&token=e2ce6ef3-6e3b-42a9-b1c4-7e7242f7cfd8");
                                                            data.put("staffName",aname);
                                                            data.put("staffPassword",md5(apassword));
                                                            data.put("staffPhone",aphone);
                                                            data.put("staffRole",1);
                                                            data.put("staffStatus",1);
                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                            db.collection("staffs")
                                                                    .add(data)
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            FirebaseAuth auth = FirebaseAuth.getInstance();
                                                                            auth.createUserWithEmailAndPassword(aemail, md5(apassword)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                                                                                }
                                                                            });
                                                                            Map<String ,Object> data =  new HashMap<>();
                                                                            data.put("staffId",documentReference.getId());
                                                                            db.collection("staffs").document(documentReference.getId())
                                                                                    .update(data);
                                                                            Intent intent = new Intent(v.getContext(), StaffManageActivity.class);
                                                                            intent.putExtra("USER_EMAIL", getemail);
                                                                            startActivity(intent);
                                                                            finish();
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });
    }
    private String md5(String pass) {
        String newpass = "";
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
            newpass =  hashtext;
        }
            catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return newpass;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, StaffManageActivity.class);
                intent.putExtra("USER_EMAIL", getemail);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }

}
