package com.example.fridge;

import java.util.ArrayList;

public class RecipeDataHolder {

    static RecipeDataHolder dataHolder;
    ArrayList<RecipeData> recipeDataList = new ArrayList<>();

    public static RecipeDataHolder getInstance(){

        if(dataHolder == null) {
            dataHolder = new RecipeDataHolder();
        }
        return dataHolder;
    }

    public RecipeData getRecipeById(String id){
        for(RecipeData data : recipeDataList){
            if(data.getRecipeId().equals(id)){
                return data;
            }
        }
        return null;
    }

    public void addRecipeData(RecipeData data){
        recipeDataList.add(data);
    }

    public ArrayList<RecipeData> getRecipeData(){
        return recipeDataList;
    }

}
