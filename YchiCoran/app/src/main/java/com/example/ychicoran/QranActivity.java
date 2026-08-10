package com.example.ychicoran;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QranActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qran);

            // 1. Находим контейнер
            LinearLayout suraContainer = findViewById(R.id.sura_list);
            LayoutInflater inflater = LayoutInflater.from(this);

            // Данные (первые 20 сур)
            String[] suras = {
                "Аль-Фатиха", "Аль-Бакара", "Аль-Имран", "Ан-Ниса", "Аль-Маида",
                "Аль-Анам", "Аль-Араф", "Аль-Анфаль", "Ат-Тауба", "Юнус",
                "Худ", "Юсуф", "Ар-Рад", "Ибрахим", "Аль-Хиджр",
                "Ан-Нахль", "Аль-Исра", "Аль-Кахф", "Марьям", "Та Ха"
            };
            String[] arabicNames = {
                "الفاتحة", "البقرة", "آل عمران", "النساء", "المائدة",
                "الأنعام", "الأعراف", "الأنفال", "التوبة", "يونس",
                "هود", "يوسف", "الرعد", "إبراهيم", "الحجر",
                "النحل", "الإسراء", "الكهف", "مريم", "طه"
            };

            // 2. Цикл создания блоков
            for (int i = 0; i < suras.length; i++) {
                // Создаем View из шаблона
                View suraView = inflater.inflate(R.layout.item_sura, suraContainer, false);

                // Находим элементы внутри карточки
                TextView name = suraView.findViewById(R.id.name_sura);
                TextView translation = suraView.findViewById(R.id.translation_sura);
                TextView number = suraView.findViewById(R.id.number_sura);
                ImageView playBtn = suraView.findViewById(R.id.play_pause_item);

                // Устанавливаем данные
                name.setText(arabicNames[i]);
                translation.setText(suras[i]);
                number.setText("Сура " + (i + 1));

                // Обработка нажатия на кнопку плеер этой конкретной суры
                int finalI = i;
                playBtn.setOnClickListener(v -> {
                    // Тут будет логика запуска аудио для суры № finalI
                });

                // 3. Добавляем карточку в общий список
                suraContainer.addView(suraView);
            }

        LinearLayout menuHome = findViewById(R.id.menu_home);
        LinearLayout menuUser = findViewById(R.id.menu_user);
        LinearLayout menuPlayList = findViewById(R.id.menu_pley_list);
        menuHome.setOnClickListener(v -> {
            Intent intent = new Intent(QranActivity.this, HomeActivity.class);
            startActivity(intent);
            onStop();
        });
        menuUser.setOnClickListener(v -> {
            Intent intent = new Intent(QranActivity.this, UserActivity.class);
            startActivity(intent);
            onStop();
        });
        menuPlayList.setOnClickListener(v -> {
            Intent intent = new Intent(QranActivity.this, PlaylistActivity.class);
            startActivity(intent);
            onStop();
        });


    }
    @Override
    public void onStop() {
        super.onStop();
        finish();
    }
    }
