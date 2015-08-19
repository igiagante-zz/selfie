package com.peryisa.dailyselfie;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import model.Item;
/**
 * Created by igiagante on 18/8/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {

    private ArrayList<Item> items;
    private Context context;
    private OnItemSelectedListener onItemSelectedListener;

    // inner class to hold a reference to each card_view of RecyclerView
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data card_view is just a string in this case
        public Long id;
        public CardView cardView;
        public ImageView imageView;
        public TextView txtTittle;
        public TextView txtDate;
        public CheckBox checkBox;
        public ImageView deleteButton;

        public ItemViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            imageView = (ImageView) v.findViewById(R.id.thumbnail);
            txtTittle = (TextView) v.findViewById(R.id.title);
            txtDate = (TextView) v.findViewById(R.id.date);
            checkBox = (CheckBox) v.findViewById(R.id.select_picture);
            deleteButton = (ImageView) v.findViewById(R.id.delete_image);
            deleteButton.setClickable(true);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Item item = items.get(position);
                    File file = new File(item.getPath());
                    if(file.delete()){
                        remove(position);
                    }
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item item = items.get(getAdapterPosition());
                    onItemSelectedListener.itemSelected(item);
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {

                    }
                }
            });
        }
    }

    public interface OnItemSelectedListener {
        void itemSelected(Item item);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, OnItemSelectedListener onItemSelectedListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.onItemSelectedListener = onItemSelectedListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, final int position) {

        itemViewHolder.id = items.get(position).getId();
        itemViewHolder.imageView.setImageBitmap(items.get(position).getImage());
        itemViewHolder.txtTittle.setText(items.get(position).getName());
        itemViewHolder.txtDate.setText(items.get(position).getPath());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void add(Item item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    public void removeAllViews() {

        boolean alDelete = false;

        for(int i = 0; i < items.size(); i++){
            File file = new File(items.get(i).getPath());
            if(file.delete() && i == items.size() - 1){
                alDelete = true;
            }
        }
        if(alDelete){
            items.clear();
            this.notifyDataSetChanged();
        }
    }
}
