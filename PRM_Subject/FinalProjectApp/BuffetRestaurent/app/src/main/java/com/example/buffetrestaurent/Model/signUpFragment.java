package com.example.buffetrestaurent.Model;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffetrestaurent.R;
import com.example.buffetrestaurent.Utils.Apis;
import com.example.buffetrestaurent.Utils.CustomerService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signUpFragment extends Fragment {

    Button btnSignUp;
    CustomerService service;
    TextView txtEmail,txtPass,txtRePass,txtEmailError,txtRePassError,txtPassError;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    private static final String VALID_EMAIL_ADDRESS_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$";

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
        btnSignUp= rootView.findViewById(R.id.btnsignUp);
        txtEmail= rootView.findViewById(R.id.signUp_txtEmail);
        txtPass= rootView.findViewById(R.id.signUp_txtPass);
        txtRePass= rootView.findViewById(R.id.signUp_txtRePass);
        txtEmailError= rootView.findViewById(R.id.signUp_EmailError);
        txtRePassError= rootView.findViewById(R.id.signUp_txtErrorRePass);
        txtPassError= rootView.findViewById(R.id.signUp_PassError);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean emailStatus=true;
                boolean passStatus=true;
                boolean rePassStatus=true;
                Pattern pattern = Pattern.compile(VALID_EMAIL_ADDRESS_REGEX);
                Matcher matcher = pattern.matcher(txtEmail.getText().toString());
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

                if(emailStatus && passStatus && rePassStatus) {
                    checkDuplicaEmail(v);
                }
            }
        });
        return rootView;
    }

    private void checkDuplicaEmail(View v){
        service = Apis.getCustomerService();
        Call<Boolean> call=service.checkDuplicateEmail(txtEmail.getText().toString());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Boolean check =response.body();
                    if(check == true){
                        txtEmailError.setText("Email has already exist !!!");
                    }
                    else{
                        AddNewCus(v);
                        txtEmail.setText("");
                        txtEmailError.setText("");
                        txtPass.setText("");
                        txtRePass.setText("");
                    }
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("Error:",t.getMessage());
            }
        });
    }

    private void AddNewCus(View v){
        Customer newCustomer=new Customer(null,txtEmail.getText().toString(),null,null,txtPass.getText().toString(),1,0,0,1,null);
        service= Apis.getCustomerService();
        Call<Customer> call=service.addCustomer(newCustomer);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()){
                    Toast.makeText(v.getContext(),"Sign up successful !",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("Error:",t.getMessage());
            }
        });
    }
}