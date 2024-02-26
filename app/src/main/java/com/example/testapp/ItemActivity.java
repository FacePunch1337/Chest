package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        String itemName = getIntent().getStringExtra("itemName");
        String itemImageUrl = getIntent().getStringExtra("itemImageUrl");
        String itemClass = getIntent().getStringExtra("itemClass");
        String itemRarity = getIntent().getStringExtra("itemRarity");
        String itemProperties = getIntent().getStringExtra("itemProperties");
        String itemDescription = getIntent().getStringExtra("itemDescription");

        TextView itemNameTextView = findViewById(R.id.itemNameTextView);
        ImageView itemImageView = findViewById(R.id.itemImageView);
        TextView itemClassTextView = findViewById(R.id.itemClassTextView);
        TextView itemRarityTextView = findViewById(R.id.itemRarityTextView);
        TextView itemPropertiesTextView = findViewById(R.id.itemPropertiesTextView);
        TextView itemDescriptionTextView = findViewById(R.id.itemDescriptionTextView);

        itemNameTextView.setText(itemName);
        Picasso.get().load(itemImageUrl).into(itemImageView);
        itemClassTextView.setText(itemClass);

        int colorResId;
        switch (itemRarity) {
            case "Обычный":
                colorResId = R.color.common;
                break;
            case "Необычный":
                colorResId = R.color.uncommon;
                break;
            case "Редкий":
                colorResId = R.color.rare;
                break;
            case "Эпический":
                colorResId = R.color.epic;
                break;
            case "Легендарный":
                colorResId = R.color.legendary;
                break;
            case "Мифический":
                colorResId = R.color.mythical;
                break;
            case "Реликвия":
                colorResId = R.color.relic;
                break;
            case "Божественный":
                colorResId = R.color.divine;
                break;
            case "За гранью понимания":
                colorResId = R.color.beyond_comprehension;
                break;
            default:
                colorResId = android.R.color.black;
                break;
        }

        itemRarityTextView.setTextColor(getResources().getColor(colorResId));
        itemRarityTextView.setText(itemRarity);

        itemPropertiesTextView.setText(itemProperties);
        itemDescriptionTextView.setText(itemDescription);

        Button closeButton = findViewById(R.id.closeButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemActivity.this, BagActivity.class);
                startActivity(intent);
            }
        });

        ImageView deleteButton = findViewById(R.id.deleteImageButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animScaleDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down);
                animScaleDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Начало анимации
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        deleteItemFromDatabase();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // Повторение анимации
                    }
                });

                deleteButton.startAnimation(animScaleDown);
            }
        });
    }

    private void deleteItemFromDatabase() {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items");
        String itemKeyToDelete = getIntent().getStringExtra("itemKey");
        itemsRef.child(itemKeyToDelete).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Показываем Toast после успешного удаления
                    Toast.makeText(ItemActivity.this, "Предмет удалён", Toast.LENGTH_SHORT).show();
                    // Обновляем список после успешного удаления
                    Intent intent = new Intent(ItemActivity.this, BagActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // Показываем Toast в случае ошибки при удалении
                    Toast.makeText(ItemActivity.this, "Ошибка при удалении предмета", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
