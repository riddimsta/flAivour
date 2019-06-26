package com.example.fridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

//Custom list adapter to show the ingredient list with toggle switch button

public class CustomListAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> ingredient_list_selected;
    private ArrayList<String> ingredient_list_unselected;

    private Context context;


    public CustomListAdapter(ArrayList<String> list, Context context) {
        this.ingredient_list_selected = list;
        this.context = context;
        ingredient_list_unselected = (ArrayList<String>) ingredient_list_selected.clone();
    }

    @Override
    public int getCount() {
        return ingredient_list_selected.size();
    }

    @Override
    public Object getItem(int pos) {
        return ingredient_list_selected.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_ingredient_list, null);
        }

        //Handle TextView and display string

        TextView listItemText = view.findViewById(R.id.list_item_string);
        listItemText.setText(ingredient_list_selected.get(position));


        //Toggle Switch for activating/deactivating ingredients

        Switch toggle_switch = view.findViewById(R.id.toggle_switch);

        toggle_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    listItemText.setAlpha((float) 1.0);

                    ingredient_list_unselected.add(listItemText.getText().toString());

                } else {

                    // Grey out ingredient if not selected

                    listItemText.setAlpha((float) 0.2);

                    ingredient_list_unselected.remove(listItemText.getText().toString());

                }
            }
        });

        return view;
    }

    // Set ingredient list to pass to the recipe list activity

    public void setList() {
        ingredient_list_selected = ingredient_list_unselected;
    }

    public ArrayList<String> getList() {

        return ingredient_list_selected;
    }
}
