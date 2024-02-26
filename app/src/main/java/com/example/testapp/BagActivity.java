package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BagActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private Button closeButton;

    private static final int REQUEST_CODE_DELETE_ITEM = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);

        closeButton = findViewById(R.id.closeButton);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        GetBag();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BagActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GetBag() {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items");
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> imageUrls = new ArrayList<>();
                ArrayList<String> itemRarities = new ArrayList<>();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String imageUrl = itemSnapshot.child("imageUrl").getValue(String.class);
                    String itemRarity = itemSnapshot.child("rarity").getValue(String.class);
                    Log.d("ImageUrl", imageUrl);
                    imageUrls.add(imageUrl);
                    itemRarities.add(itemRarity);
                }

                ImageAdapter adapter = new ImageAdapter(BagActivity.this, imageUrls, itemRarities);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        getItemInfo(position);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getItemInfo(int position) {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items");
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot itemSnapshot = null;
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (count == position) {
                        itemSnapshot = snapshot;
                        break;
                    }
                    count++;
                }
                if (itemSnapshot != null) {
                    String itemName = itemSnapshot.child("name").getValue(String.class);
                    String itemImageUrl = itemSnapshot.child("imageUrl").getValue(String.class);
                    String itemClass = itemSnapshot.child("itemClass").getValue(String.class);
                    String itemRarity = itemSnapshot.child("rarity").getValue(String.class);
                    String itemProperties = itemSnapshot.child("properties").getValue(String.class);
                    String itemDescription = itemSnapshot.child("description").getValue(String.class);

                    Intent intent = new Intent(BagActivity.this, ItemActivity.class);
                    intent.putExtra("itemName", itemName);
                    intent.putExtra("itemImageUrl", itemImageUrl);
                    intent.putExtra("itemClass", itemClass);
                    intent.putExtra("itemRarity", itemRarity);
                    intent.putExtra("itemProperties", itemProperties);
                    intent.putExtra("itemDescription", itemDescription);
                    intent.putExtra("itemKey", itemSnapshot.getKey());
                    startActivityForResult(intent, REQUEST_CODE_DELETE_ITEM);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DELETE_ITEM) {
            if (resultCode == RESULT_OK) {
                GetBag(); // Обновляем список предметов после удаления
            }
        }
    }
}
