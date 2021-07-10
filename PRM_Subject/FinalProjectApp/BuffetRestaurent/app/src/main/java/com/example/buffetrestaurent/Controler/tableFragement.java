package com.example.buffetrestaurent.Controler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buffetrestaurent.Model.Desk;
import com.example.buffetrestaurent.R;
import com.example.buffetrestaurent.Utils.Apis;
import com.example.buffetrestaurent.Utils.DeskService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.perfmark.Tag;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tableFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tableFragement extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    List <Desk> desksList,deskListOnStatus;
    DeskAdapter deskAdapter;
    DeskService deskService;
    int deskStatus;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public tableFragement(int deskStatus) {
        // Required empty public constructor
        this.deskStatus=deskStatus;
    }

    public tableFragement() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tableFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static tableFragement newInstance(String param1, String param2) {
        tableFragement fragment = new tableFragement();
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
        View rootView= inflater.inflate(R.layout.fragment_table_fragement, container, false);
             recyclerView = rootView.findViewById(R.id.homepage_listTable);
             desksList = new ArrayList<Desk>();
        loadData();

        return rootView;
    }
    public void loadData(){
         FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("desk")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            deskListOnStatus=new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                              Desk desk = new Desk();
                              desk = document.toObject(Desk.class);
                              if(desk.getDesk_status()==deskStatus){
                                  deskListOnStatus.add(desk);
                              }
                            }

                            deskAdapter = new DeskAdapter(getContext(),deskListOnStatus);
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
                            
                            //recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(deskAdapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}