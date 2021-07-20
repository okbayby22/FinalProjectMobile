package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddFoodActivity extends AppCompatActivity {

    RadioButton fType1,fType2,fType3;
    Button btnAddImage,btnAdd;
    String userEmail,foodId;
    ImageView foodImage;
    TextView txtFoodName,txtFoodDes,txtFoodNameErr;
    int foodType=1;
    double role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strAddFood);

        userEmail= getIntent().getStringExtra("USER_EMAIL");
        role = getIntent().getDoubleExtra("ROLE",0);

        txtFoodName=findViewById(R.id.StaffManageFood_UpdateFood_foodName);
        txtFoodDes = findViewById(R.id.StaffManageFood_UpdateFood_txtFoodDes);
        txtFoodNameErr = findViewById(R.id.StaffManageFood_UpdateFood_foodNameError);
        foodImage = findViewById(R.id.StaffManageFood_UpdateFood_foodImage);
        btnAdd =findViewById(R.id.StaffManageFood_UpdateFood_button);
        btnAddImage=findViewById(R.id.StaffManageFood_UpdateFood_btnImg);
        fType1=findViewById(R.id.StaffManageFood_UpdateFood_foodType1);
        fType2=findViewById(R.id.StaffManageFood_UpdateFood_foodType2);
        fType3=findViewById(R.id.StaffManageFood_UpdateFood_foodType3);

        String defaultFoodImage="https://firebasestorage.googleapis.com/v0/b/buffetrestaurant-e631f.appspot.com/o/Food%2FdefaultFoodImage.jpg?alt=media&token=b8378b1e-908b-4286-a749-3f35db3a8ef0";
        Picasso.get().load(defaultFoodImage).into(foodImage);
        btnAdd.setText("Add food");

        fType1.setChecked(true);

        fType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodType=1;
            }
        });

        fType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodType=2;
            }
        });

        fType3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodType=3;
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=true;
                if(txtFoodName.getText().toString().isEmpty()){
                    txtFoodNameErr.setText("Food name can not empty");
                    check=false;
                }
                else{
                    txtFoodNameErr.setText("");
                    check=true;
                }

                if(check){
                    addFood();
                }
            }
        });
    }

    public void addFood(){
        Map<String ,Object> addData =  new HashMap<>();
        addData.put("foodDescription",txtFoodDes.getText().toString());
        addData.put("foodId","");
        addData.put("foodImage","");
        addData.put("foodName",txtFoodName.getText().toString());
        addData.put("foodType",foodType);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("food")
                .add(addData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        FirebaseStorage storage = FirebaseStorage.getInstance("gs://buffetrestaurant-e631f.appspot.com");
                        StorageReference storageRef = storage.getReference();


                        // Create a reference to 'images/mountains.jpg'
                        StorageReference mountainImagesRef = storageRef.child("Food/"+documentReference.getId()+".jpg");

                        // While the file names are the same, the references point to different files
                        mountainImagesRef.getName().equals(mountainImagesRef.getName());    // true
                        mountainImagesRef.getPath().equals(mountainImagesRef.getPath());    // false
                        // Get the data from an ImageView as bytes
                        foodImage.setDrawingCacheEnabled(true);
                        foodImage.buildDrawingCache();

                        Bitmap bitmap = ((BitmapDrawable) foodImage.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = mountainImagesRef.putBytes(data);
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
                                    Toast.makeText(AddFoodActivity.this,"Add successful !",Toast.LENGTH_SHORT).show();
                                    Uri downloadUri = task.getResult();
                                    String getUrlImage = downloadUri.toString();

                                    Map<String ,Object> updateData =  new HashMap<>();
                                    updateData.put("foodId",documentReference.getId());
                                    updateData.put("foodImage",getUrlImage);

                                    db.collection("food").document(documentReference.getId())
                                            .update(updateData);
                                    Intent intent = new Intent(AddFoodActivity.this, StaffManageFoodActivity.class);
                                    intent.putExtra("USER_EMAIL", userEmail);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Handle failures
                                    // ...
                                }
                            }
                        });
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
            Uri imageUri=data.getData();
            foodImage.setImageURI(imageUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , StaffManageFoodActivity.class );
                intent.putExtra("USER_EMAIL", userEmail);
                intent.putExtra("ROLE", role);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this , StaffManageFoodActivity.class );
        intent.putExtra("USER_EMAIL", userEmail);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        this.finish();
    }
}