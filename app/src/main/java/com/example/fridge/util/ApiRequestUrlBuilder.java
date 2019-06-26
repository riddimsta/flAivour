package com.example.fridge.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ApiRequestUrlBuilder {

    private String url;
    private String key;
    private int page;
    private List<String> ingredients;
    private String recipeID;

    public ApiRequestUrlBuilder addUrl(String url){
        this.url = url;
        return this;
    }

    public ApiRequestUrlBuilder addKey(String key){
        this.key = key;
        return this;
    }

    public ApiRequestUrlBuilder addIngredient(String ingredient){
        if(this.ingredients == null){
            this.ingredients = new ArrayList<>();
        }
        this.ingredients.add(ingredient);
        return this;
    }

    public ApiRequestUrlBuilder addPage(int page){
        this.page = page;
        return this;
    }

    public ApiRequestUrlBuilder addRecipeID(String recipeID){
        this.recipeID = recipeID;
        return this;
    }

    public String getSearchUrl(){
        String search = "";
        for(String ingredient : ingredients){
            ingredient = ingredient.replace(" ", "%20");
            search += ingredient + "%20";
        }
        search = search.substring(0, search.length() - 3);
        String result = String.format("%s/search?key=%s&q=%s&page=%d", url, key, search, page);
        Log.d("url", url);
        Log.d("key", key);
        Log.d("search", search);
        Log.d("page", Integer.toString(page));

        Log.d("result", result);
        return result;
    }

    public String getRecipeUrl(){
        String result = String.format("%s/get?key=%s&rId=%s", url, key, recipeID);
        Log.d("result", result);
        return result;
    }
}