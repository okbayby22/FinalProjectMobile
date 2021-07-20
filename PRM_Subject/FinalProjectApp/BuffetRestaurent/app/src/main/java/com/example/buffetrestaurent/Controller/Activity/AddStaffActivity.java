package com.example.buffetrestaurent.Controller.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
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
    Button addStaff,upImage;
    RadioGroup groupGender;
    int gender;
    double role;
    String getemail;
    ImageView avatar;
    Uri imageUri;
    String getUrlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strAddStaff);
        getemail = getIntent().getStringExtra("USER_EMAIL");
        role = getIntent().getDoubleExtra("ROLE",0);
        upImage = findViewById(R.id.addstaff_btn_Image);
        avatar = findViewById(R.id.txt_Staff_Image);
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
        //load default image to imageview
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/buffetrestaurant-e631f.appspot.com/o/staff.jpg?alt=media&token=e2ce6ef3-6e3b-42a9-b1c4-7e7242f7cfd8").into(avatar);
        //check use choose gender by click radio button
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
                //get information of staff
                aname = name.getText().toString();
                aphone = phone.getText().toString();
                aaddress = address.getText().toString();
                aemail = email.getText().toString();
                apassword = password.getText().toString();
                arepassword = confirmpassword.getText().toString();
                System.out.println("This is password"+apassword);
                //variable check input status
                boolean cname,cphone,cemail,cpass,crepass,caddress;
                if(aname.isEmpty()){//display error if name is empty, set status for check name variable
                    nameError.setText("Name must more than 1 character");
                    cname=false;
                }else{
                    nameError.setText("");
                    cname=true;
                }

                Pattern patterna = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

                if(!patterna.matcher(aemail).matches()){//display error if email wrong format, set status for check email variable
                    emailError.setText("Wong email format. Exp:abc@gmail.com");
                    cemail=false;
                }else{
                    emailError.setText("");
                    cemail=true;
                }

                Pattern p = Pattern.compile("(0[1-9])+([0-9]{8})");
                if(!p.matcher(aphone).matches()){//display error if phone wrong format, set status for check phone variable
                    phoneError.setText("Wrong phone format. Exp:0123456789");
                    cphone=false;
                }else{
                    phoneError.setText("");
                    cphone=true;
                }

                if(aaddress.isEmpty()){//display error if address is empty, set status for check address variable
                    addressError.setText("Address can not empty");
                    caddress=false;
                }else{
                    addressError.setText("");
                    caddress=true;
                }

                Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");
                if(!pattern.matcher(apassword).matches()){//display error if password wrong format, set status for check password variable
                    passwordError.setText("Password must have 8 to 20 characters,at least 1 digit,lowercase character,uppercase character,special character");
                    cpass=false;
                }else{
                    passwordError.setText("");
                    cpass=true;
                }

                if(!apassword.equals(arepassword)){//display error if confirm password not match with password, set status for check confirm password variable
                    confirmPasswordError.setText("Password not match");
                    crepass=false;
                }else{
                    confirmPasswordError.setText("");
                    crepass=true;
                }
                if(cname == true && caddress == true && cemail == true && cpass == true && crepass == true && cphone == true){
                    //if all fields are true, check duplicate email
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("customers")
                            .whereEqualTo("customerEmail",aemail)//check duplicate email in customer table
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot doc= task.getResult();
                                    if (!doc.isEmpty()) {//if email is exist, display error
                                        emailError.setText("Email has already exist");
                                    }
                                    else{
                                        db.collection("staffs")
                                                .whereEqualTo("staffEmail",aemail)//check duplicate email in staff table
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        QuerySnapshot doc= task.getResult();
                                                        if (!doc.isEmpty()) {//if email is exist, display error
                                                            emailError.setText("Email has already exist");
                                                        }
                                                        else {
                                                            uploadImageFirebase(aname,  aemail, aphone, aaddress, apassword);

                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });
        upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
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

    public void openFileChoose(){
        Intent intent = new Intent();
        intent.setType("image/*");//open library image in mobile
        intent.setAction((Intent.ACTION_GET_CONTENT));
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==2 && resultCode == RESULT_OK && data !=null){//get image and display image to screen if user choose image
            imageUri=data.getData();
            avatar.setImageURI(imageUri);
        }
    }

    public void uploadImageFirebase(String aname, String aemail,String aphone,String aaddress,String apassword){
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://buffetrestaurant-e631f.appspot.com");
        StorageReference storageRef = storage.getReference();


// Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("Avatar/"+aemail+".jpg");

// While the file names are the same, the references point to different files
        mountainImagesRef.getName().equals(mountainImagesRef.getName());    // true
        mountainImagesRef.getPath().equals(mountainImagesRef.getPath());    // false
        // Get the data from an ImageView as bytes
        avatar.setDrawingCacheEnabled(true);
        avatar.buildDrawingCache();

        Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return mountainImagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    getUrlImage = downloadUri.toString();
                    emailError.setText("");
                    Map<String ,Object> data =  new HashMap<>();
                    data.put("staffAddress",aaddress);
                    data.put("staffEmail",aemail);
                    data.put("staffGender",gender);
                    data.put("staffId","");
                    data.put("staffImage",getUrlImage);
                    data.put("staffName",aname);
                    data.put("staffPassword",md5(apassword));
                    data.put("staffPhone",aphone);
                    data.put("staffRole",1);
                    data.put("staffStatus",1);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("staffs")//add staff to database
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
                                            .update(data);//update staff id match with firebase id
                                    Intent intent = new Intent(AddStaffActivity.this, StaffManageActivity.class);
                                    intent.putExtra("USER_EMAIL", getemail);
                                    intent.putExtra("ROLE", role);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    /**
     * Event onclick of button on supported bar
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , StaffManageActivity.class );
        intent.putExtra("USER_EMAIL", getemail);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        this.finish();
    }

    /**
     * Event of Onclick for Button
     * @param view
     */
    public void onClickAddStaff(View view){
        Intent intent = new Intent(this , StaffManageActivity.class );
        intent.putExtra("USER_EMAIL", getemail);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        this.finish();
    }
}
