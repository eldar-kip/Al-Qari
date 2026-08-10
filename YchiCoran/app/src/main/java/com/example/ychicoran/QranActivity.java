package com.example.ychicoran;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ychicoran.dopclasses.BaseActivity;
import com.example.ychicoran.dopclasses.NavigationProject;

public class QranActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qran);

            // 1. Находим контейнер
            LinearLayout suraContainer = findViewById(R.id.sura_list);
            LayoutInflater inflater = LayoutInflater.from(this);

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

                View suraView = inflater.inflate(R.layout.item_sura, suraContainer, false);
                // инициализируем элементы карточки суры
                TextView name = suraView.findViewById(R.id.name_sura);
                TextView translation = suraView.findViewById(R.id.translation_sura);
                TextView number = suraView.findViewById(R.id.number_sura);
                ImageView playBtn = suraView.findViewById(R.id.play_pause_item);

                //присваевает значения элементам
                name.setText(arabicNames[i]);
                translation.setText(suras[i]);
                number.setText("Сура " + (i + 1));

                // Обработка нажатия на кнопку плеер этой конкретной суры
                int finalI = i;
                playBtn.setOnClickListener(v -> {
                    // Тут будет логика запуска аудио для суры № finalI
                });
                suraView.setOnClickListener(v -> {
                    // Создаем намерение (Intent) для перехода в SuraDetals
                    android.content.Intent intent = new android.content.Intent(QranActivity.this, SuraDetals.class);

                    // Передаем данные на следующий экран (индекс и название суры)
                    intent.putExtra("SURA_INDEX", finalI);
                    intent.putExtra("SURA_NAME", suras[finalI]);

                    // Запускаем активность
                    startActivity(intent);
                });
                suraContainer.addView(suraView);
            }
        //навигационное меню



    }

    }
