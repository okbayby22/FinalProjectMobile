package com.example.buffetrestaurent.Controller.Activity;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProfile extends AppCompatActivity {

    String userEmail,userID;
    Customer customerInfor;
    TextView txtName,txtEmail,txtPhone,txtAddress;
    TextView txtNameError,txtPhoneError;
    ImageView avt;
    Button btnuploadImage;
    Uri imageUri;
    Drawable oldimage;
    String getImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strProfile);

        /*
        Mapping view to layout
         */
        txtName=findViewById(R.id.userInfor_txtName);
        txtEmail=findViewById(R.id.userInfor_txtEmail);
        txtPhone=findViewById(R.id.userInfor_txtPhone);
        txtAddress=findViewById(R.id.userInfor_txtAddress);
        txtNameError=findViewById(R.id.userInfor_txtName_error);
        txtPhoneError=findViewById(R.id.userInfor_txtPhone_error);

        /*
        User can not edit email
         */
        txtEmail.setEnabled(false);
        txtEmail.setClickable(false);
        txtEmail.setAlpha((float) 0.3);
        avt = findViewById(R.id.userInfor_txtImage);
        btnuploadImage = findViewById(R.id.userInfor_Imagebtn);
        customerInfor =new Customer();
        userEmail= getIntent().getStringExtra("USER_EMAIL");
        loadData();
        btnuploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });
    }

    public void openFileChoose(){
        Intent intent = new Intent();
        intent.setType("image/*");//intent to image file in user's mobile
        intent.setAction((Intent.ACTION_GET_CONTENT));
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==2 && resultCode == RESULT_OK && data !=null){//if user choose image, display that image to screen
            imageUri=data.getData();
            avt.setImageURI(imageUri);//set image to imageview
        }
    }
    /**
     * Event of button on supported bar
     * @param item
     * @return
     */
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

    /**
     * Event of back button
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , HomePage.class );
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);
        this.finish();
    }

    /**
     * Load data of current user
     */
    public void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
        Load data of current user by email
         */
        db.collection("customers")
                .whereEqualTo("customerEmail", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                customerInfor = document.toObject(Customer.class);
                                /*
                                Binding to view
                                 */
                                txtName.setText(customerInfor.getCustomerName());
                                txtAddress.setText(customerInfor.getCustomerAddress());
                                txtPhone.setText(customerInfor.getCustomerPhone());
                                txtEmail.setText(customerInfor.getCustomerEmail());
                                userID = document.getId();
                                getImageUri = customerInfor.getCustomerAvatar();
                                GradientDrawable imgshape = new GradientDrawable();
                                imgshape.setShape(GradientDrawable.OVAL);
                                imgshape.setStroke(3, Color.BLACK);
                                imgshape.setCornerRadius(100);
                                Picasso.get().load(customerInfor.getCustomerAvatar()).into(avt);
                                oldimage=avt.getDrawable();//get data of old image
                                avt.setBackgroundDrawable(imgshape);
                            }
                        } else {

                        }
                    }
                });
    }

    /**
     * Method of button OnClick event
     * @param view
     */
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
            if(avt.getDrawable()!=oldimage){//if user upload new image, load that image to database and update profile
                uploadImageFirebase();
            }else{
                updateToDB();
            }
        }
    }

    /**
     * Update data to database
     */
    public void updateToDB(){
        /*
        Set update data to a hashmap
         */
        Map<String, Object> data = new HashMap<>();
        data.put("customerName", txtName.getText().toString());
        data.put("customerAddress", txtAddress.getText().toString());
        data.put("customerEmail", txtEmail.getText().toString());
        data.put("customerPhone", txtPhone.getText().toString());
        data.put("customerAvatar", getImageUri);

        List<String> fields= new ArrayList<String>();
        fields.add("customerName");
        fields.add("customerAddress");
        fields.add("customerEmail");
        fields.add("customerPhone");

        /*
        Update to database
         */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers").document(userID)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserProfile.this,"Update successful !",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull @NotNull Exception e) {
                        Log.e("Error:",e.getMessage());
                        Toast.makeText(UserProfile.this,"Error !!!!",Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void uploadImageFirebase(){
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://buffetrestaurant-e631f.appspot.com");
        StorageReference storageRef = storage.getReference();


// Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("Avatar/"+userEmail+".jpg");

// While the file names are the same, the references point to different files
        mountainImagesRef.getName().equals(mountainImagesRef.getName());    // true
        mountainImagesRef.getPath().equals(mountainImagesRef.getPath());    // false
        // Get the data from an ImageView as bytes
        avt.setDrawingCacheEnabled(true);
        avt.buildDrawingCache();

        Bitmap bitmap = ((BitmapDrawable) avt.getDrawable()).getBitmap();//convert image in imageview into bitmap
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(data);//upload image to storage in database
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return mountainImagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    getImageUri = downloadUri.toString();//get url of image after upload
                    updateToDB();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
}