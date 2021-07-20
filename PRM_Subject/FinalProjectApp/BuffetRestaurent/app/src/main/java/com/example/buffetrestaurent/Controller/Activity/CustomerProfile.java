package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Staff;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

public class CustomerProfile extends AppCompatActivity {


    String email,ID; //Current user email and ID
    Customer cus;

    EditText txtName,txtEmail,txtPhone,txtAddress; //Input text of customer Name, email, phone and address
    TextView txtNameError,txtPhoneError; //Show error of name and phone input
    ImageView avt; //avt of customer
    Button uploadImage; //Button to choose image
    Uri imageUri;
    Drawable oldimage;
    String getImageUri;
    double role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strCustomerProfile);
        email = getIntent().getStringExtra("EMAIL"); //Get email of current user
        ID = getIntent().getStringExtra("ID"); //Get id of customer
        /*
        Mapping view to layout
         */
        role = getIntent().getDoubleExtra("ROLE",0);
        txtName = findViewById(R.id.CustomerProfile_txtName);
        txtEmail = findViewById(R.id.CustomerProfile_txtEmail);
        txtPhone = findViewById(R.id.CustomerProfile_txtPhone);
        txtAddress = findViewById(R.id.CustomerProfile_txtAddress);
        txtNameError = findViewById(R.id.CustomerProfile_txtName_error);
        txtPhoneError = findViewById(R.id.CustomerProfile_txtPhone_error);
        avt = findViewById(R.id.CustomerProfile_imgAVT);
        uploadImage = findViewById(R.id.CustomerProfile_imgAVTbtn);
        /*
        Customer can not change their email
         */
        txtEmail.setFocusable(false);
        txtEmail.setFocusableInTouchMode(false);
        txtEmail.setClickable(false);
        txtEmail.setAlpha((float) 0.3);
        loadData(); //Load customer profile
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });
    }


    /**
     * Load customer profile
     */
    public void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
        Get user data with email from datababase
         */
        db.collection("customers")
                .whereEqualTo("customerEmail",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                            cus = doc.toObject(Customer.class);
                            /*
                            Binding data to view
                             */
                            txtName.setText(cus.getCustomerName());
                            txtEmail.setText(email);
                            txtPhone.setText(cus.getCustomerPhone());
                            txtAddress.setText(cus.getCustomerAddress());
                            GradientDrawable imgshape = new GradientDrawable();
                            imgshape.setShape(GradientDrawable.OVAL);
                            imgshape.setStroke(3, Color.BLACK);
                            imgshape.setCornerRadius(100);
                            Picasso.get().load(cus.getCustomerAvatar()).into(avt);
                            oldimage=avt.getDrawable();//get data of old image
                            avt.setBackgroundDrawable(imgshape);
                        }
                    }
                });
    }

    /**
     * Event onclick of button on supported bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , UserManageActivity.class );
                intent.putExtra("USER_EMAIL", email);
                intent.putExtra("ROLE", role);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , UserManageActivity.class );
        intent.putExtra("USER_EMAIL", email);
        startActivity(intent);
        this.finish();
    }


    public void onClickAddStaff(View view){
        Intent intent = new Intent(this , UserManageActivity.class );
        intent.putExtra("USER_EMAIL", email);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        this.finish();
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
            cus.setCustomerName(txtName.getText().toString());
            cus.setCustomerPhone(txtPhone.getText().toString());
            cus.setCustomerAddress(txtAddress.getText().toString());
            if(avt.getDrawable()!=oldimage){
                uploadImageFirebase();//if user upload new image, load that image to database and update profile
            }else{
                updateToDB();
            }
        }
    }

    /**
     * Update data to database
     */
    private void updateToDB() {
        Map<String, Object> data = new HashMap<>();
        data.put("customerName", txtName.getText().toString());
        data.put("customerAddress", txtAddress.getText().toString());
        data.put("customerPhone", txtPhone.getText().toString());
        data.put("customerAvatar", getImageUri);

        List<String> fields= new ArrayList<String>();
        fields.add("customerName");
        fields.add("customerAddress");
        fields.add("customerPhone");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers").document(ID)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CustomerProfile.this,"Update Customer Successful !",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull @NotNull Exception e) {
                        Log.e("Error:",e.getMessage());
                        Toast.makeText(CustomerProfile.this,"Error !!!!",Toast.LENGTH_LONG).show();
                    }
                });
    }
    public void uploadImageFirebase(){
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://buffetrestaurant-e631f.appspot.com");
        StorageReference storageRef = storage.getReference();


// Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("Avatar/"+email+".jpg");

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