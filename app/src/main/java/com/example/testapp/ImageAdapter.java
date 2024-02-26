package com.example.testapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<String> mImageUrls;
    private List<String> mItemRarities; // Список уровней редкости
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ImageAdapter(Context context, List<String> imageUrls, List<String> itemRarities) {
        mContext = context;
        mImageUrls = imageUrls;
        mItemRarities = itemRarities;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = mImageUrls.get(position);
        String itemRarity = mItemRarities.get(position); // Получаем уровень редкости текущей картинки

        Picasso.get().load(imageUrl).into(holder.imageButton);
        Picasso.get().load(imageUrl).resize(100, 100).centerCrop().into(holder.imageButton);
        // Устанавливаем цвет фона ImageButton в зависимости от уровня редкости
        int backgroundColor;
        switch (itemRarity) {
            case "Обычный":
                backgroundColor = ContextCompat.getColor(mContext, R.color.common);
                break;
            case "Необычный":
                backgroundColor = ContextCompat.getColor(mContext, R.color.uncommon);
                break;
            case "Редкий":
                backgroundColor = ContextCompat.getColor(mContext, R.color.rare);
                break;
            case "Эпический":
                backgroundColor = ContextCompat.getColor(mContext, R.color.epic);
                break;
            case "Легендарный":
                backgroundColor = ContextCompat.getColor(mContext, R.color.legendary);
                break;
            case "Мифический":
                backgroundColor = ContextCompat.getColor(mContext, R.color.mythical);
                break;
            case "Реликвия":
                backgroundColor = ContextCompat.getColor(mContext, R.color.relic);
                break;
            case "Божественный":
                backgroundColor = ContextCompat.getColor(mContext, R.color.divine);
                break;
            case "За гранью понимания":
                backgroundColor = ContextCompat.getColor(mContext, R.color.beyond_comprehension);
                break;
            default:
                backgroundColor = Color.BLACK;
                break;
        }
        holder.imageButton.setBackgroundColor(backgroundColor);

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && mListener != null) {
                    mListener.onItemClick(adapterPosition);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButton;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.imageButton);
        }
    }
}
