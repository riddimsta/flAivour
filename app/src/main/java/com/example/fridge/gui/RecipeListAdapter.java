package com.example.fridge.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fridge.R;
import com.example.fridge.RecipeData;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class RecipeListAdapter extends ArrayAdapter<RecipeData> implements View.OnClickListener  {

    private ArrayList<RecipeData> dataSet;
    Context mContext;

    // View lookup cache to save memory
    private static class ViewHolder {
        TextView title;
        TextView rank;
        ImageView image;
    }

    public RecipeListAdapter(ArrayList<RecipeData> data, Context context) {
        super(context, R.layout.recipe_adapter_view, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {
        // sets the Information about the Position as a tag in the view
        int position=(Integer) v.getTag();
    }

    public RecipeData getDataOnPosition(int position){
        return dataSet.get(position);
    }

    private int lastPosition = -1;

    @Override
    // Fills the Views with data
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RecipeData dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            // Recycle The Views which already allocated memory insted of creating new ones
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.recipe_adapter_view, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.titleView);
            viewHolder.rank = (TextView) convertView.findViewById(R.id.rankView);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        NumberFormat formatter = new DecimalFormat("#0.00");

        viewHolder.title.setText(dataModel.getTitle());
        viewHolder.rank.setText("Social rank: " + formatter.format(dataModel.getSocialRank()));
        if(dataModel.getImage() == null){
            viewHolder.image.setImageResource(R.drawable.no_image);
        }else{
            viewHolder.image.setImageBitmap(dataModel.getImage());
        }
        viewHolder.image.setTag(position);
        return convertView;
    }
}
