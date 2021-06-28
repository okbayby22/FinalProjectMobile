package com.example.buffetrestaurent.Controler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buffetrestaurent.Model.Desk;
import com.example.buffetrestaurent.R;
import com.example.buffetrestaurent.Utils.Apis;
import com.example.buffetrestaurent.Utils.DeskService;

import java.util.ArrayList;
import java.util.List;

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
    List <Desk> desksList ;
    DeskAdapter deskAdapter;
    DeskService deskService;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public tableFragement() {
        // Required empty public constructor
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
        deskService = Apis.getDeskService();
        Call<List<Desk>> call = deskService.loaddeskList();
        call.enqueue(new Callback<List<Desk>>() {
            @Override
            public void onResponse(Call<List<Desk>> call, Response<List<Desk>> response) {
                desksList = response.body();
                deskAdapter = new DeskAdapter(getContext(),desksList);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(mLayoutManager);
                //recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(deskAdapter);
            }

            @Override
            public void onFailure(Call<List<Desk>> call, Throwable t) {
                desksList = null;
            }
        });
    }
}