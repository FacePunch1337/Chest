package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements ChatGPT.ChatGPTCallback {
    public static final String TAG = "MainActivity";


    private String itemName;
    private String itemImageURL;
    private String itemClass;
    private String itemRarity;
    private String itemDescription;
    private String itemProps;
    private ImageView imageView;
    private ImageView bagImageView;
    private ImageView profileImageButton;
    private ImageView upperImageView;
    private TextView itemNameTextView;
    private TextView itemClassTextView;
    private TextView rarityTextView;
    private TextView itemPropsTextView;
    private TextView descriptionTextView;

    private Button closeButton; // Добавили кнопку "Close"
    private Button takeButton;
    private TextView generatedTextView;

    private boolean isClickable = true;
    private boolean isFirstClick = true;
    private Random random = new Random();

    private boolean isAnimating = false;

    private ChatGPT chatGPT;
    private  BagActivity bag;
    private OnNewItemAddedListener newItemListener;
    private String[] weaponImageUrls = {
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/3/3f/Crystalys_icon.png/revision/latest/scale-to-width-down/60?cb=20160530165218",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/8/8d/Armlet_of_Mordiggian_%28Inactive%29_icon.png/revision/latest/scale-to-width-down/60?cb=20160530163650",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/9/9d/Skull_Basher_icon.png/revision/latest/scale-to-width-down/60?cb=20160530173357",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/0/0f/Shadow_Blade_icon.png/revision/latest/scale-to-width-down/60?cb=20160530173313",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/5/54/Meteor_Hammer_icon.png/revision/latest/scale-to-width-down/60?cb=20171030224231",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/c/c1/Battle_Fury_icon.png/revision/latest/scale-to-width-down/60?cb=20160530163833",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/b/bc/Nullifier_icon.png/revision/latest/scale-to-width-down/60?cb=20171101183614",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/d/df/Radiance_%28Active%29_icon.png/revision/latest/scale-to-width-down/60?cb=20160530172548",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/b/b0/Monkey_King_Bar_icon.png/revision/latest/scale-to-width-down/60?cb=20160530171521",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/3/37/Revenant%27s_Brooch_icon.png/revision/latest/scale-to-width-down/60?cb=20220227041411",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/6/67/Khanda_icon.png/revision/latest/scale-to-width-down/60?cb=20231215080815",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/2/24/Daedalus_icon.png/revision/latest/scale-to-width-down/60?cb=20160530165249",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/5/5b/Ethereal_Blade_icon.png/revision/latest/scale-to-width-down/60?cb=20160530170242",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/2/28/Butterfly_icon.png/revision/latest/scale-to-width-down/60?cb=20160530164923",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/9/91/Silver_Edge_icon.png/revision/latest/scale-to-width-down/60?cb=20160530173343",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/b/b1/Divine_Rapier_icon.png/revision/latest/scale-to-width-down/60?cb=20160530165837",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/6/65/Disperser_icon.png/revision/latest/scale-to-width-down/60?cb=20230420234355",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/3/3b/Abyssal_Blade_icon.png/revision/latest/scale-to-width-down/60?cb=20160530163207",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/6/68/Witch_Blade_icon.png/revision/latest?cb=20201219004719"
    };

    private String[] armorImageUrls = {
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/c/ce/Glimmer_Cape_icon.png/revision/latest/scale-to-width-down/60?cb=20160530170610",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/f/f2/Veil_of_Discord_icon.png/revision/latest/scale-to-width-down/60?cb=20160530173959",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/a/ab/Vanguard_icon.png/revision/latest/scale-to-width-down/60?cb=20160530173946",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/1/18/Blade_Mail_icon.png/revision/latest/scale-to-width-down/60?cb=20160530163957",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/2/2b/Aeon_Disk_icon.png/revision/latest/scale-to-width-down/60?cb=20171101184000",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/2/2f/Soul_Booster_icon.png/revision/latest/scale-to-width-down/60?cb=20160530173535",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/c/c5/Eternal_Shroud_icon.png/revision/latest/scale-to-width-down/60?cb=20201218232622",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/7/70/Crimson_Guard_icon.png/revision/latest/scale-to-width-down/60?cb=20160530165153",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/c/c8/Lotus_Orb_icon.png/revision/latest/scale-to-width-down/60?cb=20160530171208",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/7/72/Black_King_Bar_icon.png/revision/latest/scale-to-width-down/60?cb=20160530163925",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/5/5a/Bloodstone_icon.png/revision/latest/scale-to-width-down/60?cb=20160530164258",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/1/13/Hurricane_Pike_icon.png/revision/latest/scale-to-width-down/60?cb=20160530170931",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/8/84/Manta_Style_icon.png/revision/latest/scale-to-width-down/60?cb=20160530171300",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/2/23/Linken%27s_Sphere_icon.png/revision/latest/scale-to-width-down/60?cb=20160530171144",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/b/b6/Shiva%27s_Guard_icon.png/revision/latest/scale-to-width-down/60?cb=20160530173326",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/d/d3/Assault_Cuirass_icon.png/revision/latest/scale-to-width-down/60?cb=20160530163712",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/7/75/Heart_of_Tarrasque_icon.png/revision/latest/scale-to-width-down/60?cb=20160530170827"

    };

    private String[] artefactsImageUrls = {


            "https://static.wikia.nocookie.net/dota2_gamepedia/images/a/a2/Force_Staff_icon.png/revision/latest/scale-to-width-down/60?cb=20160530170519",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/6/66/Rod_of_Atos_icon.png/revision/latest/scale-to-width-down/60?cb=20160530172945",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/d/d4/Aether_Lens_icon.png/revision/latest/scale-to-width-down/60?cb=20200307133528",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/8/80/Eul%27s_Scepter_of_Divinity_icon.png/revision/latest/scale-to-width-down/60?cb=20160530170259",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/3/36/Solar_Crest_icon.png/revision/latest/scale-to-width-down/60?cb=20160530173521",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/b/bc/Dagon_1_icon.png/revision/latest/scale-to-width-down/60?cb=20160530165316",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/a/ad/Orchid_Malevolence_icon.png/revision/latest/scale-to-width-down/60?cb=20160530172207",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/0/07/Aghanim%27s_Scepter_icon.png/revision/latest/scale-to-width-down/60?cb=20160530163350",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/1/13/Octarine_Core_icon.png/revision/latest/scale-to-width-down/60?cb=20160530171918",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/e/e2/Refresher_Orb_icon.png/revision/latest/scale-to-width-down/60?cb=20160530172642",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/5/54/Scythe_of_Vyse_icon.png/revision/latest/scale-to-width-down/60?cb=20160530173214",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/5/5d/Gleipnir_icon.png/revision/latest/scale-to-width-down/60?cb=20201222194556",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/2/24/Aghanim%27s_Blessing_icon.png/revision/latest/scale-to-width-down/60?cb=20190729085545",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/9/90/Wind_Waker_icon.png/revision/latest/scale-to-width-down/60?cb=20201219004456",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/d/d8/Aghanim%27s_Shard_icon.png/revision/latest/scale-to-width-down/60?cb=20201218232020"

    };

    private String[] potionsImageUrls = {
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/0/03/Blood_Grenade_icon.png/revision/latest/scale-to-width-down/60?cb=20230421051419",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/7/77/Clarity_icon.png/revision/latest/scale-to-width-down/60?cb=20160530165109",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/0/04/Smoke_of_Deceit_icon.png/revision/latest/scale-to-width-down/60?cb=20160530173506",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/e/ed/Dust_of_Appearance_icon.png/revision/latest/scale-to-width-down/60?cb=20160530170133",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/f/fd/Tango_icon.png/revision/latest/scale-to-width-down/60?cb=20160530173658",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/3/36/Healing_Salve_icon.png/revision/latest/scale-to-width-down/60?cb=20160530170815",
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/f/fa/Bottle_%28Full%29_icon.png/revision/latest/scale-to-width-down/60?cb=20160530164631",


    };

    private String[] scrollsImageUrls = {
            "https://static.wikia.nocookie.net/dota2_gamepedia/images/4/46/Town_Portal_Scroll_icon.png/revision/latest?cb=20160530173822"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        chatGPT = new ChatGPT(this, generatedTextView);
        chatGPT.setCallback(this);
        profileImageButton = findViewById(R.id.profileImageButton);
        bagImageView = findViewById((R.id.bagImageView));
        BagActivity bagActivity = new BagActivity();
        imageView = findViewById(R.id.imageView);

        generatedTextView = findViewById(R.id.generatedTextView);
        upperImageView = findViewById(R.id.upperImageView);
        itemNameTextView = findViewById(R.id.itemNameTextView);
        itemClassTextView = findViewById(R.id.itemClassTextView);
        rarityTextView = findViewById(R.id.rarityTextView);
        itemPropsTextView = findViewById(R.id.itemPropsTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        takeButton = findViewById(R.id.takeButton);
        closeButton = findViewById(R.id.closeButton);

        // Настройка параметров макета для Сундука
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(400, 400);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);



        // Настройка параметров макета для ScrollView
        RelativeLayout.LayoutParams scrollViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        scrollViewParams.addRule(RelativeLayout.BELOW, R.id.imageView);
        scrollViewParams.addRule(RelativeLayout.ABOVE, R.id.closeButton);

        ScrollView scrollView = findViewById(R.id.scrollView);
        imageView.setLayoutParams(layoutParams);
        generatedTextView.setVisibility(View.VISIBLE);


        scrollView.setLayoutParams(scrollViewParams);
        profileImageButton.setOnClickListener(new View.OnClickListener() {
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
                        // Окончание анимации
                        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(GameActivity.this);
                        if (account != null) {
                            openProfileActivity(GameActivity.this, account);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // Повторение анимации
                    }
                });

                profileImageButton.startAnimation(animScaleDown);
            }
        });
        bagImageView.setOnClickListener(new View.OnClickListener() {
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
                        // Окончание анимации
                        Intent intent = new Intent(GameActivity.this, BagActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // Повторение анимации
                    }
                });

                bagImageView.startAnimation(animScaleDown);
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickable && !isAnimating) {
                    isClickable = false;
                    isAnimating = true;

                    if (isFirstClick) {
                        isFirstClick = false;
                        chatGPT.generateItem();
                        Animation animScaleUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
                        imageView.startAnimation(animScaleUp);
                        bagImageView.setVisibility(View.INVISIBLE);
                        profileImageButton.setVisibility(View.INVISIBLE);
                    }

                }
            }
        });


        takeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeItem();
            }

            private void takeItem() {

                setNewItem(itemName, itemImageURL, itemClass, itemRarity, itemProps, itemDescription);
                restartGame();
            }

        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame(); // Метод для закрытия сундука и перезапуска игры
            }
        });


    }

    private void openProfileActivity(GameActivity gameActivity, GoogleSignInAccount account) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userName", account.getDisplayName());
        if (account.getPhotoUrl() != null) {
            intent.putExtra("userAvatar", account.getPhotoUrl().toString());
        }
        startActivity(intent);
    }




    private void restartGame() {
        isAnimating = false;
        isClickable = true;
        isFirstClick = true;
        closeButton.setVisibility(View.GONE);
        takeButton.setVisibility(View.GONE);
        bagImageView.setVisibility(View.VISIBLE);
        profileImageButton.setVisibility(View.VISIBLE);
        imageView.setClickable(true);
        generatedTextView .setText(null);
        imageView.setImageResource(R.drawable.chest);
        upperImageView.setImageDrawable(null);
        itemNameTextView.setText(null);
        itemClassTextView.setText(null);
        rarityTextView.setText(null);
        itemPropsTextView.setText(null);
        descriptionTextView.setText(null);
    }

    private JSONObject parseAndFormatResponse(String responseText) {
        JSONObject itemObject = new JSONObject();
        try {
            String[] lines = responseText.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (line.startsWith("Название предмета:")) {
                    itemName = itemObject.put("name", line.substring(line.indexOf(":") + 1).trim()).toString();

                } else if (line.startsWith("Класс предмета:")) {
                    itemClass = itemObject.put("class", line.substring(line.indexOf(":") + 1).trim()).toString();
                } else if (line.startsWith("Уровень редкости:")) {
                    itemRarity = itemObject.put("rarity", line.substring(line.indexOf(":") + 1).trim()).toString();
                } else if (line.startsWith("Описание:")) {
                    itemDescription = itemObject.put("description", line.substring(line.indexOf(":") + 1).trim()).toString();
                } else if (line.startsWith("Характеристики:")) {
                    JSONArray propertiesArray = new JSONArray();
                    while (true) {
                        line = lines[++i];
                        if (line.trim().isEmpty()) break;
                        propertiesArray.put(line.trim());
                    }
                    itemProps = itemObject.put("properties", propertiesArray).toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemObject;
    }


    private void displayItemDetails(JSONObject itemObject) {
        try {
            // Отображение основной информации о предмете
            itemNameTextView.setText(itemObject.optString("name", ""));
            itemClassTextView.setText(itemObject.optString("class", ""));

            // Получаем уровень редкости предмета
            String rarity = itemObject.optString("rarity", "");
            rarityTextView.setText(rarity);

            // Устанавливаем цвет редкости в зависимости от её значения
            int colorResId;
            switch (rarity) {
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
            rarityTextView.setTextColor(getResources().getColor(colorResId));

            descriptionTextView.setText(itemObject.optString("description", ""));

            // Отображение характеристик предмета
            JSONArray propertiesArray = itemObject.optJSONArray("properties");
            if (propertiesArray != null && propertiesArray.length() > 0) {
                StringBuilder propertiesBuilder = new StringBuilder();
                boolean firstProperty = true;
                for (int i = 0; i < propertiesArray.length(); i++) {
                    String property = propertiesArray.optString(i, "");
                    if (!firstProperty) {
                        propertiesBuilder.append("\n"); // Добавляем перенос строки между характеристиками
                    } else {
                        firstProperty = false;
                    }
                    propertiesBuilder.append(property); // Добавляем текущую характеристику
                }
                itemPropsTextView.setText(propertiesBuilder.toString());

            } else {
                itemPropsTextView.setText("Характеристики отсутствуют");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void setNewItem(String name,
                            String imageURL,
                            String itemClass,
                            String rarity,
                            String properties,
                            String description
    )
    {
        Item item = new Item();
        BagActivity myBag = new BagActivity();
        item.setName(name);
        item.setImageUrl(imageURL);
        item.setItemClass(itemClass);
        item.setRarity(rarity);

        item.setProperties(properties);
        item.setDescription(description);
        item.showItem();

        // Получение ссылки на Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference itemsRef = database.getReference("items");

        // Сохранение объекта Item в Firebase
        String itemId = itemsRef.push().getKey();
        itemsRef.child(itemId).setValue(item);

        if (newItemListener != null) {
            newItemListener.onNewItemAdded(imageURL);
        }

    }




    @Override
    public void onItemGenerated(String stringOutput) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!chatGPT.isRequestInProgress) {
                    imageView.setImageResource(R.drawable.open_chest);
                    generatedTextView.setVisibility(View.VISIBLE);
                    Animation animScaleDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down);
                    imageView.startAnimation(animScaleDown);
                    closeButton.setVisibility(View.VISIBLE);
                    takeButton.setVisibility(View.VISIBLE);
                    bagImageView.setVisibility(View.INVISIBLE);
                    profileImageButton.setVisibility(View.INVISIBLE);
                    imageView.setClickable(false);

                    String randomImageUrl;
                    switch (chatGPT.randomItem) {
                        case "Оружие":
                            randomImageUrl = weaponImageUrls[random.nextInt(weaponImageUrls.length)];
                            break;
                        case "Доспехи":
                            randomImageUrl = armorImageUrls[random.nextInt(armorImageUrls.length)];
                            break;
                        case "Артефакт":
                            randomImageUrl = artefactsImageUrls[random.nextInt(artefactsImageUrls.length)];
                            break;
                        case "Зелье":
                            randomImageUrl = potionsImageUrls[random.nextInt(potionsImageUrls.length)];
                            break;
                        case "Свиток":
                            randomImageUrl = potionsImageUrls[random.nextInt(potionsImageUrls.length)];
                            break;
                        default:
                            randomImageUrl = "";
                    }

                    Picasso.get().load(randomImageUrl).into(upperImageView);
                    itemImageURL = randomImageUrl;

                    JSONObject itemObject = parseAndFormatResponse(stringOutput);
                    if (itemObject.length() > 0) {
                        itemName = itemObject.optString("name", "");
                        itemClass = itemObject.optString("class", "");
                        itemRarity = itemObject.optString("rarity", "");
                        itemDescription = itemObject.optString("description", "");

                        JSONArray propertiesArray = itemObject.optJSONArray("properties");
                        if (propertiesArray != null && propertiesArray.length() > 0) {
                            StringBuilder propertiesBuilder = new StringBuilder();
                            for (int i = 0; i < propertiesArray.length(); i++) {
                                String property = propertiesArray.optString(i, "");
                                if (i > 0) {
                                    propertiesBuilder.append("\n");
                                }
                                propertiesBuilder.append(property);
                            }
                            itemProps = propertiesBuilder.toString();
                        } else {
                            itemProps = "Характеристики отсутствуют";
                        }

                        displayItemDetails(itemObject);

                    } else {
                        Log.d(TAG, "Chest is empty or item data is missing.");

                    }
                }
            }
        });
    }



    public void setOnNewItemAddedListener(OnNewItemAddedListener listener) {
        this.newItemListener = listener;
    }


}
