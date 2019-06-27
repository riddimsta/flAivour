package com.example.fridge;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fridge.RecipeRequestService.LocalBinder;
import com.example.fridge.gui.RecipeListAdapter;
import com.example.fridge.service.ImageDownloadTask;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    RecipeRequestService requestService;
    boolean isBound = false;

    RecipeListAdapter adapter;

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        Intent intent = new Intent(this, RecipeRequestService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        // Set custom Adapter to ListView
        ListView mListView = (ListView) findViewById(R.id.recipeListView);
        adapter = new RecipeListAdapter(RecipeDataHolder.getInstance().getRecipeData(), this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent i = new Intent(getBaseContext(), RecipeDetailActivity.class);
                RecipeData data = adapter.getDataOnPosition(position);

                // Starts a Request if an Item is Clicked to get Ingredient List
                requestService.getIngredientsById(data.getRecipeId(), new JsonResponseEventHandler() {
                    @Override
                    public void isFetched() {
                        startActivity(i);
                    }
                });
                i.putExtra("data", data);
            }
        });

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            requestService = binder.getService();
            isBound = true;
            List<String> ingredientList;
            Bundle extras = getIntent().getExtras();
            try{
                if(!extras.containsKey("ingredientList")){
                    return;
                }
            }catch(NullPointerException e){
                return;
            }

            ingredientList = extras.getStringArrayList("ingredientList");

            // start Request to collect Recipes
            requestService.collectRecipes(ingredientList, new JsonResponseEventHandler() {
                @Override

                // If collecting is finished and data is in RecipeDataHolder
                public void isFetched() {
                    adapter.notifyDataSetChanged();
                    ArrayList<RecipeData> dataList = RecipeDataHolder.getInstance().getRecipeData();

                    // if no Data is available show Toast and go Back
                    if (dataList.isEmpty()) {
                        try {
                            Thread.sleep(1500);
                            RecipeListActivity.super.onBackPressed();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Toast toast=Toast.makeText(getApplicationContext(),"No recipes with theses ingredients",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
                        toast.setMargin(50,50);
                        toast.show();
                    }

                    // else start a Async Thread to collect Images
                    // Async because Image download may take too much time
                    for (int i = 0; i < dataList.size(); i++) {
                        RecipeData data = dataList.get(i);
                        if (data.getImage() == null) {
                            ImageDownloadTask task = new ImageDownloadTask(getApplicationContext(), data, new JsonResponseEventHandler() {
                                @Override
                                public void isFetched() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            task.execute(data.getImageURL());
                        }
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
}
