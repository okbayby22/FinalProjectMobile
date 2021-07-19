package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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

public class StaffProfile extends AppCompatActivity {

    String email,ID;
    TextView txtName,txtEmail,txtPhone,txtAddress,txtGender;
    TextView txtNameError,txtPhoneError;
    Staff cus;
    ImageView avt;
    int intentID;
    Button uploadImage;
    Uri imageUri;
    Drawable oldimage;
    String getImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strProfile);
        txtName = findViewById(R.id.staffInfor_txtName);
        txtEmail = findViewById(R.id.staffInfor_txtEmail);
        txtPhone = findViewById(R.id.staffInfor_txtPhone);
        txtAddress = findViewById(R.id.staffInfor_txtAddress);
        txtGender = findViewById(R.id.staffInfor_txtGender);
        avt = findViewById(R.id.staffInfor_imgAvt);
        uploadImage = findViewById(R.id.staffProfile_imgAVTbtn);
        txtEmail.setFocusable(false);
        txtEmail.setFocusableInTouchMode(false);
        txtEmail.setClickable(false);
        txtGender.setFocusable(false);
        txtGender.setFocusableInTouchMode(false);
        txtGender.setClickable(false);
        intentID = getIntent().getIntExtra("INTENT",0);
        txtNameError = findViewById(R.id.staffInfor_txtName_error);
        txtPhoneError = findViewById(R.id.staffInfor_txtPhone_error);
        ID = getIntent().getStringExtra("ID");
        email = getIntent().getStringExtra("USER_EMAIL");
        System.out.println(">>>>>>>>>>>>>>> "+email);
        loadData();
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });

    }

    public void openFileChoose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction((Intent.ACTION_GET_CONTENT));
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==2 && resultCode == RESULT_OK && data !=null){
            imageUri=data.getData();
            avt.setImageURI(imageUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(intentID == 1){
                    Intent intent = new Intent(this , StaffManageActivity.class );
                    intent.putExtra("USER_EMAIL", email);
                    startActivity(intent);
                    this.finish();
                }else{
                    Intent intent = new Intent(this , HomePageStaff.class );
                    intent.putExtra("USER_EMAIL", email);
                    startActivity(intent);
                    this.finish();
                }

                return true;
        }
        return true;
    }

    public void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("staffs")
                .whereEqualTo("staffEmail",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                            cus = doc.toObject(Staff.class);
                            txtName.setText(cus.getStaffName());
                            txtEmail.setText(email);
                            txtPhone.setText(cus.getStaffPhone());
                            txtAddress.setText(cus.getStaffAddress());
                            getImageUri = cus.getStaffImage();
                            GradientDrawable imgshape = new GradientDrawable();
                            imgshape.setShape(GradientDrawable.OVAL);
                            imgshape.setStroke(3, Color.BLACK);
                            imgshape.setCornerRadius(100);
                            avt.setBackgroundDrawable(imgshape);
                            Picasso.get().load(cus.getStaffImage()).into(avt);
                            oldimage=avt.getDrawable();
                            if(cus.getStaffGender() == 0){
                                txtGender.setText("Male");
                            }else if(cus.getStaffGender() == 1){
                                txtGender.setText("Female");
                            }
                            cus.setStaffId(ID);
                        }
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
            cus.setStaffName(txtName.getText().toString());
            cus.setStaffPhone(txtPhone.getText().toString());
            cus.setStaffAddress(txtAddress.getText().toString());
            if(avt.getDrawable()!=oldimage){
                uploadImageFirebase();
            }else{
                updateToDB();
            }
        }

    }

    private void updateToDB() {
        Map<String, Object> data = new HashMap<>();
        data.put("staffName", txtName.getText().toString());
        data.put("staffAddress", txtAddress.getText().toString());
        data.put("staffEmail", txtEmail.getText().toString());
        data.put("staffPhone", txtPhone.getText().toString());
        data.put("staffImage", getImageUri);

        List<String> fields= new ArrayList<String>();
        fields.add("customerName");
        fields.add("customerAddress");
        fields.add("customerEmail");
        fields.add("customerPhone");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("staffs").document(ID)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(StaffProfile.this,"Update staff successful !",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull @NotNull Exception e) {
                        Log.e("Error:",e.getMessage());
                        Toast.makeText(StaffProfile.this,"Error !!!!",Toast.LENGTH_LONG).show();
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

        Bitmap bitmap = ((BitmapDrawable) avt.getDrawable()).getBitmap();
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
                    getImageUri = downloadUri.toString();
                    updateToDB();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
}