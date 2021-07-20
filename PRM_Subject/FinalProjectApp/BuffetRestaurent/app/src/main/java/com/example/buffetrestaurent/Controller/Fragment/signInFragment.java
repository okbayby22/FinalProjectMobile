package com.example.buffetrestaurent.Controller.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Controller.Activity.HomePage;
import com.example.buffetrestaurent.Controller.Activity.HomePageStaff;
import com.example.buffetrestaurent.Controller.Activity.MainActivity;
import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.R;
import com.example.buffetrestaurent.Utils.CustomerService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signInFragment extends Fragment {

    Button btnSignIn;
    CustomerService service;
    TextView txtEmail,txtPass,txtError;
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static signInFragment newInstance(String param1, String param2) {
        signInFragment fragment = new signInFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        btnSignIn= rootView.findViewById(R.id.btnSignIn);
        txtEmail= rootView.findViewById(R.id.signIn_txtEmail);
        txtPass= rootView.findViewById(R.id.signIn_txtPass);
        txtError= rootView.findViewById(R.id.signIn_txtError);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                service= Apis.getCustomerService();
//                Call<Integer> call=service.checkLogin(txtEmail.getText().toString(),txtPass.getText().toString());
//                call.enqueue(new Callback<Integer>() {
//                service= Apis.getCustomerService();
//                Call<Integer> call=service.checkLogin(txtEmail.getText().toString(),txtPass.getText().toString());
//                call.enqueue(new Callback<Integer>() {
//                    @Override
//                    public void onResponse(Call<Integer> call, Response<Integer> response) {
//                        if(response.isSuccessful()){
//                            Integer check=response.body();
//                            if(check==9){
//                                Toast.makeText(v.getContext(),"Sign In successful !",Toast.LENGTH_LONG).show();
//                                txtError.setText("");
//                                //intent : transfer to insert_teacher_actitvity
//                                String userEmail=txtEmail.getText().toString();
//                                txtEmail.setText("");
//                                txtPass.setText("");
//                                Intent intent = new Intent(v.getContext() , HomePage.class );
//                                intent.putExtra("USER_EMAIL", userEmail);
//                                startActivity(intent);
//                            }
//                            else{
//                                txtError.setText("Email or password is incorrect !!");
//                            }
//
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<Integer> call, Throwable t) {
//                        Log.e("Error:",t.getMessage());
//                    }
//                });
                btnSignIn.setClickable(false);
                btnSignIn.setAlpha((float) 0.3);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(txtEmail.getText().toString(), md5(txtPass.getText().toString())).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(v.getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("customers")
                                .whereEqualTo("customerEmail",txtEmail.getText().toString())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        QuerySnapshot query = task.getResult();
                                        if (!query.isEmpty()) {
                                            String userEmail = txtEmail.getText().toString();
                                            Intent intent = new Intent(v.getContext(), HomePage.class);
                                            intent.putExtra("USER_EMAIL", userEmail);
                                            startActivity(intent);
                                            getActivity().finish();
                                            btnSignIn.setClickable(true);
                                            btnSignIn.setAlpha((float) 1);
                                        } else {
                                            db.collection("staffs")
                                                    .whereEqualTo("staffEmail",txtEmail.getText().toString())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                double staffRole=0;
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    staffRole=document.getDouble("staffRole");
                                                                }
                                                                String userEmail = txtEmail.getText().toString();
                                                                txtEmail.setText("");
                                                                txtPass.setText("");
                                                                Intent intent = new Intent(v.getContext(), HomePageStaff.class);
                                                                intent.putExtra("USER_EMAIL", userEmail);
                                                                intent.putExtra("ROLE", staffRole);
                                                                startActivity(intent);
                                                                getActivity().finish();
                                                                btnSignIn.setClickable(true);
                                                                btnSignIn.setAlpha((float) 1);
                                                            } else {
                                                                txtError.setText("Email or password not correct !!!");
                                                                btnSignIn.setClickable(true);
                                                                btnSignIn.setAlpha((float) 1);
                                                            }
                                                        }
                                                    });
                                                            }
                                                        }
                                                    });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        txtError.setText("Email or password not correct !!!");
                        btnSignIn.setClickable(true);
                        btnSignIn.setAlpha((float) 1);
                    }
                })
                ;


            }
        });
        return rootView;
    }

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