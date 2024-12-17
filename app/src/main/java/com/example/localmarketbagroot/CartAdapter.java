package com.example.localmarketbagroot;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final List<CartDataModel> dataList;
    private final Context context;

    public CartAdapter(Context context, List<CartDataModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartDataModel data = dataList.get(position);

        // Set text
        holder.textView.setText(data.getText());

        // Load image from URL using Glide
        Glide.with(context)
                .load(data.getImageUrl()) // Load image from URL
                .override(1000,150)
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemText);
            imageView = itemView.findViewById(R.id.itemImage);
        }
    }
}



