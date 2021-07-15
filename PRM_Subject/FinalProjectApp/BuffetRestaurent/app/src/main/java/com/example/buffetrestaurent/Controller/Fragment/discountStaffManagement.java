package com.example.buffetrestaurent.Controller.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buffetrestaurent.Adapter.DiscountAdapter;
import com.example.buffetrestaurent.Model.Discount;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link discountStaffManagement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class discountStaffManagement extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<Discount> discountList;
    DiscountAdapter discountAdapter;
    RecyclerView recyclerView;

    public discountStaffManagement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment discountStaffManagement.
     */
    // TODO: Rename and change types and number of parameters
    public static discountStaffManagement newInstance(String param1, String param2) {
        discountStaffManagement fragment = new discountStaffManagement();
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
        View view =inflater.inflate(R.layout.fragment_discount_staff_management, container, false);
        recyclerView = view.findViewById(R.id.discountStaffManagement_RecylerView);
        loadDiscount(view.getContext());
        return view;
    }

    public void loadDiscount(Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("discount")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        QuerySnapshot query = task.getResult();
                        discountList = new ArrayList<>();
                        if(query.isEmpty()){

                        }else{
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Discount discount = new Discount();
                                discount = document.toObject(Discount.class);
                                discountList.add(discount);
                            }
                            discountAdapter = new DiscountAdapter(context  ,discountList);
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                            recyclerView.setAdapter(discountAdapter);
                        }

                    }
                });
    }
}