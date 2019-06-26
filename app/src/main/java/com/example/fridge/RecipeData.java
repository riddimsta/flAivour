package com.example.fridge;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class RecipeData implements Parcelable {

    String title;
    String imageURL;
    String originalURL;
    double socialRank;
    Bitmap image;
    String recipeId;
    List<String> ingredients;


    public RecipeData(String title, String imageURL, double socialRank, String originalURL, String recipeId){
        this.title = title;
        this.imageURL = imageURL;
        this.socialRank = socialRank;
        this.originalURL = originalURL;
        this.recipeId = recipeId;
    }

    public String getRecipeId(){ return recipeId; }

    public String getTitle(){ return title; }

    public String getImageURL(){
        return imageURL;
    }

    public double getSocialRank() {return socialRank; }

    public Bitmap getImage() {return image; }

    public String getOriginalURL() {return originalURL; }

    public List<String> getIngredients() {return this.ingredients; }

    public void setImage(Bitmap image) { this.image = image; }

    public void setIngredients(List<String> ingredients){ this.ingredients = ingredients; }

    // Parcelling part
    public RecipeData(Parcel in){

        this.title = in.readString();
        this.imageURL = in.readString();
        this.originalURL = in.readString();
        this.socialRank = in.readDouble();
        this.image = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        this.recipeId = in.readString();
        this.ingredients = new ArrayList<String>();
        in.readStringList(this.ingredients);
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.imageURL);
        dest.writeString(this.originalURL);
        dest.writeDouble(this.socialRank);
        dest.writeValue (this.image);
        dest.writeString(this.recipeId);
        dest.writeStringList(this.ingredients);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RecipeData createFromParcel(Parcel in) {
            return new RecipeData(in);
        }

        public RecipeData[] newArray(int size) {
            return new RecipeData[size];
        }
    };
}
