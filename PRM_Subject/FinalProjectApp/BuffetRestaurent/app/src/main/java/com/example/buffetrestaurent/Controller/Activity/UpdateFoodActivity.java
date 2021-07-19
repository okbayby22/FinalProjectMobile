package com.example.buffetrestaurent.Controller.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UpdateFoodActivity extends AppCompatActivity {

    RadioButton fType1,fType2,fType3;
    Button btnAddImage,btnUpdate;
    String userEmail,foodId;
    ImageView foodImage;
    TextView txtFoodName,txtFoodDes,txtFoodNameErr;
    int foodType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strUpdateFood);

        userEmail= getIntent().getStringExtra("USER_EMAIL");
        foodId= getIntent().getStringExtra("ID");

        txtFoodName=findViewById(R.id.StaffManageFood_UpdateFood_foodName);
        txtFoodDes = findViewById(R.id.StaffManageFood_UpdateFood_txtFoodDes);
        txtFoodNameErr = findViewById(R.id.StaffManageFood_UpdateFood_foodNameError);
        foodImage = findViewById(R.id.StaffManageFood_UpdateFood_foodImage);
        btnUpdate =findViewById(R.id.StaffManageFood_UpdateFood_button);
        btnAddImage=findViewById(R.id.StaffManageFood_UpdateFood_btnImg);
        fType1=findViewById(R.id.StaffManageFood_UpdateFood_foodType1);
        fType2=findViewById(R.id.StaffManageFood_UpdateFood_foodType2);
        fType3=findViewById(R.id.StaffManageFood_UpdateFood_foodType3);

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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
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
                    updateFood();
                }
            }
        });

        loadData();
    }

    public void updateFood(){
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://buffetrestaurant-e631f.appspot.com");
        StorageReference storageRef = storage.getReference();


        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("Food/"+foodId+".jpg");

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
                    Uri downloadUri = task.getResult();
                    String getUrlImage = downloadUri.toString();
                    Map<String ,Object> data =  new HashMap<>();
                    data.put("foodDescription",txtFoodDes.getText().toString());
                    data.put("foodImage",getUrlImage);
                    data.put("foodName",txtFoodName.getText().toString());
                    data.put("foodType",foodType);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("food").document(foodId)
                            .update(data)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@androidx.annotation.NonNull @NotNull Task<Void> task) {
                                    Toast.makeText(UpdateFoodActivity.this,"Update successful !",Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Handle failures
                    // ...
                }
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

    public void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("food")
                .whereEqualTo("foodId",foodId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Food food = document.toObject(Food.class);
                                txtFoodName.setText(food.getFoodName());
                                txtFoodDes.setText(food.getFoodDescription());
                                Picasso.get().load(food.getFoodImage()).into(foodImage);
                                if(food.getFoodType()==1){
                                    fType1.setChecked(true);
                                    foodType=1;
                                }else if(food.getFoodType()==2){
                                    fType2.setChecked(true);
                                    foodType=2;
                                }
                                else if(food.getFoodType()==3){
                                    fType3.setChecked(true);
                                    foodType=3;
                                }

                            }
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this , StaffManageFoodActivity.class );
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
                this.finish();
                return true;
        }
        return true;
    }
}