package com.example.ychicoran.dopclasses;

import android.content.Context;

import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Madhab;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import com.example.ychicoran.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeNamaz {

    // Класс для хранения данных для UI
    public static class PrayerResult {
        public String currentPrayerName;
        public String nextPrayerName;
        public String nextPrayerTime;
        public String timeRemaining;
    }

    public PrayerResult getPrayerInfo(Context context, double latitude, double longitude) {
        Coordinates coordinates = new Coordinates(latitude, longitude);
        DateComponents dateComponents = DateComponents.from(new Date());

        CalculationParameters parameters = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
        parameters.madhab = Madhab.HANAFI;
        PrayerTimes prayerTimes = new PrayerTimes(coordinates, dateComponents, parameters);
        TimeZone timeZone = TimeZone.getDefault();

        List<Date> data_list = new ArrayList<>();
        data_list.add(prayerTimes.fajr);
        data_list.add(prayerTimes.sunrise);
        data_list.add(prayerTimes.dhuhr);
        data_list.add(prayerTimes.asr);
        data_list.add(prayerTimes.maghrib);
        data_list.add(prayerTimes.isha);

        List<String> name_namaz = new ArrayList<>();
        name_namaz.add(context.getString(R.string.fajr));
        name_namaz.add(context.getString(R.string.shuruq));
        name_namaz.add(context.getString(R.string.dhuhr));
        name_namaz.add(context.getString(R.string.asr));
        name_namaz.add(context.getString(R.string.Maghrib));
        name_namaz.add(context.getString(R.string.Isha));

        Date now = new Date();
        int index_next = -1;

        for (int i = 0; i < data_list.size(); i++) {
            if (now.before(data_list.get(i))) {
                index_next = i;
                break;
            }
        }

        PrayerResult result = new PrayerResult();
        int index_current;

        if (index_next == -1) {
            index_next = 0; // Следующий - Фаджр (завтра)
            index_current = 5;
            result.timeRemaining = "--:--"; // Для простоты, расчет на завтра можно добавить позже
        } else {
            index_current = (index_next == 0) ? 5 : index_next - 1;
            
            // Расчет оставшегося времени
            long diffInMs = data_list.get(index_next).getTime() - now.getTime();
            long hours = TimeUnit.MILLISECONDS.toHours(diffInMs);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMs) % 60;
            result.timeRemaining = String.format(Locale.getDefault(), "%d:%02d", hours, minutes);
        }

        result.currentPrayerName = name_namaz.get(index_current);
        result.nextPrayerName = name_namaz.get(index_next);
        result.nextPrayerTime = formatJavaDate(data_list.get(index_next), timeZone);

        return result;
    }

    private String formatJavaDate(Date date, TimeZone timeZone) {
        if (date == null) return "--:--";
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        formatter.setTimeZone(timeZone);
        return formatter.format(date);
    }
}
