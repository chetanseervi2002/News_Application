package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.ViewHolder> {
    private ArrayList<CategoryRVModel> categoryRVModels;
    private Context context;
    private CategorClickInterface categorClickInterface;

    public CategoryRVAdapter(ArrayList<CategoryRVModel> categoryRVModels, Context context, CategorClickInterface categorClickInterface) {
        this.categoryRVModels = categoryRVModels;
        this.context = context;
        this.categorClickInterface = categorClickInterface;
    }

    @NonNull
    @Override
    public CategoryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_rv_item,parent,false);
        return new CategoryRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRVAdapter.ViewHolder holder,int position) {
        CategoryRVModel categoryRVModel = categoryRVModels.get(position);
        holder.categoryTV.setText(categoryRVModel.getCatagory());
        Picasso.get().load(categoryRVModel.getCatagoryImageUrl()).into(holder.categoryIV);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use holder.getAdapterPosition() to get the accurate position:
                int clickedposition = holder.getAdapterPosition();
                categorClickInterface.onCategoryClick(clickedposition);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryRVModels.size();
    }

    public interface CategorClickInterface{
        void onCategoryClick(int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView categoryTV;
        private ImageView categoryIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTV = itemView.findViewById(R.id.idTVCategory);
            categoryIV = itemView.findViewById(R.id.idIVCategory);

        }
    }
}
