package com.example.buffetrestaurent.Controller.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signUpFragment extends Fragment {

    Button btnSignUp; //Object of button sign up
    TextView txtEmail,txtPass,txtRePass,txtEmailError,txtRePassError,txtPassError,txtName,txtPhone,txtNameError,txtPhoneError; //Object of all textbox on view
    RadioButton txtMale,txtFemale; //Object for 2 radio button
    int gender; //Save user gender in type int

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()???[{}]:;',?/*~$^+=<>]).{8,20}$";
    private static final String VALID_EMAIL_ADDRESS_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,6}$";
    private static final String PHONE_PATTERN = "(0[1-9])+([0-9]{8})";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static signUpFragment newInstance(String param1, String param2) {
        signUpFragment fragment = new signUpFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        /*
        Get view by id
         */
        btnSignUp= rootView.findViewById(R.id.btnsignUp);
        txtEmail= rootView.findViewById(R.id.signUp_txtEmail);
        txtPass= rootView.findViewById(R.id.signUp_txtPass);
        txtRePass= rootView.findViewById(R.id.signUp_txtRePass);
        txtName = rootView.findViewById(R.id.signUp_txtName);
        txtPhone = rootView.findViewById(R.id.signUp_txtPhone);
        txtEmailError= rootView.findViewById(R.id.signUp_EmailError);
        txtRePassError= rootView.findViewById(R.id.signUp_txtErrorRePass);
        txtPassError= rootView.findViewById(R.id.signUp_PassError);
        txtNameError = rootView.findViewById(R.id.signUp_NameError);
        txtPhoneError = rootView.findViewById(R.id.signUp_PhoneError);
        txtMale = rootView.findViewById(R.id.signUp_txtMale);
        txtFemale = rootView.findViewById(R.id.signUp_txtFemale);

        /*
        Set default check and check event for radio button
         */
        txtMale.setChecked(true);
        txtMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gender=0;
            }
        });
        txtFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gender=1;
            }
        });

        /*
        Set click event for button sign up
         */
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean emailStatus=true;
                boolean passStatus=true;
                boolean rePassStatus=true;
                boolean nameStatus=true;
                boolean phoneStatus=true;

                Pattern pattern = Pattern.compile(VALID_EMAIL_ADDRESS_REGEX);
                Matcher matcher = pattern.matcher(txtEmail.getText().toString());

                /*
                Check all text box base on set regex
                 */
                if(txtEmail.getText().toString().isEmpty()){
                    txtEmailError.setText("Email can not empty !!!");
                    emailStatus=false;
                }
                else if(!matcher.matches()){
                    txtEmailError.setText("Wrong email format !!!");
                    emailStatus=false;
                }
                else{
                    txtEmailError.setText("");
                    emailStatus=true;
                }

                pattern = Pattern.compile(PHONE_PATTERN);
                matcher = pattern.matcher(txtPhone.getText().toString());
                if(txtPhone.getText().toString().isEmpty()){
                    txtPhoneError.setText("Phone can not empty !!!");
                    phoneStatus=false;
                }
                else if(!matcher.matches()){
                    txtPhoneError.setText("Wrong phone format !!! Exp: 0123456789");
                    phoneStatus=false;
                }
                else{
                    txtPhoneError.setText("");
                    phoneStatus=true;
                }

                pattern = Pattern.compile(PASSWORD_PATTERN);
                matcher = pattern.matcher(txtPass.getText().toString());
                if(txtPass.getText().toString().isEmpty()){
                    txtPassError.setText("Password can not empty !!!");
                    passStatus=false;
                }
                else if(!matcher.matches()){
                    txtPassError.setText("Password must have 8 to 20 character and contain at least 1 digit, lowercase character, uppercase character, special character");
                    passStatus=false;
                }
                else{
                    txtPassError.setText("");
                    passStatus=true;
                }
                if(txtRePass.getText().toString().compareTo(txtPass.getText().toString())!=0){
                    txtRePassError.setText("Confirm password not correct with password !!!");
                    rePassStatus=false;
                }
                else {
                    txtRePassError.setText("");
                    rePassStatus=true;
                }

                if(txtName.getText().toString().isEmpty()){
                    txtNameError.setText("Name must more than 1 character !!!");
                    nameStatus=false;
                }
                else{
                    txtNameError.setText("");
                    nameStatus=true;
                }

                if(emailStatus && passStatus && rePassStatus && phoneStatus && nameStatus) { // if all condition is correct go to check duplicate email
                    checkDuplicaEmail(v);
                }
            }
        });
        return rootView;
    }

    /**
     * Method check duplicate Email
     * @param v current view
     */
    private void checkDuplicaEmail(View v){

        /*
        Check email in customer database
         */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .whereEqualTo("customerEmail",txtEmail.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot doc= task.getResult();
                        if (!doc.isEmpty()) {
                            txtEmailError.setText("Email has already exist !!!");
                        }
                        else{
                            /*
                            Check email in staff database
                             */
                            db.collection("staffs")
                                    .whereEqualTo("staffEmail",txtEmail.getText().toString())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            QuerySnapshot doc= task.getResult();
                                            if (!doc.isEmpty()) {
                                                txtEmailError.setText("Email has already exist !!!");
                                            }
                                            else { // Add new account if email not duplicate
                                                AddNewCus(v);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    /**
     * Method add new customer account to database
     * @param v Current view
     */
    private void AddNewCus(View v){
        Customer newCustomer=new Customer(txtName.getText().toString(),txtEmail.getText().toString(),"",txtPhone.getText().toString(),md5(txtPass.getText().toString()),gender,0,0,1,"https://firebasestorage.googleapis.com/v0/b/buffetrestaurant-e631f.appspot.com/o/Avatar%2Fuser.jpg?alt=media&token=99aa5cca-86b5-45a1-b83c-b287bfbd6a02");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .add(newCustomer)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Map<String ,Object> data =  new HashMap<>();
                        data.put("customerId",documentReference.getId());
                        db.collection("customers").document(documentReference.getId())
                                .update(data);
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.createUserWithEmailAndPassword(txtEmail.getText().toString(), md5(txtPass.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                Toast.makeText(v.getContext(),"Sign up successful !",Toast.LENGTH_LONG).show();
                                txtEmail.setText("");
                                txtEmailError.setText("");
                                txtPass.setText("");
                                txtRePass.setText("");
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(v.getContext(),"Sign up fail !",Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Method convert input string to md5 string
     * @param pass Input string
     * @return md5 string
     */
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