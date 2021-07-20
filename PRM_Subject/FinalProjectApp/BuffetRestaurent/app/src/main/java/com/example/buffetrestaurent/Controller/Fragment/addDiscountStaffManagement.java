package com.example.buffetrestaurent.Controller.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.buffetrestaurent.Controller.Activity.StaffDiscountManagement;
import com.example.buffetrestaurent.Model.Discount;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addDiscountStaffManagement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addDiscountStaffManagement extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    double role;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //Contain user email of using user
    String userEmail;


    public addDiscountStaffManagement(String userEmail,double role) {
        // Required empty public constructor
        this.userEmail =userEmail;
        this.role=role;
    }
    public addDiscountStaffManagement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addDiscountStaffManagement.
     */
    // TODO: Rename and change types and number of parameters
    public static addDiscountStaffManagement newInstance(String param1, String param2) {
        addDiscountStaffManagement fragment = new addDiscountStaffManagement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    //Edit Text component
    EditText edtAddDiscountName,edtAddDiscountPoint, edtAddDiscountPercent;
    //Text view component
    TextView txtErrorName,txtErrorPoint,txtErrorPercent;
    //Button component
    Button btnSubmit,btnCancel;
    //Contain staff ID
    String staffID;
    //Load all discount name
    ArrayList<String> discountName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_discount_staff_management, container, false);
        edtAddDiscountName = view.findViewById(R.id.adddiscount_edtName);
        edtAddDiscountPoint = view.findViewById(R.id.adddiscount_edtPoint);
        edtAddDiscountPercent = view.findViewById(R.id.adddiscount_Percent);
        txtErrorName = view.findViewById(R.id.adddiscount_txtErrorName);
        txtErrorPoint = view.findViewById(R.id.adddiscount_txtErrorPoint);
        txtErrorPercent = view.findViewById(R.id.adddiscount_txtErrorPercent);
        btnSubmit = view.findViewById(R.id.adddiscount_btnSubmit);
        btnCancel = view.findViewById(R.id.adddiscount_btnCancel);
        //Load staff ID that has same user email
        loadStaffId();
        //Create object discountName
        discountName = new ArrayList<>();
        //Load all discount name and store in discount Name arraylist
        loadDiscountName();
        //Create event for button cancle
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start activity staff discount Management
                Intent intent=new Intent(v.getContext(), StaffDiscountManagement.class);
                intent.putExtra("USER_EMAIL", userEmail);
                intent.putExtra("ROLE", role);
                startActivity(intent);
                getActivity().finish();
            }
        });
        //Create event click for button Submit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkName = false,checkPoint = false,checkPercent =false;
                //Check Name
                if(edtAddDiscountName.getText().toString().equals("")){
                    txtErrorName.setText("Name of discount cannot be empty!!");
                    txtErrorName.setTextColor(Color.RED);
                }else if(discountName.contains(edtAddDiscountName.getText().toString().toUpperCase())){
                    txtErrorName.setText("Name of discount has been existed!!");
                    txtErrorName.setTextColor(Color.RED);
                }else{
                    txtErrorName.setText("");
                    checkName = true;
                }
                //Check Points
                if(edtAddDiscountPoint.getText().toString().equals("")){
                    txtErrorPoint.setText("Points cannot be empty!!");
                    txtErrorPoint.setTextColor(Color.RED);
                }else if(Integer.parseInt(edtAddDiscountPoint.getText().toString()) <=0 ){
                    txtErrorPoint.setText("Points cannot be less than 0!!");
                    txtErrorPoint.setTextColor(Color.RED);
                }else if(Integer.parseInt(edtAddDiscountPoint.getText().toString()) >=1000 ){
                    txtErrorPoint.setText("Points cannot be more than 1000!!");
                    txtErrorPoint.setTextColor(Color.RED);
                }
                else{
                    txtErrorPoint.setText("");
                    checkPoint= true;
                }
                //Check Percents
                if(edtAddDiscountPercent.getText().toString().equals("")){
                    txtErrorPercent.setText("Percent cannot be empty!!");
                    txtErrorPercent.setTextColor(Color.RED);
                }
                else if(Integer.parseInt(edtAddDiscountPercent.getText().toString()) <=0 ){
                    txtErrorPercent.setText("Percent cannot be less than 0!!");
                    txtErrorPercent.setTextColor(Color.RED);
                }else if(Integer.parseInt(edtAddDiscountPercent.getText().toString()) >100 ){
                    txtErrorPercent.setText("Percent cannot be more than 100!!");
                    txtErrorPercent.setTextColor(Color.RED);
                }else{
                    txtErrorPercent.setText("");
                    checkPercent = true;
                }
                //Add Discount
                if(checkName && checkPoint && checkPercent){
                    AddDiscount();
                }
            }
        });

        return view;
    }

    /**
     * load discount Name that has exist in database
     */
    public void loadDiscountName(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("discount")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @Override
                     public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            QuerySnapshot query = task.getResult();
                            if(query.isEmpty()){

                            }else{
                                for(QueryDocumentSnapshot document: task.getResult()){
                                    Discount discount = new Discount();
                                    discount= document.toObject(Discount.class);
                                    String dName = discount.getDiscountName();
                                    discountName.add(dName.toUpperCase());
                                }
                            }
                      }
                });
    }

    /**
     * Add discount to database
     */
    public void AddDiscount(){
        Map<String ,Object> data =  new HashMap<>();
        data.put("discountId","");
        data.put("discountName",edtAddDiscountName.getText().toString());
        data.put("discountPercent",Integer.parseInt(edtAddDiscountPercent.getText().toString()));
        data.put("discountPoint",Integer.parseInt(edtAddDiscountPoint.getText().toString()));
        data.put("discountStatus",1);
        data.put("staffId",staffID);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("discount")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Map<String ,Object> data =  new HashMap<>();
                        data.put("discountId",documentReference.getId());
                        db.collection("discount").document(documentReference.getId())
                                .update(data);
                        Intent intent=new Intent(getActivity(), StaffDiscountManagement.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        intent.putExtra("ROLE", role);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
    }

    /**
     * Load staff ID that has same user email
     */
    public void loadStaffId(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("staffs")
                .whereEqualTo("staffEmail",userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        QuerySnapshot query = task.getResult();
                        if(query.isEmpty()){

                        }else{
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                staffID = document.getId();
                            }
                        }
                    }
                });
    }
}