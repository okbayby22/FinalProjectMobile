package com.example.buffetrestaurent.Controller.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.buffetrestaurent.Model.Customer;
import com.example.buffetrestaurent.Model.Discount;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link expandTextCustomer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class expandTextCustomer extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public expandTextCustomer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment expandTextCustomer.
     */
    // TODO: Rename and change types and number of parameters
    public static expandTextCustomer newInstance(String param1, String param2) {
        expandTextCustomer fragment = new expandTextCustomer();
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
    /*
    Compoent that to get view
     */
    TextView  txtCustomer;
    int customer0=0 , customer1=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expand_text_customer, container, false);
        txtCustomer = view.findViewById(R.id.expandTextCustomer_Textview);
        loadCustomer(getContext());
        return view;
    }
    /**
     * load all customer and get status
     * @param context
     */
    public void loadCustomer(Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        QuerySnapshot query = task.getResult();
                        if(query.isEmpty()){
                        }else{
                            //Store Customer
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Customer customer = new Customer();
                                customer = document.toObject(Customer.class);
                                if(customer.getCustomerStatus()==0){
                                    customer0+=1;
                                }else if(customer.getCustomerStatus()==1) {
                                    customer1 += 1;
                                }
                            }
                            //Create text view
                            txtCustomer.setText("Customer is Disable :         "+customer0+
                                    "\nCustomer is Enable  :         "+customer1);

                        }
                    }
                });
    }
}