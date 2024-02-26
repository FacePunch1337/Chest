package com.example.testapp;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatGPT {
    private TextView textView;
    private Context context;
    private String stringURLEndPoint = "https://api.openai.com/v1/chat/completions";
    private String stringAPIKey = "";
    public String stringOutput = "";
    public String randomItem;
    public boolean isRequestInProgress = false;
    public boolean chestEmpty;
    public String[] itemClass =  {"Оружие", "Доспехи", "Артефакт", "Зелье", "Cвиток"};
    public String[] rarities = {"Обычный", "Необычный", "Редкий", "Эпический", "Легендарный", "Мифический", "Реликвия", "Божественный", "За гарнью понимания"};

    public ChatGPT(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }



    public void generateItem() {
        if (isRequestInProgress) {
            return;
        }

        isRequestInProgress = true;

        JSONObject jsonObject = new JSONObject();
        Random random = new Random();
        int randomItemIndex = random.nextInt(itemClass.length);
        int randomRarityIndex = random.nextInt(rarities.length);
        randomItem = itemClass[randomItemIndex];
        String randomRarity = rarities[randomRarityIndex];

        try {
            jsonObject.put("model", "gpt-3.5-turbo");

            JSONArray jsonArrayMessage = new JSONArray();
            JSONObject jsonObjectMessage = new JSONObject();
            jsonObjectMessage.put("role", "user");
            jsonObjectMessage.put("content", "сгенерируй случайный игровой предмет полученный из сундука строго по шаблону. \n" +
                    "Предмет и его характеристики должны зависеть от уровня редкости\n" +
                    "Шаблон:\n" +
                    "Название предмета: \n" +
                    "Класс предмета: " + randomItem + "\n" +
                    "Уровень редкости: " + randomRarity + "\n" +
                    "\n" +
                    "Характеристики: \n" +
                    "\n" +
                    "Описание: \n");
            jsonArrayMessage.put(jsonObjectMessage);

            jsonObject.put("messages", jsonArrayMessage);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                stringURLEndPoint, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                isRequestInProgress = false;

                String stringText = null;

                try {
                    stringText = response.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");
                } catch (JSONException e) {

                    throw new RuntimeException(e);

                }

                stringOutput = stringText;
                Log.d("ChatGPT", "Generated item: " + stringOutput);
                if (callback != null) {
                    callback.onItemGenerated(stringOutput);
                }
                chestEmpty = false;

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                isRequestInProgress = false;
                if (error.networkResponse != null && error.networkResponse.statusCode == 429) {
                    chestEmpty = true; // Установка chestEmpty в true при ошибке 429
                    Log.e("Chest", "Empty");
                } else {
                    Log.e("ChatGPT", "Error: " + error.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mapHeader = new HashMap<>();
                mapHeader.put("Authorization", "Bearer " + stringAPIKey);
                mapHeader.put("Content-Type", "application/json");

                return mapHeader;
            }
        };

        int intTimeoutPeriod = 10000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(intTimeoutPeriod,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    private ChatGPTCallback callback;

    public void setCallback(ChatGPTCallback callback) {
        this.callback = callback;
    }

    public void fakeGenerate() {
        stringOutput = "Название предмета: Клинок Бездны\n" +
                "Класс предмета: Оружие\n" +
                "Уровень редкости: Реликвия\n" +
                "                                                                                                    \n" +
                "Характеристики:\n" +
                "- Урон: +100\n" +
                "- Скорость атаки: +20%\n" +
                "- Шанс критического удара: +15%\n" +
                "- Восстановление здоровья при убийстве противника: +10%\n" +
                "                                                                                                    \n" +
                "Описание:\n" +
                "Клинок Бездны - оружие, покрытое силой магии и древних реликвий. Его острый клинок способен проникнуть сквозь самую прочную броню, нанося удары сокрушительной силы. Кроме того, благодаря особым свойствам клинка, он способен восстановить здоровье своего обладателя при каждом убийстве противника. Будучи реликвией, Клинок Бездны считается одним из самых редких и мощных оружий во вселенной игры.";
    }

    public interface ChatGPTCallback {
        void onItemGenerated(String stringOutput);

    }



}
