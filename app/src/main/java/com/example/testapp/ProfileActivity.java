package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Получаем данные о пользователе из Intent
        String userName = getIntent().getStringExtra("userName");
        String userAvatarUrl = getIntent().getStringExtra("userAvatar");

        // Находим View для отображения имени пользователя и его аватарки
        TextView userNameTextView = findViewById(R.id.textViewUserName);
        ImageView userAvatarImageView = findViewById(R.id.imageViewAvatar);
        ImageView logOutImageView = findViewById(R.id.logOutImageView);
        Button closeButton = findViewById(R.id.closeButton);
        // Устанавливаем имя пользователя в TextView
        userNameTextView.setText(userName);

        // Загружаем и отображаем аватар пользователя с помощью Picasso
        if (userAvatarUrl != null && !userAvatarUrl.isEmpty()) {
            Picasso.get().load(userAvatarUrl).into(userAvatarImageView);
        }

        // Инициализация клиента GoogleSignIn
        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);


        // Обработчик нажатия на кнопку closeButton
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameActivity();
            }
        });

        // Обработчик нажатия на кнопку logOutImageView
        logOutImageView.setOnClickListener(new View.OnClickListener() {
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
                        signOut();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // Повторение анимации
                    }
                });

                logOutImageView.startAnimation(animScaleDown);
            }
        });
    }

    // Метод для выхода из учетной записи Google
    private void signOut() {
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Возврат к MainActivity после выхода из учетной записи
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {

            Toast.makeText(ProfileActivity.this, "Ошибка выхода из учетной записи: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void openGameActivity() {
        Intent intent = new Intent(ProfileActivity.this, GameActivity.class);
        startActivity(intent);
    }
}

