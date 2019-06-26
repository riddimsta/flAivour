package com.example.fridge;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fridge.util.ApiRequestUrlBuilder;
import com.example.fridge.util.Config;

import org.json.*;

import java.util.ArrayList;
import java.util.List;

public class RecipeRequestService extends Service {

    private final IBinder localBinder = new LocalBinder();

    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    public class LocalBinder extends Binder{
        RecipeRequestService getService(){
            return RecipeRequestService.this;
        }
    }

    // Send a request to the food API
    public void collectRecipes(final List<String> ingredientList, final JsonResponseEventHandler eventHandler){

        // Build the HTTP Request URL
        ApiRequestUrlBuilder url = new ApiRequestUrlBuilder()
            .addUrl(Config.apiUrl)
            .addKey(Config.apiKey)
            .addPage(1);
        for(String ingredient : ingredientList){
            url.addIngredient(ingredient);
        }

        //JSON Request
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url.getSearchUrl(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            // puts the JSON Data into a Recipe Data Object
                            JSONArray array = response.getJSONArray("recipes");
                            for(int i = 0; i < array.length(); i++){
                                JSONObject recipe = array.getJSONObject(i);
                                RecipeData data = new RecipeData(
                                        recipe.getString("title"),
                                        recipe.getString("image_url"),
                                        recipe.getDouble("social_rank"),
                                        recipe.getString("source_url"),
                                        recipe.getString("recipe_id")
                                );

                                // Add the new Recipe to the RecipeDataHolder.
                                // RecipeDataHolder is designed as singleton
                                RecipeDataHolder.getInstance().addRecipeData(data);
                            }

                            // Notifys the Activity that The Json data is converted completly
                            eventHandler.isFetched();
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Invokes the Json Request in the Volley Request queue
        requestQueue.add(jsonObjectRequest);
    }

    // Starts a Json Request to get Information about a specific recipe
    // Additional Information is the Ingredient List at the food2fork API
    public void getIngredientsById(final String id, final JsonResponseEventHandler eventHandler){

        // Builds the URL
        ApiRequestUrlBuilder url = new ApiRequestUrlBuilder()
                .addUrl(Config.apiUrl)
                .addKey(Config.apiKey)
                .addRecipeID(id);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url.getRecipeUrl(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject recipe = response.getJSONObject("recipe");
                            JSONArray array = recipe.getJSONArray("ingredients");
                            RecipeData data = RecipeDataHolder.getInstance().getRecipeById(id);
                            List<String> ingredients = new ArrayList<String>();
                            for(int i = 0; i < array.length(); i++){
                                ingredients.add(array.getString(i));
                            }

                            // Adds the Ingredients to the existing data
                            data.setIngredients(ingredients);

                            // Notifys the Activity, that the json data fetching is finished
                            eventHandler.isFetched();
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Invokes the Request to Volley Queue
        requestQueue.add(jsonObjectRequest);
    }
}
