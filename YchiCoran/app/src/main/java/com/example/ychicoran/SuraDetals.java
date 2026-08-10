package com.example.ychicoran;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SuraDetals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sura_detals);

        android.content.Intent intent = getIntent();

        // Получаем индекс суры (по умолчанию 0, если ничего не передали)
        int suraIndex = intent.getIntExtra("SURA_INDEX", 0);

        // Получаем название суры (например, чтобы вывести его в заголовке)
        String suraName = intent.getStringExtra("SURA_NAME");

        // Здесь вы можете использовать конструкцию if-else или switch-case
        // чтобы загрузить нужный массив в зависимости от выбранной суры

        if (suraIndex == 0) {
            // Загружаем массив для "Аль-Фатиха" (ваш текущий код)
        } else if (suraIndex == 1) {
            // Загружаем массив для "Аль-Бакара"
        }
        LinearLayout ayahContainer = findViewById(R.id.ayah_list);
        LayoutInflater inflater = LayoutInflater.from(this);

        // Полный двумерный массив суры Аль-Фатиха (7 аятов)
        String[][] fatihaData = {
                {
                        "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ",
                        "Бисмилляхир-Рахманир-Рахим",
                        "Во имя Аллаха, Милостивого, Милосердного!",
                        "1:1"
                },
                {
                        "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                        "Альхамду лилляхи Раббиль-алямин",
                        "Хвала Аллаху, Господу миров,",
                        "1:2"
                },
                {
                        "الرَّحْمَنِ الرَّحِيمِ",
                        "Ар-Рахманир-Рахим",
                        "Милостивому, Милосердному,",
                        "1:3"
                },
                {
                        "مَالِكِ يَوْمِ الدِّينِ",
                        "Малики яумид-дин",
                        "Властелину Дня воздаяния!",
                        "1:4"
                },
                {
                        "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
                        "Ийяка набуду ва ийяка настаин",
                        "Тебе одному мы поклоняемся и Тебя одного молим о помощи.",
                        "1:5"
                },
                {
                        "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                        "Ихдинас-сыраталь-мустаким",
                        "Веди нас прямым путем,",
                        "1:6"
                },
                {
                        "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
                        "Сыратал-лязина ан’амта алейхим, гайриль-магдуби алейхим ва ляд-даллин",
                        "путем тех, кого Ты облагодетельствовал, не тех, на кого пал гнев, и не заблудших.",
                        "1:7"
                }
        };

        // В цикле достаем данные по индексам
        for (int i = 0; i < fatihaData.length; i++) {
            View ayahView = inflater.inflate(R.layout.ayah, ayahContainer, false);

            TextView ayahArabText = ayahView.findViewById(R.id.ayah_arab_text);
            TextView ayahTranscriptionText = ayahView.findViewById(R.id.ayah_transcription_text);
            TextView ayahTranslateText = ayahView.findViewById(R.id.ayah_translate_text);
            TextView ayahNumber = ayahView.findViewById(R.id.number_ayah);


            ayahArabText.setText(fatihaData[i][0]);
            ayahTranscriptionText.setText(fatihaData[i][1]);
            ayahTranslateText.setText(fatihaData[i][2]);
            ayahNumber.setText(fatihaData[i][3]);

            ayahContainer.addView(ayahView);
        }

    }
}