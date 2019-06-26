package com.example.fridge;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


public class IngredientList extends AppCompatActivity {


    public static ArrayList<String> ingredientList = new ArrayList<>();
    CustomListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_view);

        Bundle extras = getIntent().getExtras();

        // Get ingredients from previous activity.

        ingredientList = extras.getStringArrayList("ingredientList");

        // Hard coded ingredients:

        ingredientList.add("apple");
        ingredientList.add("strawberry");
        ingredientList.add("orange");

        // Instantiate custom list adapter for displaying ingredients with toggle switch

        adapter = new CustomListAdapter(ingredientList, this);

        // Handles listview and assigns adapter

        ListView lView = findViewById(R.id.list);
        lView.setAdapter(adapter);
    }

    // Create intent with ingredient list as putExtra.

    public void goToRecipes(View v) {
        Intent myIntent = new Intent(this, RecipeListActivity.class);
        adapter.setList();
        myIntent.putExtra("ingredientList", adapter.getList());
        startActivity(myIntent);
    }
}