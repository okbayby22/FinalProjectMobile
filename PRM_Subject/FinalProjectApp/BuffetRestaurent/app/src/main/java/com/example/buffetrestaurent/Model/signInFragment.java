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

import com.example.buffetrestaurent.MainActivity;
import com.example.buffetrestaurent.R;
import com.example.buffetrestaurent.Utils.Apis;
import com.example.buffetrestaurent.Utils.CustomerService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signInFragment extends Fragment {

    Button btnSignIn;
    CustomerService service;
    TextView txtEmail,txtPass,txtError;

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
                service= Apis.getCustomerService();
                Call<Integer> call=service.checkLogin(txtEmail.getText().toString(),txtPass.getText().toString());
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful()){
                            Integer check=response.body();
                            if(check==9){
                                Toast.makeText(v.getContext(),"Sign In successful !",Toast.LENGTH_LONG).show();
                                txtError.setText("");
                            }
                            else{
                                txtError.setText("Email or password is incorrect !!");
                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.e("Error:",t.getMessage());
                    }
                });
            }
        });
        return rootView;
    }
}