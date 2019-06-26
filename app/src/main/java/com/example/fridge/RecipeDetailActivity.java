package com.example.fridge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);


        RecipeData data = getIntent().getExtras().getParcelable("data");

        ImageView mDishObjectView       = (ImageView) findViewById(R.id.dishObjectView);
        TextView  mTitleTextView        = (TextView)  findViewById(R.id.titleTextView);
        TextView  mIngredientsTextView  = (TextView)  findViewById(R.id.ingredientsTextView);

        // Fill the Views with Data
        mTitleTextView.setText(data.getTitle());
        mDishObjectView.setImageBitmap(data.getImage());
        mIngredientsTextView.setText(prepareIngredientString(data.getIngredients()));
    }

    // Just Make a line break after every Ingredient
    public String prepareIngredientString(List<String> list){
        String result = "";
        for(String ingredient : list){
            result += ingredient + "\n";
        }
        return result;
    }
}
