package com.example.buffetrestaurent.Controler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.buffetrestaurent.Adapter.StaffMenuFoodAdapter;
import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.Model.Menu;
import com.example.buffetrestaurent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link staffMenuContent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class staffMenuContent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SELECTED_DAY = "selectedDay";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String selectedDay;
    private String mParam2;

    Menu selectedDayMenu;
    ArrayList<Food> listFood;
    ArrayList<String> listFoodId;
    ArrayList<String> listMenuDetailId;
    private StaffMenuFoodAdapter fAdapter;

    public staffMenuContent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mselectedDay user selected day.
     * @return A new instance of fragment staffMenuContent.
     */
    // TODO: Rename and change types and number of parameters
    public static staffMenuContent newInstance(String mselectedDay) {
        staffMenuContent fragment = new staffMenuContent();
        Bundle args = new Bundle();
        args.putString(SELECTED_DAY, mselectedDay);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedDay = getArguments().getString(SELECTED_DAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_staff_menu_content, container, false);
        // Inflate the layout for this fragment
        listFood=new ArrayList<>();
        listFoodId=new ArrayList<>();
        listMenuDetailId = new ArrayList<>();

        RecyclerView recyclerView = rootView.findViewById(R.id.staff_menu_foodView);

        fAdapter = new StaffMenuFoodAdapter(listFood, listMenuDetailId,rootView.getContext());
        GridLayoutManager mLayoutManager = new GridLayoutManager(rootView.getContext(),1,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fAdapter);
        loadData();

        return rootView;
    }

    private void loadData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("menu")
                .whereEqualTo("menuDate", selectedDay)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                selectedDayMenu = document.toObject(Menu.class);
                                loadFoodId(selectedDayMenu.getMenuId());
                            }
                        }
                    }
                });
    }

    private void loadFoodId(String menuId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("menu_detail")
                .whereEqualTo("menuId", menuId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listFoodId.add(document.getString("foodId"));
                                listMenuDetailId.add(document.getId());
                            }
                            if(listFoodId.isEmpty()){

                            }
                            else{
                                for(int i=0;i<listFoodId.size();i++)
                                    loadFood(listFoodId.get(i));
                            }
                        } else {

                        }
                    }
                });
    }
    private void loadFood(String foodid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("food")
                .whereEqualTo("foodId", foodid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listFood.add(document.toObject(Food.class));
                            }
                            fAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}