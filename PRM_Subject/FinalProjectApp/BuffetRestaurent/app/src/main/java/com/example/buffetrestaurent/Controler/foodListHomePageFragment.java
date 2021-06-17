package com.example.buffetrestaurent.Controler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buffetrestaurent.Model.Food;
import com.example.buffetrestaurent.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link foodListHomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class foodListHomePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int foodType;
    private List<Food> foodList = new ArrayList<>();
    private FoodsAdapter fAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public foodListHomePageFragment() {
        // Required empty public constructor
    }

    public foodListHomePageFragment(int foodType) {
        this.foodType=foodType;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment foodListHomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static foodListHomePageFragment newInstance(String param1, String param2) {
        foodListHomePageFragment fragment = new foodListHomePageFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_food_list_home_page, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.homepage_listFoodList);
        fAdapter = new FoodsAdapter(foodList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fAdapter);
        prepareFoodData();
        return rootView;
    }

    private void prepareFoodData() {
        Food food = new Food();
        food.setFoodName("a");
        foodList.add(food);

        food = new Food();
        food.setFoodName("b");
        foodList.add(food);

        food = new Food();
        food.setFoodName("c");
        foodList.add(food);

        food = new Food();
        food.setFoodName("d");
        foodList.add(food);

        food = new Food();
        food.setFoodName("e");
        foodList.add(food);

        food = new Food();
        food.setFoodName("f");
        foodList.add(food);
        fAdapter.notifyDataSetChanged();
    }
}