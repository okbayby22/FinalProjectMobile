package com.example.buffetrestaurent.Controller.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strProfile);
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
                    System.out.println("Name"+aname);
                    System.out.println("Phone"+aphone);
                    System.out.println("Email"+aemail);
                    System.out.println("Gender"+gender);
                    System.out.println("Pass"+apassword);
                    System.out.println("Repass"+arepassword);
                    System.out.println("Address"+aaddress);
                    Map<String ,Object> data =  new HashMap<>();
                    data.put("staffAddress",aaddress);
                    data.put("staffEmail",aemail);
                    data.put("staffGender",gender);
                    data.put("staffId","");
                    data.put("staffImage","");
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
                                    Map<String ,Object> data =  new HashMap<>();
                                    data.put("staffId",documentReference.getId());
                                    db.collection("staffs").document(documentReference.getId())
                                            .update(data);
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
}
